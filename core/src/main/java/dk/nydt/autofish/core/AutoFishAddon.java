package dk.nydt.autofish.core;

import dk.nydt.autofish.core.commands.AutoFishPingCommand;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class AutoFishAddon extends LabyAddon<AutoFishConfiguration> {
  @Override
  protected void enable() {

    this.registerSettingCategory();

    this.registerCommand(new AutoFishPingCommand());

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<AutoFishConfiguration> configurationClass() {
    return AutoFishConfiguration.class;
  }
}
