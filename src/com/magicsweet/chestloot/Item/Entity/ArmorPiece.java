package com.magicsweet.chestloot.Item.Entity;

import org.bukkit.inventory.ItemStack;

public class ArmorPiece {
	ItemStack item;
	double armor;
	public ArmorPiece(ItemStack item, double arm) {
		this.item = item;
		armor = arm;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public double getArmor() {
		return armor;
	}
}
