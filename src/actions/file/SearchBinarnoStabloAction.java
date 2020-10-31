package actions.file;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import model.Atribut;
import model.TipAtributa;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.file.UISEKFile;
import model.tree.Node;
import state.SearchState;

public class SearchBinarnoStabloAction extends AbstractActionI {

	private boolean pronadjen = false;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UIAbstractFile file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (file instanceof UIINDFile) {
			boolean[] isBroj;
			ArrayList<Atribut> att = Frame1.getInstance().getPanelRT().getSelectedEntitet().getAtributi();
			int broj = 0;
			for (int i = 0; i < att.size(); i++) {
				if (att.get(i).isPrimarniKljuc())
					broj++;
			}
			isBroj = new boolean[broj];
			int j = 0;
			for (int i = 0; i < att.size(); i++) {
				if (att.get(i).isPrimarniKljuc()) {
					if (att.get(i).getTip() == TipAtributa.Numeric) {
						isBroj[j] = true;
					} else {
						isBroj[j] = false;
					}
					j++;
				}
			}
			JTextField[] textFields = ((SearchState) Frame1.getInstance().getStateManager().getCurrentState())
					.getTextFields();
			Object[] kljucevi = new Object[textFields.length];
			for (int i = 0; i < textFields.length; i++) {
				if (isBroj[i]) {
					try {
						Integer br = Integer.parseInt(textFields[i].getText());
						kljucevi[i] = br;
					} catch (Exception e) {
					}
				} else
					kljucevi[i] = textFields[i].getText();

				if (textFields[i].getText().equals("")) {
					MessageController.errorMessage("Niste uneli : " + (i + 1) + ". kljuc");
					return;
				}
			}

			ArrayList<TreeNode> put = new ArrayList<>();
			Node node = ((UIINDFile)Frame1.getInstance().getPanelRT().getSelectedEntitet()).getTree().getRootElement();

			while (node != null) {
				put.add(node);
				Node levoDete = deteNa(node, 0);
				Node desnoDete = deteNa(node, 1);
				if (desnoDete == null) {
					node = levoDete;
					continue;
				}
				boolean boolLevi = true;
				boolean boolDesni = true;
				Object[] kljuceviLevi = null; // = new String[node.getData().get(0).getKeyValue().size()];
				Object[] kljuceviDesni = null; // = new String[node.getData().get(1).getKeyValue().size()];
				if (node.getData() == null || node.getData().size() == 0) {
					boolLevi = false;
					boolDesni = false;
				} else if (node.getData().size() == 1) {
					boolDesni = false;
					kljuceviLevi = new Object[node.getData().get(0).getKeyValue().size()];
				} else {
					kljuceviLevi = new Object[node.getData().get(0).getKeyValue().size()];
					kljuceviDesni = new Object[node.getData().get(1).getKeyValue().size()];
				}

				if (boolLevi) {
					for (int i = 0; i < node.getData().get(0).getKeyValue().size(); i++) {
						kljuceviLevi[i] = node.getData().get(0).getKeyValue().get(i).toString();
					}
					for (int i = 0; i < kljuceviLevi.length; i++) {
						if (isBroj[i]) {
							try {
								Integer br = Integer.parseInt(kljuceviLevi[i].toString().trim());
								kljuceviLevi[i] = br;
							} catch (Exception e) {
							}
						}
					}
				}

				if (boolDesni) {
					for (int i = 0; i < node.getData().get(1).getKeyValue().size(); i++) {
						kljuceviDesni[i] = node.getData().get(1).getKeyValue().get(i).toString();
					}
					for (int i = 0; i < kljuceviDesni.length; i++) {
						if (isBroj[i]) {
							try {
								Integer br = Integer.parseInt(kljuceviDesni[i].toString().trim());
								kljuceviDesni[i] = br;
							} catch (Exception e) {
							}
						}
					}
				}

				if (node.isLeaf()) {
					slogPronadjen(put, node);
					node = null;
				} else {
					boolean gde = desnoIliNe(kljuceviLevi, kljuceviDesni, kljucevi);
					if (gde) {
						node = desnoDete;
						if (node == null && levoDete != null)
							node = levoDete;
					} else {
						node = levoDete;
						if (node == null && desnoDete != null)
							node = desnoDete;
					}
				}
			}
			slogPronadjen(put, (Node) put.get(put.size() - 1));
		}
	}

	private boolean desnoIliNe(Object[] kljuceviLevi, Object[] kljuceviDesni, Object[] kljucevi) {
		int br = 0;
		boolean ret = false;
		for (int i = 0; i < kljucevi.length; i++) {
			if (kljucevi[i].equals(kljuceviDesni[i])) {
				br++;
			} else
				break;
		}
		if (br == kljucevi.length)
			return true;

		if (kljuceviDesni[br] instanceof String) {
			String a = kljucevi[br].toString();
			String b = kljuceviDesni[br].toString();
			if (a.compareTo(b) < 0) {
				return false;
			} else if (a.compareTo(b) > 0) {
				return true;
			}
		} else if (kljuceviDesni[br] instanceof Integer) {
			Integer a = (Integer) kljucevi[br];
			Integer b = (Integer) kljuceviDesni[br];
			if (a < b)
				return false;
			if (a > b)
				return true;
		}
		return ret;
	}

	private Node deteNa(Node node, int a) {
		return (Node) node.getChildAt(a);
	}

	private void slogPronadjen(ArrayList<TreeNode> nodes, Node node) {
		pronadjen = true;
		TreePath treePath = getPath(nodes, node);
		for (Object n : treePath.getPath()) {
			((Node) n).getParent();
		}
		JTextField[] textFields = ((SearchState) Frame1.getInstance().getStateManager().getCurrentState())
				.getTextFields();
		ArrayList<String> searchRec = new ArrayList<>();
		for (int i = 0; i < textFields.length; i++) {
			searchRec.add(textFields[i].getText().trim());
		}
		boolean bool = ((UIINDFile) Frame1.getInstance().getPanelRT().getSelectedEntitet()).findRecord(node,((UIINDFile) Frame1.getInstance().getPanelRT().getSelectedEntitet()),searchRec);
		if (!bool) {
			MessageController.errorMessage("Trazena stvar ne postoji.");
		}
		/*
		 * JTree tree = Frame1.getInstanceOfFrame1().getPanelRT().getIndexTree();
		 * tree.expandPath(treePath);
		 */

	}

	private TreePath getPath(ArrayList<TreeNode> nodes, TreeNode treeNode) {
		JTree tree = Frame1.getInstance().getPanelRT().getIndexTree();
		ArrayList<TreeNode> tmp = new ArrayList<>();
		for (int i = 1; i < nodes.size(); i++) {
			tmp = new ArrayList<>();
			tmp.add(nodes.get(i - 1));
			tree.expandPath(new TreePath(tmp.toArray()));
		}

		/*
		 * ArrayList<Object> nodes = new ArrayList<Object>(); if (treeNode != null) {
		 * nodes.add(treeNode); treeNode = treeNode.getParent(); while (treeNode !=
		 * null) { nodes.add(0, treeNode); treeNode = treeNode.getParent(); } }
		 */
		// Collections.reverse(nodes);
		return new TreePath(nodes.toArray());

	}

}
