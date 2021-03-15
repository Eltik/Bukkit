package eltik.set.home;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Commands implements CommandExecutor {
		// Get's the main class.
		Core core = Core.getPlugin(Core.class);
		
		// Get's the data + time for logging purposes.
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		// Constructor
		public Commands(Core core) {
			this.core = core;
			Bukkit.getPluginCommand("sethome").setExecutor(this);
			Bukkit.getPluginCommand("home").setExecutor(this);
			Bukkit.getPluginCommand("homes").setExecutor(this);
			Bukkit.getPluginCommand("delhome").setExecutor(this);
		}
		
    	final FileConfiguration config = core.getConfig();
    	File file = new File(core.getDataFolder(), "config.yml");
    	
		@SuppressWarnings("unused")
		public void setHome(Player player, String homeName) {
    		int i = 1;
    		boolean canSetHome = true;
    		core.otherLog("==================================");
    		
    		core.otherLog("config test: " + config.getConfigurationSection("players.homes." + player.getUniqueId() + "."));
    		if (config.isConfigurationSection("players.homes." + player.getUniqueId().toString())) {
				for (String key : config.getConfigurationSection("players.homes." + player.getUniqueId().toString() + ".").getKeys(false)) {
	    			i++;
	    			core.otherLog("i: " + i);
	    			if (player.hasPermission("homes." + i)) {
	    				core.otherLog("Player has permission homes." + i);
	    				canSetHome = true;
	    			} else {
	    				core.otherLog("Player doesn't have permission homes." + i);
	    				canSetHome = false;
	    			}
				}
    		}
    		core.otherLog("canSetHome: " + canSetHome);
    		if (canSetHome) {
				Location location = player.getLocation();
				config.set("players.homes." + player.getUniqueId().toString() + "." + homeName + ".x", location.getX());
				config.set("players.homes." + player.getUniqueId().toString() + "." + homeName + ".y", location.getY());
				config.set("players.homes." + player.getUniqueId().toString() + "." + homeName + ".z", location.getZ());
				config.set("players.homes." + player.getUniqueId().toString() + "." + homeName + ".world", location.getWorld().getName().toString());
				
				core.otherLog("Player is attempting to set a home. Checking whether the home exists...");
				core.otherLog("Does the config contain the same name as what the player submitted: " + config.contains("players.homes." + player.getUniqueId().toString() + "." + homeName + "."));
				core.otherLog("Player set a home.");
				core.otherLog("Player UUID: " + player.getUniqueId().toString());
				core.otherLog("Home name: " + homeName);
				core.otherLog("X: " + location.getX());
				core.otherLog("Y: " + location.getY());
				core.otherLog("Z: " + location.getZ());
				core.otherLog("world: " + location.getWorld().getName().toString());
				
				try {
					config.save(file);
					player.sendMessage(ChatColor.GREEN + "Successfully set home " + ChatColor.GOLD + homeName + ChatColor.GREEN + ".");
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
					core.otherLog("[" + format.format(now) + "]" + " Successfully was able to save home to config.");
				} catch (IOException e) {
					e.printStackTrace();
					player.sendMessage(ChatColor.RED + "There was an error setting your home. Contact Eltik.");
					core.otherLog("[" + format.format(now) + "]" + " ERROR. Could not save home to config file. Please send this to xp10d363@gmail.com. Error:");
					core.otherLog(e.getMessage());
				}
    		} else {
    			int ie = i -= 1;
    			player.sendMessage(ChatColor.RED + "You can't set more than " + ie + " home(s)!");
    		}
		}
		
	    @Override
		public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
	    	Player player = (Player) sender;
	    	
	    	if (cmd.getName().equalsIgnoreCase("sethome")) {
	    		if (args.length == 1) {
		        	String homeName = args[0];
		        	if (config.isConfigurationSection("players.homes." + player.getUniqueId().toString())) {
		        		if (!config.contains("players.homes." + player.getUniqueId().toString() + "." + homeName + ".")) {
		        			setHome(player, homeName);
		    			} else {
		    				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
		    				player.sendMessage(ChatColor.RED + "You can't set a home with the same name!");
		    			}
		        	} else {
		        		setHome(player, homeName);
		        	}
	    		} else {
	    			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
	    			player.sendMessage(ChatColor.RED + "Too many/too little arguments! Correct usage: /sethome <home_name>");
	    		}
	    	}
	    	
	    	if (cmd.getName().equalsIgnoreCase("home")) {
	    		if (args.length == 1 || args == null) {
		        	final FileConfiguration config = core.getConfig();
		        	String homeName = args[0];
		        	double locx = config.getDouble("players.homes." + player.getUniqueId().toString() + "." + homeName + ".x");
		        	double locy = config.getDouble("players.homes." + player.getUniqueId().toString() + "." + homeName + ".y");
		        	double locz = config.getDouble("players.homes." + player.getUniqueId().toString() + "." + homeName + ".z");
		        	String world = config.getString("players.homes." + player.getUniqueId().toString() + "." + homeName + ".world");
		        	
		        	if (world == null || locx == 0 || locy == 0 || locz == 0) {
		        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
		        		player.sendMessage(ChatColor.RED + "That home doesn't exist!");
		        	} else {
			        	if (!(player.getWorld().getName().toString().equalsIgnoreCase(world))) {
			        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
			        		player.sendMessage(ChatColor.RED + "This home is set in a different world! Your current world is " + ChatColor.GREEN + player.getWorld().getName().toString() + ChatColor.RED + " and that home is set in " + ChatColor.GREEN + world);
			        	} else {
			        		Location location = new Location(player.getWorld(), locx, locy, locz);
			        		player.teleport(location);
			        		core.otherLog("Player " + player.getName().toString() + " teleported to home " + args[0] + ". Location: " + location);
			        	}
		        	}
	        	} else {
	        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
	        		player.sendMessage(ChatColor.RED + "Too many/too little arguments! Correct usage: /home <home_name>");
	        	}
	    	}
	    	
	    	if (cmd.getName().equalsIgnoreCase("homes")) {
	    		
	    		if (args.length == 0) {
		        	final FileConfiguration config = core.getConfig();
		        	
	    			if (config.isConfigurationSection("players.homes." + player.getUniqueId().toString())) {
	    				int i = 0;
	    				Inventory gui = Bukkit.createInventory(null, 54,ChatColor.RED + "Homes");
	    				for (String key : config.getConfigurationSection("players.homes." + player.getUniqueId().toString() + ".").getKeys(false)) {
	    					core.otherLog("==================================");
	    					//core.otherLog("Player ran the command /homes. NOTE: If world is null, that is an issue. Please contact Eltik at xp10d363@gmail.com. Data:");
	    					core.otherLog("Player ran the command /homes.");
	    					/*
	    					core.otherLog("Home Name: " + key);
	    					double locx = config.getDouble(key + ".x");
	    					core.otherLog("Config path: " + config.getCurrentPath());
		    	        	double locy = config.getDouble(key + ".y");
		    	        	double locz = config.getDouble(key + ".z");
		    	        	String world = config.getString(key + ".world");
		    	        	core.otherLog("locx: " + locx);
		    	        	core.otherLog("locy: " + locy);
		    	        	core.otherLog("locz: " + locz);
		    	        	core.otherLog("world: " + world);
		    	        	*/
		    	        	
		    	        	ItemStack homeIcon = new ItemStack(Material.RED_BED);
		    	        	ItemMeta itemMeta = homeIcon.getItemMeta();
		    	        	itemMeta.setDisplayName(ChatColor.GOLD + key);
		    	        	
		    	        	/*
		    	        	ArrayList<String> lore = new ArrayList<String>();
		    	        	lore.add(ChatColor.WHITE + "X: " + locx);
		    	        	lore.add(ChatColor.WHITE + "Y: " + locy);
		    	        	lore.add(ChatColor.WHITE + "Z: " + locz);
		    	        	lore.add(ChatColor.WHITE + "World: " + world);
		    	        	
		    	        	itemMeta.setLore(lore);
		    	        	*/
		    	        	itemMeta.setCustomModelData(14154);
		    	        	
		    	        	homeIcon.setItemMeta(itemMeta);
		    	        	
		    	            gui.setItem(i, homeIcon);
		    	            i++;
		    			}
		    			player.openInventory(gui);
	    			} else {
	    				core.otherLog("Player " + player.getName().toString() + " opened the homes GUI, but didn't have any homes.");
	    				player.sendMessage(ChatColor.RED + "You don't have any homes!");
	    				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 10, 1);
	    			}
	    		} else {
	    			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
	    			player.sendMessage(ChatColor.RED + "Too many arguments! Correct usage: /homes");
	    		}
	    	}
	    	
	    	if (cmd.getName().equalsIgnoreCase("delhome")) {
	    		if (args.length == 1 || args == null) {
		        	final FileConfiguration config = core.getConfig();
		        	String homeName = args[0];
		        	String world = config.getString("players.homes." + player.getUniqueId().toString() + "." + homeName + ".world");
		        	
		        	core.otherLog("delhome args: " + args[0]);
		        	core.otherLog("world: " + world);
		        	
		        	if (world == null) {
		        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 10, 1);
		        		player.sendMessage(ChatColor.RED + "That home doesn't exist!");
		        	} else {
			        	config.set("players.homes." + player.getUniqueId().toString() + "." + args[0], null);
			        	try {
			        		config.save(file);
			        	} catch(IOException e) {
			        		e.printStackTrace();
			        		player.sendMessage(ChatColor.RED + "There was an error deleting that home. Please contact Eltik.");
			        		core.otherLog("There was an error deleting a home. Error:");
			        		core.otherLog(e.getMessage());
			        	}
			        	player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
			        	player.sendMessage(ChatColor.GREEN + "Deleted the home " + ChatColor.GOLD + args[0] + ChatColor.GREEN + ".");
		        	}
	        	} else {
	        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
	        		player.sendMessage(ChatColor.RED + "Too many/too little arguments! Correct usage: /home <home_name>");
	        	}
	    	}
	    	
			return false;	
	    }

}
