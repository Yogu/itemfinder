package de.yogularm.minecraft.itemfinder.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.InflaterInputStream;


public class AnvilReader implements AutoCloseable {
	private RandomAccessFile file;
	private int chunkCount = 0;
	
	private static final int SECTOR_SIZE = 4096;

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
		chunkCount = 0;
		for (int i = 0; i < 1024; i++) {
			int pos = file.readUnsignedByte() * 0x10000 + file.readUnsignedByte() * 0x100
					+ file.readUnsignedByte();
			file.readByte(); // length
			if (pos > 0) { // 0 means chunk does not exist
				chunkPositions.add(pos);
				chunkCount++;
			}
		}
	}
	
	public int getChunkCount() {
		return chunkCount;
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
			chunkStream = new BufferedInputStream(new InflaterInputStream(rawChunkStream));
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
