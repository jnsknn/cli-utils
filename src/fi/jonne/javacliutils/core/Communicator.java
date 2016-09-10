package fi.jonne.javacliutils.core;

import fi.jonne.javacliutils.core.utils.IRCBot;

public class Communicator {
	
	private static Communicator instance;
	
	protected String sender = "Local";
	protected String channel;
	
	public Communicator(){}
	
	public static Communicator getInstance(){
		if(instance == null){
			instance = new Communicator();
		}
		return instance;
	}
	
	public void handleOutput(String output){
		if(IRCBot.getInstance().isConnected()){
			IRCBot.getInstance().sendMessage(channel, output);
		}else{
			System.out.println(output);			
		}
	}
	
	public void handleError(String error){
		System.err.println(error);
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
