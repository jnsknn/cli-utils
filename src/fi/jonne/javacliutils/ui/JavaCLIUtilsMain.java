package fi.jonne.javacliutils.ui;

import fi.jonne.javacliutils.bots.IRCBot;
import fi.jonne.javacliutils.commands.Command;

public class JavaCLIUtilsMain {
	
	public static String input;
	
	public static void main(String[] args) {
		
		while(true){
			input = System.console().readLine();
			handleInput();
		}
		
	}
	
	private static void handleInput(){
		
		if(!input.startsWith("!")){
			try{
				IRCBot bot = IRCBot.getInstance();
				bot.sendMessage(bot.getChannels()[0], input);
			}catch(Exception e){
				System.err.println("handleInput() error: " + e.getMessage());
			}
		}
		
		input = input.substring(1);
		
		String[] args = input.split(" ");
		
		Command.getInstance().executeCommand(args);
		
	}
}
