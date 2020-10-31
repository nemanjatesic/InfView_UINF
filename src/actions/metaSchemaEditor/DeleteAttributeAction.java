package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.DeleteAttributeDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Atribut;
import model.Entitet;
import model.InfResurs;
import model.Relacija;
import parse.MagicJSON;

public class DeleteAttributeAction implements ActionListener{
	DeleteAttributeDialog deleteAttributeDialog;
	
	public DeleteAttributeAction(DeleteAttributeDialog deleteAttributeDialog) {
		this.deleteAttributeDialog = deleteAttributeDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		InfResurs infRes = MetaSchemaEditFrame.getCurrentInstance().getIr();
		Entitet entitet = (Entitet)deleteAttributeDialog.getEntities().getSelectedItem();
		Atribut atribut = (Atribut)deleteAttributeDialog.getAttributes().getSelectedItem();
		
		if (entitet == null || atribut == null) {
			deleteAttributeDialog.dispose();
			return;
		}
		ArrayList<Relacija> tmp = new ArrayList<>();
		ArrayList<Relacija> tmp1 = entitet.getRelacije();
		for (int i = 0 ; i < tmp1.size() ; i++) {
			if (tmp1.get(i).getFromAttribute() == atribut || tmp1.get(i).getToAttribute() == atribut) {
				Relacija relacija = tmp1.get(i);
				Entitet entitet1 = null;
				if (relacija.getFromEntity() == entitet) entitet1 = relacija.getToEntity();
				else if (relacija.getToEntity() == entitet) entitet1 = relacija.getFromEntity();
				if (entitet1 == null) continue;
				entitet1.obrisiRelaciju2(relacija);
				entitet.obrisiRelaciju2(relacija);
				
				tmp.add(relacija);
				i--;
			}
		}
		entitet.getChildren().remove(atribut);
		entitet.obrirsiAtribut2(atribut);
		
		
		for(int i=0; i<Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount(); i++){
			if(Frame1.getInstance().getPanelRT().getTabbedPane().getTitleAt(i).equals(entitet.getName())){
				Frame1.getInstance().getPanelRT().update(entitet);
			}
		}
		
		
		/*tmp1 = infRes.getRelations();
		for(Relacija relacija : tmp) {
			tmp1.remove(relacija);
		}*/
		
		Frame1.getInstance().getPanelRT().notify(null);

		deleteAttributeDialog.dispose();
		
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}
