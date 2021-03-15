package xp10d3.corelia.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListeners implements Listener {
	
	private Core core;
	
	public PlayerListeners(Core core) {
		this.core = core;
	}
	
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// For assigning NBT tags: https://bukkit.org/threads/nbt-tags.110447/
	@EventHandler
	public void onItemClick(InventoryClickEvent event) {
	    Player player = (Player) event.getWhoClicked();
	    String itemName = event.getCurrentItem().getType().name();
	    
	    
	    ItemStack item = event.getCurrentItem();
	    ItemMeta itemMeta = item.getItemMeta();
	    
	    if (item == null || !itemMeta.isUnbreakable() || item.getType() == Material.AIR || !item.hasItemMeta()) {
	    	core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ITEM CLICKED]: " + item +"[" + format.format(now) + "]");
	    	return;
	    }
	    
	    if (itemMeta.isUnbreakable()) {
	    
		    ConfigurationSection cfg = core.getConfig().getConfigurationSection("shop.items");
		    if (cfg == null) return;
		    if (!cfg.getKeys(false).contains(itemName)) return;
		   
		    int cost = cfg.getInt(itemName + ".cost");
		    String command = cfg.getString(itemName + ".command");
		    
		    try {
		        PreparedStatement statement = core.getConnection()
		                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
		        statement.setString(1, player.getUniqueId().toString());
		        ResultSet results = statement.executeQuery();
		        results.next();
		       
		        int coins = results.getInt("GOLD");
		        if (coins >= cost) {
		            player.closeInventory();
		            coins -= cost;
		            PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, -cost);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.getServer().dispatchCommand(core.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
		            core.logFile("Player has bought something. STATS: [PLAYER]: " + player + " | [ITEM CLICKED]:" + itemName + " | [COST]: " + cost + " | [COMMAND RUNNED]: " + command + " | [CURRENT GOLD]: " + coins  + " | [TIME]: " + "[" + format.format(now) + "]");
		        } else {
		            player.closeInventory();
		            player.sendMessage("You don't have enough coins!");
		            core.otherLog("Player didn't have enough coins to buy an item! STATS: [PLAYER]: " + player + " | [ITEM CLICKED]: " + itemName + " | [COST]: " + cost + " | [CURRENT GOLD]: " + coins + " | [TIME]: " + "[" + format.format(now) + "]");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	    } else {
	    	return;
	    }
	}
	@EventHandler
	public void onMine(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
			return;
		} else {
			int iron = core.getConfig().getInt("mine.block.iron");
			int gold = core.getConfig().getInt("mine.block.gold");
			int coal = core.getConfig().getInt("mine.block.coal");
			int lapis = core.getConfig().getInt("mine.block.lapis");
			int redstone = core.getConfig().getInt("mine.block.redstone");
			int diamond = core.getConfig().getInt("mine.block.diamond");
			int emerald = core.getConfig().getInt("mine.block.emerald");
			int quartz = core.getConfig().getInt("mine.block.quartz");
			boolean check = core.getConfig().getBoolean("mine.enable");
			if (check == true) {
				if (block.getType() == Material.IRON_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +iron);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: IRON_ORE" + " | [MONEY GOTTEN]: " + iron + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + iron + " gold for mining iron ore.");
				} else if (block.getType() == Material.GOLD_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +gold);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: GOLD_ORE" + " | [MONEY GOTTEN]: " + gold + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + gold + " gold for mining gold ore.");
				} else if (block.getType() == Material.COAL_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +coal);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: COAL_ORE" + " | [MONEY GOTTEN]: " + coal + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + coal + " gold for mining coal ore.");
				} else if (block.getType() == Material.LAPIS_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +lapis);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: LAPIS_ORE" + " | [MONEY GOTTEN]: " + lapis + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + lapis + " gold for mining lapis ore.");
				} else if (block.getType() == Material.REDSTONE_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +redstone);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: REDSTONE_ORE" + " | [MONEY GOTTEN]: " + redstone + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + redstone + " gold for mining redstone ore.");
				} else if (block.getType() == Material.DIAMOND_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +diamond);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: DIAMOND_ORE" + " | [MONEY GOTTEN]: " + diamond + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + diamond + " gold for mining diamond ore.");
				} else if (block.getType() == Material.EMERALD_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +emerald);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: EMERALD_ORE" + " | [MONEY GOTTEN]: " + emerald + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + emerald + " gold for mining emerald ore.");
				} else if (block.getType() == Material.NETHER_QUARTZ_ORE) {
					try {
				        PreparedStatement statement = core.getConnection()
				                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
				        statement.setString(1, player.getUniqueId().toString());
				        ResultSet results = statement.executeQuery();
				        results.next();
				        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
						statement1.setInt(1, +quartz);
						statement1.setString(2, player.getUniqueId().toString());
						statement1.executeUpdate();
			            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [BLOCK BROKEN]: NETHER_QUARTZ_ORE" + " | [MONEY GOTTEN]: " + quartz + "[" + format.format(now) + "]");
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
					player.sendMessage(ChatColor.GREEN + "You have gotten " + quartz + " gold for mining nether quartz ore.");
				}
			} else {
				return;
			}
		}
	}
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		ItemMeta clickedMeta = clicked.getItemMeta();
		if (clicked.getType() == Material.GOLD_BLOCK && clickedMeta.isUnbreakable() == true) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "Don't take the gold block :P");
		}
	}
	
	@EventHandler
	public void onMobKill(EntityDeathEvent event) {
		Entity dead = event.getEntity();
		Entity player = event.getEntity().getKiller();
		int bat = core.getConfig().getInt("kill.entity.bat");
		int bee = core.getConfig().getInt("kill.entity.bee");
		int blaze = core.getConfig().getInt("kill.entity.blaze");
		int cat = core.getConfig().getInt("kill.entity.cat");
		int cavespider = core.getConfig().getInt("kill.entity.cavespider");
		int chicken = core.getConfig().getInt("kill.entity.chicken");
		int cod = core.getConfig().getInt("kill.entity.cod");
		int cow = core.getConfig().getInt("kill.entity.cow");
		int creeper = core.getConfig().getInt("kill.entity.creeper");
		int dolphin = core.getConfig().getInt("kill.entity.dolphin");
		int donkey = core.getConfig().getInt("kill.entity.donkey");
		int drowned = core.getConfig().getInt("kill.entity.drowned");
		int elderguardian = core.getConfig().getInt("kill.entity.elderguardian");
		int enderdragon = core.getConfig().getInt("kill.entity.enderdragon");
		
		if (dead.getLastDamageCause() instanceof Player) {
			if (dead.getType().equals(EntityType.BAT)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +bat);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: BAT" + " | [MONEY GOTTEN]: " + bat + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.BEE)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +bee);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: BEE" + " | [MONEY GOTTEN]: " + bee + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.BLAZE)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +blaze);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: BLAZE" + " | [MONEY GOTTEN]: " + blaze + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.CAT)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +cat);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: CAT" + " | [MONEY GOTTEN]: " + cat + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.CAVE_SPIDER)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +cavespider);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: CAVE_SPIDER" + " | [MONEY GOTTEN]: " + cavespider + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.CHICKEN)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +chicken);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: CHICKEN" + " | [MONEY GOTTEN]: " + chicken + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.COD)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +cod);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: COD" + " | [MONEY GOTTEN]: " + cod + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.COW)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +cow);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: COW" + " | [MONEY GOTTEN]: " + cow + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.CREEPER)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +creeper);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: CREEPER" + " | [MONEY GOTTEN]: " + creeper + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.DOLPHIN)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +dolphin);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: DOLPHIN" + " | [MONEY GOTTEN]: " + dolphin + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.DONKEY)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +donkey);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: DONKEY" + " | [MONEY GOTTEN]: " + donkey + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.DROWNED)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +drowned);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: DROWNED" + " | [MONEY GOTTEN]: " + drowned + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.ELDER_GUARDIAN)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +elderguardian);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: ELDER_GUARDIAN" + " | [MONEY GOTTEN]: " + elderguardian + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			} else if (dead.getType().equals(EntityType.ENDER_DRAGON)) {
				try {
			        PreparedStatement statement = core.getConnection()
			                .prepareStatement("SELECT * FROM " + core.table + " WHERE UUID=?");
			        statement.setString(1, player.getUniqueId().toString());
			        ResultSet results = statement.executeQuery();
			        results.next();
			        PreparedStatement statement1 = core.getConnection().prepareStatement("UPDATE " + core.table + " SET GOLD = GOLD + ? WHERE UUID=?");
					statement1.setInt(1, +enderdragon);
					statement1.setString(2, player.getUniqueId().toString());
					statement1.executeUpdate();
		            core.logFile("Player has got money. STATS: [PLAYER]: " + player + " | [ENTITY KILLED]: ENDER_DRAGON" + " | [MONEY GOTTEN]: " + enderdragon + "[" + format.format(now) + "]");
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			}
		}
	}
}
