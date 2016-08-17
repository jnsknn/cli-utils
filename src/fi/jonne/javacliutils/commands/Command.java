package fi.jonne.javacliutils.commands;

import fi.jonne.javacliutils.bots.IRCBot;
import fi.jonne.javacliutils.calculators.Calculator;
import fi.jonne.javacliutils.generators.UfoName;

public class Command {
	
	private static Command instance;
	private ECommands eCommand;
	private String input;
	private String output;
	
	public Command(){
	}
	
	public static Command getInstance(){
		if(instance == null){
			instance = new Command();
		}
		
		return instance;
	}
	
	public void executeCommand(String[] args){
		
		if(validateCommand(args)){
			switch(eCommand){
			case CALCULATE:
				this.output = Calculator.getInstance().calculate(this.input);
				break;
			case UFONAME:
				this.output = new UfoName(this.input).name;
				break;
			case EXIT:
				this.output = "bye!";
				System.exit(0);
				break;
			case IRC:
				IRCBot.getInstance().setBotName(args[1]);
				IRCBot bot = IRCBot.getInstance();
				try{
					bot.setEncoding("UTF-8");
					bot.setVerbose(true);
					bot.connect(args[2]);
					bot.joinChannel(args[3]);
				}catch(Exception e){
					System.err.println("executeCommand(): " + e.getMessage());
				}
				break;
			default:
				this.output = "";
			}
			
			printOutput();
		}
	}
	
	private boolean validateCommand(String[] args){
		
		for(ECommands cmd : ECommands.values()){
			if(cmd.isCommand(args)){
				this.eCommand = cmd;
				this.input = cmd.getInputStringFromArgs(args);
				return true;
			}
		}
		
		return false;
		
	}
	
	private void printOutput(){
		System.out.println(this.output);
	}
}
