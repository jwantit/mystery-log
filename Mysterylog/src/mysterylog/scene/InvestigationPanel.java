package mysterylog.scene;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;
import mysterylog.model.Clue;

public class InvestigationPanel extends JPanel implements Scene {

	private JTextArea textArea;
	private GameManager gm;
	private JPanel buttonPanel;
	private Map<String, List<Clue>> locationClues; // 장소별 단서 매핑

	public InvestigationPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
		setBackground(Theme.PANEL_BG);

		// 상단 타이틀
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/search.png"));
		Image img = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel title = new JLabel(" 현장 조사", new ImageIcon(img), SwingConstants.CENTER);
		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR);
		title.setForeground(Theme.BUTTON_TEXT_COLOR);
		title.setFont(Theme.TITLE_FONT);
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		// 중앙 패널
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(Theme.PANEL_BG);

		JLabel infoLabel = new JLabel("조사할 장소를 선택하세요", SwingConstants.CENTER);
		infoLabel.setFont(Theme.BUTTON_FONT.deriveFont(Font.BOLD, 16f));
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(infoLabel);

		// 장소 버튼 패널
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		buttonPanel.setBackground(Theme.PANEL_BG);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(buttonPanel);

		// 단서 표시 영역
		textArea = new JTextArea(8, 40);
		textArea.setEditable(false);
		textArea.setFont(Theme.BUTTON_FONT.deriveFont(Font.PLAIN, 14f));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.PRIMARY_COLOR),
				"🔎 발견된 단서", TitledBorder.LEFT, TitledBorder.TOP, Theme.BUTTON_FONT, Theme.PRIMARY_COLOR));
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(scrollPane);
		contentPanel.add(Box.createVerticalGlue());

		add(contentPanel, BorderLayout.CENTER);

		// 하단 버튼
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		bottomPanel.setBackground(Theme.PANEL_BG);

		JButton clueListBtn = Theme.createStyleButton("단서 목록");
		JButton mainBtn = Theme.createStyleButton("메인으로");

		clueListBtn.addActionListener(e -> gm.showClueList());
		mainBtn.addActionListener(e -> gm.moveTo("MAIN"));

		bottomPanel.add(clueListBtn);
		bottomPanel.add(mainBtn);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	@Override
	public void onEnter() {
		textArea.setText("");
		createLocationButtons();
	}

	private void createLocationButtons() {
		buttonPanel.removeAll();

		if (gm.getCurrentEpisode() != null) {
			List<String> locations = gm.getCurrentEpisode().getLocations();
			for (String loc : locations) {
				JButton btn = Theme.createStyleButton(loc);
				btn.addActionListener(e -> discoverLocationClues(loc));
				buttonPanel.add(btn);
			}
		}

		buttonPanel.revalidate();
		buttonPanel.repaint();
	}

	private void discoverLocationClues(String location) {
		StringBuilder result = new StringBuilder();

		for (Clue c : gm.getCurrentEpisode().getClues()) {
			if ("INVESTIGATION".equals(c.getType()) && location.equals(c.getLocation())) {
				if (!c.isDiscovered()) {
					c.setDiscovered(true);
					result.append("단서: ").append(c.getName()).append("\n  설명: ").append(c.getDescription())
							.append("\n\n");
				}
			}
		}

		if (result.length() > 0) {
			String log = result.toString();
			textArea.append(log);

			try {
				LogManager.saveLog(log); 
				gm.getMainPanel().refreshLog(); 
			} catch (GameException ex) {
				ex.printStackTrace();
			}
		} else {
			textArea.append("[!] 새로운 단서는 없습니다.\n\n");
		}
	}

	@Override
	public void refreshClues() {
	}

	@Override
	public void onExit() {
	}
}
