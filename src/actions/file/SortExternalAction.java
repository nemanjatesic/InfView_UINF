package actions.file;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import model.Atribut;
import model.TipAtributa;
import model.file.UIAbstractFile;
import model.file.UISEKFile;
import state.SortState;

public class SortExternalAction extends AbstractActionI
{

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (Frame1.getInstance().getPanelRT().getSelectedEntitet() == null)
			return;
		UIAbstractFile file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (file instanceof UISEKFile)
		{
			ArrayList<Atribut> atributi = Frame1.getInstance().getPanelRT().getSelectedEntitet().getAtributi();

			boolean[] isBroj = new boolean[atributi.size()];

			for (int i = 0; i < atributi.size(); i++)
			{
				if (atributi.get(i).getTip() == TipAtributa.Numeric)
					isBroj[i] = true;
				else
					isBroj[i] = false;
			}

			int numOfFiles = ((UISEKFile) file).MakeFilesForSort();
			int remaining = numOfFiles;
			System.out.println(numOfFiles);
			int arr[] = ((SortState) Frame1.getInstance().getStateManager().getCurrentState()).getKriterijum();
			for (int i = 1; i <= numOfFiles; i++)
			{
				UISEKFile sekF = new UISEKFile("sort", i + ".sek", false);
				try
				{
					sekF.readHeader();
					((UISEKFile) file).sortFiles(sekF, arr, true, isBroj);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			int start = 1;
			while (remaining > 1)
			{
				numOfFiles++;
				UISEKFile sekF1 = new UISEKFile("sort", start + ".sek", false);
				start++;
				UISEKFile sekF2 = new UISEKFile("sort", start + ".sek", false);
				start++;
				try
				{
					sekF1.readHeader();
					sekF2.readHeader();
					((UISEKFile) file).merge2Files(sekF1, sekF2, arr, isBroj, numOfFiles);
					remaining--;
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			File directory = new File("sort");
			File[] files = directory.listFiles();
			long seconds = System.currentTimeMillis() / 1000l;
			String ss="";
			for (File f : files)
			{
				if (f.getName().contains(numOfFiles + "") == false)
					f.delete();
				else
				{
					boolean sekFlag = true;
					if (f.getName().contains("stxt"))
						sekFlag = false;
					ss = file.getFileName();
					ss = ss.replace(".sek", "");
					ss = ss.replace(".stxt", "");
					if (sekFlag)
					{
						ArrayList<String> list = new ArrayList<>();
						try
						{
							Scanner sc = new Scanner(f);
							while (sc.hasNextLine())
							{
								list.add(sc.nextLine());
							}
							sc.close();
							String str = "path/" + ss + "Sortirano" + seconds + ".stxt";
							RandomAccessFile afile = new RandomAccessFile(f.getAbsolutePath(), "rw");
							FileWriter fw = new FileWriter(f.getAbsolutePath(), false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter pw = new PrintWriter(bw);
							pw.print(str);
							pw.println();
							for (int i = 1; i < list.size() - 1; i++)
							{
								pw.print(list.get(i));
								pw.println();
							}
							pw.print(list.get(list.size() - 1));
							pw.close();
							bw.close();
							afile.close();
						}
						catch (FileNotFoundException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					f.renameTo(new File("Veliki Set Podataka" + File.separator + "Sekvencijalne Datoteke" + File.separator + ss + "Sortirano" + seconds
							+ (sekFlag ? ".sek" : ".stxt")));
				}
			}
			System.out.println(numOfFiles);
			MessageController.infoMessage(
					"Fajl uspesno sortiran i nalazi se u direktorijumu Veliki set podataka/Sekvencijalne Datoteke pod imenom "
							+ ss + "Sortirano" + seconds);
		}
	}

}
