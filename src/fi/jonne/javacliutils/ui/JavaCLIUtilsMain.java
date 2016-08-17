package fi.jonne.javacliutils.ui;

import fi.jonne.javacliutils.commands.Command;

public class JavaCLIUtils {
	
	public static String input;
	
	public static void main(String[] args) {
		
		while(true){
			input = System.console().readLine();
			handleInput();
		}
		
	}
	
	private static void handleInput(){
		
		if(!input.startsWith("!")){
			return;
		}
		
		input = input.substring(1);
		
		String[] args = input.split(" ");
		
		Command.getInstance().executeCommand(args);
		
	}
}
