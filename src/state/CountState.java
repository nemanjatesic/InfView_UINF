package state;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.Atribut;
import model.Entitet;
import view.Frame1;

public class CountState extends State {

	private Entitet entity;
	private ArrayList<JRadioButton> buttons;
	private JComboBox<Atribut> atributi;

	public CountState(JPanel panel) {
		super(panel);
	}

	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null)
			return;

		buttons = new ArrayList<>();
		panel.setLayout(new GridBagLayout());

		JButton buttonCount = new JButton("Count");
		buttonCount.addActionListener(Frame1.getInstance().getActionManager().getCountDBAction());
		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Count : "), c);
		c = getConstraints(1, 0);
		panel.add(new JLabel(""));

		atributi = new JComboBox<>();

		panel.add(new JLabel("Count: "), getConstraints(0, 1));
		panel.add(atributi, getConstraints(1, 1));
		panel.add(new JLabel("Group by: "), getConstraints(0, 2));
		
		for (int i = 0; i < entity.getChildCount(); i++) {
			atributi.addItem((Atribut) entity.getChildAt(i));
			JRadioButton button = new JRadioButton(((Atribut) entity.getChildAt(i)).getName());
			buttons.add(button);
			panel.add(button, getConstraints(i+1, 2));
		}

		panel.add(buttonCount, getConstraints(0, 3));
	}

	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(20, 20, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}
	
	public JComboBox<Atribut> getAtributi() {
		return atributi;
	}
	
	public ArrayList<JRadioButton> getButtons() {
		return buttons;
	}
	
	public Atribut getSelectedAtribut() {
		return (Atribut)atributi.getSelectedItem();
	}
	
	public ArrayList<Boolean> getSelectedButtonsList(){
		ArrayList<Boolean> lista = new ArrayList<>();
		for (int i = 0 ; i < buttons.size() ; i++) {
			lista.add(buttons.get(i).isSelected());
		}
		return lista;
	}
}