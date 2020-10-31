package actions.database;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;

import actions.AbstractActionI;
import controller.MessageController;
import model.db.UIDBFile;
import model.file.UIFileField;
import state.AddState;
import view.Frame1;
import wrapper.MojPanel;
import wrapper.Wrapper;

public class AddDBAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		try {
			JTable table = ((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela();

			JTextField[] textFields = ((AddState) Frame1.getInstance().getStateManager().getCurrentState()).getTextFields();
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0; i < textFields.length; i++)
				list.add(textFields[i].getText());
			for (int i = 0; i < list.size(); i++) {
				if ((file.getFields().get(i).isFieldPK() || file.getAtributi().get(i).isObavezan()) && list.get(i).equals("")) {
					MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " cannot be empty");
					return;
				}
				if (list.get(i).length() > file.getFields().get(i).getFieldLength()) {
					MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " too long");
					return;
				}
				if (list.get(i).length() < file.getFields().get(i).getFieldLength() && file.getFields().get(i).getFieldType().equals(UIFileField.TYPE_CHAR)) {
					MessageController.errorMessage("Field " + file.getFields().get(i).getFieldName() + " too short");
					return;
				}
			}
			try {
				boolean flag = file.addRecord(list);
				if (!flag)
					return;
				for (int i = 0; i < table.getModel().getRowCount(); i++) {
					flag = true;
					for (int j = 0; j < table.getModel().getColumnCount(); j++) {
						if (!table.getModel().getValueAt(i, j).toString().equals(list.get(j))) {
							flag = false;
							break;
						}

					}
					if (flag) {
						Wrapper wr = new Wrapper(file.getData(), i, i);
						file.notify(wr);
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}