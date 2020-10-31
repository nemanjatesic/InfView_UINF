package state;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import view.Frame1;
import model.Atribut;
import model.Entitet;
import model.file.UIINDFile;
import model.file.UISEKFile;
import model.file.UISERFile;

public class SearchState extends State {

	private Entitet entity;
	private JTextField[] textFields;
	private JRadioButton buttonPocetakYes = new JRadioButton("Da");
	private JRadioButton buttonPocetakNo = new JRadioButton("Ne");
	private JRadioButton buttonJedanYes = new JRadioButton("Jedan");
	private JRadioButton buttonJedanNo = new JRadioButton("Vise");
	private JRadioButton buttonNovaYes = new JRadioButton("Da");
	private JRadioButton buttonNovaNo = new JRadioButton("Ne");
	private JRadioButton buttonLinear = new JRadioButton("Linear search");
	private JRadioButton buttonBinary = new JRadioButton("Binary search");

	public SearchState(JPanel panel) {
		super(panel);
		buttonLinear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame1.getInstance().getStateManager().setSearchState();
				Frame1.getInstance().getPanelRT().update();
				buttonLinear.setSelected(true);
			}
		});

		buttonBinary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame1.getInstance().getStateManager().setSearchState();
				Frame1.getInstance().getPanelRT().update();
				buttonBinary.setSelected(true);
			}
		});
	}

	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null)
			return;

		if (!buttonBinary.isSelected() && !buttonLinear.isSelected())
			buttonLinear.setSelected(true);

		panel.setLayout(new GridBagLayout());

		JButton buttonSearch = new JButton("Search");

		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Search : "), c);

		int row = 0;

		if (Frame1.getInstance().getPanelRT().getSelectedEntitet() instanceof UISEKFile) {
			ButtonGroup btnGroup = new ButtonGroup();

			btnGroup.add(buttonLinear);
			btnGroup.add(buttonBinary);

			panel.add(buttonLinear, getConstraints(1, 0));
			panel.add(buttonBinary, getConstraints(2, 0));

			if (buttonLinear.isSelected()) {
				textFields = new JTextField[entity.getAtributi().size()];
				for (int i = 0; i < entity.getAtributi().size(); i++) {
					if (i % 3 == 0)
						row++;
					c = getConstraints((i * 2) % 6, row);
					panel.add(new JLabel(entity.getAtributi().get(i).toString() + " : "), c);
					c = getConstraints((i * 2 + 1) % 6, row);
					JTextField textField = new JTextField();
					textFields[i] = textField;
					textField.setPreferredSize(new Dimension(200, 35));
					panel.add(textField, c);
				}

				buttonPocetakYes.setSelected(true);
				buttonJedanYes.setSelected(true);
				buttonNovaYes.setSelected(true);

				ButtonGroup group1 = new ButtonGroup();
				ButtonGroup group2 = new ButtonGroup();
				ButtonGroup group3 = new ButtonGroup();

				group1.add(buttonPocetakYes);
				group1.add(buttonPocetakNo);
				group2.add(buttonJedanYes);
				group2.add(buttonJedanNo);
				group3.add(buttonNovaYes);
				group3.add(buttonNovaNo);

				JLabel lblPocetak = new JLabel("Da li zelite da trazite od pocetka");
				JLabel lblJedan = new JLabel("Da li zelite jedan ili vise rezultata");
				JLabel lblNova = new JLabel("Da li zelite da sacuvate u novi fajl");

				row++;
				panel.add(lblPocetak, getConstraints(0, row));
				panel.add(buttonPocetakYes, getConstraints(1, row));
				panel.add(buttonPocetakNo, getConstraints(2, row));
				row++;
				panel.add(lblJedan, getConstraints(0, row));
				panel.add(buttonJedanYes, getConstraints(1, row));
				panel.add(buttonJedanNo, getConstraints(2, row));
				row++;
				panel.add(lblNova, getConstraints(0, row));
				panel.add(buttonNovaYes, getConstraints(1, row));
				panel.add(buttonNovaNo, getConstraints(2, row));
				row++;

				buttonSearch.addActionListener(Frame1.getInstance().getActionManager().getLinearSearchAction());
				panel.add(buttonSearch, getConstraints(0, row));
			} else {
				ArrayList<Atribut> primarni = new ArrayList<>();
				for (Atribut a : entity.getAtributi())
					if (a.isPrimarniKljuc())
						primarni.add(a);
				textFields = new JTextField[primarni.size()];
				for (int i = 0; i < primarni.size(); i++) {
					if (i % 3 == 0)
						row++;
					c = getConstraints((i * 2) % 6, row);
					panel.add(new JLabel(primarni.get(i).toString() + " : "), c);
					c = getConstraints((i * 2 + 1) % 6, row);
					JTextField textField = new JTextField();
					textFields[i] = textField;
					textField.setPreferredSize(new Dimension(200, 35));
					panel.add(textField, c);
				}

				buttonSearch.addActionListener(Frame1.getInstance().getActionManager().getBinarySearchAction());
				panel.add(buttonSearch, getConstraints(0, row + 1));
			}

		} else if (Frame1.getInstance().getPanelRT().getSelectedEntitet() instanceof UIINDFile) {
			Entitet entitet = Frame1.getInstance().getPanelRT().getSelectedEntitet();
			int brojPrimaryKljuceva = 0;
			ArrayList<Atribut> primarniAtributi = new ArrayList<>();
			for (Atribut a : entitet.getAtributi()) {
				if (a.isPrimarniKljuc()) {
					brojPrimaryKljuceva++;
					primarniAtributi.add(a);
				}
			}
			textFields = new JTextField[brojPrimaryKljuceva];

			for (int i = 0; i < primarniAtributi.size(); i++) {
				if (i % 3 == 0)
					row++;
				c = getConstraints((i * 2) % 6, row);
				panel.add(new JLabel(primarniAtributi.get(i).toString() + " : "), c);
				c = getConstraints((i * 2 + 1) % 6, row);
				JTextField textField = new JTextField();
				textFields[i] = textField;
				textField.setPreferredSize(new Dimension(200, 35));
				panel.add(textField, c);
			}

			buttonSearch.addActionListener(Frame1.getInstance().getActionManager().getSearchBinarnoStabloAction());
			panel.add(buttonSearch, getConstraints(0, row + 1));
		} else if (Frame1.getInstance().getPanelRT().getSelectedEntitet() instanceof UISERFile) {
			textFields = new JTextField[entity.getAtributi().size()];
			for (int i = 0; i < entity.getAtributi().size(); i++) {
				if (i % 3 == 0)
					row++;
				c = getConstraints((i * 2) % 6, row);
				panel.add(new JLabel(entity.getAtributi().get(i).toString() + " : "), c);
				c = getConstraints((i * 2 + 1) % 6, row);
				JTextField textField = new JTextField();
				textFields[i] = textField;
				textField.setPreferredSize(new Dimension(200, 35));
				panel.add(textField, c);
			}

			buttonSearch.addActionListener(Frame1.getInstance().getActionManager().getLinearSearchAction());
			panel.add(buttonSearch, getConstraints(0, row + 1));
		}
	}

	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(20, 20, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}

	public JTextField[] getTextFields() {
		return textFields;
	}

	public JRadioButton getButtonPocetakYes() {
		return buttonPocetakYes;
	}

	public JRadioButton getButtonPocetakNo() {
		return buttonPocetakNo;
	}

	public JRadioButton getButtonJedanYes() {
		return buttonJedanYes;
	}

	public JRadioButton getButtonJedanNo() {
		return buttonJedanNo;
	}

	public JRadioButton getButtonNovaYes() {
		return buttonNovaYes;
	}

	public JRadioButton getButtonNovaNo() {
		return buttonNovaNo;
	}

	public JRadioButton getButtonBinary() {
		return buttonBinary;
	}

	public JRadioButton getButtonLinear() {
		return buttonLinear;
	}

}
