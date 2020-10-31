package actions.metaSchemaEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import controller.MessageController;
import view.Frame1;
import view.metaSchemaEditor.DeleteEntityDialog;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.Entitet;
import model.InfResurs;
import model.Relacija;
import parse.MagicJSON;

public class DeleteEntityAction implements ActionListener{
	DeleteEntityDialog deleteEntityDialog;
	InfResurs infRes;

	public DeleteEntityAction(InfResurs infRes, DeleteEntityDialog deleteEntityDialog) {
		this.infRes = infRes;
		this.deleteEntityDialog = deleteEntityDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		infRes = MetaSchemaEditFrame.getCurrentInstance().getIr();
		Entitet entitet = (Entitet)deleteEntityDialog.getEntitiesCombo().getSelectedItem();
		ArrayList<Relacija> tmp = new ArrayList<>();
		ArrayList<Relacija> tmp1 = entitet.getRelacije();
		for (int i = 0 ; i < tmp1.size() ; i++) {
			Relacija relacija = tmp1.get(i);
			Entitet entitet1 = null;
			if (relacija.getFromEntity() == entitet) entitet1 = relacija.getToEntity();
			else if (relacija.getToEntity() == entitet) entitet1 = relacija.getFromEntity();
			if (entitet1 == null) continue;
			entitet1.getRelacije().remove(relacija);
			entitet.getRelacije().remove(relacija);
			
			tmp.add(relacija);
			i--;
		}
		int index=-1;
		for(int i=0; i<Frame1.getInstance().getPanelRT().getTabbedPane().getTabCount(); i++){
			if(Frame1.getInstance().getPanelRT().getTabbedPane().getTitleAt(i).equals(entitet.getName())){
				index=i;
				break;
			}
		}
		if(index!=-1)
			Frame1.getInstance().getPanelRT().getTabbedPane().removeTabAt(index);
		
		infRes.getChildren().remove(entitet);
		/*tmp1 = infRes.getRelations();
		for(Relacija relacija : tmp) {
			tmp1.remove(relacija);
		}*/
		
		Frame1.getInstance().getPanelRT().notify(null);

		deleteEntityDialog.dispose();
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		MagicJSON.writeToJSON(MetaSchemaEditFrame.getCurrentInstance().getIr());
		MetaSchemaEditFrame.getJsonText().setText(MetaSchemaEditFrame.getCurrentInstance().getIr().getJsonString());
		MetaSchemaEditFrame.getJsonText().setCaretPosition(0);
		
	}

}
