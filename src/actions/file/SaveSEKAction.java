package actions.file;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;
import model.Atribut;
import model.TipAtributa;
import model.file.UIAbstractFile;
import model.file.UIFileField;
import model.file.UISEKFile;

public class SaveSEKAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent e) {
		MojPanel mojPanel = (MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent();
		if (mojPanel.getPathZaSek() == null || mojPanel.getPathZaSek().equals("")) {
			MessageController.errorMessage("Nista jos napravili nikakve izmene nad ovim fajlom.");
			return;
		}
		UISEKFile file = new UISEKFile("Tmp", mojPanel.getPathZaSek() + ".sek", false);

		try {
			file.readHeader();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<UIFileField> fields = ((UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet()).getFields();
		int arr[] = new int[fields.size() + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = 0;
			if (fields.size() == i)
				continue;
			if (fields.get(i).isFieldPK())
				arr[i] = 1;
		}
		ArrayList<Atribut> atributi = Frame1.getInstance().getPanelRT().getSelectedEntitet().getAtributi();

		int kolikoPrimarnih = 0;

		for (int i = 0; i < atributi.size(); i++)
			if (atributi.get(i).isPrimarniKljuc())
				kolikoPrimarnih++;

		int[] duzine = new int[atributi.size()];
		boolean[] isBroj = new boolean[atributi.size() + 1];
		int i;

		for (int j = 0; j < atributi.size(); j++)
			duzine[j] = atributi.get(j).getDuzina();

		for (i = 0; i < atributi.size(); i++) {
			if (atributi.get(i).getTip() == TipAtributa.Numeric)
				isBroj[i] = true;
			else
				isBroj[i] = false;
		}
		isBroj[i] = false;

		file.sortFiles(file, arr, false, isBroj);
		file.setBLOCK_SIZE(1);
		file.setFILE_POINTER(0);
		String path = file.getPath() + File.separator + file.getHeaderName().replaceAll(".sek", ".stxt");

		UIAbstractFile absFile = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();

		String[] split = Frame1.getInstance().getPanelRT().getSelectedEntitet().getUrl().split("/");

		String last = split[1].replaceAll(".sek", ".stxt");

		File fileBig = new File(absFile.getPath() + File.separator + last);
		File fileSmall = new File(path);

		try {
			/* fixovani ovaj cao */
			
			RandomAccessFile randomAccessFileError = new RandomAccessFile("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + "errorLog.txt", "rw");
			FileWriter fileWriterError = new FileWriter("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + "errorLog.txt", true);
			BufferedWriter bufferedWriterError = new BufferedWriter(fileWriterError);
			PrintWriter printWriterError = new PrintWriter(bufferedWriterError);
			
			Date date = new Date();
			long brojDate = date.getTime();
			
			String stringPut = absFile.getHeaderName().replaceAll(".sek", "") + brojDate;
			
			
			/* Za kopiranje .sek fajla */
			File fileSrc = new File("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + absFile.getHeaderName());
			File fileDest = new File("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + stringPut + ".sek");
			
			if (!fileDest.exists()) fileDest.createNewFile();
			/* Za kopiranje .sek fajla */
			
			RandomAccessFile randomAccessFile = new RandomAccessFile("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + stringPut + ".stxt", "rw");
			FileWriter fileWriter = new FileWriter("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + stringPut + ".stxt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);

			Scanner scBig = new Scanner(fileBig);
			Scanner scSmall = new Scanner(fileSmall);
			
			UISEKFile sekBig = new UISEKFile("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke", absFile.getHeaderName(), false);
			UISEKFile sekSmall = new UISEKFile("Tmp", file.getHeaderName(), false);
			
			sekBig.readHeader();
			sekSmall.readHeader();
			
			boolean bigBool = true;
			boolean smallBool = true;
			boolean prvi = true;
			boolean prviError = true;

			String big = "";
			String small = "";
			
			while (((sekBig.getFILE_POINTER() < sekBig.getFILE_SIZE()) || !big.equals("")) && ((sekSmall.getFILE_POINTER() < sekSmall.getFILE_SIZE()) || !small.equals(""))) {
				printWriter.flush();
				printWriterError.flush();
				if (bigBool)
					big = ucitaj(sekBig);
				if (smallBool)
					small = ucitaj(sekSmall);
				
				String smallNoLast = getWithOutLast(small);
				
				boolean jednaki = false;
				jednaki = jednaki(big, small);

				if (small.charAt(small.length() - 1) == 'A') {
					if (jednaki) {
						if (!prvi)
							printWriter.println();
						printWriter.print(big);
						printWriter.println();
						printWriter.print(big);
						prvi = false;
						smallBool = true;
						bigBool = true;
						small = "";
						big = "";
						continue;
					}
					if (compare(big, small, duzine, isBroj)) {
						if (!prvi)
							printWriter.println();
						printWriter.print(big);
						prvi = false;
						smallBool = false;
						bigBool = true;
						big = "";
						continue;
					} else {
						if (!prvi)
							printWriter.println();
						printWriter.print(smallNoLast);
						prvi = false;
						smallBool = true;
						bigBool = false;
						small = "";
						continue;
					}
				} else if (small.charAt(small.length() - 1) == 'E') {
					if (jednaki) {
						if (!prvi)
							printWriter.println();
						printWriter.print(smallNoLast);
						prvi = false;
						smallBool = true;
						bigBool = true;
						small = "";
						big = "";
						continue;
					}
					if (checkIfPrimaryKeySame(big,small,duzine,isBroj,kolikoPrimarnih)) {
						if (!prvi)
							printWriter.println();
						printWriter.print(smallNoLast);
						prvi = false;
						smallBool = true;
						bigBool = true;
						small = "";
						big = "";
						continue;
					}
					if (compare(big, small, duzine, isBroj)) {
						if (!prvi)
							printWriter.println();
						printWriter.print(big);
						prvi = false;
						smallBool = false;
						bigBool = true;
						big = "";
						continue;
					} else {
						smallBool = true;
						bigBool = false;
						if (!prviError)
							printWriterError.println();
						printWriterError.print(small);
						prviError = false;
						small = "";
						continue;
					}
				} else if (small.charAt(small.length() - 1) == 'D') {
					if (jednaki) {
						bigBool = true;
						smallBool = true;
						small = "";
						big = "";
						continue;
					}
					if (compare(big, small, duzine, isBroj)) {
						if (!prvi)
							printWriter.println();
						printWriter.print(big);
						prvi = false;
						smallBool = false;
						bigBool = true;
						big = "";
						continue;
					} else {
						smallBool = true;
						bigBool = false;
						if (!prviError)
							printWriterError.println();
						printWriterError.print(small);
						prviError = false;
						small = "";
						continue;
					}
				}
			}
			while ((sekBig.getFILE_POINTER() < sekBig.getFILE_SIZE())) {
				printWriter.flush();
				big = ucitaj(sekBig);
				if (!prvi) {
					printWriter.println();
				}
				printWriter.print(big);
			}
			printWriter.flush();
			printWriter.close();
			scSmall.close();
			scBig.close();
			printWriterError.flush();
			printWriterError.close();
			randomAccessFileError.close();
			randomAccessFile.close();
			
			/* Prepisivanje .sek fajla */
			FileInputStream fipSrc = new FileInputStream(fileSrc);
			FileOutputStream fipDest = new FileOutputStream(fileDest);
			FileChannel src = fipSrc.getChannel();
            FileChannel dest = fipDest.getChannel();
            
            dest.transferFrom(src, 0, src.size());
            
            fipDest.close();
            fipSrc.close();
            dest.close();
            src.close();
            /* Prepisivanje .sek fajla */
            
            ArrayList<String> nizStringova = new ArrayList<>();
            Scanner scannerPromena = new Scanner(fileDest);
            while(scannerPromena.hasNextLine()) {
            	nizStringova.add(scannerPromena.nextLine());
            }
            scannerPromena.close();
            
            String zapamcen = fileDest.getAbsolutePath();
            
            RandomAccessFile rafFile = new RandomAccessFile(zapamcen, "rw");
            FileWriter fileWriterRaf = new FileWriter(zapamcen, false);
			BufferedWriter bufferedWriterRaf = new BufferedWriter(fileWriterRaf);
			PrintWriter printWriterRaf = new PrintWriter(bufferedWriterRaf);
			
			String pathString = "path/" + fileDest.getName().replaceAll(".sek", ".stxt");
			printWriterRaf.print(pathString);
			printWriterRaf.println();
			
			for (int j = 1 ; j < nizStringova.size() - 1; j++) {
				printWriterRaf.print(nizStringova.get(j));
				printWriterRaf.println();
			}
           	
			printWriterRaf.print(nizStringova.get(nizStringova.size() - 1));
			
			printWriterRaf.close();
        	rafFile.close();
        	
        	MessageController.infoMessage("Sve promene se uspesno sacuvane. Mozete ih pronaci u : Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke.");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private String ucitaj(UISEKFile sekFile) {
		String str = "";
		sekFile.setBLOCK_SIZE(1);
		try {
			sekFile.fetchNextBlock(false);
			for (int i = 0 ; i < sekFile.getData()[0].length ; i++) {
				str += sekFile.getData()[0][i];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private boolean checkIfPrimaryKeySame(String a, String b, int[] duzine, boolean[] isBroj, int kolikoPrimarnih) {
		char[] arrA = a.toCharArray();
		char[] arrBtmp = b.toCharArray();
		char[] arrB = new char[a.length()];

		for (int i = 0; i < a.length(); i++)
			arrB[i] = arrBtmp[i];

		int[] prefiksna = new int[duzine.length + 1];
		prefiksna[0] = 0;
		prefiksna[1] = duzine[0];

		for (int i = 2; i < duzine.length + 1; i++)
			prefiksna[i] = prefiksna[i - 1] + duzine[i - 1];

		for (int i = 0; i < kolikoPrimarnih; i++) {
			String prvi = "";
			String drugi = "";

			for (int k = prefiksna[i]; k < prefiksna[i + 1]; k++) {
				prvi += arrA[k];
				drugi += arrB[k];
			}
			prvi = prvi.trim();
			drugi = drugi.trim();

			if (isBroj[i]) {
				int aa = Integer.parseInt(prvi);
				int bb = Integer.parseInt(drugi);
				if (aa == bb)
					continue;
				else
					return false;
			} else {
				if (prvi.compareTo(drugi) == 0)
					continue;
				else
					return false;
			}
		}
		return true;
	}

	private String getWithOutLast(String a) {
		String ret = "";
		char[] arrB = a.toCharArray();

		for (int i = 0; i < arrB.length - 1; i++)
			ret += arrB[i];

		return ret;
	}

	// Vraca true ako je a < b "A" i "B" vraca true
	private boolean compare(String a, String b, int[] duzine, boolean[] isBroj) {
		char[] arrA = a.toCharArray();
		char[] arrBtmp = b.toCharArray();
		char[] arrB = new char[a.length()];

		for (int i = 0; i < a.length(); i++)
			arrB[i] = arrBtmp[i];

		int[] prefiksna = new int[duzine.length + 1];
		prefiksna[0] = 0;
		prefiksna[1] = duzine[0];

		for (int i = 2; i < duzine.length + 1; i++)
			prefiksna[i] = prefiksna[i - 1] + duzine[i - 1];

		for (int i = 0; i < duzine.length; i++) {
			String prvi = "";
			String drugi = "";

			for (int k = prefiksna[i]; k < prefiksna[i + 1]; k++) {
				prvi += arrA[k];
				drugi += arrB[k];
			}
			prvi = prvi.trim();
			drugi = drugi.trim();

			if (isBroj[i]) {
				if(prvi.equals("") || drugi.equals("")) continue;
				int aa = Integer.parseInt(prvi);
				int bb = Integer.parseInt(drugi);
				if (aa == bb)
					continue;
				if (aa > bb)
					return false;
				return true;
			} else {
				if (prvi.compareTo(drugi) == 0)
					continue;
				if (prvi.compareTo(drugi) > 0)
					return false;
				return true;
			}
		}
		return true;
	}

	private boolean jednaki(String a, String b) {
		char[] arrA = a.toCharArray();
		char[] arrB = b.toCharArray();

		for (int i = 0; i < arrA.length; i++)
			if (arrA[i] != arrB[i])
				return false;

		return true;
	}

}
