package dk.nydt.autofish.core;

import dk.nydt.autofish.core.modules.MethodEnum;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class AutoFishConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @DropdownSetting
  private final ConfigProperty<MethodEnum> method = new ConfigProperty<>(MethodEnum.SOUND);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public ConfigProperty<MethodEnum> method() {
    return this.method;
  }
}
