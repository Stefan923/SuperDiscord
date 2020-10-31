package me.stefan923.superdiscord.listeners.bukkit;

import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import me.stefan923.superdiscord.utils.discord.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class PlayerCommandPreprocessListener implements Listener, DiscordMessageUtils, JSONUtils {

    private final JDA jda;

    public PlayerCommandPreprocessListener(JDA jda) {
        this.jda = jda;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        long time = System.currentTimeMillis();
        if (event.getMessage().startsWith("/helpop")) {
            String message = event.getMessage().replace("/helpop ", "");

            if (!message.equalsIgnoreCase("")) {
                TextChannel textChannel = jda.getTextChannelById(Setting.CHANNEL_IDS_HELPOP);

                if (textChannel != null) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    String playerName = event.getPlayer().getName();
                    String playerAvatar = "https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7";
                    try {
                        JSONObject jsonObject = readJsonFromUrl("https://playerdb.co/api/player/minecraft/" + playerName);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject tempObject = jsonObject.getJSONObject("data").getJSONObject("player");

                            playerAvatar = tempObject.getString("avatar");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    embedBuilder.setAuthor("HelpOp - message received", null, "https://i.imgur.com/AuCAXBr.png");
                    embedBuilder.setDescription("`" + message + "`");
                    embedBuilder.setFooter("Sent by " + playerName + " | Answered in " + (System.currentTimeMillis() - time) + " ms", playerAvatar);
                    embedBuilder.setColor(Color.decode("0x0092e2"));
                    sendEmbededMessage(textChannel, embedBuilder);
                }
            }
        }
    }

}
