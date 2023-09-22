package me.WalterWaves.Gambling;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Raffle implements CommandExecutor, Runnable {

	private static Map<String, Integer> bets = new HashMap<String, Integer>();
	private static Map<String, Float> chances = new HashMap<String, Float>();
	private static int raffle_time = 120;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("raffle")) {
			if (args.length == 0) {
				if (!bets.isEmpty()) {
					player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "RAFFLE");
					player.sendMessage("");
					for (String key : bets.keySet()) {
						player.sendMessage(ChatColor.GREEN + "Player: " + ChatColor.AQUA + key + ChatColor.GREEN
								+ " Bet: " + ChatColor.AQUA + bets.get(key) + ChatColor.GREEN + " Chance: "
								+ ChatColor.AQUA + String.format("%.2f", chances.get(key)) + "%");
					}
					player.sendMessage("");
					player.sendMessage(ChatColor.GREEN + "Use " + ChatColor.AQUA + "/raffle join <bet>"
							+ ChatColor.GREEN + " to join the raffle!");
				} else {
					player.sendMessage(ChatColor.AQUA + "Nobody joined the raffle yet!");
					player.sendMessage(ChatColor.GREEN + "Be the first one to do so by using " + ChatColor.AQUA
							+ "/raffle join <bet>" + ChatColor.GREEN + " to join the raffle!");
				}
			}
			if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
				if (!bets.containsKey(player.getName())) {
					if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer.parseInt(args[1])
							&& Integer.parseInt(args[1]) > 0) {
						Main.data.getConfig().set(player.getName() + ".Credits",
								(int) Main.data.getConfig().get(player.getName() + ".Credits")
										- Integer.parseInt(args[1]));
						Main.data.saveConfig();
						player.sendMessage(ChatColor.GREEN + "✔ You Have Successfully Joined The Raffle! Bet: "
								+ ChatColor.AQUA + args[1] + " Credits.");
						bets.put(player.getName(), Integer.parseInt(args[1]));
						int totalValue = 0;
						for (int value : bets.values()) {
							totalValue += value;
						}
						for (String key : bets.keySet()) {
							chances.put(key, (float) bets.get(key) / totalValue * 100);
						}
					} else {
						player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits Or Invalid Amount! Balance: "
								+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "⚠ You Already Joined This Raffle!");
				}
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("end")) {
				if (player.isOp()) {
					player.sendMessage(ChatColor.GREEN + "You have ended the raffle.");
					raffle_time = 120;
					if (!bets.isEmpty()) {
						for (String key : bets.keySet()) {
							if ((float) (chances.get(key) - 100) * -1 < Main.random(100)) {
								int totalValue = 0;
								for (int value : bets.values()) {
									totalValue += value;
								}
								Main.data.getConfig().set(key + ".Credits",
										(int) Main.data.getConfig().get(key + ".Credits") + totalValue);
								Main.data.saveConfig();
								Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Winner Is: " + ChatColor.AQUA + ""
										+ ChatColor.BOLD + key + ChatColor.GREEN + " For A Total Of " + ChatColor.AQUA
										+ "" + ChatColor.BOLD + totalValue + ChatColor.GREEN + " Credits!");
								Bukkit.getPlayerExact(key).sendMessage(ChatColor.GREEN
										+ "✔ You Won The Raffle's Grand Total Of: " + ChatColor.AQUA + totalValue
										+ " Credits" + ChatColor.GREEN + ". New Balance: " + ChatColor.AQUA
										+ Main.data.getConfig().get(key + ".Credits") + ChatColor.GREEN + " Credits.");
								Bukkit.getPlayerExact(key).playSound(Bukkit.getPlayerExact(key),
										Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
								Bukkit.getPlayerExact(key).getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
										Bukkit.getPlayerExact(key).getLocation().add(0, 1, 0), 250, 0.5, 0.5, 0.5,
										0.25);
								bets.clear();
								chances.clear();
								break;
							}
						}
					} else {
						Bukkit.broadcastMessage(
								ChatColor.RED + "Raffle Ended But Nobody Participated In The Current One.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		if (raffle_time == 119) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "A New Raffle Has Started.");
		}
		if (raffle_time == 60) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + "60"
					+ ChatColor.GREEN + " Seconds.");
		}
		if (raffle_time == 30) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + "30"
					+ ChatColor.GREEN + " Seconds.");
		}
		if (raffle_time == 10) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + "10"
					+ ChatColor.GREEN + " Seconds.");
		}
		if (raffle_time == 3) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + raffle_time
					+ ChatColor.GREEN + " Seconds.");
		}
		if (raffle_time == 2) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + raffle_time
					+ ChatColor.GREEN + " Seconds.");
		}
		if (raffle_time == 1) {
			Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Is Ending In " + ChatColor.AQUA + raffle_time
					+ ChatColor.GREEN + " Second.");
		}
		if (raffle_time <= 0) {
			raffle_time = 120;
			if (!bets.isEmpty()) {
				for (String key : bets.keySet()) {
					if ((float) (chances.get(key) - 100) * -1 < Main.random(100)) {
						int totalValue = 0;
						for (int value : bets.values()) {
							totalValue += value;
						}
						Main.data.getConfig().set(key + ".Credits",
								(int) Main.data.getConfig().get(key + ".Credits") + totalValue);
						Main.data.saveConfig();
						Bukkit.broadcastMessage(ChatColor.GREEN + "The Raffle Winner Is: " + ChatColor.AQUA + ""
								+ ChatColor.BOLD + key + ChatColor.GREEN + " For A Total Of " + ChatColor.AQUA + ""
								+ ChatColor.BOLD + totalValue + ChatColor.GREEN + " Credits!");
						Bukkit.getPlayerExact(key)
								.sendMessage(ChatColor.GREEN + "✔ You Won The Raffle's Grand Total Of: "
										+ ChatColor.AQUA + totalValue + " Credits" + ChatColor.GREEN + ". New Balance: "
										+ ChatColor.AQUA + Main.data.getConfig().get(key + ".Credits") + ChatColor.GREEN
										+ " Credits.");
						Bukkit.getPlayerExact(key).playSound(Bukkit.getPlayerExact(key),
								Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
						Bukkit.getPlayerExact(key).getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
								Bukkit.getPlayerExact(key).getLocation().add(0, 1, 0), 250, 0.5, 0.5, 0.5, 0.25);
						bets.clear();
						chances.clear();
						break;
					}
				}
			} else {
				Bukkit.broadcastMessage(ChatColor.RED + "Nobody Participated In The Current Raffle.");
			}
		}
		raffle_time--;
	}

}
