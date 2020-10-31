package wrapper;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Entitet;
import tabels.TableModel1;

public class MojScrollPane extends JScrollPane{

	private JTable table;
	private TableModel1 tableModel;
	private Entitet entitet;
	
	public MojScrollPane(JTable table, TableModel1 tableModel, Entitet entitet) {
		super(table);
		this.table = table;
		this.tableModel = tableModel;
		this.entitet = entitet;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public TableModel1 getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel1 tableModel) {
		this.tableModel = tableModel;
	}

	public Entitet getEntitet() {
		return entitet;
	}

	public void setEntitet(Entitet entitet) {
		this.entitet = entitet;
	}
	
	
}
