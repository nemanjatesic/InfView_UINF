package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import controller.MouseController;
import events.Observer;
import model.Entitet;
import model.Relacija;
import tabels.TableModel1;

public class PanelRB extends JPanel implements Observer {

	private JTabbedPane tabbedPane = new JTabbedPane();
	Entitet e = null;

	// kreiramo panel i prosledjujemo listener cvoru
	public PanelRB() {
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
	}

	// ovo se desi kad node pozove notify
	@Override
	public void update(Object curr) {
		if (e == null && curr != null)
			e = (Entitet) curr;

		if (e != null) {
			tabbedPane.removeAll();
			if (curr != null)
				e = (Entitet) curr;
			ArrayList<Relacija> relacije = e.getRelacije();

			for (Relacija rel : relacije) {
				if (rel.getToEntity().getName().equals(e.getName())) {
					TableModel1 model = new TableModel1(rel.getFromEntity());
					JTable tabela = new JTable(model);
					tabela.setPreferredScrollableViewportSize(new Dimension(500, 400));
					tabela.setFillsViewportHeight(true);
					tabbedPane.addTab(rel.getFromEntity().getName(), new JScrollPane(tabela));
				} else if (rel.getFromEntity().getName().equals(e.getName())) {
					TableModel1 model = new TableModel1(rel.getToEntity());
					JTable tabela = new JTable(model);
					tabela.setPreferredScrollableViewportSize(new Dimension(500, 400));
					tabela.setFillsViewportHeight(true);
					tabbedPane.addTab(rel.getToEntity().getName(), new JScrollPane(tabela));
				}
			}
		}
	}

}
