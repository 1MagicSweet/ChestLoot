package com.magicsweet.chestloot.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.magicsweet.chestloot.Exception.IllegalLocationException;
import com.magicsweet.chestloot.Main.Main;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class ChestsClearCommand {
	public ChestsClearCommand(){}

	public void registerCommand() {
		JavaPlugin plugin = Main.getPlugin(Main.class);
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("request", new LiteralArgument("clear"));
		List<String> worlds = new ArrayList<String>();
		for (World name : Main.getPlugin(Main.class).getServer().getWorlds()) {
			worlds.add(name.getName());
		}
		arguments.put("map", new StringArgument().overrideSuggestions(worlds.toArray(new String[worlds.size()])));
		
		new CommandAPICommand("chests")
	    .withArguments(arguments)
	    .executes((sender, args) -> {
	    	World world = plugin.getServer().getWorld(args[0].toString());
	    	FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "chests.yml"));
	    	for (String key : data.getConfigurationSection("island_chests." + args[0]).getKeys(false)) {
	    		//get all the chests
	    		List<Chest> chests = new ArrayList<Chest>();
	    		for (String locationString : data.getStringList("island_chests." + args[0] + "." + key)) {
	    			try {
		    		Location location = null;
		    		List<Integer> locationArrList = new ArrayList<Integer>();
	    				String[] locationStrings = locationString.split(" ");
	    				for (String locationSeparated : locationStrings) {
	    					locationArrList.add(Integer.parseInt(locationSeparated));
	    				}

	    			location = new Location(world, locationArrList.get(0), locationArrList.get(1), locationArrList.get(2));
	    			Block block = world.getBlockAt(location);
	    			if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
    				chests.add((Chest) block.getState());
	    			} else {
	    			throw new IllegalLocationException("Block at: " + locationString + " isn't a chest or trapped chest");
	    			
	    			}
	    			
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	chests.forEach((chest) -> {
	    		chest.getInventory().clear();
	    	});
	    		
	    	}
	    })
	    .register();
	}
}
