package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dimension {
	private List<DroppedItem> items = new ArrayList<>();
	private Path path;
	private LevelInfo levelInfo;
	private String name;

	public Dimension(Path regionPath, LevelInfo levleInfo, String name) {
		this.path = regionPath;
		this.levelInfo = levleInfo;
		this.name = name;
	}

	public void loadRegions(ProgressListener progessListener)
			throws IOException, InterruptedException {
		if (!Files.isDirectory(path))
			throw new InvalidSaveFormatException(
					"region directory does not exist");

		ProgressReporter progressReporter = new ProgressReporter(
				progessListener);

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
			Pattern fileNamePattern = Pattern
					.compile("r\\.(-?\\d+)\\.(-?\\d+)\\.mca");
			List<Path> regionPaths = new ArrayList<>();
			for (Path regionPath : ds) {
				Matcher fileNameMatcher = fileNamePattern.matcher(regionPath
						.getFileName().toString());
				if (fileNameMatcher.matches())
					regionPaths.add(regionPath);
			}
			
			for (Path regionPath : regionPaths) {
				ProgressListener subListener = progressReporter.startSubtask(1.0 / regionPaths.size());
				loadRegionFile(regionPath, subListener);
				progressReporter.incProgress(1.0 / regionPaths.size());

				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException();
			}
		}
	}

	private void loadRegionFile(Path regionPath,
			ProgressListener progressListener) throws IOException,
			InterruptedException {
		ProgressReporter progressReporter = new ProgressReporter(progressListener);
		
		try (AnvilReader reader = new AnvilReader(regionPath)) {
			while (reader.hasMore()) {
				try (InputStream chunkStream = reader.readChunkColumn()) {
					ChunkColumn column = ChunkColumn.load(chunkStream,
							levelInfo);
					items.addAll(column.getDroppedItems());
				}

				progressReporter.incProgress(1.0 / reader.getChunkCount());
				
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException();
			}
		}
	}

	public String getName() {
		return name;
	}

	public List<DroppedItem> getDroppedItems() {
		return items;
	}

	@Override
	public String toString() {
		return name;
	}
}
