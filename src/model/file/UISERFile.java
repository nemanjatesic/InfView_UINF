package model.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import controller.MessageController;
import wrapper.Wrapper;
import model.Entitet;

public class UISERFile extends UIAbstractFile {

	ArrayList<String> deleted;
	long LAST_POINTER = 0;

	public UISERFile(String path, String headerName, boolean directory) {
		super(path, headerName, directory);
	}
	
	public UISERFile(String path, String headerName, boolean directory, String name) {
		super(path, headerName, directory,name);
	}

	
	/**
	 * Prenos bloka iz datoteke u radnu memoriju aplikacije Velicina bloka određena
	 * je atributom BLOCK_SIZE Po zavšetku metode blok podataka iz datoteke nalazi
	 * se u radnoj memoriji aplikaciji u matrici datа[][]
	 * @throws FileNotFoundException 
	 * 
	 * @throws IOException
	 * 
	 */
	
	public boolean fetchNextBlock1() throws IOException
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

		return true;

	}
	
	public boolean fetchNextBlockCopy() throws IOException
	{

		RandomAccessFile afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "r");
		FILE_SIZE = afile.length();
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;

		// BUFFER_SIZE je uvek jednak broju slogova u jednom bloku * velicina jednog
		// sloga
		// izuzev ako poslednji blok nije pun blok
		if (FILE_POINTER / (RECORD_SIZE+1) + BLOCK_FACTOR > RECORD_NUM)
			BUFFER_SIZE = (int) (RECORD_NUM - FILE_POINTER / (RECORD_SIZE+1)) * (RECORD_SIZE+1);
		else
			BUFFER_SIZE = (int) ((RECORD_SIZE+1) * BLOCK_FACTOR);

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

		int idx=0;
		for (int i = 0; i < BUFFER_SIZE / (RECORD_SIZE+1); i++)
		{

			String line = contentS.substring(i * RECORD_SIZE + i, i * RECORD_SIZE + i + RECORD_SIZE + 1);
			//System.out.println(line.charAt(line.length()-3));
			byte byt=(byte)line.charAt(line.length()-3);
			if(byt==1)
			{
				data[idx] = new String[fields.size()];
				int k = 0;
				for (int j = 0; j < fields.size(); j++)
				{
					String field = null;
					field = line.substring(k, k + fields.get(j).getFieldLength());
					data[idx][j] = field;
					k = k + fields.get(j).getFieldLength();
				}
				idx++;
			}
		}
		LAST_POINTER = FILE_POINTER;
		FILE_POINTER = afile.getFilePointer();
		//System.out.println(FILE_POINTER + "FP");
		afile.close();
		//System.out.println("OUT");
		Wrapper wr = new Wrapper(data);
		notify(wr);
		return true;
	}
	
	public void makeFileCopy() throws IOException
	{
		RandomAccessFile afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "rw");
		long save=BLOCK_FACTOR;
		setBLOCK_SIZE(FILE_SIZE);
		afile.seek(0);
		try
		{
			fetchNextBlock1();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			FILE_POINTER = 0;
			setBLOCK_SIZE(save);
			afile.close();
			return;
		}
		try
		{
			for (int i = 0; i < data.length; i++)
			{
				for (int j = 0; j < fields.size(); j++)
				{
					afile.writeBytes(data[i][j].toString());
				}
				byte by=1;
				afile.writeByte(by);
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
		setBLOCK_SIZE(save);
		FILE_POINTER=0;
	}
	
	//ovo nam ne treba vise
	/*public boolean fetchNextBlock() throws IOException {

		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "r");
		// veli�?ina datoteke u bajtima
		FILE_SIZE = afile.length();

		// ukupan broj slogova u datoteci
		// sva zaokruzivanja su trenutno nepotrebna, moze prosto i:
		// RECORD_NUM=FILE_SIZE/RECORD_SIZE;
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));

		// ukupan broj blokova u datoteci
		// (+1) se dodaje u slucaju da poslednji blok nije potpuno popunjen sa slogovima
		// ovo se desava u 99%, za 1% BLOCK_NUM nije tacan
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;
		afile.close();
		afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "r");
		// BUFFER_SIZE je uvek jednak broju slogova u jednom bloku * velicina jednog
		// sloga
		// izuzev ako poslednji blok nije pun blok
		if (FILE_POINTER / RECORD_SIZE + BLOCK_FACTOR > RECORD_NUM)
			// na redu je citanje poslednjeg bloka
			BUFFER_SIZE = (int) (RECORD_NUM - FILE_POINTER / RECORD_SIZE) * RECORD_SIZE; // izmeniti poslednji blok da
																							// radi samo sa neizbrisanim
		else
			BUFFER_SIZE = (int) (RECORD_SIZE * BLOCK_FACTOR);

		
		//za poslednji blok:
		if (BUFFER_SIZE <= 0) {
			FILE_POINTER = 0;
			return false;
		}
		// ovo se desava zbog dodatih bajtova za brisanje:
		BUFFER_SIZE += BLOCK_FACTOR;
		buffer = new byte[BUFFER_SIZE];
		data = new String[(int) BUFFER_SIZE / RECORD_SIZE][];
		// po otvaranju pozicioniram se na prethodni kraj zahvata
		afile.seek(FILE_POINTER);
		afile.read(buffer);
		// pretvaranje procitanog niza bajtova u String
		String contentS = new String(buffer);

		// poravnanje zbog eventualne greske u encodingu, trenutno nepotrebno
		if (contentS.length() < buffer.length) {
			for (int x = contentS.length(); x < buffer.length; x++)
				contentS = contentS + " ";
		}

		// prolazi se slog po slog iz datoteke
		int curr = 0;
		int cnt = BUFFER_SIZE / RECORD_SIZE;
		if (BUFFER_SIZE % RECORD_SIZE == 0 || RECORD_SIZE <= BLOCK_FACTOR)
			cnt--;
		for (int i = 0; i < cnt; i++) {
			// svaki slog predstavlja jednu liniju teksta
			String line = contentS.substring(i * RECORD_SIZE + i, i * RECORD_SIZE + RECORD_SIZE + i);
			byte[] bytes = contentS.getBytes();
			byte b = bytes[(i + 1) * RECORD_SIZE + i];
			if (b == 0) {
				data[curr] = new String[fields.size()];
				int k = 0;
				// prolazi se polje po polje iz sloga
				for (int j = 0; j < fields.size(); j++) {
					String field = null;
					field = line.substring(k, k + fields.get(j).getFieldLength());
					data[curr][j] = field;
					k = k + fields.get(j).getFieldLength();
				}
				curr++;
			}
		}

		// pozicioniramo file pointer tamo gde smo stali sa citanjem
		LAST_POINTER = FILE_POINTER;
		FILE_POINTER = afile.getFilePointer();
		if (FILE_POINTER == afile.length())
			FILE_POINTER = 0;
		afile.close();

		return true;

	}*/

	public boolean addRecord(ArrayList<String> record) throws IOException 
	{

		for(int i=0;i<record.size();i++)
		{
			boolean flagOk=true;
			String tst=record.get(i).trim();
			if(tst.length()==0 && this.fields.get(i).isFieldPK())
			{
				MessageController.errorMessage("Polje " + this.fields.get(i).getFieldName() + " ne moze biti prazno, pokusajte ponovo");
				return false;
			}
			if(tst.length()>0 && (this.fields.get(i).getFieldType().equals(UIFileField.TYPE_NUMERIC) || this.fields.get(i).getFieldType().equals(UIFileField.TYPE_INTEGER)))
			{
				try
				{
					Integer.parseInt(record.get(i));
				}
				catch (NumberFormatException e)
				{
					flagOk=false;
				}
			}
			if(tst.length()>0 && this.fields.get(i).getFieldType().equals(UIFileField.TYPE_DECIMAL))
			{
				try
				{
					Double.parseDouble(record.get(i));
				}
				catch (NumberFormatException e)
				{
					flagOk=false;
				}
			}
			if(tst.length()>0 && this.fields.get(i).getFieldType().equals(UIFileField.TYPE_DATETIME))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try
				{
					sdf.parse(record.get(i)); 
				}
				catch(ParseException e)
				{
				      flagOk = false;
				}
			}
			if(record.get(i).length()>this.fields.get(i).getFieldLength() || !flagOk)
			{
				MessageController.errorMessage("Uneli ste neispravne podatke u polje " + this.fields.get(i).getFieldName() + ", pokusajte ponovo");
				return false;
			}
		}
		
		//dodavanje u originalni fajl:
		String newRecord = "\r\n";
		for (int i = 0; i < record.size(); i++) 
		{
			int spaces=this.fields.get(i).getFieldLength()-record.get(i).length();
			newRecord = newRecord + record.get(i);
			while(spaces>0)
			{
				spaces--;
				newRecord=newRecord+" ";
			}
		}

		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "rw");
		afile.seek(afile.length());
		afile.writeBytes(newRecord);
		afile.setLength(afile.length());
		MessageController.infoMessage("Uspesno ste dodali red na kraj originalnog fajla");
		FILE_SIZE = afile.length();
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;
		
		String ff="copy"+fileName;
		System.out.println(path + File.separator + ff + "debug putanje");
		//dodavanje u kopiju sa kojom radimo:
		RandomAccessFile file = new RandomAccessFile(path + File.separator + ff, "rw");
		byte[] b=new byte[1];
		b[0]=1;
		file.seek(file.length());
		file.writeBytes(newRecord);
		file.write(b);
		file.setLength(file.length());
		MessageController.infoMessage("Uspesno ste dodali red na kraj kopije fajla.");
		file.close();
		afile.close();
		return true;
	}

	// treba istestirati kad bude moglo, verovatno nece raditi odmah @Dejana
	// ipak ne treba ovo uraditi uopste
	public boolean updateRecord(ArrayList<String> record) throws IOException {

		return true;
	}

	/**
	 * Pretraga u serijskoj datoteci koja pocinje od pocetka datoteke Moze da se
	 * pretrazuje po bilo kom polju datoteke metoda zaustavlja pretragu na prvom
	 * slogu koji zadovoljava zadate kriterijume ili po neuspesnom trazenju sloga a
	 * to je na kraju datoteke
	 */
	public int findRecord(Entitet en, ArrayList<String> searchRec)
	{
		FILE_POINTER = 0;
		int position = -1;

		while (FILE_POINTER < FILE_SIZE && position == -1)
		{
			try
			{
				fetchNextBlockCopy();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return position;
			}

			for (int row = 0; row < data.length; row++)
			{

				if (isRowEqual(data[row], searchRec))
				{
					position = row;
					return position;
				}

			}
		}
		return position;
	}

	/**
	 * 
	 * @param aData     - jedan slog iz bloka datoteke
	 * @param searchRec - parametri pretrage
	 * @return - true ukoliko dati slog iz bloka sadrzi polja koja odgovaraju
	 *         parametrima pretrage
	 */
	private boolean isRowEqual(String[] aData, ArrayList<String> searchRec)
	{
		boolean result = true;

		for (int col = 0; col < fields.size(); col++)
		{
			if (aData == null)
				return false;
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

	public boolean deleteRecord(Entitet en, String line)
	{
		try
		{
			RandomAccessFile afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "rw");
			long fileSizeCopy = afile.length();
			long recordNumCopy = (long) Math.ceil((fileSizeCopy * 1.0000) / ((RECORD_SIZE + 1) * 1.0000));

			afile.seek(0);
			byte[] buff = new byte[RECORD_SIZE + 1];
			System.out.println(recordNumCopy);
			for (int i = 0; i < recordNumCopy; i++)
			{
				afile.read(buff);
				// afile.read(b);
				String lineCurr = new String(buff);
				// System.out.println(buff[buff.length-3]);
				// System.out.println(lineCurr);
				if (buff[buff.length - 3] == 1 && lineCurr.trim().equals(line.trim()))
				{
					afile.seek(afile.getFilePointer() - 3);
					byte be = 0;
					afile.write(be);
					break;
				}
			}
			afile.close();

		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("err delete");
		}

		try
		{
			FILE_POINTER = LAST_POINTER;
			fetchNextBlockCopy();
		}
		catch (IOException e)
		{
			System.out.println("blok delete serijska");
		}

		return true;

	}

	public boolean undeleteRecord(Entitet en, String line)
	{
		try
		{
			RandomAccessFile afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "rw");
			long fileSizeCopy = afile.length();
			long recordNumCopy = (long) Math.ceil((fileSizeCopy * 1.0000) / ((RECORD_SIZE + 1) * 1.0000));

			afile.seek(0);
			byte[] buff = new byte[RECORD_SIZE + 1];
			System.out.println(recordNumCopy);
			for (int i = 0; i < recordNumCopy; i++)
			{
				afile.read(buff);
				// afile.read(b);
				String lineCurr = new String(buff);
				// System.out.println(buff[buff.length-3]);
				// System.out.println(lineCurr);
				if (buff[buff.length - 3] == 0 && lineCurr.trim().equals(line.trim()))
				{
					afile.seek(afile.getFilePointer() - 3);
					byte be = 1;
					afile.write(be);
					break;
				}
			}
			afile.close();

		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("err undelete");
		}
		try
		{

			FILE_POINTER = LAST_POINTER;
			fetchNextBlockCopy();
		}
		catch (IOException e)
		{
			System.out.println("blok undelete serijska");
		}
		return true;
	}

	public ArrayList<String> showDeleted()
	{
		ArrayList<String> del = new ArrayList<>();

		try
		{
			RandomAccessFile afile = new RandomAccessFile(path + File.separator + "copy" + fileName, "r");
			afile.seek(0);
			byte[] buff = new byte[RECORD_SIZE + 1];
			for (int i = 0; i < RECORD_NUM / RECORD_SIZE; i++)
			{
				afile.read(buff);
				// afile.read(b);

				if (buff[buff.length - 3] == 0)
				{
					String line = new String(buff);
					line = line.substring(0, line.length() - 3);
					System.out.println(line);
					del.add(line);
				}
			}
			afile.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return del;
	}
}
