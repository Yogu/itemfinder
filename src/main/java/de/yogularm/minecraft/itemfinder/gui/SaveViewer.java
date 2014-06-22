package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.yogularm.minecraft.itemfinder.profiles.MinecraftSave;
import de.yogularm.minecraft.itemfinder.region.World;

public class SaveViewer {
	private JPanel component;
	private JLabel progressLabel;	
	private ItemList itemList;
	private Thread loadThread;
	
	public SaveViewer() {
		initUI();
	}
	
	public Component getComponent() {
		return component;
	}
	
	private void initUI() {
		component = new JPanel(new BorderLayout(10, 10));
		progressLabel = new JLabel("Please select a world");
		component.add(progressLabel, BorderLayout.NORTH);
		itemList = new ItemList();
		component.add(itemList.getComponent(), BorderLayout.CENTER);
	}
	
	public void setSave(final MinecraftSave save) {
		progressLabel.setText("Loading " + save.getDisplayName() + "...");
		if (loadThread != null && loadThread.isAlive())
			loadThread.interrupt();
		
		loadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				World world;
				try {
					world = save.loadWorld();
					if (!Thread.currentThread().isInterrupted()) {
						progressLabel.setText(save.getDisplayName());
						itemList.setItems(world.getOverworld().getDroppedItems());
					}
				} catch (IOException e) {
					if (!Thread.currentThread().isInterrupted()) {
						progressLabel.setText("Error loading " + save.getDisplayName());
						JOptionPane.showMessageDialog(null,
								"Error loading world: " + e.getMessage());
					}
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		});
		loadThread.setDaemon(true); // don't block program termination
		loadThread.start();
	}
}
