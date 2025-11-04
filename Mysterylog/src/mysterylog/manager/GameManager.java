package mysterylog.manager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.model.Episode;
import mysterylog.model.Clue;
import mysterylog.model.Suspect;
import mysterylog.scene.*;

public class GameManager {

	private JFrame frame;
	private JPanel mainContainer;
	private CardLayout cardLayout;

	// 패널
	private MainPanel mainPanel;
	private InterrogationPanel interrogationPanel;
	private InvestigationPanel investigationPanel;
	private DeductionPanel deductionPanel;
	private EpisodeSelectionPanel episodeSelectionPanel;

	// 현재 에피소드 데이터
	private Episode currentEpisode;

	public GameManager() {
		initUI();
	}

	private void initUI() {
		frame = new JFrame("Mystery Log");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 700);
		frame.setLocationRelativeTo(null);

		cardLayout = new CardLayout();
		mainContainer = new JPanel(cardLayout);

		// 패널 생성
		mainPanel = new MainPanel(this);
		interrogationPanel = new InterrogationPanel(this);
		investigationPanel = new InvestigationPanel(this);
		deductionPanel = new DeductionPanel(this);
		episodeSelectionPanel = new EpisodeSelectionPanel(this);

		// 카드로 등록
		mainContainer.add(mainPanel, "MAIN");
		mainContainer.add(interrogationPanel, "INTERROGATION");
		mainContainer.add(investigationPanel, "INVESTIGATION");
		mainContainer.add(deductionPanel, "DEDUCTION");
		mainContainer.add(episodeSelectionPanel, "EPISODE_SELECTION");

		frame.add(mainContainer);
		frame.setVisible(true);

		// 처음 진입은 에피소드 선택 화면
		showEpisodeSelection();
	}

	// 카드 전환
	public void moveTo(String panelName) {
		cardLayout.show(mainContainer, panelName);

		// 현재 패널 onEnter 호출
		Component comp = null;
		for (Component c : mainContainer.getComponents()) {
			if (mainContainer.getLayout() instanceof CardLayout) {
				if (mainContainer.getComponentZOrder(c) != -1) {
					// 카드 이름 비교
					if (mainContainer.getComponentZOrder(c) == ((CardLayout) mainContainer.getLayout()).getHgap()) {
						comp = c;
					}
				}
			}
		}

		switch (panelName) {
		case "MAIN" -> mainPanel.onEnter();
		case "INTERROGATION" -> interrogationPanel.onEnter();
		case "INVESTIGATION" -> investigationPanel.onEnter();
		case "DEDUCTION" -> deductionPanel.onEnter();
		case "EPISODE_SELECTION" -> episodeSelectionPanel.onEnter();
		}
	}

	// 메인 패널 반환
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public Episode getCurrentEpisode() {
		return currentEpisode;
	}
	
	public void setCurrentEpisode(Episode episode) {
		this.currentEpisode = episode;
	}

	// 현재 에피소드에서 용의자 반환
	public List<Suspect> getCurrentSuspects() {
		if (currentEpisode != null) {
			return currentEpisode.getSuspects();
		}
		return List.of();
	}
	
	// 현재 에피소드에서 단서 반환
	public List<Clue> getCurrentClues() {
		if (currentEpisode != null) {
			return currentEpisode.getClues();
		}
		return List.of();
	}

	// 새 사건 시작
	public void startNewCase() throws GameException {
		if (currentEpisode == null) {
			throw new GameException("선택한 에피소드가 없습니다.");
		}

		// 모든 단서 초기화
		for (Clue c : getCurrentClues()) {
			c.setDiscovered(false);
		}

		// 모든 용의자 초기화
		for (Suspect s : getCurrentSuspects()) {
			s.reset();
		}

		// 로그 초기화
		try {
			LogManager.clearLogs();
		} catch (GameException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "로그 초기화 오류", JOptionPane.ERROR_MESSAGE);
		}

		// 알림
		JOptionPane.showMessageDialog(frame, "새 사건이 시작되었습니다.", "새 사건", JOptionPane.INFORMATION_MESSAGE);

		// 메인 화면으로 이동
		moveTo("MAIN");
	}

	// 에피소드 선택
	public void selectEpisode(Episode episode) {
		this.currentEpisode = episode;
		try {
			startNewCase();
		} catch (GameException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 에피소드 선택 화면 호출
	public void showEpisodeSelection() {
		moveTo("EPISODE_SELECTION");
	}

	// 단서 목록 화면 호출
	public void showClueList() {
	    // 단서 목록 팝업 생성
	    java.util.List<Clue> clues = getCurrentClues();
	    if (clues.isEmpty()) {
	        JOptionPane.showMessageDialog(
	            frame,
	            "현재 단서가 없습니다.",
	            "단서 목록",
	            JOptionPane.INFORMATION_MESSAGE
	        );
	        return;
	    }

	    JTextArea clueArea = new JTextArea(15, 40);
	    clueArea.setEditable(false);
	    StringBuilder sb = new StringBuilder();
	    for (Clue c : clues) {
	        sb.append("- ").append(c.getName())
	          .append(c.isDiscovered() ? " (발견됨)" : "")
	          .append("\n");
	    }
	    clueArea.setText(sb.toString());
	    clueArea.setCaretPosition(0);

	    JScrollPane scrollPane = new JScrollPane(clueArea);

	    JOptionPane.showMessageDialog(
	        frame,
	        scrollPane,
	        "단서 목록",
	        JOptionPane.INFORMATION_MESSAGE
	    );
	}
}
