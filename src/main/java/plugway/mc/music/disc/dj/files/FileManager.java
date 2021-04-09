package plugway.mc.music.disc.dj.files;

import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
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
import plugway.mc.music.disc.dj.MusicDiskDj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager {

    private static List<File> filesToArchive;
    private static File sourceFolder;

    public static boolean extractFilesForWork() {
        boolean result = true;
        try {
            FabricLoader loader = FabricLoader.INSTANCE;
            Optional<ModContainer> mod = loader.getModContainer("mcmddj");
            Path modPath = mod.get().getRootPath();
            if (modPath.endsWith(".jar")){
                try {
                    JarArchiveInputStream inputStream = new JarArchiveInputStream(new FileInputStream(modPath.toString()));
                    JarArchiveEntry entry;
                    while ((entry = inputStream.getNextJarEntry()) != null) {
                        if (entry.getName().equals("mcmddj_template.zip")){
                            File copied = new File(MusicDiskDj.modDirectoryPath, entry.getName());
                            if (!copied.exists())
                                IOUtils.copy(inputStream, new FileOutputStream(copied));
                        }
                        if (entry.getName().equals("youtube-dl.exe")){
                            File copied = new File(MusicDiskDj.modDirectoryPath, entry.getName());
                            if (!copied.exists())
                                IOUtils.copy(inputStream, new FileOutputStream(copied));
                        }
                    }
                } catch (Exception e){
                    result = false;
                }
            } else {
                File copied = new File(MusicDiskDj.modDirectoryPath, "mcmddj_template.zip");
                if (!copied.exists())
                    FileUtils.copyFile(new File(modPath.toString()+"\\assets\\mcmddj\\templates\\mcmddj_template.zip"), copied);
                copied = new File(MusicDiskDj.modDirectoryPath, "youtube-dl.exe");
                if (!copied.exists())
                    FileUtils.copyFile(new File(modPath.toString()+"\\assets\\mcmddj\\ytdl\\youtube-dl.exe"), copied);
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

    public static void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (File file: filesToArchive) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(file.toString());
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
