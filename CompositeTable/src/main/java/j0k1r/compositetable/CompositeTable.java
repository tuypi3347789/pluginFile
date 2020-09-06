package j0k1r.compositetable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompositeTable extends JavaPlugin {

    @Override
    public void onEnable() {
        ShapedRecipe test = new ShapedRecipe(new
                ItemStack(Material.GOLDEN_HELMET));
        test.shape("   ", "!/!", "   ");
        test.setIngredient('!', Material.DIRT);
        test.setIngredient('/', Material.COBBLESTONE);
        getServer().addRecipe(test);
    }

    @Override
    public void onDisable() {
        getServer().clearRecipes();
    }
}
