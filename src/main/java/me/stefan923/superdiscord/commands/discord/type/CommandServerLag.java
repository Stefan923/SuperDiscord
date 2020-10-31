package me.stefan923.superdiscord.commands.discord.type;

import com.earth2me.essentials.Essentials;
import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import me.stefan923.superdiscord.utils.discord.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Level;

public class CommandServerLag extends AbstractCommand implements DiscordMessageUtils, ConversionUtils, JSONUtils {

    Essentials essentials;

    public CommandServerLag(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "lag");

        essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Server statistics - runtime", null, "https://i.imgur.com/qIQee8z.png");
        String string = Language.SERVER_LAG.replace("%time%", convertLongToString(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()))
                .replace("%cur_tps%", formatDouble(essentials.getTimer().getAverageTPS()))
                .replace("%max_mem%", (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB")
                .replace("%alloc_mem%", (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB")
                .replace("%free_mem%", (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB");

        StringBuilder worldPlaceholder = new StringBuilder();
        final List<World> worlds = Bukkit.getServer().getWorlds();
        for (final World w : worlds) {
            String worldType = "World";
            switch (w.getEnvironment()) {
                case NETHER:
                    worldType = "Nether";
                    break;
                case THE_END:
                    worldType = "The End";
                    break;
            }

            final int[] tileEntities = {0};

            try {
                Bukkit.getScheduler().runTask(instance, () -> {
                    for (final Chunk chunk : w.getLoadedChunks()) {
                        tileEntities[0] += chunk.getTileEntities().length;
                    }
                });
            } catch (final java.lang.ClassCastException ex) {
                Bukkit.getLogger().log(Level.SEVERE, "Corrupted chunk data on world " + w, ex);
            }

            worldPlaceholder.append(worldType)
                    .append(" \"**")
                    .append(w.getName())
                    .append("**\": `")
                    .append(w.getLoadedChunks().length)
                    .append("` loaded chunks, `")
                    .append(w.getEntities().size())
                    .append("` entities, `")
                    .append(tileEntities[0])
                    .append("` tile entities\n");
        }

        embedBuilder.setDescription(string.replace("%worlds%", worldPlaceholder.toString()));
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
        return "**" + Setting.COMMAND_PREFIX + "server lag**\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
