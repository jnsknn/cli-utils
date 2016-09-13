package fi.jonne.javacliutils.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fi.jonne.javacliutils.core.Communicator;
import fi.jonne.javacliutils.core.utils.IRCBot;
import fi.jonne.javacliutils.core.utils.TimerInfoContainer;
import fi.jonne.javacliutils.settings.Settings;

public class JavaCLIUtilsMain {
	
	private static BufferedReader br;
	
	public static void main(String[] args) throws IOException {
		
		initialize();
		
		while(true){
			Communicator.getInstance().printInput();
			if(IRCBot.getInstance().isConnected()){			
				Communicator.getInstance().sendMessage(br.readLine());
			}else{
				Communicator.getInstance().handleInput(Settings.LOCAL_CHANNEL, Settings.currentLocalSender, "?" + br.readLine());
			}
		}
		
	}
	
	private static void initialize(){
		Settings.initializeSettings();
		TimerInfoContainer.getInstance().initializeTimers();
		br = new BufferedReader(new InputStreamReader(System.in));
		Communicator.getInstance().printOutput(Settings.LOCAL_CHANNEL, Settings.LOCAL_SENDER, "[READY]");
	}
}
