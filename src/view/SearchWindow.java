package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


//ovo treba u state da se prebaci posle

public class SearchWindow extends JFrame
{
	public SearchWindow()
	{
		this.setLayout(new FlowLayout());
		JLabel lbl=new JLabel("Choose search method: ");
		JButton btnBinarySearch=new JButton("Binary search");
		JButton btnLinearSearch=new JButton("Linear search");
		this.add(lbl);
		lbl.setFont(new Font("", Font.BOLD, 14));
		btnBinarySearch.setFont(new Font("", Font.BOLD, 14));
		btnBinarySearch.addActionListener(Frame1.getInstance().getActionManager().getBinarySearchAction());
		btnLinearSearch.setFont(new Font("", Font.BOLD, 14));
		btnLinearSearch.addActionListener(Frame1.getInstance().getActionManager().getLinearSearchAction());
		this.add(btnBinarySearch);
		this.add(btnLinearSearch);
		this.setSize(new Dimension(200, 200));
		this.setResizable(false);
		this.setLocationRelativeTo(Frame1.getInstance());
	}
}
