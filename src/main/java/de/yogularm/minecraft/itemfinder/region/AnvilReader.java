package de.yogularm.minecraft.itemfinder.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.InflaterInputStream;


public class AnvilReader implements AutoCloseable {
	private DataInputStream stream;
	private int currentPosition;

	private static final int SECTOR_SIZE = 4096;

	/**
	 * Stores the positions where there are chunks
	 */
	private SortedSet<Integer> chunkPositions;

	public AnvilReader(InputStream stream) throws IOException {
		this.stream = new DataInputStream(stream);
		readMetadata();
	}

	private void readMetadata() throws IOException {
		chunkPositions = new TreeSet<>();
		for (int i = 0; i < 1024; i++) {
			int pos = stream.readByte() * 0x1000 + stream.readByte() * 0x100
					+ stream.readByte();
			stream.skip(1); // length
			if (pos > 0) // 0 means chunk does not exist
				chunkPositions.add(pos);
		}

		// skip timestamps
		stream.skip(4096);

		currentPosition = 2 * 4096; // after metadata
	}

	public InputStream readChunkColumn() throws IOException {
		if (chunkPositions.size() == 0)
			throw new IllegalStateException("No more chunks available");

		int nextChunkPosition = chunkPositions.first();
		chunkPositions.remove(nextChunkPosition);

		// skip to next chunk
		stream.skip(nextChunkPosition * SECTOR_SIZE - currentPosition);

		// read the raw data
		int length = stream.readInt() - 1; // one byte for the compression
		byte compression = stream.readByte();
		byte[] rawChunkData = new byte[length];
		stream.readFully(rawChunkData);
		currentPosition = nextChunkPosition * SECTOR_SIZE + length + 5;

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
		stream.close();
	}
}
