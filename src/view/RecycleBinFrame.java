package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import model.Entitet;
import model.file.UIAbstractFile;
import model.file.UISERFile;

public class RecycleBinFrame extends JFrame
{

	private ArrayList<JCheckBox> cbs = new ArrayList<JCheckBox>();
	private UIAbstractFile file;
	private ArrayList<String> lineArr = new ArrayList<>();

	public RecycleBinFrame(Entitet e)
	{

		this.setMinimumSize(new Dimension(400, 300));
		this.setPreferredSize(new Dimension(400, 300));
		this.setLocationRelativeTo(Frame1.getInstance());
		file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		ArrayList<String> del = ((UISERFile) file).showDeleted();
		JPanel panel = new JPanel();

		JPanel panTop = new JPanel();
		panTop.setLayout(new GridLayout(del.size() + 1, file.getFields().size() + 1, 15, 7));
		panTop.add(new JLabel(""));
		for (int i = 0; i < e.getAtributi().size(); i++)
		{
			JLabel lbl = new JLabel(e.getAtributi().get(i).toString());
			lbl.setFont(new Font("", Font.BOLD, 14));
			panTop.add(lbl);
		}

		for (String d : del)
		{
			// JCheckBox cb=new JCheckBox(d.trim());
			JCheckBox cb = new JCheckBox();
			cbs.add(cb);
			// panel.add(cb);
			panTop.add(cb);
			int stIdx = 0;
			lineArr.add(d);
			for (int i = 0; i < file.getFields().size(); i++)
			{
				String tmp = d.substring(stIdx, stIdx + file.getFields().get(i).getFieldLength());
				stIdx += file.getFields().get(i).getFieldLength();
				JLabel lbl = new JLabel(tmp.trim());
				lbl.setFont(new Font("", Font.BOLD, 12));
				panTop.add(lbl);
				System.out.println(tmp);
			}
		}

		JButton bt = new JButton("Undelete");
		bt.setFont(new Font("", Font.BOLD, 14));
		bt.addActionListener(Frame1.getInstance().getActionManager().getUndeleteAction());
		bt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (int i = 0; i < cbs.size(); i++)
				{
					JCheckBox cb = cbs.get(i);
					if (cb.isSelected())
					{
						String line = lineArr.get(i);
						if (file instanceof UISERFile)
						{
							((UISERFile) file).undeleteRecord(Frame1.getInstance().getPanelRT().getSelectedEntitet(),
									line);
						}
					}

				}
				dispose();
			}
		});
		panel.add(new JScrollPane(panTop, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		panel.add(bt);
		this.add(panel);

	}

}
