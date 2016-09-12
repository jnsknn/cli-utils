package fi.jonne.javacliutils.settings;

public class Settings {
	
	private static Settings instance;
	
	public static String DEFAULT_SENDER = "JavaCLIUtils";
	public static final String DEFAULT_CHANNEL = "Local";
	
	public Settings(){}
	
	public static Settings getInstace(){
		if(instance == null){
			instance = new Settings();
		}
		return instance;
	}
}
