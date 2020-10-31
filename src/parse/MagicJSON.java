package parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.ExpandVetoException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import controller.MessageController;
import view.Frame1;
import model.Atribut;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import model.Relacija;
import model.TipAtributa;
import model.TipEntiteta;
import model.db.UIDBFile;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.file.UISEKFile;
import model.file.UISERFile;
import model.Package;

public class MagicJSON {

	public static void writeToJSON(InfResurs infResurs) {
		/*
		 * String pathToFile = infResurs.getMetaSchemaLocation(); Gson gson = new
		 * GsonBuilder().setPrettyPrinting().create();
		 * 
		 * Path path = Paths.get(pathToFile); try { boolean commaFlagEntity = false;
		 * String newJsonString = ""; Files.write(path, "{\n".getBytes(),
		 * StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
		 * StandardOpenOption.WRITE); newJsonString += "{\n"; // upisivanje entiteta
		 * Files.write(path, "  \"entities\":\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "  \"entities\":\n"; Files.write(path, "  [\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "  [\n"; for (NodeInf entitet :
		 * infResurs.getChildren()) { if (commaFlagEntity) { Files.write(path,
		 * ",\n".getBytes(), StandardOpenOption.APPEND); newJsonString += ",\n"; }
		 * Files.write(path, "    {\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "    {\n"; String toWrite = "      \"name\": \"" +
		 * entitet.getName() + "\",\n      \"attributes\":\n"; Files.write(path,
		 * toWrite.getBytes(), StandardOpenOption.APPEND); newJsonString += toWrite;
		 * Files.write(path, "      [\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "      [\n"; boolean commaFlagAttribute = false; //
		 * upisivanje atributa iz trenutnog entiteta for (NodeInf atribut :
		 * entitet.getChildren()) { if (commaFlagAttribute) { Files.write(path,
		 * ",\n".getBytes(), StandardOpenOption.APPEND); newJsonString += ",\n"; }
		 * Files.write(path, "        {\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "        {\n"; toWrite = "          \"name\": \"" +
		 * atribut.getName() + "\",\n"; Files.write(path, toWrite.getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += toWrite; toWrite =
		 * "          \"type\": \"" + ((Atribut) atribut).getTip() + "\",\n";
		 * Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; toWrite = "          \"length\": \"" + ((Atribut)
		 * atribut).getDuzina() + "\",\n"; Files.write(path, toWrite.getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += toWrite; toWrite =
		 * "          \"mandatory\": \"" + ((Atribut) atribut).isObavezan() + "\",\n";
		 * Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; toWrite = "          \"default\": \"" + ((Atribut)
		 * atribut).getDefaultA() + "\",\n"; Files.write(path, toWrite.getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += toWrite; toWrite =
		 * "          \"primaryKey\": \"" + ((Atribut) atribut).isPrimarniKljuc() +
		 * "\"\n"; Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; Files.write(path, "        }".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "        }"; commaFlagAttribute
		 * = true; } Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "\n"; Files.write(path, "      ]\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "      ]\n"; Files.write(path,
		 * "    }".getBytes(), StandardOpenOption.APPEND); newJsonString += "    }";
		 * commaFlagEntity = true; } Files.write(path, "\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "\n"; Files.write(path,
		 * "  ],\n".getBytes(), StandardOpenOption.APPEND); newJsonString += "  ],\n";
		 * Files.write(path, "  \"relations\":\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "  \"relations\":\n";
		 * Files.write(path, "  [\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "  [\n"; // upisivanje relacija boolean commaFlagRelation =
		 * false; for (Relacija relacija : infResurs.getRelations()) { if
		 * (commaFlagRelation) { Files.write(path, ",\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += ",\n"; } Files.write(path,
		 * "    {\n".getBytes(), StandardOpenOption.APPEND); newJsonString += "    {\n";
		 * String toWrite = "      \"toEntity\": \"" + relacija.getToEntity().getName()
		 * + "\",\n"; Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; toWrite = "      \"toAttribute\": \"" +
		 * relacija.getToAttribute().getName() + "\",\n"; Files.write(path,
		 * toWrite.getBytes(), StandardOpenOption.APPEND); newJsonString += toWrite;
		 * toWrite = "      \"fromEntity\": \"" + relacija.getFromEntity().getName() +
		 * "\",\n"; Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; toWrite = "      \"fromAttribute\": \"" +
		 * relacija.getFromAttribute().getName() + "\",\n"; Files.write(path,
		 * toWrite.getBytes(), StandardOpenOption.APPEND); newJsonString += toWrite;
		 * toWrite = "      \"name\": \"" + relacija.getName() + "\"\n";
		 * Files.write(path, toWrite.getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += toWrite; Files.write(path, "    }".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "    }"; commaFlagRelation =
		 * true; } Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
		 * newJsonString += "\n"; Files.write(path, "  ]\n".getBytes(),
		 * StandardOpenOption.APPEND); newJsonString += "  ]\n"; Files.write(path,
		 * "}".getBytes(), StandardOpenOption.APPEND); newJsonString += "}";
		 * infResurs.setJsonString(newJsonString); } catch (IOException e) {
		 * e.printStackTrace(); } System.out.println("created");
		 */
	}

	public static boolean validateJSON(String path) {
		File schemaFile = new File("src/metasema/metametasemaKT2.json");
		File jsonFile = new File(path);

		try {
			if (ValidationUtils.isJsonValid(schemaFile, jsonFile))
				return true;
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}

	private static void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
		for (int i = startingIndex; i < rowCount; ++i) {
			tree.expandRow(i);
		}

		if (tree.getRowCount() != rowCount) {
			expandAllNodes(tree, rowCount, tree.getRowCount());
		}
	}

	public static void parse(String path, NodeInf root) {
		System.out.println("validation debug " + " cao :" + path + " :::::::" + MagicJSON.validateJSON(path));

		if (MagicJSON.validateJSON(path) == false) {
			MessageController.errorMessage("JSON file validation failed. Please choose valid JSON file");
			return;
		}

		/*
		 * if(!MagicJSON.validateJSON(path)) { MessageController.
		 * errorMessage("JSON koji ste uneli nije po pravilima metameta seme"); return;
		 * }
		 */
		File file = new File(path);
		String jsonString = "";
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine())
				jsonString += sc.nextLine() + "\n";
			sc.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Gson gson = new Gson();
		File jsonFile = Paths.get(path).toFile();
		JsonObject jsonObject;
		try {
			jsonObject = gson.fromJson(new FileReader(jsonFile), JsonObject.class);
			String location = jsonObject.get("location").getAsString();
			String nameOfInfResource = jsonObject.get("name").getAsString();
			String description = jsonObject.get("description").getAsString();
			String type = jsonObject.get("type").getAsString();
			InfResurs infResurs = new InfResurs(nameOfInfResource, description, location, path, type);
			root.addChild(infResurs);
			infResurs.setParent(root);
			JsonArray packagesArray = jsonObject.getAsJsonArray("packages");
			int cnT = 0;
			for (JsonElement pack : packagesArray) {
				System.out.println(cnT++);
				String name = ((JsonObject) pack).get("name").getAsString();
				String type1 = ((JsonObject) pack).get("type").getAsString();
				String username = "";
				String password = "";
				String connection = "";
				System.out.println(((JsonObject) pack).get("username"));
				if (!((JsonObject) pack).get("username").toString().equals("null"))
					username = ((JsonObject) pack).get("username").getAsString();
				if (!((JsonObject) pack).get("password").toString().equals("null"))
					password = ((JsonObject) pack).get("password").getAsString();
				if (!((JsonObject) pack).get("connection").toString().equals("null"))
					connection = ((JsonObject) pack).get("connection").getAsString();
				Package pac = new Package(name, type1, username, password, connection);
				JsonArray entitiesArray = ((JsonObject) pack).getAsJsonArray("entities");
				JsonArray relationsArray = ((JsonObject) pack).getAsJsonArray("relations");
				for (JsonElement entity : entitiesArray) {
					String nameE = ((JsonObject) entity).get("name").getAsString();
					String typeE = ((JsonObject) entity).get("type").getAsString();
					String urlE = ((JsonObject) entity).get("url").getAsString();
					UIAbstractFile entitet = null;
					if (!username.equals("") && !password.equals("") && !connection.equals("")) {
						entitet = new UIDBFile(nameE);
						entitet.addObserver(Frame1.getInstance().getPanelRT());
					} else {
						System.out.println("URL JE : " + urlE);
						if (urlE.contains(".ser")) {
							String split[] = urlE.split("/");
							File file1 = new File("Veliki Set Podataka" + File.separator + split[0]);
							entitet = new UISERFile(file1.getAbsolutePath(), split[1], false,nameE);
							entitet.addObserver(Frame1.getInstance().getPanelRT());
							try {
								entitet.readHeader();
								((UISERFile) entitet).makeFileCopy();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (urlE.contains(".sek")) {
							String split[] = urlE.split("/");
							File file1 = new File("Veliki Set Podataka" + File.separator + split[0]);
							entitet = new UISEKFile(file1.getAbsolutePath(), split[1], false,nameE);
							entitet.addObserver(Frame1.getInstance().getPanelRT());
							try {
								entitet.readHeader();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						if (urlE.contains(".ind")) {
							String split[] = urlE.split("/");
							File file1 = new File("Veliki Set Podataka" + File.separator + split[0]);
							entitet = new UIINDFile(file1.getAbsolutePath(), split[1], false,nameE);
							entitet.addObserver(Frame1.getInstance().getPanelRT());
							try {
								entitet.readHeader();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}

					entitet.setTipEntiteta(TipEntiteta.valueOf(typeE));
					entitet.setUrl(urlE);
					JsonArray attributesArray = ((JsonObject) entity).getAsJsonArray("attributes");
					for (JsonElement attribute : attributesArray) {
						String nameA = ((JsonObject) attribute).get("name").getAsString();
						String typeA = ((JsonObject) attribute).get("type").getAsString();
						int lengthA = ((JsonObject) attribute).get("length").getAsInt();
						boolean mandatoryA = ((JsonObject) attribute).get("mandatory").getAsBoolean();
						String groupA = ((JsonObject) attribute).get("group").getAsString();
						boolean primaryKey = ((JsonObject) attribute).get("primaryKey").getAsBoolean();
						Atribut atribut = new Atribut(nameA, TipAtributa.valueOf(typeA), lengthA, primaryKey,
								mandatoryA, groupA);
						entitet.addChild(atribut);
						entitet.dodajAtribut(atribut);
						atribut.setParent(entitet);
					}
					pac.addChild(entitet);
					entitet.setParent(pac);
					if (!username.equals("") && !password.equals("") && !connection.equals("")) {
						try {
							entitet.readHeader();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {

					}
				}

				for (JsonElement relation : relationsArray) {
					String toEntity = ((JsonObject) relation).get("toEntity").getAsString();
					String toAttributeName = ((JsonObject) relation).get("toAttribute").getAsString();
					String fromEntity = ((JsonObject) relation).get("fromEntity").getAsString();
					String nameR = ((JsonObject) relation).get("name").getAsString();
					String fromAttributeName = ((JsonObject) relation).get("fromAttribute").getAsString();
					UIAbstractFile toE1 = null;
					UIAbstractFile fromE2 = null;
					for (NodeInf entity : pac.getChildren()) {
						//System.out.println(entity.getName() + " " + toEntity);
						if (entity.getName().equals(toEntity)) {
							if (!username.equals("") && !password.equals("") && !connection.equals(""))
								toE1 = (UIDBFile) entity;
							else
								toE1 = (UIAbstractFile) entity;
						}
						// System.out.println(entity.getName() + " " + fromEntity);
						if (entity.getName().equals(fromEntity)) {
							if (!username.equals("") && !password.equals("") && !connection.equals(""))
								fromE2 = (UIDBFile) entity;
							else
								fromE2 = (UIAbstractFile) entity;
						}
						/*
						 * System.out.println("********************");
						 * System.out.println(entity.getName()); System.out.println(toEntity);
						 * System.out.println(fromEntity);
						 */
					}
					Atribut fromAttribute = null;
					Atribut toAttribute = null;
					for (NodeInf attribute : fromE2.getChildren()) {
						// System.out.println(attribute.getName() + " " +fromAttributeName);
						if (attribute.getName().equals(fromAttributeName)) {
							fromAttribute = (Atribut) attribute;
							break;
						}
					}
					// System.out.println(fromE2.getName());
					for (NodeInf attribute : toE1.getChildren()) {
						if (attribute.getName().equals(toAttributeName)) {
							toAttribute = (Atribut) attribute;
							break;
						}
					}
					System.out.println(fromE2 + " " + toE1 + " " + fromAttribute + " " + toAttribute);
					Relacija relacija = new Relacija(nameR, fromE2, toE1, fromAttribute, toAttribute);
					pac.addRelation(relacija);
					toE1.dodajRelaciju(relacija);
					fromE2.dodajRelaciju(relacija);

					// System.out.println(pac.getChildren());

				}
				if (!username.equals("") && !password.equals("") && !connection.equals("")) {
					int i = connection.length();
					for (--i; i >= 0; i--) {
						if (connection.charAt(i) == '/')
							break;
					}
					int k = i;
					for (--k; k >= 0; k--) {
						if (connection.charAt(k) == '/')
							break;
					}
					String serverName = "";
					String databaseName = "";
					for (int j = k + 1; j < i; j++)
						serverName += connection.charAt(j);
					for (int j = i + 1; j < connection.length(); j++)
						databaseName += connection.charAt(j);
					System.out.println("DEBUG " + serverName + "  :::  " + databaseName + " :: " + password);
					boolean connected = pac.openConnection(serverName, databaseName, username, password);
					/*
					 * if(connected) MessageController.infoMessage("Successfully connected");
					 */
				}
				infResurs.addChild(pac);
				pac.setParent(infResurs);
			}

			SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());
			expandAllNodes(Frame1.getInstance().getNodeTree(), 0, Frame1.getInstance().getNodeTree().getRowCount());
			SwingUtilities.updateComponentTreeUI(Frame1.getInstance().getNodeTree());

		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
