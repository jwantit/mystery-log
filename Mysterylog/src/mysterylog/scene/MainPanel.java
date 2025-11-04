package mysterylog.scene;

import javax.swing.*;
import java.awt.*;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;

public class MainPanel extends JPanel implements Scene {

	private JTextArea logArea;
	private GameManager gm;

	public MainPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout(0, 5));

		// 타이틀 패널 ============================
		JLabel title = new JLabel("🕵️ Mystery Log", SwingConstants.CENTER);
		title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
		title.setOpaque(true);
		title.setBackground(Theme.TITLE_BG_COLOR); // 어두운 배경
		title.setForeground(Color.WHITE); // 글씨 흰색
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(title, BorderLayout.NORTH);

		// 버튼 패널 ==============================
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setBackground(Theme.PANEL_BG);

		JButton interrogationBtn = new JButton("심문하기");
		JButton investigationBtn = new JButton("현장 조사");
		JButton deductionBtn = new JButton("추리하기");
		JButton clueListBtn = new JButton("단서 목록");

		// Theme의 버튼 스타일 적용
		Theme.styleButton(interrogationBtn);
		Theme.styleButton(investigationBtn);
		Theme.styleButton(deductionBtn);
		Theme.styleButton(clueListBtn);

		// 버튼 크기 통일
		Dimension btnSize = new Dimension(120, 40);
		interrogationBtn.setPreferredSize(btnSize);
		investigationBtn.setPreferredSize(btnSize);
		deductionBtn.setPreferredSize(btnSize);
		clueListBtn.setPreferredSize(btnSize);

		interrogationBtn.addActionListener(e -> gm.moveTo("INTERROGATION"));
		investigationBtn.addActionListener(e -> gm.moveTo("INVESTIGATION"));
		deductionBtn.addActionListener(e -> gm.moveTo("DEDUCTION"));
		clueListBtn.addActionListener(e -> gm.showClueList());

		buttonPanel.add(interrogationBtn);
		buttonPanel.add(investigationBtn);
		buttonPanel.add(deductionBtn);
		buttonPanel.add(clueListBtn);
		add(buttonPanel, BorderLayout.CENTER);

		// 로그 영역 ============================
		logArea = new JTextArea(15, 50);
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("📜 사건 로그"));
		add(scrollPane, BorderLayout.SOUTH);

		// 초기 로그 표시
		refreshLog();
	}

	@Override
	public void onEnter() {
		refreshLog();
	}

	@Override
	public void refreshLog() {
		logArea.setText("");
		for (String log : LogManager.getLogs()) {
			logArea.append(log + "\n");
		}
	}

	public void appendLog(String msg) {
		try {
			LogManager.saveLog(msg); // GameException 가능
			logArea.append(msg + "\n");
		} catch (GameException e) {
			// UI에서 바로 오류 표시
			JOptionPane.showMessageDialog(this, e.getMessage(), "로그 저장 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void onExit() {
	}
}
