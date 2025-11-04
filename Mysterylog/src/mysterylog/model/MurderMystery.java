package mysterylog.model;

import java.util.*;

public class MurderMystery implements Episode {

    private List<Clue> clues;
    private List<Suspect> suspects;

    public MurderMystery() {
        
    	//단서 생성
        Clue knife = new Clue("혈흔 묻은 칼", "피가 묻은 주방 칼이 발견됨", "INVESTIGATION", 5, false, "주방");
        Clue brokenLock = new Clue("깨진 방문 잠금장치", "피해자 방 문 잠금장치가 부서져 있음", "INVESTIGATION", 3, false, "피해자 방");
        Clue footprints = new Clue("이상한 발자국", "현장 근처 바닥에 남은 신발 자국", "INVESTIGATION", 2, false, "거실");

      
        Clue maidTestimony = new Clue("하녀 진술", "사건 당일 밤 주방에서 이상한 소리를 들었다고 함", "INTERROGATION");
        Clue butlerAlibi = new Clue("집사 알리바이", "집사는 사건 시간에 저택 밖에 있었다고 주장", "INTERROGATION");
        Clue gardenerNote = new Clue("정원 관리 메모", "피해자와 관련된 최근 다툼 기록", "INTERROGATION");

       
        clues = Arrays.asList(knife, brokenLock, footprints, maidTestimony, butlerAlibi, gardenerNote);

        // 용의자 생성
        Suspect maid = new Suspect(
            "엘리자베스",
            "하녀",
            Arrays.asList(
                "밤에 무슨 소리를 들었나요?",
                "주방에서 칼을 본 적 있나요?",
                "피해자와 관계는 어땠나요?"
            ),
            Arrays.asList(
                "주방에서 이상한 소리를 들었습니다.",
                "칼은 보지 못했습니다.",
                "피해자와 친했지만 다툰 적은 없습니다."
            ),
            Arrays.asList(null, knife, null)
        );

        Suspect butler = new Suspect(
            "제임스",
            "집사",
            Arrays.asList(
                "사건 당일 어디에 있었나요?",
                "깨진 방문 잠금장치를 본 적 있나요?",
                "발자국을 본 적 있나요?"
            ),
            Arrays.asList(
                "저는 저택 밖에 있었습니다.",
                "방 문 잠금장치가 깨진 것을 확인하지 못했습니다.",
                "발자국은 보지 못했습니다."
            ),
            Arrays.asList(null, brokenLock, footprints)
        );

        Suspect gardener = new Suspect(
            "헨리",
            "정원사",
            Arrays.asList(
                "피해자와 다툰 적이 있나요?",
                "정원 근처에서 메모를 본 적 있나요?",
                "최근 사건 관련 이상한 행동을 보셨나요?"
            ),
            Arrays.asList(
                "작은 다툼이 있었지만 화해했습니다.",
                "메모를 확인했습니다.",
                "특이한 행동은 없었습니다."
            ),
            Arrays.asList(null, gardenerNote, null)
        );

        suspects = Arrays.asList(maid, butler, gardener);
    }

    @Override
    public String getTitle() {
        return "저택 살인 사건";
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("한밤중, 저택에서 주인집이 살해된 사건이 발생했습니다. 현장에서는 칼과 깨진 잠금장치, 이상한 발자국이 발견되었습니다.\n\n");
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
        return Arrays.asList("주방", "피해자 방", "거실", "정원");
    }

    @Override
    public String getCulprit() {
        return "헨리"; // 범인
    }
}
