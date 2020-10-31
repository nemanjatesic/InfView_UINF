package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import view.Frame1;
import model.Entitet;
import model.NodeInf;
import tabels.TableModel1;

public class MouseController implements MouseListener
{
	int clickCount=0;

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2) {
                NodeInf node = (NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent();
                if (node == null) return;
                if (node instanceof Entitet) {
                	Frame1.getInstance().getPanelRT().newTab((Entitet)node);
                }
            }
		
     }
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
