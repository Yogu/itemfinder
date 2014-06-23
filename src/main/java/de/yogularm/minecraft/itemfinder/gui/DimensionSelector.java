package de.yogularm.minecraft.itemfinder.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.yogularm.minecraft.itemfinder.region.Dimension;

public class DimensionSelector extends Observable {
	private JComboBox<Dimension> component;
	private Dimension currentSelection;
	
	public DimensionSelector() {
		initUI();
	}
	
	private void initUI() {
		component = new JComboBox<>();
		component.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkChanged();
			}
		});
	}
	
	public void setDimensions(List<Dimension> dimensions) {
		component.setModel(new DefaultComboBoxModel<>(dimensions.toArray(new Dimension[0])));
		checkChanged();
	}
	
	private void checkChanged() {
		if (getSelection() != currentSelection) {
			currentSelection = getSelection();
			setChanged();
			notifyObservers();
		}
	}
	
	public Component getComponent() {
		return component;
	}
	
	public Dimension getSelection() {
		Object item = component.getSelectedItem();
		if (item == null)
			return null;
		return (Dimension)item;
	}
}
