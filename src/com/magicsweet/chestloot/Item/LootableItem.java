package com.magicsweet.chestloot.Item;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.magicsweet.chestloot.Exception.IllegalItemException;
import com.magicsweet.chestloot.Main.Main;
import com.magicsweet.chestloot.Util.RandomNumber;

import io.netty.handler.codec.AsciiHeadersEncoder.NewlineType;

public class LootableItem {
	private String minecraftID;
	private String countable;
	private String chance;
	private String additionalData;
	private ItemStack item;
	//Seperated
	public LootableItem(String ID, String countableNumber, String precentageChance, String unnececaryAdditionalData){
		minecraftID = ID;
		countable = countableNumber;
		chance = precentageChance;
		additionalData = unnececaryAdditionalData;
	try {
		item = new ItemStack(getMaterial());
		item.setAmount(getCount());
		item.setItemMeta(getAdditionalData(item.getType()));
	} catch (IllegalItemException e) {
		e.printStackTrace();
	}
	}
	public LootableItem(String ID, String countableNumber, String precentageChance){
		minecraftID = ID;
		countable = countableNumber;
		chance = precentageChance;
	try {
		item = new ItemStack(getMaterial());
		item.setAmount(getCount());
	} catch (IllegalItemException e) {
		e.printStackTrace();
	}
	
	}
	public LootableItem(String ID, String countableNumber) {
		minecraftID = ID;
		countable = countableNumber;
		chance = "100%";
	try {
		item = new ItemStack(getMaterial());
		item.setAmount(getCount());
	} catch (IllegalItemException e) {
		e.printStackTrace();
	}
	}

	private Material getMaterial() throws IllegalItemException {
	return new MinecraftItem().getMaterial(minecraftID);
	}
	
	private int getCount() {
		if (countable.contains("-")) {
			String[] entries = countable.split("-");
			if (entries.length != 2) {
				return new RandomNumber().generateInt(Integer.parseInt(entries[0]), Integer.parseInt(entries[1]));
			} else {
				Main.getPlugin(Main.class).getLogger().warning("\nOnly 2 ranged numbers are allowed: " + "'" + countable + "'");
				throw new NumberFormatException();
			}
			} else if (countable.contains("/")) {
				String[] entries = countable.split("/");
				return Integer.parseInt(entries[new RandomNumber().generateInt(0, entries.length - 1)]);
			} else {
				return Integer.parseInt(countable);	
			}
	
	}
	private ItemMeta getAdditionalData(Material item) throws IllegalItemException {
		ItemMeta meta = new ItemStack(getMaterial()).getItemMeta();
		PotionMeta metaPotion = null;
		try {
			metaPotion = (PotionMeta) meta;
		} catch (ClassCastException e) {}
		String[] data = additionalData.replace(", ", ",").split(",");

		String[] itemData = data[0].toString().replace("; ", ";").replace("'", "").split(";");
		String[] itemDataValuesTemp = data[1].toString().replace("; ", ";").replace("'", "").split(";");
		if (itemData.length == itemDataValuesTemp.length) {
			for (int t = 0; t < itemData.length; t++) {
			String[] itemDataValues = itemDataValuesTemp[t].split(" ");
				if (itemData[t].equals("Enchantment")) {
					meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(itemDataValues[0].replace("minecraft:", ""))), Integer.parseInt(itemDataValues[1]), Boolean.parseBoolean(itemDataValues[2]));
				} else if (itemData[t].equals("CustomEffects")) {
					metaPotion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(itemDataValues[0]), Integer.parseInt(itemDataValues[1]), Integer.parseInt(itemDataValues[2])), false);
				} else if (itemData[t].equals("Color")) {
					metaPotion.setColor(Color.fromRGB(Integer.parseInt(itemDataValues[0]), Integer.parseInt(itemDataValues[1]), Integer.parseInt(itemDataValues[2])));
				} else if (itemData[t].equals("Potion")) {
					metaPotion.setBasePotionData(new PotionData(PotionType.valueOf(itemDataValues[0]), Boolean.parseBoolean(itemDataValues[1]), Boolean.parseBoolean(itemDataValues[2])));
				} else {
					Main.getPlugin(Main.class).getLogger().warning("\nUnsupported parameter: " + additionalData + "'" + ", skipping\n");
				}
			}
			if (item.equals(Material.POTION) 
			|| item.equals(Material.SPLASH_POTION) 
			|| item.equals(Material.LINGERING_POTION) 
			|| item.equals(Material.TIPPED_ARROW)) {
				return metaPotion;
			} else {
				return meta;
			}
		} else {
			Main.getPlugin(Main.class).getLogger().warning("\nEntry " + "'" + additionalData + "'" + "' do not meet requirement 'Key 1; Key 2, Values of Key 1; Values of Key 2', skipping\n");
			return null;
		}
	}
	private boolean ifChanceRolled() {
		if (chance.contains("%") && Integer.parseInt(chance.replace("%", "")) <= 100) {
			if (new RandomNumber().generateInt(1, 100) <= Integer.parseInt(chance.replace("%", ""))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public ItemStack getItemStack(boolean respectChance) {
		if (respectChance && ifChanceRolled()) {
			return item;
		} else if (!respectChance) {
			return item;
		} else {
			return null;
		}
		
	}
	public String getItemID() {
		return minecraftID;
	}
	
}
