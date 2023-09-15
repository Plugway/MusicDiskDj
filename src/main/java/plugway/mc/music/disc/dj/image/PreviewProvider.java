package plugway.mc.music.disc.dj.image;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import plugway.mc.music.disc.dj.files.FileManager;
import plugway.mc.music.disc.dj.gui.MainGui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PreviewProvider {
    private static List<String> thumbnails = new ArrayList<>();
    private static final int desiredWidth = 360;
    private static final int desiredHeight = 197;

    public static Identifier getIdentifier(String previewUrl, String textureId) {
        BufferedImage preview;                                                                                          //downloading preview
        try (InputStream stream = new URL(previewUrl).openStream()) {
            preview = ImageIO.read(stream);
        } catch (IOException e) {
            System.out.println("Error while reading image: " + previewUrl);
            return new Identifier("mcmddj", "textures/blank.png");
        }
        preview = trimImage(preview);                                                                                   //trim
        var nativePreview = convertToNative(preview);
        var texturePreview = new NativeImageBackedTexture(nativePreview);
        var minecraftClient = MinecraftClient.getInstance();

        return minecraftClient.getTextureManager().registerDynamicTexture(textureId, texturePreview);
    }
    private static NativeImage convertToNative(BufferedImage image){
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), true);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int argbColor = image.getRGB(x, y);
                nativeImage.setColor(x, y, ((argbColor & 0xFF00FF00) | ((argbColor << 16) & 0xFF0000) | ((argbColor >> 16) & 0xFF)));
            }
        }
        return nativeImage;
    }

    public static BufferedImage trimImage(BufferedImage image){
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        // Calculate the scaling factors for width and height
        double widthScale = (double) desiredWidth / originalWidth;
        double heightScale = (double) desiredHeight / originalHeight;

        // Choose the maximum scaling factor to maintain aspect ratio
        double scale = Math.max(widthScale, heightScale);

        // Calculate the new dimensions after resizing
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        // Calculate the cropping offsets
        int offsetX = (newWidth - desiredWidth) / 2;
        int offsetY = (newHeight - desiredHeight) / 2;

        // Create a new BufferedImage with the desired dimensions
        BufferedImage resultImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);

        // Get the Graphics2D object of the new image
        Graphics2D g = resultImage.createGraphics();

        // Resize and crop the image in a single step
        g.drawImage(
                image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),
                -offsetX, -offsetY, null
        );

        // Dispose of the Graphics2D object
        g.dispose();
        return resultImage;
    }
}
