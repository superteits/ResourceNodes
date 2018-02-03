package br.github.superteits.resourcenodes.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class HelpNodeCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PaginationList.builder()
			.contents(Text.of(TextColors.GRAY, "/rnodes help - Open this index"),
					Text.of(TextColors.GRAY, "/rnodes set <node> <tier> - Set a node to where you are looking"),
					Text.of(TextColors.GRAY, "/rnodes locations <distance> - Get a list of near nodes"),
					Text.of(TextColors.GRAY, "/rnodes remove - Remove the node of your current location"),
					Text.of(TextColors.GRAY, "/rnodes list - Get the list of configured nodes"),
					Text.of(TextColors.GRAY, "/rnodes save - Save new locations to database"),
					Text.of(TextColors.GRAY, "/rnodes reload - Reload config file"))
			.title(Text.of("Resource Nodes Help"))
			.padding(Text.of(TextColors.GRAY, "-"))
			.build().sendTo(src);
		return CommandResult.success();
	}

}
