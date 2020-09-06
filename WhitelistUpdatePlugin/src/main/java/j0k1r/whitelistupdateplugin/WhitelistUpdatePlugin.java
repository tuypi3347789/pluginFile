package j0k1r.whitelistupdateplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WhitelistUpdatePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        File file = new File("D://MinecraftServer/whitelist.json");

        // 首先文件的最近一次修改时间戳
        final long[] lastTime = {file.lastModified()};

        // 定时任务，每秒来判断一下文件是否发生变动，即判断lastModified是否改变
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (file.lastModified() > lastTime[0]) {
                    getServer().reloadWhitelist();
                    getLogger().info("file update! time : " + file.lastModified());
                    lastTime[0] = file.lastModified();

                }
            }
        },0, 1, TimeUnit.SECONDS);


        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
