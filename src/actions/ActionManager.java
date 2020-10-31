package actions;

import actions.database.AddDBAction;
import actions.database.AverageDBAction;
import actions.database.CountDBAction;
import actions.database.FilterDBAction;
import actions.database.RefreshDBAction;
import actions.database.SortDBAction;
import actions.database.UpdateDBAction;
import actions.file.AddStateAction;
import actions.file.BinarySearchAction;
import actions.file.ChangeFAction;
import actions.file.DeleteStateAction;
import actions.file.EditStateAction;
import actions.file.FetchNextBlockAction;
import actions.file.LinearSearchAction;
import actions.file.MakeTreeAction;
import actions.file.RecycleAction;
import actions.file.SaveSEKAction;
import actions.file.SearchBinarnoStabloAction;
import actions.file.SortExternalAction;
import actions.file.UndeleteAction;
import actions.state.AddAction;
import actions.state.AverageAction;
import actions.state.CountAction;
import actions.state.DeleteAction;
import actions.state.EditAction;
import actions.state.FilterAction;
import actions.state.RelationsAction;
import actions.state.SearchAction;
import actions.state.SortAction;
import actions.state.UpdateAction;

public class ActionManager {

	private AboutAction aboutAction;
	private OpenResourceAction openResourceAction;
	private EditMetaschemaAction editMetaschemaAction;
	private ChangeFAction changeFAction;
	private FetchNextBlockAction fetchNextBlockAction;
	private SearchAction searchAction;
	private BinarySearchAction binarySearchAction;
	private AddAction addAction;
	private FilterAction filterAction;
	private RelationsAction relationsAction;
	private LinearSearchAction linearSearchAction;
	private DeleteAction deleteAction;
	private RecycleAction recycleAction;
	private UndeleteAction undeleteAction;
	private SearchBinarnoStabloAction searchBinarnoStabloAction;
	private SaveSEKAction saveAction;
	private DeleteStateAction deleteStateAction;
	private SortAction sortAction;
	private EditAction editAction;
	private AddStateAction addStateAction;
	private SortExternalAction sortExternalAction;
	private EditStateAction editStateAction;
	private MakeTreeAction makeTree;
	private RefreshDBAction refreshDBAction;
	private UpdateDBAction updateDBAction;
	private UpdateAction updateAction;
	private AddDBAction addDBAction;
	private FilterDBAction filterDBAction;
	private AverageAction averageAction;
	private CountAction countAction;
	private CountDBAction countDBAction;
	private AverageDBAction averageDBAction;
	private SortDBAction sortDBAction;

	public ActionManager() {
		initialiseActions();
	}

	private void initialiseActions() {
		makeTree = new MakeTreeAction();
		aboutAction = new AboutAction();
		openResourceAction = new OpenResourceAction();
		editMetaschemaAction = new EditMetaschemaAction();
		changeFAction = new ChangeFAction();
		fetchNextBlockAction = new FetchNextBlockAction();
		searchAction = new SearchAction();
		binarySearchAction = new BinarySearchAction();
		addAction = new AddAction();
		filterAction = new FilterAction();
		relationsAction = new RelationsAction();
		linearSearchAction = new LinearSearchAction();
		deleteAction = new DeleteAction();
		recycleAction = new RecycleAction();
		undeleteAction = new UndeleteAction();
		searchBinarnoStabloAction = new SearchBinarnoStabloAction();
		saveAction = new SaveSEKAction();
		deleteStateAction = new DeleteStateAction();
		sortAction = new SortAction();
		editAction = new EditAction();
		addStateAction = new AddStateAction();
		sortExternalAction = new SortExternalAction();
		editStateAction = new EditStateAction();
		refreshDBAction = new RefreshDBAction();
		updateDBAction = new UpdateDBAction();
		updateAction = new UpdateAction();
		addDBAction = new AddDBAction();
		filterDBAction = new FilterDBAction();
		averageAction = new AverageAction();
		countAction = new CountAction();
		countDBAction = new CountDBAction();
		averageDBAction = new AverageDBAction();
		sortDBAction = new SortDBAction();
	}

	public FilterDBAction getFilterDBAction() {
		return filterDBAction;
	}

	public void setFilterDBAction(FilterDBAction filterDBAction) {
		this.filterDBAction = filterDBAction;
	}

	public MakeTreeAction getMakeTree() {
		return makeTree;
	}

	public void setMakeTree(MakeTreeAction makeTree) {
		this.makeTree = makeTree;
	}

	public UndeleteAction getUndeleteAction() {
		return undeleteAction;
	}

	public void setUndeleteAction(UndeleteAction undeleteAction) {
		this.undeleteAction = undeleteAction;
	}

	public RecycleAction getRecycleAction() {
		return recycleAction;
	}

	public void setRecycleAction(RecycleAction recycleAction) {
		this.recycleAction = recycleAction;
	}

	public DeleteAction getDeleteAction() {
		return deleteAction;
	}

	public void setDeleteAction(DeleteAction deleteAction) {
		this.deleteAction = deleteAction;
	}

	public OpenResourceAction getOpenResourceAction() {
		return openResourceAction;
	}

	public void setOpenOpenResourceAction(OpenResourceAction openResourceAction) {
		this.openResourceAction = openResourceAction;
	}

	public EditMetaschemaAction getEditMetaschemaAction() {
		return editMetaschemaAction;
	}

	public void setEditMetaschemaAction(EditMetaschemaAction editMetaschemaAction) {
		this.editMetaschemaAction = editMetaschemaAction;
	}

	public AboutAction getAboutAction() {
		return aboutAction;
	}

	public void setAboutAction(AboutAction aboutAction) {
		this.aboutAction = aboutAction;
	}

	public ChangeFAction getChangeFAction() {
		return changeFAction;
	}

	public void setChangeFAction(ChangeFAction changeFAction) {
		this.changeFAction = changeFAction;
	}

	public FetchNextBlockAction getFetchNextBlockAction() {
		return fetchNextBlockAction;
	}

	public void setFetchNextBlockAction(FetchNextBlockAction fetchNextBlockAction) {
		this.fetchNextBlockAction = fetchNextBlockAction;
	}

	public SearchAction getSearchAction() {
		return searchAction;
	}

	public void setSearchAction(SearchAction searchAction) {
		this.searchAction = searchAction;
	}

	public BinarySearchAction getBinarySearchAction() {
		return binarySearchAction;
	}

	public void setBinarySearchAction(BinarySearchAction binarySearchAction) {
		this.binarySearchAction = binarySearchAction;
	}

	public AddAction getAddAction() {
		return addAction;
	}

	public void setAddAction(AddAction addAction) {
		this.addAction = addAction;
	}

	public RelationsAction getRelationsAction() {
		return relationsAction;
	}

	public void setRelationsAction(RelationsAction relationsAction) {
		this.relationsAction = relationsAction;
	}

	public LinearSearchAction getLinearSearchAction() {
		return linearSearchAction;
	}

	public void setLinearSearchAction(LinearSearchAction linearSearchAction) {
		this.linearSearchAction = linearSearchAction;
	}

	public SearchBinarnoStabloAction getSearchBinarnoStabloAction() {
		return searchBinarnoStabloAction;
	}

	public void setSearchBinarnoStabloAction(SearchBinarnoStabloAction searchBinarnoStabloAction) {
		this.searchBinarnoStabloAction = searchBinarnoStabloAction;
	}

	public SaveSEKAction getSaveAction() {
		return saveAction;
	}

	public void setSaveAction(SaveSEKAction saveAction) {
		this.saveAction = saveAction;
	}

	public DeleteStateAction getDeleteStateAction() {
		return deleteStateAction;
	}

	public void setDeleteStateAction(DeleteStateAction deleteStateAction) {
		this.deleteStateAction = deleteStateAction;
	}

	public SortAction getSortAction() {
		return sortAction;
	}

	public void setSortAction(SortAction sortAction) {
		this.sortAction = sortAction;
	}

	public EditAction getEditAction() {
		return editAction;
	}

	public void setEditAction(EditAction editAction) {
		this.editAction = editAction;
	}

	public AddStateAction getAddStateAction() {
		return addStateAction;
	}

	public void setAddStateAction(AddStateAction addStateAction) {
		this.addStateAction = addStateAction;
	}

	public SortExternalAction getSortExternalAction() {
		return sortExternalAction;
	}

	public void setSortExternalAction(SortExternalAction sortExternalAction) {
		this.sortExternalAction = sortExternalAction;
	}

	public EditStateAction getEditStateAction() {
		return editStateAction;
	}

	public void setEditStateAction(EditStateAction editStateAction) {
		this.editStateAction = editStateAction;
	}

	public FilterAction getFilterAction() {
		return filterAction;
	}

	public void setFilterAction(FilterAction filterAction) {
		this.filterAction = filterAction;
	}

	public RefreshDBAction getRefreshDBAction() {
		return refreshDBAction;
	}

	public void setRefreshDBAction(RefreshDBAction refreshDBAction) {
		this.refreshDBAction = refreshDBAction;
	}

	public void setOpenResourceAction(OpenResourceAction openResourceAction) {
		this.openResourceAction = openResourceAction;
	}

	public UpdateDBAction getUpdateDBAction() {
		return updateDBAction;
	}

	public void setUpdateDBAction(UpdateDBAction updateDBAction) {
		this.updateDBAction = updateDBAction;
	}

	public UpdateAction getUpdateAction() {
		return updateAction;
	}

	public void setUpdateAction(UpdateAction updateAction) {
		this.updateAction = updateAction;
	}

	public AddDBAction getAddDBAction() {
		return addDBAction;
	}

	public void setAddDBAction(AddDBAction addDBAction) {
		this.addDBAction = addDBAction;
	}

	public AverageAction getAverageAction() {
		return averageAction;
	}

	public void setAverageAction(AverageAction averageAction) {
		this.averageAction = averageAction;
	}

	public CountAction getCountAction() {
		return countAction;
	}

	public void setCountAction(CountAction countAction) {
		this.countAction = countAction;
	}

	public AverageDBAction getAverageDBAction() {
		return averageDBAction;
	}

	public void setAverageDBAction(AverageDBAction averageDBAction) {
		this.averageDBAction = averageDBAction;
	}

	public CountDBAction getCountDBAction() {
		return countDBAction;
	}

	public void setCountDBAction(CountDBAction countDBAction) {
		this.countDBAction = countDBAction;
	}

	public SortDBAction getSortDBAction() {
		return sortDBAction;
	}

	public void setSortDBAction(SortDBAction sortDBAction) {
		this.sortDBAction = sortDBAction;
	}
}