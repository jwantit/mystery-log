package mysterylog;

import javax.swing.*;

import mysterylog.exception.GameException;
import mysterylog.manager.GameManager;
import mysterylog.manager.LogManager;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            new GameManager();
            try {
				LogManager.saveLog("테스트 로그");
			} catch (GameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
	}

}
