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

	public String answerQuestion(int index) throws GameException  {
		if (index < 0 || index >= statements.size()) {
			throw new GameException("!질문 인덱스 오류");
		}

		// askedQuestions에 들어온 index가 이미 있을 경우
		if (askedQuestions.contains(index)) {
			return "!이미 질문한 내용입니다.";
		}

		askedQuestions.add(index);

		// 그게 아니라면 단서 리스트에서 해당 인덱스의 clue 객체 추출 후 setDiscovered 메소드 호출
		Clue clue = hiddenClues.get(index);
		String statement = statements.get(index);
		
		// 단서가 존재하고, INTERROGATION 유형일 때
	    if (clue != null && clue.getType().equals("INTERROGATION")) {
	        clue.setDiscovered(true);

	        // 강조된 포맷으로 리턴
	        return statement
	             + "\n\n<단서 발견!>\n"
	             + "================\n"
	             + clue.getName()
	             + "\n================\n";
	    }
	    
	    return statement;
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
