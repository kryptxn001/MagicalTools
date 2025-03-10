package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MagicalGive implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Objects.equals(label, "magicalgive")) {
            Player player = (Player) sender;

            if (!player.isOp()) {
                return false;
            }

            if(args.length == 0) {
                StringBuilder list = new StringBuilder();
                for (Tool tool : ToolManager.getAllTools()) {
                    list.append(tool.getName()).append(", ");
                }
                list.deleteCharAt(list.length()-1);
                list.deleteCharAt(list.length()-1);
                CommandUtils.sendMessageBranded(player, "List of available tools: "+list);
            } else if (args.length >= 2) {
                Player cmdPlayer = Bukkit.getPlayer(args[0]);
                if (cmdPlayer != null) { // když hrač existuje
                    Tool cmdtool = ToolManager.getToolbyID(args[1]);
                    if (cmdtool != null) {
                        cmdPlayer.getInventory().addItem(cmdtool.getItem());
                        CommandUtils.sendMessageBranded(player, "Gave " + cmdtool.getLabel() + CommandUtils.resetformat + " to " + args[0] + ".");
                    } else {
                        CommandUtils.sendMessageBranded(player, "Tool with this ID doesn't exist!");
                    }
                } else {
                    CommandUtils.sendMessageBranded(player, "Specified player isn't online.");
                }
            } else { // spatne pouziti
                CommandUtils.sendMessageBranded(player, "Wrong usage! Usage: /magicalgive [player] [tool]");
            }
        }
        return true;
    }
}
