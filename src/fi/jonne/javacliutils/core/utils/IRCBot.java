package fi.jonne.javacliutils.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jibble.pircbot.PircBot;

import fi.jonne.javacliutils.core.Command;
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
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String timeStamp = sdf.format(date);
		
		System.out.println( "[" + timeStamp + "] " + channel + " " + sender + "|" + message);
		
			if(message.startsWith("?")){
				
				message = message.substring(1);
				String[] args = message.split(" ");
				
				// Let IRCBot take care who to serve by setting sender and channel for communicator
				Communicator.getInstance().setSender(sender);
				Communicator.getInstance().setChannel(channel);
				
				Command cmd = new Command();
				cmd.executeCommand(args);
				
			}else if(message.split(":")[1].trim().startsWith("?")){
				
				message = message.split(":")[1].trim().substring(1);
				String[] args = message.split(" ");
				
				Communicator.getInstance().setSender(sender);
				Communicator.getInstance().setChannel(channel);
				
				Command cmd = new Command();
				cmd.executeCommand(args);
				
			}
	}
	
	public void setBotName(String botName){
		this.setName(botName);
	}
}
