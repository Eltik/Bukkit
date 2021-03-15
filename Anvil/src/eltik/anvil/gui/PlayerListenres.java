package eltik.anvil.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PlayerListenres implements Listener {
	
	// Get's the core variable.
	private Core core;
	
	// Constructor
	public PlayerListenres(Core core) {
		this.core = core;
	}
	
	// Get's the date + time to then write into core.otherLog()
	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	// ------ Player info storage variables -------
	
	public static Map<String, GUI> guiManager = new HashMap<String, GUI>(); // keeps track of GUI instances
    public static Map<String, ItemStack> storage = new HashMap<String, ItemStack>(); // Item storage for renaming
    public static Map<String, ItemStack> storage2 = new HashMap<String, ItemStack>(); // Item storage for renaming
    public static Map<String, ItemStack> preview = new HashMap<String, ItemStack>(); // Preview item for rename
	public static Map<String, Integer> anvilUses = new HashMap<String, Integer>(); // Stores item anvil uses while in anvil menu
    public static Map<String, Integer> xpCost = new HashMap<String, Integer>(); // Stores base XP cost
	public static HashSet<String> complete = new HashSet<String>(); // Boolean for preventing duplication glitches involving incomplete actions
    public static HashSet<String> renameComplete = new HashSet<String>(); // Boolean for managing renames
    private HashSet<String> inAnvil = new HashSet<String>(); // Boolean for players in anvil
	
	

    // ------- Event Handler -------
    
    // Check for anvil use
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onRightClickAnvil(PlayerInteractEvent e) {
    	Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ANVIL) {
            e.setCancelled(true);
            guiManager.put(player.getName(), new GUI(player, core));
            guiManager.get(player.getName()).openInventory();
            core.otherLog("[" + format.format(now) + "]" + " Player opened anvil at " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ() + ", " + player.getLocation().getWorld().toString() + ".");
            core.anvilLog("[" + format.format(now) + "]" + " Player opened anvil at " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ() + ", " + player.getLocation().getWorld().toString() + ".");
            xpCost.put(player.getName(), 0);
            startListeners();
            inAnvil.add(player.getName());
        }
    }
    

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    	Player p = (Player) e.getWhoClicked();
    	int slot = e.getRawSlot();
        if(inAnvil.contains(p.getName()) == false) return;
        Inventory inv = guiManager.get(p.getName()).getInventory();
    	
        if(slot == 13 && complete.contains(p.getName()) == false) {
        	e.setCancelled(true);
        	return;
        } else if(slot == 13) {
    		storage.remove(p.getName());
    		storage2.remove(p.getName());
    		preview.remove(p.getName());
        	complete.remove(p.getName());
        	renameComplete.remove(p.getName());
        	return;
        }
        //if(e.getRawSlot() == 28 && renameComplete == true) return;
        //animation(p);
        final ItemStack clickedItem = e.getCurrentItem();
        
        // verify current item is not null
        if ((clickedItem == null || clickedItem.getType() == Material.AIR) && p.getItemOnCursor() == null) return;


        // Using slots click is a best option for your inventory click's
        if(clickedItem != null) {
	        if (clickedItem.getItemMeta().hasCustomModelData()) {
	            if (clickedItem.getItemMeta().getCustomModelData() == 16473) {
	            	e.setCancelled(true);
	            	return;
	            }
	        }
        }
        
        new BukkitRunnable(){
        	@Override
        	public void run() {
        		if(storage.get(p.getName()) != null) {
        			if((preview.get(p.getName()) == null || !storage.get(p.getName()).equals(inv.getItem(28)))) {
		        		if(inv.getItem(28) != null) {
		        			preview.put(p.getName(), storage.get(p.getName()).clone());
		        		} else {
		        			preview.remove(p.getName());
		        		}
		    			xpCost.put(p.getName(), 0);
		        		storage.put(p.getName(), inv.getItem(28));
			        	storage2.put(p.getName(), inv.getItem(34));
		        	}
		        		
	        	} else {
		    		storage.put(p.getName(), inv.getItem(28));
		        	storage2.put(p.getName(), inv.getItem(34));
		        	if(storage.get(p.getName()) != null){
		        		preview.putIfAbsent(p.getName(), storage.get(p.getName()).clone());
	
		        	} else {
		        		preview.remove(p.getName());
		        	}
	        	}
	        	
	        	this.cancel();
        	}
        }.runTaskLater(core, 1);
        
        if(slot == 22) { // anvil is pressed
        	e.setCancelled(true);
        	if(inv.getItem(GUI.leftAnim[0]).getType().equals(inv.getItem(GUI.rightAnim[0]).getType()) 
        			&& inv.getItem(GUI.rightAnim[0]).getType().equals(Material.GREEN_STAINED_GLASS_PANE) 
        			&& complete.contains(p.getName()) == false 
        			&& p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName())) >= 0) {
        		int levelCalc = p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName()));
        		p.setLevel(levelCalc);
    			preview.put(p.getName(), guiManager.get(p.getName()).addItemAnvilUses(preview.get(p.getName()).clone(), p));
        		complete.add(p.getName());
        		inv.removeItem(inv.getItem(28));
        		inv.removeItem(inv.getItem(34).clone());
    			for(int i : GUI.rightAnim) {
        			inv.setItem(i, GUI.redIcon);
        		}
        		for(int i : GUI.leftAnim) {
        			inv.setItem(i, GUI.redIcon);
        		} 
        	} else if(renameComplete.contains(p.getName()) == true
        			&& p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName())) >= 0) {
        		int levelCalc = p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName()));
        		p.setLevel(levelCalc);
    			inv.setItem(13, guiManager.get(p.getName()).addItemAnvilUses(inv.getItem(13).clone(), p));
        		inv.removeItem(inv.getItem(28));
        		for(int i : GUI.leftAnim) {
        			inv.setItem(i, GUI.redIcon);
        		} 
    			complete.add(p.getName());
    		}
        } else if(slot == 31 && inv.getItem(28) != null) {
        	e.setCancelled(true);
        	if(!renameComplete.contains(p.getName())) {
        		xpCost.put(p.getName(), xpCost.get(p.getName()) + 5);
        	}
        	//renameItems(p, inv.getItem(28));
        	renameComplete.remove(p.getName());
        	guiManager.get(p.getName()).renameItems(p);
    	}
    		//p.sendMessage(e.getRawSlot() + " ");
		guiManager.get(p.getName()).animation(p);      
    }
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
    	//e.setCancelled(true);
    	Player p = (Player) e.getWhoClicked();
    	
    	if(inAnvil.contains(p.getName()) == false) return;
        Inventory inv = guiManager.get(p.getName()).getInventory();

        if(e.getRawSlots().size() != 1) {
    		e.setCancelled(true);
    		return;
    	}
    	
    	Integer slot = -1;
    	for(Integer i : e.getRawSlots()) {
    		slot = i;
    	}
    	
    	ItemStack clickedItem = e.getNewItems().get(slot);
    	
    	if ((clickedItem == null || clickedItem.getType() == Material.AIR) && p.getItemOnCursor() == null) return;
    	
        // Using slots click is a best option for your inventory click's
        if(clickedItem != null) {
	        if (clickedItem.getItemMeta().hasCustomModelData()) {
	            if (clickedItem.getItemMeta().getCustomModelData() == 16473) {
	            	e.setCancelled(true);
	            	return;
	            }
	        }
        }
        
        new BukkitRunnable(){
        	@Override
        	public void run() {
        		if(storage.get(p.getName()) != null) {
		        	if((preview.get(p.getName()) == null || !storage.get(p.getName()).equals(inv.getItem(28)))) {
		        		if(inv.getItem(28) != null) {
		        			preview.put(p.getName(), storage.get(p.getName()).clone());
		        		} else {
		        			preview.remove(p.getName());
		        		}
		    			xpCost.put(p.getName(), 0);
		        		storage.put(p.getName(), inv.getItem(28));
			        	storage2.put(p.getName(), inv.getItem(34));
		        	}
		        		
	        	} else {
		    		storage.put(p.getName(), inv.getItem(28));
		        	storage2.put(p.getName(), inv.getItem(34));
		        	if(storage.get(p.getName()) != null){
		        		preview.putIfAbsent(p.getName(), storage.get(p.getName()).clone());
		        	} else {
		        		preview.remove(p.getName());
		        	}
	        	}
	        	
	        	this.cancel();
        	}
        }.runTaskLater(core, 1);
        
        if(slot == 22) { // anvil is pressed
        	e.setCancelled(true);
        	if(inv.getItem(GUI.leftAnim[0]).getType().equals(inv.getItem(GUI.rightAnim[0]).getType()) 
        			&& inv.getItem(GUI.rightAnim[0]).getType().equals(Material.GREEN_STAINED_GLASS_PANE) 
        			&& complete.contains(p.getName()) == false 
        			&& p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName())) >= 0) {
        		int levelCalc = p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName()));
        		p.setLevel(levelCalc);
    			preview.put(p.getName(), guiManager.get(p.getName()).addItemAnvilUses(preview.get(p.getName()).clone(), p));
        		complete.add(p.getName());
        		inv.removeItem(inv.getItem(28));
        		inv.removeItem(inv.getItem(34).clone());
    			for(int i : GUI.rightAnim) {
        			inv.setItem(i, GUI.redIcon);
        		}
        		for(int i : GUI.leftAnim) {
        			inv.setItem(i, GUI.redIcon);
        		} 
        	} else if(renameComplete.contains(p.getName()) == true
        			&& p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName())) >= 0) {
        		int levelCalc = p.getLevel() - (int)(0.5 + (1 + (anvilUses.get(p.getName()))/2.0) * xpCost.get(p.getName()));
        		p.setLevel(levelCalc);
    			inv.setItem(13, guiManager.get(p.getName()).addItemAnvilUses(inv.getItem(13).clone(), p));
        		inv.removeItem(inv.getItem(28));
        		for(int i : GUI.leftAnim) {
        			inv.setItem(i, GUI.redIcon);
        		} 
    			complete.add(p.getName());
    		}
        } else if(slot == 31 && inv.getItem(28) != null) {
        	e.setCancelled(true);
        	if(!renameComplete.contains(p.getName())) {
        		xpCost.put(p.getName(), xpCost.get(p.getName()) + 5);
        	}
        	//renameItems(p, inv.getItem(28));
        	renameComplete.remove(p.getName());
        	guiManager.get(p.getName()).renameItems(p);
    	}
    		//p.sendMessage(e.getRawSlot() + " ");
		guiManager.get(p.getName()).animation(p);      
    }

    private void onAnvilClose(Player p) {
			
			// Return items if inventory closed
			if(storage.get(p.getName()) != null) {
	    		p.getInventory().addItem(storage.get(p.getName()));
	    	}
	    	if(storage2.get(p.getName()) != null) {
	    		p.getInventory().addItem(storage2.get(p.getName()));
	    	}
	    	if(preview.get(p.getName()) != null && complete.contains(p.getName()) == true) {
	    		p.getInventory().addItem(preview.get(p.getName()));
	    	}
			
			// Clear temporary data when player closes anvil
			storage.remove(p.getName());
			storage2.remove(p.getName());
			preview.remove(p.getName());
			xpCost.remove(p.getName());
			complete.remove(p.getName());
			renameComplete.remove(p.getName());
			guiManager.remove(p.getName());
	}
	
	private void startListeners() {

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(core, PacketType.Play.Client.CLOSE_WINDOW) {
			@Override
			public void onPacketReceiving(PacketEvent event){
				Player p = event.getPlayer();
				if(inAnvil.contains(p.getName())) {
					inAnvil.remove(p.getName());
					onAnvilClose(p);
				}
			}
		});
	}
		
}
