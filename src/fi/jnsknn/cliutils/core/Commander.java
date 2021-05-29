package fi.jnsknn.cliutils.core;

import fi.jnsknn.cliutils.ao.EAOCommands;

public class Commander {

	public void executeCommand(String channel, String sender, String[] args) {
		for (ECommands cmd : ECommands.values()) {
			if (cmd.isCommand(args) && cmd.isAuthorized(sender)) {
				cmd.execute(channel, sender, args);
				return;
			}
		}
		for (EAOCommands cmd : EAOCommands.values()) {
			if (cmd.isCommand(args) && cmd.isAuthorized(sender)) {
				cmd.execute(channel, sender, args);
				return;
			}
		}
	}
}
