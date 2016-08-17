package fi.jonne.javacliutils.bots;

import org.jibble.pircbot.PircBot;

public class IRCBot extends PircBot{
	
	static IRCBot instance;
	
	public IRCBot(){
	}
	
	public static IRCBot getInstance(){
		if(instance == null){
			instance = new IRCBot();
		}
		return instance;
	}
	
	public void onMessage(String channel, String sender,
            String login, String hostname, String message) {
		
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
	}
	
	public void setBotName(String botName){
		this.setName(botName);
	}
}
