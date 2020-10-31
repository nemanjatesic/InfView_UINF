package app;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.Frame1;

public class Main {
	public static File directory;
	public static File currFile;
	public static boolean saved;

	public static void main(String[] args) {

		// stavljamo look and feel ako moze
		try {
			System.out.println(UIManager.getSystemLookAndFeelClassName());
			UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
			for (UIManager.LookAndFeelInfo look : looks) {
				System.out.println(look.getName());
				if (look.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(look.getClassName());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Frame1 frame1 = Frame1.getInstance();
		frame1.setUser("user");
		frame1.doWork();
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame1.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				File directory = new File("Tmp");
				File[] files = directory.listFiles();
				for (File file : files) {
					file.delete();
				}
				directory = new File("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke");
				files = directory.listFiles();
				if (files == null) return;
				for (File file : files) {
					if (file == null) continue;
					String ime = file.getName();
					for (char s : ime.toCharArray()) {
						if (s >= '0' && s <= '9') {
							file.delete();
							break;
						}
					}
				}
			}
		});

		
		
		// uzimamo userov default directory
		Main.directory = new File(System.getProperty("user.home"));
		Main.currFile = null;
		Main.saved = true;
	}

}
