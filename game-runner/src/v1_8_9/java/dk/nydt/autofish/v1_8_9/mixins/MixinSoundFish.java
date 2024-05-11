package dk.nydt.autofish.v1_8_9.mixins;

import dk.nydt.autofish.core.AutoFishAddon;
import dk.nydt.autofish.core.modules.MethodEnum;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.TimeUnit;

@Mixin(NetHandlerPlayClient.class)
public class MixinSoundFish {

  @Shadow
  private Minecraft gameController;
  @Inject(method = "handleSoundEffect", at = @At("HEAD"))
  public void fish(S29PacketSoundEffect packetIn, CallbackInfo ci) {
    if(!(AutoFishAddon.instance.configuration().method().get().equals(MethodEnum.SOUND))) return;
    double HOOKSOUND_DISTANCESQ_THRESHOLD = 2d;
    if(packetIn.getSoundName().equals("random.splash")) {
      if(gameController.thePlayer == null || gameController.thePlayer.fishEntity == null) return;
      double x, y, z;
      x = packetIn.getX();
      y = packetIn.getY();
      z = packetIn.getZ();
      if(gameController.thePlayer.fishEntity.getDistanceSq(x, y, z) < HOOKSOUND_DISTANCESQ_THRESHOLD) {
        autoFish$processHook();
        gameController.thePlayer.fishEntity = null;
        // Delay next processhook with 1 ms without thread.sleep
        Laby.labyAPI().taskExecutor().getScheduledPool().schedule(new Runnable() {
          @Override
          public void run() {
            autoFish$processHook();
          }
        }, 100, TimeUnit.MILLISECONDS);
      }
    }
  }

  @Unique
  private void autoFish$processHook() {
    gameController.thePlayer.swingItem();
    gameController.playerController.sendUseItem(
        gameController.thePlayer,
        gameController.theWorld,
        gameController.thePlayer.getHeldItem());
  }
}
