package de.yogularm.minecraft.itemfinder.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.yogularm.minecraft.itemfinder.profiles.MinecraftSave;

public class WorldSelector extends Observable {
	private JPanel component;
	private JList<MinecraftSave> list;
	private DefaultListModel<MinecraftSave> model;
	
	public WorldSelector() {
		initUI();
	}
	
	public Component getComponent() {
		return component;
	}
	
	public void setSaves(List<MinecraftSave> saves) {
		model.clear();
		for (MinecraftSave save : saves) {
			model.addElement(save);
		}
	}
	
	public MinecraftSave getSelectedSave() {
		return list.getSelectedValue();
	}
	
	private void initUI() {
		model = new DefaultListModel<>();
		list = new JList<>(model);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				setChanged();
				notifyObservers();
			}
		});
		
		component = new JPanel(new BorderLayout());
		component.add(new JScrollPane(list), BorderLayout.CENTER);
	}
}
