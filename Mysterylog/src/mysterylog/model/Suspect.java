package mysterylog.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mysterylog.exception.GameException;

public class Suspect {
	private String name; // 용의자 이름
	private List<String> statements; // 질문별 진술
	private List<Clue> hiddenClues; // 질문별 숨겨진 단서
	private Set<Integer> askedQuestions; // 이미 물어본 질문 인덱스

	public Suspect(String name, List<String> statements, List<Clue> hiddenClues) {
		this.name = name;
		this.statements = statements;
		this.hiddenClues = hiddenClues;
		this.askedQuestions = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public List<Clue> getHiddenClues() {
		return hiddenClues;
	}

	public String answerQuestion(int index) throws GameException {
		if (index < 0 || index >= statements.size()) {
			throw new GameException("!질문 인덱스 오류");
		}

		if (askedQuestions.contains(index)) {
			return "!이미 질문한 내용입니다.";
		}

		askedQuestions.add(index);

		String statement = statements.get(index);
		Clue clue = hiddenClues.get(index);

		StringBuilder result = new StringBuilder();
		result.append("[질문 ").append(index + 1).append("] ").append(statement).append("\n");

		if (clue != null && !clue.isDiscovered()) {
			clue.setDiscovered(true);
			clue.setDiscoveredBy(this);
			result.append("  단서: " + clue.getName() + "\n");
		}

		return result.toString();
	}

	// 가짜 단서 확인
	public boolean hasFakeClue() {
		for (Clue clue : hiddenClues) {
			if (clue.isFake()) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		askedQuestions.clear();
		for (Clue c : hiddenClues) {
			if (c != null) {
				c.setDiscovered(false);
			}
		}
	}
}
