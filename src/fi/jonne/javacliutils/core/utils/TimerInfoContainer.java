package fi.jonne.javacliutils.core.utils;

import java.util.HashMap;

public class TimerInfoContainer {
	
	private static TimerInfoContainer instance;
	private static HashMap<Integer, TimerInfo> timerInfos;
	private static int nextId = 0;
	
	public TimerInfoContainer(){}
	
	public static TimerInfoContainer getInstance(){
		if(instance == null){
			instance = new TimerInfoContainer();
			timerInfos = new HashMap<Integer, TimerInfo>();
		}
		return instance;
	}
	
	public void setTimer(TimerInfo timerInfo){
		timerInfos.put(nextId, timerInfo);
	}
	
	public TimerInfo getTimer(int id){
		return timerInfos.get(id);
	}
	
	public int getNextId(){
		nextId += 1;
		return nextId;
	}
	
	public HashMap<Integer, TimerInfo> getTimers(){
		return timerInfos;
	}
	
	public void removeTimer(int id){
		timerInfos.remove(id);
	}
	
	public boolean isTimerExist(int id){
		if(timerInfos.get(id) != null){
			return true;
		}else{
			return false;
		}
	}
}
