package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import view.metaSchemaEditor.NewAttributeDialog;
import model.Atribut;
import model.Entitet;
import model.NodeInf;
import model.TipAtributa;
import parse.MagicJSON;

public class NewAttributeAction implements ActionListener{
	NewAttributeDialog newAttributeDialog;
	
	public NewAttributeAction(NewAttributeDialog newAttributeDialog) {
		this.newAttributeDialog = newAttributeDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Entitet entitet = (Entitet)newAttributeDialog.getEntities().getSelectedItem();
		int length = Integer.parseInt(newAttributeDialog.getLengthField().getText());
		boolean primaryKey = ((String)newAttributeDialog.getIsPrimaryKeyCB().getSelectedItem()).equals("True") ?  true : false;
		boolean mandatory = ((String)newAttributeDialog.getIsMandatoryCB().getSelectedItem()).equals("True") ?  true : false;
		TipAtributa tip = null;
		String name = newAttributeDialog.getNameField().getText();
		
		if (name.equals("") || entitet == null) {
			newAttributeDialog.dispose();
			return;
		}
		
		TipAtributa[] cao = TipAtributa.values();
		for (int i = 0 ; i < cao.length ; i++) {
			if (cao[i].toString().equals((String)newAttributeDialog.getTypeCB().getSelectedItem())) {
				tip = cao[i];
				break;
			}
		}
		
		Atribut atribut = new Atribut(name, tip, length, primaryKey, mandatory, "null");
		atribut.setParent(entitet);
		entitet.addChild(atribut);
		entitet.dodajAtribut(atribut);
		
		
		for(int i=0; i<Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount(); i++){
			if(Frame1.getInstance().getPanelRT().getTabbedPane().getTitleAt(i).equals(entitet.getName())){
				Frame1.getInstance().getPanelRT().update(entitet);
			}
		}
		
	
	
		Frame1.getInstance().getPanelRT().notify(null);

		newAttributeDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}