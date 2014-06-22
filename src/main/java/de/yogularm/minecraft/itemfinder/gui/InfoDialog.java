package de.yogularm.minecraft.itemfinder.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

public class InfoDialog {
	private static final String TEXT = "<html><body style=\"width: 500px; \">" +
			"<p><b>Minecraft itemfinder © Jan Melcher, 2014.</b></p>" +
			"<p>This tool scans your minecraft worlds for items that lie on the ground " +
			"and displays them in a sortable list. The default ordering shows items that " +
			"have been recently dropped at the top, so if you just died, your stuff " +
			"should be easy to find. The coordinates will then tell you exactly where you " +
			"have to look for them. Keep an eye on the age, if it reaches 5:00, " +
			"the item will despawn.</p>" +
			"<p>Tested with Minecraft 1.6 and 1.7 as well as TerraFirmaCraft. " +
			"If you're having trouble, write in the forum or open an issue on github.</p>" +
			"<p>Source code available on <a href=\"https://github.com/Yogu/itemfinder\">GitHub</a> " +
			"under the <a href=\"https://github.com/Yogu/itemfinder/blob/master/LICENSE\">Apache License 2.0</a>. " +
			"Contributions are welcome!</p>";

	public static void show() {
		JEditorPane pane = new JEditorPane("text/html", TEXT);
		pane.setEditable(false);
		pane.setHighlighter(null); // no selection
		pane.setOpaque(false);
		pane.setBackground(new Color(0,0,0,0));
		pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		pane.setFont(new JLabel().getFont()); // set system font
		
		pane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error browsing link");
					} catch (URISyntaxException e1) {
						throw new RuntimeException(e1);
					}
				}
			}
		});
		JOptionPane.showMessageDialog(null, pane, "About itemfinder", JOptionPane.INFORMATION_MESSAGE);
	}
}
