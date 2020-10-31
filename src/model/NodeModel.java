package model;

import javax.swing.tree.DefaultTreeModel;


public class NodeModel extends DefaultTreeModel
{

	public NodeModel() // konstruktor
	{
		super(new NodeInf("Workspace"));

	}

	/*public void addChild(Node1 node) // dodavanje cvora na root
	{
		((Node1) getRoot()).addChild(node);
	}*/

}

