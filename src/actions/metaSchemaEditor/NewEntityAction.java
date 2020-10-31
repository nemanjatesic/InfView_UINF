package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import view.metaSchemaEditor.NewEntityDialog;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import parse.MagicJSON;

public class NewEntityAction implements ActionListener{
	NewEntityDialog newEntityDialog;
	InfResurs infRes;
	
	public NewEntityAction(InfResurs infRes, NewEntityDialog newEntityDialog) {
		this.newEntityDialog = newEntityDialog;
		this.infRes = infRes;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		NodeInf node = new Entitet(newEntityDialog.getEntityNameTextField().getText());
		node.setParent(infRes);
		((NodeInf)infRes).addChild(node);
		newEntityDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(infRes);
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}