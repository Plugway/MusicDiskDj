package plugway.mc.music.disc.dj.image;

import plugway.mc.music.disc.dj.image.colorThief.ColorThief;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

public class TextureCreator {
    public static void modifyTexture(Image preview, File textureFile){
        try {
            Image texture = ImageIO.read(textureFile);
            BufferedImage bufferedTexture = toBufferedImage(texture);
            BufferedImage bufferedPreview = toBufferedImage(preview);
            int[] previewColor = ColorThief.getColor(bufferedPreview);
            HSV previewColorHSV = new HSV(previewColor[0], previewColor[1], previewColor[2]);
            int[][] textureColor = ColorThief.getPalette(bufferedTexture, 5);//getColor(bufferedTexture);
            int index = 0;
            for (; index < textureColor.length; index++){
                if (textureColor[index][0] != textureColor[index][1] || textureColor[index][0] != textureColor[index][2])
                    break;
            }
            HSV textureColorHSV = new HSV(textureColor[index][0], textureColor[index][1], textureColor[index][2]);
            double hueShift = previewColorHSV.getHue()-textureColorHSV.getHue();
            for (int x = 0; x < bufferedTexture.getWidth(null); x++){
                for (int y = 0; y < bufferedTexture.getHeight(null); y++){
                    Color initPixel = new Color(bufferedTexture.getRGB(x, y), true);
                    HSV pixel = new HSV(initPixel.getRed(), initPixel.getGreen(), initPixel.getBlue());
                    int[] modPixel = new HSV(pixel.getHue()+hueShift,
                            (pixel.getSaturation()+previewColorHSV.getSaturation())/2,
                            (pixel.getValue()+previewColorHSV.getValue())/2).toRGB();
                    bufferedTexture.setRGB(x, y, new Color(modPixel[0], modPixel[1], modPixel[2], initPixel.getAlpha()).getRGB());
                }
            }
            ImageIO.write(bufferedTexture, "png", textureFile);
        } catch (Exception ignored){}
    }

    private static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    private static void setAlpha(byte alpha, BufferedImage obj_img, int x, int y) {
        alpha %= 0xff;
        int color = obj_img.getRGB(x, y);
        int mc = (alpha << 24) | 0x00ffffff;
        int newcolor = color & mc;
        obj_img.setRGB(x, y, newcolor);
    }
}
