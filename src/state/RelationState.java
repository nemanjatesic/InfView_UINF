package state;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.mozilla.javascript.UintMap;

import events.Observer;
import view.Frame1;
import wrapper.MojScrollPane;
import model.Atribut;
import model.Entitet;
import model.Relacija;
import model.db.UIDBFile;
import model.file.UIINDFile;
import tabels.TableModel1;

public class RelationState extends State {

	JTabbedPane tabbedPane;
	ArrayList<JTable> tables=new ArrayList<>();
	ArrayList<Entitet> entities=new ArrayList<>();
	ArrayList<Atribut> attributes=new ArrayList<>();
	
	public RelationState(JPanel panel) {
		super(panel);
	}

	public void initState() {
		tables=new ArrayList<>();
		entities=new ArrayList<>();
		attributes=new ArrayList<>();
		tabbedPane = new JTabbedPane();
		panel.setLayout(new BorderLayout());
		panel.add(tabbedPane);
		Entitet e = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		ArrayList<Relacija> relacije = e.getRelacije();
		

			for (Relacija rel : relacije) {
				if (rel.getToEntity().getName().equals(e.getName())) {
					System.out.println(rel.getToAttribute()+"****"+rel.getFromAttribute());
					TableModel1 model = new TableModel1(rel.getFromEntity());
					JTable tabela = new JTable(model);
					tabela.setPreferredScrollableViewportSize(new Dimension(500, 400));
					tabela.setFillsViewportHeight(true);
					tabbedPane.addTab(rel.getFromEntity().getName(), new MojScrollPane(tabela,model,rel.getFromEntity()));
					tables.add(tabela);
					entities.add(rel.getFromEntity());
					attributes.add(rel.getFromAttribute());
				} else if (rel.getFromEntity().getName().equals(e.getName())) {
					TableModel1 model = new TableModel1(rel.getToEntity());
					JTable tabela = new JTable(model);
					tabela.setPreferredScrollableViewportSize(new Dimension(500, 400));
					tabela.setFillsViewportHeight(true);
					tables.add(tabela);
					entities.add(rel.getToEntity());
					attributes.add(rel.getToAttribute());
					tabbedPane.addTab(rel.getToEntity().getName(), new MojScrollPane(tabela,model,rel.getToEntity()));
				}
			}
		
		
	}
	public ArrayList<Atribut> getAttributes() {
		return attributes;
	}
	
	public ArrayList<Entitet> getEntities() {
		return entities;
	}
	
	public ArrayList<JTable> getTables() {
		return tables;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

}
