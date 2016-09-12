package fi.jonne.javacliutils.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import fi.jonne.javacliutils.core.utils.IRCBot;
import fi.jonne.javacliutils.core.utils.Playlist;

public class Communicator {
	
	private static Communicator instance;
	
	protected String sender = "JavaCLIUtils";
	protected String channel = "Local";
	
	public Communicator(){}
	
	public static Communicator getInstance(){
		if(instance == null){
			instance = new Communicator();
		}
		return instance;
	}
	
	public void handleOutput(String output){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String timeStamp = sdf.format(date);
		
		if(output.length() > 0 && output != null){			
			if(IRCBot.getInstance().isConnected()){
				IRCBot.getInstance().sendMessage(channel, output);
			}else{
				System.out.println("[" + timeStamp + "] " + channel + " " + sender + " | " + output);			
			}
		}
	}
	
	public void handleInput(String input){
				
		input = input.trim();
		String inputStringArr[] = input.split(":");
		
		if(input.substring(0, 1).equalsIgnoreCase("?")){
			input = input.substring(1);
			String[] args = input.split(" ");
			
			Command cmd = new Command();
			
			cmd.executeCommand(args);
			
		}
		else if(IRCBot.getInstance().isConnected() && input.substring(0, 1).equalsIgnoreCase("#")){
			String[] args = input.split(" ");
			
			channel = args[0];
			
			try{
				handleOutput(ECommands.IRC.getInputStringFromArgs(args));
			}catch(Exception e){
				handleError("handleInput() error: " + e.getMessage());
			}
		}
		else if(inputStringArr.length > 1 && input.split(":")[1].trim().substring(0, 1).equalsIgnoreCase("?")){
			
			input = input.split(":")[1].trim().substring(1);
			String[] args = input.split(" ");
			
			Command cmd = new Command();
			cmd.executeCommand(args);
			
		}

		if(input.contains("https://")){
			String inputStrings[] = input.split(" ");
			
			for(String str : inputStrings){
				if(str.contains("https://")){					
					Playlist.getInstance().addPlaylist(str);
				}
			}
		}
	}
	
	public void handleError(String error){

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String timeStamp = sdf.format(date);
		
		System.err.println("[" + timeStamp + "] " + channel + " " + sender + " | " + error);
	}
	
	public void printOutput(String output){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String timeStamp = sdf.format(date);
		
		System.out.println("[" + timeStamp + "] " + channel + " " + sender + " | " + output);
	}
	
	public void setSender(String senderStr){
		sender = senderStr;
	}
	
	public String getSender(){
		return sender;
	}
	
	public void setChannel(String channelStr){
		channel = channelStr;
	}
	
	public String getChannel(){
		return channel;
	}
}
