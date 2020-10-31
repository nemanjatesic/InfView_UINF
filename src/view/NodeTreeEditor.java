package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;



public class NodeTreeEditor extends DefaultTreeCellEditor implements ActionListener
{

	private Object stavka = null;
	private JTextField edit = null;

	public NodeTreeEditor(JTree arg0, DefaultTreeCellRenderer arg1)
	{
		super(arg0, arg1);
	}

	public Component getTreeCellEditorComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4,
			int arg5)
	{

		stavka = arg1;

		edit = new JTextField(arg1.toString());
		edit.addActionListener(this);
		return edit;
	}

	// ako je tri puta kliknuto na cvor ova funkcija vraca true
	public boolean isCellEditable(EventObject arg0)
	{
		if (arg0 instanceof MouseEvent)
		{
			if (((MouseEvent) arg0).getClickCount() == 3)
			{
				return true;
			}
		}
		return false;
	}

	// ova funkcija menja ime selektovanog cvora
	public void actionPerformed(ActionEvent e)
	{

	}

}