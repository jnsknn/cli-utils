package fi.jonne.javacliutils.calculators;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {
	
	private static Calculator instance;
	private static ScriptEngine engine;
	
	public Calculator(){
	}
	
	public static Calculator getInstance(){
		
		final ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("JavaScript");
		
		if(instance == null){
			instance = new Calculator();
		}
		
		return instance;
	}
	
	public String calculate(String input){
		try{
			String result = String.valueOf(engine.eval(input));
		    return result;
	    }catch(ScriptException e){
	    	return "calculate() error: " + e.getMessage();
	    }
	}
}
