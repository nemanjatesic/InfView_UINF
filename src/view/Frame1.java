package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import actions.ActionManager;
import controller.MouseController;
import model.NodeInf;
import model.NodeModel;
import model.NodeTree;
import state.StateManager;

// glavni prozor koji se otvara i u kome korisnik radi
public class Frame1 extends JFrame 
{
	
	// promenljive
	private static Frame1 frame1 = null;
	private ActionManager actionManager;
	private NodeModel nodeModel;
	private NodeTree nodeTree;
	private MenuBar1 menuBar1;
	private ToolBar1 toolBar1;
	private PanelW panelW; 
	private PanelRT panelRT;
	private PanelRB panelRB;
	private PanelS panelS;
	private String user;
	private StateManager stateManager;

	private Frame1() // private konstruktor posto je singleton 
	{
		actionManager=new ActionManager();
		stateManager = new StateManager(new JPanel());
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
	}
	
	private void inicijalizujDrvo() // f-ja za inicijalizaciju stabla
	{
		nodeTree=new NodeTree();
		nodeModel=new NodeModel();
		nodeTree.setModel(nodeModel);
		nodeTree.setToggleClickCount(0);
		nodeTree.addMouseListener(new MouseController());
	}
	
	// ova funkcija poziva funkciju za inicijalizaciju stabla
	// i podesava izgled gui-a
	public void doWork()
	{
		inicijalizujDrvo();
		//((NodeInf)this.getNodeModel().getRoot()).addChild(new NodeInf("debug"));
	//	SwingUtilities.updateComponentTreeUI(this.nodeTree);
		System.out.println(Frame1.getInstance().getNodeModel().getRoot().toString());
		int screenHeight = 650;
		int screenWidth = 800;
		setSize(screenWidth, screenHeight);
		setTitle("InfView");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

	    menuBar1 = new MenuBar1();
		this.setJMenuBar(menuBar1);

		toolBar1 = new ToolBar1();
		this.add(toolBar1, BorderLayout.NORTH);

		panelW = new PanelW();
		panelW.setPreferredSize(new Dimension(screenWidth / 3, screenHeight));


		panelRB = new PanelRB();
		panelRB.setPreferredSize(new Dimension(screenWidth / 3, screenHeight / 3));
		//this.add(panelW, BorderLayout.WEST);

		panelRT = new PanelRT();
		panelRT.setPreferredSize(new Dimension(screenWidth / 3, screenHeight / 3));

		panelS = new PanelS();
		panelS.setPreferredSize(new Dimension(screenWidth, screenHeight / 10));

		/*
		JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelRT, panelRB);
		JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelW, sp1);

		sp1.setDividerSize(3);
		sp2.setDividerSize(3);
		// sp1.setEnabled(false);
		// sp2.setEnabled(false);
		this.add(sp2, BorderLayout.CENTER);
		this.add(panelS, BorderLayout.SOUTH);
		*/
		JSplitPane sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelW, panelRT);
		//JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelW, sp1);

		sp1.setDividerSize(3);
		//sp2.setDividerSize(3);
		// sp1.setEnabled(false);
		// sp2.setEnabled(false);
		this.add(sp1, BorderLayout.CENTER);
		this.add(panelS, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static Frame1 getInstance() // funkcija koja kreira objekat klase Frame1 ako on vec ne postoji
	{
		if (frame1 == null)
			frame1 = new Frame1();
		return frame1;
	}
	
	// geteri i seteri
	
	public ActionManager getActionManager()
	{
		return actionManager;
	}

	public NodeModel getNodeModel()
	{
		return nodeModel;
	}

	public NodeTree getNodeTree()
	{
		return nodeTree;
	}

	public MenuBar1 getMenuBar1()
	{
		return menuBar1;
	}

	public ToolBar1 getToolBar1()
	{
		return toolBar1;
	}

	public PanelW getPanelW()
	{
		return panelW;
	}

	public PanelRT getPanelRT()
	{
		return panelRT;
	}

	public PanelRB getPanelRB()
	{
		return panelRB;
	}

	public PanelS getPanelS()
	{
		return panelS;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}
	
	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

}
