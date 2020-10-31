package view.metaSchemaEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import controller.MessageController;
import view.Frame1;
import model.InfResurs;
import model.NodeInf;
import parse.MagicJSON;

public class MetaSchemaEditFrame extends JFrame
{
	private static MetaSchemaEditFrame instance = null;
	
	private MetaSchemaEditToolBar msetb;
	
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private JPanel centrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //centralni panel, ovde se kuca kod
	private JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); //dugmici: OK, Apply, Close
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	private static JTextArea jsonText;
	
	private static String jsonString = ""; //ovo je celokupan json string koji nije napravljen da izgleda lepo
	
	private InfResurs ir;
	
	public MetaSchemaEditFrame(InfResurs ir) {
		this.setTitle("Editor");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(700, 600));
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MetaSchemaEditFrame.getCurrentInstance().dispose();
				if(!MagicJSON.validateJSON(MetaSchemaEditFrame.getCurrentInstance().getIr().getMetaSchemaLocation())) {
					MessageController.errorMessage("Nije uspesno validirano");
					MetaSchemaEditFrame.getCurrentInstance().dispose();
				}else {
					MessageController.infoMessage("Uspesno validirano");
					MetaSchemaEditFrame.getCurrentInstance().dispose();
				}
			}
		});
		bottomPanel.add(getOkButton());
		//bottomPanel.add(getCancelButton());
		
		this.ir = ir;
		
	    msetb = MetaSchemaEditToolBar.getInstance();
		this.add(msetb, BorderLayout.NORTH);
			
		mainPanel.add(centrePanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel,  BorderLayout.SOUTH);
		this.add(mainPanel);
	}

	private void initializeTextField(String infResString) 
	{
		centrePanel.removeAll();
		jsonText = new JTextArea(40,40);
		
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		jsonText.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		Font font = new Font("Courier", 10, 15);
		jsonText.setFont(font);
		
		
		
		/* Ovo treba promeni kada se nadje nacin da prosledim infresurs direktno*/
		jsonText.setText(((InfResurs)((NodeInf)Frame1.getInstance().getNodeModel().getRoot()).getChildren().get(0)).getJsonString());

		
		JScrollPane sp = new JScrollPane(jsonText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.setPreferredSize(new Dimension(2 * this.getWidth() / 3, (int) (this.getHeight() * 0.83f)));
		centrePanel.add(sp, BorderLayout.EAST);
		jsonText.setEditable(false);
		jsonText.setCaretPosition(0);
	}
	
	public static MetaSchemaEditFrame getInstance(InfResurs ir) {
		if (instance == null) {
			instance = new MetaSchemaEditFrame(ir);
		}
		instance.ir = ir;
		instance.initializeTextField(ir.getJsonString());
		return instance;
	}
	
	public static MetaSchemaEditFrame getInstance(Boolean b) {
		if (instance == null) {
			return null;
		}
		return instance;
	}
	
	public static MetaSchemaEditFrame getCurrentInstance() {
		return instance;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public InfResurs getIr() {
		return ir;
	}

	public void setJsonText(JTextArea jsonText) {
		this.jsonText = jsonText;
	}

	public static String getJsonString() {
			return jsonString;
	}

	public static void setJsonString(String jsonString) {
		MetaSchemaEditFrame.jsonString = jsonString;
	}

	public static JTextArea getJsonText() {
		return jsonText;
	}
}
