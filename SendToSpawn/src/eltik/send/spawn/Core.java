package eltik.send.spawn;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
	}
}
