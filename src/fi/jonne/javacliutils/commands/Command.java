package fi.jonne.javacliutils.commands;

import fi.jonne.javacliutils.bots.IRCBot;
import fi.jonne.javacliutils.calculators.Calculator;
import fi.jonne.javacliutils.generators.UfoName;

public class Command {
	
	private ECommands eCommand;
	private String input;
	private String output;
	
	public Command(){
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
				IRCBot.getInstance().disconnect();
				System.exit(0);
				break;
			case IRC:
				IRCBot.getInstance().setBotName(args[1]);
				IRCBot bot = IRCBot.getInstance();
				try{
					bot.setVerbose(true);
					bot.setEncoding("UTF-8");
					System.out.println("Connecting to " + args[2] + "...");
					
					bot.connect(args[2]);
					
					System.out.println("[OK]");
					System.out.println("Joining channel " + args[3] + "...");
					bot.joinChannel(args[3]);
					System.out.println("[OK]");
					
					if(bot.isConnected()){
						bot.setVerbose(false);
					}
					
				}catch(Exception e){
					System.err.println("[IRC ERROR]" + e.getMessage());
				}
				break;
			default:
				this.output = "";
			}
			
			if(!IRCBot.getInstance().isConnected()){
				printOutput();
			}
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
	
	public String getOutput(){
		return this.output;
	}
	
	private void printOutput(){
		System.out.println(this.output);
	}
}
