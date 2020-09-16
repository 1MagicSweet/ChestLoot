package com.magicsweet.chestloot.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.magicsweet.chestloot.Exception.TypeFormatException;
import com.magicsweet.chestloot.Exception.UnsupportedTypeException;
import com.magicsweet.chestloot.Item.Entity.ArmorPiece;
import com.magicsweet.chestloot.Item.Entity.CategoryType;
import com.magicsweet.chestloot.Main.Main;

public class ItemType {
	JavaPlugin plugin = Main.getPlugin(Main.class);
	boolean isModifiable = true;
	boolean withOther = false;
	List<ItemStack> items = new ArrayList<ItemStack>();
	CategoryType cat;
	String[] parameters;
	public ItemType(String type) {
		try {
		if (!type.startsWith("[type:") && !type.endsWith("]")) throw new TypeFormatException(type);
		cat = CategoryType.valueOf(type.substring(type.indexOf("[type:"), type.indexOf(",")).toUpperCase());
		parameters = type.substring(type.indexOf(",")).replace(", ", ",").split(",");
		
		if (cat.equals(CategoryType.ARMOR)) {
			generateArmor();
		} else if (cat.equals(CategoryType.RANKED_ARMOR)) {
			generateArmor();
			isModifiable = false;
		} else {
			throw new UnsupportedTypeException(type);
		}
		
		} catch (Exception e) { e.printStackTrace(); return; }
	}
	
	void generateArmor() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "types/" + "armor.yml"));
		List<ArmorPiece> armor = new ArrayList<>();
		
		List<ArmorPiece> helmets = new ArrayList<>();
		List<ArmorPiece> chestplates = new ArrayList<>();
		List<ArmorPiece> leggings = new ArrayList<>();
		List<ArmorPiece> boots = new ArrayList<>();
		List<ArmorPiece> other = new ArrayList<>();

		
		//helmets
		config.getStringList("armor_protection.helmets.items").forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				helmets.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[0])));
			} else {
				helmets.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[0])));
			}
		});
		
		//chestplates
		config.getStringList("armor_protection.chestplates.items").forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				chestplates.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[0])));
				
			} else {
				chestplates.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[0])));
			}
		});
		
		//leggings
		config.getStringList("armor_protection.leggings.items").forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				leggings.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[0])));
			} else {
				leggings.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[0])));
			}
		});
		
		//boots
		config.getStringList("armor_protection.boots.items").forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				boots.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[0])));
			} else {
				boots.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[0])));
			}
		});
		
		//other
		config.getStringList("armor_protection.other.items").forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				other.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[0])));
			} else {
				other.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[0])));
			}
		});
		
		
		
		for (ArmorPiece helm : helmets) {
			for (ArmorPiece chest : chestplates) {
				for (ArmorPiece legg : leggings) {
					for (ArmorPiece boot : boots) {
						
					}
				}
			}
		}
	}
	
	public boolean isModifiable() {
		return isModifiable;
	}
}
