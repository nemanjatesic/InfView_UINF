package actions.file;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;
import model.file.UIAbstractFile;
import model.file.UISERFile;

public class UndeleteAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*UIAbstractFile file =  Frame1.getInstanceOfFrame1().getPanelRT().getHashMap().get(Frame1.getInstanceOfFrame1().getPanelRT().getSelectedEntitet());
		if(file instanceof UISERFile)
		{
			int row = ((MojPanel) Frame1.getInstanceOfFrame1().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela().getSelectedRow();
			if(row==-1) {
				MessageController.errorMessage("Selektujte red ukoliko zelite da obrisete nesto.");
				Frame1.getInstanceOfFrame1().getStateManager().setDelSer();
				Frame1.getInstanceOfFrame1().getPanelRT().update();
				return;
			}
			StringBuffer line=new StringBuffer();
			String [][]data=((UISERFile)file).getData();
			for(int i=0; i<file.getFields().size(); i++) {
				line.append(data[row][i]);
			}
			String del=line.toString();
			System.out.println(del);
			((UISERFile)file).deleteRecord(Frame1.getInstanceOfFrame1().getPanelRT().getSelectedEntitet(), del);
			Frame1.getInstanceOfFrame1().getStateManager().setDelSer();
			Frame1.getInstanceOfFrame1().getPanelRT().update();
			MessageController.infoMessage("Uspesno ste obrisali red.\r\n Ukoliko zelite da ponistite ovu akciju, pogledajte RecycleBin.");
		}*/
		
	}

}
