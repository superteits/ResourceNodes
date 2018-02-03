package br.github.superteits.resourcenodes.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import br.github.superteits.resourcenodes.ResourceNodes;

public class GetLocationsCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		double distance = args.<Double>getOne("distance").get();
		if(src instanceof Player) {
			Player player = (Player) src;
			PaginationList.builder()
			.contents(ResourceNodes.INSTANCE.getDatabase().getNodesLocations(player.getLocation(), distance))
			.title(Text.of(TextColors.GRAY, "Resource Nodes"))
			.padding(Text.of(TextColors.DARK_GRAY, "-"))
			.header(Text.of(TextColors.GOLD, "Locations of near nodes - Limited to 50 meters, click to tp"))
			.build()
			.sendTo(player);
		}
		return CommandResult.success();
	}

}
