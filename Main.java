package me.WalterWaves.Gambling;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.WalterWaves.Gambling.Files.DataManager;

public class Main extends JavaPlugin {
	
	public static DataManager data;
	
	@Override
	public void onEnable() {
		data = new DataManager(this);
		getCommand("roulette").setExecutor(new Roulette());
		getCommand("credits").setExecutor(new Roulette());
		getServer().getPluginManager().registerEvents(new Roulette(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Roulette(), 0, 1);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				data.reloadConfig();
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
