package fi.jnsknn.cliutils.core;

public interface ICommands {
	boolean isCommand(String[] args);

	String getInputStringFromArgs(String[] args);

	boolean isAuthorized(String sender);

	void execute(String channel, String sender, String[] args);
}
