package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.yogularm.minecraft.itemfinder.region.ProgressListener;
import de.yogularm.minecraft.itemfinder.region.World;

public class WorldViewer {
	private JPanel component;
	private JLabel progressLabel;	
	private ItemList itemList;
	private Thread loadThread;
	private JProgressBar progressBar;
	
	private static final int PROGRESS_RESOLUTION = 10000;
	
	public WorldViewer() {
		initUI();
	}
	
	public Component getComponent() {
		return component;
	}
	
	private void initUI() {
		component = new JPanel(new BorderLayout(10, 10));
		
		JPanel topPanel = new JPanel(new BorderLayout(10, 10));
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(200, 15));
		topPanel.add(progressBar, BorderLayout.EAST);
		progressBar.setVisible(false);
		progressBar.setMaximum(PROGRESS_RESOLUTION);

		progressLabel = new JLabel("Please select a world");
		progressLabel.setFont(progressLabel.getFont().deriveFont(Font.BOLD));
		topPanel.add(progressLabel, BorderLayout.CENTER);
		
		component.add(topPanel, BorderLayout.NORTH);
		itemList = new ItemList();
		component.add(itemList.getComponent(), BorderLayout.CENTER);
	}
	
	public void setSave(final World world) {
		progressLabel.setText(world.getDisplayName() + ": Loading...");
		if (loadThread != null && loadThread.isAlive())
			loadThread.interrupt();
		
		loadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					world.load(new WorldLoadProgressListener(world));
					if (!Thread.currentThread().isInterrupted()) {
						finishProgress();	
						progressLabel.setText(world.getDisplayName());
						itemList.setItems(world.getDimensions().get(0).getDroppedItems());
					}
				} catch (IOException e) {
					if (!Thread.currentThread().isInterrupted()) {
						finishProgress();
						progressLabel.setText("Error loading " + world.getDisplayName());
						JOptionPane.showMessageDialog(null,
								"Error loading world: " + e.getMessage());
					}
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		});
		initProgress();
		loadThread.setDaemon(true); // don't block program termination
		loadThread.start();
	}
	
	private void initProgress() {
		progressBar.setValue(0);
		progressBar.setVisible(true);
	}
	
	private void finishProgress() {
		progressBar.setVisible(false);
	}
	
	private class WorldLoadProgressListener implements ProgressListener {
		private World world;
		
		public WorldLoadProgressListener(World world) {
			this.world = world;
		}
		
		@Override
		public void onAction(String action) {
			if (!Thread.currentThread().isInterrupted())
				progressLabel.setText(world.getDisplayName() + ": " + action);
		}

		@Override
		public void onProgress(double progress) {
			if (!Thread.currentThread().isInterrupted())
				progressBar.setValue((int)(progress * PROGRESS_RESOLUTION));
		}
	}
}
