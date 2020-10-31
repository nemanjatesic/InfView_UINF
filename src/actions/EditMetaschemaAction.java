package actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import view.Frame1;
import view.metaSchemaEditor.MetaSchemaEditFrame;
import model.InfResurs;

public class EditMetaschemaAction extends AbstractActionI
{
	public EditMetaschemaAction()
	{

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		putValue(SMALL_ICON, loadIcon("src/images/images.jpg"));
		putValue(NAME, "Edit Metaschema");
		putValue(SHORT_DESCRIPTION, "Edit Metaschema");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		//debugovanje
		Object ir = Frame1.getInstance().getNodeTree().getLastSelectedPathComponent();
		if (ir instanceof InfResurs) {
			InfResurs infResource = (InfResurs)ir;
			MetaSchemaEditFrame msef = MetaSchemaEditFrame.getInstance(infResource);
			msef.setVisible(true);
		}
	}
}