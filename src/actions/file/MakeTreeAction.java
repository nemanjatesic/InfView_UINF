package actions.file;

import java.awt.event.ActionEvent;
import java.io.IOException;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import view.PanelRT;
import model.Entitet;
import model.TipEntiteta;
import model.file.UISEKFile;

public class MakeTreeAction extends AbstractActionI{

	@Override
	public void actionPerformed(ActionEvent arg0) {
				
				Entitet ent = Frame1.getInstance().getPanelRT().getSelectedEntitet();
				if (ent == null) return;
				
				if (ent.getTipEntiteta() != TipEntiteta.sequential) {
					MessageController.errorMessage("Molimo selektujte sekvencijalnu datoteku.");
					ent = null;
					return;
				}
				try {
					((UISEKFile)Frame1.getInstance().getPanelRT().getSelectedEntitet()).makeTree();
					MessageController.infoMessage("Uspesno ste serijalizovali stablo.");
					System.out.println("Stablo upisano, za veci ispis na konzoli pogledati klasu model.tree.Tree.");
				} catch (IOException e1) {
					System.out.println("greska make tree");
				}
		
	}

}
