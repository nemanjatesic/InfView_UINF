package actions;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import view.metaSchemaEditor.*;
import model.InfResurs;
import state.SortState;

public class AboutAction extends AbstractActionI
{

	// u konstruktoru postavljamo accelerator
	// ucitavamo slicicu pozivajuci funkciju(kojoj prosledjujemo String sa putanjom
	// do fajla sa slicicom) iz klase AbstractActionI
	// setujemo ime i opis ikonice
	public AboutAction()
	{

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, loadIcon("src/images/info.png"));
		putValue(NAME, "About");
		putValue(SHORT_DESCRIPTION, "About");
	}

	// ova funkcija otvara prozor u kome se nalaze podaci o autoru InstaFram-a
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		/*try {
			File ff = new File("C:\\Users\\Nemanja\\Desktop\\tmp.txt");
			File ffff = new File("C:\\eclipse-workspace\\InfView_UINF_201_2\\Veliki Set Podataka\\Sekvencijalne Datoteke\\NastavniPredmeti.stxt");
			FileWriter fr = new FileWriter(ff);
			Scanner scc = new Scanner(ffff);
			while(scc.hasNextLine()) {
				String s = scc.nextLine();
				fr.write(s + "\n");
				System.out.println(s);
			}
			scc.close();
			fr.close();
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		
		JFrame frame = new JFrame("Group info"); // postavljamo naslov prozora
		frame.setSize(730, 430); // postavljamo velicinu prozora
		frame.setLocationRelativeTo(null); // stavljamo da se prozor pojavi na centru ekrana
		frame.setLayout(new BorderLayout()); // postavljamo Layout
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // podesavanje za zatvaranje prozora

		ImageIcon imgIcon = new ImageIcon("src/images/grupna_slika.png"); // ucitavamo slicicu
		frame.add(new JLabel(imgIcon), BorderLayout.CENTER); // postavljamo slicicu na centar

		frame.setVisible(true);// prikazujemo prozor
		
		/*SortState sort = new SortState(new JPanel());
		sort.initState();*/
	}

}
