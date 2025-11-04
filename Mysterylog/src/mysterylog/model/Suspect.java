package mysterylog.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mysterylog.exception.GameException;

public class Suspect {
	private String name; // 용의자 이름
	private String occupation; // 직업
	private List<String> questions; // 질문 목록
	private List<String> statements; // 질문별 진술
	private List<Clue> hiddenClues; // 질문별 숨겨진 단서
	private Set<Integer> askedQuestions; // 이미 물어본 질문 인덱스

	public Suspect(String name, String occupation, List<String> questions, List<String> statements, List<Clue> hiddenClues) {
		this.name = name;
		this.occupation = occupation;
		this.questions = questions;
		this.statements = statements;
		this.hiddenClues = hiddenClues;
		this.askedQuestions = new HashSet<>();
	}

	public String getName() {
		return name;
	}
	
	public String getOccupation() {
        return occupation;
    }
	
	public List<String> getquestions() {
		return questions;
	}

	public List<Clue> getHiddenClues() {
		return hiddenClues;
	}

	public String answerQuestion(int index) throws GameException {
		if (index < 0 || index >= statements.size()) {
			throw new GameException("질문 인덱스 오류");
		}

		if (askedQuestions.contains(index)) {
			return "이미 질문한 내용입니다.";
		}

		askedQuestions.add(index);

		String question = questions.get(index);
		String statement = statements.get(index);
		Clue clue = hiddenClues.get(index);

		StringBuilder result = new StringBuilder();
		result.append("[질문 ").append(index + 1).append("] ").append(question).append("\n");
		result.append(name).append(": ").append(statement).append("\n");

		// 단서가 발견될 경우
		if (clue != null && "INTERROGATION".equals(clue.getType()) && !clue.isDiscovered()) {
			clue.setDiscovered(true);
			clue.setDiscoveredBy(this);

			result.append("\n---- 발견된 단서 ----\n");
			result.append("발견자: ").append(this.getName()).append("\n");
			result.append("단서: ").append(clue.getName()).append("\n");
			result.append("설명: ").append(clue.getDescription()).append("\n");
			result.append("--------------------\n");
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
