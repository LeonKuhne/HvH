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
                return false;
        }
        
        return true;
    }
    
    public boolean parseCommand(Player player, List<String> args) {
        String cmd = args.remove(0);
        
        switch (cmd) {
            case "join":
                return joinGame(player, args.get(0));
            case "setspawn":
                setSpawn(player.getLocation());
                break;
            case "spawn":
                spawn(player);
                break;
            default:
                return false;
        }
        
        return true;
    }
    
    public void setSpawn(Location location) {
        spawn = location;
    }
    
    public void spawn(Player player) {
        Location loc = player.getLocation();
        loc.setWorld(spawn.getWorld());
        loc.setX(spawn.getX());
        loc.setY(spawn.getY());
        loc.setZ(spawn.getZ());
    }
    
}
