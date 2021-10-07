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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class whes1015 extends PluginBase implements Listener {

    public String a="";
    private final int vercode=300;
    private final String vername="V 3.0.0-stable";

    @Override
    public void onEnable() {

        URL url = null;
        try {
            url = new URL("http://exptech.mywire.org/Nukkit-Engineer.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            assert url != null;
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert http != null;
            http.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            InputStream input = http.getInputStream();
            byte[] data = new byte[1024];
            int idx = input.read(data);
            String str = new String(data, 0, idx);
            int x = Integer.parseInt(str);
            if(vercode < x) {
                this.getLogger().warning(TextFormat.RED + "Please Update Your Plugin! "+vername);
                this.getLogger().info(TextFormat.RED + "DownloadLink: https://github.com/ExpTech-tw/Nukkit-Engineer/tags");
                this.getPluginLoader().disablePlugin(this);
            }else{
                this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Update Checking Success! "+vername);
                this.getLogger().info(TextFormat.BLUE + "Nukkit-Engineer Loading! - Designed by ExpTech.tw (whes1015) "+vername);
                this.getServer().getPluginManager().registerEvents(this, this);

            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        http.disconnect();

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
        this.getLogger().info(String.valueOf(event.getBlock().getId()));

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