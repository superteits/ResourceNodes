package br.github.superteits.resourcenodes.storage;

import br.github.superteits.resourcenodes.Node;
import br.github.superteits.resourcenodes.ResourceNodes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class H2Storage {
	
	private SqlService sql;
	
	public H2Storage() {
		createDatabase();
	}
	
	public DataSource getDataSource(String jdbcUrl) throws SQLException {
	    if (sql == null) {
	        sql = Sponge.getServiceManager().provide(SqlService.class).get();
	    }
	    return sql.getDataSource(jdbcUrl);
	}
	
	public void createDatabase() {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String create = "CREATE TABLE IF NOT EXISTS nodeslocations(id INT AUTO_INCREMENT, world VARCHAR(50), x DOUBLE, y DOUBLE, z DOUBLE)";
		String create2 = "CREATE TABLE IF NOT EXISTS restartlocations(id INT AUTO_INCREMENT, world VARCHAR(50), x DOUBLE, y DOUBLE, z DOUBLE, node VARCHAR(20))";
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(create);
			PreparedStatement stmt2 = conn.prepareStatement(create2)) {
			stmt.execute();
			stmt2.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	public void insertNode(Location<World> location) {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String insert = "INSERT INTO nodeslocations (world,x,y,z) VALUES (?,?,?,?)";
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(insert)) {
			stmt.setString(1, location.getExtent().getName());
			stmt.setDouble(2, location.getX());
			stmt.setDouble(3, location.getY());
			stmt.setDouble(4, location.getZ());
			stmt.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertRestartNode(Location<World> location, Node node) {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String insert = "INSERT INTO restartlocations (world,x,y,z,node) VALUES (?,?,?,?,?)";
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(insert)) {
			stmt.setString(1, location.getExtent().getName());
			stmt.setDouble(2, location.getX());
			stmt.setDouble(3, location.getY());
			stmt.setDouble(4, location.getZ());
			stmt.setString(5, node.getName());
			stmt.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Text> getNodesLocations(Location<World> location, double distance) {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String getLocations = "SELECT x, y, z FROM nodeslocations WHERE x BETWEEN ? AND ? AND z BETWEEN ? AND ?";
		List<Text> list = new ArrayList<>();
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(getLocations)) {
			stmt.setDouble(1, location.getX() - distance);
			stmt.setDouble(2, location.getX() + distance);
			stmt.setDouble(3, location.getZ() - distance);
			stmt.setDouble(4, location.getZ() + distance);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				list.add(Text.builder(
						"X=" + result.getDouble("x") +
						" Y=" + result.getDouble("y") +
						" Z=" + result.getDouble("z"))
							.color(TextColors.YELLOW)
							.format(TextFormat.of(TextStyles.UNDERLINE))
							.onClick(TextActions.runCommand("/tp " +
									((int)result.getDouble("x")) + " " +
									result.getDouble("y") + " " +
									((int)result.getDouble("z")))).build());
			}
			result.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void removeNode(Location<World> location) {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String remove = "DELETE FROM nodeslocations WHERE world=? AND x=? AND y=? AND z=?";
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(remove)) {
			stmt.setString(1, location.getExtent().getName());
			stmt.setDouble(2, location.getX());
			stmt.setDouble(3, location.getY());
			stmt.setDouble(4, location.getZ());
			stmt.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void loadLocations() {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String load = "SELECT world, x, y, z FROM nodeslocations";

		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(load)) {
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				ResourceNodes.INSTANCE.getLocations().add(new Location<World>(
						Sponge.getServer().getWorld(result.getString("world")).get(),
						result.getDouble("x"),
						result.getDouble("y"),
						result.getDouble("z")));
			}
			result.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateLocations() {
		if(!ResourceNodes.INSTANCE.getStorageQueue().get(ActionsEnum.REMOVE).isEmpty()) {
			for(Location<World> loc : ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.REMOVE)) {
				removeNode(loc);
			}
		}
		if(!ResourceNodes.INSTANCE.getStorageQueue().get(ActionsEnum.SAVE).isEmpty()) {
			for(Location<World> loc : ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.SAVE)) {
				insertNode(loc);
			}
		}
	}

	public void updateRestartLocations() {
		if(!ResourceNodes.INSTANCE.getRestartMap().isEmpty()) {
			for(Location<World> loc : ResourceNodes.INSTANCE.getRestartMap().keySet()) {
				insertRestartNode(loc, ResourceNodes.INSTANCE.getRestartMap().get(loc)) ;
			}
		}
	}

	public HashMap<Location<World>, Node> loadRestartLocations() {
		String jdbcUrl = "jdbc:h2:./config/resourcenodes/nodeslocations.db";
		String load = "SELECT world, x, y, z, node FROM restartlocations";

		HashMap<Location<World>, Node> map = new HashMap<>();
		try(Connection conn = getDataSource(jdbcUrl).getConnection();
			PreparedStatement stmt = conn.prepareStatement(load)) {
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				map.put(new Location<World>(
						Sponge.getServer().getWorld(result.getString("world")).get(),
						result.getDouble("x"),
						result.getDouble("y"),
						result.getDouble("z")),
						ResourceNodes.INSTANCE.getConfig().getNode(result.getString("node")));
			}
			result.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public enum ActionsEnum {
		SAVE, REMOVE
	}


}
