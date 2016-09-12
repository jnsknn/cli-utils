package fi.jonne.javacliutils.core;

public interface ICommands {
	public boolean isCommand(String[] args);
	public String getInputStringFromArgs(String[] args);
	public boolean isAuthorized(String sender);
	public void execute(String channel, String sender, String args[]);
}
