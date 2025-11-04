package mysterylog;

import javax.swing.*;
import mysterylog.manager.GameManager;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            new GameManager();
        });
	}

}
