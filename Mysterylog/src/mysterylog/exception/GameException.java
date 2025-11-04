package mysterylog.exception;

import java.io.IOException;

public class GameException extends Exception {

	// 1. 일반적인 에러 메시지용 생성자
	public GameException(String message) {
		super("[GameException] " + message);
	}

	// 2. IOException 처리용 생성자
	public GameException(String message, IOException e) {
        super(message, e);
        System.err.println("[GameException] " + message + " | 원인: " + e.getMessage());
    }
	
	// 선택적으로 Exception 전체를 받아서 통합 처리 가능
	public GameException(Exception e) {
		super("[GameException][" + e.getClass().getSimpleName() + "] " + e.getMessage(), e);
	}

	// toString() 오버라이드: System.out.println(e) 시 내 메시지 출력
	@Override
	public String toString() {
		return getMessage();
	}
}
