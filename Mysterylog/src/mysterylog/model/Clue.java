package mysterylog.model;

public class Clue {
	private String name; //단서명
	private String description; //설명 
	private String type; // "INTERROGATION" or "INVESTIGATION"
	private boolean discovered; //발견여부

	public Clue(String name, String description, String locationType) {
		this.name = name;
		this.description = description;
		this.type = locationType;
		this.discovered = false;
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
}
