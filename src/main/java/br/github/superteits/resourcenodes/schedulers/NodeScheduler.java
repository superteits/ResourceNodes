package br.github.superteits.resourcenodes.schedulers;

import java.util.concurrent.TimeUnit;

import br.github.superteits.resourcenodes.Node;
import br.github.superteits.resourcenodes.ResourceNodes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import br.github.superteits.resourcenodes.api.NodeRespawnEvent;

public class NodeScheduler {
	
	public void scheduleNodeReplacement(Location<World> location, Node node) {
		ResourceNodes.INSTANCE.getRestartMap().put(location, node);
		double r = Math.random();
		int tier;		
		if(r < (((double)node.getChanceMap().get(1)) / 100))
			tier = 1;
		else if(r < (((double)(node.getChanceMap().get(1) + node.getChanceMap().get(2))) / 100))
			tier = 2;
		else 
			tier = 3;
		BlockState block = node.getBlockMap().get(tier);
		BlockSnapshot blocksnap = BlockSnapshot.builder()
				.blockState(block)
				.position(location.getBlockPosition())
				.world(location.getExtent().getProperties())
				.build();
		Task.builder()
			.execute(
				() -> {
					location.setBlock(block);
					Sponge.getEventManager().post(
							new NodeRespawnEvent(
									node,
									Cause.builder().insert(0, this).build(EventContext.empty()),
									blocksnap));
					ResourceNodes.INSTANCE.getRestartMap().remove(location);
				})
			.delay(node.getCooldown(), TimeUnit.SECONDS)
			.submit(ResourceNodes.INSTANCE);
	}

}
