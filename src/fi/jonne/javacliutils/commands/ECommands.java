package fi.jonne.javacliutils.commands;

public enum ECommands {
	CALCULATE {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("calc") && args.length == 2){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString;
		}
	},
	UFONAME {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("ufo") && args.length >= 2){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString;
		}
	},
	EXIT {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("exit")){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString;
		}
	},
	IRC {
		public boolean isCommand(String[] args){
			if(args[0].toLowerCase().startsWith("irc") && args.length == 4){
				return true;
			}
			return false;
		}
		public String getInputStringFromArgs(String[] args){
			String inputString = "";
			
			for(int i = 1; i < args.length; i++){
				inputString += args[i]+" ";
			}
			
			return inputString;
		}
	};
	
	public abstract boolean isCommand(String[] args);
	public abstract String getInputStringFromArgs(String[] args);
}
