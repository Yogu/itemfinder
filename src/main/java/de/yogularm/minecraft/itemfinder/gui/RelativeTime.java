package de.yogularm.minecraft.itemfinder.gui;

public class RelativeTime implements Comparable<RelativeTime> {
	private int seconds;

	public RelativeTime(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		if (seconds == 0)
			return "just now";
		if (seconds < 0)
			return absolute(-seconds) + " ago";
		return "in " + absolute(seconds);
	}
	
	private String absolute(int seconds) {
		if (seconds == 1)
			return "1 second";
		if (seconds < 60)
			return seconds + " seconds";

		int minutes = seconds / 60;
		if (minutes <= 1)
			return "1 minute";
		if (minutes < 60)
			return minutes + " minutes";

		int days = minutes / 20;
		if (days <= 1)
			return "1 game day";
		return days + " game days";
	}

	@Override
	public int compareTo(RelativeTime o) {
		return Integer.compare(this.seconds, o.seconds);
	}
}
