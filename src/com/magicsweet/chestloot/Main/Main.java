package com.magicsweet.chestloot.Main;


import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.magicsweet.chestloot.Command.ChestsClearCommand;
import com.magicsweet.chestloot.Command.ChestsFillCommand;
import com.magicsweet.chestloot.Command.TestCommand;

public class Main extends JavaPlugin {

	public void onEnable() {
		/*try {
			new ChestsFillCommand().registerCommand();
			new ChestsClearCommand().registerCommand();
			new TestCommand().registerCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public void onLoad() {
		try {
			new ChestsFillCommand().registerCommand();
			new ChestsClearCommand().registerCommand();
			new TestCommand().registerCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
