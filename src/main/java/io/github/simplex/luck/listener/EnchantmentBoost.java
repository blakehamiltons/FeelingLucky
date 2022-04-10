package io.github.simplex.luck.listener;

import io.github.simplex.luck.FeelingLucky;
import io.github.simplex.luck.player.Luck;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.List;
import java.util.Map;

public record EnchantmentBoost(FeelingLucky plugin) implements Listener {
    public EnchantmentBoost(FeelingLucky plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void enchantItem(EnchantItemEvent event) {
        Map<Enchantment, Integer> enchMap = event.getEnchantsToAdd();
        List<Enchantment> enchList = enchMap.keySet().stream().toList();
        Player player = event.getEnchanter();
        Luck luck = plugin.handler.getLuckContainer(player);
        if (luck.quickRNG(luck.getPercentage())) {
            Enchantment particular = enchList.get(Luck.RNG().nextInt(enchList.size()));
            int rng = Luck.RNG().nextInt(1, 5);

            if ((enchMap.get(particular) + rng) > particular.getMaxLevel()) {
                enchMap.replace(particular, particular.getMaxLevel());
            }

            enchMap.replace(particular, enchMap.get(particular) + rng);
        }
    }
}
