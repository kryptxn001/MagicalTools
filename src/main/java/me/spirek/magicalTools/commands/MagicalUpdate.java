package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.UpdateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MagicalUpdate implements CommandExecutor {
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
