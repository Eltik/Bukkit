package eltik.set.home;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerListeners implements Listener {
	
	// Get's the main class.
	Core core = Core.getPlugin(Core.class);
	
	// Get's the data + time for logging purposes.
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}
		if (event.getCurrentItem().getItemMeta().hasCustomModelData() && !event.getCurrentItem().getType().equals(Material.AIR)) {
			if (event.getCurrentItem().getItemMeta().getCustomModelData() == 14154) {
				player.sendMessage(ChatColor.RED + "Use /home <home_name> instead of the GUI!");
				event.setCancelled(true);
			}
		} else {
			return;
		}
	}

}
