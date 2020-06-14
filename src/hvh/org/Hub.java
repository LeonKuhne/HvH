package hvh.org;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Leee Leee
 */
public class Hub {
    
    List<Game> games;
    Location spawn;
    
    public Hub(Location spawn) {
        this.spawn = spawn;
    }
    
    public boolean joinGame(Player player, String team) {
        
        switch (team) {
            case "hunted":
                for (Game game : games) {
                    if (game.addHunted(player)) {
                        return true;
                    }
                }
                break;
            case "hunter":
                for (Game game : games) {
                    if (game.addHunter(player)) {
                        return true;
                    }
                }
                break;
            default:
		player.sendMessage("you must join the team either 'hunter' or 'hunted'");
                return false;
        }
        
        return true;
    }
    
    public boolean parseCommand(Player player, List<String> args) {
        String cmd = args.remove(0);
        
        switch (cmd) {
            case "join":
		if (args.size() > 0) {
                    return joinGame(player, args.get(0));
		} else {
		    player.sendMessage("cmd: /hvh join [hunter/hunted]");
		}
            case "setspawn":
                setSpawn(player.getLocation());
		player.sendMessage("setting hub spawn");
                break;
            case "spawn":
                spawn(player);
		player.sendMessage("teleporting to hub");
                break;
            default:
		player.sendMessage("cmds: join, spawn, setspawn");
                return false;
        }
        
        return true;
    }
    
    public void setSpawn(Location location) {
        spawn = location;
    }
    
    public void spawn(Player player) {
	player.teleport(spawn);
    }
    
}
