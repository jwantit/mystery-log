package mysterylog.model;

import java.util.List;

public interface Episode {
	String getTitle(); // 에피소드 제목

	String getDescription(); // 에피소드 설명

	List<Clue> getClues(); // 사건 단서

	List<Suspect> getSuspects(); // 사건 용의자

	List<String> getLocations(); // 사건 조사할 장소

	String getCulprit(); // 사건의 범인
}
