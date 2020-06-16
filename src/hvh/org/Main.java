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

    private void help(Player player, String message) {
        player.sendMessage("[HvH] " + message);
    }

    /**
     * Called on any /hvh command
     */
    private void hvhHandler(Player player, String[] args) {
        if (hub == null) {
            String cmd = args.length >= 1 ? args[0] : ""; 
            
            // no hub exists, create one
            switch (cmd) {
                case "create":
                    hub = new Hub(this, player);
                    help(player, "Hub Created!");
                    break;
                case "delete":
                    hub = null;
                    help(player, "Hub deleted. :(");
                    break;
                default:
		            help(player, "Unknown command \"' + cmd + '\"");
                case "":
		            help(player, "Type \"/hvh create\" to start");
                    break;
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
