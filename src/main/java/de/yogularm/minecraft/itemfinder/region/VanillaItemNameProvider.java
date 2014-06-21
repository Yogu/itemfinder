package de.yogularm.minecraft.itemfinder.region;

public class VanillaItemNameProvider implements ItemNameProvider {
	@Override
	public String getItemName(int itemID, int damageValue) {
		return ItemNames.getName(itemID, damageValue);
	}
}
