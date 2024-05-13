package dk.nydt.autofish.core.listener;

import dk.nydt.autofish.core.AutoFishAddon;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.entity.player.ClientPlayerInteractEvent;
import net.labymod.api.event.client.entity.player.ClientPlayerInteractEvent.InteractionType;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoFishPlayerInteractListener {

  private final AutoFishAddon addon;
  public static boolean ROD_CASTED = false;

  public AutoFishPlayerInteractListener(AutoFishAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onPlayerInteractEvent(ClientPlayerInteractEvent event) {
    ClientPlayer player = event.clientPlayer();
    if (event.type().equals(InteractionType.INTERACT)) {
      if (player.getMainHandItemStack().isFishingTool()) {
        System.out.println("Player cast or reel in fishing rod.");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
          ROD_CASTED = true;
        }, 10000, TimeUnit.MILLISECONDS);
      }
    }
  }
}
