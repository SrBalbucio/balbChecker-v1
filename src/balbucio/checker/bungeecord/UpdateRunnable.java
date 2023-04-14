package balbucio.checker.bungeecord;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import net.md_5.bungee.api.ProxyServer;

public class UpdateRunnable implements Runnable {

	private double version;
	private File originalFile;
	private String plugin;

	public UpdateRunnable(String plugin, double version, File originalFile) {
		this.plugin = plugin;
		this.version = version;
		this.originalFile = originalFile;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		ProxyServer.getInstance().getConsole().sendMessage("§d[UPDATER] §a["+plugin+"] §f>> §aUma atualização do Plugin foi encontrada e está sendo baixada! §7[v"+version+"]");
		try (BufferedInputStream in = new BufferedInputStream(new URL("https://plugins.balbucio.xyz/plugins/download?plugin="+plugin+"&version="+version).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(originalFile)) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ProxyServer.getInstance().getConsole().sendMessage("§d[UPDATER] §a["+plugin+"] §f>> §aO plugin foi atualizado com sucesso");
		}
	}

}
