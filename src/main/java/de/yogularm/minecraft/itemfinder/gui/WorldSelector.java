package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.List;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.yogularm.minecraft.itemfinder.region.World;

public class WorldSelector extends Observable {
	private JPanel component;
	private JList<World> list;
	private DefaultListModel<World> model;

	public WorldSelector() {
		initUI();
	}

	public Component getComponent() {
		return component;
	}

	public void setWorlds(List<World> worlds) {
		model.clear();
		for (World world : worlds) {
			model.addElement(world);
		}
	}

	public World getSelectedWorld() {
		return list.getSelectedValue();
	}

	private void initUI() {
		model = new DefaultListModel<>();
		list = new JList<>(model);
		list.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						setChanged();
						notifyObservers();
					}
				});

		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				browseForDir();
			}
		});

		component = new JPanel(new BorderLayout(10, 10));
		component.add(new JScrollPane(list), BorderLayout.CENTER);
		component.add(browseButton, BorderLayout.SOUTH);
	}

	private void browseForDir() {
		JFileChooser dialog = new JFileChooser();
		dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dialog.setDialogTitle("Select your minecraft save directory");
		if (dialog.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
			Path path = dialog.getSelectedFile().toPath();
			model.addElement(new World(path, "Manually added"));
			list.setSelectedIndex(model.size() - 1);
		}
	}
}
