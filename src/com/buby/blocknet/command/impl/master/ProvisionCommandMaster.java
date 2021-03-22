package com.buby.blocknet.command.impl.master;

import static com.buby.blocknet.util.CommonUtils.log;

import com.buby.blocknet.BlockNet;
import com.buby.blocknet.command.CommandExecutor;
import com.buby.blocknet.command.CommandResponse;
import com.buby.blocknet.core.Servlet;
import com.buby.blocknet.core.master.BlockNetMaster;
import com.buby.blocknet.util.model.Pair;

public class ProvisionCommandMaster extends CommandExecutor{
	public ProvisionCommandMaster() {
		super("provision");
	}
	
	@Override
	public CommandResponse exec(String[] args) {
		BlockNetMaster blockNet = (BlockNetMaster)BlockNet.blockNetCore;
		if(args.length < 2) {
			log("Not enough arguments.");
			return CommandResponse.OK;
		}
		if(!blockNet.getServerTemplates().contains(args[1])) {
			log("Master does not contain template \"" + args[1] + "\" resfusing to provision.");
			return CommandResponse.OK;
		}
		Pair<Servlet,Integer> provisioned = blockNet.provisionServer(args[1]);
		if(provisioned == null) {
			log("All slaves were at maximum capacity");
			log("Consider modifying server weights or adding more servers to network");
			return CommandResponse.OK;
		}
		log("Sucessfully provisioned template \"" + args[1] + "\" instance on" + provisioned.getLeft().getIp() + ":" + provisioned.getRight());
		return CommandResponse.OK;
	}
}
