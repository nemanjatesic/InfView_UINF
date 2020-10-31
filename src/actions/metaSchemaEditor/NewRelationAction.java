package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import view.metaSchemaEditor.NewRelationDialog;
import model.Atribut;
import model.Entitet;
import model.Relacija;
import parse.MagicJSON;

public class NewRelationAction implements ActionListener {
	NewRelationDialog newRelationDialog;

	public NewRelationAction(NewRelationDialog newRelationAction) {
		this.newRelationDialog = newRelationAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (newRelationDialog.getFromEntCombo().getSelectedItem() == null
				|| newRelationDialog.getToEntCombo().getSelectedItem() == null) {
			newRelationDialog.dispose();
			return;
		}
		if (newRelationDialog.getNameTf().getText().equals("")) {
			newRelationDialog.dispose();
			return;
		}
		Relacija relacija = new Relacija(newRelationDialog.getNameTf().getText(),
				(Entitet) newRelationDialog.getFromEntCombo().getSelectedItem(),
				(Entitet) newRelationDialog.getToEntCombo().getSelectedItem(),
				(Atribut) newRelationDialog.getFromAttCombo().getSelectedItem(),
				(Atribut) newRelationDialog.getToAttCombo().getSelectedItem());
		//MetaSchemaEditFrame.getCurrentInstance().getIr().getRelations().add(relacija);
		((Entitet)newRelationDialog.getFromEntCombo().getSelectedItem()).getRelacije().add(relacija);
		((Entitet)newRelationDialog.getToEntCombo().getSelectedItem()).getRelacije().add(relacija);
		
		if(Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount()>0) {
		String ime=(Frame1.getInstance().getPanelRT().getTabbedPane()).getTitleAt
				(Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedIndex());
		if(((Entitet)newRelationDialog.getToEntCombo().getSelectedItem()).getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(null);
		else if(((Entitet)newRelationDialog.getFromEntCombo().getSelectedItem()).getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(null);
		}
		
		newRelationDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}