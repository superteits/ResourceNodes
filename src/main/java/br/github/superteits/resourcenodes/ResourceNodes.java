package br.github.superteits.resourcenodes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import br.github.superteits.resourcenodes.commands.*;
import br.github.superteits.resourcenodes.schedulers.NodeScheduler;
import br.github.superteits.resourcenodes.schedulers.StorageScheduler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.geared.core.api.SpongePlugin;
import com.google.inject.Inject;

import br.github.superteits.resourcenodes.api.NodeHarvestEvent;
import br.github.superteits.resourcenodes.config.RNConfig;
import br.github.superteits.resourcenodes.storage.H2Storage;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id=ResourceNodes.ID, name=ResourceNodes.NAME, version=ResourceNodes.VERSION, authors="Teits")
public class ResourceNodes extends SpongePlugin{
	
	public static final String ID = "resourcenodes";
	public static final String NAME = "Resource Nodes";
	public static final String VERSION = "1.1.0";
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private Path defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	private RNConfig configInstance = new RNConfig();
	private HashMap<H2Storage.ActionsEnum, ArrayList<Location<World>>> storageQueue = new HashMap<>();
	private HashSet<Location<World>> locationSet = new HashSet<>();
	private HashMap<Location<World>, Node> restartMap = new HashMap<>();
	private NodeScheduler nsc = new NodeScheduler();
	private StorageScheduler sc = new StorageScheduler();
	public static ResourceNodes INSTANCE;
	private H2Storage db;
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		if(Files.exists(defaultConfig)) {
			configInstance.loadConfig(configManager);
		}
		else {
			configInstance.setupConfig(configManager);
			configInstance.loadConfig(configManager);
		}
		INSTANCE = this;
		db = new H2Storage();
		storageQueue.put(H2Storage.ActionsEnum.REMOVE, new ArrayList<Location<World>>());
		storageQueue.put(H2Storage.ActionsEnum.SAVE, new ArrayList<Location<World>>());
		connect();
	}

	@Listener
	public void onServerStarting(GameStartingServerEvent e) {

		CommandSpec getNodeSpec = CommandSpec.builder()
				.arguments(GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("distance"))))
				.description(Text.of("Get near nodes locations"))
				.permission("resourcenodes.command.locations")
				.executor(new GetLocationsCommand())
				.build();

		CommandSpec listNodeSpec = CommandSpec.builder()
				.description(Text.of("List of configured nodes"))
				.permission("resourcenodes.command.list")
				.executor(new ListNodesCommand())
				.build();

		CommandSpec removeNodeSpec = CommandSpec.builder()
				.description(Text.of("Remove node from where you are looking"))
				.permission("resourcenodes.command.remove")
				.executor(new RemoveNodeCommand())
				.build();

		CommandSpec helpNodeSpec = CommandSpec.builder()
				.description(Text.of("Help command to the Resource Nodes plugin"))
				.permission("resourcenodes.command.help")
				.executor(new HelpNodeCommand())
				.build();

		CommandSpec setNodeSpec = CommandSpec.builder()
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("node"))),
						GenericArguments.onlyOne(GenericArguments.integer(Text.of("tier"))))
				.description(Text.of("Set a node to where you are looking."))
				.permission("resourcenodes.command.set")
				.executor(new SetNodeCommand())
				.build();

		CommandSpec infoNodeSpec = CommandSpec.builder()
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("node"))))
				.description(Text.of("See informations about a node"))
				.permission("resourcenodes.command.info")
				.executor(new NodeInfoCommand())
				.build();

		CommandSpec saveLocationsSpec = CommandSpec.builder()
				.description(Text.of("Save locations to database"))
				.permission("resourcenodes.command.save")
				.executor(new SaveLocationsCommand())
				.build();

		CommandSpec reloadConfigSpec = CommandSpec.builder()
				.description(Text.of("Reload plugin config"))
				.permission("resourcenodes.command.reload")
				.executor(new ReloadConfigCommand())
				.build();

		CommandSpec baseNodeSpec = CommandSpec.builder()
				.description(Text.of("Base command."))
				.permission("resourcenodes.command.base")
				.child(setNodeSpec, "set")
				.child(reloadConfigSpec, "reload")
				.child(saveLocationsSpec, "save")
				.child(helpNodeSpec, "help")
				.child(removeNodeSpec, "remove")
				.child(listNodeSpec, "list")
				.child(getNodeSpec, "locations")
				.child(infoNodeSpec, "info")
				.build();

		Sponge.getCommandManager().register(this, baseNodeSpec, "rnode", "rnodes", "node", "nodes", "resourcenodes", "resourcenode", "rns", "rn");
		getDatabase().loadLocations();
		if(getConfig().getAutoSave().isEnabled()) {
			sc.scheduleStorageUpdate();
		}
		HashMap<Location<World>, Node> nodeMap = db.loadRestartLocations();
		if(!nodeMap.isEmpty()) {
			for(Location<World> loc : nodeMap.keySet()) {
				nsc.scheduleNodeReplacement(loc, nodeMap.get(loc));
			}
		}

	}
	
	@Listener
	public void onBlockBreak(ChangeBlockEvent.Break e, @First Player player) {
		BlockSnapshot blocksnap = e.getTransactions().get(0).getOriginal();
		BlockState block = e.getTransactions().get(0).getOriginal().getState();
		Location<World> location = e.getTransactions().get(0).getOriginal().getLocation().get();
		if(getLocations().contains(location)) {
			NodeHarvestEvent event = new NodeHarvestEvent(getConfig().getNodeFromBlockState(block), player, e.getCause(), blocksnap);
			Sponge.getEventManager().post(event);
			nsc.scheduleNodeReplacement(location, getConfig().getNodeFromBlockState(block));
			if(event.isCancelled()) {
				e.setCancelled(true);
			}
		}
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent e) {
		getDatabase().updateLocations();
		getDatabase().updateRestartLocations();
	}
	
	@Override
	public void doConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDisconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	public HashSet<Location<World>> getLocations() {
		return this.locationSet;
	}

	public HashMap<H2Storage.ActionsEnum, ArrayList<Location<World>>> getStorageQueue() {
		return this.storageQueue;
	}

	public H2Storage getDatabase() {
		return this.db;
	}

	public RNConfig getConfig() {
		return this.configInstance;
	}

	public HashMap<Location<World>, Node> getRestartMap() {
		return this.restartMap;
	}

	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
		return this.configManager;
	}
}
