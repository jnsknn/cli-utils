package fi.jonne.javacliutils.core;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import fi.jonne.javacliutils.core.utils.Calculator;
import fi.jonne.javacliutils.core.utils.IRCBot;
import fi.jonne.javacliutils.core.utils.Playlist;
import fi.jonne.javacliutils.core.utils.TimerInfo;
import fi.jonne.javacliutils.core.utils.TimerInfoContainer;
import fi.jonne.javacliutils.core.utils.UfoName;
import fi.jonne.javacliutils.settings.Settings;

/**
 * This enum contains all core commands and their implementations
 * **/
public enum ECommands implements ICommands {
	CALCULATE {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("calc") && args.length == 2){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance()
			.handleOutput(channel, sender, Calculator.getInstance()
					.calculate(getInputStringFromArgs(args)));
		}
	},
	UFONAME {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("ufo") && args.length >= 2){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance()
			.handleOutput(channel, sender, new UfoName(getInputStringFromArgs(args)).name);
		}
	},
	EXIT {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("exit")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			for(EAuth auth : EAuth.values()){
				if(auth.toString().equalsIgnoreCase(sender)){
					return true;
				}
			}
			return false;
		}
		public void execute(String channel, String sender, String[] args) {
			if(IRCBot.getInstance().isConnected()){					
				IRCBot.getInstance().quitServer("Disconnecting...");
			}else{
				System.exit(0);
			}
		}
	},
	IRC {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("irc") && args.length == 4){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			for(EAuth auth : EAuth.values()){
				if(auth.toString().equalsIgnoreCase(sender)){
					return true;
				}
			}
			return false;
		}
		public void execute(String channel, String sender, String[] args) {
			if(!IRCBot.getInstance().isConnected()){
				
				IRCBot.getInstance().setBotName(args[1]);
				Settings.currentLocalSender = args[1];
				
				try{
					IRCBot.getInstance().setVerbose(true);
					IRCBot.getInstance().setEncoding("UTF-8");
					
					Communicator.getInstance()
					.printOutput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "Connecting to " + args[2] + "...");
					
					IRCBot.getInstance().connect(args[2]);
					
					Communicator.getInstance()
					.printOutput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "[OK]");
					Communicator.getInstance()
					.printOutput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "Joining channel " + args[3] + "...");
					IRCBot.getInstance().joinChannel(args[3]);
					Communicator.getInstance()
					.printOutput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "[OK]");
					
					if(IRCBot.getInstance().isConnected()){
						IRCBot.getInstance().setVerbose(false);
					}
					
				}catch(UnknownHostException e){
					Settings.currentLocalSender = Settings.LOCAL_SENDER;
					Communicator.getInstance().handleError(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "Unknown host: " + e.getMessage());
				} catch (NickAlreadyInUseException e) {
					Settings.currentLocalSender = Settings.LOCAL_SENDER;
					Communicator.getInstance().handleError(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "Nick already in use: " + e.getMessage());
				} catch (IOException e) {
					Settings.currentLocalSender = Settings.LOCAL_SENDER;
					Communicator.getInstance().handleError(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "IO Error: " + e.getMessage());
				} catch (IrcException e) {
					Settings.currentLocalSender = Settings.LOCAL_SENDER;
					Communicator.getInstance().handleError(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "IRC Error: " + e.getMessage());
				}
			}
		}
	},
	TIMER {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("ti") && args.length >= 1){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			if(args.length > 1){
				
				if(!IRCBot.getInstance().isConnected()){
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), false);						
				}else{
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), sender, channel, false);
				}
				
			}else if(args.length == 1){
				
				if(TimerInfoContainer.getInstance().getTimers().size() < 1){
					Communicator.getInstance().handleOutput(channel, sender, "No timers set");
				}else{						
					String timers = "";
					
					for(Map.Entry<Integer, TimerInfo> timer : TimerInfoContainer.getInstance().getTimers().entrySet()){
						timers += ">>Timer [" + timer.getValue().id + "] [" + timer.getValue().name + "] for " + timer.getValue().owner + " has " + timer.getValue().parseTimeStringFromTime(timer.getValue().time, false) + " remaining";
						
						if(timer.getValue().isTimerRepeating){
							timers += " and is repeated every " + timer.getValue().parseTimeStringFromTime(timer.getValue().timeStampEnd - timer.getValue().timeStampStart, true) + "!<<";
						}else{
							timers += "!<<";
						}
					}
					Communicator.getInstance().handleOutput(channel, sender, timers);
				}
			}
		}
	},
	PTIMER {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("pti") && args.length >= 1){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			if(args.length > 1){
				
				if(!IRCBot.getInstance().isConnected()){
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), false);						
				}else{
					
					// This timer is personal, so set channel as sender!
					new TimerInfo(args[1], "0s", getInputStringFromArgs(args), sender, sender, false);
				}
			}
		}
	},
	RMTIMER {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("rmti") && args.length == 2){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			
			int id = Integer.valueOf(getInputStringFromArgs(args));
			
			if(TimerInfoContainer.getInstance().isTimerExist(id)){
				TimerInfo timer = TimerInfoContainer.getInstance().getTimer(id);				
				Communicator.getInstance().handleOutput(timer.channel, timer.owner, timer.owner + ", your timer [" + timer.id + "] [" + timer.name + "] has been removed!");
				TimerInfoContainer.getInstance().removeTimer(timer);
			}else{
				Communicator.getInstance().handleOutput(channel, sender, "No timer [" + String.valueOf(id)+ "] found");
			}
		}
	},
	RTIMER {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("rti") && args.length >= 3){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 3; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			
			if(!IRCBot.getInstance().isConnected()){
				new TimerInfo(args[1], args[2], getInputStringFromArgs(args), true);						
			}else{
				new TimerInfo(args[1], args[2], getInputStringFromArgs(args), sender, channel, true);
			}
		}
	},
	HELP {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("help")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance().handleOutput(channel, sender, "https://github.com/jnsknn/java-cli-utils/blob/master/README.md");
		}
	},
	PLAYLIST {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("pl")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			if(args.length == 1){				
				Communicator.getInstance().handleOutput(channel, sender, Playlist.getInstance().getPlaylistURL());
			}
		}
	},
	CLRPLAYLIST {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("clrpl")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			if(args.length == 1){
				Playlist.getInstance().clearPlaylist();
				Communicator.getInstance().handleOutput(channel, sender, "Playlist cleared");
			}
		}
	};
}
