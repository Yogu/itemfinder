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
	private LevelInfo forgeData;

	public World(Path path) throws IOException, InterruptedException {
		forgeData = new LevelInfo(path.resolve("level.dat"));
		
		overworld = new Dimension(path.resolve("region"), forgeData);
		
		Path p = path.resolve("DIM-1").resolve("region");
		if (Files.isDirectory(p))
			nether = new Dimension(p, forgeData);

		p = path.resolve("DIM1").resolve("region");
		if (Files.isDirectory(p))
			end = new Dimension(p, forgeData);
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
	
	public LevelInfo getForgeData() {
		return forgeData;
	}
}
