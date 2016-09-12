package fi.jonne.javacliutils.ao;

import fi.jonne.javacliutils.core.Communicator;
import fi.jonne.javacliutils.core.ICommands;

/**
 * This enum contains all Anarchy Online commands and their implementations
 * **/
public enum EAOCommands implements ICommands {
	LAIVA {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("laiva")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 2; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString.trim();
		}
		public boolean isAuthorized(String sender){
			return true;
		}
		public void execute(String channel, String sender, String[] args) {
			Communicator.getInstance().handleOutput(channel, sender, "/fxscript Tweak_Rubi-Ka_Sunlight #");
		}
	};
}
