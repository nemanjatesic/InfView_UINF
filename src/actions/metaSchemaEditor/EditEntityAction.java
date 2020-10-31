package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.EditEntityDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Entitet;
import model.Relacija;
import parse.MagicJSON;

public class EditEntityAction implements ActionListener{
	EditEntityDialog editEntityDialog;
	
	public EditEntityAction(EditEntityDialog editEntityDialog) {
		this.editEntityDialog = editEntityDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String change = editEntityDialog.getNameTf().getText();
		Entitet entitet = ((Entitet)editEntityDialog.getEntitiesCombo().getSelectedItem());

		int index=-1;
		for(int i=0; i<Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount(); i++){
			if(Frame1.getInstance().getPanelRT().getTabbedPane().getTitleAt(i).equals(entitet.getName())){
				index=i;
				break;
			}
		}
		if(index!=-1) {
			Frame1.getInstance().getPanelRT().getTabbedPane().setTitleAt(index, change);
		}
		entitet.setName(change);		
		
		Frame1.getInstance().getPanelRT().notify(null);

		editEntityDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}
