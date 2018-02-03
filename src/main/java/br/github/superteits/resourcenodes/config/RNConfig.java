package br.github.superteits.resourcenodes.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.github.superteits.resourcenodes.AutoSave;
import com.google.common.reflect.TypeToken;

import br.github.superteits.resourcenodes.Node;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.block.BlockState;

public class RNConfig {
	
	NodeSerializer ns = new NodeSerializer();
	AutoSaveSerializer ass = new AutoSaveSerializer();
	private Node oreNode = SomeNodes.getOreDefault();
	private Node cropNode = SomeNodes.getCropDefault();
	private Node woodNode = SomeNodes.getWoodDefault();
	private AutoSave autoSave;
	
	public void setupConfig(ConfigurationLoader<CommentedConfigurationNode> configManager) {
		try {
			CommentedConfigurationNode configNode = configManager.createEmptyNode();
			ass.serialize(configNode.getNode("Auto Save Task"));
			ns.serialize(oreNode, configNode.getNode("Ore"));
			ns.serialize(cropNode, configNode.getNode("Crop"));
			ns.serialize(woodNode, configNode.getNode("Wood"));
			configManager.save(configNode);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ObjectMappingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadConfig(ConfigurationLoader<CommentedConfigurationNode> configManager) {
		try {
			CommentedConfigurationNode configNode = configManager.load();
			autoSave = ass.deserialize(configNode.getNode("Auto Save Task"));
			oreNode = ns.deserialize(configNode.getNode("Ore"));
			cropNode = ns.deserialize(configNode.getNode("Crop"));
			woodNode = ns.deserialize(configNode.getNode("Wood"));
			oreNode.setName("Ore");
			cropNode.setName("Crop");
			woodNode.setName("Wood");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public Node getNode(String node) {
		if(node.equalsIgnoreCase("ore"))
			return this.oreNode;
		if(node.equalsIgnoreCase("crop"))
			return this.cropNode;
		if(node.equalsIgnoreCase("wood"))
			return this.woodNode;
		return null;
	}

	public Node getNodeFromBlockState(BlockState block) {
		if(oreNode.getBlockMap().values().contains(block)) {
			return this.oreNode;
		}
		if(cropNode.getBlockMap().values().contains(block)) {
			return this.cropNode;
		}
		if(woodNode.getBlockMap().values().contains(block)) {
			return this.woodNode;
		}
		return null;
	}
	
	public List<Node> getNodesList() {
		List<Node> list = new ArrayList<>();
		list.add(oreNode);
		list.add(cropNode);
		list.add(woodNode);
		return list;
	}

	public AutoSave getAutoSave() {
		return this.autoSave;
	}


}
