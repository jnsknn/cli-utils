package fi.jonne.javacliutils.ui;

import fi.jonne.javacliutils.commands.Command;
import fi.jonne.javacliutils.commands.ECommands;
import fi.jonne.javacliutils.utils.IRCBot;

public class JavaCLIUtilsMain {
	
	public static void main(String[] args) {
		
		while(true){
			handleInput(System.console().readLine());
		}
		
	}
	
	private static void handleInput(String input){
		
		if(input.startsWith("?")){
			input = input.substring(1);
			String[] args = input.split(" ");
			
			Command cmd = new Command();
			
			cmd.executeCommand(args);
			
		}else if(input.startsWith("#")){
			String[] args = input.split(" ");
			
			try{
				IRCBot bot = IRCBot.getInstance();
				bot.sendMessage(args[0], ECommands.IRC.getInputStringFromArgs(args));
			}catch(Exception e){
				System.err.println("handleInput() error: " + e.getMessage());
			}
		}
	}
}
