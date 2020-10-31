package me.stefan923.superdiscord.utils.discord;

import FactionsTopV5.FactionsTopPlugin;
import FactionsTopV5.Objects.BlockWorth;
import FactionsTopV5.Objects.FTop;
import FactionsTopV5.Objects.ItemWorth;
import FactionsTopV5.Objects.SpawnerWorth;
import com.massivecraft.factions.Faction;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface DiscordMessageUtils {

    default void sendEmbededMessage(TextChannel textChannel, EmbedBuilder embedBuilder) {
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    default void sendMessage(long channelID, String message) {

    }

    default String replacePlaceholders(String string, MessageReceivedEvent event) {
        User user = event.getAuthor();
        return string.replace("%user_id%", user.getId())
                .replace("%user_name%", user.getName());
    }

    default String replacePlaceholders(Player player, String string) {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, string) : string;
    }

    default String replacePlaceholders(FactionsTopPlugin factionsTopPlugin, String factionID, String string) {
        FTop fTop = factionsTopPlugin.unsorted.get(factionID);

        string = string.replace("%ftop_faction%", fTop.getFactionName())
                .replace("%ftop_leader%", fTop.getLeader())
                .replace("%ftop_value%", fTop.getTotalWorthFormatted())
                .replace("%ftop_spawner_value%", fTop.getSpawnerWorthFormatted())
                .replace("%ftop_block_value%", fTop.getBlockWorthFormatted())
                .replace("%ftop_percentage_changed%", removeFormatting(fTop.getPercentageChanged()))
                .replace("%ftop_top1spawner%", fTop.getTop1SpawnerType())
                .replace("%ftop_top1spawnercount%", fTop.getTop1SpawnerCount())
                .replace("%ftop_top2spawner%", fTop.getTop2SpawnerType())
                .replace("%ftop_top2spawnercount%", fTop.getTop2SpawnerCount())
                .replace("%ftop_top3spawner%", fTop.getTop3SpawnerType())
                .replace("%ftop_top3spawnercount%", fTop.getTop3SpawnerCount())
                .replace("%ftop_top4spawner%", fTop.getTop4SpawnerType())
                .replace("%ftop_top4spawnercount%", fTop.getTop4SpawnerCount())
                .replace("%ftop_top5spawner%", fTop.getTop5SpawnerType())
                .replace("%ftop_top5spawnercount%", fTop.getTop5SpawnerCount());

        for (final BlockWorth blockworth : factionsTopPlugin.getBlockWorth()) {
            string = string.replace("%ftop_" + blockworth.getReplace1() + "%", "" + fTop.getAmountOfBlock(blockworth));
            string = string.replace("%ftop_" + blockworth.getReplace2() + "%", "" + blockworth.getValueString());
            string = string.replace("%ftop_" + blockworth.getReplace3() + "%", blockworth.getValueByAmount(fTop.getAmountOfBlock(blockworth)));
            string = string.replace("%ftop_" + blockworth.getReplace4() + "%", blockworth.getCountFormatted(fTop.getAmountOfBlock(blockworth)));
        }
        for (final SpawnerWorth spawnerworth : factionsTopPlugin.getSpawnerWorth()) {
            string = string.replace("%ftop_" + spawnerworth.getReplace1() + "%", "" + fTop.getAmountOfSpawner(spawnerworth));
            string = string.replace("%ftop_" + spawnerworth.getReplace2() + "%", spawnerworth.getValueString());
            string = string.replace("%ftop_" + spawnerworth.getReplace3() + "%", spawnerworth.getValueByAmount(fTop.getAmountOfSpawner(spawnerworth)));
            string = string.replace("%ftop_" + spawnerworth.getReplace4() + "%", spawnerworth.getCountFormatted(fTop.getAmountOfSpawner(spawnerworth)));
        }
        for (final ItemWorth itemworth : factionsTopPlugin.getItemWorth()) {
            string = string.replace("%ftop_" + itemworth.getReplace1() + "%", "" + fTop.getAmountOfItem(itemworth));
            string = string.replace("%ftop_" + itemworth.getReplace2() + "%", itemworth.getValueString());
            string = string.replace("%ftop_" + itemworth.getReplace3() + "%", itemworth.getValueByAmount(fTop.getAmountOfItem(itemworth)));
            string = string.replace("%ftop_" + itemworth.getReplace4() + "%", itemworth.getCountFormatted(fTop.getAmountOfItem(itemworth)));
        }
        fTop.getPercentageChanged();

        return string;
    }

    default String removeFormatting(String string) {
        return ChatColor.stripColor(string).replaceAll("[^\\d\\s.%+-]+", "");
    }

}
