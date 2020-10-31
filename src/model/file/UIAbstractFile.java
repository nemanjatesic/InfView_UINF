package model.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import model.Entitet;
import model.file.UIAbstractFile;
import model.file.UIFileField;

public class UIAbstractFile extends Entitet implements IUIFile
{
	static public final int BROWSE_MODE = 1;
	static public final int ADD_MODE = 2;
	static public final int UPDATE_MODE = 3;
	static public final int DELETE_MODE = 4;
	static public final int FIND_MODE = 5;

	// velicina bloka izražena u broju slogova koji se uzimaju u jednom zahvatu iz
	// datoteke, tj. faktor blokiranja
	protected long BLOCK_FACTOR = 20;

	// velicina jednog sloga u datoteci u bajtovima, u datotekama sa kojima ćemo mi
	// raditi slogovi su fiksne velicine
	// to zna�?i da svaki slog u jednoj datoteci ima istu dužinu
	protected int RECORD_SIZE = 0;

	// velicina bafera u bajtima, odnosno broj bajtova koji se uzimaju u jednom
	// bloku: BLOCK_FACTOR x RECORD_SIZE
	protected int BUFFER_SIZE = 0;

	// ukupan broj blokova u datoteci
	protected int BLOCK_NUM = 0;

	// ukupan broj slogova u datoteci
	protected long RECORD_NUM = 0;

	// pointer u datoteci
	protected long FILE_POINTER = 0;

	// velicina datoteke u bajtima
	protected long FILE_SIZE = 0;

	// rezim rada datoteke, inicijalno je datoteka u režimu pregleda
	protected int MODE = UIAbstractFile.BROWSE_MODE;

	// putanja do direktorijuma u kome se nalazi datoteka
	protected String path;
	// naziv fajla u kome se nalazi header datoteke
	protected String headerName;
	// naziv fajla u kome se nalazi podaci datoteke
	protected String fileName;

	protected boolean directory;

	// opis polja koji cine slog dobija se iz header datoteke
	protected ArrayList<UIFileField> fields = new ArrayList<UIFileField>();

	// sadrzaj bloka koji se uzima u jednom zahvatu iz datoteke, ovo je u stvari
	// bafer
	protected byte[] buffer;

	// sadrzaj jednog bloka predstavljen kao matrica
	protected String[][] data = null;

	// lista sluša�?a koja se koristi da se osveži prikaz tabele u klasi FileView
	// prilikom u�?itavanja novog bloka iz datoteke

	public UIAbstractFile(String path, String headerName, boolean directory)
	{
		this.path = path;
		this.headerName = headerName;
		this.directory = directory;
		this.fileName = headerName;
	}
	
	public UIAbstractFile(String path, String headerName, boolean directory, String name)
	{
		super(name);
		this.path = path;
		this.headerName = headerName;
		this.directory = directory;
		this.fileName = headerName;
	}

	public UIAbstractFile(String name)
	{
		super(name);
	}
	
	/**
	 * Metoda za formiranje zaglavlja je identi�?na kod serijske i sekvencijalne
	 * datoteke, zbog toga se nalazi u ovoj apstraktnoj klasi
	 */
	public void readHeader() throws IOException, SQLException 
	{
		String delimiter = "\\/";
		ArrayList<String> headRec = new ArrayList<String>();
		RandomAccessFile headerFile = null;
		Object data[] = null;

		headerFile = new RandomAccessFile(path + File.separator + headerName, "r");
		while (headerFile.getFilePointer() < headerFile.length())
			headRec.add(headerFile.readLine());

		headerFile.close();

		int row = 0;

		for (String record : headRec)
		{
			StringTokenizer tokens = new StringTokenizer(record, delimiter);

			int cols = tokens.countTokens();
			data = new String[cols];
			int col = 0;
			while (tokens.hasMoreTokens())
			{
				data[col] = tokens.nextToken();
				if (data[col].equals("field"))
				{
					String fieldName = tokens.nextToken();
					String fieldType = tokens.nextToken();
					int fieldLenght = Integer.parseInt(tokens.nextToken());
					// veli�?ina sloga jednaka je zbiru veli�?ine pojedina�?nih polja
					RECORD_SIZE = RECORD_SIZE + fieldLenght;
					boolean fieldPK = new Boolean(tokens.nextToken());
					UIFileField field = new UIFileField(fieldName, fieldType, fieldLenght, fieldPK);

					fields.add(field);
				}
				else if (data[col].equals("path"))
				{
					fileName = tokens.nextToken();

				}

			}

			row++;

		}
		// jos 2 bajta za oznaku novog reda u liniji datoteke
		RECORD_SIZE = RECORD_SIZE + 2;

		// postavljanje atributa datoteke
		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "r");
		FILE_SIZE = afile.length();
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;
		afile.close();
		
	}

	public void setBLOCK_SIZE(long block_size)
	{
		BLOCK_FACTOR = block_size;
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;
	}

	// get metode za klasu
	public static int getADD_MODE()
	{
		return ADD_MODE;
	}

	public static int getBROWSE_MODE()
	{
		return BROWSE_MODE;
	}

	public static int getDELETE_MODE()
	{
		return DELETE_MODE;
	}

	public static int getFIND_MODE()
	{
		return FIND_MODE;
	}

	public static int getUPDATE_MODE()
	{
		return UPDATE_MODE;
	}

	public int getBLOCK_NUM()
	{
		return BLOCK_NUM;
	}

	public long getBLOCK_FACTOR()
	{
		return BLOCK_FACTOR;
	}

	public byte[] getBlockContent()
	{
		return buffer;
	}

	public String[][] getData()
	{
		return data;
	}

	public boolean isDirectory()
	{
		return directory;
	}

	public ArrayList<UIFileField> getFields()
	{
		return fields;
	}

	public long getFILE_POINTER()
	{
		return FILE_POINTER;
	}

	public long getFILE_SIZE()
	{
		return FILE_SIZE;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getHeaderName()
	{
		return headerName;
	}

	public int getMODE()
	{
		return MODE;
	}

	public String getPath()
	{
		return path;
	}

	public long getRECORD_NUM()
	{
		return RECORD_NUM;
	}

	public int getRECORD_SIZE()
	{
		return RECORD_SIZE;
	}

	public void setMODE(int mode)
	{
		MODE = mode;
	}

	public String toString()
	{
		return super.toString();
	}

	public void setFILE_POINTER(long fILEPOINTER) {
		FILE_POINTER = fILEPOINTER;
	}

	public void setRECORD_SIZE(int rECORDSIZE) {
		RECORD_SIZE = rECORDSIZE;
	}
	
	@Override
	public boolean fetchNextBlock(boolean bool) throws IOException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRecord(ArrayList<String> record) throws IOException, SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRecord(ArrayList<String> record) throws IOException, SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int findRecord(ArrayList<String> searchRec)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteRecord(ArrayList<String> searchRec)throws IOException, SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}
}
