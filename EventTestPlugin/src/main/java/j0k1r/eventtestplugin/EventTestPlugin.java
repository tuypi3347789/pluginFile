package j0k1r.eventtestplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class EventTestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.BLUE + "********");
        p.sendMessage(ChatColor.AQUA + "  welcome to test server  ");
        p.sendMessage(ChatColor.GOLD + "  have fun!!!!  ");
        p.sendMessage(ChatColor.BLUE + "********");
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.sendMessage(ChatColor.GOLD + "87" + p.getName() + "is death!!");
    }
    @EventHandler
    public  void onPlayerEgg(PlayerEggThrowEvent e) {
        e.setHatchingType(EntityType.SHEEP);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        int x = (int) p.getLocation().getX();
        int y = (int) p.getLocation().getY();
        int z = (int) p.getLocation().getZ();
        if (x>=151 && x<=155 && z>=18 && z<=23 && y == 63) {
            p.sendMessage(ChatColor.RED + "誤踏禁區，強制轉送離開!");
            p.teleport(new Location(p.getWorld(),147,63,21));
        }
    }
}
