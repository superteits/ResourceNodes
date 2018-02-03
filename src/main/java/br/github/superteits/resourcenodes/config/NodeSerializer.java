package br.github.superteits.resourcenodes.config;

import java.util.HashMap;
import java.util.Map;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import com.google.common.reflect.TypeToken;

import br.github.superteits.resourcenodes.Node;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class NodeSerializer {

	public Node deserialize(ConfigurationNode value) throws ObjectMappingException {
		HashMap<Integer, BlockState> blockMap = value.getNode("block states").getValue(new TypeToken<HashMap<Integer, BlockState>>(){});
		HashMap<Integer, Integer> chanceMap = value.getNode("chances").getValue(new TypeToken<HashMap<Integer, Integer>>(){});
		long cooldown = value.getNode("cooldown").getLong();
		return new Node(blockMap, chanceMap, cooldown);
	}

	public void serialize(Node obj, CommentedConfigurationNode value) throws ObjectMappingException {
		value.getNode("block states").setValue(new TypeToken<HashMap<Integer, BlockState>>(){}, obj.getBlockMap());
		value.getNode("block states").setComment("A map which maps tiers to BlockStates.");
		value.getNode("chances").setValue(new TypeToken<HashMap<Integer, Integer>>(){}, obj.getChanceMap());
		value.getNode("chances").setComment("A map which maps tiers to chances.");
		value.getNode("cooldown").setValue(obj.getCooldown());
		value.getNode("cooldown").setComment("Node's respawn interval in seconds.");
	}
	
	

}
