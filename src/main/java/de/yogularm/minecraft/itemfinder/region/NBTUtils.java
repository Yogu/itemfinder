package de.yogularm.minecraft.itemfinder.region;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagCompound;

public class NBTUtils {
	public static TagCompound load(InputStream stream) throws IOException {
		try (NbtInputStream nbtStream = new NbtInputStream(stream)) {
			ITag tag = nbtStream.readTag();
			if (!(tag instanceof TagCompound))
				throw new InvalidSaveFormatException(
						"The root tag of the NBT file should be a compound, but is of type "
								+ tag.getClass().getSimpleName());
			return (TagCompound)tag;
		}
	}

	public static TagCompound load(Path file) throws IOException {
		try (InputStream stream = new GZIPInputStream(new FileInputStream(file.toFile()))) {
			return load(stream);
		}
	}
}
