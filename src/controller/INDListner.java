package controller;

import java.io.IOException;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import actions.file.FetchNextBlockAction;
import view.Frame1;
import view.PanelRT;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.tree.Node;
import model.tree.NodeElement;

public class INDListner implements TreeSelectionListener {

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		UIINDFile uiFile = (UIINDFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		Node node = (Node) e.getPath().getLastPathComponent();
		if (node.getChildCount() == 0) {
			NodeElement nodeElement = node.getData().get(0);
			int newFilePointer = nodeElement.getBlockAddress() * uiFile.getRECORD_SIZE();
			uiFile.setFILE_POINTER(newFilePointer);
			Frame1.getInstance().getActionManager().getFetchNextBlockAction().actionPerformed(null);
		}
	}
}
