package com.buby.blocknet.core.master;

import com.buby.blocknet.command.CommandProcessor;
import com.buby.blocknet.command.impl.ExitCommand;
import com.buby.blocknet.command.impl.master.ProvisionCommandMaster;

public class MasterCommandProcessor extends CommandProcessor{
	MasterCommandProcessor(){
		super(
			new ExitCommand(),
			new ProvisionCommandMaster()
		);
	}
}
