package fi.jonne.javacliutils.utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TimerInfo extends TimerTask{
	
	public static final long PERIOD = 1000;
	public static final long DELAY = 0;
	public static final HashMap<String, Integer> timeMultipliers = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;{
		put("h", 3600000);
		put("m", 60000);
		put("s", 1000);
	}};
	
	public String name;
	public String owner;
	public String channel;
	public long time;
	private Timer timer;
	private TimerTask timerTask;
	private static IRCBot bot;
	public int id;
	
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
			
			bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has been set for [" + timerTime + "]!");
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
			
			System.out.println("Your timer [" + this.name + "] has been set for [" + timerTime + "]!");
		}
	}

	private boolean parseTimeFromTimerString(String timerTime){
		try{
			
			final int strLength = timerTime.length();
			int hours = 0, minutes = 0, seconds = 0;
			char hms;
			String timerTimeTemp = timerTime;
			
			for(int i = 0;i < strLength; i++){
				
				hms = timerTime.charAt(i);
				
				switch(hms){
				case 'h':
					String[] hourArr = timerTimeTemp.split("h");
					if(hourArr.length > 1){
						timerTimeTemp = hourArr[1];					
					}
					hours += Integer.valueOf(hourArr[0]);
					break;
				case 'm':
					String[] minArr = timerTimeTemp.split("m");
					if(minArr.length > 1){
						timerTimeTemp = minArr[1];					
					}
					minutes += Integer.valueOf(minArr[0]);
					break;
				case 's':
					String[] secArr = timerTimeTemp.split("s");
					if(secArr.length > 1){
						timerTimeTemp = secArr[1];						
					}
					seconds += Integer.valueOf(secArr[0]);
					break;
				default:
					break;
				}
			}
			
			this.time = (
					hours*timeMultipliers.get("h")+
					minutes*timeMultipliers.get("m")+
					seconds*timeMultipliers.get("s")
				);
			
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
