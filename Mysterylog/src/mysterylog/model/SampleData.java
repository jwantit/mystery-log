package mysterylog.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleData {

    public static List<Clue> clues = new ArrayList<>();
    public static List<Suspect> suspects = new ArrayList<>();

    static {
        // --- 단서 생성 ---
        Clue cctv = new Clue(
                "CCTV 영상 끊김",
                "전시실 근처 CCTV 영상이 밤 12시 30분경에 갑자기 끊겼음",
                "INTERROGATION",
                5, false, null, false
        );

        Clue card = new Clue(
                "퇴근 기록 이상",
                "보관함 근처 출입 기록에서 Mira의 퇴근 기록이 비정상적으로 남아 있음",
                "INVESTIGATION",
                4, false, "Mira", false
        );

        Clue key = new Clue(
                "복제 열쇠 발견",
                "창문 근처 바닥에서 Yoon의 사무실 열쇠와 동일한 복제 열쇠 발견",
                "INVESTIGATION",
                3, false, "Yoon", false
        );

        Clue fakeNote = new Clue(
                "가짜 쪽지",
                "‘범인은 너야’라고 적혀 있으나 글씨체가 범인과 달라 신뢰도 낮음",
                "INTERROGATION",
                1, true, null, false
        );

        Clue securityLog = new Clue(
                "보안 로그 이상",
                "전산실에서 밤 12시~1시 사이 보안 로그 일부 누락됨",
                "INVESTIGATION",
                4, false, null, false
        );

        Clue witness = new Clue(
                "목격자 진술",
                "목격자가 밤 12시 30분경 ‘누군가 창문 근처를 서성였다’고 증언",
                "INTERROGATION",
                3, false, null, false
        );

        // 파생 단서
        Clue insider = Clue.linkClues(cctv, card); // CCTV 끊김 + 퇴근 기록 이상 → 내부자 개입 추론

        // 리스트에 추가
        clues.addAll(Arrays.asList(cctv, card, key, fakeNote, securityLog, witness, insider));

        // --- 용의자 생성 ---
        suspects.add(new Suspect(
                "Mira",
                Arrays.asList(
                        "제가 1시 전에 퇴근했다고요. 왜 또 확인하시는 거죠?",
                        "보관함 기록요? 네… 뭔가 이상하게 남아 있긴 합니다.",
                        "CCTV요? 그때 제가 전시실 근처에 없었어요.",
                        "목격자요… 그분이 뭔가 잘못 본 것 같아요.",
                        "보안 로그 확인했는데, 특이한 점은 있었습니다.",
                        "내부자 개입이라… 누가 그런 걸 할까요?",
                        "가짜 쪽지요? 아… 그거 정말 쓸데없는 장난이에요."
                ),
                Arrays.asList(null, card, null, witness, securityLog, insider, fakeNote)
        ));

        suspects.add(new Suspect(
                "Alex",
                Arrays.asList(
                        "전시실에 계속 있었습니다. CCTV로 확인하시면 돼요.",
                        "CCTV 영상상으로는 별 문제 없었어요.",
                        "열쇠요? 전혀 못 봤습니다.",
                        "목격자요… 음, 잘못 기억하신 듯합니다.",
                        "보안 로그요? 확인할 필요 없습니다.",
                        "내부자 개입과 저는 관련 없어요.",
                        "가짜 쪽지요? 본 적 없습니다."
                ),
                Arrays.asList(cctv, null, null, witness, null, insider, fakeNote)
        ));

        suspects.add(new Suspect(
                "Yoon",
                Arrays.asList(
                        "퇴근 기록은 정확합니다. 왜 저만 의심하죠?",
                        "CCTV요? 그 시간 전시실 근처에 없었습니다.",
                        "복제 열쇠요? 그런 건 없어요.",
                        "목격자 증언과 저는 무관합니다.",
                        "보안 로그 확인 안 했습니다. 문제가 있었나요?",
                        "내부자 개입과는 관련 없어요.",
                        "가짜 쪽지는 발견하지 못했습니다."
                ),
                Arrays.asList(null, null, key, witness, securityLog, insider, null)
        ));
    }
}
