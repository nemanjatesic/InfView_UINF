package model.file;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IUIFile
{
	public void readHeader() throws IOException,SQLException;
	public boolean fetchNextBlock(boolean bool) throws IOException;
	public boolean addRecord(ArrayList<String> record) throws IOException, SQLException;
	public boolean updateRecord(ArrayList<String> record)throws IOException, SQLException;
	public int findRecord(ArrayList<String> searchRec);
	public boolean deleteRecord(ArrayList<String> searchRec)throws IOException, SQLException;
}
