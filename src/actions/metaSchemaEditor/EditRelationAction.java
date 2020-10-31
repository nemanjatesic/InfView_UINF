package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.EditRelationDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Atribut;
import model.Entitet;
import model.Relacija;
import parse.MagicJSON;

public class EditRelationAction implements ActionListener{
	EditRelationDialog editRelationDialog;
	
	
	public EditRelationAction(EditRelationDialog editRelationDialog) {
		this.editRelationDialog = editRelationDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Relacija relacija = (Relacija)editRelationDialog.getRelationsCombo().getSelectedItem();
		String name = editRelationDialog.getNameTf().getText();
		Entitet entitet1 = (Entitet)editRelationDialog.getFromEntCombo().getSelectedItem();
		Entitet entitet2 = (Entitet)editRelationDialog.getToEntCombo().getSelectedItem();
		Atribut atribut1 = (Atribut)editRelationDialog.getFromAttCombo().getSelectedItem();
		Atribut atribut2 = (Atribut)editRelationDialog.getToAttCombo().getSelectedItem();
		
		if (relacija == null || entitet1 == null || entitet2 == null || atribut1 == null || atribut2 == null || name.equals("")) {
			editRelationDialog.dispose();
			return;
		}
		
		relacija.setName(name);
		relacija.setFromEntity(entitet1);
		relacija.setToEntity(entitet2);
		relacija.setFromAttribute(atribut1);
		relacija.setToAttribute(atribut2);
        
		if(Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount()>0) {
		String ime=(Frame1.getInstance().getPanelRT().getTabbedPane()).getTitleAt
				(Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedIndex());
		if(entitet1.getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(entitet1);
		else if(entitet2.getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(entitet2);
		}
		
		editRelationDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}