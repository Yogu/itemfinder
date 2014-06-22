package de.yogularm.minecraft.itemfinder.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;

import de.yogularm.minecraft.itemfinder.region.DroppedItem;

public class ItemList {
	private Component component;
	private JXTable table;
	private ItemTableModel model;
	private TableRowSorter<ItemTableModel> sorter;

	public ItemList() {
		initUI();
	}

	private void initUI() {
		model = new ItemTableModel();
		table = new JXTable(model);
		sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);
		sorter.setSortKeys(Arrays.asList(
				new RowSorter.SortKey(4, SortOrder.DESCENDING),
				new RowSorter.SortKey(3, SortOrder.ASCENDING)));
		
		// items with age 0 should be at the bottom
		sorter.setComparator(3, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 == 0)
					o1 = Integer.MAX_VALUE;
				if (o2 == 0)
					o2 = Integer.MAX_VALUE;
				return Integer.compare(o1, o2);
			}
		});
		
		table.getColumnModel().getColumn(0).setWidth(200);

		component = new JScrollPane(table);
	}

	public void setItems(List<DroppedItem> items) {
		model.setItems(items);
		table.packAll(); // adjust column widths
	}

	public Component getComponent() {
		return component;
	}

	private static class ItemTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1904800093497922295L;

		private static final String[] COLUMNS = new String[] { "Item Name",
				"Stack Size", "Location", "Age", "Chunk Update" };

		private List<DroppedItem> items;

		public ItemTableModel() {
			this.items = new ArrayList<>();
		}

		public void setItems(List<DroppedItem> items) {
			this.items = items;
			fireTableDataChanged();
		}

		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public String getColumnName(int column) {
			return COLUMNS[column];
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex >= items.size())
				return "";
			DroppedItem item = items.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return item.getCount();
			case 2:
				return item.getPosition().toRoundedString();
			case 3:
				return item.getAge();
			case 4:
				return item.getLastChunkUpdate();
			default:
				return "";
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
			case 2:
				return String.class;
			case 3:
				return Integer.class;
			case 4:
				return Integer.class;
			default:
				return null;
			}
		}
	}
}
