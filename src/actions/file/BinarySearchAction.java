package actions.file;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JTextField;

import actions.AbstractActionI;
import view.Frame1;
import model.file.UIAbstractFile;
import model.file.UISEKFile;
import state.SearchState;

public class BinarySearchAction extends AbstractActionI
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(Frame1.getInstance().getPanelRT().getSelectedEntitet()==null)
			return;
		UIAbstractFile file =  (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if(file instanceof UISEKFile)
		{
			JTextField[] textFields = ((SearchState) Frame1.getInstance().getStateManager().getCurrentState())
					.getTextFields();
			
			String toSearch="";
			for(int i=0;i<textFields.length;i++)
			{
				if(i>0)
					toSearch+="___";
				toSearch+=textFields[i].getText();
			}
			
			String keyToSearch="";
			for(int i=0;i<textFields.length;i++)
				keyToSearch+=textFields[i].getText();
			((UISEKFile)file).binarySearchViewUpdate(Frame1.getInstance().getPanelRT().getSelectedEntitet(), toSearch);
		}
		
	}

}
