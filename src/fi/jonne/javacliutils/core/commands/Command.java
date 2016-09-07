package fi.jonne.javacliutils.core.commands;

import java.util.Map;

import fi.jonne.javacliutils.core.utils.Calculator;
import fi.jonne.javacliutils.core.utils.IRCBot;
import fi.jonne.javacliutils.core.utils.TimerInfo;
import fi.jonne.javacliutils.core.utils.TimerInfoContainer;
import fi.jonne.javacliutils.core.utils.UfoName;

public class Command {
	
	protected ECommands eCommand;
	protected String input;
	protected String output;
	protected static IRCBot bot;
	
	protected String sender;
	protected String channel;
	
	public Command(){
		bot = IRCBot.getInstance();
	}
	
	public void executeCommand(String[] args){
		
		if(validateCommand(args)){
			switch(eCommand){
			case CALCULATE:
				output = input + " = " + Calculator.getInstance().calculate(input);
				break;
			case UFONAME:
				output = new UfoName(input).name;
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
					
					TimerInfo timer;
					
					if(!bot.isConnected()){
						timer = new TimerInfo(args[1], input);						
					}else{
						timer = new TimerInfo(args[1], input, sender, channel);
					}
					
					//Check if timer thread started correctly
					if(timer.isTimerRunning){
						TimerInfoContainer.getInstance().setTimer(timer);
					}
					
				}else if(args.length == 1){
					
					if(TimerInfoContainer.getInstance().getTimers().size() < 1){
						output = "No timers set. Use ?timer [(int)time (char)h/m/s] [timer name] to set a timer";
					}else{						
						String timers = "";
						
						for(Map.Entry<Integer, TimerInfo> timer : TimerInfoContainer.getInstance().getTimers().entrySet()){
							
							timers += ">>Timer [" + timer.getValue().id + "] [" + timer.getValue().name + "] for " + timer.getValue().owner + " has " + timer.getValue().parseTimeStringFromTime() + " remaining!<<";
							
						}
						
						output = timers;
					}
					
				}
				break;
			case RMTIMER:
					if(TimerInfoContainer.getInstance().isTimerExist(Integer.valueOf(input))){
						TimerInfo timer = TimerInfoContainer.getInstance().getTimer(Integer.valueOf(input));
						timer.isTimerRunning = false;
					}else{
						output = "No timer " + input + " found. Use ?rmtimer [(int)id] to remove a timer";
					}
				break;
			default:
				output = "";
			}
			
			if(!bot.isConnected() && output != null){
				printOutput();
			}
		}
	}
	
	private boolean validateCommand(String[] args){
		
		for(ECommands cmd : ECommands.values()){
			if(cmd.isCommand(args)){
				eCommand = cmd;
				input = cmd.getInputStringFromArgs(args);
				return true;
			}
		}
		
		return false;
		
	}
	
	public String getOutput(){
		return output;
	}
	
	public void setOutput(String outputStr){
		output = outputStr;
	};
	
	private void printOutput(){
		System.out.println(output);
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
