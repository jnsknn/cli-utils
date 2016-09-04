package fi.jonne.javacliutils.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jibble.pircbot.PircBot;

import fi.jonne.javacliutils.core.commands.Command;

public class IRCBot extends PircBot{
	
	private static IRCBot instance;
	
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
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String timeStamp = sdf.format(date);
		
		System.out.println( "[" + timeStamp + "] " + channel + " " + sender + "|" + message);
		
			if(message.startsWith("?")){
				
				message = message.substring(1);
				String[] args = message.split(" ");
				
				Command cmd = new Command();
				
				cmd.setSender(sender);
				cmd.setChannel(channel);
				cmd.executeCommand(args);
				
				if(cmd.getOutput() != null)
					sendMessage(channel, cmd.getOutput());
				
			}else if(message.split(":")[1].trim().startsWith("?")){
				
				message = message.split(":")[1].trim().substring(1);
				String[] args = message.split(" ");
				
				Command cmd = new Command();
				
				cmd.setSender(sender);
				cmd.setChannel(channel);
				cmd.executeCommand(args);
				
				if(cmd.getOutput() != null)
					sendMessage(channel, cmd.getOutput());
			}
	}
	
	public void setBotName(String botName){
		this.setName(botName);
	}
}
