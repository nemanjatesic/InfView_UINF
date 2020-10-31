package model;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class NodeInf implements TreeNode {

	private String name;
	private ArrayList<NodeInf> children;
	private NodeInf parent;

	public NodeInf(String name) {
		this.name = name;
		this.children = new ArrayList<>();
		this.parent = null;
	}

	public NodeInf() {
		this.children = new ArrayList<>();
		this.parent = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addChild(NodeInf node) {
		this.children.add(node);
	}

	public ArrayList<NodeInf> getChildren() {
		return children;
	}

	@Override
	public Enumeration children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int arg0) {
		return children.get(arg0);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public int getIndex(TreeNode arg0) {
		for (int i = 0 ; i < children.size() ; i++) {
			if (children.get(i) == arg0)
				return i;
		}
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	public void setParent(NodeInf parent) {
		this.parent = parent;
	}

	@Override
	public boolean isLeaf() {
		if (children.size() == 0)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
