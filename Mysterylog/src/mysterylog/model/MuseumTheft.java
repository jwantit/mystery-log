package mysterylog.model;

import java.util.*;

public class MuseumTheft implements Episode {

	private List<Clue> clues;
	private List<Suspect> suspects;

	public MuseumTheft() {
		clues = new ArrayList<>();
		suspects = new ArrayList<>();

		// --- 단서 ---
		Clue cctv = new Clue("CCTV 영상 끊김", "전시실 근처 CCTV 영상이 밤 12시 30분경 끊겼음", "INTERROGATION", 5, false, null);
		Clue card = new Clue("퇴근 기록 이상", "보관함 근처 출입 기록에서 Mira의 퇴근 기록이 이상함", "INVESTIGATION", 4, false, "보관함");
		Clue key = new Clue("복제 열쇠 발견", "창문 근처 바닥에서 Yoon의 사무실 열쇠와 동일한 복제 열쇠 발견", "INVESTIGATION", 3, false, "창문 근처");
		Clue fakeNote = new Clue("가짜 쪽지", "‘범인은 너야’라고 적혀 있으나 글씨체가 범인과 달라 신뢰도 낮음", "INTERROGATION", 1, true, null);
		Clue securityLog = new Clue("보안 로그 이상", "전산실에서 밤 12시~1시 사이 보안 로그 일부 누락됨", "INVESTIGATION", 4, false, "보관함");
		Clue witness = new Clue("목격자 진술", "목격자가 밤 12시 30분경 ‘누군가 창문 근처를 서성였다’고 증언", "INTERROGATION", 3, false, null);

		clues.addAll(Arrays.asList(cctv, card, key, fakeNote, securityLog, witness));

		// --- 용의자 ---
		suspects.add(new Suspect("Mira", "전시실 관리자",
				Arrays.asList("퇴근 시간은 언제였습니까?", "보관함 출입기록에 대해 설명해 주실 수 있나요?", "CCTV에 찍힌 건 본 적 있나요?",
						"목격자 진술에 대해 어떻게 생각하시나요?", "보안 로그에서 특이한 점을 보셨나요?", "가짜 쪽지에 대해 아시나요?"),
				Arrays.asList("제가 1시 전에 퇴근했다고요. 왜 또 확인하시는 거죠?", "보관함 기록요? 네… 뭔가 이상하게 남아 있긴 합니다.",
						"CCTV요? 그때 제가 전시실 근처에 없었어요.", "목격자요… 그분이 뭔가 잘못 본 것 같아요.", "보안 로그 확인했는데, 특이한 점은 있었습니다.",
						"가짜 쪽지요? 아… 그거 정말 쓸데없는 장난이에요."),
				Arrays.asList(null, card, null, witness, securityLog, fakeNote)));

		suspects.add(new Suspect("Alex", "전시실 경비원",
				Arrays.asList("사건 당일 어디에 계셨습니까?", "CCTV 영상에 대해 알고 계신가요?", "복제 열쇠에 대해 들어본 적 있나요?",
						"목격자가 창문 근처에서 누군가를 봤다고 합니다. 알고 계신가요?", "보안 로그 관련해서는 어떤가요?", "가짜 쪽지를 본 적 있나요?"),
				Arrays.asList("전시실에 계속 있었습니다. CCTV로 확인하시면 돼요.", "CCTV 영상상으로는 별 문제 없었어요.", "열쇠요? 전혀 못 봤습니다.",
						"목격자요… 음, 잘못 기억하신 듯합니다.", "보안 로그요? 확인할 필요 없습니다.", "가짜 쪽지요? 본 적 없습니다."),
				Arrays.asList(cctv, null, null, witness, null, fakeNote)));

		suspects.add(new Suspect("Yoon", "보안 관리자",
				Arrays.asList("퇴근 기록에 이상이 있던데, 설명해 주시겠습니까?", "CCTV 영상에서는 어디 계셨나요?", "복제 열쇠에 대해 알고 계신가요?",
						"목격자가 당신을 봤다고 증언했습니다. 어떻게 생각하세요?", "보안 로그에 대해 아시는 게 있나요?"),
				Arrays.asList("퇴근 기록은 정확합니다. 왜 저만 의심하죠?", "CCTV요? 그 시간 전시실 근처에 없었습니다.", "복제 열쇠요? 그런 건 없어요.",
						"목격자 증언과 저는 무관합니다.", "보안 로그 확인 안 했습니다. 문제가 있었나요?"),
				Arrays.asList(null, null, key, witness, securityLog)));
	}

	@Override
	public String getTitle() {
		return "미술관 도난 사건";
	}

	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("전시된 명화가 한밤중에 사라졌다. CCTV와 출입 기록을 분석해야 한다.\n\n");

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
		return Arrays.asList("전시실", "창문 근처", "보관함");
	}

	@Override
	public String getCulprit() {
		return "Yoon"; // 범인
	}
}
