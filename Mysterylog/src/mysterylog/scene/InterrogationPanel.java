package mysterylog.scene;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.SampleData;
import mysterylog.model.Suspect;
//
//public class InterrogationPanel extends JPanel implements Scene {
//
//	private GameManager gm;
//	private JPanel contentPanel; // 메인 내용 부분 (용의자 목록 ↔ 질문 화면 전환)
//	private JTextArea statementArea;
//	private Suspect currentSuspect;
//
//	public InterrogationPanel(GameManager gm) {
//		this.gm = gm;
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//		// 제목 패널 ============================
//		JLabel title = new JLabel("👁️‍🗨️ 심문하기", SwingConstants.CENTER);
//		title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
//		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
//		add(title, BorderLayout.NORTH);
//
//		// 내용 전환용 패널 (CardLayout으로 구성) ====================
//		contentPanel = new JPanel(new CardLayout());
//		contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//		add(contentPanel);
//
//		// 1 - 용의자 목록 화면
//		JPanel suspectListPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
//		suspectListPanel.add(new JLabel("심문할 용의자를 선택하세요"));
//		for (Suspect s : SampleData.suspects) {
//			JButton btn = new JButton(s.getName());
//			btn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
//			btn.addActionListener(e -> showQuestionScreen(s));
//			suspectListPanel.add(btn);
//		}
//
//		// 2 - 질문 화면
//		JPanel questionPanel = new JPanel();
//		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
//
//		JLabel suspectLabel = new JLabel("", SwingConstants.CENTER);
//		suspectLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
//		suspectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//		questionPanel.add(suspectLabel);
//
//		// 질문 버튼 3개
//		JPanel questionBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
//		JButton[] questionBtns = new JButton[3];
//		for (int i = 0; i < 3; i++) {
//			int index = i;
//			questionBtns[i] = new JButton((i + 1) + ") 질문 " + (i + 1));
//			questionBtns[i].setFont(new Font("맑은 고딕", Font.PLAIN, 16));
//			questionBtns[i].addActionListener(e -> askQuestion(index));
//			questionBtnPanel.add(questionBtns[i]);
//		}
//		questionPanel.add(questionBtnPanel);
//
//		// 뒤로가기 버튼
//		JButton backBtn = new JButton("← 용의자 목록으로");
//		backBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
//		backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//		backBtn.addActionListener(e -> showSuspectList());
//		questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//		questionPanel.add(backBtn);
//		questionPanel.add(Box.createVerticalGlue());
//
//		// 답변 영역
//	    statementArea = new JTextArea(8, 40);
//	    statementArea.setEditable(false);
//	    statementArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
//	    JScrollPane scrollPane = new JScrollPane(statementArea);
//	    scrollPane.setBorder(BorderFactory.createTitledBorder("답변"));
//	    questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//	    questionPanel.add(scrollPane);
//
//
//		// 두 화면 등록
//		contentPanel.add(suspectListPanel, "suspects");
//		contentPanel.add(questionPanel, "questions");
//
//		// 초기엔 용의자 목록부터 보이게
//		showSuspectList();
//
//		// 하단 패널 ==================================
//	    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
//	    JButton mainBtn = new JButton("메인으로");
//	    mainBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
//	    mainBtn.addActionListener(e -> gm.moveTo("MAIN"));
//	    bottomPanel.add(mainBtn);
//	    add(bottomPanel, BorderLayout.SOUTH);
//	}
//
//	// 용의자 목록 화면 보여주기
//	private void showSuspectList() {
//		currentSuspect = null;
//		((CardLayout) contentPanel.getLayout()).show(contentPanel, "suspects");
//	}
//
//	// 질문 선택 화면 보여주기
//	private void showQuestionScreen(Suspect suspect) {
//        currentSuspect = suspect;
//        statementArea.setText(""); // 초기화
//        suspectLabel.setText("👤 " + suspect.getName() + " 심문 중..."); // ← 바로 접근
//        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "questions");
//    }
//
//	private void askQuestion(int index) {
//		if (currentSuspect == null)
//			return;
//		try {
//			String result = currentSuspect.answerQuestion(index);
//
//			statementArea.append(result + "\n\n");
//			LogManager.saveLog(result);
//			gm.getMainPanel().refreshLog();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onEnter() {
//
//	}
//
//	@Override
//	public void refreshLog() {
//		statementArea.setText("");
//	}
//
//	@Override
//	public void onExit() {
//	}
//}
public class InterrogationPanel extends JPanel implements Scene {

    private GameManager gm;
    private JPanel contentPanel; // 메인 내용 부분 (용의자 목록 ↔ 질문 화면 전환)
    private JTextArea statementArea;
    private Suspect currentSuspect;

    private JLabel suspectLabel; // ← 여기 멤버 변수로 선언

    public InterrogationPanel(GameManager gm) {
        this.gm = gm;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 제목
        JLabel title = new JLabel("👁️‍🗨️ 심문하기", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title);

        // 내용 전환용 패널
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(contentPanel);

        // 1 - 용의자 목록
        JPanel suspectListPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        suspectListPanel.add(new JLabel("심문할 용의자를 선택하세요"));
        for (Suspect s : SampleData.suspects) {
            JButton btn = new JButton(s.getName());
            btn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            btn.addActionListener(e -> showQuestionScreen(s));
            suspectListPanel.add(btn);
        }

        // 2 - 질문 화면
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        suspectLabel = new JLabel("", SwingConstants.CENTER); // ← 멤버 변수로 저장
        suspectLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        suspectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(suspectLabel);

        // 질문 버튼
        JPanel questionBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton[] questionBtns = new JButton[3];
        for (int i = 0; i < 3; i++) {
            int index = i;
            questionBtns[i] = new JButton((i + 1) + ") 질문 " + (i + 1));
            questionBtns[i].setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            questionBtns[i].addActionListener(e -> askQuestion(index));
            questionBtnPanel.add(questionBtns[i]);
        }
        questionPanel.add(questionBtnPanel);

        // 뒤로가기 버튼
        JButton backBtn = new JButton("← 용의자 목록으로");
        backBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> showSuspectList());
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(backBtn);
        questionPanel.add(Box.createVerticalGlue());

        // 답변 영역
        statementArea = new JTextArea(8, 40);
        statementArea.setEditable(false);
        statementArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(statementArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("답변"));
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(scrollPane);

        // 카드패널 등록
        contentPanel.add(suspectListPanel, "suspects");
        contentPanel.add(questionPanel, "questions");

        showSuspectList();

        // 하단 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton mainBtn = new JButton("메인으로");
        mainBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        mainBtn.addActionListener(e -> gm.moveTo("MAIN"));
        bottomPanel.add(mainBtn);
        add(bottomPanel);
    }

    private void showSuspectList() {
        currentSuspect = null;
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "suspects");
    }

    private void showQuestionScreen(Suspect suspect) {
        currentSuspect = suspect;
        statementArea.setText(""); // 초기화
        suspectLabel.setText("👤 " + suspect.getName() + " 심문 중..."); // ← 바로 접근
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "questions");
    }

    private void askQuestion(int index) {
        if (currentSuspect == null) return;
        try {
            String result = currentSuspect.answerQuestion(index);
            statementArea.append(result + "\n\n");
            LogManager.saveLog(result);
            gm.getMainPanel().refreshLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnter() {}

    @Override
    public void refreshLog() {
        statementArea.setText("");
    }

    @Override
    public void onExit() {}
}
