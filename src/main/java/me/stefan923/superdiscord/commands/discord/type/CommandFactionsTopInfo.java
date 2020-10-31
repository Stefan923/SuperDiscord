package me.stefan923.superdiscord.commands.discord.type;

import FactionsTopV5.FactionsTopPlugin;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;

import java.awt.*;

public class CommandFactionsTopInfo extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {

    private final FactionsTopPlugin factionsTopPlugin;

    public CommandFactionsTopInfo() {
        super(null, true, "ftop");

        factionsTopPlugin = (FactionsTopPlugin) Bukkit.getServer().getPluginManager().getPlugin("FactionsTop");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        final Faction faction = Factions.getInstance().getByTag(args[0]);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("SuperDiscord Factions - faction info", null, "https://i.imgur.com/3kLXEoy.png");
        embedBuilder.setDescription(replacePlaceholders(factionsTopPlugin, faction.getId(), Language.FACTIONS_TOP_INFO));
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
        return Setting.COMMAND_PREFIX + "ftop";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
