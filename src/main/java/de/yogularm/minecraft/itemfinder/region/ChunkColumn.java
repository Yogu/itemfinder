package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.evilco.mc.nbt.tag.TagCompound;
import com.google.common.collect.ImmutableList.Builder;


public class ChunkColumn {
	private int x;
	private int z;
	private List<DroppedItem> items;
	
	private ChunkColumn() {
	}
	
	public static ChunkColumn load(InputStream stream, ForgeData forgeData)
			throws IOException {
		ChunkColumn column = new ChunkColumn();
		
		TagCompound tag = NBTUtils.load(stream);
		TagCompound level = tag.getCompound("Level");
		
		if (level.getTag("xPos") != null) {
			column.x = level.getInteger("xPos");
			column.z = level.getInteger("zPos");
		}
		
		Builder<DroppedItem> items = new Builder<>();
		for (TagCompound entity : level.getList("Entities", TagCompound.class)) {
			if (!entity.getString("id").equals("Item"))
				continue;
			items.add(new DroppedItem(entity, forgeData));
		}
		column.items = items.build();
		
		return column;
	}
	
	public String getName() {
		return x + "," + z;
	}
	
	public List<DroppedItem> getDroppedItems() {
		return items;
	}
}
