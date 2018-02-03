package br.github.superteits.resourcenodes.commands;

import br.github.superteits.resourcenodes.storage.H2Storage;
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

public class SetNodeCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			String node = args.<String>getOne("node").get();
			int tier = args.<Integer>getOne("tier").get();
			BlockState block = ResourceNodes.INSTANCE.getConfig().getNode(node).getBlockMap().get(tier);
			Player player = (Player) src;
			BlockRay<World> br = BlockRay.from(player).distanceLimit(5).stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
			if(br.hasNext()) {
				BlockRayHit<World> hit = br.end().get();
				if(!hit.getLocation().getBlock().getType().equals(BlockTypes.AIR)) {
					hit.getLocation().setBlock(block);
					player.sendMessage(Text.of(TextColors.GREEN, "You settled a node to where you are looking"));
					if(!ResourceNodes.INSTANCE.getLocations().contains(hit.getLocation())) {
						ResourceNodes.INSTANCE.getLocations().add(hit.getLocation());
						ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.SAVE).add(hit.getLocation());
					}
				}
				else {
					player.sendMessage(Text.of(TextColors.RED, "Please look to a block in 5 blocks distance"));
				}
			}
		}
		return CommandResult.success();
	}

}
