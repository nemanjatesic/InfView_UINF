package actions.file;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextField;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import model.file.UIAbstractFile;
import model.file.UIFileField;
import model.file.UISEKFile;
import model.file.UISERFile;
import state.AddState;

public class AddStateAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent e) {
		Frame1 frame = Frame1.getInstance();
		UIAbstractFile file = (UIAbstractFile)frame.getPanelRT().getSelectedEntitet();
		if (file instanceof UISEKFile) {

			JTextField[] textFields = ((AddState) frame.getStateManager().getCurrentState()).getTextFields();
			String[] tekst = new String[textFields.length];
			for (int i = 0; i < textFields.length; i++) {
				tekst[i] = textFields[i].getText();
			}
			ArrayList<UIFileField> fileFields = file.getFields();

			// Provera da li je sve po propisu filovih polja
			for (int i = 0; i < fileFields.size(); i++) {
				if (tekst[i].length() > fileFields.get(i).getFieldLength()) {
					MessageController.errorMessage("Polje : " + fileFields.get(i).getFieldName()
							+ " , ima previse karaktera. Maksimalan broj karaktera za to polje je : " + fileFields.get(i).getFieldLength());
					return;
				}
			}

			// Pravljenje stringa koji ce se upisati u fajl
			String upis = "";
			for (int i = 0; i < tekst.length; i++) {
				int broj = fileFields.get(i).getFieldLength() - tekst[i].length();
				for (int j = 0; j < broj; j++) {
					tekst[i] += " ";
				}
				upis += tekst[i];
			}
			upis += "A";
			StaticFileCreate.doStuff(upis);
			
			MessageController.infoMessage("Uspeno ste dodali resurs. Kako bi ste napravili izmene pritisnite Save.");
			
			// Obrisati kasnije
			String[] tmpString = new String[tekst.length + 1];
			for (int i = 0 ; i < tekst.length ; i++) {
				tmpString[i] = tekst[i];
			}
			tmpString[tekst.length] = "A";
			StaticFileCreate.getDataTmp().add(tmpString);
			for (String[] s : StaticFileCreate.getDataTmp()) {
				for (String ss : s) 
					System.out.print(ss + " ");
				System.out.println();
			}
			System.out.println("--------------");
		}
		if(file instanceof UISERFile)
		{
			if (Frame1.getInstance().getPanelRT().getSelectedEntitet() == null)
				return;
			JTextField[] textFields = ((AddState) Frame1.getInstance().getStateManager().getCurrentState())
					.getTextFields();
			ArrayList<String> list = new ArrayList<>();
			for(int i=0;i<textFields.length;i++)
			{
				list.add(textFields[i].getText());
			}
			try
			{
				file.addRecord(list);
				
				//MessageController.infoMessage("Uspesno dodavnanje");
			}
			catch (IOException e1)
			{
				MessageController.errorMessage("Greska pri dodavanju");
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}	
}
