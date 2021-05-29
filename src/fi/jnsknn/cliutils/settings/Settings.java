package fi.jnsknn.cliutils.settings;

public final class Settings {

	public static final boolean IS_SOUND_ENABLED = false;

	private static volatile String currentLocalSender = Constants.LOCAL_SENDER;

	private Settings() {
		throw new IllegalStateException("Utility Class");
	}

	public static String getCurrentLocalSender() {
		return currentLocalSender;
	}

	public static void setCurrentLocalSender(String currentLocalSender) {
		Settings.currentLocalSender = currentLocalSender;
	}

}
