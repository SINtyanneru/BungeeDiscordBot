package su.rumishistem.bungeediscordbot;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeEvent implements Listener{
	@EventHandler
	public void onPlayerLogin(PostLoginEvent e) {
		ProxiedPlayer Player = e.getPlayer();

		String Text = BungeeDiscordBot.CONFIG_DATA.get("MESSAGE").getData("JOIN").asString();
		Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));

		DiscordBot.SendMessage(Text);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer Player = e.getPlayer();

		String Text = BungeeDiscordBot.CONFIG_DATA.get("MESSAGE").getData("LEFT").asString();
		Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));

		DiscordBot.SendMessage(Text);
	}

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent e) {
		ProxiedPlayer Player = e.getPlayer();
		ServerInfo From = e.getFrom();
		ServerInfo To = Player.getServer().getInfo();

		if (From != null && !From.getName().equals(To.getName())) {
			String Text = BungeeDiscordBot.CONFIG_DATA.get("MESSAGE").getData("SWITCH").asString();
			Text = Text.replace("$NAME", Player.getDisplayName().replace("@", "[@]"));
			Text = Text.replace("$FROM", ResolveServerName(From.getName()).replace("@", "[@]"));
			Text = Text.replace("$TO", ResolveServerName(To.getName()).replace("@", "[@]"));

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
