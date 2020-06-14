/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvh.org;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Leee Leee
 */
public class Main extends JavaPlugin {

    Hub hub;
    
    @Override
    public void onEnable() {
        getLogger().info("starting");
        hub = Util.loadHub();
    }
    
    @Override
    public void onDisable() {
        getLogger().info("stopping");
        Util.saveHub(hub);
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {;
        // is player
        if (cs instanceof Player) {
            Player player = (Player) cs;
            
            
            // is command hvh && player has permissions
            player.sendMessage("entered: " + string);
            if (player.isOp() && string.equals("hvh")) {
                if (args.length <= 0) {
		    player.sendMessage("type '/hvh create' to start");
		}
                
                if (hub == null) {
                    if (args[0].equals("create")) {
                        hub = new Hub(player.getLocation());
                        player.sendMessage("HvH Hub Created!");
                    } else if (args[0].equals("delete")) {
                        player.sendMessage("Hub has been deleted");
                    } else {
                        player.sendMessage("No hub exists yet. Type '/hvh create' to start");
                    }
                    
                    return true;
                } else if (hub.parseCommand(player, new ArrayList(Arrays.asList(args)))) {
                    return true;
                }
            }
            
        }
        return false;
    }
    
}
