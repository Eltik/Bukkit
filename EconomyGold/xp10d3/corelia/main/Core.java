package xp10d3.corelia.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin implements Listener {
	
	// MySQL/Database stuff.
	private Connection connection;
	public String host, database, username, password, table;
	public int port;

	// When the server starts up, create the custom config, load the config, setup the MySQL stuff,
	// and register the MySQLSetterGetter class.
	
	FileConfiguration config = getConfig();
	
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public void onEnable() {
		otherLog("[" + format.format(now) + "]" + "Attempting to load config...");
		loadConfig();
		otherLog("[" + format.format(now) + "]" + "Attempting to connect to MySQL database...");
		mysqlSetup();
		
		new Commands(this);
		getLogger().info("Successfully loaded commands.");
		
		config = getConfig();
		
		this.getServer().getPluginManager().registerEvents(new MysqlSetterGetter(), this);
		getLogger().info("Loaded MySQL class.");
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
		getLogger().info("Registered listeners.");
		logFile("Server has started.");
		otherLog("Server has started at " + "[" + format.format(now) + "]");
	}
	
	public void onDisable() {
		logFile("Server has stopped.");
		otherLog("Server has stopped at " + "[" + format.format(now) + "]");
	}
	
	// Loads the config (default one).
	public void loadConfig(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		otherLog("[" + format.format(now) + "]" + "Successfully loaded config.");
	}

    // Connects to the config to get the host, port, database name, username, password, and table.
    // Also sets up the MySQL connection.
	public void mysqlSetup() {
		host = this.getConfig().getString("host");
		port = this.getConfig().getInt("port");
		database = this.getConfig().getString("database");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");
		table = this.getConfig().getString("table");

		try {

			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}

				Class.forName("com.mysql.jdbc.Driver");
				setConnection(
						DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
								this.username, this.password));

				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
				this.otherLog("MySQL successfully conected at " + "[" + format.format(now) + "]");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public void logFile(String message) {
		try {
			File dataFolder = getDataFolder();
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			
			File saveTo = new File(getDataFolder(), "buy_log.txt");
			if (!saveTo.exists()) {
				saveTo.createNewFile();
			}
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void otherLog(String message) {
		try {
			File dataFolder = getDataFolder();
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			
			File saveTo = new File(getDataFolder(), "debug_log.txt");
			if (!saveTo.exists()) {
				saveTo.createNewFile();
				otherLog("[" + format.format(now) + "]" + "Successfully created debug_log.txt.");
			}
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}