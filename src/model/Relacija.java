package model;

public class Relacija
{

	private String name;
	private Entitet fromEntity;
	private Entitet toEntity;
	private Atribut fromAttribute;
	private Atribut toAttribute;

	public Relacija(String name, Entitet fromEntity, Entitet toEntity, Atribut fromAttribute, Atribut toAttribute)
	{
		super();
		this.name = name;
		this.fromEntity = fromEntity;
		this.toEntity = toEntity;
		this.fromAttribute = fromAttribute;
		this.toAttribute = toAttribute;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Entitet getFromEntity()
	{
		return fromEntity;
	}

	public void setFromEntity(Entitet fromEntity)
	{
		this.fromEntity = fromEntity;
	}

	public Entitet getToEntity()
	{
		return toEntity;
	}

	public void setToEntity(Entitet toEntity)
	{
		this.toEntity = toEntity;
	}

	public Atribut getFromAttribute()
	{
		return fromAttribute;
	}

	public void setFromAttribute(Atribut fromAttribute)
	{
		this.fromAttribute = fromAttribute;
	}

	public Atribut getToAttribute()
	{
		return toAttribute;
	}

	public void setToAttribute(Atribut toAttribute)
	{
		this.toAttribute = toAttribute;
	}

	@Override
	public String toString()
	{
		return name;
	}

}
