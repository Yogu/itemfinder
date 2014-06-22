package de.yogularm.minecraft.itemfinder.gui;

public class HourSecond implements Comparable<HourSecond> {
	private int seconds;
	
	public HourSecond(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		if (seconds == Integer.MAX_VALUE)
			return "--";
		return String.format("%d:%02d", seconds / 60, seconds % 60);
	}
	
	@Override
	public int compareTo(HourSecond o) {
		return Integer.compare(seconds, o.seconds);
	}
}
