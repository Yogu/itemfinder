package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.yogularm.minecraft.itemfinder.profiles.MinecraftSave;
import de.yogularm.minecraft.itemfinder.profiles.ProfilesCollection;
import de.yogularm.minecraft.itemfinder.region.World;

public class GUI {
	private JFrame frame;
	private SaveViewer saveViewer;
	private WorldSelector selector;

	public GUI() {
		initUI();

		try {
			ProfilesCollection profiles = new ProfilesCollection(Paths.get(
					System.getProperty("user.home")).resolve(".minecraft"));
			selector.setSaves(profiles.getSaves());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Error loading minecraft profiles: " + e.getMessage());
		}
	}

	private void initUI() {
		frame = new JFrame("itemfinder");
		JPanel mainPanel = new JPanel(new BorderLayout());
		frame.setContentPane(mainPanel);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		saveViewer = new SaveViewer();
		selector = new WorldSelector();

		selector.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				MinecraftSave save = selector.getSelectedSave();
				if (save != null) {
					saveViewer.setSave(save);
				}
			}
		});

		mainPanel.add(selector.getComponent(), BorderLayout.WEST);
		mainPanel.add(saveViewer.getComponent(), BorderLayout.CENTER);
	}

	public void show() {
		frame.setVisible(true);
	}
}
