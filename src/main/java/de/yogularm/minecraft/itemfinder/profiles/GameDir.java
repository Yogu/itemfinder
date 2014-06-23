package de.yogularm.minecraft.itemfinder.profiles;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.yogularm.minecraft.itemfinder.region.World;

public class GameDir {
	private List<World> worlds;
	
	public GameDir(Path path, String name) throws IOException {
		Path savePath = path.resolve("saves");
		if (Files.isDirectory(savePath)) {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(savePath)) {
				ImmutableList.Builder<World> builder = new ImmutableList.Builder<>();
				for (Path worldPath : ds) {
					if (Files.isDirectory(worldPath))
						builder.add(new World(worldPath, name));
				}
				worlds = builder.build();
			}
		} else {
			worlds = ImmutableList.of();
		}
	}
	
	public List<World> getWorlds() {
		return worlds;
	}
}
