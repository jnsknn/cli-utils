package fi.jonne.javacliutils.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerInfo extends TimerTask{
	
	private static final long PERIOD = 1000;
	private static final long DELAY = 0;
	
	private String name;
	private String owner;
	private String channel;
	private long time;
	private Timer timer;
	private TimerTask timerTask;
	private static IRCBot bot;
	private int id;
	
	public TimerInfo(String timerTime, String timerName, String timerOwner, String timerChannel){
		
		this.id = TimerContainer.getInstance().getId()+1;
		this.name = timerName;
		this.owner = timerOwner;
		this.channel = timerChannel;
		
		bot = IRCBot.getInstance();

		if(parseTimeFromTimerString(timerTime)){
		
			this.timer = new Timer();
			this.timerTask = this;
			
			this.timer.scheduleAtFixedRate(this.timerTask, DELAY, PERIOD);
			
			bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has been set");
		}
	}
	
	public TimerInfo(String timerTime, String timerName){
		
		this.id = TimerContainer.getInstance().getId()+1;
		this.name = timerName;
		
		bot = IRCBot.getInstance();
		
		if(parseTimeFromTimerString(timerTime)){
			
			this.timer = new Timer();
			this.timerTask = this;
			
			this.timer.scheduleAtFixedRate(this.timerTask, DELAY, PERIOD);
			
			System.out.println("Your timer [" + this.name + "] has been set");
		}
	}

	private boolean parseTimeFromTimerString(String timerTime){
		try{
			char multiplier = timerTime.toLowerCase().charAt(timerTime.length()-1);
			timerTime = timerTime.substring(0, timerTime.length()-1);
			
			int value = Integer.valueOf(timerTime);
			
			switch(multiplier){
			case 's':
				this.time = value * 1000;
				break;
			case 'm':
				this.time = value * 60 * 1000;
				break;
			case 'h':
				this.time = value * 3600 * 1000;
				break;
			default:
				this.time = 0;
				break;
			}
			return true;
		}catch(NumberFormatException e){
			
			String errorMsg = "Use ?timer [(int)time|h|m|s] [Timer name]";
			
			if(bot.isConnected()){
				bot.sendMessage(this.channel, errorMsg);
			}else{
				System.out.println(errorMsg);				
			}
			
			return false;
		}
	}

	@Override
	public void run() {
		
		this.time -= PERIOD;
		
		if(this.time == 3600 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 1 hour left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 1 hour left");				
			}
		}
		else if(this.time == 1800 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 30 minutes left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 30 minutes left");				
			}
		}
		else if(this.time == 600 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 10 minutes left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 10 minutes left");				
			}
		}
		else if(this.time == 300 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 5 minutes left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 5 minutes left");				
			}
		}
		else if(this.time == 60 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 1 minute left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 1 minute left");				
			}
		}
		else if(this.time == 30 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has 30 seconds left");
			}else{
				System.out.println("Your timer [" + this.name + "] has 30 seconds left");				
			}
		}
		else if(this.time <= 0){
			
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has finished!");
			}else{
				System.out.println("Your timer [" + this.name + "] has finished!");				
			}
			
			this.timer.cancel();
			
			TimerContainer.getInstance().removeTimer(this.id);
		}
	}

}
