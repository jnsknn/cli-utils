package fi.jnsknn.cliutils.main;

import java.util.Scanner;

import fi.jnsknn.cliutils.core.Communicator;
import fi.jnsknn.cliutils.core.utils.IRCBot;
import fi.jnsknn.cliutils.core.utils.TimerInfoContainer;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

public class CliUtilsMain {

	private static volatile boolean isRunning = true;

	private static boolean isRunning(Scanner scanner) {
		if (!isRunning) {
			return false;
		}
		return scanner.hasNextLine();
	}

	public static void main(String[] args) {

		System.out.println("CliUtils#main#isRunning: " + isRunning);

		initialize();

		try (Scanner scanner = new Scanner(System.in);) {
			while (isRunning(scanner)) {
				if (IRCBot.getInstance().isConnected()) {
					// Handle input as messages to be sent on all joined channels
					Communicator.getInstance().sendMessage(scanner.nextLine());
				} else {
					// Handle input as commands
					Communicator.getInstance().handleInput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"?" + scanner.nextLine());
				}
			}
		} catch (Exception e) {
			isRunning = false;
			System.err.println("CliUtils#main: " + e.getMessage());
		}

		System.out.println("CliUtils#main#isRunning: " + isRunning);
	}

	private static void initialize() {
		TimerInfoContainer.getInstance().initializeTimers();
		Communicator.getInstance().printOutput(Constants.LOCAL_CHANNEL, Constants.LOCAL_SENDER, "[READY]");
	}
}
