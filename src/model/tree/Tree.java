package model.tree;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Tree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6342356072961982683L;
	private Node rootElement;

	public Tree() {
		super();
	}

	// ako par nema s kojim da se spoji bice null
	// stavljen ispis samo za gornja 3 nivoa stabla
	// ako treba vise izbbaciti lvl<3 u while-u

	public void printBFS() {
		Node root = rootElement;
		LinkedList<Node> list = new LinkedList<>();
		list.add(root);
		int lvl = 0;
		int cnt = 0;
		StringBuffer curr = new StringBuffer();
		while (!list.isEmpty() && lvl < 3) {
			Node n = list.removeFirst();
			if (n != null) {
				List<Node> children = n.getChildren();
				for (Node ch : children) {
					list.addLast(ch);
				}
				curr.append(n.getData().toString() + " ");
			} else
				curr.append("null ");
			cnt++;
			if (cnt == Math.pow(2d, lvl)) {
				cnt = 0;
				lvl++;
				System.out.println(curr.toString());
				curr = new StringBuffer();
			}
		}
	}

	public Node getRootElement() {
		return this.rootElement;
	}

	public void setRootElement(Node rootElement) {
		this.rootElement = rootElement;
	}

}
