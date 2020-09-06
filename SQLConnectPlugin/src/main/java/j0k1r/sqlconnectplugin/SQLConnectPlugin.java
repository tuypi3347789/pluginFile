package j0k1r.sqlconnectplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public final class SQLConnectPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("setmotd")) {
            if(args.length == 0) p.sendMessage("/setmotd <message> -to set the join message");
            else {
                String str= "";
                for(int i=0;i<args.length;i++) str += args[i] + "";
                setMotd(str);
                p.sendMessage(ChatColor.GREEN + "SETTING DONE!");
            }
        }
        if (label.equalsIgnoreCase("setspawn")) {
            setSpawn(p);
        }
        if (label.equalsIgnoreCase("spawn")) {
            gotoSpawn(p);
        }
        if (label.equalsIgnoreCase("getItem")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "/getItem <ID> <AMOUNT> -to get the custom item");
            } else {
                int id = 0, amount = 1;
                if (args.length == 2) {
                    try{
                        amount = Integer.parseInt(args[1]);
                    }catch (Exception e) {
                        p.sendMessage(ChatColor.RED + "Amount must be a integer!");
                    }
                }
                try{
                    id = Integer.parseInt(args[0]);
                }catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "ID must be a Integer!");
                }
                String name = null;
                List<String> lore = new ArrayList<String>();
                ItemStack is = null;

                try{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/minecraft", "abc123", "123456");
                    String query = "SELECT * FROM custom_item WHERE id=" + id;
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);

                    if (rs.next()){
                        name = rs.getString("item_name");
                        lore.clear();
                        lore.addAll(Arrays.asList(rs.getString("item_lore").split("@")));
                        is = new ItemStack(Material.getMaterial(rs.getString("item_type")), amount);

                        p.getInventory().addItem(setMeta(is, name, lore));
                    } else p.sendMessage(ChatColor.RED + "The item not found");
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public ItemStack setMeta(ItemStack material, String name, List<String> lore) {
        if (((material == null) || material.getType() == Material.AIR) || (name == null && lore == null)) {
            return null;
        }
        ItemMeta im = material.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (lore != null) {
            im.setLore(lore);
        }
        material.setItemMeta(im);
        return material;
    }

    public void gotoSpawn(Player p) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/minecraft","abc123","123456");
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM player_spawn WHERE player_name=?");
            ps.setString(1,p.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String world = rs.getString("world_name");
                double x = rs.getDouble("loc_x");
                double y = rs.getDouble("loc_y");
                double z = rs.getDouble("loc_z");
                p.teleport(new Location(getServer().getWorld(world),x,y,z));
                p.sendMessage(ChatColor.GREEN + "TELEPORT DONE!!");
            } else {
                p.sendMessage(ChatColor.RED + "You are not set the spawn location yet!");
            }
            connection.close();
            ps.close();
            rs.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSpawn(Player p) {
        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/minecraft", "abc123","123456");
            if (canFindThePlayer(p)) {
                PreparedStatement search = connection.prepareStatement("SELECT * FROM player_spawn WHRER player_name=?");
                search.setString(1,p.getName());
                ResultSet rs = search.executeQuery();
                rs.next();

                PreparedStatement set = connection.prepareStatement("UPDATE player_spawn SET loc_x=?,loc_y=?,loc_z=?,world_name=? WHERE player_name=?");
                set.setDouble(1,x);
                set.setDouble(2,y);
                set.setDouble(3,z);
                set.setString(4,p.getWorld().getName());
                set.setString(5,p.getName());
                set.executeUpdate();

                search.close();
                rs.close();
                set.close();
                p.sendMessage(ChatColor.GREEN + "SETTING DONE!!");
            }else {
                PreparedStatement newPlayer = connection.prepareStatement("INSERT INTO player_spawn(player_name,world_name,loc_x,loc_y,loc_z) VALUES (?,?,?,?,?)");
                newPlayer.setString(1,p.getName());
                newPlayer.setString(2,p.getWorld().getName());
                newPlayer.setDouble(3,x);
                newPlayer.setDouble(4,y);
                newPlayer.setDouble(5,z);
                newPlayer.execute();

                newPlayer.close();
                p.sendMessage(ChatColor.GREEN + "INSERT DONE!!");
            }
        }catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean canFindThePlayer(Player p) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/minecraft","abc123","123456");
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM player_spawn WHERE player_name=?");
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            boolean canFind = rs.next();

            ps.close();
            rs.close();
            return canFind;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void setMotd(String str) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/minecraft","abc123","123456");
            Statement state = connection.createStatement();
            state.executeUpdate("UPDATE player_join SET message='"+str.replace('&','§')+" WHERE id = 1");
            connection.close();
            state.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.setJoinMessage(null);
    }
}
