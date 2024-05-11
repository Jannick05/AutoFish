package dk.nydt.autofish.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class AutoFishPingCommand extends Command {

  public AutoFishPingCommand() {
    super("ping", "pong");

    this.withSubCommand(new AutoFishPingSubCommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ping")) {
      this.displayMessage(Component.text("Ping!", NamedTextColor.AQUA));
      return false;
    }

    this.displayMessage(Component.text("Pong!", NamedTextColor.GOLD));
    return true;
  }
}
