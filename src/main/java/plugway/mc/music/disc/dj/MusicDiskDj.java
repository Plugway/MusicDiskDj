package plugway.mc.music.disc.dj;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import plugway.mc.music.disc.dj.files.FileManager;
import plugway.mc.music.disc.dj.keybinds.OpenMenu;

import java.io.File;

public class MusicDiskDj implements ModInitializer {

    public static final String mcDirectoryPath = MinecraftClient.getInstance().runDirectory.toPath().normalize().toString();
    public static final String modDirectoryPath = mcDirectoryPath+"\\musicDiskDj";
    public static final String tempPath = modDirectoryPath+"\\temp";
    public static final String templatePath = modDirectoryPath+"\\template";
    public static final String resultPath = tempPath+"\\result";
    @Override
    public void onInitialize() {
        if (!createModFolders()) //if can't create directory
            return;
        initKeyBinds();
    }

    private void initKeyBinds(){
        OpenMenu.initOpenMenu();
    }
    private boolean createModFolders(){
        boolean result = true;
        File modDir = new File(modDirectoryPath);
        File tempDir = new File(tempPath);
        File templateDir = new File(templatePath);
        File resultDir = new File(resultPath);
        if (!modDir.exists())
            result = modDir.mkdir();
        if (!tempDir.exists() && result)
            result = tempDir.mkdir();
        if (!templateDir.exists() && result)
            result = templateDir.mkdir();
        if (!resultDir.exists() && result)
            result = resultDir.mkdir();
        if (result)
            result = FileManager.extractFilesForWork();
        return result;
    }
}
