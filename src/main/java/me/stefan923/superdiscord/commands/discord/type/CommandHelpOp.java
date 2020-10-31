package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.bukkit.MessageUtils;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;

public class CommandHelpOp extends AbstractCommand implements DiscordMessageUtils, ConversionUtils, MessageUtils {
    public CommandHelpOp() {
        super(null, true, "helpop");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length > 1) {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Player Not Online - Error", null, "https://i.imgur.com/EmNLVFm.png");
                embedBuilder.setDescription(Language.MUST_BE_ONLINE);
                embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
                embedBuilder.setColor(Color.decode("0x0092e2"));
                sendEmbededMessage(event.getTextChannel(), embedBuilder);

                return ReturnType.FAILURE;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                stringBuilder.append(args[i]).append(" ");
            }

            player.sendMessage(formatAll(Language.FORMAT_HELPOP.replace("%message%", stringBuilder.toString())));

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("HelpOp - message sent", null, "https://i.imgur.com/AuCAXBr.png");
            embedBuilder.setDescription("`" + stringBuilder.toString() + "`");
            embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
            embedBuilder.setColor(Color.decode("0x0092e2"));
            sendEmbededMessage(event.getTextChannel(), embedBuilder);

            return ReturnType.SUCCESS;
        }

        return ReturnType.SYNTAX_ERROR;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "helpop <player> <message>**\n\n***message***  must be replaced with a message you want to send to all online players.";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
