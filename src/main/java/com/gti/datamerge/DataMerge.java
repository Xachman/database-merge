/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import java.io.File;
import java.util.List;

import com.gti.datamerge.config.Config;
import com.gti.datamerge.DatabaseConnection.Mysql;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author xach
 */
public class DataMerge {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
	
		// TODO code application logic here
		Options options	= new Options();

		Option database1Name = new Option("d1", "database-1-name", true, "First database name");
		database1Name.setRequired(true);
		Option database1Password = new Option("d1p", "database-1-password", true, "First database password");
		Option database1Host = new Option("d1h", "database-1-host", true, "First database hostname or ip of host");
		database1Host.setRequired(true);
		Option database1Username = new Option("d1u", "database-1-username", true, "First database username");

		options.addOption(database1Name);
		options.addOption(database1Password);
		options.addOption(database1Host);
		options.addOption(database1Username);

		Option database2Name = new Option("d2", "database-2-name", true, "Second database name");
		database2Name.setRequired(true);
		Option database2Password = new Option("d2p", "database-2-password", true, "Second database password");
		Option database2Host = new Option("d2h", "database-2-host", true, "Second database hostname or ip of host");
		database2Host.setRequired(true);
		Option database2Username = new Option("d2u", "database-2-username", true, "Second database username");

		options.addOption(database2Name);
		options.addOption(database2Password);
		options.addOption(database2Host);
		options.addOption(database2Username);

		Option connectionType = new Option("t", "type", true, "Type of connection ex: mysql");

		options.addOption(connectionType);

		Option actionsOption = new Option(null, "actions", false, "Print out all actions that would be taken");
		options.addOption(actionsOption);

		Option tableOption = new Option(null, "table", true, "Table to be merged");
		options.addOption(tableOption);

		Option configFileOption = new Option(null, "config", true, "Yaml config file");
		options.addOption(configFileOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;


		try {
			cmd = parser.parse(options, args);
			Config config = null;
			if(cmd.hasOption("config")) {
				config = new Config(new File(cmd.getOptionValue("config")));
			}
			if(cmd.hasOption("actions")) {
				System.out.println("[");
				int count = 0;
				List<Action> actions = getActions(cmd, config);
				int total = actions.size();
				for(Action action : actions) {
					String print = action.toJson();
					count++;
					if(count < total) {
						print += ",";
					}
					System.out.println(print);
				}
				System.out.println("]");

			}else{
				mergeData(cmd, config);
			}
		} catch (ParseException ex) {
			System.out.println(ex.getMessage());
			formatter.printHelp("database-merge", options);
			System.exit(1);
		}


	}
	
	static public void mergeData(CommandLine cmd, Config config) {
		String type = "mysql";
		DatabaseConnectionI dbc1;
		DatabaseConnectionI dbc2;
		Database db1;
		Database db2;

		if(cmd.hasOption("t")) {
			type = cmd.getOptionValue("t");
		}

		if(type.equals("mysql")) {
			dbc1 = new Mysql(getMysqlUrl(cmd.getOptionValue("d1h"), cmd.getOptionValue("d1"), cmd.getOptionValue("d1u"), cmd.getOptionValue("d1p")));
			dbc2 = new Mysql(getMysqlUrl(cmd.getOptionValue("d2h"), cmd.getOptionValue("d2"), cmd.getOptionValue("d2u"), cmd.getOptionValue("d2p")));
			if(config != null) {
				db1 = new Database(dbc1, config);
				db2 = new Database(dbc2, config);
			}else{
				db1 = new Database(dbc1);
				db2 = new Database(dbc2);
			}

			if(cmd.hasOption("table")) {
				db1.mergeTable(cmd.getOptionValue("table"), db2);
				return;
			}
			db1.merge(db2);

		}

	}

	static List<Action> getActions(CommandLine cmd, Config config) {
		String type = "mysql";
		DatabaseConnectionI dbc1;
		DatabaseConnectionI dbc2;
		Database db1;
		Database db2;

		if(cmd.hasOption("t")) {
			type = cmd.getOptionValue("t");
		}

		if(type.equals("mysql")) {
			dbc1 = new Mysql(getMysqlUrl(cmd.getOptionValue("d1h"), cmd.getOptionValue("d1"), cmd.getOptionValue("d1u"), cmd.getOptionValue("d1p")));
			dbc2 = new Mysql(getMysqlUrl(cmd.getOptionValue("d2h"), cmd.getOptionValue("d2"), cmd.getOptionValue("d2u"), cmd.getOptionValue("d2p")));
			if(config != null) {
				db1 = new Database(dbc1, config);
				db2 = new Database(dbc2, config);
			}else{
				db1 = new Database(dbc1);
				db2 = new Database(dbc2);
			}

			if (cmd.hasOption("table")) {
				return db1.mergeTableActions(cmd.getOptionValue("table"), db2);
			}
			return db1.mergeTablesActions(db2);
		}
		return null;
	}

	static String getMysqlUrl(String host, String name, String user, String password) {
		return "jdbc:mysql://" + host + "/" + name + "?user=" + user + "&password=" + password + "&verifyServerCertificate=false&useSSL=true&zeroDateTimeBehavior=convertToNull&sessionVariables=sql_mode=''";
	}
}
