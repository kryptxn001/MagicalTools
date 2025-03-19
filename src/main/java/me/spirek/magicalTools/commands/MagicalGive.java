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
    /**
     * Handles the "/magicalgive" command, giving / spawning a magical item.
     *
     * @param sender  The entity (player or console) that executed the command.
     * @param command The command that was executed.
     * @param label   The alias of the command used.
     * @param args    Additional arguments provided with the command.
     * @return        True if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Objects.equals(label, "magicalgive")) {
            Player player = (Player) sender;

            if(!sender.hasPermission("magicaltools.give")) {
                CommandUtils.sendNotPermissions(player);
                return true;
            }

            if(args.length == 0) {
                StringBuilder list = new StringBuilder();
                for (Tool tool : ToolManager.getAllTools()) {
                    if(!tool.isDisabled()) {
                        list.append(tool.getName()).append(", ");
                    }
                }
                list.deleteCharAt(list.length()-1);
                list.deleteCharAt(list.length()-1);
                CommandUtils.sendMessageBranded(player, "All of the available tools: "+list);
            } else if (args.length >= 2) {
                Player cmdPlayer = Bukkit.getPlayer(args[0]);
                if (cmdPlayer != null) { // když hrač existuje
                    Tool cmdtool = ToolManager.getToolbyID(args[1].toLowerCase());
                    if (cmdtool != null) {
                        if(cmdtool.isDisabled() && !player.hasPermission("magicaltools.ignoredisabled")) {
                            CommandUtils.sendMessageBranded(player,cmdtool.getName() +" is disabled! You can't spawn it, unless you have magicaltools.ignoredisabled permission.");
                            return true;
                        }
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
