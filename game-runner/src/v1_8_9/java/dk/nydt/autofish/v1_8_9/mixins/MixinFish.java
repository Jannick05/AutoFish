package dk.nydt.autofish.v1_8_9.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinFish {

  @Shadow
  private Minecraft gameController;
  @Inject(method = "handleSoundEffect", at = @At("HEAD"))
  public void fish(S29PacketSoundEffect packetIn, CallbackInfo ci) {
    double HOOKSOUND_DISTANCESQ_THRESHOLD = 25D;
    if(packetIn.getSoundName().equals("random.splash")) {
      if(gameController.thePlayer == null || gameController.thePlayer.fishEntity == null) return;
      double x, y, z;
      x = packetIn.getX();
      y = packetIn.getY();
      z = packetIn.getZ();
      if(gameController.thePlayer.fishEntity.getDistanceSq(x, y, z) < HOOKSOUND_DISTANCESQ_THRESHOLD) {
        gameController.playerController.sendUseItem(gameController.thePlayer, gameController.theWorld, gameController.thePlayer.getHeldItem());
      }
    }
  }
}
