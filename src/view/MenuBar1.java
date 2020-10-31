package view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;


//ovde samo dodajemo komponente u menubar
public class MenuBar1 extends JMenuBar
{
	public MenuBar1()
	{
		JMenu jmFile = new JMenu("File");
		JMenu jmEdit = new JMenu("Edit");
		JMenu jmPomoc = new JMenu("Help");
		JMenu jmAbout = new JMenu("About");

	
		jmFile.add(Frame1.getInstance().getActionManager().getOpenResourceAction());	
		jmEdit.add(Frame1.getInstance().getActionManager().getEditMetaschemaAction());
		jmAbout.add(Frame1.getInstance().getActionManager().getAboutAction());

		this.add(jmFile);
		this.add(jmEdit);
		this.add(jmPomoc);
		this.add(jmAbout);
	}

}