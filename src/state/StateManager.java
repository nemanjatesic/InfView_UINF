package state;

import java.io.Serializable;

import javax.swing.JPanel;

import view.Frame1;

public class StateManager implements Serializable {
	private State currentState = null;

	private AddState addState;
	private EditState editState;
	private DeleteState deleteState;
	private SearchState searchState;
	private SortState sortState;
	private RelationState relationState;
	private FilterState filterState;
	private EmptyState emptyState;
	private UpdateState updateState;
	private AverageState averageState;
	private CountState countState;

	private JPanel panel;

	public StateManager(JPanel panel) {
		this.panel = panel;
		currentState = null;
		addState = new AddState(panel);
		editState = new EditState(panel);
		deleteState = new DeleteState(panel);
		searchState = new SearchState(panel);
		sortState = new SortState(panel);
		relationState = new RelationState(panel);
		filterState = new FilterState(panel);
		emptyState = new EmptyState(panel);
		updateState = new UpdateState(panel);
		averageState = new AverageState(panel);
		countState = new CountState(panel);
	}

	public void setAddState() {
		currentState = addState;
		if (panel != null) {
			panel = new JPanel();
			addState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setFilterState() {
		currentState = filterState;
		if (panel != null) {
			panel = new JPanel();
			filterState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setEditState() {
		currentState = editState;
		if (panel != null) {
			panel = new JPanel();
			editState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setDeleteState() {
		currentState = deleteState;
		if (panel != null) {
			panel = new JPanel();
			deleteState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setSearchState() {
		currentState = searchState;
		if (panel != null) {
			panel = new JPanel();
			searchState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setSortState() {
		currentState = sortState;
		if (panel != null) {
			panel = new JPanel();
			sortState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setShowRelationsState() {
		currentState = relationState;
		if (panel != null) {
			panel = new JPanel();
			relationState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public void setUpdateState() {
		currentState = updateState;
		if (panel != null) {
			panel = new JPanel();
			updateState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}
	
	public void setEmptyState() {
		currentState = emptyState;
		if (panel != null) {
			panel = new JPanel();
			emptyState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}
	
	public void setCountState() {
		currentState = countState;
		if (panel != null) {
			panel = new JPanel();
			countState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}
	
	public void setAverageState() {
		currentState = averageState;
		if (panel != null) {
			panel = new JPanel();
			averageState.panel = panel;
			Frame1.getInstance().validate();
			currentState.initState();
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public JPanel getPanel() {
		return panel;
	}
}
