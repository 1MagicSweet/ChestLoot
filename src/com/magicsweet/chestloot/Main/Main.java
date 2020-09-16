package com.magicsweet.chestloot.Main;


import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.magicsweet.chestloot.Command.ChestsFillCommand;
import com.magicsweet.chestloot.Command.TestCommand;

public class Main extends JavaPlugin {

	public void onEnable() {
		//getServer().getPluginManager().registerEvents(new com.firestorm.Events.JoinEvent(), this);
		try {
			new ChestsFillCommand().registerCommand();
			new TestCommand().registerCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
