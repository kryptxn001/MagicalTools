package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MagicalHelp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Objects.equals(label, "magicalhelp")) {
            Player player = (Player) sender;

            if (args.length == 0) {
                //HELP obecne
                CommandUtils.sendMessageBranded(player, "Welcome to the Magical Tools help page.\n" +
                        "------------------------------------\n" +
                        "Magical Tools adds various weapons and tools that can be obtained via the /magicalgive command.\n" +
                        "To check the magical tool ID and Update ID of the item in hand, use the /magicalcheck command.\n" +
                        "If you changed the attributes of a tool in the config file, you must use the /magicalupdate afterwards the server got reloaded. Otherwise the outdated tools will not update.");
            } else if(args.length == 1) {
                // HELP prikaz
                switch (args[0]) {
                    case "magicalgive" -> CommandUtils.sendMessageBranded(player, "Gives a player magical tool. \n" +
                            "Usage: /magicalgive [player] [tool]");
                    case "magicalcheck" ->
                            CommandUtils.sendMessageBranded(player, "Returns the magical tool ID and the update ID of the item in your hand. \n" +
                                    "Usage: /magicalcheck");
                    case "magicalupdate" ->
                            CommandUtils.sendMessageBranded(player, "Updates the global update ID. Use this command immediately after changing the attributes of a tool\n" +
                                    "Usage: /magicalupdate");
                    case null, default ->
                            CommandUtils.sendMessageBranded(player, "This command doesn't exist. Use /magicalhelp without a command specified to learn about all the commands.");
                }
            }
        }

        return true;
    }
}
