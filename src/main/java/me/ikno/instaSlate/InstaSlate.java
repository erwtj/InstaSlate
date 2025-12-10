package me.ikno.instaSlate;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class InstaSlate extends JavaPlugin implements Listener {
    FileConfiguration _config = getConfig();
    private Set<Material> _otherMaterials;

    float _deepslateMineSpeed;
    boolean _overwriteToolComponent;

    @Override
    public void onLoad() {
        _otherMaterials = new HashSet<>(Tag.MINEABLE_PICKAXE.getValues());
        _otherMaterials.remove(Material.DEEPSLATE);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        _config.addDefault("deepslate-mine-speed", 45.0);
        _config.addDefault("overwrite-toolcomponent", false);
        _config.options().copyDefaults(true);
        saveConfig();

        _deepslateMineSpeed = (float) _config.getDouble("deepslate-mine-speed");
        _overwriteToolComponent = _config.getBoolean("overwrite-toolcomponent");
    }

    @Override
    public void onDisable() {}

    @SuppressWarnings("UnstableApiUsage")
    void applyInstaSlate(ItemStack i, Player p) {
        if (i == null) return;
        if (i.getType() != Material.NETHERITE_PICKAXE) return; // TODO: Add diamond pickaxe toggle to config

        ItemMeta m = i.getItemMeta();
        if (!i.hasItemMeta() || m == null) {
            m = getServer().getItemFactory().getItemMeta(Material.NETHERITE_PICKAXE);
        }

        @SuppressWarnings("ConstantConditions")
        ToolComponent tool = m.getTool(); // Can't be null so ignore warning

        if (!tool.getRules().isEmpty() && _overwriteToolComponent) {
            tool = getServer().getItemFactory().getItemMeta(Material.NETHERITE_PICKAXE).getTool(); // Reset tool rules
        }

        tool.setDamagePerBlock(1);
        tool.addRule(_otherMaterials, 9f, true); // ToolComponent does not come with default rules
        tool.addRule(Material.DEEPSLATE, _deepslateMineSpeed, true);

        // Not sure if meta is passed by ref
        m.setTool(tool);
        i.setItemMeta(m);
    }

    @EventHandler
    public void onItemHold(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        ItemStack i = e.getPlayer().getInventory().getItem(e.getNewSlot());

        applyInstaSlate(i, p);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();

        applyInstaSlate(i, p);
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        ItemStack i = e.getMainHandItem();

        applyInstaSlate(i, p);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        LivingEntity ent = e.getEntity();
        if (!(ent instanceof Player p))
            return;

        ItemStack i = e.getItem().getItemStack();
        applyInstaSlate(i, p);
        e.getItem().setItemStack(i); // Not sure if needed
    }
}
