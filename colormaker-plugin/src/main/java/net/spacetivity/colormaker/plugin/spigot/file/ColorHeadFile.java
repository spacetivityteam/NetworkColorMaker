package net.spacetivity.colormaker.plugin.spigot.file;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ColorHeadFile extends FileAPI {

    public ColorHeadFile(JavaPlugin plugin) {
        super(plugin, "color_heads");
    }

    @Override
    public void insertDefaults(YamlConfiguration configuration) {
        configuration.set("color.green", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3R" +
                "leHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=");
        configuration.set("color.dark_green", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQub" +
                "mV0L3RleHR1cmUvNjZkNTNhNTc2ODVmMjVmMjkzYzdlOWM3YjQ0MThkNDUyZjY5MzFjNTNhOTg0OWEwYjNjYTEwOTM4NTU3NyJ9fX0=");
        configuration.set("color.yellow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L" +
                "3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==");
        configuration.set("color.gold", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3R" +
                "leHR1cmUvODViZWMxNTc0ZGUzNzNkZmI3NzVhYjU4YjJjMWU0NjIxZDkyYzZkYWFjN2M2YTc0ZDc4ZmI3MGZmZjRkMCJ9fX0=");
        configuration.set("color.black", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3" +
                "RleHR1cmUvMjM0MmI5YmY5ZjFmNjI5NTg0MmIwZWZiNTkxNjk3YjE0NDUxZjgwM2ExNjVhZTU4ZDBkY2ViZDk4ZWFjYyJ9fX0=");
        configuration.set("color.dark_gray", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubm" +
                "V0L3RleHR1cmUvMzZkYzZkNDI0OTU4MTdiOGQ0ZGIxMzExMTE1ZDFmNWNmYmVkZTY4MzIxOTJiNTJmMzJkNDhlZjdkOGI1ZCJ9fX0=");
        configuration.set("color.dark_purple", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQub" +
                "mV0L3RleHR1cmUvNmM0ZGM4MzM4NDE1YjJiOTg5YWI3OTQ5NTk4NmVjNzg5NWFhMDM2NzlkMmJjZGQ2ZDllYmI2MzNiODdmYzQifX19");
        configuration.set("color.dark_red", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV" +
                "0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=");
        configuration.set("color.light_purple", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZn" +
                "QubmV0L3RleHR1cmUvZGYzODdiNjlmOTRmZWVhMjU2NzFmYzM5OWViNzVkYmVhYzYwZjUxMmE0YmZlNzhlNDU0MTcxZWUzZmMwIn19fQ==");
        configuration.set("color.white", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L" +
                "3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=");
        configuration.set("color.gray", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQub" +
                "mV0L3RleHR1cmUvMWE0YjBlYWY1MjY5YTM0ZTI3OGNhZThiYzVjNmNiMTEyMDIzNDRhOTRiZDhmNGE3OWFlNmI4MTJjZDZjZmYifX19");
        configuration.set("color.dark_blue", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV" +
                "0L3RleHR1cmUvMWRhNzZkNmRkY2RiYjQ5NzMzMzIxNTU3OTZjMDMwY2ZhMmZlODU1Y2U1MzllODEzZTU4OGM2MWQ0ZDZiY2E1YyJ9fX0=");
        configuration.set("color.blue", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleH" +
                "R1cmUvZjQ3N2Y0NDM4OTM2MmM0Yzc2NGM4NDdhOTczOWJjNzhjMzI0NjdlYWI0ZTM4MzBhZTRjOGJlYWMzNDQyZWY5In19fQ==");
        configuration.set("color.dark_aqua", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubm" +
                "V0L3RleHR1cmUvNDgxODk5ZDA3ZWQ4YTU0NDFiYjNmYWYxZTE5ZGNjOTc1MzdjYmRmOTFjNzAzZWJmODVhYzMxYzg4MTk4ZmMifX19");
        configuration.set("color.aqua", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3Rl" +
                "eHR1cmUvMjllNGEzOTliZWZkOTY3ZWMyODJjNzE5ZmJmZDY0NDg2MzI4MTM3NGRjYjgxMzdhNzJmN2M0ZDAxM2E3M2YifX19");
        saveFile();
    }

    @Override
    public void cacheData(YamlConfiguration configuration) {

    }
}
