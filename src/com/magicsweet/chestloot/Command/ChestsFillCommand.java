package com.magicsweet.chestloot.Command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
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
import com.magicsweet.chestloot.Item.LootableItem;
import com.magicsweet.chestloot.Main.Main;
import com.magicsweet.chestloot.Util.RandomNumber;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TextArgument;

public class ChestsFillCommand {
	public ChestsFillCommand() {}
	
	public void registerCommand() throws IOException  {
    	JavaPlugin plugin = Main.getPlugin(Main.class);
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("request", new LiteralArgument("fill"));
		
		String string = Main.getPlugin(Main.class).getDataFolder().toString() + "/loot";
		List<String> paths = new ArrayList<String>();
		for (Path path : Files.walk(Paths.get(string)).collect(Collectors.toList())) {
		if (path.toString().endsWith(".yml")) {
		paths.add(new String('"' + new String(new String(path.toString()).replace("\\", "/").split("/loot/", 2)[1]) + '"'));
			}
		}
		arguments.put("lootConfig", new TextArgument().overrideSuggestions(paths.toArray(new String[paths.size()])));
		
		List<String> worlds = new ArrayList<String>();
		for (World name : Main.getPlugin(Main.class).getServer().getWorlds()) {
			worlds.add(name.getName());
		}
		arguments.put("map", new StringArgument().overrideSuggestions(worlds.toArray(new String[worlds.size()])));
		

		
		//register the command
		new CommandAPICommand("chests")
	    .withArguments(arguments)
	    .executes((sender, args) -> {

			FileConfiguration data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "chests.yml"));
			int[] chestCount = {0, 0};
	    try {
	    if (new File(plugin.getDataFolder(), "loot/" + args[0]).exists()) {
	    	FileConfiguration loot = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "loot/" + args[0]));
		    if (plugin.getServer().getWorld(args[1].toString()) != null) {
		    	World world = plugin.getServer().getWorld(args[1].toString());
		    	Collection<ItemStack> rolledItems = new ArrayList<ItemStack>();
	    		
	    		//roll items
	    		for (String configEntry : loot.getStringList("island_chests.items")) {
	    			String[] entries = configEntry.replace(", ", ",").split(",");
	    			ItemStack item = null;
	    			if (entries.length > 4) {
	    				item = new LootableItem(entries[0], entries[1], entries[2], entries[4] + "," + entries[5]).getItemStack(true);
	    			} else {
	    				item = new LootableItem(entries[0], entries[1], entries[2]).getItemStack(true);
	    			}	
	    			
	    				if (item != null) {
	    					
	    					rolledItems.add(item);
	    			//Duplicates
	    					if (Boolean.parseBoolean(entries[3]) && loot.getInt("island_chests.max_duplicate_count") > 0) {
	    						if (new RandomNumber().generateInt(1, 100) <= loot.getInt("island_chests.duplicate_chance")) {
		    						int r = new RandomNumber().generateInt(1, loot.getInt("island_chests.max_duplicate_count"));
		    						for (int t = 0; t < r; t++) {
		    						rolledItems.add(item);
	    						}	
	    					}
	    				}
	    			//Dependables
	    				for (String keyd : loot.getConfigurationSection("island_chests.dependable_items").getKeys(false)) {
	    					List<String> originalEntry = new ArrayList<String>(Arrays.asList(keyd.replace(", ", ",").split(",")));
	    					List<String> matchingKey = new ArrayList<String>(Arrays.asList(entries));
	    					matchingKey.remove(2);
	    					matchingKey.remove(2);
	    					if (originalEntry.get(1).equals("%")) originalEntry.set(1, matchingKey.get(1));
	    					
	    					if (originalEntry.equals(matchingKey)) {
	    						int rollCount;
	    						rollCount = loot.getInt("island_chests.dependable_items." + keyd + ".max_item_count");
	    						if (rollCount == 0) rollCount = 1;
	    						System.out.println(rollCount);
    							List<Integer> ints = new RandomNumber().generateCompleteInts(1, loot.getStringList("island_chests.dependable_items." + keyd + ".items").size(), rollCount).stream().collect(Collectors.toList());	    						
	    						for (int t = 0; t < rollCount; t++) {
	    							String[] e = loot.getStringList("island_chests.dependable_items." + keyd + ".items").get(ints.get(t) - 1).replace(", ", ",").split(",");
	    							if (e.length > 2) {
	    								rolledItems.add(new LootableItem(e[0], e[1], "100%", e[2] + "," + e[3]).getItemStack(false));
	    							} else {
	    								rolledItems.add(new LootableItem(e[0], e[1]).getItemStack(false));
	    							}
	    						}
	    						
	    						
	    					} else {
	    					}

	    				}
	    		}
	    	}
	    		for (String configEntry : loot.getStringList("categorized_items")) {
	    				//TODO
	    		}
	    		
		    	for (String key : data.getConfigurationSection("island_chests." + args[1]).getKeys(false)) {
		    		//get all the chests
		    		List<Chest> chests = new ArrayList<Chest>();
		    		for (String locationString : data.getStringList("island_chests." + args[1] + "." + key)) {
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
		    			chestCount[1]++;
		    		}
		    	}
		    	List<ItemStack> items = rolledItems.stream().collect(Collectors.toList());
		    	//put loot here
		    	for (ItemStack item : items) {
		    		int chestNum = new RandomNumber().generateInt(0, chests.size() - 1);
		    		int chestSlot = new RandomNumber().generateInt(0, 26);
		    		System.out.println(chests.get(chestNum).getInventory().getContents());
		    		while (chests.get(chestNum).getInventory().getContents()[chestSlot] != null) {
		    			chestSlot = new RandomNumber().generateInt(0, 26);
		    		}
		    		chests.get(chestNum).getInventory().setItem(chestSlot, item);
		    	}
		    	}
		    
				System.out.println(chestCount[0] + " " + chestCount[1]);
		    } else {
		    	sender.sendMessage("§cUnknown dimension: " + args[1]);
		    }	
	    } else {
	    	sender.sendMessage(new File(plugin.getDataFolder(), "loot/" + args[0]) + "");
	    	
	    }

	    
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    }
	    })
	    .register();
	}
	private boolean dependable_isKeysMatching() {
		
		return false;
		
	}
	
	
	private ItemStack dependable_getItemStack() {
		
		return null;
		
	}
}
