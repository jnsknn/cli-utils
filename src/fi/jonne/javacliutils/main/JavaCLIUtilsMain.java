package fi.jonne.javacliutils.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fi.jonne.javacliutils.core.Communicator;
import fi.jonne.javacliutils.core.utils.TimerInfoContainer;
import fi.jonne.javacliutils.settings.Settings;

public class JavaCLIUtilsMain {
	
	public static void main(String[] args) throws IOException {
		
		TimerInfoContainer.getInstance().initializeTimers();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			Communicator.getInstance().handleInput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, br.readLine());
		}
		
	}
}
