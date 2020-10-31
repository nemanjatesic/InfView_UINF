package wrapper;

import javax.swing.JPanel;
import javax.swing.JTable;

import model.Entitet;
import model.file.UISEKFile;

public class MojPanel extends JPanel{
	
	private Entitet entitet;
	private JTable tabela;
	private JPanel panel;
	private UISEKFile file;
	private String pathZaSek;
	private boolean countOrAverage = false;
	
	public MojPanel(Entitet entitet, JTable tabela, JPanel panel) {
		super();
		this.entitet = entitet;
		this.tabela = tabela;
		this.panel = panel;
	}
	
	public Entitet getEntitet() {
		return entitet;
	}
	
	public JTable getTabela() {
		return tabela;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public UISEKFile getFile() {
		return file;
	}
	
	public void setFile(UISEKFile file) {
		this.file = file;
	}
	
	public String getPathZaSek() {
		return pathZaSek;
	}
	
	public void setPathZaSek(String pathZaSek) {
		this.pathZaSek = pathZaSek;
	}
	
	public boolean isCountOrAverage() {
		return countOrAverage;
	}
	
	public void setCountOrAverage(boolean countOrAverage) {
		this.countOrAverage = countOrAverage;
	}
}
