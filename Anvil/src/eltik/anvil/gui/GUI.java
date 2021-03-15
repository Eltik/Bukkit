package eltik.anvil.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;

public class GUI {
	private Inventory inv;
	private Core core;
	private Player p;
	
	// ----- GUI icons ------
	
	public static ItemStack redIcon = new ItemStack(Material.RED_STAINED_GLASS_PANE);
	
	private ItemStack greenIcon = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
	
	private ItemStack glassIcon = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

	
	// ----- GUI animation arrays ------
	
	public static final int[] leftAnim = {10, 11, 12, 19};
	public static final int[] rightAnim = {14, 15, 16, 25};
	
	
	// ------- GUI --------	
	public GUI(Player p, Core core) {
		this.core = core;
		this.p = p;
		/*this.p = p;*/
		inv = Bukkit.createInventory(null, 54, "Anvil");
		
		ItemMeta redMeta = redIcon.getItemMeta();

		
		ItemMeta greenMeta = greenIcon.getItemMeta();

    	ItemMeta itemMeta = glassIcon.getItemMeta();
    	itemMeta.setDisplayName(" ");
    	itemMeta.setCustomModelData(16473);
    	glassIcon.setItemMeta(itemMeta);
    	
    	
    	redMeta.setDisplayName(" ");
    	redMeta.setCustomModelData(16473);
    	redIcon.setItemMeta(redMeta);
    	
    	
    	greenMeta.setDisplayName(" ");
    	greenMeta.setCustomModelData(16473);
    	greenIcon.setItemMeta(greenMeta);
    	
    	ItemStack finishIcon = new ItemStack(Material.ANVIL);
    	ItemMeta finishMeta = finishIcon.getItemMeta();
    	finishMeta.setDisplayName(ChatColor.GREEN + "Combine");
    	finishMeta.setCustomModelData(2101);
    	finishIcon.setItemMeta(finishMeta);
    	
    	ItemStack renameIcon = new ItemStack(Material.OAK_SIGN);
    	ItemMeta renameMeta = renameIcon.getItemMeta();
    	renameMeta.setDisplayName(ChatColor.WHITE + "Rename");
    	renameMeta.setCustomModelData(2100);
    	renameIcon.setItemMeta(renameMeta);
    	
    	inv.setItem(0, glassIcon);
    	inv.setItem(1, glassIcon);
    	inv.setItem(2, glassIcon);
    	inv.setItem(3, glassIcon);
    	inv.setItem(4, glassIcon);
    	inv.setItem(5, glassIcon);
    	inv.setItem(6, glassIcon);
    	inv.setItem(7, glassIcon);
    	inv.setItem(8, glassIcon);
    	inv.setItem(9, glassIcon);
    	inv.setItem(10, redIcon);
    	inv.setItem(11, redIcon);
    	inv.setItem(12, redIcon);
    	inv.setItem(13, glassIcon);
    	inv.setItem(14, redIcon);
    	inv.setItem(15, redIcon);
    	inv.setItem(16, redIcon);
    	inv.setItem(17, glassIcon);
    	inv.setItem(18, glassIcon);
    	inv.setItem(19, redIcon);
    	inv.setItem(20, glassIcon);
    	inv.setItem(21, glassIcon);
    	inv.setItem(22, finishIcon);
    	inv.setItem(23, glassIcon);
    	inv.setItem(24, glassIcon);
    	inv.setItem(25, redIcon);
    	inv.setItem(26, glassIcon);
    	inv.setItem(27, glassIcon);
    	inv.setItem(29, glassIcon);
    	inv.setItem(30, glassIcon);
    	inv.setItem(31, renameIcon);
    	inv.setItem(32, glassIcon);
    	inv.setItem(33, glassIcon);
    	inv.setItem(35, glassIcon);
    	inv.setItem(36, glassIcon);
    	inv.setItem(37, glassIcon);
    	inv.setItem(38, glassIcon);
    	inv.setItem(39, glassIcon);
    	inv.setItem(40, glassIcon);
    	inv.setItem(41, glassIcon);
    	inv.setItem(42, glassIcon);
    	inv.setItem(43, glassIcon);
    	inv.setItem(44, glassIcon);
    	inv.setItem(45, glassIcon);
    	inv.setItem(46, glassIcon);
    	inv.setItem(47, glassIcon);
    	inv.setItem(48, glassIcon);
    	inv.setItem(49, glassIcon);
    	inv.setItem(50, glassIcon);
    	inv.setItem(51, glassIcon);
    	inv.setItem(52, glassIcon);
    	inv.setItem(53, glassIcon);
	}
	
	public void openInventory() {
    	p.openInventory(inv);
	}
	public Inventory getInventory() {
		return inv;
	}
	// Manage the animation and creation of a preview item
    public void animation(Player p){
	    new BukkitRunnable() {
			@Override
			public void run() {
				if(inv.getItem(28) != null && inv.getItem(34) != null) {
		        	if((inv.getItem(34).getType().equals(Material.ENCHANTED_BOOK) && !inv.getItem(28).getType().equals(Material.ENCHANTED_BOOK))) {
		        		EnchantmentStorageMeta book = (EnchantmentStorageMeta) inv.getItem(34).getItemMeta();
		        		boolean canEnchant = false;
		        		boolean conflict = false;
		        		for(Enchantment e : book.getStoredEnchants().keySet()) {
		        			if(e.canEnchantItem(inv.getItem(28))) {
		        				canEnchant = true;
		        				if(inv.getItem(28).getItemMeta().hasEnchants()) {
		        					ItemMeta firstItem = inv.getItem(28).getItemMeta();
		        					int conflicts = 0;
		        					for(Enchantment e2 : firstItem.getEnchants().keySet()) {
		        						if(e.conflictsWith(e2)) conflicts++;
		        					}
		        					if(conflicts == 0) conflict = true;
		        				} else conflict = true;
		        			}
		        		}
		        		if(canEnchant && conflict) {
			        		for(int i : rightAnim) {
			        			inv.setItem(i, greenIcon);
			        		}
			        		for(int i : leftAnim) {
			        			inv.setItem(i, greenIcon);
			        		}
		        		} else {
		        			for(int i : rightAnim) {
			        			inv.setItem(i, redIcon);
			        		}
			        		for(int i : leftAnim) {
			        			inv.setItem(i, redIcon);
			        		}
		        		}
		        	} else if(inv.getItem(34).getType().equals(inv.getItem(28).getType())) {
		        		if(inv.getItem(34).getType().equals(Material.ENCHANTED_BOOK)) {
			        		boolean conflict = false;
			        		EnchantmentStorageMeta book2 = (EnchantmentStorageMeta) inv.getItem(34).getItemMeta();
			        		EnchantmentStorageMeta book = (EnchantmentStorageMeta) inv.getItem(28).getItemMeta();
			        		for(Enchantment e : book2.getStoredEnchants().keySet()) {
		        				if(book.hasStoredEnchants()) {
		        					int conflicts = 0;
		        					for(Enchantment e2 : book.getStoredEnchants().keySet()) {
		        						if(e.conflictsWith(e2)) conflicts++;
		        					}
		        					if(conflicts == 0) conflict = true;
		        				} else conflict = true;
			        		}
			        		if(conflict) {
				        		for(int i : rightAnim) {
				        			inv.setItem(i, greenIcon);
				        		}
				        		for(int i : leftAnim) {
				        			inv.setItem(i, greenIcon);
				        		}
			        		} else {
			        			for(int i : rightAnim) {
				        			inv.setItem(i, redIcon);
				        		}
				        		for(int i : leftAnim) {
				        			inv.setItem(i, redIcon);
				        		}
			        		}
		        		} else {
			        		boolean canEnchant = false;
			        		boolean conflict = false;
			        		if(inv.getItem(34).getItemMeta().hasEnchants()) {
			        			ItemMeta secondMeta = inv.getItem(34).getItemMeta();
				        		for(Enchantment e : secondMeta.getEnchants().keySet()) {
				        			if(e.canEnchantItem(inv.getItem(28))) {
				        				canEnchant = true;
				        				if(inv.getItem(28).getItemMeta().hasEnchants()) {
				        					ItemMeta firstMeta = inv.getItem(28).getItemMeta();
				        					int conflicts = 0;
				        					for(Enchantment e2 : firstMeta.getEnchants().keySet()) {
				        						if(e.conflictsWith(e2)) conflicts ++;
				        					}
				        					if(conflicts == 0) conflict = true;
				        				} else conflict = true;
				        			}
				        		}
				        		if(canEnchant && conflict) {
					        		for(int i : rightAnim) {
					        			inv.setItem(i, greenIcon);
					        		}
					        		for(int i : leftAnim) {
					        			inv.setItem(i, greenIcon);
					        		}
				        		} else {
				        			for(int i : rightAnim) {
					        			inv.setItem(i, redIcon);
					        		}
					        		for(int i : leftAnim) {
					        			inv.setItem(i, redIcon);
					        		}
				        		}
			        		} else {
			        			for(int i : rightAnim) {
				        			inv.setItem(i, greenIcon);
				        		}
				        		for(int i : leftAnim) {
				        			inv.setItem(i, greenIcon);
				        		}
			        		}
		        		}
		        	} else {
		        		for(int i : rightAnim) {
		        			inv.setItem(i, redIcon);
		        		}
		        		for(int i : leftAnim) {
		        			inv.setItem(i, redIcon);
		        		}
		        	}
				} else {
					if(inv.getItem(28) != null) {
			    		for(int i : leftAnim) {
			        		inv.setItem(i, greenIcon);
			    		}
			    	} else {
			    		for(int i : leftAnim) {
			        		inv.setItem(i, redIcon);
			    		}
			    	}
			    	if(inv.getItem(34) != null) {
			    		for(int i : rightAnim) {
			    			inv.setItem(i, greenIcon);
			    		}
			    	} else {
			    		for(int i : rightAnim) {
			        		inv.setItem(i, redIcon);
			    		}
			    	}
		        }
				if(inv.getItem(leftAnim[0]).getType().equals(inv.getItem(rightAnim[0]).getType()) && inv.getItem(rightAnim[0]).getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
	        		combineItems(p);
	        	} else if(inv.getItem(leftAnim[0]).getType().equals(Material.GREEN_STAINED_GLASS_PANE) && PlayerListenres.preview.get(p.getName()) != null) {
	        		NBTItem nbtiPr  = new NBTItem(PlayerListenres.preview.get(p.getName()));
	        		PlayerListenres.preview.put(p.getName(), nbtiPr.getItem());
	        		PlayerListenres.anvilUses.put(p.getName(), nbtiPr.getInteger("ANVIL_USES"));
	        		
	        		ItemStack finishIcon = inv.getItem(22);
	        		ItemMeta meta = finishIcon.getItemMeta();
	        		meta.setLore(Arrays.asList("", "XP COST: " + (int)(0.5 + (PlayerListenres.anvilUses.get(p.getName()) / 2.0 + 1) * PlayerListenres.xpCost.get(p.getName()))));
	        		finishIcon.setItemMeta(meta);
	        		inv.setItem(13, PlayerListenres.preview.get(p.getName()));
	        	}
				else if(PlayerListenres.complete.contains(p.getName()) == false){
	        		inv.setItem(13, glassIcon);
	        	}
				this.cancel();
			}
		}.runTaskLater(core.INSTANCE, 1);
    }


	// ------- Anvil functions -------
	
	// Main anvil function
	public void combineItems(Player p) {
		
		ItemStack firstItem = inv.getItem(28);
		ItemStack secondItem = inv.getItem(34);
		ItemStack pre = inv.getItem(13);
		ItemStack finishIcon = inv.getItem(22);
		if(pre == null) return;
        PlayerListenres.xpCost.put(p.getName(), 0);		
		if (firstItem.getType().equals(secondItem.getType()) && !secondItem.getType().equals(Material.ENCHANTED_BOOK)) {
			pre.setItemMeta(enchant(firstItem, p, firstItem.getItemMeta(), secondItem.getItemMeta()));
		} else if(!firstItem.getType().equals(Material.ENCHANTED_BOOK) && secondItem.getType().equals(Material.ENCHANTED_BOOK)) {
			pre.setItemMeta(enchant(firstItem, p, firstItem.getItemMeta(), (EnchantmentStorageMeta)secondItem.getItemMeta()));
		} else if(firstItem.getType().equals(secondItem.getType())) {
			pre.setItemMeta(enchant(firstItem, p, (EnchantmentStorageMeta)firstItem.getItemMeta(), (EnchantmentStorageMeta)secondItem.getItemMeta()));
		} else return;
		
		NBTItem nbtiPr  = new NBTItem(pre);
		PlayerListenres.anvilUses.put(p.getName(), nbtiPr.getInteger("ANVIL_USES"));
		pre = nbtiPr.getItem();
		ItemMeta meta = finishIcon.getItemMeta();
		meta.setLore(Arrays.asList("", "XP COST: " + (int)(0.5 + (1 + (PlayerListenres.anvilUses.get(p.getName()))/2.0) * PlayerListenres.xpCost.get(p.getName()))));
		finishIcon.setItemMeta(meta);
		PlayerListenres.preview.put(p.getName(), pre);
		inv.setItem(13, pre);
//		p.closeInventory();
		//p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 10);
		//p.sendMessage(ChatColor.GREEN + "Successfully combined items!");
	}
	
public void combineItems(String p) {
		
		ItemStack firstItem = inv.getItem(28);
		ItemStack secondItem = inv.getItem(34);
		ItemStack pre = inv.getItem(13);
		ItemStack finishIcon = inv.getItem(22);
		if(pre == null) return;
        PlayerListenres.xpCost.put(p, 0);		
		if(firstItem.getType().equals(secondItem.getType())) {
			pre.setItemMeta(enchant(firstItem, p, (EnchantmentStorageMeta)firstItem.getItemMeta(), (EnchantmentStorageMeta)secondItem.getItemMeta()));
		} else return;
		
		NBTItem nbtiPr  = new NBTItem(pre);
		PlayerListenres.anvilUses.put(p, nbtiPr.getInteger("ANVIL_USES"));
		pre = nbtiPr.getItem();
		ItemMeta meta = finishIcon.getItemMeta();
		meta.setLore(Arrays.asList("", "XP COST: " + (Math.round((1 + (float)(PlayerListenres.anvilUses.get(p))/2.0 + 1) * PlayerListenres.xpCost.get(p)))));
		finishIcon.setItemMeta(meta);
		PlayerListenres.preview.put(p, pre);
		inv.setItem(13, pre);
//		p.closeInventory();
		//p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 10);
		//p.sendMessage(ChatColor.GREEN + "Successfully combined items!");
	}

	// Add the anvil uses at the end
    ItemStack addItemAnvilUses(ItemStack s, Player p) {
    	NBTItem nbti = new NBTItem(s);
    	PlayerListenres.anvilUses.put(p.getName(), PlayerListenres.anvilUses.get(p.getName()) + 1);
    	nbti.setInteger("ANVIL_USES", PlayerListenres.anvilUses.get(p.getName()));
    	return nbti.getItem();
    }

	// Enchant overloading to support books
	ItemMeta enchant(ItemStack s, Player player, ItemMeta firstMeta, ItemMeta secondMeta) {
		//p.sendMessage("e");
		for(Enchantment i : secondMeta.getEnchants().keySet()){
			//p.sendMessage("ee");
			if(firstMeta.hasEnchant(i)){
				//p.sendMessage("eee");
				//core.anvilLog("[" + format.format(now) + "] Second item has the enchantment " + i.toString());
				int secondLvl = secondMeta.getEnchantLevel(i);
				int firstLvl = firstMeta.getEnchantLevel(i);
				if((secondLvl == firstLvl) && (firstLvl < i.getMaxLevel())){
					if(i.isTreasure() == true) {
			        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (25 * firstLvl));
					} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (10 * firstLvl));

					firstMeta.addEnchant(i, firstLvl += 1, false);
				} else{
					if(i.isTreasure() == true) {
			        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * Math.max(firstLvl, secondLvl));
					} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * Math.max(firstLvl, secondLvl));
					firstMeta.addEnchant(i, Math.max(secondLvl, firstLvl), true);
				}
				
			} else {
				//p.sendMessage("eeeee");
				if(i.canEnchantItem(s)) {
					int k = 0;
					for(Enchantment e : firstMeta.getEnchants().keySet()) {
						if(i.conflictsWith(e)) {
							k++;
						}
					}
					if(k == 0) {
						//p.sendMessage("eeee");
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * secondMeta.getEnchantLevel(i));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * secondMeta.getEnchantLevel(i));
						firstMeta.addEnchant(i, secondMeta.getEnchantLevel(i), true);
					}
				}
			}
		}
		//firstMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&o&f" + firstMeta.getDisplayName().toString()));
		return firstMeta;
	}
	EnchantmentStorageMeta enchant(ItemStack s, Player player, EnchantmentStorageMeta firstMeta, EnchantmentStorageMeta secondMeta) {
			//p.sendMessage("e");
			for(Enchantment i : secondMeta.getStoredEnchants().keySet()){
				//p.sendMessage("ee");
				if(firstMeta.hasStoredEnchant(i)){
					//p.sendMessage("eee");
					//core.anvilLog("[" + format.format(now) + "] Second item has the enchantment " + i.toString());
					int secondLvl = secondMeta.getStoredEnchantLevel(i);
					int firstLvl = firstMeta.getStoredEnchantLevel(i);
					if((secondLvl == firstLvl) && (firstLvl < i.getMaxLevel())){
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (25 * firstLvl));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (10 * firstLvl));
						firstMeta.addStoredEnchant(i, firstLvl += 1, false);
					} else{
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * Math.max(firstLvl, secondLvl));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * Math.max(firstLvl, secondLvl));
						firstMeta.addStoredEnchant(i, Math.max(secondLvl, firstLvl), true);
					}
					
				} else {
					int k = 0;
					for(Enchantment e : firstMeta.getStoredEnchants().keySet()) {
						if(i.conflictsWith(e)) {
							k++;
						}
					}
					if(k == 0) {
						//p.sendMessage("eeee");
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * secondMeta.getEnchantLevel(i));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * secondMeta.getEnchantLevel(i));
						firstMeta.addStoredEnchant(i, secondMeta.getStoredEnchantLevel(i), true);
					}
				}
			}
			//firstMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&o&f" + firstMeta.getDisplayName().toString()));
			return firstMeta;
		}
	ItemMeta enchant(ItemStack s, Player player, ItemMeta firstMeta, EnchantmentStorageMeta secondMeta) {
			//p.sendMessage("e");
			for(Enchantment i : secondMeta.getStoredEnchants().keySet()){
				//p.sendMessage("ee");
				if(firstMeta.hasEnchant(i)){
					//p.sendMessage("eee");
					//core.anvilLog("[" + format.format(now) + "] Second item has the enchantment " + i.toString());
					int secondLvl = secondMeta.getStoredEnchantLevel(i);
					int firstLvl = firstMeta.getEnchantLevel(i);
					if((secondLvl == firstLvl) && (firstLvl < i.getMaxLevel())){
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (25 * firstLvl));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + (10 * firstLvl));
						firstMeta.addEnchant(i, firstLvl += 1, false);
					} else{
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * Math.max(firstLvl, secondLvl));
						} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * Math.max(firstLvl, secondLvl));
						firstMeta.addEnchant(i, Math.max(secondLvl, firstLvl), true);
					}
					
				} else {
					if(i.canEnchantItem(s)) {
						int k = 0;
						for(Enchantment e : firstMeta.getEnchants().keySet()) {
							if(i.conflictsWith(e)) {
								k++;
							}
						}
						if(k == 0) {
							//p.sendMessage("eeee");
							if(i.isTreasure() == true) {
					        	PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 25 * secondMeta.getEnchantLevel(i));
							} else PlayerListenres.xpCost.put(player.getName(), PlayerListenres.xpCost.get(player.getName()) + 10 * secondMeta.getEnchantLevel(i));
							firstMeta.addEnchant(i, secondMeta.getStoredEnchantLevel(i), true);
						}
					}
				}
			}
			//firstMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&o&f" + firstMeta.getDisplayName().toString()));
			return firstMeta;
		}
	ItemMeta enchant(ItemStack s, String player, ItemMeta firstMeta, EnchantmentStorageMeta secondMeta) {
		//p.sendMessage("e");
		for(Enchantment i : secondMeta.getStoredEnchants().keySet()){
			//p.sendMessage("ee");
			if(firstMeta.hasEnchant(i)){
				//p.sendMessage("eee");
				//core.anvilLog("[" + format.format(now) + "] Second item has the enchantment " + i.toString());
				int secondLvl = secondMeta.getStoredEnchantLevel(i);
				int firstLvl = firstMeta.getEnchantLevel(i);
				if((secondLvl == firstLvl) && (firstLvl < i.getMaxLevel())){
					if(i.isTreasure() == true) {
			        	PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + (25 * firstLvl));
					} else PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + (10 * firstLvl));
					firstMeta.addEnchant(i, firstLvl += 1, false);
				} else{
					if(i.isTreasure() == true) {
			        	PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + 25 * Math.max(firstLvl, secondLvl));
					} else PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + 10 * Math.max(firstLvl, secondLvl));
					firstMeta.addEnchant(i, Math.max(secondLvl, firstLvl), true);
				}
				
			} else {
				if(i.canEnchantItem(s)) {
					int k = 0;
					for(Enchantment e : firstMeta.getEnchants().keySet()) {
						if(i.conflictsWith(e)) {
							k++;
						}
					}
					if(k == 0) {
						//p.sendMessage("eeee");
						if(i.isTreasure() == true) {
				        	PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + 25 * secondMeta.getEnchantLevel(i));
						} else PlayerListenres.xpCost.put(player, PlayerListenres.xpCost.get(player) + 10 * secondMeta.getEnchantLevel(i));
						firstMeta.addEnchant(i, secondMeta.getStoredEnchantLevel(i), true);
					}
				}
			}
		}
		//firstMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&o&f" + firstMeta.getDisplayName().toString()));
		return firstMeta;
	}

	
    // ------- Renaming -------
    
    // Reopen anvil menu after renaming menu closes
    void reopenAfterRename(Player p) {
    	new BukkitRunnable() {

			@Override
			public void run() {
				openInventory();
		    	inv.setItem(28, PlayerListenres.storage.get(p.getName()));
		    	inv.setItem(34, PlayerListenres.storage2.get(p.getName()));
		    	animation(p);
		    	PlayerListenres.renameComplete.add(p.getName());
	    		this.cancel();
			}
    	}.runTaskLater(core.INSTANCE, 1);
    }
    
    // Main renaming function
    void renameItems(Player player) {
    	SignMenuFactory signMenuFactory = new SignMenuFactory(core.INSTANCE);
    	signMenuFactory
        .newMenu(Arrays.asList("&o&f" + PlayerListenres.preview.get(player.getName()).getItemMeta().getDisplayName(), "---------------", "Set new display", "name"))
        .response((p, lines) -> {
            if(lines[0] != null) {
            	ItemMeta meta = PlayerListenres.preview.get(player.getName()).getItemMeta();
            	meta.setDisplayName(lines[0]);
            	ItemStack s = PlayerListenres.preview.get(player.getName());
            	s.setItemMeta(meta);
            	PlayerListenres.preview.put(player.getName(), s);
            	reopenAfterRename(player);
            	return true;
            }
            return false; // failure. becaues reopenIfFail was called, menu will reopen when closed.
        })
        .open(player);
	 }
    
    
    
}
