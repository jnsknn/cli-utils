package fi.jnsknn.cliutils.core.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {

	private static Calculator instance;

	private ScriptEngine engine;

	public static Calculator getInstance() {

		if (instance == null) {
			instance = new Calculator(new ScriptEngineManager());
		}

		return instance;
	}

	private Calculator(ScriptEngineManager sem) {
		this.engine = sem.getEngineByName("JavaScript");
	}

	public String calculate(String input) {
		try {
			return String.valueOf(input + " = " + engine.eval(input));
		} catch (ScriptException e) {
			return "calculate() error: " + e.getMessage();
		}
	}
}
