package mysterylog.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleData {

    // 모든 단서
    public static List<Clue> clues = new ArrayList<>();

    // 모든 용의자
    public static List<Suspect> suspects = new ArrayList<>();

    static {
        // 1️⃣ 단서 생성
    	Clue cctv = new Clue("CCTV 로그 끊김", "전시실 근처에서 CCTV 전원이 끊긴 흔적 발견", "INTERROGATION");
        Clue card = new Clue("퇴근 카드 기록 이상", "보관함 옆 단말기에서 Mira의 퇴근 기록 이상 발견", "INVESTIGATION");
        Clue key = new Clue("복제 열쇠", "창문 근처 바닥에서 Yoon의 열쇠와 동일한 복제 열쇠 발견", "INVESTIGATION");

        clues.add(cctv);
        clues.add(card);
        clues.add(key);

        // 2️⃣ 용의자 생성
        suspects.add(new Suspect("Mira",
                Arrays.asList(
                        "1시 전에 퇴근했어요.",
                        "퇴근 기록 이상 발견",
                        "CCTV 영상이 끊겼습니다."),
                Arrays.asList(null, card, null)));

        suspects.add(new Suspect("Alex",
                Arrays.asList(
                        "나는 계속 전시실에 있었어요.",
                        "CCTV 이상 없음",
                        "열쇠를 본 적 없다."),
                Arrays.asList(cctv, null, null)));

        suspects.add(new Suspect("Yoon",
                Arrays.asList(
                        "퇴근 기록은 정확합니다.",
                        "CCTV는 확인 못했어요",
                        "복제 열쇠는 없어요."),
                Arrays.asList(null, null, key)));
    }
}
