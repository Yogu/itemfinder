package de.yogularm.minecraft.itemfinder.region;

public final class Vector {
	private double x;
	private double y;
	private double z;
		
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(double[] components) {
		if (components.length != 3)
			throw new IllegalArgumentException("must have length of 3");
		this.x = components[0];
		this.y = components[1];
		this.z = components[2];
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
}
