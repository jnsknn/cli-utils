package fi.jonne.javacliutils.settings;

public abstract class Settings {
	
	public static final String LOCAL_SENDER = "JavaCLIUtils";
	public static final String LOCAL_CHANNEL = "Local";

	public static String currentLocalSender = LOCAL_SENDER;
	
	public static final void initializeSettings(){
		// To do: add reading settings from file!
	}

}
