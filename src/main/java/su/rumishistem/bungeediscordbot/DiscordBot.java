package su.rumishistem.bungeediscordbot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import su.rumishistem.rumi_java_lib.ArrayNode;

public class DiscordBot {
	private static ArrayNode MessageConfig = BungeeDiscordBot.CONFIG_DATA.get("MESSAGE");
	public static JDA Bot = null;
	
	public static void Init() throws InterruptedException {
		JDABuilder Builder = JDABuilder.createDefault(BungeeDiscordBot.CONFIG_DATA.get("DISCORD").getData("TOKEN").asString());
		Builder.enableIntents(
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_MEMBERS,
			GatewayIntent.MESSAGE_CONTENT
		);
		
		Builder.setRawEventsEnabled(true);
		Builder.setEventPassthrough(true);
		Builder.addEventListeners(new ListenerAdapter() {
			@Override
			public void onMessageReceived(MessageReceivedEvent e) {
				if (e.getAuthor().isBot()) return;
				if (!BungeeDiscordBot.CONFIG_DATA.get("DISCORD").getData("CHANNEL").asString().contains(e.getChannel().getId())) return;

				String Text = MessageConfig.getData("DISCORD_MESSAGE").asString()
					.replace("$SERVER", e.getGuild().getName())
					.replace("$CHANNEL", e.getChannel().getName())
					.replace("$NAME", e.getAuthor().getEffectiveName())
					.replace("$TEXT", e.getMessage().getContentRaw());

				for (ProxiedPlayer Player:ProxyServer.getInstance().getPlayers()) {
					Player.sendMessage(Text);
				}
			}
		});
		Builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		Builder.setAutoReconnect(true);

		//ステータス
		Builder.setStatus(OnlineStatus.ONLINE);

		Bot = Builder.build();
		Bot.awaitReady();
	}

	public static void SendMessage(String Text) {
		String[] ChannelID = BungeeDiscordBot.CONFIG_DATA.get("DISCORD").getData("CHANNEL").asString().split(",");
		for (String ID:ChannelID) {
			TextChannel Channel = DiscordBot.Bot.getTextChannelById(ID);
			if (Channel != null) {
				Channel.sendMessage(Text).queue();
			}
		}
	}
}
