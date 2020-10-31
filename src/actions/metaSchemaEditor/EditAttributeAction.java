package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.EditAttributeDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Atribut;
import model.Entitet;
import model.TipAtributa;
import parse.MagicJSON;

public class EditAttributeAction implements ActionListener{
	EditAttributeDialog editAttributeDialog;
	
	public EditAttributeAction(EditAttributeDialog editAttributeDialog) {
		this.editAttributeDialog = editAttributeDialog;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Atribut atribut = (Atribut)editAttributeDialog.getAttributes().getSelectedItem();
		Entitet entitet = (Entitet)editAttributeDialog.getEntities().getSelectedItem();
		int length = Integer.parseInt(editAttributeDialog.getLengthField().getText());
		boolean primaryKey = ((String)editAttributeDialog.getIsPrimaryKeyCB().getSelectedItem()).equals("True") ?  true : false;
		boolean mandatory = ((String)editAttributeDialog.getIsMandatoryCB().getSelectedItem()).equals("True") ?  true : false;
		TipAtributa tip = null;
		String name = editAttributeDialog.getNameField().getText();
		
		if (name.equals("") || entitet == null || atribut == null) {
			editAttributeDialog.dispose();
			return;
		}
		
		TipAtributa[] cao = TipAtributa.values();
		for (int i = 0 ; i < cao.length ; i++) {
			if (cao[i].toString().equals((String)editAttributeDialog.getTypeCB().getSelectedItem())) {
				tip = cao[i];
				break;
			}
		}
		
		atribut.setName(name);
		atribut.setPrimarniKljuc(primaryKey);
		atribut.setDuzina(length);
		atribut.setObavezan(mandatory);
		atribut.setTip(tip);
		
		for(int i=0; i<Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount(); i++){
			if(Frame1.getInstance().getPanelRT().getTabbedPane().getTitleAt(i).equals(entitet.getName())){
				Frame1.getInstance().getPanelRT().update(entitet);
			}
		}
		Frame1.getInstance().getPanelRT().notify(null);

		editAttributeDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}
