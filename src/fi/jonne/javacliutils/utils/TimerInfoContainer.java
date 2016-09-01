package fi.jonne.javacliutils.utils;

import java.util.HashMap;

public class TimerContainer {
	
	private static TimerContainer instance;
	private static HashMap<Integer, TimerInfo> timerInfos;
	
	public TimerContainer(){
		
	}
	
	public static TimerContainer getInstance(){
		if(instance == null){
			instance = new TimerContainer();
			timerInfos = new HashMap<Integer, TimerInfo>();
		}
		return instance;
	}
	
	public void setTimer(TimerInfo timerInfo){
		timerInfos.put(getId()+1, timerInfo);
	}
	
	public TimerInfo getTimer(int id){
		return timerInfos.get(id);
	}
	
	public int getId(){
		return timerInfos.size();
	}
	
	public HashMap<Integer, TimerInfo> getTimers(){
		return timerInfos;
	}
	
	public void removeTimer(int id){
		timerInfos.remove(id);
	}
}
