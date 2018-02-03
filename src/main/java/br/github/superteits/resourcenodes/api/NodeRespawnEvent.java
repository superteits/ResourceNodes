package br.github.superteits.resourcenodes.api;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.TargetBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import br.github.superteits.resourcenodes.Node;

public class NodeRespawnEvent extends AbstractEvent implements TargetBlockEvent{
	
	private final Node node;
	private final Cause cause;
	private final BlockSnapshot blockSnapshot;
	
	public NodeRespawnEvent(Node node, Cause cause, BlockSnapshot block) {
		this.node = node;
		this.cause = cause;
		this.blockSnapshot = block;
	}
	
	@Override
	public BlockSnapshot getTargetBlock() {
		return this.blockSnapshot;
	}
	
	/**
	 * Gets the {@link Node} related to this event.
	 * 
	 * @return The NodeData
	 */
	public Node getNode() {
		return this.node;
	}

	@Override
	public Cause getCause() {
		return this.cause;
	}

}
