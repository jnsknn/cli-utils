package fi.jonne.javacliutils.core.utils;

import org.jibble.pircbot.PircBot;

import fi.jonne.javacliutils.core.Communicator;

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
		System.out.println("IRCBot disconnected!");
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
