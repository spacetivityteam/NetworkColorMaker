package net.spacetivity.colormaker.plugin.spigot.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder {

    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
    }

    public SkullBuilder setOwnerWithGameProfile(String hash) {
        new SkullProfile(hash).applyTextures(this.itemStack);
        return this;
    }

    static class SkullProfile {

        private final GameProfile gameProfile;

        SkullProfile(String hash) {
            this.gameProfile = new GameProfile(UUID.randomUUID(), null);
            PropertyMap propertyMap = this.gameProfile.getProperties();
            propertyMap.put("textures", new Property("textures", hash));
        }

        void applyTextures(ItemStack itemStack) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            assert skullMeta != null;
            Class<?> c_skullMeta = skullMeta.getClass();
            try {
                Field f_profile = c_skullMeta.getDeclaredField("profile");
                f_profile.setAccessible(true);
                f_profile.set(skullMeta, this.gameProfile);
                f_profile.setAccessible(false);
                itemStack.setItemMeta(skullMeta);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
