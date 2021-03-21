package com.buby.blocknet.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandExecutor {
	List<String> aliases = new ArrayList<>();
	
	public CommandExecutor(String... aliases) {
		this.aliases = Arrays.asList(aliases);
	}
	
	public abstract CommandResponse exec(String[] args);
}
