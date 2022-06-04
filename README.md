![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/spacetivityteam/NetworkColorMaker)
# NetworkColorManager
Plugin & API to provide a platform for Minecraft networks and servers to let their players choose the 
colors they want to use for prefixes and highlighting. (Available for: Spigot, Paper, Velocity & BungeeCord)

If you are using a proxy (velocity or bungeecord) the spigot server will send update (redis-)packets to the proxy server
when a player changes his primary or secondary color or when a new color was created.

## Control Inventories
![Color Inventory](https://imgur.com/gallery/A0Qe62V)

## Usage

#### There are two main classes for API usages. (ColorAPI.java & CacheAPI.java) With this classes you can access all important methods to update & manage player colors.

#### ColorAPI usage example
Here you can see an example to use the 'toPaper' method for the paper included kyori adventure api. If you are using the proxy software 'Velocity' there is a 'toVelocity' method also with the kyori adventure api to add messages to message-components.
````java
public void test(Player player) {
    NetworkColor primaryColor = ColorAPI.getPrimaryColor(player.getUniqueId());
    NetworkColor secondaryColor = ColorAPI.getSecondaryColor(player.getUniqueId());

    player.sendMessage(Component.text()
        .append(Component.text(player.getName()).color(primaryColor.toPaper()))
        .append(Component.text(" has the level: "))
        .append(Component.text(player.getLevel()).color(secondaryColor.toPaper()))
        .append(Component.text(" (For: Paper & Velocity)")));
}
````

Here you can see an example to use the 'toSpigot' to use colors in a string send by the sendMessage method of spigot / bukkit. If you are using the proxy software bungeecord too, there is the method 'toBungee' to use these colors in chat messages.
````java
public void test(Player player) {
    NetworkColor primaryColor = ColorAPI.getPrimaryColor(player.getUniqueId());
    NetworkColor secondaryColor = ColorAPI.getSecondaryColor(player.getUniqueId());

    player.sendMessage(primaryColor.toSpigot() + player.getName() + " §rhas the level: " + secondaryColor.toSpigot() + player.getLevel());
}
````

### ColorAPI Event
This event is fired when a player updated his primary or secondary color. This
can for example, help a server to update its scoreboard, when the player colors
change.
````java
@EventHandler
public void onColorUpdatePlayer(SpigotColorUpdateEvent event) {
    Player player = event.getPlayer();
    ColorPlayer colorPlayer = event.getColorPlayer();
    String primaryColor = colorPlayer.getPrimaryColor();
    String secondaryColor = colorPlayer.getSecondaryColor();
}
````


## Including API in your project

### Gradle
#### Groovy
````groovy
repositories {
    maven {
        url 'https://nexus.neptuns-world.com/repository/maven-group/'
    }
}

dependencies {
    implementation '-'
}
````
