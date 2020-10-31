package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.exceptions.MissingPermissionException;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import me.stefan923.superdiscord.utils.discord.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandLitebansCount extends AbstractCommand implements DiscordMessageUtils, ConversionUtils, JSONUtils {
    public CommandLitebansCount(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "count");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) throws MissingPermissionException, SQLException {
        if (args.length < 3)  {
            return ReturnType.SYNTAX_ERROR;
        }
        if (!args[1].equalsIgnoreCase("bans") && !args[1].equalsIgnoreCase("mutes")
                && !args[1].equalsIgnoreCase("kicks") && !args[1].equalsIgnoreCase("warnings")) {
            return ReturnType.SYNTAX_ERROR;
        }

        long time = 0;
        for (int i = 3; i < args.length; i++) {
            time += convertStringToLong(args[i]);
        }

        String description = Language.GIVEN_PUNISHMENTS;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(sender.getName());
        try {
            JSONObject jsonObject = readJsonFromUrl("https://playerdb.co/api/player/minecraft/" + args[2]);
            if (jsonObject.getBoolean("success")) {
                JSONObject tempObject = jsonObject.getJSONObject("data").getJSONObject("player");

                String avatar = tempObject.getString("avatar");
                String username = tempObject.getString("username");

                description = description.replace("%player%", username);
                embedBuilder.setAuthor(username, null, avatar);
                embedBuilder.setThumbnail(avatar);
            } else {
                description = description.replace("%player%", args[2]);
                embedBuilder.setAuthor(args[2], null, "https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
                embedBuilder.setThumbnail("https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            }
        } catch (IOException e) {
            description = description.replace("%player%", args[2]);
            embedBuilder.setAuthor(args[1], null, "https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            embedBuilder.setThumbnail("https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            e.printStackTrace();
        }
        embedBuilder.setDescription(description.replace("%type%", StringUtils.substring(args[1], 0, args[1].length() - 1))
                .replace("%count%", String.valueOf(getPunishmentsCountBy(instance, args[1].toLowerCase(), args[2], time))));
        embedBuilder.addField("Active " + args[1], String.valueOf(getActivePunishmentsCountBy(instance, args[1], args[2], time)), true);
        embedBuilder.addField("IP " + args[1], String.valueOf(getIpPunishmentsCountBy(instance, args[1], args[2], time)), true);
        embedBuilder.addField("Silent " + args[1], String.valueOf(getSilentPunishmentsCountBy(instance, args[1], args[2], time)), true);
        embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
        embedBuilder.setColor(Color.decode("0x0092e2"));
        sendEmbededMessage(event.getTextChannel(), embedBuilder);

        return ReturnType.SUCCESS;
    }

    public long getPunishmentsCountBy(SuperDiscord instance, String table, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + table + "` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (System.currentTimeMillis() - time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getActivePunishmentsCountBy(SuperDiscord instance, String table, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + table + "` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (System.currentTimeMillis() - time) + "' AND `active`='1';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getIpPunishmentsCountBy(SuperDiscord instance, String table, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + table + "` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (System.currentTimeMillis() - time) + "' AND `ipban`='1';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getSilentPunishmentsCountBy(SuperDiscord instance, String table, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + table + "` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (System.currentTimeMillis() - time) + "' AND `silent`='1';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getPunishmentsCount(SuperDiscord instance, String table, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + table + "` WHERE `time`>='" + (System.currentTimeMillis() - time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "litebans count <bans|mutes|kicks|warnings> <player> [duration]**\n\n***player***  must be replaced with player's name or `*`.\n***duration***  must be of type: `14d 2h 10m`\n `y - year` `mo - month` `w - week` `d - day` `h - hour` `m - minute` `s - second`\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
