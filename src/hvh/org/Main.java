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

    private void help(String message) {
        player.sendMessage("[HvH] " + message);
    }

    /**
     * Called on any /hvh command
     */
    private void hvhHandler(Player player, String[] args) {
        if (hub == null) {
            String cmd = args.length >= 0 ? args[0] : null; 
            
            // no hub exists, create one
            if (cmd != null) {
		        help("Unknown command \"' + cmd + '\"");
		        help("Type \"/hvh create\" to start");
            } else if (cmd.equals("create")) {
                hub = new Hub(this, player);
                help("Hub Created!");
            } else if (cmd.equals("delete")) {
                help("Hub deleted. :(");
            } else {
                // have the hub handle commands
		        help("Type \"/hvh create\" to start");
            }
        
        } else {
            // let the hub deal with it
            hub.parseCommand(player, new ArrayList(Arrays.asList(args)));
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {;
        // is player
        if (cs instanceof Player) {
            Player player = (Player) cs;
            
            // command is hvh && player has permissions
            if (player.isOp() && string.equals("hvh")) {
                hvhHandler(player, args);
                return true;
            }
		}
		
        return false;
    }
    
}
