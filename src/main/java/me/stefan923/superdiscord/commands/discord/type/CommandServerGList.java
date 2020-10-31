package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandServerGList extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandServerGList(AbstractCommand abstractCommand) {
        super(abstractCommand, false, "glist");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Server statistics - global list", null, "https://i.imgur.com/qIQee8z.png");
        embedBuilder.setDescription(replacePlaceholders(null, Language.SERVER_GLIST));
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
        sendEmbededMessage(event.getTextChannel(), embedBuilder);

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return Setting.COMMAND_PREFIX + "server glist";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
