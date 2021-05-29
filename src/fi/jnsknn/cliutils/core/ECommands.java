package fi.jnsknn.cliutils.core;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import fi.jnsknn.cliutils.core.utils.Calculator;
import fi.jnsknn.cliutils.core.utils.IRCBot;
import fi.jnsknn.cliutils.core.utils.Playlist;
import fi.jnsknn.cliutils.core.utils.TimerInfo;
import fi.jnsknn.cliutils.core.utils.TimerInfoContainer;
import fi.jnsknn.cliutils.core.utils.api.WeirdNameGenerator;
import fi.jnsknn.cliutils.core.utils.api.impl.WeirdNameGeneratorImpl;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

/**
 * This enum contains all core commands and their implementations
 **/
public enum ECommands implements ICommands {
	CALCULATE {
		@Override
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance().handleOutput(channel, sender,
					Calculator.getInstance().calculate(getInputStringFromArgs(args)));
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i < args.length; i++) {
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
			return args[0].toLowerCase().startsWith("calc") && args.length == 2;
		}
	},
	UFONAME {
		@Override
		public void execute(String channel, String sender, String[] args) {
			WeirdNameGenerator wng = new WeirdNameGeneratorImpl(getInputStringFromArgs(args));
			Communicator.getInstance().handleOutput(channel, sender, wng.generate().getFixedName());
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i < args.length; i++) {
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
			return args[0].toLowerCase().startsWith("ufo") && args.length >= 2;
		}
	},
	EXIT {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (IRCBot.getInstance().isConnected()) {
				IRCBot.getInstance().quitServer("Disconnecting...");
			} else {
				System.exit(0);
			}
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}

			return sb.toString().trim();
		}

		@Override
		public boolean isAuthorized(String sender) {
			for (EAuth auth : EAuth.values()) {
				if (auth.toString().equalsIgnoreCase(sender)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isCommand(String[] args) {
			return args[0].toLowerCase().startsWith("exit");
		}
	},
	IRC {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (!IRCBot.getInstance().isConnected()) {

				IRCBot.getInstance().setBotName(args[1]);
				Settings.setCurrentLocalSender(args[1]);

				try {
					IRCBot.getInstance().setVerbose(true);
					IRCBot.getInstance().setEncoding("UTF-8");

					Communicator.getInstance().printOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"Connecting to " + args[2] + "...");

					IRCBot.getInstance().connect(args[2]);

					Communicator.getInstance().printOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"[OK]");
					Communicator.getInstance().printOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"Joining channel " + args[3] + "...");
					IRCBot.getInstance().joinChannel(args[3]);
					Communicator.getInstance().printOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"[OK]");

					if (IRCBot.getInstance().isConnected()) {
						IRCBot.getInstance().setVerbose(false);
					}

				} catch (UnknownHostException e) {
					Settings.setCurrentLocalSender(Constants.LOCAL_SENDER);
					Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"Unknown host: " + e.getMessage());
				} catch (NickAlreadyInUseException e) {
					Settings.setCurrentLocalSender(Constants.LOCAL_SENDER);
					Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"Nick already in use: " + e.getMessage());
				} catch (IOException e) {
					Settings.setCurrentLocalSender(Constants.LOCAL_SENDER);
					Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"IO Error: " + e.getMessage());
				} catch (IrcException e) {
					Settings.setCurrentLocalSender(Constants.LOCAL_SENDER);
					Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
							"IRC Error: " + e.getMessage());
				}
			}
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}

			return sb.toString().trim();
		}

		@Override
		public boolean isAuthorized(String sender) {
			for (EAuth auth : EAuth.values()) {
				if (auth.toString().equalsIgnoreCase(sender)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isCommand(String[] args) {
			return args[0].toLowerCase().startsWith("irc") && args.length == 4;
		}
	},
	TIMER {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (args.length > 1) {

				if (!IRCBot.getInstance().isConnected()) {
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), false);
				} else {
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), sender, channel, false);
				}

			} else if (args.length == 1) {

				if (TimerInfoContainer.getInstance().getTimers().size() < 1) {
					Communicator.getInstance().handleOutput(channel, sender, "No timers set");
				} else {
					StringBuilder timers = new StringBuilder();

					for (Map.Entry<Integer, TimerInfo> timer : TimerInfoContainer.getInstance().getTimers()
							.entrySet()) {
						timers.append(">>Timer [" + timer.getValue().getId() + "] [" + timer.getValue().getName()
								+ "] for " + timer.getValue().getOwner() + " has "
								+ timer.getValue().parseTimeStringFromTime(timer.getValue().getTime(), false)
								+ " remaining");

						if (timer.getValue().isTimerRepeating()) {
							timers.append(" and is repeated every " + timer.getValue().parseTimeStringFromTime(
									timer.getValue().getTimeStampEnd() - timer.getValue().getTimeStampStart(), true)
									+ "!<<");
						} else {
							timers.append("!<<");
						}
					}
					Communicator.getInstance().handleOutput(channel, sender, timers.toString());
				}
			}
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
			return args[0].toLowerCase().startsWith("ti") && args.length >= 1;
		}
	},
	PTIMER {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (args.length > 1) {

				if (!IRCBot.getInstance().isConnected()) {
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), false);
				} else {

					// This timer is personal, so set channel as sender!
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), sender, sender, false);
				}
			}
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
			return args[0].toLowerCase().startsWith("pti") && args.length >= 1;
		}
	},
	RMTIMER {
		@Override
		public void execute(String channel, String sender, String[] args) {

			int id = Integer.parseInt(getInputStringFromArgs(args));

			if (TimerInfoContainer.getInstance().isTimerExist(id)) {
				TimerInfo timer = TimerInfoContainer.getInstance().getTimer(id);
				Communicator.getInstance().handleOutput(timer.getChannel(), timer.getOwner(), timer.getOwner()
						+ ", your timer [" + timer.getId() + "] [" + timer.getName() + "] has been removed!");
				TimerInfoContainer.getInstance().removeTimer(timer);
			} else {
				Communicator.getInstance().handleOutput(channel, sender, "No timer [" + id + "] found");
			}
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i < args.length; i++) {
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
			return args[0].toLowerCase().startsWith("rmti") && args.length == 2;
		}
	},
	RTIMER {
		@Override
		public void execute(String channel, String sender, String[] args) {

			if (!IRCBot.getInstance().isConnected()) {
				new TimerInfo(args[1], args[2], getInputStringFromArgs(args), true);
			} else {
				new TimerInfo(args[1], args[2], getInputStringFromArgs(args), sender, channel, true);
			}
		}

		@Override
		public String getInputStringFromArgs(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 3; i < args.length; i++) {
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
			return args[0].toLowerCase().startsWith("rti") && args.length >= 3;
		}
	},
	HELP {
		@Override
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance().handleOutput(channel, sender,
					"https://github.com/jnsknn/cli-utils/blob/master/README.md");
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
			return args[0].toLowerCase().startsWith("help");
		}
	},
	PLAYLIST {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (args.length == 1) {
				Communicator.getInstance().handleOutput(channel, sender, Playlist.getInstance().getPlaylistURL());
			}
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
			return args[0].toLowerCase().startsWith("pl");
		}
	},
	CLRPLAYLIST {
		@Override
		public void execute(String channel, String sender, String[] args) {
			if (args.length == 1) {
				Playlist.getInstance().clearPlaylist();
				Communicator.getInstance().handleOutput(channel, sender, "Playlist cleared");
			}
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
			return args[0].toLowerCase().startsWith("clrpl");
		}
	};
}
