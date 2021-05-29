package fi.jnsknn.cliutils.ao;

import fi.jnsknn.cliutils.core.Communicator;
import fi.jnsknn.cliutils.core.ICommands;

/**
 * This enum contains all Anarchy Online commands and their implementations
 **/
public enum EAOCommands implements ICommands {
	LAIVA {
		@Override
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance().handleOutput(channel, sender, "/fxscript Tweak_Rubi-Ka_Sunlight #");
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 2; i < args.length; i++) {
				sb.append(args[i] + " ");
			}

			return sb.toString().trim();
		}

		@Override
		public boolean isAuthorized(String sender) {
			return true;
		}

		@Override
		public boolean isCommand(String[] args) {
			return args[0].toLowerCase().startsWith("laiva");
		}
	};
}
