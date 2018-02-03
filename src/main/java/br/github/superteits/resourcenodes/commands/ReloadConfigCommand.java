package br.github.superteits.resourcenodes.commands;

import br.github.superteits.resourcenodes.ResourceNodes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadConfigCommand implements CommandExecutor{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ResourceNodes.INSTANCE.getConfig().loadConfig(ResourceNodes.INSTANCE.getConfigManager());
        src.sendMessage(Text.of(TextColors.GREEN, "Config Reloaded"));
        return CommandResult.success();
    }
}
