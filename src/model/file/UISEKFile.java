package model.file;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.MessageController;
import wrapper.Wrapper;
import model.Atribut;
import model.Entitet;
import model.tree.KeyElement;
import model.tree.Node;
import model.tree.NodeElement;
import model.tree.Tree;

public class UISEKFile extends UIAbstractFile
{

	boolean prviPut = false;

	public UISEKFile(String path, String headerName, boolean directory) {
		super(path, headerName, directory);
	}

	public UISEKFile(String path, String headerName, boolean directory, String name) {
		super(path, headerName, directory, name);
	}

	/**
	 * Prenos bloka iz datoteke u radnu memoriju aplikacije Velicina bloka određena
	 * je atributom BLOCK_SIZE Po zavšetku metode blok podataka iz datoteke nalazi
	 * se u radnoj memoriji aplikaciji u matrici datа[][]
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	public boolean fetchNextBlock(boolean bool) throws IOException
	{

		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "r");
		FILE_SIZE = afile.length();
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;

		// BUFFER_SIZE je uvek jednak broju slogova u jednom bloku * velicina jednog
		// sloga
		// izuzev ako poslednji blok nije pun blok
		if (FILE_POINTER / RECORD_SIZE + BLOCK_FACTOR > RECORD_NUM)
			BUFFER_SIZE = (int) (RECORD_NUM - FILE_POINTER / RECORD_SIZE) * RECORD_SIZE;
		else
			BUFFER_SIZE = (int) (RECORD_SIZE * BLOCK_FACTOR);

		buffer = new byte[BUFFER_SIZE];
		data = new String[(int) BUFFER_SIZE / RECORD_SIZE][];
		// po otvaranju pozicioniram se na prethodni kraj zahvata
		afile.seek(FILE_POINTER);
		afile.read(buffer);
		String contentS = new String(buffer);
		if (contentS.length() < buffer.length)
		{
			for (int x = contentS.length(); x < buffer.length; x++)
				contentS = contentS + " ";
		}

		for (int i = 0; i < BUFFER_SIZE / RECORD_SIZE; i++)
		{

			String line = contentS.substring(i * RECORD_SIZE, i * RECORD_SIZE + RECORD_SIZE);
			data[i] = new String[fields.size()];
			int k = 0;
			for (int j = 0; j < fields.size(); j++)
			{
				String field = null;
				field = line.substring(k, k + fields.get(j).getFieldLength());
				data[i][j] = field;
				k = k + fields.get(j).getFieldLength();
			}

		}

		FILE_POINTER = afile.getFilePointer();
		afile.close();

		// ucitavanje novog bloka treba da izazove osvezivanje podataka u tabeli
		// fireUpdateBlockPerformed();
		Wrapper wr = new Wrapper(data);
		if (bool) notify(wr);
		return true;

	}

	/**
	 * Metoda za dodavanje sloga u sekvencijalnoj datoteci datoteke.
	 * 
	 */

	public boolean addRecord(ArrayList<String> record) throws IOException
	{

		boolean result = true;
		// dodavanje slogova treba poceti neuspensom pretragom po kljucu
		// argument trazenja su vrednosti obelezja koje se zele dodati
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < fields.size(); i++)
		{
			if (fields.get(i).isFieldPK())
			{
				if (record.get(i).trim().equals(""))
				{
					// nije uneta vrednost kljuca, ne mozemo nastaviti unos:

					JOptionPane.showMessageDialog(null, "Niste uneli vrednost obeležja " + fields.get(i).getFieldName(),
							"UI Project", 1);
					return false;
				}
				else
				{

					temp.add(record.get(i));
				}
			}
			else
			{
				temp.add(" ");
			}

		}
		int[] position = new int[1];
		position[0] = -1;
		if (findRecord(temp, position))
		{
			JOptionPane.showMessageDialog(null, "Slog sa istom vrednošću PK već postoji", "UI Project", 1);
			return false;

		}
		// slog sa istom vrednoscu kljuca ne postoji a u position se nalazi
		// relativne adresa lokacija na kojoj treba smestiti novi slog
		long oldFilePointer = (FILE_POINTER / RECORD_SIZE - BLOCK_FACTOR) * RECORD_SIZE;
		long newPosition = FILE_POINTER / RECORD_SIZE - BLOCK_FACTOR + position[0];

		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "rw");
		byte[] record_buffer = new byte[RECORD_SIZE];
		for (int i = (int) RECORD_NUM - 1; i >= newPosition; i--)
		{
			afile.seek(i * RECORD_SIZE);
			afile.read(record_buffer);
			afile.write(record_buffer);
		}
		// ostalo je još da dodamo novi slog
		String newRecord = "";
		for (int i = 0; i < record.size(); i++)
		{
			newRecord = newRecord + record.get(i);
		}

		newRecord = newRecord + "\r\n";
		afile.seek(newPosition * RECORD_SIZE);
		afile.writeBytes(newRecord);
		afile.close();
		FILE_POINTER = oldFilePointer;
		fetchNextBlock(false);
		return result;

	}

	/**
	*  
	*  
	*/
	public boolean updateRecord(ArrayList<String> record) throws IOException
	{

		return false;
	}

	/// splitovano je sa ___
	public static ArrayList<String> linearSearchZaNemanju(ArrayList<String> searchRec, UIAbstractFile file,
			ArrayList<UIFileField> fileFields)
	{
		ArrayList<String> ans = new ArrayList<>();
		long saveFP = (long) file.FILE_POINTER;
		file.FILE_POINTER = 0;
		while (file.FILE_POINTER < file.FILE_SIZE)
		{
			try
			{
				file.fetchNextBlock(false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return ans;
			}
			for (int row = 0; row < file.data.length; row++)
			{
				if (((UISEKFile) file).isRowEqual(file.data[row], searchRec, file, fileFields))
				{
					String tmp = "";
					for (int i = 0; i < file.data[row].length; i++)
					{
						String cpy = file.data[row][i];// .trim();
						tmp += cpy;
						if (i != file.data[row].length - 1)
							tmp += "___";// po ovom cu da splitujem posle
					}
					ans.add(tmp);
				}
			}
		}
		file.FILE_POINTER = saveFP;
		if (ans.size() == 0)
			return null;
		return ans;
	}

	private boolean isRowEqual(String[] aData, ArrayList<String> searchRec, UIAbstractFile f,
			ArrayList<UIFileField> fileFields)
	{
		boolean result = true;
		/*
		 * System.out.print("Here "); for (String s : aData) System.out.print(" " + s);
		 * System.out.println();
		 */
		for (int i = 0; i < fileFields.size(); i++)
		{
			for (int j = 0; j < f.getFields().size(); j++)
			{
				if (f.getFields().get(j).equals(fileFields.get(i)))
				{
					if (!searchRec.get(i).trim().equals(""))
					{
						if (!aData[j].trim().equals(searchRec.get(i).trim()))
						{
							result = false;
							return result;
						}
					}
				}
			}
		}
		/*
		 * for (int col = 0; col < searchRec.size(); col++) { if
		 * (!searchRec.get(col).trim().equals("")) { if
		 * (!aData[col].trim().equals(searchRec.get(col).trim())) { result = false;
		 * return result; } }
		 * 
		 * }
		 */
		return result;
	}

	public ArrayList<String> linearSearch(ArrayList<String> searchRec, boolean flagResetFilePointer,
			boolean flagStopAfterFirst)
	{
		ArrayList<String> ans = new ArrayList<>();
		long saveFP = (long) FILE_POINTER;
		if (flagResetFilePointer)
			FILE_POINTER = 0;
		while (FILE_POINTER < FILE_SIZE)
		{
			try
			{
				fetchNextBlock(false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return ans;
			}
			for (int row = 0; row < data.length; row++)
			{
				if (isRowEqual(data[row], searchRec))
				{
					String tmp = "";
					for (int i = 0; i < data[row].length; i++)
					{
						String cpy = data[row][i];// .trim();
						tmp += cpy;
						if (i != data[row].length - 1)
							tmp += "___";// po ovom cu da splitujem posle
					}
					ans.add(tmp);
					if (flagStopAfterFirst)
					{
						FILE_POINTER = saveFP; // vracam file pointer tamo gde je bio na pocetku
						return ans;
					}
				}
			}
		}
		FILE_POINTER = saveFP;
		if (ans.size() == 0)
			return null;
		return ans;
	}

	public void linearSearchViewUpdate(Entitet e, ArrayList<String> searchRec, boolean flagResetFilePointer,
			boolean flagStopAfterFirst, boolean flagWriteToFile)
	{
		ArrayList<String> result = linearSearch(searchRec, flagResetFilePointer, flagStopAfterFirst);
		if (result == null)
		{
			MessageController.errorMessage("Bezuspesna pretraga.");
			return;
		}
		data = new String[result.size()][];
		for (int i = 0; i < result.size(); i++)
		{
			// System.out.println("RES get " + result.get(i));
			String split[] = result.get(i).split("___");
			// System.out.println("split size " + split.length);
			for (int j = 0; j < split.length; j++)
				System.out.println("split  " + split[j]);

			data[i] = new String[split.length];
			for (int j = 0; j < split.length; j++)
				data[i][j] = split[j];
		}
		// System.out.println(data.length + "Duzina");
		/*
		 * for(int i=0;i<data.length;i++) { for(int j=0;j<data[i].length;j++)
		 * System.out.print(data[i][j] + ", "); System.out.println(); }
		 */
		if (flagWriteToFile)
		{
			try
			{
				this.makeNewSEKFile();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		Wrapper wr = new Wrapper(data);
		notify(wr);
	}

	public boolean makeNewSEKFile() throws IOException
	{
		boolean result = true;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Selektujte folder u kom ce nova datoteka biti sacuvana");
		fileChooser.showSaveDialog(null);

		System.out.println(fileChooser.getCurrentDirectory());
		System.out.println(fileChooser.getSelectedFile().getAbsolutePath());

		// posle sortiranja u baferu data[][] imamo sortirane slogove
		// trebamo jos da napravimo novu sekvencijalnu datoteke sa novim headerom
		/*
		 * String headerSekName=headerName.replaceAll(".ser",".sek"); File serHeader=new
		 * File(path+File.separator+headerSekName); if (!serHeader.exists()){
		 * serHeader.createNewFile(); }
		 */

		// otvaramo header file sekvincijalne datoteke koju smo pretrazivali
		RandomAccessFile afile = new RandomAccessFile(path + File.separator + headerName, "r");
		byte[] temp_buffer = new byte[(int) afile.length()];
		// promenicemo putanju do fajla sa podacima
		String tpath = afile.readLine();
		tpath = tpath.replace(".stxt", "");
		long seconds = System.currentTimeMillis() / 1000l;
		tpath += seconds + ".stxt";
		afile.read(temp_buffer);
		afile.close();
		System.out.println(tpath);

		String newHeaderName = headerName;
		System.out.println(newHeaderName);
		newHeaderName = newHeaderName.replace(".sek", "");
		newHeaderName += seconds + ".sek";
		System.out.println(newHeaderName);

		// u header sekvencijalne datoteke upisujemo novu putanju i isti opis
		// afile=new RandomAccessFile(serHeader.getAbsoluteFile(),"rw");
		afile = new RandomAccessFile(fileChooser.getSelectedFile().getAbsolutePath() + File.separator + newHeaderName,
				"rw");
		afile.seek(0);
		afile.writeBytes(tpath + "\r\n");
		for (int i = 0; i < temp_buffer.length; i++)
		{
			if (temp_buffer[i] != 0)
				afile.write(temp_buffer[i]);
		}
		// afile.write(temp_buffer);
		afile.setLength(afile.length());
		afile.close();

		System.out.println(fileName);
		String newFileName = fileName;
		newFileName = newFileName.replace(".stxt", seconds + ".stxt");
		System.out.println(newFileName);

		// jos ostaje da se sortirani slogovi upisu u novu datoteku
		// String fileSekName=fileName.replaceAll(".txt",".stxt");
		File newFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + File.separator + newFileName);
		newFile.createNewFile();
		afile = new RandomAccessFile(newFile.getAbsoluteFile(), "rw");
		afile.seek(0);
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < fields.size(); j++)
			{
				afile.writeBytes(data[i][j].toString());
			}
			afile.writeBytes("\r\n");
		}

		afile.setLength(afile.length());
		afile.close();

		if (Desktop.isDesktopSupported())
		{
			File myFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + File.separator + newFileName);
			Desktop.getDesktop().open(myFile);
		}
		return result;
	}

	public int MakeFilesForSort()
	{
		int cnt = 1;
		long saveFilePointer = FILE_POINTER;
		long saveBlockFactor = BLOCK_FACTOR;
		FILE_POINTER = 0;
		this.setBLOCK_SIZE(750);
		while (FILE_POINTER < FILE_SIZE)
		{
			try
			{
				fetchNextBlock(false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				FILE_POINTER = saveFilePointer;
				this.setBLOCK_SIZE(saveBlockFactor);
				return cnt - 1;
			}
			try
			{
				RandomAccessFile afile = new RandomAccessFile(path + File.separator + headerName, "r");
				byte[] temp_buffer = new byte[(int) afile.length()];
				String tpath = afile.readLine();
				tpath = "path" + "/" + cnt + ".stxt";
				afile.read(temp_buffer);
				afile.close();

				String newHeaderName = cnt + ".sek";
				afile = new RandomAccessFile("sort" + File.separator + newHeaderName, "rw");
				afile.seek(0);
				afile.writeBytes(tpath + "\r\n");
				for (int i = 0; i < temp_buffer.length; i++)
				{
					if (temp_buffer[i] != 0)
						afile.write(temp_buffer[i]);
				}
				// afile.write(temp_buffer);
				afile.setLength(afile.length());
				afile.close();

				afile = new RandomAccessFile("sort" + File.separator + cnt + ".stxt", "rw");
				cnt++;
				afile.seek(0);
				for (int i = 0; i < data.length; i++)
				{
					for (int j = 0; j < fields.size(); j++)
					{
						afile.writeBytes(data[i][j].toString());
					}
					if (i != data.length - 1)
						afile.writeBytes("\r\n");
				}
				afile.setLength(afile.length());
				afile.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FILE_POINTER = saveFilePointer;
		this.setBLOCK_SIZE(saveBlockFactor);
		return cnt - 1;
	}

	public void sortFiles(UISEKFile sekFile, int arr[], boolean saveOrTmp, boolean[] brojFlags)
	{
		sekFile.setBLOCK_SIZE(750);
		sekFile.FILE_POINTER = 0;
		try
		{
			sekFile.fetchNextBlock(false);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageController.errorMessage("Greska pri sortiranju");
			return;
		}
		for (int curr = 0; curr < arr.length; curr++)
		{
			if (arr[curr] == 0)
				continue;
			for (int i = 0; i < sekFile.data.length; i++)
			{
				for (int j = i + 1; j < sekFile.data.length; j++)
				{
					boolean check1 = (arr[curr] == 1 && sekFile.data[i][curr].compareTo(sekFile.data[j][curr]) > 0)
							|| (arr[curr] == -1 && sekFile.data[i][curr].compareTo(sekFile.data[j][curr]) < 0);
					boolean check2 = false;
					if (brojFlags[curr] == true)
						check2 = (arr[curr] == 1 && Integer.parseInt(sekFile.data[i][curr].trim()) > Integer
								.parseInt(sekFile.data[j][curr].trim()))
								|| (arr[curr] == -1 && Integer.parseInt(sekFile.data[i][curr].trim()) < Integer
										.parseInt(sekFile.data[j][curr].trim()));

					if ((brojFlags[curr] == false && check1) || (brojFlags[curr] == true && check2))
					{
						boolean flag = true;
						for (int k = 0; k < curr; k++)
						{
							if (arr[k] != 0 && sekFile.data[i][k].compareTo(sekFile.data[j][k]) != 0)
							{
								flag = false;
								break;
							}
						}
						if (flag)
						{
							String tmpArr[] = new String[sekFile.data[i].length];
							for (int k = 0; k < sekFile.data[i].length; k++)
							{
								tmpArr[k] = sekFile.data[i][k];
								sekFile.data[i][k] = sekFile.data[j][k];
							}
							for (int k = 0; k < sekFile.data[j].length; k++)
								sekFile.data[j][k] = tmpArr[k];
						}

					}
				}
			}
		}
		RandomAccessFile afile;
		try
		{
			String noIdeaHowToNameThis = sekFile.getHeaderName();
			noIdeaHowToNameThis = noIdeaHowToNameThis.replaceAll(".sek", ".stxt");
			System.out.println(noIdeaHowToNameThis);
			if (saveOrTmp)
				afile = new RandomAccessFile("sort" + File.separator + noIdeaHowToNameThis, "rw");
			else
				afile = new RandomAccessFile("Tmp" + File.separator + noIdeaHowToNameThis, "rw");
			afile.seek(0);
			for (int i = 0; i < sekFile.data.length; i++)
			{
				for (int j = 0; j < sekFile.fields.size(); j++)
				{
					afile.writeBytes(sekFile.data[i][j].toString());
				}
				if (i != sekFile.data.length - 1)
					afile.writeBytes("\r\n");
			}
			afile.setLength(afile.length());
			afile.close();
		}
		catch (FileNotFoundException e)
		{
			MessageController.errorMessage("Greska pri sortiranju");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			MessageController.errorMessage("Greska pri sortiranju");
			e.printStackTrace();
		}
	}

	public void merge2Files(UISEKFile sekFile1, UISEKFile sekFile2, int arr[], boolean[] brojFlags, int cnt)
			throws IOException
	{
		boolean firstTime = true;
		RandomAccessFile afile;

		afile = new RandomAccessFile(path + File.separator + headerName, "r");
		byte[] temp_buffer = new byte[(int) afile.length()];
		String tpath = afile.readLine();
		tpath = "path" + "/" + cnt + ".stxt";
		afile.read(temp_buffer);
		afile.close();

		String newHeaderName = cnt + ".sek";
		afile = new RandomAccessFile("sort" + File.separator + newHeaderName, "rw");
		afile.seek(0);
		afile.writeBytes(tpath + "\r\n");
		for (int i = 0; i < temp_buffer.length; i++)
		{
			if (temp_buffer[i] != 0)
				afile.write(temp_buffer[i]);
		}
		afile.close();
		// afile.write(temp_buffer);
		/*
		 * afile.setLength(afile.length()); afile.close();
		 */

		afile = new RandomAccessFile("sort" + File.separator + cnt + ".stxt", "rw");
		afile.seek(0);

		/*
		 * for (int i = 0; i < data.length; i++) { for (int j = 0; j < fields.size();
		 * j++) { afile.writeBytes(data[i][j].toString()); } if (i != data.length - 1)
		 * afile.writeBytes("\r\n"); } afile.setLength(afile.length()); afile.close();
		 */

		sekFile1.FILE_POINTER = 0;
		sekFile1.setBLOCK_SIZE(1);
		sekFile2.FILE_POINTER = 0;
		sekFile2.setBLOCK_SIZE(1);
		try
		{
			sekFile1.fetchNextBlock(false);
			sekFile2.fetchNextBlock(false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		while (true)
		{
			boolean flagFirst = true;
			for (int curr = 0; curr < arr.length; curr++)
			{
				if (arr[curr] == 0)
					continue;
				for (int i = 0; i < sekFile1.data.length; i++)
				{
					boolean check1 = (arr[curr] == 1 && sekFile1.data[0][curr].compareTo(sekFile2.data[0][curr]) > 0)
							|| (arr[curr] == -1 && sekFile1.data[0][curr].compareTo(sekFile2.data[0][curr]) < 0);

					boolean check2 = false;
					if (brojFlags[curr] == true)
						check2 = (arr[curr] == 1 && Integer.parseInt(sekFile1.data[0][curr].trim()) > Integer
								.parseInt(sekFile2.data[0][curr].trim()))
								|| (arr[curr] == -1 && Integer.parseInt(sekFile1.data[0][curr].trim()) < Integer
										.parseInt(sekFile2.data[0][curr].trim()));

					if ((brojFlags[curr] == false && check1) || (brojFlags[curr] == true && check2))
					{
						boolean flag = true;
						for (int k = 0; k < curr; k++)
						{
							if (arr[k] != 0 && sekFile1.data[0][k].compareTo(sekFile2.data[0][k]) != 0)
							{
								flag = false;
								break;
							}
						}
						if (flag)
						{
							flagFirst = false;
							break;
						}
					}
				}
			}
			if (flagFirst)
			{
				if (!firstTime)
					afile.writeBytes("\r\n");
				firstTime = false;
				for (int j = 0; j < sekFile1.fields.size(); j++)
					afile.writeBytes(sekFile1.data[0][j].toString());
				if (sekFile1.FILE_POINTER >= sekFile1.FILE_SIZE)
				{
					afile.writeBytes("\r\n");
					for (int j = 0; j < sekFile2.fields.size(); j++)
						afile.writeBytes(sekFile2.data[0][j].toString());
					break;
				}
				else
					sekFile1.fetchNextBlock(false);
			}
			else
			{
				if (!firstTime)
					afile.writeBytes("\r\n");
				firstTime = false;
				for (int j = 0; j < sekFile2.fields.size(); j++)
					afile.writeBytes(sekFile2.data[0][j].toString());
				if (sekFile2.FILE_POINTER >= sekFile2.FILE_SIZE)
				{
					afile.writeBytes("\r\n");
					for (int j = 0; j < sekFile1.fields.size(); j++)
						afile.writeBytes(sekFile1.data[0][j].toString());
					break;
				}
				else
					sekFile2.fetchNextBlock(false);
			}
		}
		while (sekFile1.FILE_POINTER < sekFile1.FILE_SIZE)
		{
			sekFile1.fetchNextBlock(false);
			if (!firstTime)
				afile.writeBytes("\r\n");
			firstTime = false;
			for (int j = 0; j < sekFile1.fields.size(); j++)
				afile.writeBytes(sekFile1.data[0][j].toString());

		}
		while (sekFile2.FILE_POINTER < sekFile2.FILE_SIZE)
		{
			sekFile2.fetchNextBlock(false);
			if (!firstTime)
				afile.writeBytes("\r\n");
			firstTime = false;
			for (int j = 0; j < sekFile2.fields.size(); j++)
				afile.writeBytes(sekFile2.data[0][j].toString());
		}
		afile.setLength(afile.length());
		afile.close();
	}

	/**
	 * Pretraga u sekvencijalnoj datoteci koja pocinje od pocetka datoteke Moze da
	 * se pretrazuje po bilo kom polju datoteke metoda zaustavlja pretragu na prvom
	 * slogu koji zadovoljava zadate kriterijume ili po neuspesnom trazenju sloga a
	 * to je na kraju datoteke Ukoliko se pretraga vrsi po kljucu pretraga se moze
	 * zaustaviti pri prvoj vecoj vrednosti od kljuca
	 */
	public boolean findRecord(ArrayList<String> searchRec, int[] position)
	{
		FILE_POINTER = 0;
		boolean result = false;

		while (FILE_POINTER < FILE_SIZE && position[0] == -1)
		{
			try
			{
				fetchNextBlock(false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				position[0] = -1;
				return false;
			}

			for (int row = 0; row < data.length; row++)
			{

				if (isRowEqual(data[row], searchRec))
				{
					position[0] = row;
					return true;
				}
				else if (isRowGreater(data[row], searchRec))
				{
					position[0] = row;

					return false;
				}

			}
		}
		return result;
	}

	private int indOfKey(Entitet e)
	{
		ArrayList<Atribut> at = e.getAtributi();
		for (int i = 0; i < at.size(); i++)
		{
			if ((at.get(i)).isPrimarniKljuc())
				return i;
		}
		// ovo je nemoguce ali za svaki slucaj
		return -1;
	}

	private int numOfKeys(Entitet e)
	{
		ArrayList<Atribut> at = e.getAtributi();
		int br = 0;
		for (Atribut a : at)
		{
			if (a.isPrimarniKljuc())
				br++;
		}
		return br;
	}

	public String binarySearch(Entitet e, String key)
	{
		try
		{
			System.out.println("Trazim: " + key);
			RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "r");
			int ind = indOfKey(e);
			int brK = numOfKeys(e);
			long l = 0;
			long r = RECORD_NUM - 1;
			byte[] buff = new byte[RECORD_SIZE];
			int dbg = 0;
			while (l <= r)
			{
				System.out.print(dbg++ + " " + l + " " + r + " ");
				long mid = (l + r) / 2;
				afile.seek(mid * RECORD_SIZE);
				afile.read(buff);
				String line = new String(buff);
				int k = 0;
				int br = 0;
				String field = new String();
				while (br <= ind)
				{
					field = line.substring(k, k + fields.get(br).getFieldLength());
					k += fields.get(br).getFieldLength();
					br++;
				}
				StringBuffer sb = new StringBuffer(field);
				for (int i = 1; i < brK; i++)
				{
					field = line.substring(k, k + fields.get(br).getFieldLength());
					k += fields.get(br).getFieldLength();
					sb.append("___");
					sb.append(field);
					br++;
				}
				String primaryKey = sb.toString().trim();
				System.out.println(primaryKey);

				String split1[] = primaryKey.split("___");
				String split2[] = key.split("___");
				int cmpVal = 0;
				for (int i = 0; i < split1.length; i++)
				{
					split1[i] = split1[i].trim();
					split2[i] = split2[i].trim();
					if (split2[i].length() == 0)
						continue;
					System.out.println(this.getFields().get(i).getFieldType());
					boolean varcharIfFlag = true;
					try
					{
						Integer.parseInt(split1[i]);
					}
					catch (NumberFormatException e2)
					{
						varcharIfFlag = false;
					}
					// ovaj if je radjen pod pretpostavkom da se primarni kljucevi uvek nalaze na
					// pocetku fajlova, sto i jeste slucaj za sve fajlove iz velikog seta podataka
					if (this.getFields().get(i).getFieldType().equals("TYPE_NUMERIC")
							|| this.getFields().get(i).getFieldType().equals("TYPE_INTEGER")
							|| this.getFields().get(i).getFieldType().equals("TYPE_DECIMAL") || varcharIfFlag)
					{
						if (this.getFields().get(i).getFieldType().equals("TYPE_DECIMAL"))
						{
							if (Double.parseDouble(split1[i]) > Double.parseDouble(split2[i]))
							{
								cmpVal = 1;
								break;
							}
							if (Double.parseDouble(split1[i]) < Double.parseDouble(split2[i]))
							{
								cmpVal = -1;
								break;
							}
						}
						else
						{
							if (Integer.parseInt(split1[i]) > Integer.parseInt(split2[i]))
							{
								cmpVal = 1;
								break;
							}
							if (Integer.parseInt(split1[i]) < Integer.parseInt(split2[i]))
							{
								cmpVal = -1;
								break;
							}
						}
					}
					else
					{
						if (split1[i].compareTo(split2[i]) > 0)
						{
							cmpVal = 1;
							break;
						}
						if (split1[i].compareTo(split2[i]) < 0)
						{
							cmpVal = -1;
							break;
						}
					}

				}

				if (cmpVal == 0)
					return line;
				if (cmpVal > 0)
					r = mid - 1;
				else
					l = mid + 1;
			}
			System.out.println("LR " + l + " " + r);
			afile.close();

		}
		catch (FileNotFoundException l)
		{
			l.printStackTrace();
		}
		catch (IOException u)
		{
			// TODO Auto-generated catch block
			u.printStackTrace();
		}
		return null;
	}

	// metoda koja poziva binarnu pretragu i prikazuje rezultat u tabu
	public void binarySearchViewUpdate(Entitet e, String key)
	{
		String result = binarySearch(e, key);
		System.out.println("RES " + result);
		if (result == null)
		{
			MessageController.errorMessage("Bezuspesna pretraga.");
			return;
		}
		data = new String[1][];
		data[0] = new String[fields.size()];
		int k = 0;
		for (int j = 0; j < fields.size(); j++)
		{
			String field = null;
			field = result.substring(k, k + fields.get(j).getFieldLength());
			data[0][j] = field;
			k = k + fields.get(j).getFieldLength();
		}

		Wrapper wr = new Wrapper(data);
		notify(wr);
	}

	/**
	 * 
	 * @param aData
	 *            - jedan slog iz bloka datoteke
	 * @param searchRec
	 *            - yparametri pretrage
	 * @return - true ukoliko dati slog iz bloka sadrzi polja koja odgovaraju
	 *         parametrima pretrage
	 */
	private boolean isRowEqual(String[] aData, ArrayList<String> searchRec)
	{
		boolean result = true;

		for (int col = 0; col < fields.size(); col++)
		{
			if (!searchRec.get(col).trim().equals(""))
			{
				if (!aData[col].trim().equals(searchRec.get(col).trim()))
				{
					result = false;
					return result;
				}
			}

		}

		return result;
	}

	/**
	 * metoda koja proverava da li je tekući slog pretrage veći od zadatog parametra
	 * traženja
	 * 
	 * @param aData
	 *            -slog iz datoteke u baferu koji se poredi
	 * @param searchRec
	 *            - parametri pretrage
	 * @return
	 */
	private boolean isRowGreater(String[] aData, ArrayList<String> searchRec)
	{
		boolean result = true;
		int noPK = 0;
		boolean prev = true;

		for (int i = 0; i < fields.size(); i++)
		{
			if (!searchRec.get(i).trim().equals("") && !fields.get(i).isFieldPK())
			{
				return false;
			}
			if (fields.get(i).isFieldPK())
				noPK++;

		}

		for (int col = 0; col < fields.size(); col++)
		{

			if (!searchRec.get(col).trim().equals(""))
			{

				if (aData[col].trim().compareToIgnoreCase(searchRec.get(col).trim()) > 0 && col < noPK - 1)
				{
					return true;

				}
				else if (aData[col].trim().compareToIgnoreCase(searchRec.get(col).trim()) != 0 && col < noPK - 1)
				{
					result = false;
					prev = false;
				}
				else if (aData[col].trim().compareToIgnoreCase(searchRec.get(col).trim()) <= 0)
				{
					result = false;

				}
				else if (aData[col].trim().compareToIgnoreCase(searchRec.get(col).trim()) > 0 && prev
						&& col == (noPK - 1))
				{
					result = true;
				}
			}
		}

		return result;
	}

	/**
	* 
	*/
	public boolean deleteRecord(ArrayList<String> searchRec)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void makeTree() throws IOException
	{
		FILE_POINTER = 0;
		LinkedList<Node> listNodes = new LinkedList<Node>();

		Tree tree = null;
		long tFilePointer = 0;

		// citanje bloka po bloka i formiranje za svaki blok po jedan NodeElement
		// dva NodeElementa cine jedan Node
		while (FILE_POINTER < FILE_SIZE)
		{
			tFilePointer = FILE_POINTER;
			fetchNextBlock(false);
			List<KeyElement> listKeyElements = new ArrayList<KeyElement>();
			List<NodeElement> listNodeElements = new ArrayList<NodeElement>();
			for (int i = 0; i < fields.size(); i++)
			{
				if (fields.get(i).isFieldPK())
				{
					KeyElement keyElement = new KeyElement(fields.get(i).getFieldType(), data[0][i]);
					listKeyElements.add(keyElement);
				}

			}
			// posle ovoga moze se kreirati jedan NodeElement
			NodeElement nodeElement = new NodeElement((int) (tFilePointer / RECORD_SIZE), listKeyElements);

			listNodeElements.add(nodeElement);
			Node node = new Node(listNodeElements);
			tFilePointer = FILE_POINTER;
			fetchNextBlock(false);
			listKeyElements = new ArrayList<KeyElement>();

			for (int i = 0; i < fields.size(); i++)
			{
				if (fields.get(i).isFieldPK())
				{
					KeyElement keyElement = new KeyElement(fields.get(i).getFieldType(), data[0][i]);
					listKeyElements.add(keyElement);
				}

			}
			// posle ovoga moze se kreirati jod jedan NodeElement
			nodeElement = new NodeElement((int) (tFilePointer / RECORD_SIZE), listKeyElements);

			listNodeElements.add(nodeElement);
			// dva NodeElement-a cine jedan Node
			node = new Node(listNodeElements);
			listNodes.add(node);
		}

		// Uputstvo sa vezbi: ???
		// posle ovoga u listNodes imamo za svaka 2 bloka po jedan Node

		// prolazak kroz Nodov-e prvog nivoa i formiranje ostalih nivoa:
		// od dva susedne Node-a iz liste listNodes uzimaju se podaci prvog
		// NodeElement-a
		// i pravi novi Node na visem nivou. Njegovi NodeElementi imaju kao adresu
		// ne adresu bloka u datoteci već poziciju u listi childova neposrednog
		// NodeElement-a

		// ....
		// ....
		// ....
		// .....
		Node root = new Node();
		int lastNum = listNodes.size();

		while (listNodes.size() > 1)
		{

			Node novi = new Node();
			Node curr1 = listNodes.getFirst();
			listNodes.removeFirst();
			lastNum--;
			Node curr2 = null;

			if (lastNum > 0)
			{
				curr2 = listNodes.getFirst();
				listNodes.removeFirst();
				lastNum--;
			}

			novi.getChildren().add(curr1);
			if (curr2 != null)
				novi.getChildren().add(curr2);

			NodeElement node1 = new NodeElement(curr1.getData().get(0).getBlockAddress(), new ArrayList<KeyElement>());

			for (KeyElement k : curr1.getData().get(0).getKeyValue())
				node1.getKeyValue().add(k);

			NodeElement node2 = null;
			if (curr2 != null)
			{
				node2 = new NodeElement(curr2.getData().get(0).getBlockAddress(), new ArrayList<KeyElement>());

				for (KeyElement k : curr2.getData().get(0).getKeyValue())
					node2.getKeyValue().add(k);
			}

			novi.getData().add(node1);
			if (node2 != null)
				novi.getData().add(node2);

			listNodes.add(novi);
			if (lastNum == 0)
			{
				lastNum = listNodes.size();
			}
		}

		root = listNodes.getFirst();
		tree = new Tree();
		tree.setRootElement(root);
		tree.printBFS();

		FILE_POINTER = 0;
		// imamo stablo potrebno je serijalizovati ga
		ObjectOutputStream os = null;
		String treeFileName = headerName.replaceAll(".sek", ".tree");
		try
		{
			os = new ObjectOutputStream(new FileOutputStream(path + File.separator + treeFileName));
			os.writeObject(tree);
			os.close();
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			// ispravno zatvaranje resursa
			if (os != null)
				os.close();
		}

	}

	public void pisanjeUNoviFajl(String line) throws IOException
	{
		System.out.println("OVO JE PATH : " + path);
		RandomAccessFile randomAccessFile = new RandomAccessFile(path + File.separator + "cao.sek", "r");
		FileWriter fileWriter = new FileWriter(path + File.separator + "cao.sek", true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		PrintWriter printWriter = new PrintWriter(bufferedWriter);

		if (prviPut)
			printWriter.println();
		printWriter.print(line);
		printWriter.flush();
		printWriter.close();
		randomAccessFile.close();
		prviPut = true;
	}

}
