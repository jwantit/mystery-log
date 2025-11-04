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
import mysterylog.model.Suspect;

public class DeductionPanel extends JPanel implements Scene {

	private JTextArea textArea;
	private GameManager gm;

	private Suspect selectedSuspect = null; // 선택된 용의자
	private JLabel selectedLabel; // 선택된 용의자 표시

	public DeductionPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
		setBackground(Theme.PANEL_BG);

		// 타이틀
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/deduction.png"));
		Image img = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel title = new JLabel(" 추리하기: 범인을 지목", new ImageIcon(img), SwingConstants.CENTER);
		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR);
		title.setForeground(Theme.BUTTON_TEXT_COLOR);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		// 중앙 패널
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(Theme.PANEL_BG);

		JLabel infoLabel = new JLabel("범인을 지목할 용의자를 선택하세요", SwingConstants.CENTER);
		infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		centerPanel.add(infoLabel);

		// 선택된 용의자 표시
		selectedLabel = new JLabel("선택된 용의자: 없음", SwingConstants.CENTER);
		selectedLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		selectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		centerPanel.add(selectedLabel);

		// 용의자 버튼 패널
		JPanel suspectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		suspectPanel.setBackground(Theme.PANEL_BG);
		suspectPanel.setName("suspectPanel");
		centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		centerPanel.add(suspectPanel);

		// 제출 버튼: 용의자 버튼 아래
		JButton submitBtn = Theme.createStyleButton("제출");
		submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		centerPanel.add(submitBtn);

		submitBtn.addActionListener(e -> {
			try {
				if (selectedSuspect == null) {
					throw new GameException("용의자를 선택해주세요!");
				}

				int confirm = JOptionPane.showConfirmDialog(this,
						"정말로 " + selectedSuspect.getName() + "을(를) 범인으로 지목하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					accuse(selectedSuspect);
					selectedSuspect = null;
					selectedLabel.setText("선택된 용의자: 없음");
				}

			} catch (GameException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "경고", JOptionPane.WARNING_MESSAGE);
			}
		});

		// 결과 텍스트 영역
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

		endBtn.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(null, "정말 사건을 종료하시겠습니까?", "사건 종료 확인",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				gm.setCurrentEpisode(null);
				gm.moveTo("EPISODE_SELECTION");
			}
		});

		mainBtn.addActionListener(e -> gm.moveTo("MAIN"));

		bottomPanel.add(newCaseBtn);
		bottomPanel.add(endBtn);
		bottomPanel.add(mainBtn);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	// ============================================================
	// 범인 지목
	private void accuse(Suspect s) {
		if (textArea == null)
			return;

		String culprit = gm.getCurrentEpisode().getCulprit();
		String msg = s.getName().equals(culprit) ? "\nO 정답! 범인은 " + culprit + "입니다!" : "\nX 틀렸습니다. 진범은 아직 잡히지 않았습니다...";

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
		if (textArea == null)
			return;

		textArea.setText("\n단서 요약:\n");
		Set<Clue> displayed = new HashSet<>();

		// 심문 단서
		for (Suspect s : gm.getCurrentSuspects()) {
			for (Clue c : s.getHiddenClues()) {
				if (c != null && c.isDiscovered() && "INTERROGATION".equals(c.getType()) && c.getDiscoveredBy() != null
						&& !displayed.contains(c)) {
					textArea.append("• 심문 단서: " + c.getDiscoveredBy().getName() + ": " + c.getName() + "\n"
							+ "       설명: " + c.getDescription() + (c.isFake() ? " (의심)" : "") + " (신뢰도: "
							+ c.getReliability() + ")\n");
					displayed.add(c);
				}
			}
		}

		// 현장조사 단서
		for (Clue c : gm.getCurrentClues()) {
			if (c.isDiscovered() && "INVESTIGATION".equals(c.getType()) && c.getDiscoveredBy() == null
					&& !displayed.contains(c)) {
				textArea.append("• 현장 단서: " + c.getName() + "\n" + "       설명: " + c.getDescription() + " (신뢰도: "
						+ c.getReliability() + ")\n");
				displayed.add(c);
			}
		}

		// 용의자 버튼 갱신
		JPanel centerPanel = (JPanel) getComponent(1);
		JPanel suspectPanel = null;
		for (Component c : centerPanel.getComponents()) {
			if (c instanceof JPanel p && "suspectPanel".equals(p.getName())) {
				suspectPanel = p;
				break;
			}
		}

		if (suspectPanel != null) {
			suspectPanel.removeAll();
			for (Suspect s : gm.getCurrentSuspects()) {
				JButton btn = Theme.createStyleButton(s.getName());
				btn.addActionListener(e -> {
					selectedSuspect = s;
					selectedLabel.setText("선택된 용의자: " + s.getName());
				});
				suspectPanel.add(btn);
			}
			suspectPanel.revalidate();
			suspectPanel.repaint();
		}
	}

	@Override
	public void onExit() {
	}
}
