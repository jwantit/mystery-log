package mysterylog.config;

import javax.swing.*;
import java.awt.*;

public class Theme {

    // 색상
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);       // 짙은 파란색 (Steel Blue)
    public static final Color PRIMARY_HOVER_COLOR = new Color(100, 149, 237); // 마우스 오버 시 밝은 파란색
    public static final Color TITLE_BG_COLOR = new Color(50, 50, 50);
    public static final Color PANEL_BG = new Color(230, 230, 230); //회색
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;               // 버튼 글자색
    public static final Color BUTTON_BG_COLOR = PRIMARY_COLOR;               // 버튼 배경색

    // 폰트
    public static final Font TITLE_FONT = new Font("맑은 고딕", Font.BOLD, 26);
    public static final Font BUTTON_FONT = new Font("맑은 고딕", Font.PLAIN, 16);

    // 버튼 스타일 적용 메서드 (Hover 효과 추가)
    public static void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_BG_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //마우스 오버(Hover) 효과 추가
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_HOVER_COLOR); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }
    
    public static JButton createStyleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Theme.BUTTON_BG_COLOR);
        btn.setForeground(Theme.BUTTON_TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        return btn;
    }
}