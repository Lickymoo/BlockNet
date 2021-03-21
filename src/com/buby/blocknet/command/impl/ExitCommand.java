package com.buby.blocknet.command.impl;

import com.buby.blocknet.command.CommandExecutor;
import com.buby.blocknet.command.CommandResponse;

public class ExitCommand extends CommandExecutor{

	public ExitCommand() {
		super("exit");
	}
	
	@Override
	public CommandResponse exec(String[] args) {
		return CommandResponse.EXIT;
	}

}
