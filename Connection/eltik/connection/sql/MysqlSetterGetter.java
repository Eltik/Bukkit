package eltik.connection.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MysqlSetterGetter implements Listener {

	// Get's the main class
	Core plugin = Core.getPlugin(Core.class);

	// If player exists then send console message saying it was found.
	// If not found, send a console message saying it's not found.
	public boolean playerExists(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());

			ResultSet results = statement.executeQuery();
			if (results.next()) {
				Bukkit.getLogger().info(ChatColor.YELLOW + "Player Found");
				return true;
			}
			Bukkit.getLogger().info(ChatColor.RED + "Player NOT Found");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Check if the MySQL table exists and create it if it doesn't.
	public void checkIfTable() {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `rpg_data` (race varchar(255), class varchar(255), name varchar(255), UUID varchar(255), exp int, level int)");
			int results = statement.executeUpdate();
			if (results == 1) {
				Bukkit.getLogger().info(ChatColor.GREEN + "Table wasn't found. Connection is trying to create one...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// If player is not found, add it to the database with the UUID, name, and other properties that are necessary.
	public void createPlayer(final UUID uuid, Player player) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				if (playerExists(uuid) != true) {
					PreparedStatement insert = plugin.getConnection()
							.prepareStatement("INSERT INTO " + plugin.table + " (race,class,name,UUID,exp,level) VALUES (?,?,?,?,?,?)");
					insert.setString(1, "null");
					insert.setString(2, "null");
					insert.setString(3, player.getName());
					insert.setString(4, uuid.toString());
					insert.setInt(5, 0);
					insert.setInt(6, 1);
					insert.executeUpdate();

					Bukkit.getLogger().info(ChatColor.GREEN + "Player Inserted");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Checks if a string is an integer
	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Update method (not needed, just there for reference reasons)
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

}