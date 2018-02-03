package br.github.superteits.resourcenodes.commands;

import br.github.superteits.resourcenodes.ResourceNodes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SaveLocationsCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ResourceNodes.INSTANCE.getDatabase().updateLocations();
        src.sendMessage(Text.of(TextColors.GREEN, "Save successful"));
        return CommandResult.success();
    }
}
