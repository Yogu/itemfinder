package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;
import java.io.InputStream;

import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagType;


public class NBTUtils {
	@SuppressWarnings("unchecked")
	public static <T extends ITag> T load(InputStream stream,
			Class<T> rootTagClass) throws IOException {
		try (NbtInputStream nbtStream = new NbtInputStream(stream)) {
			ITag tag = nbtStream.readTag();
			if (!rootTagClass.isInstance(tag))
				throw new InvalidSaveFormatException(
						"The root tag of the NBT file should be of type "
								+ rootTagClass.getSimpleName()
								+ ", but is of type "
								+ tag.getClass().getSimpleName());
			return (T) tag;
		}
	}
}
