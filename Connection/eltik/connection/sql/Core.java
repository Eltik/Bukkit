package eltik.connection.sql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
	
	/* The MySQL variables */
	// The connection variable for connecting to the database itself.
	private Connection connection;
	// List of variables for getting the host, database, username, password, and table of the database.
	public String host, database, username, password, table;
	// The port of the database.
	public int port;
	
	// Get's the config.yml file.
	FileConfiguration config = getConfig();
	
	// Get's the data + time for logging purposes.
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// When the server starts up...
	public void onEnable() {
		// Send a log for loading the config...
		otherLog("[" + format.format(now) + "]" + "Attempting to load config...");
		// Load the config
		loadConfig();
		// Send a log for attempting to connect to the MySQl database.
		otherLog("[" + format.format(now) + "]" + "Attempting to connect to MySQL database...");
		// Set up MySQL.
		mysqlSetup();
		
		// Config variable.
		config = getConfig();
		
		// Register all events in the MySQL class.
		this.getServer().getPluginManager().registerEvents(new MysqlSetterGetter(), this);
		getLogger().info("Loaded MySQL class.");
		getLogger().info("Reloaded all player data...");
		otherLog("Loaded all player data at " + "[" + format.format(now) + "]");
		// Register events in the PlayerListeners class.
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
		getLogger().info("Registered listeners in PlayerListeners...");
		otherLog("Registered listeners in PlayerListeners...");
		// Register commands
		new Commands(this);
		getLogger().info("Successfully loaded commands.");
		otherLog("Loaded commands.");
		otherLog("Server has started at " + "[" + format.format(now) + "]");
		// For logging
		getLogger().info("Plugin 'Connection' loaded! Server Version: " + Bukkit.getServer().getVersion());
		otherLog("Server version: " + Bukkit.getServer().getVersion());
		getLogger().info("Bukkit version: " + Bukkit.getBukkitVersion());
		otherLog("Bukkit version: " + Bukkit.getBukkitVersion());
		getLogger().info("Server IP: " + Bukkit.getServer().getIp());
		otherLog("Server IP: " + Bukkit.getServer().getIp());
	}
	
	// When the server stops...
	public void onDisable() {
		// Send a message saying that it's stopping the plugin.
		getLogger().info("Stopping plugin 'Connection'...");
		// Send log info.
		otherLog("Server has stopped at " + "[" + format.format(now) + "]");
		otherLog("------------");
		getLogger().info("Plugin stopped. Goodbye!");
	}
	
	// Loading config
	public void loadConfig(){
		// Copy everything from the config.yml to the actual file in the folder.
		getConfig().options().copyDefaults(true);
		// Save the config.
		saveConfig();
		otherLog("[" + format.format(now) + "]" + "Successfully loaded config.");
	}
	
	// MySQL setup
	public void mysqlSetup() {
		// Get's the current variables and sets it to whatever is in the config.
		host = this.getConfig().getString("host");
		port = this.getConfig().getInt("port");
		database = this.getConfig().getString("database");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");
		table = this.getConfig().getString("table");

		try {

			synchronized (this) {
				// If the connection is closed or is null...
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				
				// Connection to the database.
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(
						DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
								this.username, this.password));
				// send a success message if it works.
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
				this.otherLog("MySQL successfully conected at " + "[" + format.format(now) + "]");
			}
		} /* And if there are any errors... */ catch (SQLException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.otherLog("[" + format.format(now) + "]" + "COULD NOT CONNECT TO DATABASE. Error:");
			this.otherLog(errors.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Gets the connection.
	public Connection getConnection() {
		return connection;
	}

	// Sets the connection.
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	// Logging method
	public void otherLog(String message) {
		try {
			// Get the folder of the plugin...
			File dataFolder = getDataFolder();
			// If the folder doesn't exist create it/get the directory.
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			
			// Where to save the file to/what the name of the file should be.
			File saveTo = new File(getDataFolder(), "debug_log.txt");
			if (!saveTo.exists()) { // If the file doesn't exist create it.
				saveTo.createNewFile();
				otherLog("[" + format.format(now) + "]" + "Successfully created debug_log.txt.");
			}
			// Writing the actual message.
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// If there are any errors log it.
			this.getLogger().info("Couldn't create debug_log.txt. Errors:");
			e.printStackTrace();
		}
	}
	// DON'T DELETE THE BELOW
	// It is used for not repeating the code multiple times. Same thing as runMultiple() except without the runnable.
	/*
	public void getOtherConfig() throws SQLException {
		
		File file = new File("plugins/EndranLevelingSystem/players.yml");
		FileConfiguration otherFile = YamlConfiguration.loadConfiguration(file);
		
		for (String key : otherFile.getConfigurationSection("Players").getKeys(false)) {
			System.out.println(ChatColor.YELLOW + "Getting info...");
			System.out.println("----------------------");
            System.out.println(key);
            System.out.println(otherFile.getString("Players." + key + ".name"));
            System.out.println(otherFile.getString("Players." + key + ".race"));
			System.out.println(otherFile.getString("Players." + key + ".class"));
			System.out.println(otherFile.getString("Players." + key + ".level"));
			System.out.println(otherFile.getString("Players." + key + ".exp"));
			PreparedStatement statement = this.getConnection()
					.prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
			statement.setString(1, key);
			System.out.println("Checking if inserting is required...");
			if (playerExists(key) == false) {
				System.out.println("Inserting was required. Inserting player...");
				PreparedStatement insert = this.getConnection()
						.prepareStatement("INSERT INTO " + this.table + " (race,class,name,UUID,exp,level) VALUES (?,?,?,?,?,?)");
				insert.setString(1, otherFile.getString("Players." + key + ".race"));
				insert.setString(2, otherFile.getString("Players." + key + ".class"));
				insert.setString(3, otherFile.getString("Players." + key + ".name"));
				insert.setString(4, key);
				insert.setInt(5, otherFile.getInt("Players." + key + ".exp"));
				insert.setInt(6, otherFile.getInt("Players." + key + ".level"));
				insert.executeUpdate();

				System.out.println(ChatColor.GREEN + "Player Inserted");						
			} else {
				System.out.println("Inserting was not required.");
			}
        }
		otherLog("[" + format.format(now) + "]" + "Successfully imported YML to database!");
	}
	*/
	
	// Checking if the player exists.
	public boolean playerExists(String string) {
		try {
			// Get everything from the table "this.table" where the UUID is equal to whatever "string" is.
			PreparedStatement statement = this.getConnection()
					.prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
			statement.setString(1, string.toString());

			ResultSet results = statement.executeQuery();
			// If the SQL statement is true, then send a message saying the player was found.
			if (results.next()) {
				//Bukkit.getLogger().info(ChatColor.YELLOW + "Player Found");
				return true;
			}
			// Otherwise send a message saying the player wasn't found.
			//Bukkit.getLogger().info(ChatColor.RED + "Player NOT Found");

		} catch (SQLException e) {
			// If there are any errors log it.
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.otherLog("[" + format.format(now) + "]" + "Couldn't check if a player exists or not.");
			this.otherLog(errors.toString());
		}
		return false;
	}
	
	// The big method: importing everything from the file "players.yml" to the MySQL database.
	public void runMultiple() throws SQLException {
		// Gets the file "players.yml" from the plugin EndranLevelingSystem.
		File file = new File("plugins/EndranLevelingSystem/players.yml");
		// Loads the file.
		FileConfiguration otherFile = YamlConfiguration.loadConfiguration(file);
		// Get's the config section "Players".
		for (String key : otherFile.getConfigurationSection("Players").getKeys(false)) {
			
			// Get's everything from the table this.table where the UUID is equal to the UUID in the players.yml file.
			PreparedStatement statement = this.getConnection()
					.prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
			statement.setString(1, key);
			// If the player doesn't exist...
			if (playerExists(key) == false) {
				System.out.println("Player wasn't found! Inserting player...");
				otherLog("Inserting was required for this player with the name of " + otherFile.getString("Players." + key + ".name") + ".");
				// Then insert into the table this.table all the data where the values are equal to ___.
				PreparedStatement insert = this.getConnection()
						.prepareStatement("INSERT INTO " + this.table + " (race,class,name,UUID,exp,level) VALUES (?,?,?,?,?,?)");
				// Insert all the data.
				insert.setString(1, otherFile.getString("Players." + key + ".race"));
				insert.setString(2, otherFile.getString("Players." + key + ".class"));
				insert.setString(3, otherFile.getString("Players." + key + ".name"));
				insert.setString(4, key);
				insert.setInt(5, otherFile.getInt("Players." + key + ".exp"));
				insert.setInt(6, otherFile.getInt("Players." + key + ".level"));
				insert.executeUpdate();
				System.out.println(ChatColor.GREEN + "Player Inserted.");
				otherLog("Successfully inserted the player with the name of " + otherFile.getString("Players." + key + ".name") + ".");
			} else {
				// If inserting wasn't required, update the data.
				try {
					// Update this.table and set the race, class, name, exp, and level to everything where UUID is equal to ___.
					PreparedStatement statement1 = this.getConnection()
							.prepareStatement("UPDATE " + this.table + " SET race=?, class=?, name=?, exp=?, level=? WHERE UUID=?");
					// Update all the data.
					statement1.setString(1, otherFile.getString("Players." + key + ".race"));
					statement1.setString(2, otherFile.getString("Players." + key + ".class"));
					statement1.setString(3, otherFile.getString("Players." + key + ".name"));
					statement1.setInt(4, otherFile.getInt("Players." + key + ".exp"));
					statement1.setInt(5, otherFile.getInt("Players." + key + ".level"));
					statement1.setString(6, key);
					// Execute the SQL.
					statement1.executeUpdate();
				} catch (SQLException e) {
					// If there was an error, log it.
					System.out.println("Error has occurred when updating data!");
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.otherLog("[" + format.format(now) + "]" + "Error has occurred when updating data!");
					this.otherLog(errors.toString());
				}
			}
		}
	}

}
