package de.yogularm.minecraft.itemfinder.profiles;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.ImmutableList;

public class ProfilesCollection {
	private Path rootDir;
	private List<MinecraftSave> saves;

	public ProfilesCollection() {
		saves = ImmutableList.of();
	}

	public ProfilesCollection(Path rootDir) throws IOException {
		this.rootDir = rootDir;

		ImmutableList.Builder<MinecraftSave> builder = new ImmutableList.Builder<>();
		for (GameDir gameDir : getGameDirs()) {
			builder.addAll(gameDir.getSaves());
		}
		saves = builder.build();
	}

	public static ProfilesCollection getDefault() throws IOException {
		// Linux
		Path path = Paths.get(System.getProperty("user.home")).resolve(
				".minecraft");
		if (Files.isDirectory(path))
			return new ProfilesCollection(path);

		// Windows
		String appdata = System.getenv("APPDATA");
		if (appdata != null) {
			path = Paths.get(appdata).resolve(".minecraft");
			if (Files.isDirectory(path))
				return new ProfilesCollection(path);
		}

		// Mac OS
		path = Paths.get(System.getProperty("user.home")).resolve("Library")
				.resolve("Application Support").resolve("minecraft");
		if (Files.isDirectory(path))
			return new ProfilesCollection(path);
		
		throw new IOException("Unable to find your minecraft profile.");
	}

	public List<MinecraftSave> getSaves() {
		return saves;
	}

	private List<GameDir> getGameDirs() throws IOException {
		Map<Path, List<String>> pathProfileNames = new HashMap<Path, List<String>>();

		pathProfileNames.put(rootDir, Arrays.asList("Minecraft"));

		byte[] bytes = Files.readAllBytes(rootDir
				.resolve("launcher_profiles.json"));
		String json = new String(bytes, Charset.forName("utf8"));
		try {
			JSONObject object = new JSONObject(json);
			JSONObject profiles = object.getJSONObject("profiles");
			for (String profileName : JSONObject.getNames(profiles)) {
				JSONObject profile = profiles.getJSONObject(profileName);
				if (profile.has("gameDir")) {
					Path gameDir = Paths.get(profile.getString("gameDir"));

					if (!pathProfileNames.containsKey(gameDir))
						pathProfileNames.put(gameDir, new ArrayList<String>());
					pathProfileNames.get(gameDir).add(profileName);
				}
			}
		} catch (JSONException e) {
			throw new IOException("Invalid launcher_profiles.json file", e);
		}

		List<GameDir> list = new ArrayList<>();
		for (Map.Entry<Path, List<String>> entry : pathProfileNames.entrySet()) {
			String gameDirName = StringUtils.join(entry.getValue(), ", ");
			list.add(new GameDir(entry.getKey(), gameDirName));
		}
		return list;
	}

}
