package de.yogularm.minecraft.itemfinder.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.InflaterInputStream;


public class AnvilReader implements AutoCloseable {
	private RandomAccessFile file;

	private static final int SECTOR_SIZE = 4096;
	private static final int METADATA_SIZE = 4096 * 2;

	/**
	 * Stores the positions where there are chunks
	 */
	private SortedSet<Integer> chunkPositions;

	public AnvilReader(Path path) throws IOException {
		file = new RandomAccessFile(path.toFile(), "r");
		readMetadata();
	}

	private void readMetadata() throws IOException {
		chunkPositions = new TreeSet<>();
		for (int i = 0; i < 1024; i++) {
			int pos = file.readUnsignedByte() * 0x10000 + file.readUnsignedByte() * 0x100
					+ file.readUnsignedByte();
			file.readByte(); // length
			if (pos > 0) // 0 means chunk does not exist
				chunkPositions.add(pos);
		}
	}

	public InputStream readChunkColumn() throws IOException {
		if (chunkPositions.size() == 0)
			throw new IllegalStateException("No more chunks available");

		int nextChunkPosition = chunkPositions.first();
		chunkPositions.remove(nextChunkPosition);

		// skip to next chunk
		file.seek((long)nextChunkPosition * SECTOR_SIZE);

		// read the raw data
		int length = file.readInt() - 1; // one byte for the compression
		byte compression = file.readByte();
		byte[] rawChunkData = new byte[length];
		file.readFully(rawChunkData);

		// get a stream for the uncompressed data
		InputStream rawChunkStream = new ByteArrayInputStream(rawChunkData);
		InputStream chunkStream;
		switch (compression) {
		case 2:
			/*Inflater inflater = new Inflater();
			inflater.setInput(rawChunkData);
			byte[] uncompressed = new byte[100000];
			int uncompressedLength = 0;
			try {
				uncompressedLength = inflater.inflate(uncompressed);
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uncompressed = Arrays.copyOf(uncompressed, uncompressedLength);
			System.out.println(rawChunkData.length);
			System.out.println(uncompressedLength);
			try (FileOutputStream os = new FileOutputStream("/home/jan/test-" + nextChunkPosition + ".nbt")) {
				os.write(uncompressed, 0, uncompressedLength); 
			}*/
			
			chunkStream = new BufferedInputStream(new InflaterInputStream(rawChunkStream));
			//byte[] uncompressed2 = new byte[uncompressedLength];
			//new DataInputStream(chunkStream).readFully(uncompressed2);
			//System.out.println(Arrays.equals(uncompressed2, uncompressed));
			//chunkStream = new ByteArrayInputStream(uncompressed2);
			
			//System.out.println("/home/jan/test-" + nextChunkPosition + ".nbt");
			//chunkStream = new FileInputStream("/home/jan/test-" + nextChunkPosition + ".nbt");
			break;
		default:
			throw new InvalidSaveFormatException(
					"Unsupported compression type: " + compression);
		}
		
		return chunkStream;
	}

	public boolean hasMore() {
		return chunkPositions.size() > 0;
	}

	@Override
	public void close() throws IOException {
		file.close();
	}
}
