package dk.nydt.autofish.core;

import dk.nydt.autofish.core.commands.AutoFishPingCommand;
import dk.nydt.autofish.core.listener.AutoFishPlayerInteractListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class AutoFishAddon extends LabyAddon<AutoFishConfiguration> {
  public static AutoFishAddon instance;
  @Override
  protected void enable() {

    instance = this;

    this.registerSettingCategory();

    this.registerCommand(new AutoFishPingCommand());
    this.registerListener(new AutoFishPlayerInteractListener(this));

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<AutoFishConfiguration> configurationClass() {
    return AutoFishConfiguration.class;
  }
}
