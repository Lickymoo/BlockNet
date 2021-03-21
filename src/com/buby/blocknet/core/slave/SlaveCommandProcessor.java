package com.buby.blocknet.core.slave;

import com.buby.blocknet.command.CommandProcessor;
import com.buby.blocknet.command.impl.ExitCommand;

public class SlaveCommandProcessor extends CommandProcessor{
	SlaveCommandProcessor(){
		super(
			new ExitCommand()
		);
	}
}
