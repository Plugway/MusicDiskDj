package plugway.mc.music.disc.dj.files;

import net.fabricmc.loader.api.FabricLoader;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static List<File> filesToArchive;
    private static File sourceFolder;

    public static boolean extractFilesForWork() {
        boolean result = true;
        try {
            Path modPath = Path.of("/");
            CodeSource codeSource = MusicDiskDj.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                try {
                    modPath = Path.of(codeSource.getLocation().toURI());
                } catch (URISyntaxException e) {
                    MusicDiskDj.LOGGER.severe("Something went seriously wrong and I don’t know what, honestly: " + e);
                    MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
                }
            }
            if (modPath.toString().trim().endsWith(".jar")){
                MusicDiskDj.LOGGER.info("Mod file:" + modPath);
                try {
                    JarArchiveInputStream inputStream = new JarArchiveInputStream(new FileInputStream(modPath.toString()));
                    JarArchiveEntry entry;
                    while ((entry = inputStream.getNextJarEntry()) != null) {
                        if (entry.getName().trim().equals("assets/mcmddj/templates/mcmddj_template.zip")){
                            var fileName = entry.getName().split("/");
                            File copied = new File(MusicDiskDj.modDirectoryPath, fileName[fileName.length-1]);
                            if (!copied.exists())
                                IOUtils.copy(inputStream, new FileOutputStream(copied));
                        }
                        if (entry.getName().trim().equals("assets/mcmddj/ytdl/yt-dlp.exe")){
                            var fileName = entry.getName().split("/");
                            File copied = new File(MusicDiskDj.modDirectoryPath, fileName[fileName.length-1]);
                            if (!copied.exists())
                                IOUtils.copy(inputStream, new FileOutputStream(copied));
                        }
                    }
                    inputStream.close();
                } catch (Exception e){
                    MusicDiskDj.LOGGER.severe("Something went seriously wrong and I don’t know what, honestly: " + e);
                    MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
                    result = false;
                }
            } else {//WHY is it here        idk maybe 6 month after: it's for ide
                modPath = FabricLoader.getInstance().getModContainer("mcmddj").orElse(null).getRootPaths().get(0);
                File copied = new File(MusicDiskDj.modDirectoryPath, "mcmddj_template.zip");
                if (!copied.exists())
                    FileUtils.copyFile(new File(modPath.toString()+"\\assets\\mcmddj\\templates\\mcmddj_template.zip"), copied);
                copied = new File(MusicDiskDj.modDirectoryPath, "yt-dlp.exe");
                if (!copied.exists())
                    FileUtils.copyFile(new File(modPath.toString()+"\\assets\\mcmddj\\ytdl\\yt-dlp.exe"), copied);
            }

            ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new FileInputStream(MusicDiskDj.modDirectoryPath+"\\mcmddj_template.zip"));
            ZipArchiveEntry entry;
            while ((entry = inputStream.getNextZipEntry()) != null){
                File curfile = new File(MusicDiskDj.templatePath, entry.getName());
                if (curfile.exists())
                    continue;
                if (entry.isDirectory()){
                    result = curfile.mkdir();
                    continue;
                }
                IOUtils.copy(inputStream, new FileOutputStream(curfile));
            }
        } catch (Exception e){
            MusicDiskDj.LOGGER.severe("Error while extracting files: " + e);
            MusicDiskDj.LOGGER.severe("Stack trace: " + ExceptionUtils.getStackTrace(e));
            result = false;
        }
        return result;
    }

    public static void archiveDirContents(File directory, File archive) throws IOException {
        filesToArchive = new ArrayList<>();
        sourceFolder = directory;
        generateFileList(directory);
        ZipParameters params = new ZipParameters();
        params.setCompressionMethod(CompressionMethod.STORE);
        createArchive(directory, new ZipFile(archive), params);
    }

    private static void createArchive(File root, ZipFile zipFile, ZipParameters params) throws ZipException {
        File[] files = root.listFiles();
        for (File file : files){
            if (file.isFile())
                zipFile.addFile(file, params);
            else
                zipFile.addFolder(file, params);
        }
    }

    private static void generateFileList(File node){
        if (node.isFile()) {
            filesToArchive.add(new File(node.toString().substring(sourceFolder.toString().length() + 1)));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }
}
