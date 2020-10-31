package model;

public class InfResurs extends NodeInf
{

	private String description;
	private String location;
	private String metaSchemaLocation;
	private String jsonString = new String();

	public InfResurs(String name, String description, String location, String metaSchemaLocation, String jsonString)
	{
		super(name);
		this.description = description;
		this.location = location;
		this.metaSchemaLocation = metaSchemaLocation;
		this.jsonString = jsonString;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getMetaSchemaLocation()
	{
		return metaSchemaLocation;
	}

	public void setMetaSchemaLocation(String metaSchemaLocation)
	{
		this.metaSchemaLocation = metaSchemaLocation;
	}

	public String getJsonString()
	{
		return jsonString;
	}

	public void setJsonString(String jsonString)
	{
		this.jsonString = jsonString;
	}


}
