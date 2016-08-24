package fi.jonne.javacliutils.commands;

import java.util.Map;

import fi.jonne.javacliutils.utils.Calculator;
import fi.jonne.javacliutils.utils.IRCBot;
import fi.jonne.javacliutils.utils.TimerContainer;
import fi.jonne.javacliutils.utils.TimerInfo;
import fi.jonne.javacliutils.utils.UfoName;

public class Command {
	
	private ECommands eCommand;
	private String input;
	private String output;
	private static IRCBot bot;
	
	private String sender;
	private String channel;
	
	public Command(){
		bot = IRCBot.getInstance();
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
				bot.setBotName(args[1]);
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
			case TIMER:
				if(args.length > 1){
					if(!bot.isConnected()){
						TimerContainer.getInstance().setTimer(new TimerInfo(args[1], this.input));						
					}else{
						TimerContainer.getInstance().setTimer(new TimerInfo(args[1], this.input, this.sender, this.channel));	
					}					
				}else if(!bot.isConnected()){
					for(Map.Entry<Integer, TimerInfo> timer : TimerContainer.getInstance().getTimers().entrySet()){
//					    System.out.printf("Key : %s and Value: %s %n", timer.getKey(), timer.getValue().name);
					}
				}else{
					
				}
				break;
			default:
				this.output = "";
			}
			
			if(!bot.isConnected() && this.output != null){
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
	
	public void setOutput(String output){
		this.output = output;
	};
	
	private void printOutput(){
		System.out.println(this.output);
	}
	
	public void setSender(String sender){
		this.sender = sender;
	}
	
	public String getSender(){
		return this.sender;
	}
	
	public void setChannel(String channel){
		this.channel = channel;
	}
	
	public String getChannel(){
		return this.channel;
	}
}
