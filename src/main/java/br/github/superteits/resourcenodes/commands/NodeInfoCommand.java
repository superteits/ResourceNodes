package br.github.superteits.resourcenodes.commands;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import br.github.superteits.resourcenodes.ResourceNodes;


public class NodeInfoCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<String> node = args.<String>getOne("node");
		if(node.isPresent()) {
			List<Text> contents = new ArrayList<>();
			contents.add(Text.of(TextColors.GRAY, "Tier 1: " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getBlockMap().get(1) + " - " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getChanceMap().get(1)));
			contents.add(Text.of(TextColors.GRAY, "Tier 2: " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getBlockMap().get(2) + " - " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getChanceMap().get(2)));
			contents.add(Text.of(TextColors.GRAY, "Tier 3: " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getBlockMap().get(3) + " - " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getChanceMap().get(3)));
			contents.add(Text.of(TextColors.GRAY, "Cooldown: " +
					ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getCooldown()));
			PaginationList.builder()
				.contents(contents)
				.title(Text.of(TextColors.YELLOW, ResourceNodes.INSTANCE.getConfig().getNode(node.get()).getName() + " Info"))
				.header(Text.of(TextColors.GOLD, "Tier: Block - Chance"))
				.padding(Text.of(TextColors.DARK_GRAY, "-"))
				.build().sendTo(src);
			
		}
		return CommandResult.success();
	}

}
