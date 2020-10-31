package tabels;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Atribut;
import model.Entitet;

public class TableModel1 implements TableModel {

	private Object[] columns;
	int n;
	private Entitet entitet;
	// trebace nam kasnije,sad nemamo podatke
	private Object[][] data;

	public TableModel1(Entitet e) {
		int i = 0;
		columns = new Object[e.getAtributi().size()];
		ArrayList<Atribut> atributi = e.getAtributi();
		for (Atribut a : atributi) {
			columns[i] = a.getName();
			i++;
		}
		n = i;
		entitet = e;
		data = new Object[0][0];
	}

	public TableModel1(Entitet e, String data[][]) {
		int i = 0;
		columns = new Object[e.getAtributi().size()];
		ArrayList<Atribut> atributi = e.getAtributi();
		for (Atribut a : atributi) {
			columns[i] = a.getName();
			i++;
		}
		n = i;
		entitet = e;
		this.data = data;
	}

	public TableModel1(Entitet e, ResultSet rs) {
		int i = 0;
		columns = new Object[e.getAtributi().size()];
		ArrayList<Atribut> atributi = e.getAtributi();
		for (Atribut a : atributi) {
			columns[i] = a.getName();
			i++;
		}
		n = i;
		entitet = e;
		try {
			buildTableModel(rs);
		} catch (SQLException e1) {
			System.out.println("Greska klasa TableModel1");
		}

	}

	public TableModel1(Vector<Vector<String>> vectorData, ArrayList<String> groupByList) {
		data = new Object[vectorData.size()][vectorData.get(0).size()];
		for (int i = 0; i < vectorData.size(); i++) {
			for (int j = 0; j < vectorData.get(i).size(); j++) {
				data[i][j] = vectorData.get(i).get(j);
			}
		}
		columns = new Object[groupByList.size()];
		for (int i = 0; i < groupByList.size(); i++) {
			columns[i] = groupByList.get(i);
		}
		n = groupByList.size();
	}

	public void buildTableModel(ResultSet rs) throws SQLException {

		Vector<Vector<String>> data1 = new Vector<Vector<String>>();
		while (rs.next()) {
			Vector<String> vector = new Vector<String>();
			for (int columnIndex = 1; columnIndex <= this.getColumnCount(); columnIndex++) {
				vector.add(rs.getObject(columnIndex).toString());
			}
			data1.add(vector);
		}
		this.data = new String[data1.size()][this.getColumnCount()];
		for (int i = 0; i < data1.size(); i++)
			for (int j = 0; j < this.getColumnCount(); j++)
				data[i][j] = data1.get(i).get(j);
	}

	public Entitet getEntitet() {
		return entitet;
	}

	public void newColumn(String s) {
		Object[] cols = new Object[n + 1];
		for (int i = 0; i < n; i++)
			cols[i] = columns[i];
		cols[n] = s;
		n++;
		columns = cols;

	}

	public Object[] getColumns() {
		return columns;
	}

	public void setColumns(Object[] columns) {
		this.columns = columns;
	}

	/// promeniti kad budemo imali podatke:

	@Override
	public int getRowCount() {
		int i = 0;
		while (i < data.length && data[i] != null) {
			i++;
		}
		return i;
	}

	@Override
	public int getColumnCount() {

		return n;
	}

	@Override
	public String getColumnName(int columnIndex) {

		return (String) columns[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}

	@Override
	public String toString() {
		return "";
	}

	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

}
