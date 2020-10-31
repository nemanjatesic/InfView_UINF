package model.file;

public class UIFileField
{

	public static final String TYPE_VARCHAR = "TYPE_VARCHAR";
	public static final String TYPE_CHAR = "TYPE_CHAR";
	public static final String TYPE_INTEGER = "TYPE_INTEGER";
	public static final String TYPE_NUMERIC = "TYPE_NUMERIC";
	public static final String TYPE_DECIMAL = "TYPE_DECIMAL";
	public static final String TYPE_DATETIME = "TYPE_DATETIME";

	private String fieldName;
	private String fieldType;
	private int fieldLength;
	private boolean fieldPK;
	private boolean sort;
	private boolean asc;

	public UIFileField(String fieldName, String fieldType, int fieldLength, boolean fieldPK)
	{
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLength = fieldLength;
		this.fieldPK = fieldPK;
		this.sort=fieldPK;
		this.asc=fieldPK;
	}

	public int getFieldLength()
	{
		return fieldLength;
	}

	public void setFieldLength(int fieldLength)
	{
		this.fieldLength = fieldLength;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public boolean isFieldPK()
	{
		return fieldPK;
	}

	public void setFieldPK(boolean fieldPK)
	{
		this.fieldPK = fieldPK;
	}

	public String getFieldType()
	{
		return fieldType;
	}

	public void setFieldType(String fieldType)
	{
		this.fieldType = fieldType;
	}

	public String toString()
	{
		return fieldName;
	}
	
	public boolean isSort()
	{
		return sort;
	}

	public void setSort(boolean sort)
	{
		this.sort = sort;
	}

	public boolean isAsc()
	{
		return asc;
	}

	public void setAsc(boolean asc)
	{
		this.asc = asc;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof UIFileField)) return false;
		UIFileField f = (UIFileField)arg0;
		if (f.fieldName.equals(fieldName) && f.fieldType.equals(fieldType) && f.fieldLength == fieldLength && f.fieldPK == fieldPK) return true;
		return false;
	}
	
	
}
