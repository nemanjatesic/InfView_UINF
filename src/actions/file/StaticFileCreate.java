package actions.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import view.Frame1;
import wrapper.MojPanel;
import model.file.UIAbstractFile;

public class StaticFileCreate {
	
	private static HashMap<String, Boolean> prviPut = new HashMap<>();
	private static ArrayList<String[]> dataTmp = new ArrayList<>();
	
	public static void doStuff(String upis) {
		UIAbstractFile file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		MojPanel panel = (MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent();
		
		Date date = new Date();
		String str = "";
		str += date.getTime();

		if (panel.getPathZaSek() != null && !panel.getPathZaSek().equals("")) {
			String path = "Tmp" + File.separator + panel.getPathZaSek() + ".stxt";
			boolean prvi = isPrviPut(panel.getPathZaSek());
			
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
				FileWriter fileWriter = new FileWriter(path, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter);
				
				if(!prvi)
					printWriter.println();
				printWriter.print(upis);
				printWriter.flush();
				printWriter.close();
				randomAccessFile.close();
			} catch (Exception e3) {
				e3.printStackTrace();
				return;
			}
			
		} else {
			File fileSek;
			File fileStxt;
			try {
				fileSek = new File("Tmp" + File.separator + str + ".sek");
				fileSek.createNewFile();
				fileStxt = new File("Tmp" + File.separator + str + ".stxt");
				fileStxt.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			
			String path = file.getPath() + File.separator + file.getHeaderName();
			File citanje = new File(path);
			
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile("Tmp" + File.separator + str + ".sek", "rw");
				FileWriter fileWriter = new FileWriter("Tmp" + File.separator + str + ".sek", true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter);
				
				Scanner sc = new Scanner(citanje);
				boolean bool = false;
				
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					if (bool) {
						printWriter.println();
						printWriter.print(line);
					}else {
						printWriter.print("path/" + str + ".stxt");
					}
					bool = true;
				}
				
				printWriter.println();
				printWriter.print("field/TIP PROMENE/TYPE_CHAR/1/false");
				
				sc.close();
				printWriter.flush();
				printWriter.close();
				randomAccessFile.close();
			}catch (Exception e2) {
				e2.printStackTrace();
				return;
			}
			
			panel.setPathZaSek(str);
			
			String path1 = "Tmp" + File.separator + panel.getPathZaSek() + ".stxt";
			boolean prvi = isPrviPut(panel.getPathZaSek());
			
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(path1, "rw");
				FileWriter fileWriter = new FileWriter(path1, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter);
				
				if(!prvi)
					printWriter.println();
				printWriter.print(upis);
				printWriter.flush();
				printWriter.close();
				randomAccessFile.close();
			} catch (Exception e3) {
				e3.printStackTrace();
				return;
			}
		}
	}
	
	
	private static boolean isPrviPut(String string) {
		if(prviPut.containsKey(string)) {
			return false;
		}
		prviPut.put(string, false);
		return true;
	}
	
	public static ArrayList<String[]> getDataTmp() {
		return dataTmp;
	}
}
