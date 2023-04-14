package balbucio.checker.spigot;

import java.io.File;

import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import balbucio.checker.common.JsonReader;
import balbucio.checker.spigot.UpdateRunnable;

public class Checker {

	private String plugin;
	private String description;
	private double version;
	private JavaPlugin main;
	private double lastestVersion;
	private boolean isPremium = false;
	private boolean isUpdated = false;

	public Checker(String plugin, double version, JavaPlugin main) {
		this.plugin = plugin;
		this.version = version;
		this.main = main;
	}
	
	public String getDetailsPage() {
		return "https://plugins.balbucio.xyz/details.jsp?plugin="+plugin;
	}

	public boolean hasNewVersion() {
		JSONObject json = JsonReader.readJsonFromUrl("https://plugins.balbucio.xyz/license/update?plugin=" + plugin);
		if (json.getBoolean("error")) {
			Bukkit.getConsoleSender().sendMessage("§c[KEY] §a[" + plugin + "] §f>> §cNão foi possível checar se há uma nova atualização!");
			return false;
		}
		this.lastestVersion = json.getDouble("version");
		this.description = json.getString("description");
		return lastestVersion > version;
	}

	public void startUpdater() {
		this.isUpdated = true;
		Bukkit.getScheduler().runTaskAsynchronously(main,
				new UpdateRunnable(plugin, lastestVersion, new File("plugins", plugin + "-" + lastestVersion)));
	}

	public void checkKey(YamlConfiguration config) {
		boolean isActive = config.getBoolean("key.active");
		String chave = config.getString("key.chave");
		String email = config.getString("key.email");
		if (isActive) {
			JSONObject json = JsonReader.readJsonFromUrl("https://plugins.balbucio.xyz/license/check?plugin=" + plugin
					+ "&key=" + chave + "&email=" + email);

			if (json.getBoolean("error")) {
				return;
			}

			if (!json.getBoolean("active")) {
				Bukkit.getConsoleSender()
						.sendMessage("§c[KEY] §a[" + plugin + "] §f>> §cNão conseguimos validar a sua Key!");
				Bukkit.getConsoleSender().sendMessage("§c[KEY] §a[" + plugin
						+ "] §f>> §cCaso acredite que isso é um erro, entre em contato com nosso suporte!");
				Bukkit.getPluginManager().disablePlugin(main);
			} else {
				Bukkit.getConsoleSender()
						.sendMessage("§c[KEY] §a[" + plugin + "] §f>> §2Perfeito! Seu plugin foi ativado com sucesso!");
				isPremium = true;
			}
		}
	}

	public void checkKey(String email, String chave){
		JSONObject json = JsonReader.readJsonFromUrl("https://plugins.balbucio.xyz/license/check?plugin=" + plugin
				+ "&key=" + chave + "&email=" + email);

		if (json.getBoolean("error")) {
			return;
		}

		if (!json.getBoolean("active")) {
			Bukkit.getConsoleSender()
					.sendMessage("§c[KEY] §a[" + plugin + "] §f>> §cNão conseguimos validar a sua Key!");
			Bukkit.getConsoleSender().sendMessage("§c[KEY] §a[" + plugin
					+ "] §f>> §cCaso acredite que isso é um erro, entre em contato com nosso suporte!");
			Bukkit.getPluginManager().disablePlugin(main);
		} else {
			Bukkit.getConsoleSender()
					.sendMessage("§c[KEY] §a[" + plugin + "] §f>> §2Perfeito! Seu plugin foi ativado com sucesso!");
			isPremium = true;
		}
	}

	public void customCheckKey(String email, String chave, String plugin){
		JSONObject json = JsonReader.readJsonFromUrl("https://plugins.balbucio.xyz/license/check?plugin=" + plugin
				+ "&key=" + chave + "&email=" + email);

		if (json.getBoolean("error")) {
			return;
		}

		if (!json.getBoolean("active")) {
			Bukkit.getConsoleSender()
					.sendMessage("§c[KEY] §a[" + this.plugin + "] §f>> §cNão conseguimos validar a sua Key!");
			Bukkit.getConsoleSender().sendMessage("§c[KEY] §a[" + this.plugin
					+ "] §f>> §cCaso acredite que isso é um erro, entre em contato com nosso suporte!");
			Bukkit.getPluginManager().disablePlugin(main);
		} else {
			Bukkit.getConsoleSender()
					.sendMessage("§c[KEY] §a[" + this.plugin + "] §f>> §2Perfeito! Seu plugin foi ativado com sucesso!");
			isPremium = true;
		}
	}

	public void sendMsg(String msg){
		Bukkit.getConsoleSender()
				.sendMessage("§c[KEY] §a[" + this.plugin + "] §f>> "+msg);
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}
	
	public boolean isPremium() {
		return isPremium;
	}
}
