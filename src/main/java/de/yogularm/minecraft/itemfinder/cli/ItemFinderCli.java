package de.yogularm.minecraft.itemfinder.cli;

import java.io.IOException;
import java.nio.file.Paths;

import de.yogularm.minecraft.itemfinder.region.DroppedItem;
import de.yogularm.minecraft.itemfinder.region.World;

public class ItemFinderCli {
	public static void main(String[] args) throws IOException {
		World world = new World(Paths.get(args[0]));
		
		for (DroppedItem item : world.getOverworld().getDroppedItems()) {
			System.out.println(item.toString());
		}
	}
}
