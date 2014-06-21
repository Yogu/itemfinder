package de.yogularm.minecraft.itemfinder.cli;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.yogularm.minecraft.itemfinder.region.DroppedItem;
import de.yogularm.minecraft.itemfinder.region.World;

public class ItemFinderCli {
	public static void main(String[] args) throws IOException {
		// path via command-line argument
		if (args.length > 0 && Files.isDirectory(Paths.get(args[0])))
			processSave(Paths.get(args[0]));
		else
			showMinecraftWorlds();
	}

	private static void showMinecraftWorlds() throws IOException {
		Path homePath = Paths.get(System.getProperty("user.home"));
		Path savesPath = homePath.resolve(".minecraft").resolve("saves");
		if (!Files.isDirectory(savesPath)) {
			System.out.println("No default minecraft save location found");
			return;
		}
		
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(savesPath)) {
			List<Path> paths = new ArrayList<>();
			System.out.println("Your minecraft saves:");
			for (Path worldPath : ds) {
				paths.add(worldPath);
				System.out.println(paths.size() + ": " + worldPath.getFileName());
			}
			System.out.print("Choose a number: ");
			@SuppressWarnings("resource")
			int number = new Scanner(System.in).nextInt();
			if (number < 1 || number > paths.size())
				System.out.println("Invalid number");
			else
				processSave(paths.get(number - 1));
		}
	}

	private static void processSave(Path location) throws IOException {
		System.out.println("Loading world in " + location);
		World world;
		try {
			world = new World(location);
		} catch (IOException e) {
			System.out.println("Error loading world: " + e.getMessage());
			return;
		}

		System.out.println("");
		System.out.println("Found Items:");

		for (DroppedItem item : world.getOverworld().getDroppedItems()) {
			System.out.println(item.toString());
		}
	}
}
