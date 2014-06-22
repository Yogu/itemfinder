package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import de.yogularm.minecraft.itemfinder.profiles.MinecraftSave;
import de.yogularm.minecraft.itemfinder.profiles.ProfilesCollection;

public class GUI {
	private JFrame frame;
	private SaveViewer saveViewer;
	private WorldSelector selector;

	public GUI() {
		setLookAndFeel();
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
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.setContentPane(mainPanel);
		frame.setSize(950, 700);
		frame.setLocationRelativeTo(null); // center
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
	
	private void setLookAndFeel() {
		try {
			// Set System L&F
		    UIManager.setLookAndFeel(
		        UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException|ClassNotFoundException|InstantiationException|
				IllegalAccessException e) {
			e.printStackTrace();
			//just use the default look and feel
		}
	}
}
