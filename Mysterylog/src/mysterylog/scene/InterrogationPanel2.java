package mysterylog.scene;

import javax.swing.*;
import java.awt.*;

import mysterylog.config.Theme;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;
import mysterylog.model.SampleData;
import mysterylog.model.Suspect;

public class InterrogationPanel2 extends JPanel implements Scene {

	private GameManager gm;
	private JPanel contentPanel;
	private JTextArea statementArea;
	private Suspect currentSuspect;
	private JLabel suspectLabel;
	private JPanel questionBtnPanel;

	public InterrogationPanel2(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
		setBackground(Theme.PANEL_BG);

		// 상단 타이틀
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/interrogate.png"));
		Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel title = new JLabel(" 심문하기", new ImageIcon(img), SwingConstants.CENTER);
		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		// 내용 패널 (CardLayout)
		contentPanel = new JPanel(new CardLayout());
		contentPanel.setBackground(Theme.PANEL_BG);
		add(contentPanel, BorderLayout.CENTER);

		// 1. 용의자 목록 화면
		JPanel suspectListPanel = new JPanel();
		suspectListPanel.setLayout(new BoxLayout(suspectListPanel, BoxLayout.Y_AXIS));
		suspectListPanel.setBackground(Theme.PANEL_BG);
		suspectListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel infoLabel = new JLabel("심문할 용의자를 선택하세요", SwingConstants.CENTER);
		infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		suspectListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		suspectListPanel.add(infoLabel);
		suspectListPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// 용의자 버튼 세로로 추가
		for (Suspect s : SampleData.suspects) {
		    JButton btn = Theme.createStyleButton(s.getName());
		    btn.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
		    btn.setMaximumSize(new Dimension(200, 40));    // 버튼 크기 제한
		    btn.addActionListener(e -> showQuestionScreen(s));
		    suspectListPanel.add(btn);
		    suspectListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 버튼 간 간격
		}

		// 2. 질문 화면
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
		questionPanel.setBackground(Theme.PANEL_BG);

		suspectLabel = new JLabel("", SwingConstants.CENTER);
		suspectLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		suspectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		questionPanel.add(suspectLabel);

		// 질문 버튼 패널 (동적 생성 위해 빈 패널로 초기화)
		questionBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		questionBtnPanel.setBackground(Theme.PANEL_BG);
		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		questionPanel.add(questionBtnPanel);

		// 뒤로가기 버튼
		JButton backBtn = Theme.createStyleButton("← 용의자 목록으로");
		backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		backBtn.addActionListener(e -> showSuspectList());
		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		questionPanel.add(backBtn);

		// 답변 영역
		statementArea = new JTextArea(8, 40);
		statementArea.setEditable(false);
		statementArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		
		JScrollPane scrollPane = new JScrollPane(statementArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("답변"));
		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		questionPanel.add(scrollPane);
		questionPanel.add(Box.createVerticalGlue());

		// 카드 등록
		contentPanel.add(suspectListPanel, "suspects");
		contentPanel.add(questionPanel, "questions");

		showSuspectList();

		// 하단 메인 버튼
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		bottomPanel.setBackground(Theme.PANEL_BG);
		JButton mainBtn = Theme.createStyleButton("메인으로");
		mainBtn.addActionListener(e -> gm.moveTo("MAIN"));
		bottomPanel.add(mainBtn);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private void showSuspectList() {
		currentSuspect = null;
		((CardLayout) contentPanel.getLayout()).show(contentPanel, "suspects");
	}

	private void showQuestionScreen(Suspect suspect) {
		currentSuspect = suspect;
		statementArea.setText("");

		// 라벨 아이콘 및 텍스트 설정
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/person.png"));
		Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		suspectLabel.setIcon(new ImageIcon(img));
		suspectLabel.setText(suspect.getName() + " 심문 중...");

		// 질문 버튼 패널 갱신
		questionBtnPanel.removeAll();
		System.out.println("HiddenClues size: " + suspect.getHiddenClues().size());
		for (int i = 0; i < suspect.getHiddenClues().size(); i++) {
			Clue clue = suspect.getHiddenClues().get(i);
			String clueName = (clue != null) ? clue.getName() : "일반 질문";
			JButton btn = new JButton("질문 " + (i+1) + " (" + clueName + ")");
//			Theme.styleButton(btn);
		    int index = i;
		    btn.addActionListener(e -> askQuestion(index));
		    questionBtnPanel.add(btn);
		}
		questionBtnPanel.revalidate();
		questionBtnPanel.repaint();

		((CardLayout) contentPanel.getLayout()).show(contentPanel, "questions");
	}

	private void askQuestion(int index) {
		if (currentSuspect == null)
			return;
		try {
			// Suspect에서 이미 답변 + 단서 포맷을 만들어 리턴
			String answer = currentSuspect.answerQuestion(index);

			// 텍스트 영역에 바로 append
			statementArea.append(answer + "\n");

			// 로그 저장
			LogManager.saveLog(answer);
			gm.getMainPanel().refreshLog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnter() {
	}

	@Override
	public void refreshLog() {
		statementArea.setText("");
	}

	@Override
	public void onExit() {
	}
}
