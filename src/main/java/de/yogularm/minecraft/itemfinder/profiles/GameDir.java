package de.yogularm.minecraft.itemfinder.profiles;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class GameDir {
	private List<MinecraftSave> saves;
	
	public GameDir(Path path, String name) throws IOException {
		Path savePath = path.resolve("saves");
		if (Files.isDirectory(savePath)) {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(savePath)) {
				ImmutableList.Builder<MinecraftSave> builder = new ImmutableList.Builder<>();
				for (Path worldPath : ds) {
					if (Files.isDirectory(worldPath))
						builder.add(new MinecraftSave(worldPath, name));
				}
				saves = builder.build();
			}
		} else {
			saves = ImmutableList.of();
		}
	}
	
	public List<MinecraftSave> getSaves() {
		return saves;
	}
}
