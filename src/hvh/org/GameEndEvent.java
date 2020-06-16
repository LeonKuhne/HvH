import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.PlayerRespawnEvent;

public class GameEndEvent extends Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        HvHPlayer hplayer = Main.hub.createGamer(player);
        
        // if player is hunter, end game, otherwise we don't care
        if (hplayer.playing) {
            if (hplayer.team == "hunted") {
                player.spigot().respawn();
                hplayer.endGame();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        HvHPlayer hplayer = Main.hub.createGamer(player);
        
        // if player is hunter, say hi
        if (hplayer.playing) {
            if (hplayer.team == "hunter") {
                player.help("It's not too late, keep chasing!");
            }
        }   
    }
}