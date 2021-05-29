package fi.jnsknn.cliutils.core.utils.api;

public class WeirdName {

	private String generatedName = "";
	private String originalName = "";
	private String fixedName = "";

	public WeirdName(String generatedName, String originalName, String fixedName) {
		this.setGeneratedName(generatedName);
		this.setOriginalName(originalName);
		this.setFixedName(fixedName);
	}

	public String getGeneratedName() {
		return generatedName;
	}

	public void setGeneratedName(String generatedName) {
		this.generatedName = generatedName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getFixedName() {
		return fixedName;
	}

	public void setFixedName(String fixedName) {
		this.fixedName = fixedName;
	}

}
