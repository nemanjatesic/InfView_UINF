package state;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.MessageController;
import model.Atribut;
import model.Entitet;
import model.TipAtributa;
import view.Frame1;

public class FilterState extends State {

	private Entitet entity;
	private JButton buttonFilter;
	private JComboBox<Atribut> izborAtributa = new JComboBox<>();
	private JComboBox<String> operacija = new JComboBox<String>();
	private JComboBox<String> andOr = new JComboBox<String>();
	private ArrayList<JComboBox<Atribut>> listaAtributa = new ArrayList<>();
	private ArrayList<JComboBox<String>> listaOperacija = new ArrayList<>();
	private ArrayList<JComboBox<String>> listaAndOr = new ArrayList<>();
	private ArrayList<JLabel> listaLabela = new ArrayList<>();
	private ArrayList<JTextField> textFields = new ArrayList<>();
	private ArrayList<MojaKlasa> listaMojeKlase = new ArrayList<>();
	private Integer row;

	public FilterState(JPanel panel) {
		super(panel);
	}

	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null)
			return;
		isprazniListe();
		andOr.removeAllItems();
		operacija.removeAllItems();
		izborAtributa.removeAllItems();
		listaAtributa.add(izborAtributa);
		listaOperacija.add(operacija);
		listaAndOr.add(andOr);

		andOr.addItem("");
		andOr.addItem("AND");
		andOr.addItem("OR");

		panel.setLayout(new GridBagLayout());

		buttonFilter = new JButton("Filter");

		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Filter parametri: "), c);
		c = getConstraints(1, 0);
		panel.add(new JLabel(""));

		row = 1;

		for (int i = 0; i < entity.getAtributi().size(); i++) {
			izborAtributa.addItem(entity.getAtributi().get(i));
		}
		changeOperations(izborAtributa, operacija);
		fixuj();
		if (izborAtributa.getActionListeners().length == 0) {
			izborAtributa.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (izborAtributa.getItemCount() == 0)
						return;
					changeOperations(izborAtributa, operacija);
				}
			});
		}
		if (buttonFilter.getActionListeners().length == 0) {
			buttonFilter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*for (int i = 0; i < listaAndOr.size(); i++) {
						System.out.println("I je  " + i + " , " + (String) listaAndOr.get(i).getSelectedItem());
					}*/
					try{
						System.out.println(getSQLCode());
					}catch (Exception ex) {
					}
				}
			});
		}
		if (andOr.getActionListeners().length == 0) {
			MojaKlasa mojaKlasa = new MojaKlasa(0);
			listaMojeKlase.add(mojaKlasa);
			andOr.addActionListener(mojaKlasa);
			fixuj();
		}
		fixuj();
		c = getConstraints(0, row);
		JLabel lbl = new JLabel("WHERE");
		listaLabela.add(lbl);
		panel.add(lbl, c);
		c = getConstraints(1, row);
		panel.add(izborAtributa, c);
		c = getConstraints(2, row);
		panel.add(operacija, c);
		c = getConstraints(3, row);
		JTextField textField = new JTextField();
		textFields.add(textField);
		textField.setPreferredSize(new Dimension(200, 35));
		panel.add(textField, c);
		panel.add(andOr, getConstraints(0, ++row));
		buttonFilter.addActionListener(Frame1.getInstance().getActionManager().getFilterDBAction());
		panel.add(buttonFilter, getConstraints(0, ++row));
		fixuj();
	}

	private void changeOperations(JComboBox<Atribut> izborAtributa, JComboBox<String> operacija) {
		/*operacija.removeAllItems();
		operacija.addItem("LIKE");
		operacija.addItem(">");
		operacija.addItem("=");
		operacija.addItem("<");*/
		if (((Atribut) (izborAtributa.getSelectedItem())).getTip().equals(TipAtributa.valueOf("Varchar"))
				|| ((Atribut) (izborAtributa.getSelectedItem())).getTip().equals(TipAtributa.valueOf("Char"))) {
			operacija.removeAllItems();
			operacija.addItem("LIKE");
		} else {
			operacija.removeAllItems();
			operacija.addItem(">");
			operacija.addItem("=");
			operacija.addItem("<");
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

	public ArrayList<JTextField> getTextFields() {
		return textFields;
	}

	public JComboBox<Atribut> getIzaborAtributa() {
		return izborAtributa;
	}

	public JComboBox<String> getOperacija() {
		return operacija;
	}

	class MojaKlasa implements ActionListener {
		private int broj_reda;

		public MojaKlasa(int broj_reda) {
			this.broj_reda = broj_reda;
		}
		
		public void update(int broj) {
			if (broj_reda >= broj) {
				broj_reda--;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("br reda : " + broj_reda + ", lista size : " + listaAndOr.size());
			if (listaAndOr.size() == 0 || listaAndOr.get(0).getItemCount() == 0) {
				return;
			}
			
			if (!(broj_reda+1 >= listaAndOr.size()) && ((String) listaAndOr.get(broj_reda).getSelectedItem()).equals("")) {
				listaAndOr.get(broj_reda).setSelectedIndex(listaAndOr.get(broj_reda+1).getSelectedIndex());
				ArrayList<Object> zaBrisanje = new ArrayList<>();
				zaBrisanje.add(listaAndOr.get(broj_reda+1));
				zaBrisanje.add(listaAtributa.get(broj_reda+1));
				zaBrisanje.add(listaOperacija.get(broj_reda+1));
				zaBrisanje.add(textFields.get(broj_reda+1));
				zaBrisanje.add(listaLabela.get(broj_reda+1));
				listaAndOr.remove(broj_reda+1);
				listaAtributa.remove(broj_reda+1);
				listaOperacija.remove(broj_reda+1);
				textFields.remove(broj_reda+1);
				listaLabela.remove(broj_reda+1);
				for (int i = 0 ; i < zaBrisanje.size() ; i++) {
					panel.remove((Component)zaBrisanje.get(i));
				}
				panel.revalidate();
				panel.repaint();
				obavestiSve(broj_reda+1);
				return;
			}
			
			if ((broj_reda >= listaAndOr.size()) && ((String) listaAndOr.get(broj_reda).getSelectedItem()).equals("")) {
				return;
			}
			
			if (((String) listaAndOr.get(broj_reda).getSelectedItem()).equals("AND") || ((String) listaAndOr.get(broj_reda).getSelectedItem()).equals("OR")) {
				if (broj_reda != listaAndOr.size() - 1) {
					return;
				}
				panel.remove(buttonFilter);
				JComboBox<Atribut> izborAtributa1 = new JComboBox<>();
				JComboBox<String> operacija1 = new JComboBox<String>();
				JComboBox<String> andOr1 = new JComboBox<String>();

				andOr1.addItem("");
				andOr1.addItem("AND");
				andOr1.addItem("OR");

				if (andOr1.getActionListeners().length == 0) {
					MojaKlasa mojaKlasa = new MojaKlasa(broj_reda + 1);
					listaMojeKlase.add(mojaKlasa);
					andOr1.addActionListener(mojaKlasa);
				}

				listaAtributa.add(izborAtributa1);
				listaOperacija.add(operacija1);
				listaAndOr.add(andOr1);

				for (int i = 0; i < entity.getAtributi().size(); i++) {
					izborAtributa1.addItem(entity.getAtributi().get(i));
				}
				changeOperations(izborAtributa1, operacija1);
				if (izborAtributa1.getActionListeners().length == 0) {
					izborAtributa1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (izborAtributa1.getItemCount() == 0)
								return;
							changeOperations(izborAtributa1, operacija1);
						}
					});
				}
				GridBagConstraints c;
				c = getConstraints(0, row);
				JLabel lbl = new JLabel("WHERE");
				listaLabela.add(lbl);
				panel.add(lbl, c);
				c = getConstraints(1, row);
				panel.add(izborAtributa1, c);
				c = getConstraints(2, row);
				panel.add(operacija1, c);
				c = getConstraints(3, row);
				JTextField textField = new JTextField();
				textField.setPreferredSize(new Dimension(200, 35));
				panel.add(textField, c);
				textFields.add(textField);
				panel.add(andOr1, getConstraints(0, ++row));
				panel.add(buttonFilter, getConstraints(0, ++row));

				panel.repaint();
			}
		}
	}
	
	private void obavestiSve(int broj) {
		for (MojaKlasa mojaKlasa : listaMojeKlase) {
			mojaKlasa.update(broj);
		}
	}
	
	private void fixuj() {
		if (listaAndOr.size() == 2)
			listaAndOr.remove(listaAndOr.size() - 1);
		if (listaAtributa.size() == 2)
			listaAtributa.remove(listaAtributa.size() - 1);
		if (listaOperacija.size() == 2)
			listaOperacija.remove(listaOperacija.size() - 1);
		if (textFields.size() == 2)
			textFields.remove(textFields.size() - 1);
		if (listaLabela.size() == 2)
			listaLabela.remove(listaLabela.size() - 1);
	}
	
	private void isprazniListe() {
		listaAtributa = new ArrayList<>();
		listaOperacija = new ArrayList<>();
		listaAndOr = new ArrayList<>();
		listaLabela = new ArrayList<>();
		textFields = new ArrayList<>();
		listaMojeKlase = new ArrayList<>();
		row = 0;
	}

	public ArrayList<JComboBox<String>> getListaAndOr() {
		return listaAndOr;
	}
	
	public ArrayList<JComboBox<String>> getListaOperacija() {
		return listaOperacija;
	}
	
	public ArrayList<JComboBox<Atribut>> getListaAtributa() {
		return listaAtributa;
	}
	
	public String getSQLCode() throws SQLException{
		String sqlCode = "WHERE ";
		int broj = listaAndOr.size();
		for (int i = 0 ; i < broj ; i++) {
			sqlCode += listaAtributa.get(i).getSelectedItem() + " ";
			sqlCode += listaOperacija.get(i).getSelectedItem() + " ";
			if (((Atribut) (listaAtributa.get(i).getSelectedItem())).getTip().equals(TipAtributa.valueOf("Varchar"))){
				sqlCode += "'" + textFields.get(i).getText() + "' ";
			}else if (((Atribut) (listaAtributa.get(i).getSelectedItem())).getTip().equals(TipAtributa.valueOf("Numeric"))){
				try {
					int a = Integer.parseInt(textFields.get(i).getText());
					sqlCode += a + " ";
				}catch (NumberFormatException e) {
					MessageController.errorMessage("Treba da unesete broj u " + i + "-om redu");
					throw new SQLException();
				}
			}else if (((Atribut) (listaAtributa.get(i).getSelectedItem())).getTip().equals(TipAtributa.valueOf("Datetime"))) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				try{
					sdf.parse(textFields.get(i).getText());
					sqlCode += textFields.get(i).getText() + " ";
				}catch(ParseException e){
					MessageController.errorMessage("Treba da unesete datum u " + i + "-om redu, (dd-MM-yyyy)");
					throw new SQLException();
				}
			}else if (((Atribut) (listaAtributa.get(i).getSelectedItem())).getTip().equals(TipAtributa.valueOf("Char"))) {
				ArrayList<Atribut> atributi = Frame1.getInstance().getPanelRT().getSelectedEntitet().getAtributi();
				for (Atribut a : atributi) {
					if (a.getName().equals((((Atribut) (listaAtributa.get(i).getSelectedItem())).getName()))){
						sqlCode += "'" + textFields.get(i).getText() + "' ";
						/*if (a.getDuzina() == textFields.get(i).getText().length()) {
							sqlCode += "'" + textFields.get(i).getText() + "' ";
						}else {
							MessageController.errorMessage("Treba da unesete " + a.getDuzina() + " karaktera u " + i + "-om redu");
							throw new SQLException();
						}*/
					}
				}
			}
			sqlCode += listaAndOr.get(i).getSelectedItem() + " "; 
		}
		sqlCode = sqlCode.trim();
		return sqlCode;
	}
	
}
