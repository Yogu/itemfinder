package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evilco.mc.nbt.tag.TagCompound;

public class ForgeData implements ItemNameProvider {
	private Map<Integer, String> itemNames;

	public ForgeData(Path levelDatPath) throws IOException {
		itemNames = new HashMap<>();
		
		TagCompound tag = NBTUtils.load(levelDatPath);
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
		if (itemNames.containsKey(itemID))
			return itemNames.get(itemID);
		return ItemNames.getName(itemID, damageValue);
	}
}
