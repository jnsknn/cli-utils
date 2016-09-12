package fi.jonne.javacliutils.core;

import fi.jonne.javacliutils.ao.EAOCommands;

public class Command {
	
	protected ECommands command;
	
	public Command(){}
	
	public void executeCommand(String channel, String sender, String[] args){
		for(ECommands cmd : ECommands.values()){
			if(cmd.isCommand(args) && cmd.isAuthorized(sender)){
				cmd.execute(channel, sender, args);
				return;
			}
		}
		for(EAOCommands cmd : EAOCommands.values()){
			if(cmd.isCommand(args) && cmd.isAuthorized(sender)){
				cmd.execute(channel, sender, args);
				return;
			}
		}
		ECommands.HELP.execute(channel, sender, null);
	}
}
