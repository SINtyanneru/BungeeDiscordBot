package su.rumishistem.bungeediscordbot;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import su.rumishistem.rumi_java_lib.ArrayNode;

public class BungeeEvent implements Listener{
	private static ArrayNode MessageConfig = BungeeDiscordBot.CONFIG_DATA.get("MESSAGE");

	@EventHandler
	public void onPlayerLogin(PostLoginEvent e) {
		ProxiedPlayer Player = e.getPlayer();

		String Text = MessageConfig.getData("JOIN").asString();
		Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));

		DiscordBot.SendMessage(Text);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer Player = e.getPlayer();

		String Text = MessageConfig.getData("LEFT").asString();
		Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));

		DiscordBot.SendMessage(Text);
	}

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent e) {
		ProxiedPlayer Player = e.getPlayer();
		ServerInfo From = e.getFrom();
		ServerInfo To = Player.getServer().getInfo();

		if (From != null && !From.getName().equals(To.getName())) {
			String Text = MessageConfig.getData("SWITCH").asString();
			Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));
			Text = Text.replace("$FROM", ResolveServerName(From.getName()).replace("@", "[@]"));
			Text = Text.replace("$TO", ResolveServerName(To.getName()).replace("@", "[@]"));

			DiscordBot.SendMessage(Text);
		}
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) return;

		ProxiedPlayer Player = (ProxiedPlayer) e.getSender();
		String Name = Player.getDisplayName().replace("@", "[@]");
		String Message = e.getMessage().replace("@", "[@]");

		if (!e.isCommand()) {
			String Text = MessageConfig.getData("GAME_CHAT").asString();
			Text = Text.replace("$NAME", Name);
			Text = Text.replace("$TEXT", Message);
			DiscordBot.SendMessage(Text);
		}
	}

	private String ResolveServerName(String ServerName) {
		if (BungeeDiscordBot.ServerNameTable.get(ServerName) != null && !BungeeDiscordBot.ServerNameTable.get(ServerName).isNull()) {
			return BungeeDiscordBot.ServerNameTable.get(ServerName).asText();
		} else {
			return ServerName;
		}
	}
}
