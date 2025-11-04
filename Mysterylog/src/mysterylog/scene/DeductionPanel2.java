package mysterylog.scene;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;
import mysterylog.model.SampleData;
import mysterylog.model.Suspect;

public class DeductionPanel2 extends JPanel implements Scene {

	private JTextArea textArea;
	private GameManager gm;

	public DeductionPanel2(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
		setBackground(Theme.PANEL_BG);

		// 타이틀 ==========================
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/deduction.png"));
		Image img = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel title = new JLabel(" 추리하기: 범인을 지목", new ImageIcon(img), SwingConstants.CENTER);
		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR);
		title.setForeground(Theme.BUTTON_TEXT_COLOR);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		// ============================================================
		// 중앙 내용 영역 (BoxLayout)
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(Theme.PANEL_BG);

		JLabel infoLabel = new JLabel("범인을 지목할 용의자를 선택하세요", SwingConstants.CENTER);
		infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		centerPanel.add(infoLabel);

		// 용의자 버튼 패널
		JPanel suspectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		suspectPanel.setBackground(Theme.PANEL_BG);

		for (Suspect s : SampleData.suspects) {
			JButton btn = Theme.createStyleButton(s.getName());
			btn.addActionListener(e -> accuse(s));
			suspectPanel.add(btn);
		}

		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		centerPanel.add(suspectPanel);

		// 🔹 결과 텍스트 영역
		textArea = new JTextArea(8, 40);
		textArea.setEditable(false);
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.PRIMARY_COLOR),
				"🧩 추리 결과 / 단서 요약", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
				new Font("맑은 고딕", Font.BOLD, 14), Theme.PRIMARY_COLOR));
		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		centerPanel.add(scrollPane);
		centerPanel.add(Box.createVerticalGlue());

		add(centerPanel, BorderLayout.CENTER);

		// ============================================================
		// 하단 버튼 패널
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		bottomPanel.setBackground(Theme.PANEL_BG);

		JButton newCaseBtn = Theme.createStyleButton("새 사건 시작");
		JButton endBtn = Theme.createStyleButton("사건 종료");
		JButton mainBtn = Theme.createStyleButton("메인으로");

		newCaseBtn.addActionListener(e -> {
			try {
				gm.startNewCase();
			} catch (GameException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		});

		endBtn.addActionListener(e -> gm.endCase());
		mainBtn.addActionListener(e -> gm.moveTo("MAIN"));

		bottomPanel.add(newCaseBtn);
		bottomPanel.add(endBtn);
		bottomPanel.add(mainBtn);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	// ============================================================

	// 용의자 선택 시 실행 함수
	private void accuse(Suspect s) {
		if (textArea == null)
			return;

		// 임시로 진범 이름 설정
		String culprit = "Yoon";
		String msg = s.getName().equals(culprit) ? "O 정답! 범인은 " + culprit + "입니다!" : "X 틀렸습니다. 진범은 아직 잡히지 않았습니다...";

		textArea.append(msg + "\n");

		try {
			LogManager.saveLog(msg);
			gm.getMainPanel().refreshLog();
		} catch (GameException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "로그 저장 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	// ============================================================
	@Override
	public void onEnter() {
		refreshClues();
	}

	@Override
	public void refreshClues() {
	    if (textArea == null) return;

	    StringBuilder summary = new StringBuilder("\n단서 요약:\n");
	    Set<Clue> displayed = new HashSet<>();

	    // 심문 단서 (INTERROGATION, discoveredBy != null)
	    for (Suspect s : SampleData.suspects) {
	        for (Clue c : s.getHiddenClues()) {
	            if (c != null && c.isDiscovered() && "INTERROGATION".equals(c.getType())
	                    && c.getDiscoveredBy() != null && !displayed.contains(c)) {
	                summary.append("• 심문 단서: ").append(c.getDiscoveredBy().getName())
	                       .append(": ").append(c.getName())
	                       .append(c.isFake() ? " ⚠️(의심)" : "")
	                       .append(" [" + c.getType() + "]")
	                       .append(" (신뢰도: ").append(c.getReliability()).append(")\n");
	                displayed.add(c);
	            }
	        }
	    }

	    // 현장조사 단서 (INVESTIGATION, discoveredBy == null)
	    for (Clue c : SampleData.clues) {
	        if (c.isDiscovered() && "INVESTIGATION".equals(c.getType()) 
	                && c.getDiscoveredBy() == null && !displayed.contains(c)) {
	            summary.append("• 현장 단서: ").append(c.getName())
	                   .append(" [" + c.getType() + "]")
	                   .append(" (신뢰도: ").append(c.getReliability()).append(")\n");
	            displayed.add(c);
	        }
	    }

	    // 파생 단서 (derived == true)
	    for (Clue c : SampleData.clues) {
	        if (c.isDerived() && c.isDiscovered() && !displayed.contains(c)) {
	            summary.append("• 파생 단서: ").append(c.getName())
	                   .append(" [" + c.getType() + "]")
	                   .append(" (신뢰도: ").append(c.getReliability()).append(")\n");
	            displayed.add(c);
	        }
	    }

	    textArea.setText(summary.toString());
	}

	@Override
	public void onExit() {
	}
}
