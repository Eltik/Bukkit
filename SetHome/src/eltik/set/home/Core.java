package eltik.set.home;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

	// Get's the config.yml file.
	FileConfiguration config = getConfig();
	
	// Get's the data + time for logging purposes.
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public void onEnable() {
		otherLog("[" + format.format(now) + "]" + " Server starting.");
		// Load the config
		loadConfig();
		
		// Config variable.
		config = getConfig();
		
		// Register commands
		new Commands(this);
		getLogger().info("Successfully loaded commands.");
		otherLog("Loaded commands.");
		// For logging
		getLogger().info("Plugin " +  this.getDescription().getFullName().toString() + " version " + this.getDescription().getVersion().toString() + " loaded.");
		otherLog("Loaded plugin " + this.getDescription().getFullName().toString() + " version " + this.getDescription().getVersion().toString() + ".");
		getLogger().info(this.getDescription().getFullName().toString() + "'s native version is " + this.getDescription().getAPIVersion().toString() + " and is running on server version " + Bukkit.getServer().getVersion().toString() + ".");
		otherLog(this.getDescription().getFullName().toString() + "'s native version is " + this.getDescription().getAPIVersion().toString() + " and is running on server version " + Bukkit.getServer().getVersion().toString() + ".");
		if (this.getDescription().getAPIVersion().toString().equalsIgnoreCase(Bukkit.getVersion().toString())) {
			getLogger().info(this.getDescription().getFullName().toString() + " is running on the correct version.");
			otherLog(this.getDescription().getFullName().toString() + " is running on the correct version.");
		} else {
			getLogger().info("WARNING. Plugin is running on a different version. Some features may not work.");
			otherLog("WARNING. Plugin is running on a different version. Some features may not work.");
		}
		getLogger().info("Bukkit version: " + Bukkit.getBukkitVersion());
		otherLog("Bukkit version: " + Bukkit.getBukkitVersion());
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
		getLogger().info("Registered listeners.");
		otherLog("Registered listeners.");
		otherLog("[" + format.format(now) + "]" + " Succesfully started " + this.getDescription().getFullName().toString() + this.getDescription().getVersion());
	}
	
	// When the server stops...
	public void onDisable() {
		// Send a message saying that it's stopping the plugin.
		getLogger().info("Stopping plugin 'SetHome'...");
		otherLog("Server stopping...");
		// Send log info.
		getLogger().info("Plugin stopped. Goodbye!");
		otherLog("[" + format.format(now) + "]" + " Plugin stopped.");
	}
	
	// Loading config
	public void loadConfig(){
		// Copy everything from the config.yml to the actual file in the folder.
		getConfig().options().copyDefaults(true);
		otherLog("Copying defaults from config.yml...");
		// Save the config.
		saveConfig();
		otherLog("Saving config.yml...");
		otherLog("[" + format.format(now) + "]" + " Succesfully loaded config.yml.");
	}
	
	// Logging method
	public void otherLog(String message) {
		
		boolean log = config.getBoolean("log-items");
		if (log) {
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
					otherLog("[" + format.format(now) + "]" + " Successfully created debug_log.txt.");
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
	}
	
}
