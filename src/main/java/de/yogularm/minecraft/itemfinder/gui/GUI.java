package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import de.yogularm.minecraft.itemfinder.profiles.ProfilesCollection;
import de.yogularm.minecraft.itemfinder.region.World;

public class GUI {
	private JFrame frame;
	private WorldViewer worldViewer;
	private WorldSelector selector;

	public GUI() {
		setLookAndFeel();
		initUI();
		
		ProfilesCollection profiles;
		try {
			profiles = ProfilesCollection.getDefault();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Error loading minecraft profiles: " + e.getMessage() + 
					"  You can still open individual saves using the Browse... button.",
					"Error", JOptionPane.ERROR_MESSAGE);
			profiles = new ProfilesCollection();
		}

		selector.setWorlds(profiles.getWorlds());
	}

	private void initUI() {
		frame = new JFrame("itemfinder");
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.setContentPane(mainPanel);
		frame.setSize(950, 700);
		frame.setLocationRelativeTo(null); // center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel topBar = new JPanel(new BorderLayout(10, 10));
		topBar.add(new JLabel("Just died in Minecraft and can't find your stuff? I'll tell you exactly where it is!"), BorderLayout.CENTER);
		JButton helpButton = new JButton("More Info");
		topBar.add(helpButton, BorderLayout.EAST);
		mainPanel.add(topBar, BorderLayout.NORTH);
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				InfoDialog.show();
			}
		});

		worldViewer = new WorldViewer();
		selector = new WorldSelector();

		selector.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				World world = selector.getSelectedWorld();
				if (world != null) {
					worldViewer.setSave(world);
				}
			}
		});

		mainPanel.add(selector.getComponent(), BorderLayout.WEST);
		mainPanel.add(worldViewer.getComponent(), BorderLayout.CENTER);
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
