package actions.database;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;

import actions.AbstractActionI;
import controller.MessageController;
import model.db.UIDBFile;
import model.file.UIFileField;
import state.UpdateState;
import view.Frame1;
import wrapper.*;

public class UpdateDBAction extends AbstractActionI
{

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		try
		{
			JTable table = ((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent())
					.getTabela();
			if (table.getSelectedRowCount() > 1)
			{
				MessageController.errorMessage("To many rows selected, please select exactly 1 row");
				return;
			}
			if (table.getSelectedRowCount() < 1)
			{
				MessageController.errorMessage("Please select row that you want to update");
				return;
			}

			JTextField[] textFields = ((UpdateState) Frame1.getInstance().getStateManager().getCurrentState())
					.getTextFields();
			ArrayList<String> list = new ArrayList<>();
			ArrayList<String> pKeys = new ArrayList<>();
			ArrayList<String> restore = new ArrayList<>();
			for(int j=0;j<table.getModel().getColumnCount();j++)
				restore.add(table.getModel().getValueAt(table.getSelectedRow(), j).toString());
			System.out.println(restore);
			for (int i = 0; i < textFields.length; i++)
				list.add(textFields[i].getText());
			for (int i = 0; i < list.size(); i++)
			{
				if(file.getFields().get(i).isFieldPK())
					pKeys.add((String) table.getModel().getValueAt(table.getSelectedRow(), i));
				if ((file.getFields().get(i).isFieldPK() || file.getAtributi().get(i).isObavezan()) && list.get(i).equals(""))
				{
					MessageController
							.errorMessage("Field " + file.getFields().get(i).getFieldName() + " cannot be empty");
					return;
				}
				if (list.get(i).length() > file.getFields().get(i).getFieldLength())
				{
					MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " too long");
					return;
				}
				if (list.get(i).length() < file.getFields().get(i).getFieldLength() && file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_CHAR))
				{
					MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " too short");
					return;
				}
				/*if (file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_INTEGER)
						|| file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_NUMERIC))
				{
					try
					{
						Integer.parseInt(list.get(i));
					}
					catch (NumberFormatException e)
					{
						MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " must be "
								+ file.getFields().get(i).getFieldType());
						return;
					}

				}
				if (file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_DECIMAL)
						|| file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_NUMERIC))
				{
					try
					{
						Double.parseDouble(list.get(i));
					}
					catch (NumberFormatException e)
					{
						MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " must be "
								+ file.getFields().get(i).getFieldType());
						return;
					}

				}
				if(file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_DATETIME))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try
					{
						sdf.parse(list.get(i)); 
					}
					catch(ParseException e)
					{
						MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " must be "
								+ file.getFields().get(i).getFieldType());
						return;
					}
				}*/
			}
			try
			{
				boolean success = file.updateRecord(list,restore,pKeys,true);
				if(!success) return;
				for(int i=0;i<table.getModel().getRowCount();i++)
				{
					boolean flag=true;
					for(int j=0;j<table.getModel().getColumnCount();j++)
					{
						if(!table.getModel().getValueAt(i, j).toString().equals(list.get(j)))
						{
							flag=false;
							break;
						}
							
					}
					if(flag)
					{
						Wrapper wr = new Wrapper(file.getData(),i,i);
						file.notify(wr);
					}
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
