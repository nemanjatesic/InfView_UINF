package model;

public class Atribut extends NodeInf
{
	private TipAtributa tip;
	private int duzina;
	private boolean primarniKljuc;
	private boolean obavezan;
	private String group;

	public Atribut(String ime, TipAtributa tip, int duzina, boolean primarniKljuc, boolean obavezan, String defaultA)
	{
		super(ime);
		this.tip = tip;
		this.duzina = duzina;
		this.primarniKljuc = primarniKljuc;
		this.obavezan = obavezan;
		this.group = defaultA;
	}

	public TipAtributa getTip()
	{
		return tip;
	}

	public void setTip(TipAtributa tip)
	{
		this.tip = tip;
	}

	public int getDuzina()
	{
		return duzina;
	}

	public void setDuzina(int duzina)
	{
		this.duzina = duzina;
	}

	public boolean isPrimarniKljuc()
	{
		return primarniKljuc;
	}

	public void setPrimarniKljuc(boolean primarniKljuc)
	{
		this.primarniKljuc = primarniKljuc;
	}

	public boolean isObavezan()
	{
		return obavezan;
	}

	public void setObavezan(boolean obavezan)
	{
		this.obavezan = obavezan;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String defaultA)
	{
		this.group = defaultA;
	}
	
	@Override
	public String toString() 
	{
		return this.getName();
	}
}
