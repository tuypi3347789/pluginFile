package j0k1r.tutorplugin;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Attr;

import javax.naming.Name;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Supplier;

public final class TutorPlugin extends JavaPlugin implements Listener {
    protected static ItemStack item01;
    protected static ItemStack Portal;
    static {
        item01 = new ItemStack(Material.DIAMOND_SWORD, (short) 1);
        Portal = new ItemStack(Material.PAPER, (short) 1);
    }
    @Override
    public void onEnable() {
        ItemMeta getItem = item01.getItemMeta();
        getItem.setDisplayName(ChatColor.GOLD + "破邪之劍");
        getItem.setLore(Arrays.asList(new String[] {
                (ChatColor.WHITE + "擁有強大力量的建"),
                (ChatColor.WHITE + "非常危險!")
        }));

        Multimap<org.bukkit.attribute.Attribute, AttributeModifier> attributeModifiers;
        attributeModifiers = getItem.getAttributeModifiers(EquipmentSlot.HAND);
        attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),
                "generic.attack_damage", 17, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeModifiers.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),
                "generic.attack_speed", 6.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeModifiers.put(Attribute.GENERIC_FLYING_SPEED, new AttributeModifier(UUID.randomUUID(),
                "generic.flying_speed", 100, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        getItem.setAttributeModifiers(attributeModifiers);
        getLogger().info(String.valueOf(getItem));

        ItemMeta por = Portal.getItemMeta();
        por.setDisplayName(ChatColor.BLUE + "傳送紙");
        por.setLore(Arrays.asList(new String[] {
                (ChatColor.WHITE + "傳送至瑪格麗西亞")
        }));

        item01.setItemMeta(getItem);
        Portal.setItemMeta(por);
        getServer().getPluginManager().registerEvents(this, this);
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("get")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "請輸入物品編號!");
            } else if (args.length == 1) {
                String a = args[0];
                switch(a) {
                    case "001":
                        p.getInventory().addItem(item01);
                        p.updateInventory();
                        getLogger().info("test");
                        break;
                    case "002":
                        p.getInventory().addItem(Portal);
                        p.updateInventory();
                        break;
                default:
                    p.sendMessage(ChatColor.RED + "無此物品編號!");
                }
            }
        }
        return false;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        int x = (int) p.getLocation().getX();
        int y = (int) p.getLocation().getY();
        int z = (int) p.getLocation().getZ();

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && p.getInventory().getItemInMainHand().equals(Portal)) {
            if(x>=158 && x<= 161 && z>=31 && z<=34 && y == 64) {
                p.sendMessage(ChatColor.GREEN + "傳送成功");
                p.teleport(new Location(p.getWorld(), 166.436, 63, 14.520));
            } else {
                p.sendMessage((ChatColor.RED + "請至特定區域嘗試一遍"));
            }
        }
    }
}
