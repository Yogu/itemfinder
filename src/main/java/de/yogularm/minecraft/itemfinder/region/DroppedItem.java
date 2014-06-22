package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;

import com.evilco.mc.nbt.tag.TagCompound;

public class DroppedItem {
	private int id;
	private int damage;
	private int count;
	private int age;
	private Vector position;
	private String name;
	private long lastChunkUpdate;

	public DroppedItem(TagCompound entityTag, ItemNameProvider nameProvider,
			ChunkColumn chunkColumn) throws IOException {
		TagCompound item = entityTag.getCompound("Item");

		position = new Vector(entityTag.getDoubleArray("Pos"));
		age = entityTag.getShort("Age");
		id = item.getShort("id");
		damage = item.getShort("Damage");
		count = item.getByte("Count");
		name = nameProvider.getItemName(id, damage);
		lastChunkUpdate = chunkColumn.getLastUpdate();
	}

	public int getID() {
		return id;
	}

	public int getDamage() {
		return damage;
	}

	public int getCount() {
		return count;
	}

	public Vector getPosition() {
		return position;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
	
	public long getLastChunkUpdate() {
		return lastChunkUpdate;
	}

	@Override
	public String toString() {
		String str = name;
		if (count > 1)
			str = count + "x " + str;
		str += " @ " + position + " (age: " + age + ")";
		return str;
	}
}
