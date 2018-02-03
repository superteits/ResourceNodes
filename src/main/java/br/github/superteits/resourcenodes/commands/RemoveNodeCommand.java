package br.github.superteits.resourcenodes.commands;

import br.github.superteits.resourcenodes.storage.H2Storage;
import com.google.common.base.Predicates;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import br.github.superteits.resourcenodes.ResourceNodes;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Set;

public class RemoveNodeCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player) src;
			BlockRay<World> br = BlockRay.from(player).distanceLimit(5).stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
			if(br.hasNext()) {
				BlockRayHit<World> hit = br.end().get();
				if(!hit.getLocation().getBlock().getType().equals(BlockTypes.AIR)) {
					if(ResourceNodes.INSTANCE.getLocations().contains(hit.getLocation())) {
						hit.getLocation().setBlock(BlockState.builder().blockType(BlockTypes.AIR).build());
						ResourceNodes.INSTANCE.getLocations().remove(hit.getLocation());
						ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.REMOVE).add(hit.getLocation());
						player.sendMessage(Text.of(TextColors.GREEN, "A node was removed from here."));
					}
					else
						player.sendMessage(Text.of(TextColors.RED, "Don't have a node here"));
				}
				else {
					player.sendMessage(Text.of(TextColors.RED, "Please look to a block in 5 blocks distance"));
				}
			}
		}
		return CommandResult.success();
	}

}
