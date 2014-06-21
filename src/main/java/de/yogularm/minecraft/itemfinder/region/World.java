package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents the terrain of one minecraft world
 */
public class World {
	private Dimension overworld;
	private Dimension nether;
	private Dimension end;
	
	public World(Path path) throws IOException {
		overworld = new Dimension(path.resolve("region"));
		
		Path p = path.resolve("DIM-1").resolve("region");
		if (Files.isDirectory(p))
			nether = new Dimension(p);

		p = path.resolve("DIM1").resolve("region");
		if (Files.isDirectory(p))
			end = new Dimension(p);
	}
	
	public Dimension getOverworld() {
		return overworld;
	}

	public Dimension getNether() {
		return nether;
	}

	public Dimension getEnd() {
		return end;
	}
}
