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
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandLitebansStaffinfo extends AbstractCommand implements DiscordMessageUtils, ConversionUtils, JSONUtils {

    public CommandLitebansStaffinfo(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "staffinfo");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) throws SQLException {
        if (args.length < 2) {
            return ReturnType.SYNTAX_ERROR;
        }

        long time = 0;
        for (int i = 2; i < args.length; i++) {
            time += convertStringToLong(args[i]);
        }

        time = System.currentTimeMillis() - time;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            JSONObject jsonObject = readJsonFromUrl("https://playerdb.co/api/player/minecraft/" + args[1]);
            if (jsonObject.getBoolean("success")) {
                JSONObject tempObject = jsonObject.getJSONObject("data").getJSONObject("player");
                embedBuilder.setAuthor(tempObject.getString("username"), null, tempObject.getString("avatar"));
                embedBuilder.setThumbnail(tempObject.getString("avatar"));
            } else {
                embedBuilder.setAuthor(args[1], null, "https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
                embedBuilder.setThumbnail("https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            }
        } catch (IOException e) {
            embedBuilder.setAuthor(args[1], null, "https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            embedBuilder.setThumbnail("https://crafatar.com/avatars/8667ba71b85a4004af54457a9734eed7");
            e.printStackTrace();
        }
        long bans = getBansCountBy(instance, args[1], time);
        long mutes = getMutesCountBy(instance, args[1], time);
        long kicks = getKicksCountBy(instance, args[1], time);
        long warnings = getWarningsCountBy(instance, args[1], time);
        embedBuilder.addField("Bans count:", String.valueOf(bans), true);
        embedBuilder.addField("Mutes count:", String.valueOf(mutes), true);
        embedBuilder.addField("Kicks count:", String.valueOf(kicks), true);
        embedBuilder.addField("Warnings count:", String.valueOf(warnings), true);
        embedBuilder.addField("Time online:", convertLongToString(getTimeOnline(instance, args[1], time)), true);
        String chartGenerator = "https://quickchart.io/chart?c=%7B%22type%22%3A%22outlabeledPie%22%2C%22data%22%3A%7B%22labels%22%3A%5B%22Bans%22%2C%22Mutes%22%2C%22Kicks%22%2C%22Warnings%22%5D%2C%22datasets%22%3A%5B%7B%22backgroundColor%22%3A%5B%22%23FF3784%22%2C%22%2336A2EB%22%2C%22%234BC0C0%22%2C%22%23F77825%22%5D%2C%22data%22%3A%5B%25banp%25%2C%25mutep%25%2C%25kickp%25%2C%25warningp%25%5D%7D%5D%7D%2C%22options%22%3A%7B%22plugins%22%3A%7B%22legend%22%3Afalse%2C%22outlabels%22%3A%7B%22text%22%3A%22%25l%20%25p%22%2C%22color%22%3A%22white%22%2C%22stretch%22%3A35%2C%22font%22%3A%7B%22resizable%22%3Atrue%2C%22minSize%22%3A13%2C%22maxSize%22%3A18%7D%7D%7D%7D%7D";
        embedBuilder.setImage(chartGenerator.replace("%25banp%25", String.valueOf(bans))
                .replace("%25mutep%25", String.valueOf(mutes))
                .replace("%25kickp%25", String.valueOf(kicks))
                .replace("%25warningp%25", String.valueOf(warnings)));
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
        sendEmbededMessage(event.getTextChannel(), embedBuilder);

        return ReturnType.SUCCESS;
    }

    public long getBansCountBy(SuperDiscord instance, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + "bans` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getMutesCountBy(SuperDiscord instance, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + "mutes` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getWarningsCountBy(SuperDiscord instance, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + "warnings` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getKicksCountBy(SuperDiscord instance, String banned_by_name, long time) throws SQLException {
        long count = 0;

        String query = "SELECT COUNT(*) FROM `" + Setting.LITEBANS_TABLE_PREFIX + "kicks` WHERE UPPER(`banned_by_name`)='" + banned_by_name.toUpperCase() + "' AND `time`>='" + (time) + "';";
        PreparedStatement preparedStatement = instance.getLitebansDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        resultSet.close();
        preparedStatement.close();
        return count;
    }

    public long getTimeOnline(SuperDiscord instance, String playerName, long time) throws SQLException {
        long timeOnline = 0;

        String query = "SELECT * FROM `ultimateafk_logs` WHERE UPPER(`player_key`)='" + playerName.toUpperCase() + "' AND `quit_time`>='" + time + "';";
        PreparedStatement preparedStatement = instance.getUltimateAfkDatabase().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            long quitTime = resultSet.getLong("quit_time");
            long joinTime = resultSet.getLong("join_time");
            if (joinTime < time) {
                timeOnline += quitTime - time;
            } else {
                timeOnline += quitTime - joinTime;
            }
        }

        resultSet.close();
        preparedStatement.close();
        return timeOnline;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "litebans staffinfo <player> [duration]**\n\n***player***  must be replaced with player's name or `*`.\n***duration***  must be of type: `14d 2h 10m`\n `y - year` `mo - month` `w - week` `d - day` `h - hour` `m - minute` `s - second`\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
