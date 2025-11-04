package mysterylog.scene;

import javax.swing.*;
import java.awt.*;
import mysterylog.config.Theme;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;
import mysterylog.model.Suspect;

public class InterrogationPanel extends JPanel implements Scene {

    private GameManager gm;
    private JPanel contentPanel;
    private JTextArea statementArea;
    private Suspect currentSuspect;
    private JLabel suspectLabel;
    private JPanel questionBtnPanel;
    private JPanel suspectListPanel;

    public InterrogationPanel(GameManager gm) {
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
        suspectListPanel = new JPanel();
        suspectListPanel.setLayout(new BoxLayout(suspectListPanel, BoxLayout.Y_AXIS));
        suspectListPanel.setBackground(Theme.PANEL_BG);
        suspectListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(suspectListPanel, "suspects");

        // 2. 질문 화면
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(Theme.PANEL_BG);

        suspectLabel = new JLabel("", SwingConstants.CENTER);
        suspectLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        suspectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(suspectLabel);

        questionBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        questionBtnPanel.setBackground(Theme.PANEL_BG);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(questionBtnPanel);

        JButton backBtn = Theme.createStyleButton("← 용의자 목록으로");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> showSuspectList());
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(backBtn);

        statementArea = new JTextArea(8, 40);
        statementArea.setEditable(false);
        statementArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(statementArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("답변"));
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(scrollPane);
        questionPanel.add(Box.createVerticalGlue());

        contentPanel.add(questionPanel, "questions");

        // 하단 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(Theme.PANEL_BG);
        JButton mainBtn = Theme.createStyleButton("메인으로");
        mainBtn.addActionListener(e -> gm.moveTo("MAIN"));
        bottomPanel.add(mainBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {
        // 진입 시 항상 용의자 목록 갱신
        showSuspectList();
        refreshSuspectButtons();
    }

    private void refreshSuspectButtons() {
        suspectListPanel.removeAll();

        JLabel infoLabel = new JLabel("심문할 용의자를 선택하세요", SwingConstants.CENTER);
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        suspectListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        suspectListPanel.add(infoLabel);
        suspectListPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (Suspect s : gm.getCurrentSuspects()) {
            JButton btn = Theme.createStyleButton(s.getName());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.addActionListener(e -> showQuestionScreen(s));
            suspectListPanel.add(btn);
            suspectListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        suspectListPanel.revalidate();
        suspectListPanel.repaint();
    }

    private void showSuspectList() {
        currentSuspect = null;
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "suspects");
        statementArea.setText("");
        refreshSuspectButtons();
    }

    private void showQuestionScreen(Suspect suspect) {
        currentSuspect = suspect;
        statementArea.setText("");

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/person.png"));
        Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        suspectLabel.setIcon(new ImageIcon(img));
        suspectLabel.setText(suspect.getName() + " 심문 중...");

        questionBtnPanel.removeAll();
        for (int i = 0; i < suspect.getquestions().size(); i++) {
            JButton btn = Theme.createStyleButton("질문 " + (i + 1));
            int index = i;
            btn.addActionListener(e -> askQuestion(index));
            questionBtnPanel.add(btn);
        }
        questionBtnPanel.revalidate();
        questionBtnPanel.repaint();

        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "questions");
    }

    private void askQuestion(int index) {
        if (currentSuspect == null) return;
        try {
            String answer = currentSuspect.answerQuestion(index);
            statementArea.append(answer + "\n");

            LogManager.saveLog(answer);
            gm.getMainPanel().refreshLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshLog() {
        statementArea.setText("");
    }

    @Override
    public void onExit() {}
}
