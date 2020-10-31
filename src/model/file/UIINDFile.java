package model.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import wrapper.Wrapper;
import model.tree.Node;
import model.tree.Tree;

public class UIINDFile extends UIAbstractFile {

	private Tree tree;

	private String treeFileName;
	private String overZoneFileName;

	public UIINDFile(String path, String headerName, boolean directory) {
		super(path, headerName, directory);
	}
	
	public UIINDFile(String path, String headerName, boolean directory, String name) {
		super(path, headerName, directory,name);
	}

	/*
	 * Zaglavlje kod indeks - sekvencijalne datoteke je drugacije u odnosu na
	 * serijsku i sekvencijalnu pa se metoda redefinise
	 */
	@Override
	public void readHeader() throws IOException,SQLException {
		String delimiter = "\\/";
		ArrayList<String> headRec = new ArrayList<String>();
		RandomAccessFile headerFile = null;
		Object data[] = null;

		headerFile = new RandomAccessFile(path + File.separator + headerName, "r");
		while (headerFile.getFilePointer() < headerFile.length())
			headRec.add(headerFile.readLine());

		headerFile.close();

		int row = 0;

		for (String record : headRec) {
			StringTokenizer tokens = new StringTokenizer(record, delimiter);

			int cols = tokens.countTokens();
			data = new String[cols];
			int col = 0;
			while (tokens.hasMoreTokens()) {
				data[col] = tokens.nextToken();
				if (data[col].equals("field")) {
					String fieldName = tokens.nextToken();
					String fieldType = tokens.nextToken();
					int fieldLenght = Integer.parseInt(tokens.nextToken());
					RECORD_SIZE = RECORD_SIZE + fieldLenght;
					boolean fieldPK = new Boolean(tokens.nextToken());
					UIFileField field = new UIFileField(fieldName, fieldType, fieldLenght, fieldPK);

					fields.add(field);
				} else if (data[col].equals("path")) {
					fileName = tokens.nextToken();

				} else if (data[col].equals("tree")) {
					treeFileName = tokens.nextToken();
				} else if (data[col].equals("overZone")) {
					overZoneFileName = tokens.nextToken();
				}

			}

			row++;

		}
		RECORD_SIZE = RECORD_SIZE + 2;

		// postavljanje atributa datoteke
		RandomAccessFile afile = new RandomAccessFile(path + File.separator + fileName, "r");
		FILE_SIZE = afile.length();
		RECORD_NUM = (long) Math.ceil((FILE_SIZE * 1.0000) / (RECORD_SIZE * 1.0000));
		BLOCK_NUM = (int) (RECORD_NUM / BLOCK_FACTOR) + 1;
		afile.close();
		// makeTree();
		openTree(path + File.separator + treeFileName);

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
	public boolean fetchNextBlock(boolean bool) throws IOException {

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
		if (contentS.length() < buffer.length) {
			for (int x = contentS.length(); x < buffer.length; x++)
				contentS = contentS + " ";
		}

		for (int i = 0; i < BUFFER_SIZE / RECORD_SIZE; i++) {

			String line = contentS.substring(i * RECORD_SIZE, i * RECORD_SIZE + RECORD_SIZE);
			data[i] = new String[fields.size()];
			int k = 0;
			for (int j = 0; j < fields.size(); j++) {
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

	public boolean findRecord(Node node, UIINDFile sekFile, ArrayList<String> searchRec) {
		long zapamcenBlock = sekFile.BLOCK_FACTOR;
		long zapamcenPointer = sekFile.FILE_POINTER;

		System.out.println("OVOLIKO SAM : " + node.getData().get(0).getBlockAddress());
		
		sekFile.setBLOCK_SIZE(node.getData().get(1).getBlockAddress() - node.getData().get(0).getBlockAddress());
		sekFile.FILE_POINTER = node.getData().get(0).getBlockAddress()*sekFile.getRECORD_SIZE();// - sekFile.BLOCK_FACTOR;
		if (sekFile.FILE_POINTER < 0) sekFile.FILE_POINTER = 0;

		ArrayList<UIFileField> fileFields = new ArrayList<>();
		for (int j = 0; j < sekFile.getFields().size(); j++) {
			if (sekFile.getFields().get(j).isFieldPK()) {
				fileFields.add(sekFile.getFields().get(j));
			}
		}

		try {
			sekFile.fetchNextBlock(false);
			for (int row = 0; row < sekFile.data.length; row++) {
				if (isRowEqual(sekFile.data[row], searchRec,sekFile,fileFields)) {
					Wrapper wr = new Wrapper(sekFile.getData(), row, row);
					notify(wr);
					sekFile.setBLOCK_SIZE(zapamcenBlock);
					sekFile.FILE_POINTER = zapamcenPointer;
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		sekFile.setBLOCK_SIZE(zapamcenBlock);
		sekFile.FILE_POINTER = zapamcenPointer;
		return false;
	}

	private static boolean isRowEqual(String[] aData, ArrayList<String> searchRec, UIAbstractFile f, ArrayList<UIFileField> fileFields) {
		boolean result = true;
		/*
		System.out.print("Here "); for (String s : aData) System.out.print(" " + s);
	    System.out.println();
	    System.out.print("Here1 "); for (String s : searchRec) System.out.print(" " + s);
	    System.out.println();
	    */
		for (int i = 0; i < fileFields.size(); i++) {
			for (int j = 0; j < f.getFields().size(); j++) {
				if (f.getFields().get(j).equals(fileFields.get(i))) {
					if (!searchRec.get(i).trim().equals("")) {
						if (!aData[j].trim().equals(searchRec.get(i).trim())) {
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

	/**
	 * Metoda za dodavanje sloga - Neće se realizovati
	 * 
	 */

	public boolean addRecord(ArrayList<String> record) throws IOException {

		return true;
	}

	/**
	 * Izmena podataka - Neće se realizovati
	 * 
	 */
	public boolean updateRecord(ArrayList<String> record) throws IOException {

		return false;
	}

	/**
	 * Pretraga
	 */
	public boolean findRecord(ArrayList<String> searchRec, int[] position) {
		boolean result = false;

		return result;
	}

	/**
	 * Brisanje - Neće se realizovati
	 */
	public boolean deleteRecord(ArrayList<String> searchRec) {
		return false;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	private void openTree(String treeFilePath) {
		ObjectInputStream os = null;
		try {
			os = new ObjectInputStream(new FileInputStream(treeFilePath));
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		try {
			tree = (Tree) os.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
