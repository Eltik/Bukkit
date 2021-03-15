package xp10d3.corelia.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MysqlSetterGetter implements Listener {

	Core plugin = Core.getPlugin(Core.class);

	// On the player join, run the "createPlayer" method.
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		checkIfTable();
		getCoinOrCreatePlayer(player.getUniqueId(), player);
	}

	// How the player gets gold
	/*
	 * @EventHandler public void onPlayerDeath(PlayerDeathEvent event) { Player
	 * player = event.getEntity(); Player get = player.getPlayer();
	 * updateCoins(get.getPlayer().getUniqueId());
	 * getCoins(get.getPlayer().getUniqueId()); }
	 */

	// If player exists then send console message saying it was found.
	// If not found, send a console message saying it's not found.
	/*
	public boolean playerExists(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());

			ResultSet results = statement.executeQuery();
			if (results.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Player Found");
				return true;
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Player NOT Found");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	*/
	
	public void checkIfTable() {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `player_data` (GOLD int, NAME varchar(255), UUID varchar(255))");
			int results = statement.executeUpdate();
			if (results == 1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Table wasn't found. EconomyGold is trying to create one...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean playerExists(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
               //Record(s) found(s). player already in the table
            	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Player found in database. No action needed.");
            }
           else {
              //No record found. Player not in the table. We can add it here
        	   getCoinOrCreatePlayer(player.getUniqueId(), player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	// If player is not found, add it to the database with the UUID, name, and gold
	// amount.
	/*
	public void createPlayer(final UUID uuid, Player player) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				if (playerExists(uuid) != true) {
					PreparedStatement insert = plugin.getConnection()
							.prepareStatement("INSERT INTO " + plugin.table + " (UUID,NAME,GOLD) VALUES (?,?,?)");
					insert.setString(1, uuid.toString());
					insert.setString(2, player.getName());
					insert.setInt(3, 0);
					insert.executeUpdate();

					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Player Inserted");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public int getCoinOrCreatePlayer(final UUID uuid, Player player) {
        int playerCoin = 0;

        try {
            PreparedStatement statement = plugin.getConnection()
                .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                    PreparedStatement insert = plugin.getConnection()
                            .prepareStatement("INSERT INTO " + plugin.table + " (UUID,NAME,GOLD) VALUES (?,?,?)");
                    insert.setString(1, uuid.toString());
                    insert.setString(2, player.getName());
                    insert.setInt(3, 0);
                    insert.executeUpdate();

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Player Inserted");
             
            } else {
                playerCoin = results.getInt("GOLD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playerCoin;
    }

	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Update coin value (add 1).
	public void updateCoins(UUID uuid) {
		try {
			PreparedStatement statement1 = plugin.getConnection()
					.prepareStatement("UPDATE " + plugin.table + " SET GOLD=? WHERE UUID=?");
			ResultSet results = statement1.executeQuery();
			results.next();
			statement1.setInt(1, results.getInt("GOLD") + 1);
			statement1.setString(2, uuid.toString());
			statement1.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void getCoins(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();

			//System.out.print("Player has " + results.getInt("GOLD") + " gold.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}