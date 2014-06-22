package de.yogularm.minecraft.itemfinder.profiles;

import java.io.IOException;
import java.nio.file.Path;

import de.yogularm.minecraft.itemfinder.region.World;

public class MinecraftSave {
	private Path path;
	private String gameDirName;
		
	public MinecraftSave(Path path, String gameDirName) {
		this.path = path;
		this.gameDirName = gameDirName;
	}
	
	public World loadWorld() throws IOException, InterruptedException {
		return new World(path);
	}
	
	public String getWorldName() {
		return path.getFileName().toString();
	}
	
	public String getGameDirName() {
		return gameDirName;
	}
	
	public String getDisplayName() {
		return getWorldName() + " (" + getGameDirName() + ")";
	}
	
	@Override
	public String toString() {
		return getDisplayName();
	}
}
