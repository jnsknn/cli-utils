package fi.jonne.javacliutils.core.utils;

import org.jibble.pircbot.PircBot;

import fi.jonne.javacliutils.core.Communicator;
import fi.jonne.javacliutils.settings.Settings;

public class IRCBot extends PircBot{
	
	private static IRCBot instance;
	
	public IRCBot(){}
	
	public static IRCBot getInstance(){
		if(instance == null){
			instance = new IRCBot();
		}
		return instance;
	}
	
	public void onDisconnect(){
		Settings.currentLocalSender = Settings.LOCAL_SENDER;
		Communicator.getInstance().handleOutput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "IRCBot disconnected!");
		getInstance().dispose();
		instance = null;
	}
	
	public void onMessage(String channel, String sender,
            String login, String hostname, String message) {
		
		Communicator.getInstance().handleInput(channel, sender, message);
		Communicator.getInstance().printOutput(channel, sender, message);
	}
	
	public void setBotName(String botName){
		this.setName(botName);
	}
}
