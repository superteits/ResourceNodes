package br.github.superteits.resourcenodes.api;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.block.TargetBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import br.github.superteits.resourcenodes.Node;


public class NodeHarvestEvent extends AbstractEvent implements Cancellable, TargetBlockEvent{
	
	private final Node node;
	private final Player player;
	private final Cause cause;
	private final BlockSnapshot blockSnapshot;
	private boolean cancelled = false;
	
	public NodeHarvestEvent(Node node, Player player, Cause cause, BlockSnapshot block) {
		this.node = node;
		this.player = player;
		this.cause = cause;
		this.blockSnapshot = block;
	}

	/**
	 * Gets the {@link Player} who harvested the node related to this event.
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Gets the {@link Node} related to this event.
	 * 
	 * @return The Node
	 */
	public Node getNode() {
		return this.node;
	}

	@Override
	public Cause getCause() {
		return this.cause;
	}

	@Override
	public BlockSnapshot getTargetBlock() {
		return this.blockSnapshot;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
		
	}

}
