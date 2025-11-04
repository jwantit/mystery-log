package mysterylog.model;

public class Clue {
	private String name; //단서 이름
	private String description; //단서 설명
	private String type; // "INTERROGATION", "INVESTIGATION"
	private boolean discovered; //발견 유무
	private int reliability; // 신뢰도 (1~5)
	private boolean fake; // 가짜 단서 여부
	private Suspect discoveredBy; // 이 단서를 발견한 용의자
	private String location; // 발견된 장소

	public Clue(String name, String description, String type) {
		this(name, description, type, 3, false, null);
	}

	public Clue(String name, String description, String type, int reliability, boolean fake, String location) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.discovered = false;
		this.reliability = reliability;
		this.fake = fake;
		this.location = location;
	}

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

	public Suspect getDiscoveredBy() {
	    return discoveredBy;
	}
	
	public void setDiscoveredBy(Suspect discoveredBy) {
	    this.discoveredBy = discoveredBy;
	}
	
	public String getLocation() {
		return location;
	}
}
