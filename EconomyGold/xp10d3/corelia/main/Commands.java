package xp10d3.corelia.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Commands implements CommandExecutor {
	
	Core core = Core.getPlugin(Core.class);
	
	public Commands(Core core) {
		this.core = core;
		Bukkit.getPluginCommand("eshop").setExecutor(this);
		Bukkit.getPluginCommand("epay").setExecutor(this);
		Bukkit.getPluginCommand("gold").setExecutor(this);
	}
	
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
    public Inventory shopGUI() {
    	
    	final FileConfiguration config = core.getConfig();
    	
    	
		// Creates the inventory.
		final Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.BLACK + "Shop");
		  int i = 0;
		  for(String key : config.getConfigurationSection("shop.items").getKeys(false)) {
			    Material material = Material.getMaterial(key.toUpperCase());
			    String name = config.getString("shop.items." + key + ".name");
			    double cost = config.getDouble("shop.items." + key + ".cost");
			    if (name == null) {
			    	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Name is null! Please fill in the appropriate name.");
			    	core.otherLog("[" + format.format(now) + "]" + " Name is null.");
			    	return inventory;
			    } else if (material == null) {
			    	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Material is null! Please fill in the appropriate material.");
			    	core.otherLog("[" + format.format(now) + "]" + " Material is null.");
			    	return inventory;
			    }
			    final ItemStack item = new ItemStack(material);
			    final ItemMeta itemMeta = item.getItemMeta();
			    ArrayList<String> lore = new ArrayList<String>();
			    lore.add("Price: " + cost);
			    itemMeta.setLore(lore);
			    itemMeta.setUnbreakable(true);
			    itemMeta.setDisplayName(ChatColor.WHITE + name);
			    
			    item.setItemMeta(itemMeta);
			    
			    inventory.setItem(i, item);
		    i++;
		  }
		
		return inventory;
		
	}
    
    public Inventory balance(Player player) {
    	
    	String uuid = player.getUniqueId().toString();
    	
    	final Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.GOLD + "Balance");
	    
		final ItemStack item = new ItemStack(Material.GOLD_BLOCK);
	    final ItemMeta itemMeta = item.getItemMeta();
	    itemMeta.setUnbreakable(true);
    	
    	try {
			PreparedStatement statement = core.getConnection().prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			/*
			player.sendMessage("You have " + results.getInt("GOLD") + " gold.");
			*/
		    itemMeta.setDisplayName(ChatColor.GOLD + "Balance: " + results.getInt("GOLD"));
		    
		    item.setItemMeta(itemMeta);
		    
		    inventory.setItem(4, item);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventory;
		
	}
    
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to access this command!");
			core.otherLog("[" + format.format(now) + "]" + " Something other then a player tried to run a command. Sender was: " + sender);
			return false;
		}
    	Player player = (Player) sender;
    	String playerName = player.getName();
		UUID uuid = player.getUniqueId();
		if (cmd.getName().equalsIgnoreCase("eshop") && player.hasPermission("shop.eshop")) {
			if (args.length == 0) {
				core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " ran the command /eshop.");
				player.openInventory(shopGUI());
				core.otherLog("[" + format.format(now) + "]" + " Successfully opened GUI inventory.");
				return true;
			} else {
				player.sendMessage("Too many arguments! Correct usage: /eshop");
				core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to run the command /eshop but had too many arguments.");
			}
		} else if (!player.hasPermission("shop.eshop")) {
			player.sendMessage(ChatColor.RED + "You don't have permission to execute this command!");
			core.otherLog("[" + format.format(now) + "]" + " Player didn't have the permission shop.eshop and tried to run the command /eshop.");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("gold")) {
			if (args.length == 0) {
				try {
					PreparedStatement statement = core.getConnection().prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
					statement.setString(1, uuid.toString());
					ResultSet results = statement.executeQuery();
					results.next();
					/*
					player.sendMessage("You have " + results.getInt("GOLD") + " gold.");
					*/
					player.openInventory(balance(player));
					core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " ran the command /gold.");
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				player.sendMessage("Too many arguments! Correct usage: /gold");
				core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " ran the command /gold but had too many arguments.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("epay") && player.hasPermission("shop.epay")) {
			if (args.length == 2) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player isn't online!");
					core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " ran the command /epay but target was offline. Target player: " + target + ".");
				} else {
					if (isInt(args[1]) == true) {
						int argumentTwo = Integer.parseInt(args[1]);
						try {
							PreparedStatement statement = core.getConnection()
									.prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
							statement.setString(1, uuid.toString());
							ResultSet results = statement.executeQuery();
							results.next();
							int coinsAmount = (results.getInt("GOLD"));
							if (argumentTwo < 1) {
								player.sendMessage(ChatColor.RED + "You can't pay another person a negative amount!");
								core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to pay another player a negative amount. Amount player tried to pay: " + argumentTwo + ".");
								return false;
							} else if (coinsAmount < 1 || coinsAmount < argumentTwo) {
								player.sendMessage(ChatColor.RED + "You don't have enough gold!");
								core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to pay player " + target + " but didn't have enough gold.");
								return false;
							} else {
								PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
								statement1.setInt(1, argumentTwo);
								statement1.setString(2, target.getUniqueId().toString());
								statement1.executeUpdate();
								PreparedStatement statement2 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
								statement2.setInt(1, -argumentTwo);
								statement2.setString(2, player.getUniqueId().toString());
								statement2.executeUpdate();
								player.sendMessage("Gave " + target.getName() + " " + argumentTwo + " gold.");
								target.sendMessage(ChatColor.GREEN + "You have recieved " + argumentTwo + " gold.");
								core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " payed " + target + argumentTwo + " gold. " + target + " successfully recieved " + argumentTwo + " gold.");
							}
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						player.sendMessage(ChatColor.RED + "Please use an integer. Example: /epay Xp10d3 50");
						core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to pay " + target + " but didn't use an integer.");
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "Too few arguments! Correct usage: /epay <player> <amount>");
				core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to run command /epay but used too few arguments.");
			}
		} else if (!player.hasPermission("shop.epay")) {
			player.sendMessage(ChatColor.RED + "You don't have permission to execute this command!");
			core.otherLog("[" + format.format(now) + "]" + " Player " + playerName + " tried to run command /epay but didn't have the permission shop.epay.");
			return false;
		}
		return false;
	}
    
    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
	public void getCoins(UUID uuid) {
		try {
			PreparedStatement statement = core.getConnection()
					.prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
