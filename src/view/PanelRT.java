package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import controller.INDListner;
import controller.TableClickListner;
import events.Observable;
import events.Observer;
import model.Entitet;
import model.NodeInf;
import model.Package;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.tree.TreeCellRendered;
import state.AddState;
import state.AverageState;
import state.CountState;
import state.DeleteState;
import state.EditState;
import state.EmptyState;
import state.FilterState;
import state.RelationState;
import state.SearchState;
import state.SortState;
import state.State;
import state.UpdateState;
import tabels.TableModel1;
import wrapper.MojPanel;
import wrapper.Wrapper;

public class PanelRT extends JPanel implements Observer, Observable {
	private JTabbedPane tabbedPane = new JTabbedPane();
	private ArrayList<Observer> listeners = new ArrayList<>();
	private HashMap<Entitet, JTextField> hashMapTextBlock = new HashMap<>();
	private JTextField txtFileSize;
	private JTextField txtRecordSize;
	private JTextField txtRecordNum;
	private JTextField txtBlockNum;
	private JTree indexTree;

	public PanelRT() {
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
		this.addObserver(Frame1.getInstance().getPanelRB());
		tabbedPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
				}

				try {
					update();
					String name = (Frame1.getInstance().getPanelRT().getTabbedPane())
							.getTitleAt(Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedIndex());
					ArrayList<NodeInf> inf = ((NodeInf) Frame1.getInstance().getNodeModel().getRoot()).getChildren();
					for (NodeInf node : inf) {
						ArrayList<NodeInf> ent = ((NodeInf) node).getChildren();
						for (NodeInf no : ent) {
							ArrayList<NodeInf> ents = no.getChildren();
							for (NodeInf entitet : ents) {
								if (entitet.getName().equals(name))
									Frame1.getInstance().getPanelRT().notify(entitet);
							}
						}
					}
				} catch (Exception ee) {
				}

			}
		});
	}

	private JPanel initFilePanel(Entitet entitet) {
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());

		JLabel lblBlockFactor = new JLabel("f(block factor): ");
		lblBlockFactor.setFont(new Font("", Font.BOLD, 14));
		JTextField txtBlockSize = new JTextField();
		txtBlockSize.setColumns(5);
		txtBlockSize.setText(String.valueOf(((UIAbstractFile)entitet).getBLOCK_FACTOR()));
		hashMapTextBlock.put(entitet, txtBlockSize);
		JButton btnChangeF = new JButton("Change f");
		btnChangeF.setFont(new Font("", Font.BOLD, 14));
		btnChangeF.addActionListener(Frame1.getInstance().getActionManager().getChangeFAction());
		JLabel lblFileSize = new JLabel("File size: ");
		lblFileSize.setFont(new Font("", Font.BOLD, 14));
		txtFileSize = new JTextField();
		txtFileSize.setColumns(7);
		txtFileSize.setText(String.valueOf(Math.ceil(((UIAbstractFile)entitet).getFILE_SIZE() / 1024.0000)) + "KB");
		txtFileSize.setEnabled(false);
		JLabel lblRecordSize = new JLabel("Record size(B): ");
		lblRecordSize.setFont(new Font("", Font.BOLD, 14));
		txtRecordSize = new JTextField();
		txtRecordSize.setColumns(7);
		txtRecordSize.setText(String.valueOf(((UIAbstractFile)entitet).getRECORD_SIZE()));
		txtRecordSize.setEnabled(false);
		JLabel lblRecordNum = new JLabel("Record num: ");
		lblRecordNum.setFont(new Font("", Font.BOLD, 14));
		txtRecordNum = new JTextField();
		txtRecordNum.setColumns(7);
		txtRecordNum.setText(String.valueOf(((UIAbstractFile)entitet).getRECORD_NUM()));
		txtRecordNum.setEnabled(false);
		JLabel lblBlockNum = new JLabel("Block num: ");
		lblBlockNum.setFont(new Font("", Font.BOLD, 14));
		txtBlockNum = new JTextField();
		txtBlockNum.setColumns(7);
		txtBlockNum.setText(String.valueOf(((UIAbstractFile)entitet).getBLOCK_NUM()));
		txtBlockNum.setEnabled(false);

		top.add(lblBlockFactor);
		top.add(txtBlockSize);
		top.add(btnChangeF);
		top.add(lblFileSize);
		top.add(txtFileSize);
		top.add(lblRecordSize);
		top.add(txtRecordSize);
		top.add(lblRecordNum);
		top.add(txtRecordNum);
		top.add(lblBlockNum);
		top.add(txtBlockNum);

		JButton btnFetch = new JButton("Fetch next block");
		JButton btnAdd = new JButton("Add");
		JButton btnEdit = new JButton("Edit");
		JButton btnDelete = new JButton("Delete");
		JButton btnSearch = new JButton("Search");
		JButton btnRelation = new JButton("Show Relations");
		JButton btnSort = new JButton("Sort");
		JButton btnSave = new JButton("Save");
		JButton btnTree = new JButton("Make Tree");
		JButton btnFilter = new JButton("Filter");

		btnFetch.setFont(new Font("", Font.BOLD, 14));
		btnAdd.setFont(new Font("", Font.BOLD, 14));
		btnEdit.setFont(new Font("", Font.BOLD, 14));
		btnDelete.setFont(new Font("", Font.BOLD, 14));
		btnSearch.setFont(new Font("", Font.BOLD, 14));
		btnRelation.setFont(new Font("", Font.BOLD, 14));
		btnSort.setFont(new Font("", Font.BOLD, 14));
		btnSave.setFont(new Font("", Font.BOLD, 14));
		btnTree.setFont(new Font("", Font.BOLD, 14));
		btnFilter.setFont(new Font("", Font.BOLD, 14));

		btnFetch.addActionListener(Frame1.getInstance().getActionManager().getFetchNextBlockAction());
		btnSearch.addActionListener(Frame1.getInstance().getActionManager().getSearchAction());
		btnAdd.addActionListener(Frame1.getInstance().getActionManager().getAddAction());
		btnRelation.addActionListener(Frame1.getInstance().getActionManager().getRelationsAction());
		btnDelete.addActionListener(Frame1.getInstance().getActionManager().getDeleteAction());
		btnSave.addActionListener(Frame1.getInstance().getActionManager().getSaveAction());
		btnSort.addActionListener(Frame1.getInstance().getActionManager().getSortAction());
		btnEdit.addActionListener(Frame1.getInstance().getActionManager().getEditAction());
		btnTree.addActionListener(Frame1.getInstance().getActionManager().getMakeTree());

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());

		bottom.add(btnFetch);
		bottom.add(btnAdd);
		bottom.add(btnEdit);
		bottom.add(btnDelete);
		bottom.add(btnSearch);
		bottom.add(btnRelation);
		bottom.add(btnSort);
		bottom.add(btnSave);
		bottom.add(btnTree);

		pan.add(top, BorderLayout.NORTH);
		pan.add(bottom, BorderLayout.CENTER);
		return pan;
	}

	private JPanel initDatabasePanel() {
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());

		JButton btnRefresh = new JButton("Refresh");
		JButton btnAddRecord = new JButton("Add record");
		JButton btnUpdateRecord = new JButton("Update record");
		JButton btnFilterFind = new JButton("Filter find");
		JButton btnSort = new JButton("Sort");
		JButton btnCount = new JButton("Count");
		JButton btnAvg = new JButton("Average");
		JButton btnRelations = new JButton("Relations");

		btnRefresh.setFont(new Font("", Font.BOLD, 14));
		btnAddRecord.setFont(new Font("", Font.BOLD, 14));
		btnUpdateRecord.setFont(new Font("", Font.BOLD, 14));
		btnFilterFind.setFont(new Font("", Font.BOLD, 14));
		btnSort.setFont(new Font("", Font.BOLD, 14));
		btnCount.setFont(new Font("", Font.BOLD, 14));
		btnAvg.setFont(new Font("", Font.BOLD, 14));
		btnRelations.setFont(new Font("", Font.BOLD, 14));

		btnFilterFind.addActionListener(Frame1.getInstance().getActionManager().getFilterAction());
		btnRelations.addActionListener(Frame1.getInstance().getActionManager().getRelationsAction());
		btnRefresh.addActionListener(Frame1.getInstance().getActionManager().getRefreshDBAction());
		btnUpdateRecord.addActionListener(Frame1.getInstance().getActionManager().getUpdateAction());
		btnAddRecord.addActionListener(Frame1.getInstance().getActionManager().getAddAction());
		btnCount.addActionListener(Frame1.getInstance().getActionManager().getCountAction());
		btnAvg.addActionListener(Frame1.getInstance().getActionManager().getAverageAction());
		btnSort.addActionListener(Frame1.getInstance().getActionManager().getSortAction());

		top.add(btnRefresh);
		top.add(btnAddRecord);
		top.add(btnUpdateRecord);
		top.add(btnFilterFind);
		top.add(btnSort);
		top.add(btnCount);
		top.add(btnAvg);
		top.add(btnRelations);
		pan.add(top, BorderLayout.NORTH);
		return pan;
	}

	public void updateTop() {
		Entitet entitet = this.getSelectedEntitet();
		txtFileSize.setText(String.valueOf(Math.ceil(((UIAbstractFile)entitet).getFILE_SIZE() / 1024.0000)) + "KB");
		txtRecordSize.setText(String.valueOf(((UIAbstractFile)entitet).getRECORD_SIZE()));
		txtRecordNum.setText(String.valueOf(((UIAbstractFile)entitet).getRECORD_NUM()));
		txtBlockNum.setText(String.valueOf(((UIAbstractFile)entitet).getBLOCK_NUM()));
	}

	public void newTab(Entitet node) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Entitet ent = ((MojPanel) tabbedPane.getComponent(i)).getEntitet();
			if (ent == node) {
				tabbedPane.setSelectedIndex(i);
				notify(node);
				update();
				return;
			}
		}
		int n = tabbedPane.getTabCount();
		TableModel1 model = new TableModel1(node);
		JTable tabela = new JTable(model);
		tabela.getSelectionModel().addListSelectionListener(new TableClickListner());
		tabela.setPreferredScrollableViewportSize(new Dimension(500, 400));
		tabela.setFillsViewportHeight(true);

		if (node.getUrl().contains(".ind")) {
			JPanel statePanel = new JPanel();
			MojPanel panel = new MojPanel(node, tabela, statePanel);
			panel.setLayout(new BorderLayout());

			DefaultTreeModel treeModel = new DefaultTreeModel(((UIINDFile)node).getTree().getRootElement());
			indexTree = new JTree(treeModel);
			TreeCellRendered rendered = new TreeCellRendered();
			indexTree.setCellRenderer(rendered);
			indexTree.addTreeSelectionListener(new INDListner());
			JScrollPane scTree = new JScrollPane(indexTree);
			if (Frame1.getInstance().getStateManager().getCurrentState() != null) {
				statePanel.add(Frame1.getInstance().getStateManager().getPanel());
			}
			JSplitPane splitPaneState = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabela),
					new JScrollPane(statePanel));
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scTree, splitPaneState);
			splitPane.setDividerLocation(350);
			splitPaneState.setDividerLocation(420);
			panel.add(splitPane, BorderLayout.CENTER);
			panel.add(initFilePanel(node), BorderLayout.NORTH);

			tabbedPane.addTab(node.getName(), panel);
			tabbedPane.setSelectedIndex(n);
			notify(node);

		} else {
			JPanel statePanel = new JPanel();
			if (Frame1.getInstance().getStateManager().getCurrentState() != null) {
				statePanel.add(Frame1.getInstance().getStateManager().getPanel());
			}
			MojPanel panel = new MojPanel(node, tabela, statePanel);
			panel.setLayout(new BorderLayout());
			JSplitPane splitPaneState = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabela),
					new JScrollPane(statePanel));
			splitPaneState.setDividerLocation(420);
			panel.add(splitPaneState, BorderLayout.CENTER);
			if (!((Package) node.getParent()).getConnection().equals(""))
				panel.add(initDatabasePanel(), BorderLayout.NORTH);
			else
				panel.add(initFilePanel(node), BorderLayout.NORTH);

			tabbedPane.addTab(node.getName(), panel);

			tabbedPane.setSelectedIndex(n);
			notify(node);
		}
		update();
	}

	public Entitet getSelectedEntitet() {
		return ((MojPanel) tabbedPane.getSelectedComponent()).getEntitet();
	}

	public void update() {
		TableModel1 model = new TableModel1(((MojPanel) tabbedPane.getSelectedComponent()).getEntitet());
		if (((MojPanel) tabbedPane.getSelectedComponent()).isCountOrAverage()) 
			((MojPanel) tabbedPane.getSelectedComponent()).getTabela().setModel(model);
		if (Frame1.getInstance().getStateManager().getCurrentState() != null) {
			String konekcija = ((Package) ((MojPanel) tabbedPane.getSelectedComponent()).getEntitet().getParent())
					.getConnection();
			if (konekcija.equals("")) {
				// Treba proveriti da li treba napraivti taj state ili ipak empty state
				if (!checkState(konekcija))
					Frame1.getInstance().getStateManager().setEmptyState();
				setujState();
				JPanel panel = ((MojPanel) tabbedPane.getSelectedComponent()).getPanel();
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				if (Frame1.getInstance().getStateManager().getCurrentState() instanceof RelationState)
					Frame1.getInstance().getStateManager().getPanel().setPreferredSize(panel.getSize());
				panel.add(Frame1.getInstance().getStateManager().getPanel());
			} else {
				// Treba proveriti da li treba napraivti taj state ili ipak empty state
				if (!checkState(konekcija))
					Frame1.getInstance().getStateManager().setEmptyState();
				setujState();
				JPanel panel = ((MojPanel) tabbedPane.getSelectedComponent()).getPanel();
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				if (Frame1.getInstance().getStateManager().getCurrentState() instanceof RelationState)
					Frame1.getInstance().getStateManager().getPanel().setPreferredSize(panel.getSize());
				panel.add(Frame1.getInstance().getStateManager().getPanel());
			}
		}
	}

	public void updateTabData(Entitet e, TableModel1 model) {
		((MojPanel) tabbedPane.getSelectedComponent()).getTabela().setModel(model);
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	private void setujState() {
		State state = Frame1.getInstance().getStateManager().getCurrentState();
		if (state instanceof AddState)
			Frame1.getInstance().getStateManager().setAddState();
		if (state instanceof DeleteState)
			Frame1.getInstance().getStateManager().setDeleteState();
		if (state instanceof EditState)
			Frame1.getInstance().getStateManager().setEditState();
		if (state instanceof RelationState)
			Frame1.getInstance().getStateManager().setShowRelationsState();
		if (state instanceof SearchState)
			Frame1.getInstance().getStateManager().setSearchState();
		if (state instanceof SortState)
			Frame1.getInstance().getStateManager().setSortState();
		if (state instanceof FilterState)
			Frame1.getInstance().getStateManager().setFilterState();
		if (state instanceof UpdateState)
			Frame1.getInstance().getStateManager().setUpdateState();
		if (state instanceof EmptyState)
			Frame1.getInstance().getStateManager().setEmptyState();
		if (state instanceof CountState)
			Frame1.getInstance().getStateManager().setCountState();
		if (state instanceof AverageState)
			Frame1.getInstance().getStateManager().setAverageState();
	}

	private boolean checkState(String konekcija) {
		State state = Frame1.getInstance().getStateManager().getCurrentState();
		if (konekcija.equals("")) {
			if (((MojPanel) tabbedPane.getSelectedComponent()).isCountOrAverage())
				return false;
			if (state instanceof UpdateState)
				return false;
			if (state instanceof FilterState)
				return false;
			if (state instanceof CountState)
				return false;
			if (state instanceof AverageState)
				return false;
			return true;
		} else {
			if (((MojPanel) tabbedPane.getSelectedComponent()).isCountOrAverage())
				return false;
			if (state instanceof UpdateState)
				return true;
			if (state instanceof FilterState)
				return true;
			if (state instanceof AddState)
				return true;
			if (state instanceof RelationState)
				return true;
			if (state instanceof CountState)
				return true;
			if (state instanceof AverageState)
				return true;
			if (state instanceof SortState)
				return true;
			return false;
		}
	}

	public JTextField getTxtBlockSize() {
		return hashMapTextBlock.get(this.getSelectedEntitet());
	}

	public JTree getIndexTree() {
		return indexTree;
	}

	@Override
	public void update(Object o) {
		if (o instanceof Wrapper) {
			Wrapper wr = (Wrapper) o;
			if (wr.getData() != null && wr.getFirstRow() != -2 && wr.getSecondRow() != -2) {
				TableModel1 tableModel1 = new TableModel1(this.getSelectedEntitet(), wr.getData());
				this.updateTabData(this.getSelectedEntitet(), tableModel1);
				((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela()
						.setRowSelectionInterval(wr.getFirstRow(), wr.getSecondRow());
			}else if (wr.getData() != null && wr.getFirstRow() == -2 && wr.getSecondRow() == -2) {
				TableModel1 tableModel1 = new TableModel1(this.getSelectedEntitet(), wr.getData());
				this.updateTabData(this.getSelectedEntitet(), tableModel1);
			}else if (wr.getVectorData() != null && wr.getGroupByList() != null) {
				TableModel1 tableModel1 = new TableModel1(wr.getVectorData(), wr.getGroupByList());
				this.updateTabData(this.getSelectedEntitet(), tableModel1);
			}
		}

	}

	@Override
	public void addObserver(Observer observer) {
		listeners.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		listeners.remove(observer);
	}

	@Override
	public void notify(Object o) {
		for (Observer ob : listeners) {
			ob.update(o);
		}
	}
}
