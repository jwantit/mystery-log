package mysterylog.scene;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;
import mysterylog.model.SampleData;

public class InvestigationPanel extends JPanel implements Scene {
	private JTextArea textArea;
	private GameManager gm;

	public InvestigationPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
        setBackground(Theme.PANEL_BG);

		// 타이틀 영역 =======================================
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/search.png")); // 가정: 현장 조사 아이콘
		Image img = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel title = new JLabel(" 현장 조사", new ImageIcon(img), SwingConstants.CENTER);

		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR);
		title.setForeground(Theme.BUTTON_TEXT_COLOR);
		title.setFont(Theme.TITLE_FONT);
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		add(Box.createRigidArea(new Dimension(0, 15)));

		// 중앙 내용 패널 =======================
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(Theme.PANEL_BG);

		// 조사 위치 안내 라벨
        JLabel infoLabel = new JLabel("조사할 장소를 선택하세요", SwingConstants.CENTER);
        infoLabel.setFont(Theme.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Theme.PANEL_BG);
        
		JButton exhibitionBtn = Theme.createStyleButton("전시실");
        JButton windowBtn = Theme.createStyleButton("창문 근처");
        JButton lockerBtn = Theme.createStyleButton("보관함");

        exhibitionBtn.addActionListener(e -> searchClue("전시실"));
        windowBtn.addActionListener(e -> searchClue("창문 근처"));
        lockerBtn.addActionListener(e -> searchClue("보관함"));

        buttonPanel.add(exhibitionBtn);
        buttonPanel.add(windowBtn);
        buttonPanel.add(lockerBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(buttonPanel);

		// 단서 표시 영역 =========================
        textArea = new JTextArea(8, 40);
        textArea.setEditable(false);
        textArea.setFont(Theme.BUTTON_FONT.deriveFont(Font.PLAIN, 14f));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.PRIMARY_COLOR),
                "🔎 발견된 단서",
                TitledBorder.LEFT, TitledBorder.TOP,
                Theme.BUTTON_FONT,
                Theme.PRIMARY_COLOR
        ));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);

		// 버튼 패널 =======================
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // 간격 조정
		bottomPanel.setBackground(Theme.PANEL_BG);

		JButton clueListBtn = Theme.createStyleButton("단서 목록");
		JButton mainBtn = Theme.createStyleButton("메인으로");

		clueListBtn.addActionListener(e -> gm.showClueList());
		mainBtn.addActionListener(e -> gm.moveTo("MAIN"));
		bottomPanel.add(clueListBtn);
        bottomPanel.add(mainBtn);
        add(bottomPanel, BorderLayout.SOUTH);
	}

	private void searchClue(String location) {
		boolean foundNew = false;
		for (Clue c : SampleData.clues) {
			if (c.getType().equals("INVESTIGATION") && c.getDescription().contains(location)
					&& !c.isDiscovered()) {
				c.setDiscovered(true);
				String msg = "단서 발견: " + c.getName();
				textArea.append(msg + "\n");

				try {
					LogManager.saveLog(msg); // GameException 가능
					gm.getMainPanel().refreshLog();
				} catch (GameException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "로그 저장 오류", JOptionPane.ERROR_MESSAGE);
				}
				foundNew = true;
			}
		}

		if (!foundNew) {
			textArea.append("새 단서는 발견되지 않았습니다.\n");
		}
		refreshClues();
	}

	@Override
	public void onEnter() {
		refreshClues();
	}

	@Override
	public void refreshClues() {
		StringBuilder sb = new StringBuilder();
		for (Clue c : SampleData.clues) {
			if (c.isDiscovered()) {
				sb.append("단서 발견: ").append(c.getName()).append("\n");
			}
		}
		textArea.setText(sb.toString());
	}

	@Override
	public void onExit() {
	}
}
