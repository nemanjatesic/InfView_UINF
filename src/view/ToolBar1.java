package view;

import javax.swing.JToolBar;
import javax.swing.SwingConstants;

// ovde samo dodajemo komponente u toolbar
public class ToolBar1 extends JToolBar
{

	public ToolBar1() // prosledjujemo wantSwitch jer imamo 2 toolbara koji se razlikuju
	{

		super(SwingConstants.HORIZONTAL);
		
		this.add(Frame1.getInstance().getActionManager().getOpenResourceAction());	
		this.add(Frame1.getInstance().getActionManager().getEditMetaschemaAction());
		
		// stavljamo da toolbar moze da se pomera
		this.setFloatable(true);

	}
	

}
