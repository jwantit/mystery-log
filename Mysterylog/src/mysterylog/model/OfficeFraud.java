package mysterylog.model;

import java.util.*;

public class OfficeFraud implements Episode {

	private List<Clue> clues;
	private List<Suspect> suspects;

	public OfficeFraud() {

		// 단서
		Clue fakeInvoice = new Clue("가짜 인보이스", "회사 경리부에서 발견된 의심스러운 인보이스", "INVESTIGATION", 5, false, "경리부 사무실");
		Clue missingReport = new Clue("분실된 보고서", "재무 보고서 일부가 사라짐", "INVESTIGATION", 2, false, "회계팀 책상");
		Clue accessLog = new Clue("출입 기록 이상", "회계팀 출입 기록에 이상이 있음", "INVESTIGATION", 3, false, "회계팀 출입구");

		Clue kimEmail = new Clue("김민수 이메일", "심문 중 드러난 이메일", "INTERROGATION");
		Clue leeReport = new Clue("이영희 보고서", "심문 중 드러난 보고서 내용", "INTERROGATION");
		Clue parkNote = new Clue("박지훈 메모", "심문 중 드러난 메모 내용", "INTERROGATION");

		clues = Arrays.asList(fakeInvoice, missingReport, accessLog, kimEmail, leeReport, parkNote, null, null);

		// 용의자
		Suspect kim = new Suspect("김민수", "회계팀 직원",
				Arrays.asList("최근에 어떤 보고서를 작성하셨나요?", "이 이메일의 출처가 본인 맞나요?", "분실된 보고서를 본 적 있습니까?"),
				Arrays.asList("저는 아무 것도 몰랐습니다.", "그 이메일은 제 것이 아닙니다.", "보고서를 본 적 없습니다."),
				Arrays.asList(fakeInvoice, kimEmail, missingReport));

		Suspect lee = new Suspect("이영희", "회계팀 팀장",
				Arrays.asList("분실된 보고서에 대해 알고 계신가요?", "이메일 관련 문제를 확인하셨나요?", "최근 직원들의 출입 기록 확인하셨나요?"),
				Arrays.asList("회사 회계와 관련 없습니다.", "이메일을 열어본 적 없습니다.", "출입 기록 확인했지만 특이 사항 없었습니다."),
				Arrays.asList(null, leeReport, accessLog));

		Suspect park = new Suspect("박지훈", "경리부 사무보조",
				Arrays.asList("최근 어떤 문서를 다루셨나요?", "이 메모 내용에 대해 설명해 주실 수 있나요?", "가짜 인보이스와 관련해서 본 것이 있나요?"),
				Arrays.asList("재무 보고서 일부를 처리했습니다.", "이 메모는 단순한 업무 기록입니다.", "가짜 인보이스요? 저는 발견하지 못했습니다."),
				Arrays.asList(null, parkNote, fakeInvoice));

		suspects = Arrays.asList(kim, lee, park);
	}

	@Override
	public String getTitle() {
		return "사무실 회계 사기 사건";
	}

	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("회사 내부에서 발생한 회계 사기 사건. 가짜 인보이스와 의심스러운 이메일이 발견되었습니다.\n\n");

		for (Suspect s : suspects) {
			sb.append("용의자: ").append(s.getName()).append("\n");
			sb.append("직업: ").append(s.getOccupation()).append("\n\n");
		}

		return sb.toString();
	}

	@Override
	public List<Clue> getClues() {
		return clues;
	}

	@Override
	public List<Suspect> getSuspects() {
		return suspects;
	}

	@Override
	public List<String> getLocations() {
		return Arrays.asList("경리부 사무실", "회계팀 책상", "회계팀 출입구");
	}

	@Override
	public String getCulprit() {
		return "김민수"; // 범인
	}
}
