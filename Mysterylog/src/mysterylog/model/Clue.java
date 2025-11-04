package mysterylog.model;

public class Clue {
	private String name;
	private String description;
	private String type; // "INTERROGATION", "INVESTIGATION", "INFERENCE"
	private boolean discovered;

	// --- 확장 요소 ---
	private int reliability; // 신뢰도 (1~5)
	private boolean fake; // 가짜 단서 여부
	private String relatedTo; // 관련 용의자 이름 (선택적)
	private boolean derived; // 파생 단서 여부
	private Suspect discoveredBy; // 이 단서를 발견한 용의자

	public Clue(String name, String description, String type) {
		this(name, description, type, 3, false, null, false);
	}

	public Clue(String name, String description, String type, int reliability, boolean fake, String relatedTo,
			boolean derived) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.discovered = false;
		this.reliability = reliability;
		this.fake = fake;
		this.relatedTo = relatedTo;
		this.derived = derived;
	}

	// --- getter/setter ---
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public int getReliability() {
		return reliability;
	}

	public boolean isFake() {
		return fake;
	}

	public String getRelatedTo() {
		return relatedTo;
	}

	public boolean isDerived() {
		return derived;
	}
	
	public Suspect getDiscoveredBy() {
	    return discoveredBy;
	}
	
	public void setDiscoveredBy(Suspect discoveredBy) {
	    this.discoveredBy = discoveredBy;
	}

	@Override
	public String toString() {
		return name + (fake ? " ⚠️(가짜)" : "") + " [" + type + "]" + (derived ? " (파생)" : "") + " (신뢰도: "
				+ getReliabilityText() + ")";
	}

	// 신뢰도를 텍스트로 변환
	private String getReliabilityText() {
		switch (reliability) {
		case 1:
			return "매우 낮음";
		case 2:
			return "낮음";
		case 3:
			return "보통";
		case 4:
			return "높음";
		case 5:
			return "매우 높음";
		default:
			return "알 수 없음";
		}
	}

	// 단서 연결로 파생 단서 생성
	public static Clue linkClues(Clue a, Clue b) {
		String name = "파생: " + a.getName() + " + " + b.getName();
		String desc = "이 단서는 " + a.getName() + "와 " + b.getName() + "의 조합으로 추론됨";
		int rel = Math.min(a.getReliability(), b.getReliability()); // 신뢰도는 둘 중 낮은 값
		return new Clue(name, desc, "INFERENCE", rel, false, null, true);
	}
}
