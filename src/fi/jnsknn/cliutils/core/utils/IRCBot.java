package fi.jnsknn.cliutils.core.utils;

import org.jibble.pircbot.PircBot;

import fi.jnsknn.cliutils.core.Communicator;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

public class IRCBot extends PircBot {

	private static IRCBot instance;

	private IRCBot() {
	}

	public static IRCBot getInstance() {
		if (instance == null) {
			instance = new IRCBot();
		}
		return instance;
	}

	@Override
	public void onDisconnect() {
		Settings.setCurrentLocalSender(Constants.LOCAL_SENDER);
		Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
				"[IRCBot disconnected]");
		getInstance().dispose();
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {

		Communicator.getInstance().handleInput(channel, sender, message);
		Communicator.getInstance().printOutput(channel, sender, message);
	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname, String message) {

		Communicator.getInstance().handleInput(sender, sender, message);
		Communicator.getInstance().printOutput("PRIVATE", sender, message);

	}

	public void setBotName(String botName) {
		this.setName(botName);
	}
}
