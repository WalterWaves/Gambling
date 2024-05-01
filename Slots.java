package me.WalterWaves.Gambling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Slots implements Runnable, CommandExecutor, Listener {
	
	private static Map<String, Inventory> inventories = new HashMap<String, Inventory>();
	private static Map<String, Integer> isSpinning = new HashMap<String, Integer>();
	private static Map<String, Integer> spinTime = new HashMap<String, Integer>();
	
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (inventories.get(player.getName()) == null) {
				inventories.put(player.getName(), Bukkit.createInventory(player, 54,
						ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "SLOTS"));
			}
			if (isSpinning.get(player.getName()) == null) {
				isSpinning.put(player.getName(), 0);
			}
			if (spinTime.get(player.getName()) == null) {
				spinTime.put(player.getName(), 0);
			}
			List<Integer> row1 = new ArrayList<Integer>();
			List<Integer> row2 = new ArrayList<Integer>();
			List<Integer> row3 = new ArrayList<Integer>();
			List<Integer> row4 = new ArrayList<Integer>();
			List<Integer> row5 = new ArrayList<Integer>();
			List<Integer> row6 = new ArrayList<Integer>();
			List<Integer> row7 = new ArrayList<Integer>();
			List<ItemStack> items = new ArrayList<ItemStack>();
			items.add(new ItemStack(Material.DIAMOND));
			items.add(new ItemStack(Material.DIAMOND));
			items.add(new ItemStack(Material.EMERALD));
			items.add(new ItemStack(Material.EMERALD));
			items.add(new ItemStack(Material.GOLD_INGOT));
			items.add(new ItemStack(Material.GOLD_INGOT));
			items.add(new ItemStack(Material.COAL));
			items.add(new ItemStack(Material.COAL));
			items.add(new ItemStack(Material.COAL));
			items.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
			row1.add(10);
			row1.add(19);
			row1.add(28);
			row1.add(37);
			row2.add(11);
			row2.add(20);
			row2.add(29);
			row2.add(38);
			row3.add(12);
			row3.add(21);
			row3.add(30);
			row3.add(39);
			row4.add(13);
			row4.add(22);
			row4.add(31);
			row4.add(40);
			row5.add(14);
			row5.add(23);
			row5.add(32);
			row5.add(41);
			row6.add(15);
			row6.add(24);
			row6.add(33);
			row6.add(42);
			row7.add(16);
			row7.add(25);
			row7.add(34);
			row7.add(43);
			
			if (isSpinning.get(player.getName()) == 1 && spinTime.get(player.getName()) > 0) {
				int ratio = 2;
				if (spinTime.get(player.getName()) > 25 && spinTime.get(player.getName()) < 50) {
					ratio = 5;
				}
				if (spinTime.get(player.getName()) < 25) {
					ratio = 10;
				}
				if (spinTime.get(player.getName()) % ratio == 0) {
					for (int i = 2; i >= 0; i--) {
						ItemStack a = inventories.get(player.getName()).getItem(row1.get(i));
						ItemStack b = inventories.get(player.getName()).getItem(row1.get(i + 1));
						ItemStack aux = a;
						a = b;
						b = aux;
						inventories.get(player.getName()).setItem(row1.get(i), a);
						inventories.get(player.getName()).setItem(row1.get(i + 1), b);
						
						ItemStack a2 = inventories.get(player.getName()).getItem(row2.get(i));
						ItemStack b2 = inventories.get(player.getName()).getItem(row2.get(i + 1));
						ItemStack aux2 = a2;
						a2 = b2;
						b2 = aux2;
						inventories.get(player.getName()).setItem(row2.get(i), a2);
						inventories.get(player.getName()).setItem(row2.get(i + 1), b2);
						
						ItemStack a3 = inventories.get(player.getName()).getItem(row3.get(i));
						ItemStack b3 = inventories.get(player.getName()).getItem(row3.get(i + 1));
						ItemStack aux3 = a3;
						a3 = b3;
						b3 = aux3;
						inventories.get(player.getName()).setItem(row3.get(i), a3);
						inventories.get(player.getName()).setItem(row3.get(i + 1), b3);
						
						ItemStack a4 = inventories.get(player.getName()).getItem(row4.get(i));
						ItemStack b4 = inventories.get(player.getName()).getItem(row4.get(i + 1));
						ItemStack aux4 = a4;
						a4 = b4;
						b4 = aux4;
						inventories.get(player.getName()).setItem(row4.get(i), a4);
						inventories.get(player.getName()).setItem(row4.get(i + 1), b4);
						
						ItemStack a5 = inventories.get(player.getName()).getItem(row5.get(i));
						ItemStack b5 = inventories.get(player.getName()).getItem(row5.get(i + 1));
						ItemStack aux5 = a5;
						a5 = b5;
						b5 = aux5;
						inventories.get(player.getName()).setItem(row5.get(i), a5);
						inventories.get(player.getName()).setItem(row5.get(i + 1), b5);
						
						ItemStack a6 = inventories.get(player.getName()).getItem(row6.get(i));
						ItemStack b6 = inventories.get(player.getName()).getItem(row6.get(i + 1));
						ItemStack aux6 = a6;
						a6 = b6;
						b6 = aux6;
						inventories.get(player.getName()).setItem(row6.get(i), a6);
						inventories.get(player.getName()).setItem(row6.get(i + 1), b6);
						
						ItemStack a7 = inventories.get(player.getName()).getItem(row7.get(i));
						ItemStack b7 = inventories.get(player.getName()).getItem(row7.get(i + 1));
						ItemStack aux7 = a7;
						a7 = b7;
						b7 = aux7;
						inventories.get(player.getName()).setItem(row7.get(i), a7);
						inventories.get(player.getName()).setItem(row7.get(i + 1), b7);
					}
					inventories.get(player.getName()).setItem(10, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(11, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(12, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(13, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(14, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(15, items.get(Main.random(10)));
					inventories.get(player.getName()).setItem(16, items.get(Main.random(10)));
					player.updateInventory();
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 2f);
				}
				spinTime.put(player.getName(), spinTime.get(player.getName()) - 1);
				if (spinTime.get(player.getName()) == 0) {
					isSpinning.put(player.getName(), 0);
					int diamonds = 0;
					int emeralds = 0;
					int gold = 0;
					int coal = 0;
					int gapple = 0;
					int total = 0;
					for (ItemStack c : inventories.get(player.getName()).getContents()) {
						if (c.getType() == Material.DIAMOND) {
							diamonds++;
						}
						if (c.getType() == Material.EMERALD) {
							emeralds++;
						}
						if (c.getType() == Material.GOLD_INGOT) {
							gold++;
						}
						if (c.getType() == Material.COAL) {
							coal++;
						}
						if (c.getType() == Material.ENCHANTED_GOLDEN_APPLE) {
							gapple++;
						}
					}
					player.sendMessage(ChatColor.GREEN + "EMERALD (100) x" + emeralds + " = " + ChatColor.BOLD + emeralds * 100 + " Credits");
					player.sendMessage(ChatColor.AQUA + "DIAMOND (50) x" + diamonds + " = " + ChatColor.BOLD + diamonds * 50 + " Credits");
					player.sendMessage(ChatColor.GOLD + "GOLD (25) x" + gold + " = " + ChatColor.BOLD + gold * 25 + " Credits");
					if (coal > 15) {
						player.sendMessage(ChatColor.DARK_GRAY + "COAL x15 BONUS = " + ChatColor.BOLD + " +500 Credits");
						total += 500;
					}
					if (coal == 0) {
						player.sendMessage(ChatColor.DARK_GRAY + "COAL x0 BONUS = " + ChatColor.BOLD + " +5000 Credits");
						total += 5000;
					}
					if (emeralds == 0) {
						player.sendMessage(ChatColor.GREEN + "EMERALD x0 BONUS" + ChatColor.BOLD + " +500 Credits");
						total += 500;
					}
					if (diamonds == 0) {
						player.sendMessage(ChatColor.AQUA + "DIAMOND x0 BONUS" + ChatColor.BOLD + " +500 Credits");
						total += 500;
					}
					if (gold == 0) {
						player.sendMessage(ChatColor.GOLD + "GOLD x0 BONUS" + ChatColor.BOLD + " +500 Credits");
						total += 500;
					}
					if (gapple >= 7) {
						player.sendMessage(ChatColor.GOLD + "777 ENCHANTED GOLDEN APPLE BONUS 777" + ChatColor.BOLD + " +10000 Credits");
						Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + " HAS WON THE 777 JACKPOT (+10000 CREDITS)");
						total += 10000;
						player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
					}
					player.sendMessage(ChatColor.GRAY + "-----------------------------");
					total += emeralds * 100 + diamonds * 50 + gold * 25;
					player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TOTAL: " + ChatColor.AQUA + "" + ChatColor.BOLD + total);
					diamonds = 0;
					emeralds = 0;
					gold = 0;
					player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
					Main.data.getConfig().set(player.getName() + ".Credits",
							(int) Main.data.getConfig().get(player.getName() + ".Credits") + total);
					Main.data.saveConfig();
					Main.logs.getConfig()
					.set(player.getName() + "'s Logs" + ".Slots" + "." + Main.formatter.format(Main.date)
							+ ".TransactionID " + (Main.random(1000000) + 1000000),
							"Won " + total + " at Slots. Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
					Main.logs.saveConfig();
					player.sendMessage(ChatColor.GREEN + "✔ Winnings Successfully Added To Your Account! New Balance: "
							+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
				}
			}
		}
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("slots")) {
			Player player = (Player) sender;
			player.openInventory(inventories.get(player.getName()));
			ItemStack blank_space = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta blank_space_meta = blank_space.getItemMeta();
			blank_space_meta.setDisplayName(" ");
			blank_space.setItemMeta(blank_space_meta);
			ItemStack spin = new ItemStack(Material.TRIPWIRE_HOOK);
			ItemMeta spin_meta = spin.getItemMeta();
			spin_meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN!");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + "Cost: 1000 Credits");
			spin_meta.setLore(lore);
			spin.setItemMeta(spin_meta);
			for (int i = 0; i <= 53; i++) {
				inventories.get(player.getName()).setItem(i, blank_space);
			}
			inventories.get(player.getName()).setItem(49, spin);
		}
		return false;
	}
	
	@EventHandler
	public void slotsInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() != null && isSpinning.get(player.getName()) == 0
				&& event.getCurrentItem().getType() == Material.TRIPWIRE_HOOK && event.getCurrentItem().getItemMeta()
						.getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN!")) {
			if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= 1000) {
				Main.data.getConfig().set(player.getName() + ".Credits",
						(int) Main.data.getConfig().get(player.getName() + ".Credits") - 1000);
				Main.data.saveConfig();
				Main.logs.getConfig()
				.set(player.getName() + "'s Logs" + ".Slots" + "." + Main.formatter.format(Main.date)
						+ ".TransactionID " + (Main.random(1000000) + 1000000),
						"Spun for 1000 Credits. Balance: "
								+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
				Main.logs.saveConfig();
				isSpinning.put(player.getName(), 1);
				spinTime.put(player.getName(), 100 + Main.random(150));
				player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f);
				player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! New Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
			}
		}
	}
}
