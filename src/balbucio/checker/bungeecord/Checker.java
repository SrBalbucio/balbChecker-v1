package balbucio.checker.bungeecord;

import org.json.JSONObject;
import balbucio.checker.bungeecord.UpdateRunnable;
import balbucio.checker.common.JsonReader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class Checker {

	private String plugin;
	private String description;
	private double version;
	private Plugin main;
	private double lastestVersion;
	private boolean isPremium = false;
	private boolean isUpdated = false;

	public Checker(String plugin, double version, Plugin main) {
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
			ProxyServer.getInstance().getConsole().sendMessage("§c[UPDATER] §a[" + plugin + "] §c>> §cNão foi possível checar se há uma nova atualização!");
			return false;
		}
		this.lastestVersion = json.getDouble("version");
		this.description = json.getString("description");
		return lastestVersion > version;
	}

	public void startUpdater() {
		this.isUpdated = true;
		ProxyServer.getInstance().getScheduler().runAsync(main,
				new UpdateRunnable(plugin, lastestVersion, main.getFile()));
	}

	@SuppressWarnings("deprecation")
	public void checkKey(Configuration config) {
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
				ProxyServer.getInstance().getConsole().sendMessage("§c[KEY] §a[" + plugin + "] §f>> §cNão conseguimos validar a sua Key!");
				ProxyServer.getInstance().getConsole().sendMessage("§c[KEY] §a[" + plugin + "] §f>> §cCaso acredite que isso é um erro, entre em contato com nosso suporte!");
			} else {
				ProxyServer.getInstance().getConsole().sendMessage("§c[KEY] §a[" + plugin + "] §cf>> §2Perfeito! Seu plugin foi ativado com sucesso!");
				isPremium = true;
			}
		}
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}
	
	public boolean isPremium() {
		return isPremium;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
