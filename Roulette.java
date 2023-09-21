package me.WalterWaves.Gambling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
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

public class Roulette implements CommandExecutor, Listener, Runnable {

	private static Map<String, Inventory> inventories = new HashMap<String, Inventory>();
	private static Map<String, Integer> isSpinning = new HashMap<String, Integer>();
	private static Map<String, Integer> spinTime = new HashMap<String, Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("credits")) {
			sender.sendMessage(ChatColor.AQUA + "You have " + ChatColor.GREEN
					+ Main.data.getConfig().get(player.getName() + ".Credits") + ChatColor.AQUA
					+ " credits available.");
		}
		if (label.equalsIgnoreCase("roulette")) {
			ItemStack blank_space_black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemStack blank_space_red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemStack selected_reward = new ItemStack(Material.HOPPER);
			ItemStack spin_left = new ItemStack(Material.EMERALD_BLOCK);
			ItemStack spin_right = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta blank_space_black_meta = blank_space_black.getItemMeta();
			ItemMeta blank_space_red_meta = blank_space_red.getItemMeta();
			ItemMeta selected_reward_meta = selected_reward.getItemMeta();
			ItemMeta spin_left_meta = spin_left.getItemMeta();
			ItemMeta spin_right_meta = spin_right.getItemMeta();
			blank_space_black_meta.setDisplayName(" ");
			blank_space_red_meta.setDisplayName(" ");
			selected_reward_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "YOUR PRIZE");
			spin_left_meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN TO LEFT");
			spin_right_meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN TO RIGHT");
			List<String> spin_lore = new ArrayList<String>();
			spin_lore.add(ChatColor.GREEN + "Cost: 100 Credits");
			spin_left_meta.setLore(spin_lore);
			spin_right_meta.setLore(spin_lore);
			blank_space_black.setItemMeta(blank_space_black_meta);
			blank_space_red.setItemMeta(blank_space_red_meta);
			selected_reward.setItemMeta(selected_reward_meta);
			spin_left.setItemMeta(spin_left_meta);
			spin_right.setItemMeta(spin_right_meta);
			for (int i = 0; i <= 52; i += 2) {
				inventories.get(player.getName()).setItem(i, blank_space_black);
			}
			for (int i = 1; i <= 53; i += 2) {
				inventories.get(player.getName()).setItem(i, blank_space_red);
			}
			inventories.get(player.getName()).setItem(4, selected_reward);
			inventories.get(player.getName()).setItem(26, spin_right);
			inventories.get(player.getName()).setItem(35, spin_right);
			inventories.get(player.getName()).setItem(18, spin_left);
			inventories.get(player.getName()).setItem(27, spin_left);
			inventories.get(player.getName()).setItem(13, new ItemStack(Material.DIAMOND));
			inventories.get(player.getName()).setItem(12, new ItemStack(Material.EMERALD));
			inventories.get(player.getName()).setItem(14, new ItemStack(Material.COAL));
			inventories.get(player.getName()).setItem(24, new ItemStack(Material.REDSTONE));
			inventories.get(player.getName()).setItem(20, new ItemStack(Material.IRON_INGOT));
			inventories.get(player.getName()).setItem(33, new ItemStack(Material.LAPIS_LAZULI));
			inventories.get(player.getName()).setItem(29, new ItemStack(Material.GOLD_INGOT));
			inventories.get(player.getName()).setItem(38, new ItemStack(Material.QUARTZ));
			inventories.get(player.getName()).setItem(42, new ItemStack(Material.GOLDEN_APPLE));
			inventories.get(player.getName()).setItem(48, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
			inventories.get(player.getName()).setItem(49, new ItemStack(Material.TOTEM_OF_UNDYING));
			inventories.get(player.getName()).setItem(50, new ItemStack(Material.COPPER_INGOT));
			player.openInventory(inventories.get(player.getName()));
		}
		return false;
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (Main.data.getConfig().get(player.getName() + ".Credits") == null) {
				Main.data.getConfig().set(player.getName() + ".Credits", 0);
				Main.data.saveConfig();
			}

			if (inventories.get(player.getName()) == null) {
				inventories.put(player.getName(), Bukkit.createInventory(player, 54,
						ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "ROULETTE"));
			}
			if (isSpinning.get(player.getName()) == null) {
				isSpinning.put(player.getName(), 0);
			}
			if (spinTime.get(player.getName()) == null) {
				spinTime.put(player.getName(), 0);
			}
			List<Integer> roulette_items = new ArrayList<Integer>();
			roulette_items.add(13);
			roulette_items.add(14);
			roulette_items.add(24);
			roulette_items.add(33);
			roulette_items.add(42);
			roulette_items.add(50);
			roulette_items.add(49);
			roulette_items.add(48);
			roulette_items.add(38);
			roulette_items.add(29);
			roulette_items.add(20);
			roulette_items.add(12);
			if (isSpinning.get(player.getName()) == 1 && spinTime.get(player.getName()) > 0) {
				int ratio = 1;
				if (spinTime.get(player.getName()) < 150 && spinTime.get(player.getName()) > 100) {
					ratio = 2;
				}
				if (spinTime.get(player.getName()) < 100 && spinTime.get(player.getName()) > 50) {
					ratio = 5;
				}
				if (spinTime.get(player.getName()) < 50) {
					ratio = 10;
				}
				if (spinTime.get(player.getName()) % ratio == 0) {
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 2f);
					for (int i = 0; i <= 10; i++) {
						ItemStack a = inventories.get(player.getName()).getItem(roulette_items.get(i));
						ItemStack b = inventories.get(player.getName()).getItem(roulette_items.get(i + 1));
						ItemStack aux = a;
						a = b;
						b = aux;
						inventories.get(player.getName()).setItem(roulette_items.get(i), a);
						inventories.get(player.getName()).setItem(roulette_items.get(i + 1), b);
					}
					player.updateInventory();
				}
				spinTime.put(player.getName(), spinTime.get(player.getName()) - 1);
				if (spinTime.get(player.getName()) == 0) {
					player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
					player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
					player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().add(0, 1, 0), 250, 0.5, 0.5, 0.5, 0.25);
					player.getInventory().addItem(inventories.get(player.getName()).getItem(13));
					isSpinning.put(player.getName(), 0);
				}
			}

			if (isSpinning.get(player.getName()) == 2 && spinTime.get(player.getName()) > 0) {
				int ratio = 1;
				if (spinTime.get(player.getName()) < 150 && spinTime.get(player.getName()) > 100) {
					ratio = 2;
				}
				if (spinTime.get(player.getName()) < 100 && spinTime.get(player.getName()) > 50) {
					ratio = 5;
				}
				if (spinTime.get(player.getName()) < 50) {
					ratio = 10;
				}
				if (spinTime.get(player.getName()) % ratio == 0) {
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 2f);
					for (int i = 10; i >= 0; i--) {
						ItemStack a = inventories.get(player.getName()).getItem(roulette_items.get(i));
						ItemStack b = inventories.get(player.getName()).getItem(roulette_items.get(i + 1));
						ItemStack aux = a;
						a = b;
						b = aux;
						inventories.get(player.getName()).setItem(roulette_items.get(i), a);
						inventories.get(player.getName()).setItem(roulette_items.get(i + 1), b);
					}
					player.updateInventory();
				}
				spinTime.put(player.getName(), spinTime.get(player.getName()) - 1);
				if (spinTime.get(player.getName()) == 0) {
					player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
					player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
					player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().add(0, 1, 0), 250, 0.5, 0.5, 0.5, 0.25);
					player.getInventory().addItem(inventories.get(player.getName()).getItem(13));
					isSpinning.put(player.getName(), 0);
				}
			}
		}
	}

	@EventHandler
	public void rouletteInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() != null && isSpinning.get(player.getName()) == 0
				&& event.getCurrentItem().getType() == Material.EMERALD_BLOCK && event.getCurrentItem().getItemMeta()
						.getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN TO LEFT")) {
			if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= 100) {
				Main.data.getConfig().set(player.getName() + ".Credits",
						(int) Main.data.getConfig().get(player.getName() + ".Credits") - 100);
				Main.data.saveConfig();
				player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f);
				player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! New Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + ".");
				isSpinning.put(player.getName(), 1);
				spinTime.put(player.getName(), 200 + Main.random(150));
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + ".");
			}
		}
		if (event.getCurrentItem() != null && isSpinning.get(player.getName()) == 0
				&& event.getCurrentItem().getType() == Material.EMERALD_BLOCK && event.getCurrentItem().getItemMeta()
						.getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "SPIN TO RIGHT")) {
			if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= 100) {
				Main.data.getConfig().set(player.getName() + ".Credits",
						(int) Main.data.getConfig().get(player.getName() + ".Credits") - 100);
				Main.data.saveConfig();
				player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f);
				player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! New Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + ".");
				isSpinning.put(player.getName(), 2);
				spinTime.put(player.getName(), 200 + Main.random(150));
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
						+ Main.data.getConfig().get(player.getName() + ".Credits") + ".");
			}
		}
		for (ItemStack item : event.getInventory().getContents()) {
			if (event.getCurrentItem() != null && item.getType() == Material.HOPPER && item.getItemMeta()
					.getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "YOUR PRIZE")) {
				event.setCancelled(true);
			}
		}
	}
}
