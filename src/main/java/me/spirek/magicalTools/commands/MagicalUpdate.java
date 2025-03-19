package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.UpdateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MagicalUpdate implements CommandExecutor {
    /**
     * Handles the "/magicalupdate" command, updates the update ID.
     *
     * @param sender  The entity (player or console) that executed the command.
     * @param command The command that was executed.
     * @param label   The alias of the command used.
     * @param args    Additional arguments provided with the command.
     * @return        True if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Objects.equals(label, "magicalupdate")) {
            Player player = (Player) sender;

            if(!sender.hasPermission("magicaltools.update")) {
                CommandUtils.sendNotPermissions(player);
                return true;
            }

            UpdateManager.newUpdate();
            CommandUtils.sendMessageBranded(player, "Updated! New update id: "+UpdateManager.update_id);
        }
        return true;
    }
}
