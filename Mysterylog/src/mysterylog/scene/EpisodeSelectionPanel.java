package mysterylog.scene;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import mysterylog.config.Theme;
import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.model.Episode;
import mysterylog.model.MurderMystery;
import mysterylog.model.MuseumTheft;
import mysterylog.model.OfficeFraud;

public class EpisodeSelectionPanel extends JPanel implements Scene {

	private GameManager gm;

	// 정보 표시용 컴포넌트
	private JLabel titleLabel;
	private JTextArea descriptionArea;
	private JButton startBtn;

	public EpisodeSelectionPanel(GameManager gm) {
		this.gm = gm;
		setLayout(new BorderLayout());
		setBackground(Theme.PANEL_BG);

		// 좌측: 에피소드 버튼 리스트
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Theme.PANEL_BG);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel panelTitle = new JLabel("에피소드 선택", SwingConstants.CENTER);
		panelTitle.setFont(new Font("맑은 고딕", Font.BOLD, 28));
		panelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		leftPanel.add(panelTitle);

		List<Episode> episodes = List.of(new MuseumTheft(), new OfficeFraud(), new MurderMystery());

		for (Episode ep : episodes) {
			JButton btn = Theme.createStyleButton(ep.getTitle());
			btn.setMaximumSize(new Dimension(300, 50));
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);

			btn.addActionListener(e -> {
				// 선택한 에피소드 세팅
				gm.setCurrentEpisode(ep);

				// 우측 패널에 제목/설명 표시
				titleLabel.setText(ep.getTitle());
				descriptionArea.setText(ep.getDescription());

				// 사건 시작 버튼 활성화
				startBtn.setEnabled(true);
			});

			leftPanel.add(btn);
			leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		add(leftPanel, BorderLayout.WEST);

		// 우측: 선택한 에피소드 정보 표시
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBackground(Theme.PANEL_BG);
		rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		titleLabel = new JLabel("에피소드를 선택하세요", SwingConstants.CENTER);
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		rightPanel.add(titleLabel, BorderLayout.NORTH);

		descriptionArea = new JTextArea();
		descriptionArea.setEditable(false);
		descriptionArea.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		rightPanel.add(scrollPane, BorderLayout.CENTER);

		startBtn = Theme.createStyleButton("사건 시작");
		startBtn.setEnabled(false); // 선택 전에는 비활성화
		startBtn.addActionListener(e -> {
			if (gm.getCurrentEpisode() != null) {
				try {
					gm.startNewCase();
				} catch (GameException ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		rightPanel.add(startBtn, BorderLayout.SOUTH);

		add(rightPanel, BorderLayout.CENTER);
	}

	@Override
	public void onEnter() {
	}

	@Override
	public void refreshLog() {
	}

	@Override
	public void onExit() {
	}
}
