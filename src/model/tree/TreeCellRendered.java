package model.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class TreeCellRendered extends DefaultTreeCellRenderer {

	public TreeCellRendered() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		Node node = (Node) value;
		if(node.getChildAt(0)==null)
			node.getChildren().removeAll(node.getChildren());
		if (node.getChildCount() > 0)
			setIcon(null);

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setBackground(Color.WHITE);
		panel.setMinimumSize(new Dimension(240, 35));

		for (int i = 0; i < node.getData().size(); i++) {
			if (node.getData().get(i) != null) {
				ElementPane elementPane = new ElementPane(node.getData().get(i));
				panel.add(elementPane);
			}
		}
		if (sel) {
			panel.setBackground(Color.BLUE);
		}
		return panel;

	}

	private class ElementPane extends JPanel {
		public ElementPane(NodeElement nodeElement) {
			setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

			for (int i = 0; i < nodeElement.getKeyValue().size(); i++) {
				JLabel lblKeyvalue = new JLabel(String.valueOf(nodeElement.getKeyValue().get(i).getValue()));
				add(lblKeyvalue);
				if (i < nodeElement.getKeyValue().size() - 1) {
					add(new JLabel("|"));
				}

			}

			JLabel lblAddress = new JLabel(String.valueOf(nodeElement.getBlockAddress()));

			lblAddress.setBackground(Color.YELLOW);
			lblAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			lblAddress.setHorizontalAlignment(CENTER);
			add(lblAddress);
		}
	}

}
