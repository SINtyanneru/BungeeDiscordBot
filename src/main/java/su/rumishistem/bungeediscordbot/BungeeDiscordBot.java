package su.rumishistem.bungeediscordbot;

import java.io.FileOutputStream;
import java.nio.file.*;

import com.fasterxml.jackson.databind.*;

import net.md_5.bungee.api.plugin.Plugin;
import su.rumishistem.rumi_java_lib.*;

public class BungeeDiscordBot extends Plugin{
	public static Path PluginDirPath = Path.of("./plugins/BungeeDiscordBot/");
	public static ArrayNode CONFIG_DATA = null;
	public static JsonNode ServerNameTable;

	@Override
	public void onEnable() {
		try {
			getLogger().info("Nha!");

			//設定フォルダを作成
			if (!Files.exists(PluginDirPath)) {
				Files.createDirectories(PluginDirPath);
			}

			//設定ファイルを作成
			if (!Files.exists(PluginDirPath.resolve("Config.ini"))) {
				Files.createFile(PluginDirPath.resolve("Config.ini"));

				FileOutputStream FOS = new FileOutputStream(PluginDirPath.resolve("Config.ini").toFile());
				FOS.write("[DISCORD]\n".getBytes());
				FOS.write("TOKEN=\"TOKENをここに\"\n".getBytes());
				FOS.write("CHANNEL=\"チャンネルのIDをここに(SnowFlake)\"\n".getBytes());
				FOS.write("\n".getBytes());
				FOS.write("[MESSAGE]\n".getBytes());
				FOS.write("JOIN=\"$NAMEが参加しました！\"\n".getBytes());
				FOS.write("LEFT=\"$NAMEが退出しました\"\n".getBytes());
				FOS.write("SWITCH=\"$NAMEが$TOへ移動しました\"\n".getBytes());
				FOS.flush();
				FOS.close();
			}
			if (!Files.exists(PluginDirPath.resolve("ServerName.json"))) {
				Files.createFile(PluginDirPath.resolve("ServerName.json"));

				FileOutputStream FOS = new FileOutputStream(PluginDirPath.resolve("ServerName.json").toFile());
				FOS.write("{}".getBytes());
				FOS.flush();
				FOS.close();
			}

			getLogger().info("Init!");

			//設定とかを読み込み
			CONFIG_DATA = new ConfigLoader(PluginDirPath.resolve("Config.ini").toFile()).DATA;
			getLogger().info("Load Config.ini");

			ServerNameTable = new ObjectMapper().readTree(PluginDirPath.resolve("ServerName.json").toFile());

			DiscordBot.Init();
			getLogger().info("DiscordBot Ready!");

			getProxy().getPluginManager().registerListener(this, new BungeeEvent());

			DiscordBot.SendMessage("サーバーが起動しました");
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
