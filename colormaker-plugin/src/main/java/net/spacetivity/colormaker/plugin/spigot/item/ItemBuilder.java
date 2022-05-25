package net.spacetivity.colormaker.plugin.spigot.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public class ItemBuilder {

    protected ItemStack itemStack;
    private ItemMeta itemMeta;
    private Consumer<PlayerInteractEvent> clickAction;
    private final NamespacedKey idKey = new NamespacedKey(SpigotInitializer.getPlugin(SpigotInitializer.class), "id");

    protected final Gson gson = new GsonBuilder().create();

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = new ItemStack(itemStack);
        this.itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (!itemMeta.getPersistentDataContainer().has(idKey, PersistentDataType.INTEGER)) {
                itemMeta.getPersistentDataContainer().set(idKey, PersistentDataType.INTEGER,
                        ThreadLocalRandom.current().nextInt(5000000) + 1000);
                this.itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey idKey = new NamespacedKey(SpigotInitializer.getPlugin(SpigotInitializer.class), "id");
            if (!itemMeta.getPersistentDataContainer().has(idKey, PersistentDataType.INTEGER)) {
                itemMeta.getPersistentDataContainer().set(idKey, PersistentDataType.INTEGER,
                        ThreadLocalRandom.current().nextInt(5000000) + 1000);
                this.itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public void setData(String key, Object data) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(SpigotInitializer.getPlugin(SpigotInitializer.class), key);
            if (!itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
                //Gson gson = CoreAPI.GSON;
                itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, gson.toJson(data));
                this.itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public boolean hasData(String key) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        } else {
            NamespacedKey namespacedKey = new NamespacedKey(SpigotInitializer.getPlugin(SpigotInitializer.class), key);
            return itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
        }
    }

    public <T> T getData(String key, Class<T> tClass) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        } else {
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            NamespacedKey namespacedKey = new NamespacedKey(SpigotInitializer.getPlugin(SpigotInitializer.class), key);
            //Gson gson = CoreAPI.GSON;
            String jsonString = dataContainer.getOrDefault(namespacedKey, PersistentDataType.STRING, "");

            return gson.fromJson(jsonString, tClass);
        }
    }

    public int getId() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return 0;
        } else {
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            return dataContainer.getOrDefault(idKey, PersistentDataType.INTEGER, 0);
        }
    }

    public ItemBuilder onInteract(Consumer<PlayerInteractEvent> clickAction) {
        this.clickAction = clickAction;
        //SpigotConnector.interactiveItems.add(this);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof Damageable damageable) {
            damageable.setDamage(durability);
        }
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        this.itemMeta.displayName(Component.text(name));
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDisplayNameSpigot(String name) {
        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        this.itemMeta.setDisplayName(name);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setType(Material material) {
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemStack.setType(material);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        List<Component> lores = this.itemMeta.lore();
        assert lores != null;
        lores.add(Component.text(lore));
        this.itemMeta.lore(lores);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemStack.setAmount(amount);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder addEnchant(Enchantment enchantment, int strength) {
        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        this.itemMeta.addEnchant(enchantment, strength, true);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.itemMeta = this.itemStack.getItemMeta();

        if (!enchantments.isEmpty())
            enchantments.keySet().forEach(enchantment -> this.itemMeta.addEnchant(enchantment, enchantments.get(enchantment), true));

        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemflag) {
        this.itemMeta = this.itemStack.getItemMeta();
        Objects.requireNonNull(this.itemMeta).addItemFlags(itemflag);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setLores(List<String> lore) {
        this.itemMeta = this.itemStack.getItemMeta();
        Objects.requireNonNull(this.itemMeta).lore(lore.stream().map(Component::text).collect(Collectors.toList()));
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        this.itemMeta = this.itemStack.getItemMeta();
        Objects.requireNonNull(this.itemMeta).setUnbreakable(true);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setGlow() {
        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        this.itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        if (!glowing)
            return this;

        this.itemMeta = this.itemStack.getItemMeta();
        assert this.itemMeta != null;
        this.itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public String toBase64() {
        this.itemMeta = this.itemStack.getItemMeta();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(this.itemStack);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack", e);
        }
    }

    public ItemBuilder fromBase64(String from) {
        this.itemMeta = this.itemStack.getItemMeta();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(from));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            this.itemStack = ((ItemStack) dataInput.readObject());
            dataInput.close();
        } catch (java.io.IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemStack build() {
        return this.itemStack.clone();
    }

}