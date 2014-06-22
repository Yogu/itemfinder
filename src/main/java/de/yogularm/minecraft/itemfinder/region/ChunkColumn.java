package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.evilco.mc.nbt.tag.TagCompound;
import com.google.common.collect.ImmutableList.Builder;


public class ChunkColumn {
	private int x;
	private int z;
	private long lastUpdate;
	private List<DroppedItem> items;
	
	private ChunkColumn() {
	}
	
	public static ChunkColumn load(InputStream stream, LevelInfo levelInfo)
			throws IOException {
		ChunkColumn column = new ChunkColumn();
		
		TagCompound tag = NBTUtils.load(stream);
		TagCompound level = tag.getCompound("Level");
		
		column.x = level.getInteger("xPos");
		column.z = level.getInteger("zPos");
		column.lastUpdate = level.getLong("LastUpdate");
		
		Builder<DroppedItem> items = new Builder<>();
		for (TagCompound entity : level.getList("Entities", TagCompound.class)) {
			if (!entity.getString("id").equals("Item"))
				continue;
			items.add(new DroppedItem(entity, levelInfo, column));
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
	
	public long getLastUpdate() {
		return lastUpdate;
	}
}
