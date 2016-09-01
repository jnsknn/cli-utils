package fi.jonne.javacliutils.utils;

import java.util.HashMap;

public class TimerInfoContainer {
	
	private static TimerInfoContainer instance;
	private static HashMap<Integer, TimerInfo> timerInfos;
	
	public TimerInfoContainer(){
		
	}
	
	public static TimerInfoContainer getInstance(){
		if(instance == null){
			instance = new TimerInfoContainer();
			timerInfos = new HashMap<Integer, TimerInfo>();
		}
		return instance;
	}
	
	public void setTimer(TimerInfo timerInfo){
		timerInfos.put(getNextId(), timerInfo);
	}
	
	public TimerInfo getTimer(int id){
		return timerInfos.get(id);
	}
	
	public int getNextId(){
		return timerInfos.size()+1;
	}
	
	public HashMap<Integer, TimerInfo> getTimers(){
		return timerInfos;
	}
	
	public void removeTimer(int id){
		timerInfos.get(id).cancel();
		timerInfos.remove(id);
	}
	
	public boolean isTimer(int id){
		if(timerInfos.get(id) != null){
			return true;
		}else{
			return false;
		}
	}
}
