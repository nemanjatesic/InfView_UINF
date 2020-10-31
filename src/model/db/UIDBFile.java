package model.db;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import org.joda.time.chrono.AssembledChronology.Fields;

import controller.MessageController;
import model.file.UIAbstractFile;
import model.file.UIFileField;
import wrapper.Wrapper;
import model.Atribut;
import model.Entitet;
import model.Package;
import model.TipAtributa;

public class UIDBFile extends UIAbstractFile
{

	private ArrayList<PreparedStatement> arrRestoreSQL;
	
	// naziv tabele iz baze podtaka koju UIDBFile predstavlja
	protected String TABLE_NAME;

	/*
	 * U konstruktoru se inicijalizuje naziv tabele koju instanca klase predstavlja
	 */
	public UIDBFile(String tableName)
	{
		super("", tableName + ".db", false, tableName);
		this.TABLE_NAME = tableName;
	}

	/*
	 * Metoda Ä�ita naziv svih kolona iz otvorene tabele, proverava koje kolone
	 * ulaze u sastav primarnog kljuÄ�a u tabeli i na osnovu toga formira listu
	 * polja (lista fields)
	 */
	public void readHeader() throws IOException, SQLException
	{

		for (int i = 0; i < this.getAtributi().size(); i++)
		{
			super.fields.add(
					new UIFileField(this.getAtributi().get(i).getName(), this.getAtributi().get(i).getTip().toString(),
							this.getAtributi().get(i).getDuzina(), this.getAtributi().get(i).isPrimarniKljuc()));
		}

	}

	/*
	 * Metoda Ä�ita sve podatke iz tabele SELECT upit se generiÄ�ki formira na
	 * osnovu opisa polja u tabeli
	 */
	
	public boolean fetchNextBlock() throws IOException, SQLException
	{
		//readHeader();
		// broj slogova u tabeli :
		Statement stmt0 = ((Package) this.getParent()).getConn().createStatement();
		ResultSet rs0 = stmt0.executeQuery("SELECT COUNT(*) as broj FROM " + TABLE_NAME);
		if (rs0.next())
		{

			RECORD_NUM = rs0.getInt(1);
		}
		stmt0.close();
		rs0.close();

		// formiranje dela upita za SELECT sql nad tabelom
		String columnParams = null;
		for (int i = 0; i < fields.size(); i++)
		{
			if (columnParams == null)
			{
				columnParams = fields.get(i).getFieldName();
			}
			else
			{
				columnParams += "," + fields.get(i).getFieldName();
			}

		}

		Statement stmt = ((Package) this.getParent()).getConn().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT " + columnParams + " FROM " + TABLE_NAME);

		data = new String[(int) RECORD_NUM][];
		
	//	buildData(rs);
		int i = 0;
		while (rs.next())
		{
			data[i] = new String[fields.size()];
			for (int j = 0; j < fields.size(); j++)
			{
				data[i][j] = rs.getString(j + 1);
			}
			i++;
		}
		System.out.println("broj slogova:" + (TABLE_NAME));
		System.out.println("broj slogova:" + (i++));
		Wrapper wr = new Wrapper(data);
		notify(wr);
		System.out.println("broj slogova:" + (TABLE_NAME));
		System.out.println("broj slogova:" + data.length);
		return true;
	}

	/*
	 * Metoda za dodavanje slogova u tabelu. Metoda treba da bude realizovana
	 * koriÅ¡Ä‡enjem objekta PreparedStatement. Sve SQLException-e u sluÄ�aju
	 * neuspeÅ¡nog dodavanja sloga prikazivati kroz JOptionPane. Nakon uspeÅ¡nog
	 * dodavanja sloga, sadrÅ¾aj tabele treba da bude osveÅ¾en (ponovo proÄ�itan iz
	 * baze podataka) i u tabeli treba da bude selektovan dodati slog.
	 */
	public boolean addRecord(ArrayList<String> record) throws IOException, SQLException {

		String updateSQL = "INSERT INTO dbo." + this.getName() + " ( ";
		for (int i = 0; i < this.fields.size(); i++) {
			updateSQL += " " + this.fields.get(i).getFieldName();
			if (i < this.fields.size() - 1)
				updateSQL += ", ";
		}
		updateSQL += ") VALUES (";
		for (int i = 0; i < record.size(); i++) {
			updateSQL += "'";
			updateSQL += record.get(i) + "'";
			if (i < this.fields.size() - 1)
				updateSQL += ", ";
		}
		updateSQL += ")";
		System.out.println(updateSQL);
		try {
			Statement statement = ((Package) this.getParent()).getConn().createStatement();

			for (int i = 0; i < this.getRelacije().size(); i++) {
				if (this.getRelacije().get(i).getToEntity().getName().equals(this.TABLE_NAME)) {
					String whatToFind = "";
					int saveJ = 0;
					for (int j = 0; j < this.fields.size(); j++) {
						System.out.println(this.getRelacije().get(i).getToAttribute().getName());
						if (this.fields.get(j).getFieldName()
								.equals(this.getRelacije().get(i).getToAttribute().getName())) {
							whatToFind = record.get(j);
							saveJ = j;
							break;
						}
					}
					UIDBFile currFile = (UIDBFile) this.getRelacije().get(i).getFromEntity();
					String tmpSQL = "SELECT " + " COUNT(*) AS \"rowcount\" " + " FROM " + currFile.TABLE_NAME
							+ " WHERE " + this.getRelacije().get(i).getFromAttribute().getName() + " = ?";
					System.out.println(tmpSQL);
					try {
						PreparedStatement tmpPreparedStatement = ((Package) this.getParent()).getConn()
								.prepareStatement(tmpSQL);
						tmpPreparedStatement.setObject(1, whatToFind);
						ResultSet rs = tmpPreparedStatement.executeQuery();
						rs.next();
						int count = rs.getInt("rowcount");
						rs.close();
						System.out.println(count);
						if (count == 0) {
							MessageController.errorMessage("Error in field " + this.fields.get(saveJ).getFieldName()
									+ ". Entered primary key doesn't exist in table " + currFile.TABLE_NAME
									+ ", please enter valid primary key");
							return false;
						}
					} catch (SQLException e) {
						MessageController.errorMessage(e.getMessage());
						return false;
					}

				}
			}
			System.out.println("DEBUG----");
			System.out.println(updateSQL);
			statement.execute(updateSQL);
		} catch (SQLException e) {
			MessageController.errorMessage(e.getMessage());
			return false;
		}

		fetchNextBlock();
		return true;
	}

	public ResultSet average(String selected, ArrayList<String> groupByList)
	{
		String codeSQL="SELECT ";
		for(int i=0;i<groupByList.size();i++)
			codeSQL+=groupByList.get(i)+", ";
		codeSQL+=" AVG("+selected+") FROM dbo."+this.TABLE_NAME;
		if(groupByList.size()>0)
			codeSQL+=" GROUP BY ";
		for(int i=0;i<groupByList.size();i++)
		{
			codeSQL+=groupByList.get(i);
			if(i<groupByList.size()-1)
				codeSQL+=", ";
		}
		Statement statement;
		ResultSet rs= null;
		try
		{
			statement = ((Package) this.getParent()).getConn().createStatement();
			rs = statement.executeQuery(codeSQL);
		}
		catch (SQLException e)
		{
			MessageController.errorMessage(e.getMessage());
			return null;
		}
		return rs;
	}

	/*
	 * Metoda za izmenu vrednosti selektovanog sloga. Metoda treba da bude
	 * realizovana koriÅ¡Ä‡enjem objekta PreparedStatement. Sve SQLException-e u
	 * sluÄ�aju neuspeÅ¡nog dodavanja sloga prikazivati kroz JOptionPane. Nakon
	 * uspeÅ¡ne izmene sloga, sadrÅ¾aj tabele treba da bude osveÅ¾en (ponovo
	 * proÄ�itan iz baze podataka) i u tabeli treba da bude selektovan izmenjeni
	 * slog.
	 */
	//@Override
	public boolean updateRecord(ArrayList<String> record,ArrayList<String> restore, ArrayList<String> pKeys, boolean flagFetch) throws IOException, SQLException
	{
		arrRestoreSQL= new ArrayList<>();
		/*System.out.println(this.getRelacije().size() + "br relacija");
		for(int i=0;i<this.getRelacije().size();i++)
		{
			System.out.println(this.getRelacije().get(i).getToAttribute());
		}*/
		String updateSQL="UPDATE dbo."+this.getName()+" SET ";
		for(int i=0;i<this.fields.size();i++)
		{
			updateSQL+=" "+ this.fields.get(i).getFieldName()+" = ?";
			if(i<this.fields.size()-1)
				updateSQL+=",";
		}
		updateSQL+=" WHERE ";
		for (int i = 0; i < fields.size(); i++)
		{
			if (fields.get(i).isFieldPK())
			{
				if (i == 0)
				{
					updateSQL += fields.get(i).getFieldName() + " = ?";
				}
				else
				{
					updateSQL += " AND " + fields.get(i).getFieldName() + " = ?";
				}
			}
		}
		System.out.println("DBG 22");
		System.out.println(updateSQL);
		try
		{
			PreparedStatement preparedStatement=((Package)this.getParent()).getConn().prepareStatement(updateSQL);
			PreparedStatement restoreSQL=((Package)this.getParent()).getConn().prepareStatement(updateSQL);
			
			int currIdx=0;
			int currIdxPrep=1;
			for(int i=0;i<record.size();i++)
			{
				restoreSQL.setObject(currIdxPrep,restore.get(i));
				preparedStatement.setObject(currIdxPrep++, record.get(i));
			}
			for (int i = 0; i < fields.size(); i++)
			{
				if (fields.get(i).isFieldPK())
				{
					restoreSQL.setObject(currIdxPrep,record.get(i)); ///proveri ovo
					preparedStatement.setObject(currIdxPrep++, pKeys.get(currIdx++));
				}
			}
			
			for(int i=0;i<this.getRelacije().size();i++)
			{
				if(this.getRelacije().get(i).getToEntity().getName().equals(this.TABLE_NAME))
				{
					String whatToFind="";
					int saveJ=0;
					for(int j=0;j<this.fields.size();j++)
					{
						System.out.println(this.getRelacije().get(i).getToAttribute().getName());
						if(this.fields.get(j).getFieldName().equals(this.getRelacije().get(i).getToAttribute().getName()))
						{
							whatToFind=record.get(j);
							saveJ=j;
							break;
						}
					}
					UIDBFile currFile=(UIDBFile) this.getRelacije().get(i).getFromEntity();
					String tmpSQL = "SELECT " +" COUNT(*) AS \"rowcount\" "+ " FROM "
							+ currFile.TABLE_NAME + " WHERE " + this.getRelacije().get(i).getFromAttribute().getName()+ " = ?";
					System.out.println(tmpSQL);
					try
					{
						PreparedStatement tmpPreparedStatement = ((Package) this.getParent()).getConn().prepareStatement(tmpSQL);
						tmpPreparedStatement.setObject(1, whatToFind);
						ResultSet rs = tmpPreparedStatement.executeQuery();
						rs.next();
						int count = rs.getInt("rowcount");
						rs.close();
						System.out.println("deb33");
						System.out.println(count);
						if (count == 0)
						{
							MessageController.errorMessage("Error in field " + this.fields.get(saveJ).getFieldName()
									+ ". Entered primary key doesn't exist in table " + currFile.TABLE_NAME
									+ ", please enter valid primary key");
							return false;
						}
					}
					catch (SQLException e)
					{
						MessageController.errorMessage(e.getMessage());
						return false;
					}
					
				}
			}
			preparedStatement.execute();
			arrRestoreSQL.add(restoreSQL);
		}
		catch (SQLException e)
		{
			MessageController.errorMessage(e.getMessage());
			return false;
		}
		
		try
		{
			for(int i=0;i<this.getRelacije().size();i++)
			{
				UIDBFile currFile=null;
				Atribut currAtribut=null;
				String whatToFind="";
				String whatToPut="";
				int saveJ=0;
				if(this.getRelacije().get(i).getToEntity().getName().equals(this.TABLE_NAME))
				{ 
					continue;
					/*currFile=(UIDBFile) this.getRelacije().get(i).getFromEntity();
					currAtribut=this.getRelacije().get(i).getFromAttribute();
					for(int j=0;j<this.fields.size();j++)
					{
						//System.out.println(this.getRelacije().get(i).getToAttribute().getName());
						if(this.fields.get(j).getFieldName().equals(this.getRelacije().get(i).getToAttribute().getName()))
						{
							whatToPut=record.get(j);
							whatToFind=restore.get(j);
							saveJ=j;
							break;
						}
					}*/
				}
				else
				{
					currFile=(UIDBFile) this.getRelacije().get(i).getToEntity();
					currAtribut=this.getRelacije().get(i).getToAttribute();
					for(int j=0;j<this.fields.size();j++)
					{
						//System.out.println(this.getRelacije().get(i).getToAttribute().getName());
						if(this.fields.get(j).getFieldName().equals(this.getRelacije().get(i).getFromAttribute().getName()))
						{
							whatToPut=record.get(j);
							whatToFind=restore.get(j);
							saveJ=j;
							break;
						}
					}
				}
				String tmpSQL = "UPDATE dbo."+currFile.getName()+" SET " + currAtribut.getName() + " = ? WHERE " + currAtribut.getName() + " = ?";
				PreparedStatement tmpPreparedStatement =  ((Package) currFile.getParent()).getConn().prepareStatement(tmpSQL);
				PreparedStatement tmpRestoreStatement = ((Package) currFile.getParent()).getConn().prepareStatement(tmpSQL);
				/*System.out.println("DEB77");
				System.out.println(tmpSQL + " __ " + whatToPut + " __ " + whatToFind);*/
				tmpPreparedStatement.setObject(1, whatToPut);
				tmpPreparedStatement.setObject(2, whatToFind);
				tmpRestoreStatement.setObject(1, whatToFind);
				tmpRestoreStatement.setObject(2, whatToPut);
				tmpPreparedStatement.execute();
				arrRestoreSQL.add(tmpRestoreStatement);
			}
		}
		catch (SQLException e)
		{
			try
			{	
				for(int i=arrRestoreSQL.size()-1;i>=0;i--)
					System.out.println(arrRestoreSQL.get(i).toString());
				for(int i=arrRestoreSQL.size()-1;i>=0;i--)
					arrRestoreSQL.get(i).execute();
				MessageController.errorMessage(e.getMessage());
				//e.printStackTrace();
			}
			catch (SQLException e2)
			{
				e2.printStackTrace();
				//MessageController.errorMessage(e2.getMessage());
				MessageController.errorMessage("TERMINAL ERROR");
			}
			return false;
		}
		
		if(flagFetch)
			fetchNextBlock();
		return true;
	}

	
	public ResultSet count(String selected, ArrayList<String> groupByList)
	{
		String codeSQL="SELECT ";
		for(int i=0;i<groupByList.size();i++)
			codeSQL+=groupByList.get(i)+", ";
		codeSQL+=" COUNT("+selected+") FROM dbo."+this.TABLE_NAME;
		if(groupByList.size()>0)
			codeSQL+=" GROUP BY ";
		for(int i=0;i<groupByList.size();i++)
		{
			codeSQL+=groupByList.get(i);
			if(i<groupByList.size()-1)
				codeSQL+=", ";
		}
		Statement statement;
		ResultSet rs= null;
		try
		{
			statement = ((Package) this.getParent()).getConn().createStatement();
			rs = statement.executeQuery(codeSQL);
		}
		catch (SQLException e)
		{
			MessageController.errorMessage(e.getMessage());
			return null;
		}
		return rs;
	}
	
	
	public void resultSetToMatrix(ResultSet rs, ArrayList<String> groupByList) throws SQLException
	{
		if(rs==null) return;
		Vector<Vector<String>> data1 = new Vector<Vector<String>>();
		while (rs.next())
		{
			Vector<String> vector = new Vector<String>();
			for (int columnIndex = 1; columnIndex <= groupByList.size(); columnIndex++)
			{
				vector.add(rs.getObject(columnIndex).toString());
			}
			data1.add(vector);
		}
		Wrapper wr=new Wrapper(data1,groupByList);
		notify(wr);
		//this.data = new String[data1.size()][this.getFields().size()];
		/*for (int i = 0; i < data1.size(); i++)
		{
			for (int j = 0; j <groupByList.size(); j++)
				System.out.print(data1.get(i).get(j) + " ");
			System.out.println();
		}*/
		/*this.data = new String[data1.size()][this.getFields().size()];
		for (int i = 0; i < data1.size(); i++)
			for (int j = 0; j < this.getFields().size(); j++)
				data[i][j] = data1.get(i).get(j);*/
	}
	
	
	
	/*
	 * 
	 * Brisanje sloga iz datoteke, po vrednosti primarnog kljuca:
	 */
	@Override
	public boolean deleteRecord(ArrayList<String> deleteRec) throws IOException, SQLException
	{
		// TODO Auto-generated method stub
		/*
		 * //formirati DELETE iskaz na osnovu naziva tabele, na osnovu naziva PK i
		 * vrednosti iz deleteRec: String whereStmt=" WHERE ";
		 * 
		 * 
		 * int j = 0; for (int i=0;i<fields.size();i++){ //u WHERE uslov ulaze samo
		 * polja iz primarnog kljuca
		 * 
		 * if (fields.get(i).isFieldPK()){
		 * 
		 * if (j==0){
		 * whereStmt+=fields.get(i).getFieldName()+"= "+((fields.get(i).isStringType())?
		 * "'"+ deleteRec.get(i) +"'":deleteRec.get(i)); }else{
		 * whereStmt+=" AND "+fields.get(i).getFieldName()+"= "+((fields.get(i).
		 * isStringType())?"'"+ deleteRec.get(i) +"'":deleteRec.get(i)); } j++; }
		 * 
		 * } System.out.println("DELETE FROM "+TABLE_NAME+whereStmt);
		 * 
		 * Statement stmt=AppCore.getInstance().getConn().createStatement();
		 * stmt.execute("DELETE FROM "+TABLE_NAME+whereStmt); fetchNextBlock(); return
		 * true;
		 */

		// formirati DELETE iskaz na osnovu naziva tabele, na osnovu naziva PK i
		// vrednosti iz deleteRec:

		String deleteSQL = " DELETE FROM " + TABLE_NAME + " WHERE ";

		for (int i = 0; i < fields.size(); i++)
		{
			// u WHERE uslov ulaze samo polja iz primarnog kljuca

			if (fields.get(i).isFieldPK())
			{
				if (i == 0)
				{
					deleteSQL += fields.get(i).getFieldName() + "  = ? ";
				}
				else
				{
					deleteSQL += " AND " + fields.get(i).getFieldName() + "  =  ?";
				}
			}

		}
		System.out.println(deleteSQL);

		/*PreparedStatement pstmt = AppCore.getInstance().getConn().prepareStatement(deleteSQL);
		for (int i = 0; i < fields.size(); i++)
		{
			// u WHERE uslov ulaze samo polja iz primarnog kljuca

			if (fields.get(i).isFieldPK())
			{
				pstmt.setObject(i + 1, deleteRec.get(i));
			}

		}

		pstmt.execute();
		fetchNextBlock();*/
		return true;

	}

	/*@Override
	public boolean findRecord(ArrayList<String> searchRec, FindResult findResult)
	{
		// TODO Auto-generated method stub
		boolean result = false;
		for (int i = 0; i < RECORD_NUM - 1; i++)
		{

			// provera za jedan slog u matrici data[][]
			result = true;
			for (int j = 0; j < fields.size(); j++)
			{
				if (!searchRec.get(j).trim().equals(""))
				{
					if (!data[i][j].trim().equals(searchRec.get(j).trim()))
					{

						result = false;
						break;
					}
				}
			}

			// provera da li smo pronasli smo odgovarajuÄ‡i slog
			if (result)
			{
				findResult.setPosition(i);
				return true;
			}

		}
		return result;
	}*/

	//podaci u tabeli nakon upita
	public void buildData(ResultSet rs) throws SQLException {
		
		Vector<Vector<String>> data1 = new Vector<Vector<String>>();
		while (rs.next()) {
			Vector<String> vector = new Vector<String>();
			for (int columnIndex = 1; columnIndex <= this.getFields().size(); columnIndex++) {
				vector.add(rs.getObject(columnIndex).toString());
			}
			data1.add(vector);
		}
		this.data = new String[data1.size()][this.getFields().size()];
		for (int i = 0; i < data1.size(); i++)
			for (int j = 0; j < this.getFields().size(); j++)
				data[i][j] = data1.get(i).get(j);
	}
	
	/*
	 * Metoda koja postavlja WHERE upit u bazu, ResultSet sa slogovima koji
	 * odgovaraju parametrima pretrage se prikazuje u tabeli
	 */
	public boolean filterFind(String upit) throws SQLException
	{
		
		String columnParams = null;
		for (int i = 0; i < fields.size(); i++)
		{
			if (columnParams == null)
			{
				columnParams = fields.get(i).getFieldName();
			}
			else
			{
				columnParams += "," + fields.get(i).getFieldName();
			}

		}
		String filterSQL = " SELECT "+columnParams+" FROM " + TABLE_NAME +" "+ upit;
		PreparedStatement pstmt = ((Package)this.getParent()).getConn().prepareStatement(filterSQL);
		ResultSet rs = pstmt.executeQuery();
		buildData(rs);
		Wrapper wr = new Wrapper(data);
		notify(wr);
		return true;
	}
	
	public ResultSet RelationFind(Entitet e, Atribut atribut, String value) {
		ArrayList<UIFileField> fields1=new ArrayList<>();
		for (int i = 0; i < e.getAtributi().size(); i++)
		{
			fields1.add(
					new UIFileField(e.getAtributi().get(i).getName(), e.getAtributi().get(i).getTip().toString(),
							e.getAtributi().get(i).getDuzina(), e.getAtributi().get(i).isPrimarniKljuc()));
		}
		String columnParams = null;
		for (int i = 0; i < fields1.size(); i++)
		{
			if (columnParams == null)
			{
				columnParams = fields1.get(i).getFieldName();
			}
			else
			{
				columnParams += "," + fields1.get(i).getFieldName();
			}

		}

		String upit= "SELECT "+columnParams+" FROM "+ e.getName()+ 
				" WHERE " +atribut+" = ";
		if(atribut.getTip().equals(TipAtributa.valueOf("Varchar")) || atribut.getTip().equals(TipAtributa.valueOf("Char")))
			upit += "'"+value+"'";
		else upit += value;
		System.out.println(upit);
		PreparedStatement pstmt;
		try {
			pstmt = ((Package)this.getParent()).getConn().prepareStatement(upit);
			return pstmt.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
		
		
	}

	/*
	 * Sortiranje Ä‡e odraditi generisanjem SELECT - ORDER BY iskaza i izvrÅ¡iti
	 * koriÅ¡Ä‡enjem Statement objekta
	 */
	public void sortMDI(int[] kriterijum) throws IOException
	{
		String codeSQL="SELECT * FROM dbo."+this.TABLE_NAME;
		int cnt=0;
		for(int i=0;i<kriterijum.length;i++)
		{
			if(kriterijum[i]!=0)
				cnt++;
		}
		if(cnt==0) return;
		codeSQL+=" ORDER BY ";
		boolean flagComa=false;
		for(int i=0;i<kriterijum.length;i++)
		{
			if(kriterijum[i]==1)
			{
				if(flagComa)
					codeSQL+=", ";
				else
					flagComa=true;
				codeSQL+=this.getAtributi().get(i).getName()+" ASC";
			}
			if(kriterijum[i]==-1)
			{
				if(flagComa)
					codeSQL+=", ";
				else
					flagComa=true;
				codeSQL+=this.getAtributi().get(i).getName()+" DESC";
			}
		}
		System.out.println(codeSQL);
		Statement statement;
		ResultSet rs= null;
		try
		{
			statement = ((Package) this.getParent()).getConn().createStatement();
			rs = statement.executeQuery(codeSQL);
		}
		catch (SQLException e)
		{
			MessageController.errorMessage(e.getMessage());
			return;
		}
		try
		{
			data = new String[(int) RECORD_NUM][];
			int i = 0;
			while (rs.next())
			{
				data[i] = new String[fields.size()];
				for (int j = 0; j < fields.size(); j++)
				{
					data[i][j] = rs.getString(j + 1);
				}
				i++;
			}
			Wrapper wr = new Wrapper(data);
			notify(wr);
		}
		catch (SQLException e)
		{
			MessageController.errorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * Ne koristi se!!!
	 */
	public void sortMM() throws IOException
	{
		// TODO Auto-generated method stub

	}

}
