package fi.jonne.javacliutils.utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerInfo extends TimerTask{
	
	public static final long PERIOD = 1000;
	public static final long DELAY = 0;
	public static final HashMap<String, Integer> timeMultipliers = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;{
		put("h", 3600000);
		put("m", 60000);
		put("s", 1000);
	}};
	
	public String name = "timer";
	public String owner = "you";
	public String channel;
	private long time;
	private Timer timer;
	private TimerTask timerTask;
	private static IRCBot bot;
	public int id;
	public boolean isTimerRunning = false;
	
	/**
	 * Use this constructor for creating public online timers if IRCBot is connected
	 * **/
	public TimerInfo(String timerTime, String timerName, String timerOwner, String timerChannel){
		
		this.id = TimerInfoContainer.getInstance().getNextId();
		
		if(timerName != null && timerName != ""){
			this.name = timerName;			
		}
		
		this.owner = timerOwner;
		this.channel = timerChannel;
		
		bot = IRCBot.getInstance();

		if(parseTimeFromTimerString(timerTime)){
		
			this.timer = new Timer();
			this.timerTask = this;
			
			this.timer.scheduleAtFixedRate(this.timerTask, DELAY, PERIOD);
			this.isTimerRunning = true;
			bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has been set for " + parseTimeStringFromTime() + "!");
		}
	}
	
	/**
	 * Use this constructor for creating offline local timers
	 * **/
	public TimerInfo(String timerTime, String timerName){
		
		this.id = TimerInfoContainer.getInstance().getNextId();
		
		if(timerName != null && timerName != ""){
			this.name = timerName;			
		}
		
		bot = IRCBot.getInstance();
		
		if(parseTimeFromTimerString(timerTime)){
			
			this.timer = new Timer();
			this.timerTask = this;
			
			this.timer.scheduleAtFixedRate(this.timerTask, DELAY, PERIOD);
			this.isTimerRunning = true;
			System.out.println("Your timer [" + this.name + "] has been set for " + parseTimeStringFromTime() + "!");
		}
	}

	private boolean parseTimeFromTimerString(String timerTime){
		try{
			
			final int strLength = timerTime.length();
			long hours = 0, minutes = 0, seconds = 0;
			char hms;
			boolean charFound = false;
			
			String timerTimeTemp = timerTime;
			
			for(int i = 0;i < strLength; i++){
				
				hms = timerTime.charAt(i);
				
				switch(hms){
				case 'h':
					String[] hourArr = timerTimeTemp.split("h");
					if(hourArr.length > 1){
						timerTimeTemp = hourArr[1];					
					}
					hours += Long.valueOf(hourArr[0]);
					charFound = true;
					break;
				case 'm':
					String[] minArr = timerTimeTemp.split("m");
					if(minArr.length > 1){
						timerTimeTemp = minArr[1];					
					}
					minutes += Long.valueOf(minArr[0]);
					charFound = true;
					break;
				case 's':
					String[] secArr = timerTimeTemp.split("s");
					if(secArr.length > 1){
						timerTimeTemp = secArr[1];						
					}
					seconds += Long.valueOf(secArr[0]);
					charFound = true;
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
			
			if(!charFound){
				
				String msg = "Use ?timer [(int)time (char)h|m|s] [timer name] to set a timer";
				
				if(bot.isConnected()){
					bot.sendMessage(this.channel, msg);
				}else{
					System.out.println(msg);				
				}
			}
			
			return charFound;
		}catch(NumberFormatException e){
			
			String errorMsg = "ERROR " + e.getMessage();
			
			if(bot.isConnected()){
				bot.sendMessage(this.channel, errorMsg);
			}else{
				System.out.println(errorMsg);				
			}
			
			return false;
		}
	}
	
	public String parseTimeStringFromTime(){
		String timeString = "";
		
		long timeLeft = this.time;
		
		try{			
			long hoursLeft = TimeUnit.MILLISECONDS.toHours(timeLeft);
			
			long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeLeft-(hoursLeft * timeMultipliers.get("h")));
			
			long secondsLeft = TimeUnit.MILLISECONDS.toSeconds((timeLeft - minutesLeft * timeMultipliers.get("m"))-(hoursLeft * timeMultipliers.get("h")));
			
			if(hoursLeft > 0){
				
				if(hoursLeft == 1){
					timeString += hoursLeft + " hour";				
				}else{
					timeString += hoursLeft + " hours";
				}
			}
			
			if(minutesLeft > 0 && minutesLeft < 60){
				
				if(hoursLeft > 0){
					timeString += " and ";
				}
				
				if(minutesLeft == 1){
					timeString += minutesLeft + " minute";
				}else{
					timeString += minutesLeft + " minutes";
				}
			}
			
			if(secondsLeft > 0 && secondsLeft < 60){
				if(hoursLeft > 0 || minutesLeft > 0){
					timeString += " and ";
				}
				
				if(secondsLeft == 1){
					timeString += secondsLeft + " second";
				}else{
					timeString += secondsLeft + " seconds";
				}
			}
			
			return timeString;
		}catch(NumberFormatException e){
			return e.getMessage();
		}
	}

	@Override
	public void run() {
		
		this.time -= PERIOD;
		
		if(this.time == 3600 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time == 1800 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time == 600 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time == 300 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time == 60 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time == 30 * 1000){
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");
			}else{
				System.out.println("Your timer [" + this.name + "] has " + parseTimeStringFromTime() + " left");				
			}
		}
		else if(this.time <= 0){
			
			if(bot.isConnected()){
				bot.sendMessage(this.channel, this.owner + ", your timer [" + this.name + "] has finished!");
			}else{
				System.out.println("Your timer [" + this.name + "] has finished!");				
			}
			
			this.timer.cancel();
			
			TimerInfoContainer.getInstance().removeTimer(this.id);
		}
	}

}
