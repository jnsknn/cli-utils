package fi.jonne.javacliutils.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import fi.jonne.javacliutils.core.utils.IRCBot;
import fi.jonne.javacliutils.core.utils.Playlist;
import fi.jonne.javacliutils.settings.Settings;

public class Communicator {
	
	private static Communicator instance;
	
	public Communicator(){}
	
	public static Communicator getInstance(){
		if(instance == null){
			instance = new Communicator();
		}
		return instance;
	}
	
	/**
	 * Send message as bot to all joined channels
	 * **/
	public void sendMessage(String message){
		try{
			if(IRCBot.getInstance().isConnected()){				
				for(String ch : IRCBot.getInstance().getChannels()){					
					handleOutput(ch, IRCBot.getInstance().getName(), message);
					printOutput(ch, Settings.currentLocalSender, message);
				}
			}
		}catch(Exception e){
			handleError(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "sendMessage() error: " + e.getMessage());
		}
	}
	
	/**
	 * Send output to channel if bot is connected, system output if not
	 * **/
	public void handleOutput(String channel, String sender, String output){
		
		String timeStamp = getTimeStamp();
		
		if(output.length() > 0 && output != null){			
			if(IRCBot.getInstance().isConnected()){
				IRCBot.getInstance().sendMessage(channel, output);
			}else{
				System.out.println("[" + timeStamp + "] " + channel + " " + sender + " | " + output);			
			}
		}
	}
	
	/**
	 * Analyze input for executing commands
	 * **/
	public void handleInput(String channel, String sender, String input){
				
		input = input.trim();
		
		if(input.length() < 1 || input == null || input == "")
			return;
		
		String inputStringArr[] = input.split(":");
		
		// Handle commands
		if(input.substring(0, 1).equalsIgnoreCase("?")){
			input = input.substring(1);
			String[] args = input.split(" ");
			
			Command cmd = new Command();
			cmd.executeCommand(channel, sender, args);
			
		}
		// Handle commands from input string starting with ":"
		else if(inputStringArr.length > 1 && input.split(":")[1].trim().substring(0, 1).equalsIgnoreCase("?")){
			
			input = input.split(":")[1].trim().substring(1);
			String[] args = input.split(" ");
			
			Command cmd = new Command();
			cmd.executeCommand(channel, sender, args);
			
		}
		
		// Parse all urls from input string to form youtube playlist
		if(input.contains("https://")){
			String inputStrings[] = input.split(" ");
			
			for(String str : inputStrings){
				if(str.contains("https://")){					
					Playlist.getInstance().addPlaylist(str);
				}
			}
		}
	}
	
	/**
	 * Print error to system error
	 * **/
	public void handleError(String channel, String sender, String error){

		String timeStamp = getTimeStamp();
		
		System.err.println("[" + timeStamp + "] " + channel + " " + sender + " ! " + error);
	}
	
	/**
	 * Print output to system output
	 * **/
	public void printOutput(String channel, String sender, String output){
		
		String timeStamp = getTimeStamp();
		
		System.out.println("[" + timeStamp + "] " + channel + " " + sender + " | " + output);
	}
	
	/**
	 * Print input to system output
	 * **/
	public void printInput(){
		
		String timeStamp = getTimeStamp();

		String identifier = IRCBot.getInstance().isConnected() ? "#" : "?";
		System.out.print("[" + timeStamp + "] " + Settings.LOCAL_CHANNEL + " " + Settings.currentLocalSender + " " + identifier + " ");
	}
	
	private String getTimeStamp(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(date);
	}
}
