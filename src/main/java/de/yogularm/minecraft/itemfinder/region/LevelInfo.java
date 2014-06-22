package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evilco.mc.nbt.tag.TagCompound;

public class LevelInfo implements ItemNameProvider {
	private Map<Integer, String> itemNames;
	private long currentTick;
	
	public LevelInfo(Path levelDatPath) throws IOException {
		TagCompound tag = NBTUtils.load(levelDatPath);
		
		TagCompound data = tag.getCompound("Data");
		currentTick = data.getLong("Time");
		
		itemNames = new HashMap<>();
		
		if (tag.getTag("FML") != null) {
			TagCompound fml = tag.getCompound("FML");
			if (fml.getTag("ModItemData") != null) {
				List<TagCompound> modItemData = fml.getList("ModItemData", TagCompound.class);
				for (TagCompound item : modItemData) {
					int itemId = item.getInteger("ItemId");
					String itemType = item.getString("ItemType");
					String name = item.getTag("ForcedName") != null ? item.getString("ForcedName") : itemType;
					itemNames.put(itemId, name);
				}
			}
		}
		
	}

	public String getItemName(int itemID, int damageValue) {
		if (ItemNames.getNameProvider().hasNameFor(itemID))
			return ItemNames.getNameProvider().getItemName(itemID, damageValue);
		
		if (itemNames.containsKey(itemID))
			return itemNames.get(itemID);

		return "Item " + itemID;
	}
	
	public long getCurrentTick() {
		return currentTick;
	}

	@Override
	public boolean hasNameFor(int itemID) {
		return itemNames.containsKey(itemID);
	}
}
