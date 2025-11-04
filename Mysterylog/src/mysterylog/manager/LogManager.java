package mysterylog.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mysterylog.exception.GameException;

public class LogManager {
	private static List<String> logs = new ArrayList<>();
	private static final String LOG_FILE = "game_log.txt";
	
	// 로그 저장 (메모리 + 파일)
	public static void saveLog(String msg) throws GameException {
		logs.add(msg);
		appendFile(msg);
	}

	public static List<String> getLogs() {
		return new ArrayList<>(logs);
	}

	// 전체 로그 초기화
	public static void clearLogs() throws GameException{
		logs.clear();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE))) {
			bw.write(""); // 파일 비우기
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameException("로그 파일 초기화 실패", e);
		}
	}
	
	private static void appendFile(String msg) throws GameException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
			bw.write(msg);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameException("로그 파일 저장 실패", e);
		}
	}
}
