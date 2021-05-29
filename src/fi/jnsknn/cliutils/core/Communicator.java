package fi.jnsknn.cliutils.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import fi.jnsknn.cliutils.core.utils.IRCBot;
import fi.jnsknn.cliutils.core.utils.Playlist;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

public class Communicator {

	private static Communicator instance;

	public static Communicator getInstance() {
		if (instance == null) {
			instance = new Communicator();
		}
		return instance;
	}

	private String createTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(date);
	}

	/**
	 * Returns identifier
	 *
	 * @return "#" if IRCBot is connected
	 * @return "?" if IRCBot is not connected
	 **/
	public String getIdentifier() {
		return IRCBot.getInstance().isConnected() ? "#" : "?";
	}

	/**
	 * Print error to system error
	 **/
	public void handleError(String channel, String sender, String error) {
		System.err.println("[" + createTimeStamp() + "] " + channel + " " + sender + " " + getIdentifier() + error);
	}

	/**
	 * Handle input for executing commands
	 **/
	public void handleInput(String channel, String sender, String input) {

		if (input == null) {
			return;
		}

		input = input.trim();

		if (input.isEmpty()) {
			return;
		}

		String[] inputStringArr = input.split(":");

		// Handle commands
		if (input.startsWith("?")) {
			input = input.substring(1);
			String[] args = input.split(" ");

			Commander cmd = new Commander();
			cmd.executeCommand(channel, sender, args);
		}
		// Handle commands from input string starting with ":"
		else if (inputStringArr.length > 1 && input.split(":")[1].trim().startsWith("?")) {

			input = input.split(":")[1].trim().substring(1);
			String[] args = input.split(" ");

			Commander cmd = new Commander();
			cmd.executeCommand(channel, sender, args);
		}

		// Parse all urls from input string to form youtube playlist
		if (input.contains("https://")) {
			String[] inputStrings = input.split(" ");
			for (String str : inputStrings) {
				if (str.contains("https://")) {
					Playlist.getInstance().addPlaylist(str);
				}
			}
		}
	}

	/**
	 * Send output to channel if bot is connected, system output if not
	 **/
	public void handleOutput(String channel, String sender, String output) {

		String timeStamp = createTimeStamp();

		if (output != null && output.length() > 0) {
			if (IRCBot.getInstance().isConnected()) {
				IRCBot.getInstance().sendMessage(channel, output);
			} else {
				System.out.println("[" + timeStamp + "] " + channel + " " + sender + " " + getIdentifier() + output);
			}
		}
	}

	/**
	 * Print input to system output
	 **/
	public void printInput() {
		System.out.print("[" + createTimeStamp() + "] " + Constants.LOCAL_CHANNEL + " "
				+ Settings.getCurrentLocalSender() + " " + getIdentifier());
	}

	/**
	 * Print output to system output
	 **/
	public void printOutput(String channel, String sender, String output) {
		System.out.println("[" + createTimeStamp() + "] " + channel + " " + sender + " " + getIdentifier() + output);
	}

	/**
	 * Send message as bot to all joined channels
	 **/
	public void sendMessage(String message) {
		try {
			if (IRCBot.getInstance().isConnected()) {
				for (String ch : IRCBot.getInstance().getChannels()) {
					handleOutput(ch, IRCBot.getInstance().getName(), message);
					printOutput(ch, Settings.getCurrentLocalSender(), message);
				}
			}
		} catch (Exception e) {
			handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"sendMessage() error: " + e.getMessage());
		}
	}
}
