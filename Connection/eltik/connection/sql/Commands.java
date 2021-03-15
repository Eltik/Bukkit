package eltik.connection.sql;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Commands implements CommandExecutor {
	
	// Get's the main class.
	Core core = Core.getPlugin(Core.class);
	
	// Constructor
	public Commands(Core core) {
		this.core = core;
		Bukkit.getPluginCommand("rpgreload").setExecutor(this);
		Bukkit.getPluginCommand("stats").setExecutor(this);
		Bukkit.getPluginCommand("getdata").setExecutor(this);
	}
	
	// Get's the time + date for logging purposes
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// Get's the file "players.yml" in the plugin EndranLeveling system...
	File file = new File("plugins/EndranLevelingSystem/players.yml");
	// And then loads the file itself.
	FileConfiguration otherFile = YamlConfiguration.loadConfiguration(file);
	
	// The stats GUI
    public Inventory stats(Player player) {
    	
    	// Get the player's UUID
    	String uuid = player.getUniqueId().toString();
    	
    	// Create the inventory with 53 rows and a title of "Stats"
    	final Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Stats");
	    
    	// Create the Gold Block, add the enchantment, hide the enchantments.
		final ItemStack head = new ItemStack(Material.GOLD_BLOCK);
	    final ItemMeta headMeta = head.getItemMeta();
	    head.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1385);
	    headMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    
	    // Same with the exp bottle
	    final ItemStack experience = new ItemStack(Material.EXP_BOTTLE);
	    final ItemMeta expMeta = experience.getItemMeta();
	    experience.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1385);
	    expMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

	    // And the other exp bottle...
	    final ItemStack level = new ItemStack(Material.EXP_BOTTLE);
	    final ItemMeta lvlMeta = level.getItemMeta();
	    level.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1385);
	    lvlMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    
	    // And the skull...
	    final ItemStack race = new ItemStack(Material.SKULL_ITEM);
	    final ItemMeta raceMeta = race.getItemMeta();
	    race.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1385);
	    raceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    
	    // And the other skull...
	    final ItemStack classItem = new ItemStack(Material.SKULL_ITEM);
	    final ItemMeta classMeta = classItem.getItemMeta();
	    classItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1385);
	    classMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    
    	
    	try {
    		// Get everything from the table where the UUID = the player's UUID
			PreparedStatement statement = core.getConnection().prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			
			// Set the display name of the head to the player's name and then stats. Do something similar for the rest...
			headMeta.setDisplayName(ChatColor.GOLD + results.getString("name") + "'s Stats");
		    expMeta.setDisplayName("" + ChatColor.WHITE + results.getInt("exp") + ChatColor.GREEN + " experience");
		    lvlMeta.setDisplayName("" + ChatColor.WHITE + "Level " + ChatColor.GREEN + results.getInt("level"));
		    raceMeta.setDisplayName("" + ChatColor.WHITE + "Race: " + ChatColor.GOLD + results.getString("race"));
		    classMeta.setDisplayName("" + ChatColor.WHITE + "Class: " + ChatColor.GOLD + results.getString("class"));
		    
		    // Set the meta of each item to their respective variables.
		    head.setItemMeta(headMeta);
		    experience.setItemMeta(expMeta);
		    level.setItemMeta(lvlMeta);
		    race.setItemMeta(raceMeta);
		    classItem.setItemMeta(classMeta);
		    
		    // Set the placement of each item
		    inventory.setItem(4, head);
		    inventory.setItem(12, experience);
		    inventory.setItem(14, level);
		    inventory.setItem(21, race);
		    inventory.setItem(23, classItem);
			statement.close();
		} catch (SQLException e) {
			// If there are any errors, log it.
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			core.otherLog("[" + format.format(now) + "]" + "Error has occured when trying to create the inventory 'Stats.'");
			core.otherLog(errors.toString());
		}
		return inventory;
		
	}
    
    public void getData(Player player) {
    	File file = new File("plugins/EndranLevelingSystem/players.yml");
    	// Loads the file.
    	FileConfiguration otherFile = YamlConfiguration.loadConfiguration(file);
    	// Get's the config section "Players".
    	for (String key : otherFile.getConfigurationSection("Players").getKeys(false)) {
    		// Get's each var
    		String race = otherFile.getString("Players." + key + ".race");
			String cClass = otherFile.getString("Players." + key + ".class");
			String name = otherFile.getString("Players." + key + ".name");
			int exp = otherFile.getInt("Players." + key + ".exp");
			int level = otherFile.getInt("Players." + key + ".level");
			String mainKey = key;
			// Logs it.
			core.otherLog(
					"Race: " + race + " | Class: " + cClass + " | Name: " + name + " | Experience: " + exp + " | Level: " + level + " | UUID: " + mainKey
					+ "========"
					);
			// Sends a message to the player
			player.sendMessage(
					"Race: " + race + " | Class: " + cClass + " | Name: " + name + " | Experience: " + exp + " | Level: " + level + " | UUID: " + mainKey
					+ "========"
					);
    	}
    }
	
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
    	// If the sender (console, player, etc.) is not a player, send a message saying that only players are allowed to run commands.
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players are allowed to execute commands!");
		}
		// The player variable.
    	Player player = (Player) sender;
    	
    	// Stats command
    	if (cmd.getName().equalsIgnoreCase("stats")) {
    		// Open up the stats GUI
    		player.openInventory(stats(player));
    	}
    	
    	// Reload config command
    	if (cmd.getName().equalsIgnoreCase("rpgreload") && player.hasPermission("connection.reload") || player.hasPermission("connection.*")) {
    		core.saveConfig();
    		// Log everything
    		core.otherLog("Saved config.");
    		core.reloadConfig();
    		core.otherLog("Reloaded config.");
    		player.sendMessage(ChatColor.GREEN + "Reloaded config for the RPG.");
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("getdata") && player.hasPermission("connection.getdata") || player.hasPermission("connection.*")) {
    		// Get the data from the database
    		getData(player);
    	}
    	
		return false;
	}
}
