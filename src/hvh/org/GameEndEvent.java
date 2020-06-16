import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class GameEndEvent extends Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        HvHPlayer hplayer = Main.hub.createGamer(player);
        
        // if player is hunter
        if (hplayer.playign)

        hplayer.endGame();
    }   
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    
    }
}
