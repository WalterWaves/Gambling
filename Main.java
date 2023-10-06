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
					+ Main.data.getConfig().get(player.getName() + ".Credits") + ChatColor.AQUA
					+ " credits available.");
		}
		return false;
	}

	@Override
	public void onEnable() {
		data = new DataManager(this);
		logs = new LogsManager(this);
		getCommand("roulette").setExecutor(new Roulette());
		getCommand("dice").setExecutor(new Dice());
		getCommand("double").setExecutor(new Double());
		getCommand("raffle").setExecutor(new Raffle());
		getCommand("shop").setExecutor(new Shop());
		getServer().getPluginManager().registerEvents(new Roulette(), this);
		getServer().getPluginManager().registerEvents(new Shop(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Roulette(), 0, 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Raffle(), 0, 20);

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
