package com.magicsweet.chestloot.Command;


import org.bukkit.entity.Player;


import dev.jorel.commandapi.CommandAPICommand;

public class TestCommand {
	public TestCommand() {}
		public void registerCommand()  {
			new CommandAPICommand("test")
		    .executes((sender, args) -> {
		    	Player player = (Player) sender;
		    }).register();
		}
}
