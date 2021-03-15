package eltik.connection.sql;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;

public class PlayerListeners implements Listener {
	
	// Get's the core variable.
	private Core core;
	
	// Constructor
	public PlayerListeners(Core core) {
		this.core = core;
	}
	
	// Get's the date + time to then write into core.otherLog()
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// On player join check if user is in the config file. If they aren't insert
	// data. Otherwise, update the MySQL table.
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		core.otherLog("[" + format.format(now) + "]" + "Player has joined! Running the runMultiple() method...");
		try {
			core.runMultiple();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// On player leave check if user is in the config file. If they aren't insert
	// data. Otherwise, update the MySQL table.
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		core.otherLog("[" + format.format(now) + "]" + "Player has left. Running the runMultiple() method...");
		try {
			core.runMultiple();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		//Player player = event.getPlayer();
		try {
			core.runMultiple();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		//core.dungeonPortal(player);
	}
	
	// Check if the mob that was killed by a player has a certain name. Add experience to
	// player if it matches.
	@EventHandler
	public void onMobDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity(); // Get's the entity
		String name = entity.getCustomName(); // Get's the entities' name
		Entity player = event.getEntity().getKiller(); // Get the entities' killer
		System.out.println(entity.getLastDamageCause());
		File file = new File("plugins/EndranLevelingSystem/players.yml"); // Get the players.yml file
		Plugin plugin = Bukkit.getPluginManager().getPlugin("EndranLevelingSystem"); // Get the plugin EndranLevelingSystem
		FileConfiguration otherFile = YamlConfiguration.loadConfiguration(file); // Load the file
		if (player instanceof Player && entity.getType().equals(EntityType.SKELETON)) { // If the player is equal to an actual player AND the entity is equal to a skeleton...
			core.otherLog("----------");
			core.otherLog("Entity skeleton was killed.");
			core.otherLog("Entity: " + entity);
			core.otherLog("Name of mob that was killed: " + name);
			core.otherLog("Player that killed mob: " + player);
			if (name == ChatColor.GREEN + "Skeletal Knight") { // If the name of the entity is equal to "Skeletal Knight"...
				// Write into the players.yml file
				for (String key : otherFile.getConfigurationSection("Players").getKeys(false)) {
					if (key.equals(player.getUniqueId().toString())) {
						int exp = otherFile.getInt("Players." + key + ".exp");
						otherFile.set("Players." + key + ".exp", exp + 250);
						try {
							otherFile.save(file);
							plugin.reloadConfig();
							YamlConfiguration.loadConfiguration(file);
						} catch (IOException e) {
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							core.otherLog("[" + format.format(now) + "]" + "Error has occured when checking loading config file.");
							core.otherLog(errors.toString());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onItemPickup(InventoryClickEvent event) {
		// If the item is not equal to null and the item is equal to GOLD_BLOCK, or if the is not equal to null and the item is equal to EXP_BOTTLE or SKULL_ITEM, then...
		if (event.getCurrentItem().getType() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().getType().equals(Material.GOLD_BLOCK) || event.getCurrentItem().getType().equals(Material.EXP_BOTTLE) || event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
			core.otherLog("Player clicked an item in the GUI. Item is " + event.getCurrentItem().getType() + ". It has the meta " + event.getCurrentItem().getItemMeta() + ".");
			// Log the information.
			if (event.getCurrentItem().getType() != null && event.getCurrentItem().getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) { // If the above is true and the item has the ItemFlag HIDE_ENCHANTS...
				core.otherLog("Player clicked an item that was in a GUI AND it has the item flag HIDE_ENCHANTS.");
				event.setCancelled(true);
				// Cancel the event.
				core.otherLog("Stopped the event.");
			}
		}
	}

}
