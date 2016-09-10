package fi.jonne.javacliutils.core;

import fi.jonne.javacliutils.ao.EAOCommands;

public class Command {
	
	protected ECommands command;
	
	public Command(){}
	
	public void executeCommand(String[] args){
		for(ECommands cmd : ECommands.values()){
			if(cmd.isCommand(args) && cmd.isAuthorized(Communicator.getInstance().getSender())){
				cmd.execute(args);
				return;
			}
		}
		for(EAOCommands cmd : EAOCommands.values()){
			if(cmd.isCommand(args) && cmd.isAuthorized(Communicator.getInstance().getSender())){
				cmd.execute(args);
				return;
			}
		}
	}
}
