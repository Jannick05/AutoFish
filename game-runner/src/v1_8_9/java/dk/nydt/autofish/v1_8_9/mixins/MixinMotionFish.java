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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Mixin(NetHandlerPlayClient.class)
public class MixinMotionFish {

  private static final double XZ_MOTION_THRESHOLD = 0.0D;
  private static final double Y_MOTION_THRESHOLD = 0.02D;
  private static final long SCHEDULE_DELAY = 100;
  @Shadow
  private Minecraft gameController;

  @Inject(method = "handleEntityVelocity", at = @At("HEAD"))
  public void fish(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
    if (!(AutoFishAddon.instance.configuration().method().get().equals(MethodEnum.MOTION)))
      return;
    if (gameController.thePlayer == null)
      return; //Returns if player is null
    if (gameController.thePlayer.fishEntity == null)
      return; //Returns if fishEntity is null
    if (gameController.thePlayer.fishEntity.getEntityId() != packetIn.getEntityID())
      return; //Returns if player's fishEntity ID is not equal to packet entity ID
    EntityFishHook fishEntity = gameController.thePlayer.fishEntity;

    //Wait for the hook to not be moving
    final ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];
    futureHolder[0] = Laby.labyAPI().taskExecutor().getScheduledPool().scheduleAtFixedRate(() -> {
      if (fishEntity.motionX < XZ_MOTION_THRESHOLD && fishEntity.motionY < Y_MOTION_THRESHOLD
          && fishEntity.motionZ < XZ_MOTION_THRESHOLD) {
        //Hook is not moving, check for Y motion
        if (fishEntity.motionY < Y_MOTION_THRESHOLD) {
          autoFish$processHook();
          futureHolder[0].cancel(false); //Cancel the scheduled task
        }
      }
    }, SCHEDULE_DELAY, SCHEDULE_DELAY, TimeUnit.MILLISECONDS);
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
