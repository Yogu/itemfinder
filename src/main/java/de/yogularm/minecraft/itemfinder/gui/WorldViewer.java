package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import de.yogularm.minecraft.itemfinder.region.ProgressListener;
import de.yogularm.minecraft.itemfinder.region.World;

public class WorldViewer {
	private JPanel component;
	private JLabel progressLabel;	
	private ItemList itemList;
	private Thread loadThread;
	private JProgressBar progressBar;
	private World world;
	private DimensionSelector dimensionSelector;
	private JPanel toolbar;
	
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
		
		JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		topPanel.add(topRightPanel, BorderLayout.EAST);
		
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(300, 30));
		progressBar.setVisible(false);
		progressBar.setMaximum(PROGRESS_RESOLUTION);
		topRightPanel.add(progressBar);
		
		toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		toolbar.setVisible(false);
		topRightPanel.add(toolbar);
		
		dimensionSelector = new DimensionSelector();
		toolbar.add(dimensionSelector.getComponent());
		dimensionSelector.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				itemList.setItems(dimensionSelector.getSelection().getDroppedItems());
			}
		});
		
		reloadButton = new JButton("Reload");
		reloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadWorld(world, true /* force reload */);
			}
		});
		// spacing between button and combobox
		JPanel reloadButtonPanel = new JPanel(new BorderLayout());
		reloadButtonPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		reloadButtonPanel.add(reloadButton);
		toolbar.add(reloadButtonPanel);

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
		toolbar.setVisible(true);
		progressLabel.setText(world.getDisplayName());
		dimensionSelector.setDimensions(world.getDimensions());
	}
	
	public void setWorld(final World world) {
		loadWorld(world, false);
	}
	
	private void initProgress() {
		progressBar.setValue(0);
		progressBar.setVisible(true);
		toolbar.setVisible(false);
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
