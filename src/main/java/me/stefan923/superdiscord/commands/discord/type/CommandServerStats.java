package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import me.stefan923.superdiscord.utils.discord.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandServerStats extends AbstractCommand implements DiscordMessageUtils, ConversionUtils, JSONUtils {

    public CommandServerStats(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "stats");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) throws SQLException {
        if (args.length < 2) {
            return ReturnType.SYNTAX_ERROR;
        }
        if (!args[1].equalsIgnoreCase("version")) {
            return ReturnType.SYNTAX_ERROR;
        }

        long time = 0;
        for (int i = 2; i < args.length; i++) {
            time += convertStringToLong(args[i]);
        }

        time = System.currentTimeMillis() - time;

        EmbedBuilder embedBuilder = setStats(instance, time);
        embedBuilder.setAuthor("Server statistics - version", null, "https://i.imgur.com/qIQee8z.png");
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
        sendEmbededMessage(event.getTextChannel(), embedBuilder);

        return ReturnType.SUCCESS;
    }

    public EmbedBuilder setStats(SuperDiscord instance, long time) throws SQLException {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String link = "https://quickchart.io/chart?c=%7B%22type%22%3A%22outlabeledPie%22%2C%22data%22%3A%7B%22labels%22%3A%5B%221.8%22%2C%221.9%22%2C%221.10%22%2C%221.11%22%2C%221.12%22%2C%221.13%22%2C%221.14%22%2C%221.15%22%2C%221.16%22%5D%2C%22datasets%22%3A%5B%7B%22data%22%3A%5B%2518%25%2C%2519%25%2C%25110%25%2C%25111%25%2C%25112%25%2C%25113%25%2C%25114%25%2C%25115%25%2C%25116%25%5D%7D%5D%7D%2C%22options%22%3A%7B%22plugins%22%3A%7B%22legend%22%3Afalse%2C%22outlabels%22%3A%7B%22text%22%3A%22%25l%20%25p%22%2C%22color%22%3A%22white%22%2C%22stretch%22%3A35%2C%22font%22%3A%7B%22resizable%22%3Atrue%2C%22minSize%22%3A13%2C%22maxSize%22%3A18%7D%7D%7D%7D%7D";

        String query = "SELECT `version`, COUNT(DISTINCT `player_key`) AS `p_count` FROM `ultimateafk_logs` WHERE `quit_time`>='" + time + "' GROUP BY `version`;";
        PreparedStatement preparedStatement = instance.getUltimateAfkDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String version = resultSet.getString("version");
            long count = resultSet.getLong("p_count");
            embedBuilder.addField(version + " players", String.valueOf(count), true);
            link = link.replace("%25" + version.replace(".", "") + "%25", String.valueOf(count));
        }

        link = link.replace("%2518%25", String.valueOf(0))
                .replace("%2519%25", String.valueOf(0))
                .replace("%25110%25", String.valueOf(0))
                .replace("%25111%25", String.valueOf(0))
                .replace("%25112%25", String.valueOf(0))
                .replace("%25113%25", String.valueOf(0))
                .replace("%25114%25", String.valueOf(0))
                .replace("%25115%25", String.valueOf(0))
                .replace("%25116%25", String.valueOf(0));
        embedBuilder.setImage(link);

        resultSet.close();
        preparedStatement.close();
        return embedBuilder;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "server stats version [duration]**\n\n***duration***  must be of type: `14d 2h 10m`\n `y - year` `mo - month` `w - week` `d - day` `h - hour` `m - minute` `s - second`\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
