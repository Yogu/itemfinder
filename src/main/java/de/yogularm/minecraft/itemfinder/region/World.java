package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Represents the terrain of one minecraft world
 */
public class World {
	private List<Dimension> dimensions = ImmutableList.of();
	private LevelInfo forgeData;
	private Path path;
	private String gameDirName;

	public World(Path path, String gameDirName) {
		this.path = path;
		this.gameDirName = gameDirName;
	}

	public void load(ProgressListener progressListener) throws IOException,
			InterruptedException {
		forgeData = new LevelInfo(path.resolve("level.dat"));

		ImmutableList.Builder<Dimension> builder = new ImmutableList.Builder<>();
		tryAddDimension(builder, "Overworld", path);
		tryAddDimension(builder, "Nether", path.resolve("DIM-1"));
		tryAddDimension(builder, "End", path.resolve("DIM1"));
		dimensions = builder.build();
		
		// load actual data
		ProgressReporter progressReporter = new ProgressReporter(progressListener);
		for (Dimension dimension : dimensions) {
			progressReporter.onAction("Loading " + dimension.getName() + "...");
			ProgressListener subListener = progressReporter.startSubtask(1.0 / dimensions.size());
			dimension.loadRegions(subListener);
			progressReporter.incProgress(1.0 / dimensions.size());
		}
	}
	
	private void tryAddDimension(ImmutableList.Builder<Dimension> list, String name, Path path) {
		Path regionPath = path.resolve("region");
		if (Files.isDirectory(regionPath))
			list.add(new Dimension(regionPath, forgeData, name));
	}

	public List<Dimension> getDimensions() {
		return dimensions;
	}

	public String getWorldName() {
		return path.getFileName().toString();
	}

	public String getGameDirName() {
		return gameDirName;
	}

	public String getDisplayName() {
		return getWorldName() + (gameDirName.length() > 0 ? " (" + getGameDirName() + ")" : "");
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
