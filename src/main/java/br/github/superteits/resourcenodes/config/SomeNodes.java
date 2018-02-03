package br.github.superteits.resourcenodes.config;

import java.util.HashMap;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import br.github.superteits.resourcenodes.Node;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.LogAxes;
import org.spongepowered.api.data.type.LogAxis;
import org.spongepowered.api.data.type.TreeTypes;

public class SomeNodes {
	
	public static Node getOreDefault() {
		HashMap<Integer, BlockState> blockMap = new HashMap<>();
		HashMap<Integer, Integer> chanceMap = new HashMap<>();
		
		blockMap.put(1, BlockState.builder().blockType(BlockTypes.COAL_ORE).build());
		blockMap.put(2, BlockState.builder().blockType(BlockTypes.GOLD_ORE).build());
		blockMap.put(3, BlockState.builder().blockType(BlockTypes.DIAMOND_ORE).build());
		
		chanceMap.put(1, 50);
		chanceMap.put(2, 40);
		chanceMap.put(3, 10);
		
		return new Node(blockMap, chanceMap, 60);
	}
	
	public static Node getCropDefault() {
		HashMap<Integer, BlockState> blockMap = new HashMap<>();
		HashMap<Integer, Integer> chanceMap = new HashMap<>();
		
		blockMap.put(1, BlockState.builder().blockType(BlockTypes.BEETROOTS).add(Keys.GROWTH_STAGE, 4).build());
		blockMap.put(2, BlockState.builder().blockType(BlockTypes.WHEAT).add(Keys.GROWTH_STAGE, 4).build());
		blockMap.put(3, BlockState.builder().blockType(BlockTypes.POTATOES).add(Keys.GROWTH_STAGE, 4).build());
		
		chanceMap.put(1, 50);
		chanceMap.put(2, 40);
		chanceMap.put(3, 10);
		
		return new Node(blockMap, chanceMap, 60);
	}
	
	public static Node getWoodDefault() {
		HashMap<Integer, BlockState> blockMap = new HashMap<>();
		HashMap<Integer, Integer> chanceMap = new HashMap<>();
		
		blockMap.put(1, BlockState.builder().blockType(BlockTypes.LOG).add(Keys.LOG_AXIS, LogAxes.X).add(Keys.TREE_TYPE, TreeTypes.JUNGLE).build());
		blockMap.put(2, BlockState.builder().blockType(BlockTypes.LOG).add(Keys.LOG_AXIS, LogAxes.Y).add(Keys.TREE_TYPE, TreeTypes.DARK_OAK).build());
		blockMap.put(3, BlockState.builder().blockType(BlockTypes.LOG2).add(Keys.LOG_AXIS, LogAxes.Z).add(Keys.TREE_TYPE, TreeTypes.ACACIA).build());
		
		chanceMap.put(1, 50);
		chanceMap.put(2, 40);
		chanceMap.put(3, 10);
		
		return new Node(blockMap, chanceMap, 60);
	}
}
