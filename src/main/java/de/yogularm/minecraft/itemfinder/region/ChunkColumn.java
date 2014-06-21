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
	
	public static ChunkColumn load(InputStream stream, int regionX, int regionZ)
			throws IOException {
		ChunkColumn column = new ChunkColumn();
		
		TagCompound tag = NBTUtils.load(stream, TagCompound.class);
		TagCompound level = tag.getCompound("Level");
		
		if (level.getTag("xPos") != null) {
			column.x = level.getInteger("xPos");
			column.z = level.getInteger("zPos");
			System.out.println((column.x % 32) + " " + (column.z % 32));
		}
		
		Vector chunkPosition = new Vector(column.x * 16, 0, column.z * 16);
		
		Builder<DroppedItem> items = new Builder<>();
		for (TagCompound entity : level.getList("Entities", TagCompound.class)) {
			if (!entity.getString("id").equals("Item"))
				continue;
			TagCompound item = entity.getCompound("Item");
			
			Vector position = new Vector(entity.getDoubleArray("Pos")).add(chunkPosition);
			
			items.add(new DroppedItem(
					item.getShort("id"),
					item.getByte("Count"),
					item.getShort("Damage"),
					position));
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
