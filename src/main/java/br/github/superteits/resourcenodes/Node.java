package br.github.superteits.resourcenodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

public class Node {
	private String name;
	private HashMap<Integer, BlockState> blockMap;
	private HashMap<Integer, Integer> chanceMap;
	private long cooldown;
	
	public Node(HashMap<Integer, BlockState> blockMap, HashMap<Integer, Integer> chanceMap, long cooldown) {
		this.blockMap = blockMap;
		this.chanceMap = chanceMap;
		this.cooldown = cooldown;
	}
	
	public HashMap<Integer, BlockState> getBlockMap() {
		return this.blockMap;
	}
	
	public HashMap<Integer, Integer> getChanceMap() {
		return this.chanceMap;
	}
	
	public long getCooldown() {
		return this.cooldown;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

}
