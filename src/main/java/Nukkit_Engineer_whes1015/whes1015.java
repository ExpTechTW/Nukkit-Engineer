package Nukkit_Engineer_whes1015;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class whes1015 extends PluginBase implements Listener {

    public String a="";
    private final int vercode=305;
    private final String vername="V 3.0.5-stable";

    @Override
    public void onEnable() {

        String webPage = "https://api.github.com/repos/ExpTechTW/Nukkit-Engineer/releases";

        InputStream is = null;
        try {
            is = new URL(webPage).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        JsonElement jsonElement = new JsonParser().parse(reader);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        saveDefaultConfig();
        if (jsonObject.get("tag_name").toString() != vername) {
            if ((getConfig().getString("BetaVersion") == "true" && jsonObject.get("prerelease").getAsBoolean() == true) || (getConfig().getString("BetaVersion") == "false" && jsonObject.get("prerelease").getAsBoolean() == false)) {
                this.getLogger().warning(TextFormat.RED + "Please Update Your Plugin ! " + vername);
                this.getLogger().info(TextFormat.RED + "DownloadLink: https://github.com/ExpTechTW/Nukkit-Engineer/releases");
                this.getPluginLoader().disablePlugin(this);
            } else {
                this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Update Checking Success! " + vername);
                this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Loading! - Designed by ExpTech.tw (whes1015) " + vername);
                this.getServer().getPluginManager().registerEvents(this, this);
            }
        } else {
            this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Update Checking Success! " + vername);
            this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Loading! - Designed by ExpTech.tw (whes1015) " + vername);
            this.getServer().getPluginManager().registerEvents(this, this);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Closing! VersionName:"+vername+" VersionCode:"+vercode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Config config = new Config(new File(getDataFolder() + "/spawnData/" + event.getPlayer().getName() + ".yml"), Config.YAML);
        if (config.get(event.getPlayer().getName() + "X") != null) {
            this.getLogger().info(TextFormat.BLUE + event.getPlayer().getName() + "'s Location Data Loaded! " + vername);
            config.get(event.getPlayer().getName() + "X");
            event.getPlayer().teleport(new Vector3((Double) config.get(event.getPlayer().getName() + "X"), (Double) config.get(event.getPlayer().getName() + "Y"), (Double) config.get(event.getPlayer().getName() + "Z")));
        }else{
            this.getLogger().info(TextFormat.BLUE + event.getPlayer().getName() + "'s Location Data Unfind!(the Location Data will create when this player leave the server) " + vername);
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        File file = new File(getDataFolder() + "/spawnData/"+event.getPlayer().getName()+".yml");
        Config config = new Config(new File(getDataFolder() + "/spawnData/"+event.getPlayer().getName()+".yml"), Config.YAML);
        config.setAll(new ConfigSection(){
            {
                put(event.getPlayer().getName()+"X", event.getPlayer().getX());
                put(event.getPlayer().getName()+"Y", event.getPlayer().getY());
                put(event.getPlayer().getName()+"Z", event.getPlayer().getZ());
            }
        });
        config.save();
        this.getLogger().info(TextFormat.BLUE + event.getPlayer().getName()+"'s Location Data Saved! "+vername);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (String.valueOf(event.getInventory()).contains("FurnaceInventory")) {
            if (event.getSourceItem().getCount() > 0 && event.getSlot() == 2) {

                event.getPlayer().addExperience(event.getSourceItem().getCount());

            }
        }

    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {

        if(event.getBlock().getId()==118){
            if(!a.contains(event.getPlayer().getName())) {
                a = event.getPlayer().getName() + a;

            }
        }
    }
    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent event){

        if(a.contains(event.getPlayer().getName())&&event.getItem().getId()==0) {

            event.getPlayer().giveItem(Item.fromString("glass_bottle"));
            a = a.replace(event.getPlayer().getName(), "");

        }

    }

}