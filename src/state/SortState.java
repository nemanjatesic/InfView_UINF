package state;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import view.Frame1;
import model.Entitet;
import model.Package;

public class SortState extends State {

	private Entitet entity;
	private JRadioButton[][] buttons;

	public SortState(JPanel panel) {
		super(panel);
	}

	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null)
			return;

		buttons = new JRadioButton[entity.getAtributi().size()][3];

		panel.setLayout(new GridBagLayout());

		JButton buttonSort = new JButton("Sort");
		if (((Package) entity.getParent()).getConnection().equals(""))
			buttonSort.addActionListener(Frame1.getInstance().getActionManager().getSortExternalAction());
		else
			buttonSort.addActionListener(Frame1.getInstance().getActionManager().getSortDBAction());

		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Sort : "), c);
		c = getConstraints(1, 0);
		panel.add(new JLabel(""));
		int i;
		for (i = 0; i < entity.getAtributi().size(); i++) {
			c = getConstraints(0, i + 1);
			panel.add(new JLabel(entity.getAtributi().get(i).toString() + " : "), c);
			c = getConstraints(1, i + 1);
			ButtonGroup group = new ButtonGroup();
			JRadioButton button1 = new JRadioButton("Asc");
			buttons[i][0] = button1;
			panel.add(button1, c);
			c = getConstraints(2, i + 1);
			JRadioButton button2 = new JRadioButton("Des");
			buttons[i][1] = button2;
			panel.add(button2, c);
			c = getConstraints(3, i + 1);
			JRadioButton button3 = new JRadioButton("Non");
			buttons[i][2] = button3;
			panel.add(button3, c);
			group.add(button1);
			group.add(button2);
			group.add(button3);
		}

		panel.add(buttonSort, getConstraints(0, i + 1));
	}

	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(20, 20, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}

	public JRadioButton[][] getButtons() {
		return buttons;
	}

	public int[] getKriterijum() {
		int[] arr = new int[buttons.length];
		for (int i = 0; i < buttons.length; i++)
			arr[i] = 0;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i][0].isSelected())
				arr[i] = 1;
			if (buttons[i][1].isSelected())
				arr[i] = -1;
			if (buttons[i][2].isSelected())
				arr[i] = 0;
		}
		return arr;
	}
}
