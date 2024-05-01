package me.WalterWaves.Gambling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.WalterWaves.Gambling.Files.DataManager;
import me.WalterWaves.Gambling.Files.LogsManager;

public class Main extends JavaPlugin {

	public static DataManager data;
	public static LogsManager logs;

	public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static Date date = new Date();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("credits") || label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("bal")) {
			sender.sendMessage(ChatColor.AQUA + "You have " + ChatColor.GREEN
					+ "" + ChatColor.BOLD + Main.data.getConfig().get(player.getName() + ".Credits") + ChatColor.GREEN + " Credits"
					+ ChatColor.AQUA + " available.");
		}
		int keysAmount = 0;
		int[] amounts = new int[11];
		String[] names = new String[11];
		if (label.equalsIgnoreCase("baltop") || label.equalsIgnoreCase("balancetop")
				|| label.equalsIgnoreCase("creditstop")) {
			sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Balance Top");
			sender.sendMessage("");
			for (String keys : Main.data.getConfig().getKeys(false)) {
				keysAmount++;
				if (keysAmount > 10) {
					keysAmount = 10;
				}
				amounts[keysAmount] = Main.data.getConfig().getInt(keys + ".Credits");
				names[keysAmount] = keys;
			}
			for (int i = 1; i < keysAmount; i++) {
				for (int j = i + 1; j <= keysAmount; j++) {
					if (amounts[i] < amounts[j]) {
						int aux;
						String saux;
						aux = amounts[i];
						amounts[i] = amounts[j];
						amounts[j] = aux;
						saux = names[i];
						names[i] = names[j];
						names[j] = saux;
					}
				}
			}
			for (int i = 1; i <= keysAmount; i++) {
				sender.sendMessage("" + ChatColor.GOLD + i + ". " + ChatColor.AQUA + names[i] + " " + ChatColor.GREEN
						+ "" + ChatColor.BOLD + amounts[i] + ChatColor.GREEN + " Credits.");
			}
		}
		return false;
	}

	@Override
	public void onEnable() {
		data = new DataManager(this);
		logs = new LogsManager(this);
		getCommand("roulette").setExecutor(new Roulette());
		getCommand("slots").setExecutor(new Slots());
		getCommand("dice").setExecutor(new Dice());
		getCommand("double").setExecutor(new Double());
		getCommand("raffle").setExecutor(new Raffle());
		getCommand("shop").setExecutor(new Shop());
		getCommand("pay").setExecutor(new Pay());
		getCommand("blackjack").setExecutor(new Blackjack());
		getCommand("bj").setExecutor(new Blackjack());
		getServer().getPluginManager().registerEvents(new Roulette(), this);
		getServer().getPluginManager().registerEvents(new Slots(), this);
		getServer().getPluginManager().registerEvents(new Shop(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Roulette(), 0, 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Slots(), 0, 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Raffle(), 0, 20);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Blackjack(), 0, 1);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				data.reloadConfig();
				logs.reloadConfig();

				formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				date = new Date();
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (Main.data.getConfig().get(player.getName() + ".Credits") == null) {
						Main.data.getConfig().set(player.getName() + ".Credits", 0);
						Main.data.saveConfig();
					}
				}
			}
		}, 0, 1);
	}

	@Override
	public void onDisable() {

	}

	public static Integer random(Integer max) {
		Random ran = new Random();
		return ran.nextInt(max);
	}
}
