# Overview
This is a spigot plugin that boosts the mining efficiency for netherite pickaxes against deepslate. It is fast enough to allow insta-mining with efficiency 5 and haste 2. 
Since this functions as a plugin replacing actual nbt data, it should out-perform loop based datapacks and properly inform client side mods.

> [!Warning]
> This plugin uses the ToolComponent feature and replaces all of the ToolComponent data on the pickaxe. Using another plugin that manipulates the ToolComponent will not work. The mining boost is permanent, even if you delete the plugin.

# Configs?
The config only has two options:
```
deepslate-mine-speed: 45.0
overwrite-toolcomponent: false
```
- Deepslate mine speed determines the speed modifier at which to mine deepslate (45 works fine)
- Overwrite ToolComponent sets whether or not to overwrite previous changes. So if you already used the plugin with another speed and you now want to change it, you have to set this to true. This will also overwrite ToolComponent modifiers from other plugins.

# Bugs
- Pickaxes instantly added to the inventory (e.g. using /give, or other plugins) will not receive the boost untill you switch slots or relog.

# Building
You can build it yourself using ``mvn package``.
