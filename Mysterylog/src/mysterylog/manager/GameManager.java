package mysterylog.manager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import mysterylog.scene.*;
import mysterylog.exception.GameException;
import mysterylog.model.Clue;
import mysterylog.model.SampleData;

public class GameManager {

	private JFrame frame;
	private JPanel mainContainer;
	private CardLayout cardLayout;

	// 패널
	private MainPanel mainPanel;
	private InterrogationPanel2 interrogationPanel;
	private InvestigationPanel investigationPanel;
	private DeductionPanel2 deductionPanel;

	public GameManager() {
		frame = new JFrame("Mystery Log");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 600);

		cardLayout = new CardLayout();
		mainContainer = new JPanel(cardLayout);

		// 패널 생성
		mainPanel = new MainPanel(this);
		interrogationPanel = new InterrogationPanel2(this);
		investigationPanel = new InvestigationPanel(this);
		deductionPanel = new DeductionPanel2(this);

		// CardLayout에 패널 추가
		mainContainer.add(mainPanel, "MAIN");
		mainContainer.add(interrogationPanel, "INTERROGATION");
		mainContainer.add(investigationPanel, "INVESTIGATION");
		mainContainer.add(deductionPanel, "DEDUCTION");

		frame.add(mainContainer);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// 화면 전환
	public void moveTo(String panelName) {
		switch (panelName) {
		case "MAIN":
			mainPanel.onEnter();
			break;
		case "INTERROGATION":
			interrogationPanel.onEnter();
			break;
		case "INVESTIGATION":
			investigationPanel.onEnter();
			break;
		case "DEDUCTION":
			deductionPanel.onEnter();
			break;
		}
		cardLayout.show(mainContainer, panelName);
	}

	// 단서 목록 팝업
	public void showClueList() {
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (Clue c : SampleData.clues) {
			if (c.isDiscovered()) {
				sb.append("- ").append(c.getName()).append("\n");
				found = true;
			}
		}
		if (!found)
			sb.append("아직 발견한 단서가 없습니다.");
		JOptionPane.showMessageDialog(frame, sb.toString(), "발견 단서", JOptionPane.INFORMATION_MESSAGE);
	}

	// MainPanel 접근 (로그 표시용)
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public void startNewCase() throws GameException {
		// SampleData 초기화
		for (Clue c : SampleData.clues) {
			c.setDiscovered(false);
		}
		for (var s : SampleData.suspects) {
			s.reset();
		}

		// 로그 초기화
		try {
			LogManager.clearLogs();
		} catch (GameException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "로그 초기화 오류", JOptionPane.ERROR_MESSAGE);
		}

		JOptionPane.showMessageDialog(frame, "새 사건이 시작되었습니다.", "새 사건", JOptionPane.INFORMATION_MESSAGE);

		// 화면 초기화
		moveTo("MAIN");
	}

	public void endCase() {
		int confirm = JOptionPane.showConfirmDialog(frame, "정말 사건을 종료하시겠습니까?", "사건 종료", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				LogManager.saveLog("사건 종료"); // GameException 가능
			} catch (GameException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(frame, "사건이 종료되었습니다.", "종료", JOptionPane.INFORMATION_MESSAGE);
			moveTo("MAIN");
		}
	}
}
