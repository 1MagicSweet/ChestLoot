package com.magicsweet.chestloot.Item;

import org.bukkit.Material;

import com.magicsweet.chestloot.Exception.IllegalItemException;

public class MinecraftItem {
	public MinecraftItem() {}
	
	public Material getMaterial(String identifier) throws IllegalItemException {
		if (!identifier.contains(":")) {
			throw new IllegalItemException("Item " + identifier + " does not specifies which namespace it uses");
		} else {
			String[] item = identifier.split(":");
			if (item[0].equals("minecraft")) {
			try {
			if (Material.getMaterial(item[1].toUpperCase()) == null) {
				throw new IllegalItemException("Unknown item: " + identifier);
			} else {
				return Material.getMaterial(item[1].toUpperCase());
			}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalItemException("Item cannot be null: " + identifier);
			}
			} else {
				throw new IllegalItemException("Only minecraft items are supported: " + identifier);
			}
		}
	}
	public void getItemName(Material material) {
	}
	
}
