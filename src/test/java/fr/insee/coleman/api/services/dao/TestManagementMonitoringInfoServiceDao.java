package fr.insee.coleman.api.services.dao;

import java.sql.Connection;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.xml.sax.InputSource;

import fr.insee.coleman.api.utils.Utils;


public class TestManagementMonitoringInfoServiceDao {
	private DatabaseConnection myDbConnection;
	private Connection connection;


	@Before
	public void initDB() throws Exception {
		connection = Utils.openConnection();
	
		connection
				.prepareStatement(
						"CREATE TABLE if not exists banque (VILLE varchar PRIMARY KEY, dollars integer, distance real)")
				.execute();
		connection
		.prepareStatement(
				"CREATE TABLE if not exists bandit (NOM varchar PRIMARY KEY, BANQUE varchar, MAGOT integer, CONSTRAINT FK_BANQUE FOREIGN KEY (BANQUE) REFERENCES BANQUE(VILLE))")
		.execute();
		myDbConnection = new DatabaseConnection(Utils.openConnection());
		FlatXmlProducer producer = new FlatXmlProducer(new InputSource("src/test/resources/data.xml"));
		IDataSet mySetUpDataset = new FlatXmlDataSet(producer);
		DatabaseOperation.CLEAN_INSERT.execute(myDbConnection, mySetUpDataset);
	}

	@After
	public void closeDB() throws Exception {
		myDbConnection.close();
		connection.close();
	}	
	

}
