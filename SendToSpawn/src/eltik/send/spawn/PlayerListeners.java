package eltik.send.spawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {
	
	Core core = Core.getPlugin(Core.class);
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			double locx = core.getServer().getWorld("spawn").getSpawnLocation().getX();
			double locy = core.getServer().getWorld("spawn").getSpawnLocation().getY();
			double locz = core.getServer().getWorld("spawn").getSpawnLocation().getZ();
			
			Location to = new Location(core.getServer().getWorld("spawn"), locx, locy, locz);
			player.teleport(to);
		}
	}
}
