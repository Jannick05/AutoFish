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
    System.out.println("Sound: " + packetIn.getSoundName());
    if(packetIn.getSoundName().equals("random.splash")) {
      gameController.playerController.sendUseItem(gameController.thePlayer, gameController.theWorld, gameController.thePlayer.getHeldItem());
    }
  }
}
