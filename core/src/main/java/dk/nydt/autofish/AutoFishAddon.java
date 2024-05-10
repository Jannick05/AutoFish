package dk.nydt.autofish;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import dk.nydt.autofish.commands.AutoFishPingCommand;

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
