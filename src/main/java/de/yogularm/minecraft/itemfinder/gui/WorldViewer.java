package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import de.yogularm.minecraft.itemfinder.region.ProgressListener;
import de.yogularm.minecraft.itemfinder.region.World;

public class WorldViewer {
	private JPanel component;
	private JLabel progressLabel;	
	private ItemList itemList;
	private Thread loadThread;
	private JProgressBar progressBar;
	private World world;
	
	private static final int PROGRESS_RESOLUTION = 10000;
	private JButton reloadButton;
	
	public WorldViewer() {
		initUI();
	}
	
	public Component getComponent() {
		return component;
	}
	
	private void initUI() {
		component = new JPanel(new BorderLayout(10, 10));
		
		JPanel topPanel = new JPanel(new BorderLayout(10, 10));
		component.add(topPanel, BorderLayout.NORTH);
		topPanel.setPreferredSize(new Dimension(100, 35)); // enough space for button and progress bar
		
		JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		topPanel.add(topRightPanel, BorderLayout.EAST);
		
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(300, 30));
		progressBar.setVisible(false);
		progressBar.setMaximum(PROGRESS_RESOLUTION);
		topRightPanel.add(progressBar);
		
		reloadButton = new JButton("Reload");
		reloadButton.setVisible(false);
		reloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadWorld(world, true /* force reload */);
			}
		});
		topRightPanel.add(reloadButton);

		progressLabel = new JLabel("Please select a world");
		progressLabel.setFont(progressLabel.getFont().deriveFont(Font.BOLD));
		topPanel.add(progressLabel, BorderLayout.CENTER);
		
		itemList = new ItemList();
		component.add(itemList.getComponent(), BorderLayout.CENTER);
	}
	
	private void loadWorld(final World world, boolean forceReload) {
		if (loadThread != null && loadThread.isAlive())
			loadThread.interrupt();
		
		if (!forceReload && world.isLoaded()) {
			onLoaded(world);
			return;
		}
		
		progressLabel.setText(world.getDisplayName() + ": Loading...");
		
		loadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					world.load(new WorldLoadProgressListener(world));					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (!Thread.currentThread().isInterrupted())
								onLoaded(world);
						}
					});
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
	
	private void onLoaded(World world) {
		this.world = world;
		finishProgress();	
		reloadButton.setVisible(true);
		progressLabel.setText(world.getDisplayName());
		itemList.setItems(world.getDimensions().get(0).getDroppedItems());
	}
	
	public void setWorld(final World world) {
		loadWorld(world, false);
	}
	
	private void initProgress() {
		progressBar.setValue(0);
		progressBar.setVisible(true);
		reloadButton.setVisible(false);
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
