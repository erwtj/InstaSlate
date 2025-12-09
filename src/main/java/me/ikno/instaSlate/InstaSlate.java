package me.ikno.instaSlate;

import org.bukkit.*;
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
    private Set<Material> _otherMaterials;

    @Override
    public void onLoad() {
        _otherMaterials = new HashSet<>(Tag.MINEABLE_PICKAXE.getValues());
        _otherMaterials.remove(Material.DEEPSLATE);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
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
            p.sendMessage("Created new item meta");
        }

        @SuppressWarnings("ConstantConditions")
        ToolComponent tool = m.getTool(); // Can't be null so ignore warning

        tool.addRule(_otherMaterials, 9f, true); // ToolComponent does not come with default rules
        tool.addRule(Material.DEEPSLATE, 31f, true); // TODO: Add config option for insta-mine speed

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
