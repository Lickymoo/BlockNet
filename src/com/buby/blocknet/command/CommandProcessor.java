package com.buby.blocknet.command;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

public abstract class CommandProcessor {
	
	@Getter private static Set<CommandExecutor> commandRegistry = new HashSet<>();
	
	public CommandProcessor(CommandExecutor... executors) {
		registerCmds(executors);
	}
	
	private void registerCmds(CommandExecutor... executors) {
		for(CommandExecutor executor : executors) {
			commandRegistry.add(executor);
		}
	}
	
	public CommandResponse processInput(String input) {
		String[] args = input.split(" ");
		for(CommandExecutor executor : commandRegistry) {
			if(executor.aliases.contains(args[0])) {
				return executor.exec(args);
			}
		}
		return CommandResponse.INVALID;
	}
}
