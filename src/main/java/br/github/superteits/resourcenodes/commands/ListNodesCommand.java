package br.github.superteits.resourcenodes.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;

public class ListNodesCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Text wood = Text.builder("Wood").color(TextColors.GRAY).format(TextFormat.of(TextStyles.UNDERLINE))
				.onClick(TextActions.runCommand("/node info wood")).build();
		Text ore = Text.builder("Ore").color(TextColors.GRAY).format(TextFormat.of(TextStyles.UNDERLINE))
				.onClick(TextActions.runCommand("/node info ore")).build();
		Text crop = Text.builder("Crop").color(TextColors.GRAY).format(TextFormat.of(TextStyles.UNDERLINE))
				.onClick(TextActions.runCommand("/node info crop")).build();
		PaginationList.builder()
		.contents(wood, Text.NEW_LINE, ore, Text.NEW_LINE, crop)
		.title(Text.of(TextColors.YELLOW, "Configured Nodes"))
		.header(Text.of(TextColors.GOLD, "Click to see some informations"))
		.padding(Text.of(TextColors.GRAY, "-"))
		.build().sendTo(src);
		return CommandResult.success();
	}

}
