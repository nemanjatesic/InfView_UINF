package actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.Frame1;
import view.PanelRT;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import parse.MagicJSON;

public class OpenResourceAction extends AbstractActionI
{
	public OpenResourceAction()
	{
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		putValue(SMALL_ICON, loadIcon("src/images/path.png"));
		putValue(NAME, "Open Resource");
		putValue(SHORT_DESCRIPTION, "Open Resource");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		JFileChooser fileChooser= new JFileChooser();
		fileChooser.setPreferredSize(new Dimension(600, 500));
		fileChooser.setDialogTitle("Choose your metashema json file");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON", "json");
		fileChooser.setFileFilter(filter);
		int chosen=fileChooser.showOpenDialog(Frame1.getInstance());
		if(chosen!=0)
			return;
		String path = fileChooser.getSelectedFile().getAbsolutePath().toString();
		MagicJSON.parse(path, (NodeInf)Frame1.getInstance().getNodeModel().getRoot());
		SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
		//ParseJSON.writeToJSON((InfResurs)((NodeInf)Frame1.getInstanceOfFrame1().getNodeModel().getRoot()).getChildAt(0));
		
	}
}
