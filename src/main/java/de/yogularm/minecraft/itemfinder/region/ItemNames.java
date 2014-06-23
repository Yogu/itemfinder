package de.yogularm.minecraft.itemfinder.region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ItemNames {
	private static Map<Integer, String> itemNames;
	private static Map<String, Integer> ordinalsOfIDs;
	private static ItemNameProvider nameProvider;
	
	static {
		try {
			itemNames = new HashMap<>();
			try (InputStream stream = ItemNames.class
					.getResourceAsStream("items.tsv")) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream));
				while (true) {
					String line = reader.readLine();
					if (line == null || line.equals(""))
						break;

					String parts[] = line.split("\\t");
					int itemID = Integer.parseInt(parts[0]);
					int damageValue = Integer.parseInt(parts[1]);
					String name = parts[2];
					itemNames.put(getKey(itemID, damageValue), name);
				}
			}

			ordinalsOfIDs = new HashMap<>();
			try (InputStream stream = ItemNames.class
					.getResourceAsStream("item-ids.tsv")) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream));
				while (true) {
					String line = reader.readLine();
					if (line == null || line.equals(""))
						break;

					String parts[] = line.split("\\t");
					int ordinal = Integer.parseInt(parts[0]);
					String id = parts[1];
					ordinalsOfIDs.put(id, ordinal);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		nameProvider = new ItemNameProvider() {
			@Override
			public String getItemName(int itemID, int damageValue) {
				int key = getKey(itemID, damageValue);
				if (itemNames.containsKey(key))
					return itemNames.get(key);
				key = getKey(itemID, 0);
				if (itemNames.containsKey(key))
					return itemNames.get(key);
				return "Item " + itemID;
			}

			@Override
			public boolean hasNameFor(int itemID) {
				return itemNames.containsKey(getKey(itemID, 0));
			}
		};
	}
	
	public static ItemNameProvider getNameProvider() {
		return nameProvider;
	}
	
	public static Integer getOrdinalFromID(String id) {
		return ordinalsOfIDs.get(id);
	}
	
	private static int getKey(int itemID, int damageValue) {
		return itemID * 0x10000 + damageValue;
	}
}
