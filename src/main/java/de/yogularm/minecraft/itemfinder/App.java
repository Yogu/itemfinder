package de.yogularm.minecraft.itemfinder;

import java.io.IOException;

import de.yogularm.minecraft.itemfinder.gui.GUI;

public class App {
	public static void main(String[] args) throws IOException {
		//ItemFinderCli.main(args);
		GUI gui = new GUI();
		gui.show();
	}
}
