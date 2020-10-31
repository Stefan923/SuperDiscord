package me.stefan923.superdiscord.language;

public class Language {

    public static String INVALID_COMMAND_SYNTAX = "Invalid syntax or you have no permission!\n\nThe valid syntax is: %syntax%";
    public static String NO_PERMISSION = "You need the **%permission%** permission to do that!";
    public static String NO_SUCH_COMMAND = "The command you entered does not exist or is spelt incorrectly.";
    public static String MUST_BE_ONLINE = "The target player is not online.";
    public static String TOGGLE_COMMAND = "\nYou have successfully **%action%** the `%command%` command.\n";

    public static String GIVEN_PUNISHMENTS = "\nThe player **%player%** gave `%count%` punishments of type `%type%`.";

    public static String SERVER_LAG = "\u2022 Server's online time: **%time%**\n" +
            "\u2022 Current TPS: **%cur_tps%**\n" +
            "\u2022 Maximum RAM: **%max_mem%**\n" +
            "\u2022 Allocated RAM: **%alloc_mem%**\n" +
            "\u2022 Free RAM: **%free_mem%**\n" +
            "\u2022 Server Worlds: \n%worlds%";
    public static String SERVER_GLIST = "\u2022 Factions: %pinger_isonline_Factions% (%pinger_players_Factions%)\n" +
            "\u2022 OneBlock: %pinger_isonline_176.9.160.190:25586% (%pinger_players_176.9.160.190:25586%)\n" +
            "\u2022 Survival 1.16: %pinger_isonline_Survival-1.16% (%pinger_players_Survival-1.16%)\n";

    public static String FORMAT_HELPOP = "&8[&3Staff-Response&8] &e%message%";

    public static String FACTIONS_TOP = "%ftop_rank%. ** %ftop_faction% `(%ftop_leader%)` ** - $%ftop_value%  (**%ftop_percentage_changed%**)\n";
    public static String FACTIONS_TOP_INFO = "**Info about `%ftop_faction%` faction:**\n" +
            "\u2022 Faction Leader: `%ftop_leader%`\n" +
            "\u2022 Faction Value: `$%ftop_value%`\n" +
            "\u2022 Spawner Value: `$%ftop_spawner_value%`\n" +
            "\u2022 Block Value: `$%ftop_block_value%`\n\n" +
            "**Top Spawners:**\n" +
            "[1] %ftop_top1spawner%: ` %ftop_top1spawnercount% `\n" +
            "[2] %ftop_top2spawner%: ` %ftop_top2spawnercount% `\n" +
            "[3] %ftop_top3spawner%: ` %ftop_top3spawnercount% `\n\n" +
            "**Spawner Breakdown:**\n" +
            "\u2022 SilverFish: ` %ftop_(spawners:SilverFish)% `\n" +
            "\u2022 IronGolem: ` %ftop_(spawners:IronGolem)% `\n" +
            "\u2022 Pigman: ` %ftop_(spawners:Pigman)% `\n" +
            "\u2022 Creeper: ` %ftop_(spawners:Creeper)% `\n" +
            "\u2022 Blaze: ` %ftop_(spawners:Blaze)% `\n" +
            "\u2022 Enderman: ` %ftop_(spawners:Enderman)% `\n" +
            "\u2022 Skeleton: ` %ftop_(spawners:Skeleton)% `\n" +
            "\u2022 Zombie: ` %ftop_(spawners:Zombie)% `\n\n" +
            "**Block Breakdown:**\n" +
            "\u2022 Beacons: ` %ftop_(blocks:BEACON:0)% `\n" +
            "\u2022 Dispensers: ` %ftop_(blocks:DISPENSER:0)% `\n" +
            "\u2022 Droppers: ` %ftop_(blocks:DROPPER:0)% `\n" +
            "\u2022 Hoppers: ` %ftop_(blocks:HOPPER:0)% `";

    public static String GENERIC_YEAR = "year";
    public static String GENERIC_YEARS = "years";
    public static String GENERIC_MONTH = "month";
    public static String GENERIC_MONTHS = "months";
    public static String GENERIC_DAY = "day";
    public static String GENERIC_DAYS = "days";
    public static String GENERIC_HOUR = "hour";
    public static String GENERIC_HOURS = "hours";
    public static String GENERIC_MINUTE = "minute";
    public static String GENERIC_MINUTES = "minutes";
    public static String GENERIC_SECOND = "second";
    public static String GENERIC_SECONDS = "seconds";

}
