package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.DeleteRelationDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Entitet;
import model.Relacija;
import parse.MagicJSON;

public class DeleteRelationAction implements ActionListener{
	DeleteRelationDialog deleteRelationDialog;
	
	public DeleteRelationAction(DeleteRelationDialog deleteRelationDialog) {
		this.deleteRelationDialog = deleteRelationDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (deleteRelationDialog.getRelationsCombo().getSelectedItem() == null) {
			deleteRelationDialog.dispose();
			return;
		}
		((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem()).getFromEntity().getRelacije().remove((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem());
		((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem()).getToEntity().getRelacije().remove((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem());
		/*MetaSchemaEditFrame.getCurrentInstance().getIr().getRelations().remove((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem());
		*/
		if(Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount()>0) {
		String ime=(Frame1.getInstance().getPanelRT().getTabbedPane()).getTitleAt
				(Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedIndex());
		if(((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem()).getFromEntity().getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(((Relacija)deleteRelationDialog.getRelationsCombo()
					.getSelectedItem()).getFromEntity());
		else if(((Relacija)deleteRelationDialog.getRelationsCombo().getSelectedItem()).getToEntity().getName().equals(ime))
			Frame1.getInstance().getPanelRT().notify(((Relacija)deleteRelationDialog.getRelationsCombo()
					.getSelectedItem()).getToEntity());
		}
		
		deleteRelationDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}
