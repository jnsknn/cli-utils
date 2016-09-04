package fi.jonne.javacliutils.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fi.jonne.javacliutils.core.commands.Command;
import fi.jonne.javacliutils.core.commands.ECommands;
import fi.jonne.javacliutils.core.utils.IRCBot;

public class JavaCLIUtilsMain {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			handleInput(br.readLine());
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
