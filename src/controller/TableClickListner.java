package controller;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import view.Frame1;
import wrapper.MojPanel;
import wrapper.MojScrollPane;
import model.Atribut;
import model.Entitet;
import model.file.UIAbstractFile;
import model.file.UIFileField;
import model.file.UISEKFile;
import state.EditState;
import state.RelationState;
import tabels.TableModel1;
import model.Package;
import model.db.UIDBFile;

public class TableClickListner implements ListSelectionListener {

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (Frame1.getInstance().getStateManager().getCurrentState() instanceof RelationState) {
			RelationState state = (RelationState) Frame1.getInstance().getStateManager().getCurrentState();
			JTable table = ((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent())
					.getTabela();
			Entitet entitet = Frame1.getInstance().getPanelRT().getSelectedEntitet();
			if (((Package) entitet.getParent()).getConnection() != "") {
				int row = table.getSelectedRow();
				if (row == -1)
					return;
				ArrayList<JTable> tabele = state.getTables();
				ArrayList<Entitet> entities = state.getEntities();
				//System.out.println(entities);
				ArrayList<Atribut> attributes = state.getAttributes();
				for (int i = 0; i < entities.size(); i++) {
					String value = "";
					for (int j = 0; j < entitet.getAtributi().size(); j++) {
						//System.out.println(attributes);
						if (entitet.getAtributi().get(j).getName().equals(attributes.get(i).getName()))
							value = table.getValueAt(row, j).toString();
					}
					if (entitet instanceof UIDBFile) {
						ResultSet rs = ((UIDBFile) entitet).RelationFind(entities.get(i), attributes.get(i), value);
						TableModel1 model = new TableModel1(entities.get(i), rs);
						tabele.get(i).setModel(model);
					}
				}

			} else {
				int row = table.getSelectedRow();
				if (row == -1)
					return;
				ArrayList<String> lista = new ArrayList<>();
				for (int i = 0; i < entitet.getAtributi().size(); i++) {
					if (entitet.getAtributi().get(i).isPrimarniKljuc())
						lista.add(table.getValueAt(row, i).toString());
				}

				ArrayList<Entitet> veze = new ArrayList<>();
				for (int i = 0; i < entitet.getRelacije().size(); i++) {
					if (entitet.getRelacije().get(i).getFromEntity() == entitet) {
						veze.add(entitet.getRelacije().get(i).getToEntity());
					} else
						veze.add(entitet.getRelacije().get(i).getFromEntity());
				}

				ArrayList<UISEKFile> files = new ArrayList<>();

				for (int i = 0; i < veze.size(); i++) {
					String split[] = veze.get(i).getUrl().split("/");
					File file = new File("Veliki Set Podataka" + File.separator + split[0]);
					UISEKFile indFile = new UISEKFile(file.getAbsolutePath(), split[1], false);
					try {
						indFile.readHeader();
						files.add(indFile);
					} catch (IOException ee) {
						ee.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				UIAbstractFile abstFile = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();

				ArrayList<UIFileField> fileFields = new ArrayList<>();
				for (int j = 0; j < abstFile.getFields().size(); j++) {
					if (abstFile.getFields().get(j).isFieldPK()) {
						fileFields.add(abstFile.getFields().get(j));
					}
				}

				ArrayList<ArrayList<String>> niz = new ArrayList<>();
				for (int i = 0; i < files.size(); i++) {
					niz.add(UISEKFile.linearSearchZaNemanju(lista, files.get(i), fileFields));
				}

				for (int i = 0; i < niz.size(); i++) {
					if (niz.get(i) != null) {
						String[][] split = new String[niz.get(i).size()][];
						for (int br = 0; br < niz.get(i).size(); br++) {
							split[br] = niz.get(i).get(br).split("___");
						}
						MojScrollPane pane = (MojScrollPane) state.getTabbedPane().getComponentAt(i);
						TableModel1 t = new TableModel1(pane.getEntitet(), split);
						pane.getTable().setModel(t);
						pane.setTableModel(t);
					}
				}
			}
		} else if (Frame1.getInstance().getStateManager().getCurrentState() instanceof EditState) {
			EditState state = (EditState) Frame1.getInstance().getStateManager().getCurrentState();
			JTable table = ((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent())
					.getTabela();
			Entitet entitet = Frame1.getInstance().getPanelRT().getSelectedEntitet();
			int row = table.getSelectedRow();
			if (row == -1)
				return;
			ArrayList<String> lista = new ArrayList<>();

			for (int i = 0; i < entitet.getAtributi().size(); i++) {
				lista.add(table.getValueAt(row, i).toString());
			}

			for (int i = 0; i < lista.size(); i++) {
				state.getTextFields()[i].setText(lista.get(i).trim());
			}
		}

	}

}
