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
		return count + "x " + id + " @ " + position;
	}
}
