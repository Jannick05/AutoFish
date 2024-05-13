package dk.nydt.autofish.v1_8_9.mixins;

import dk.nydt.autofish.core.AutoFishAddon;
import dk.nydt.autofish.core.listener.AutoFishPlayerInteractListener;
import dk.nydt.autofish.core.modules.MethodEnum;
import net.labymod.api.Laby;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
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
  private static final double Y_MOTION_THRESHOLD = -0.02D;
  @Unique
  private static final long SCHEDULE_DELAY = 1000;
  @Unique
  private double LAST_Y_MOTION = 0.0D;

  @Shadow
  private Minecraft gameController;

  @Inject(method = "handleEntityVelocity", at = @At("HEAD"))
  public void fish(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
    if (!(AutoFishAddon.instance.configuration().method().get().equals(MethodEnum.MOTION))) {
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

    System.out.println("Hook does move");
    System.out.println("Y: " + fishEntity.motionY);
    if(fishEntity.motionX > XZ_MOTION_THRESHOLD || fishEntity.motionZ > XZ_MOTION_THRESHOLD) {
      System.out.println("Hook moves in X or Z direction");
      return; //Returns if hook moves in X or Z direction
    }
    if(fishEntity.motionY > LAST_Y_MOTION) {
      System.out.println("Hook moves upwards");
      LAST_Y_MOTION = fishEntity.motionY;
      return; //Returns if hook moves upwards
    }
    if(fishEntity.motionY < Y_MOTION_THRESHOLD) {
      System.out.println("Hook moves downwards");
      return; //Returns if hook moves downwards
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
