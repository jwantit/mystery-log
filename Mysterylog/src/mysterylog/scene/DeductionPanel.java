package mysterylog.scene;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;
import mysterylog.model.SampleData;
import mysterylog.model.Suspect;

public class DeductionPanel extends JPanel implements Scene {
	private JTextArea textArea;
	private GameManager gm;

	public DeductionPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// 제목
		JLabel title = new JLabel("🕵️ 추리하기: 범인을 지목", SwingConstants.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);

		add(Box.createRigidArea(new Dimension(0, 10)));

		// textArea 먼저 생성
		textArea = new JTextArea(10, 30);
		textArea.setEditable(false);
		
		// JScrollPane에 넣기
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

		// BoxLayout에서 크기 보장
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
		scrollPane.setMinimumSize(new Dimension(300, 100));

		add(scrollPane);

		add(Box.createRigidArea(new Dimension(0, 10)));

		// 용의자 버튼
		JPanel suspectPanel = new JPanel();
		for (Suspect s : SampleData.suspects) {
			JButton btn = new JButton(s.getName());
			btn.addActionListener(e -> {
				accuse(s);
				System.out.println("버튼 클릭됨: " + s.getName());
			});
			suspectPanel.add(btn);
		}
		suspectPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(suspectPanel);

		// 하단 버튼
		JPanel bottomPanel = new JPanel();
		JButton newCaseBtn = new JButton("새 사건 시작");
		JButton endBtn = new JButton("사건 종료");
		JButton mainBtn = new JButton("메인으로");
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
		add(bottomPanel);
	}

	private void accuse(Suspect s) {
		if (textArea == null)
			return;
		String culprit = "Yoon";
		String msg = s.getName().equals(culprit) ? "🎯 정답! 범인은 " + culprit + "입니다." : "진범은 아직 잡히지 않았다...";

		// DeductionPanel의 textArea에 출력
		textArea.append(msg + "\n");

		try {
	        LogManager.saveLog(msg); // GameException 가능
	        gm.getMainPanel().refreshLog();
	    } catch (GameException e) {
	        JOptionPane.showMessageDialog(this, e.getMessage(), "로그 저장 오류", JOptionPane.ERROR_MESSAGE);
	    }
	}

	@Override
	public void onEnter() {
		refreshClues(); // 화면 진입 시 단서 갱신
	}

	@Override
	public void refreshClues() {
		if (textArea == null)
			return;
		StringBuilder summary = new StringBuilder("단서 요약:\n");
		Set<Clue> discovered = new HashSet<>();
		for (Suspect s : SampleData.suspects) {
			for (Clue c : s.getHiddenClues()) {
				if (c != null && c.isDiscovered() && !discovered.contains(c)) {
					summary.append(s.getName()).append(": ").append(c.getName()).append("\n");
					discovered.add(c);
				}
			}
		}
		textArea.append(summary.toString());
		textArea.revalidate();
		textArea.repaint();
	}

	@Override
	public void onExit() {
	}
}
