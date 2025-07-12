package su.rumishistem.bungeediscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class DiscordBot {
	public static JDA Bot = null;
	
	public static void Init() throws InterruptedException {
		JDABuilder Builder = JDABuilder.createDefault(BungeeDiscordBot.CONFIG_DATA.get("DISCORD").getData("TOKEN").asString());
		Builder.enableIntents(
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_MEMBERS
		);
		
		Builder.setRawEventsEnabled(true);
		Builder.setEventPassthrough(true);
		//Builder.addEventListeners(new DiscordEventListener());
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
