package eltik.anvil.gui;


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
	
	FileConfiguration config = getConfig();

	// Get's the data + time for logging purposes.
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public Core INSTANCE = this;
	public void onEnable() {
		getLogger().info("Starting up...");
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListenres(this), this);
		getLogger().info("Registered listeners.");
		otherLog("[" + format.format(now) + "]" + " Registered plugin listeners.");
	}

	// Loading config
	public void loadConfig(){
		// Copy everything from the config.yml to the actual file in the folder.
		getConfig().options().copyDefaults(true);
		otherLog("[" + format.format(now) + "]" + " Copied config defaults.");
		// Save the config.
		saveConfig();
		otherLog("[" + format.format(now) + "]" + " Successfully loaded config.");
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
			File saveTo = new File(getDataFolder(), "misc_log.txt");
			if (!saveTo.exists()) { // If the file doesn't exist create it.
				saveTo.createNewFile();
				otherLog("=BEGINNING OF MISCELLANEOUS LOG=");
				otherLog("=DESCRIPTION=");
				otherLog("This is a log file that logs all miscellaneous information that isn't important, such as whether listeners are registered, the plugin description, server version, etc. Especially helpful for plugin developers or possible plugin clashes. Note that there are time/date stamps on most pieces of information that are logged.");
				otherLog("[" + format.format(now) + "]" + " Log file did not exist, so we created it.");
			}
			// Writing the actual message.
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// If there are any errors log it.
			this.getLogger().info("Couldn't create debug_log.txt. Error: ");
			e.printStackTrace();
		}
	}
	
	// Logging method
	public void anvilLog(String message) {
		try {
			// Get the folder of the plugin...
			File dataFolder = getDataFolder();
			// If the folder doesn't exist create it/get the directory.
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			
			// Where to save the file to/what the name of the file should be.
			File saveTo = new File(getDataFolder(), "anvil_log.txt");
			if (!saveTo.exists()) { // If the file doesn't exist create it.
				saveTo.createNewFile();
				otherLog("=BEGINNING OF ANVIL LOG=");
				otherLog("=DESCRIPTION=");
				otherLog("This log file will log all information that is important to enchanting and such. It will log anvil activity such as enchanting and such. Note that there are time/date stamps on most pieces of information that are logged.");
				otherLog("[" + format.format(now) + "]" + " Log file did not exist, so we created it.");
			}
			// Writing the actual message.
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// If there are any errors log it.
			this.getLogger().info("Couldn't create debug_log.txt. Error: ");
			e.printStackTrace();
		}
	}
	
}
