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

	public Dimension(Path regionPath) throws IOException {
		this.path = regionPath;
		loadRegions();
	}

	public void loadRegions() throws IOException {
		if (!Files.isDirectory(path))
			throw new InvalidSaveFormatException(
					"region directory does not exist");

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
			Pattern fileNamePattern = Pattern
					.compile("r\\.(-?\\d+)\\.(-?\\d+)\\.mca");
			for (Path regionPath : ds) {
				Matcher fileNameMatcher = fileNamePattern.matcher(regionPath
						.getFileName().toString());
				if (!fileNameMatcher.matches())
					continue;

				try (AnvilReader reader = new AnvilReader(regionPath)) {
					while (reader.hasMore()) {
						try (InputStream chunkStream = reader.readChunkColumn()) {
							ChunkColumn column = ChunkColumn.load(chunkStream);
							items.addAll(column.getDroppedItems());
						}
					}
				}
			}
		}
	}

	public List<DroppedItem> getDroppedItems() {
		return items;
	}
}
