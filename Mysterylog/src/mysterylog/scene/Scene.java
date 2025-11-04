package mysterylog.scene;

public interface Scene {
	void onEnter(); // 화면 진입 시 실행

	void onExit(); // 화면 종료 시 실행 // 화면 진입 시 단서 갱신, 필요시 오버라이드

	//단서 목록 갱신
	default void refreshClues() {}

	// 로그 갱신
	default void refreshLog() {}
}
