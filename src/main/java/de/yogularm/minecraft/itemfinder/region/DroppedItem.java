package de.yogularm.minecraft.itemfinder.region;



public class DroppedItem {
	private int id;
	private int damage;
	private int count;
	public Vector position;

	public DroppedItem(int id, int damage, int count, Vector position) {
		this.id = id;
		this.damage = damage;
		this.count = count;
		this.position = position;
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
	
	@Override
	public String toString() {
		return toString(new VanillaItemNameProvider());
	}
	
	public String toString(ItemNameProvider itemNameProvider) {
		String name = itemNameProvider.getItemName(id, damage);
		if (count > 1)
			name = count + "x " + name;
		name += " @ " + position;
		return name;
	}
}
