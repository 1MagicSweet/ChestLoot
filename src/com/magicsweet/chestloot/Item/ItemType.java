package com.magicsweet.chestloot.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.magicsweet.chestloot.Exception.InvalidTypeException;
import com.magicsweet.chestloot.Exception.TypeFormatException;
import com.magicsweet.chestloot.Exception.UnsupportedTypeException;
import com.magicsweet.chestloot.Item.Entity.ArmorPiece;
import com.magicsweet.chestloot.Item.Entity.CategoryType;
import com.magicsweet.chestloot.Main.Main;
import com.magicsweet.chestloot.Util.RandomNumber;

public class ItemType {
	JavaPlugin plugin = Main.getPlugin(Main.class);
	boolean isModifiable = true;
	boolean withOther = false;
	List<ItemStack> items = new ArrayList<ItemStack>();
	CategoryType cat;
	String[] parameters;
	String originalType;
	public ItemType(String type) {
		originalType = type;
		try {
		if (!type.startsWith("[type:") && !type.endsWith("]")) throw new TypeFormatException(type);
		
		if (type.contains(",")) {
			cat = CategoryType.valueOf(type.substring(type.indexOf("[type:") + 6, type.indexOf(",")).toUpperCase());	
			parameters = type.replace(", ", ",").substring(type.indexOf(",") + 1).replace("]", "").split(",");
			
		}else {
			cat = CategoryType.valueOf(type.substring(type.indexOf("[type:") + 6, type.indexOf("]")).toUpperCase());
		}
		
		
		
		
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
	
	void generateArmor() throws InvalidTypeException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "types/" + "armor.yml"));
		
		List<ArmorPiece> helmets = new ArrayList<>();
		List<ArmorPiece> chestplates = new ArrayList<>();
		List<ArmorPiece> leggings = new ArrayList<>();
		List<ArmorPiece> boots = new ArrayList<>();

		
		
		//helmets
		config.getStringList("armor_protection.helmets.items." + parameters[0]).forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				helmets.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[1])));
			} else {
				helmets.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[1])));
			}
		});
		
		//chestplates
		config.getStringList("armor_protection.chestplates.items." + parameters[0]).forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				chestplates.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[1])));
				
			} else {
				chestplates.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[1])));
			}
		});
		
		//leggings
		config.getStringList("armor_protection.leggings.items." + parameters[0]).forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				leggings.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[1])));
			} else {
				leggings.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[1])));
			}
		});
		
		//boots
		config.getStringList("armor_protection.boots.items." + parameters[0]).forEach((str) -> {
			String[] st2 = str.replace(", ", ",").split(",");
			if (st2.length <= 2) {
				boots.add(new ArmorPiece(new LootableItem(st2[0], "1").getItemStack(false), Double.parseDouble(st2[1])));
			} else {
				boots.add(new ArmorPiece(new LootableItem(st2[0], "1", "0%", st2[2] + "," + st2[3]).getItemStack(false), Double.parseDouble(st2[1])));
			}
		});
		
		
		
		List<List<ArmorPiece>> armor = new ArrayList<>();
		int ap = 0;
		//put all of the variants
		for (ArmorPiece helm : helmets) {
			for (ArmorPiece chest : chestplates) {
				for (ArmorPiece legg : leggings) {
					for (ArmorPiece boot : boots) {
						List<ArmorPiece> piece = new ArrayList<>();
						piece.add(helm);
						piece.add(chest);
						piece.add(legg);
						piece.add(boot);
						
						armor.add(ap, piece);
						
						ap++;
					}
				}
			}
		}
		ap = 0;
		
		List<ItemStack> finalArmor = parseArmor(armor);
		if (finalArmor == null) {
			throw new InvalidTypeException("No such armor to generate: " + originalType);
		}
		items = finalArmor;
	}


	
	List<ItemStack> parseArmor(List<List<ArmorPiece>> armor) {
		//TODO all possible variants of {true, true, true, true} <- (armor pieces)
		//THEN (here) check for number -> add to list
		boolean[] boolsToParse = {false, true};
		List<List<Boolean>> bools = new ArrayList<>();
		
		List<List<ItemStack>> finalStack = new ArrayList<>();
		
		for (boolean b1: boolsToParse) {
			for (boolean b2: boolsToParse) {
				for (boolean b3: boolsToParse) {
					for (boolean b4: boolsToParse) {
						List<Boolean> listBool = new ArrayList<>();
						
						listBool.add(b1);
						listBool.add(b2);
						listBool.add(b3);
						listBool.add(b4);
						
						bools.add(listBool);
					}
				}
			}
		}
		
		for (List<ArmorPiece> listArm : armor) {
			
			for (List<Boolean> listBools : bools) {
				List<ArmorPiece> arm = new ArrayList<>();
				if (listBools.get(0)) arm.add(listArm.get(0));
				if (listBools.get(1)) arm.add(listArm.get(1));
				if (listBools.get(2)) arm.add(listArm.get(2));
				if (listBools.get(3)) arm.add(listArm.get(3));
				
				double i = 0;
				for (ArmorPiece armp : arm) {
					i = i + armp.getArmor();
				}
				
				if (isNumberEligible(i, parameters[parameters.length - 1])) {
					List<ItemStack> armIStack = new ArrayList<>();
					arm.forEach((arm2) -> {
						armIStack.add(arm2.getItemStack());
					});
					
					finalStack.add(armIStack);
				}
				i = 0;
			}
			
		}
		if (finalStack.size() == 0) 
			return null;
		
		return finalStack.get(new RandomNumber().generateInt(0, finalStack.size() - 1));
		
	}
	boolean isNumberEligible(double num, String str) {
		
		if (str.contains("/")) {
			String[] str2 = str.split("/");
			
			boolean bool = false;
			for (String s : str2) {
				if (Double.parseDouble(s) == num) bool = true;
			}
			return bool;
			
		} else if (str.contains("-")) {
			String[] str2 = str.split("-");
			
			 return((num > Double.parseDouble(str2[0])) && (num < Double.parseDouble(str2[1])));
			 
		} else if (str.equals("%")) {
			return true;
		} else {
			return(Double.parseDouble(str) == num);
		}
	}
	public List<ItemStack> getFinalStack() {
		return items;
	}
	public boolean isModifiable() {
		return isModifiable;
	}
}
