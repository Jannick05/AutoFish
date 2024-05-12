package dk.nydt.autofish.v1_8_9.mixins;

import dk.nydt.autofish.core.AutoFishAddon;
import dk.nydt.autofish.core.listener.AutoFishPlayerInteractListener;
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

  @Unique
  private static final double XZ_MOTION_THRESHOLD = 0.0D;
  @Unique
  private static final double Y_MOTION_THRESHOLD = 0.02D;
  @Unique
  private static final long SCHEDULE_DELAY = 1000;
  @Shadow
  private Minecraft gameController;

  @Inject(method = "handleEntityVelocity", at = @At("HEAD"))
  public void fish(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
    if (!(AutoFishAddon.instance.configuration().method().get().equals(MethodEnum.MOTION))) {
      System.out.println("Method is not motion");
      return;
    }
    if (gameController.thePlayer == null) {
      System.out.println("Player is null");
      return; //Returns if player is null
    }
    if (gameController.thePlayer.fishEntity == null) {
      System.out.println("Fish entity is null");
      return; //Returns if fishEntity is null
    }
    if (gameController.thePlayer.fishEntity.getEntityId() != packetIn.getEntityID()) {
      System.out.println("Fish entity ID is not equal to packet entity ID");
      return; //Returns if player's fishEntity ID is not equal to packet entity ID
    }

    EntityFishHook fishEntity = gameController.thePlayer.fishEntity;
    System.out.println("FishEntity - x: " + fishEntity.motionX + ", y: " + fishEntity.motionY + ", z: " + fishEntity.motionZ);

    boolean checkHook = AutoFishPlayerInteractListener.CHECK_HOOK;
    boolean rodCasted = AutoFishPlayerInteractListener.ROD_CASTED;
    System.out.println("Check hook: " + checkHook);
    System.out.println("Rod casted: " + rodCasted);

    if(rodCasted) {
      if (fishEntity.motionY < -0.02D) {
        System.out.println("Hook moved horizontally.");
        Laby.labyAPI().taskExecutor().getScheduledPool().schedule(new Runnable() {
          @Override
          public void run() {
            AutoFishPlayerInteractListener.ROD_CASTED = false;
            autoFish$processHook();
          }
        }, 100, TimeUnit.MILLISECONDS);
      }
    } else {
      System.out.println("Rod not casted.");
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
