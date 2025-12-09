# Overview
This is a spigot plugin that boosts the mining efficiency for netherite pickaxes against deepslate. It is fast enough to allow insta-mining with efficiency 5 and haste 2. 
Since this functions as a plugin replacing actual nbt data, it should out-perform loop based datapacks and properly inform client side mods.

Short warning though, it uses the ToolComponent feature and replaces all of the ToolComponent data on the pickaxe. Using another plugin that manipulates the ToolComponent will not work. Also the boost is permanent even if you delete the plugin.

# Configs?
Not yet, although I am planning on it.

# Bugs
- Pickaxes instantly added to the inventory (e.g. using /give, or other plugins) will not receive the boost untill you switch slots or relog.

# Building
You can build it yourself using ``mvn package``.
