package wrapper;

import java.util.ArrayList;
import java.util.Vector;

public class Wrapper {

	private String[][] data = null;
	private int firstRow = -2;
	private int secondRow = -2;
	private Vector<Vector<String>> vectorData=null;
	private ArrayList<String> groupByList=null;
	
	public Wrapper(String[][] data, int firstRow, int secondRow) {
		this.data = data;
		this.firstRow = firstRow;
		this.secondRow = secondRow;
	}
	
	public Wrapper(Vector<Vector<String>> vectorData, ArrayList<String> groupByList)
	{
		this.vectorData=vectorData;
		this.groupByList=groupByList;
	}
	
	public Wrapper(String[][] data) {
		this.data = data;
	}

	public String[][] getData() {
		return data;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getSecondRow() {
		return secondRow;
	}

	public Vector<Vector<String>> getVectorData()
	{
		return vectorData;
	}

	public ArrayList<String> getGroupByList()
	{
		return groupByList;
	}
	
}
