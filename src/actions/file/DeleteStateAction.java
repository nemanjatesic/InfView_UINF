package actions.file;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;
import model.file.UIAbstractFile;
import model.file.UISEKFile;
import model.file.UISERFile;

public class DeleteStateAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent e) {
		UIAbstractFile file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		int row = ((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela().getSelectedRow();
		if (row == -1) return;
		
		StringBuffer line = new StringBuffer();
		String[][] data = file.getData();
		
		for (int i = 0; i < file.getFields().size(); i++)  
			line.append(data[row][i]);
		
		String del = line.toString();
		
		if (file instanceof UISERFile) {
			((UISERFile) file).deleteRecord(Frame1.getInstance().getPanelRT().getSelectedEntitet(), del);
		} else if (file instanceof UISEKFile) {
			int br = file.getFields().size() - 1;
			int duzina = file.getFields().get(br).getFieldLength() - data[row][br].length();
			
			for (int i = 0 ; i < duzina ; i++)
				del += " ";
			
			StaticFileCreate.doStuff(del + "D");
			
			MessageController.infoMessage("Uspeno ste obrisali resurs. Kako bi ste napravili izmene pritisnite Save.");
			
			// Obrisati kasnije
			String[] tmpString = new String[data[row].length + 1];
			for (int i = 0 ; i < data[row].length ; i++) {
				tmpString[i] = data[row][i];
			}
			tmpString[data[row].length] = "D";
			StaticFileCreate.getDataTmp().add(tmpString);
			for (String[] s : StaticFileCreate.getDataTmp()) {
				for (String ss : s) 
					System.out.print(ss + " ");
				System.out.println();
			}
			System.out.println("--------------");
		}
	}

}
