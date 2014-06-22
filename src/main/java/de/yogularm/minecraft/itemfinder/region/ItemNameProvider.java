package de.yogularm.minecraft.itemfinder.region;

public interface ItemNameProvider {
	String getItemName(int itemID, int damageValue);
	
	boolean hasNameFor(int itemID);
}
