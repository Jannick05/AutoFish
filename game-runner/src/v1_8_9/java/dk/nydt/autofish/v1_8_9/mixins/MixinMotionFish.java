package dk.nydt.autofish.v1_8_9.mixins;

import dk.nydt.autofish.core.AutoFishAddon;
import dk.nydt.autofish.core.modules.MethodEnum;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.TimeUnit;

@Mixin(NetHandlerPlayClient.class)
public class MixinMotionFish {
  @Shadow
  private Minecraft gameController;

  @Inject(method = "handleEntityVelocity", at = @At("HEAD"))
  public void fish(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
    if (!(AutoFishAddon.instance.configuration().method().get().equals(MethodEnum.MOTION))) return;
    if (gameController.thePlayer == null) return;
    if (gameController.thePlayer.fishEntity == null) return;
    System.out.println("packetIn.getEntityID(): " + packetIn.getEntityID());
    System.out.println("fishEntity.getEntityId(): " + gameController.thePlayer.fishEntity.getEntityId());
    if (gameController.thePlayer.fishEntity.getEntityId() != packetIn.getEntityID()) return;
    EntityFishHook fishEntity = gameController.thePlayer.fishEntity;
    if(fishEntity.motionX != 0.0D || fishEntity.motionZ != 0.0D) return;
    if(fishEntity.motionY < 0.02D) return;
    System.out.println("fishEntity.motionX: " + fishEntity.motionX);
    System.out.println("fishEntity.motionY: " + fishEntity.motionY);
    System.out.println("fishEntity.motionZ: " + fishEntity.motionZ);
    Laby.labyAPI().taskExecutor().getScheduledPool().schedule(new Runnable() {
      @Override
      public void run() {
        autoFish$processHook();
      }
    }, 100, TimeUnit.MILLISECONDS);
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
