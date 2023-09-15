package plugway.mc.music.disc.dj.books;


import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import java.util.List;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class TextbookLogic {                                                    //https://github.com/The-Fireplace-Minecraft-Mods/Textbook
    /*private static final Pattern NEWLINE_REGEX = Pattern.compile("\\R");

    public static List<String> toPages(List<String> lines) {
        List<String> pages = Lists.newArrayList();
        StringBuilder page = new StringBuilder();
        for (String line: lines) {
            if (fitsOnPage(page+line)) {
                //noinspection HardcodedLineSeparator
                page.append(line).append("\n");
            } else if (!fitsOnPage(line)) {
                String[] parts = line.split(" ");
                StringBuilder addPart = new StringBuilder();
                for (String part: parts) {
                    if (fitsOnPage(page + addPart.toString() + part + " ")) {
                        addPart.append(part).append(" ");
                    } else {
                        if (!page.toString().isEmpty() || !addPart.toString().isEmpty()) {
                            pages.add(page.append(addPart).toString());
                            page = new StringBuilder();
                            addPart = new StringBuilder();
                        }
                        if (fitsOnPage(part)) {
                            addPart.append(part).append(" ");
                        } else {
                            char[] chars = part.toCharArray();
                            for (char c : chars) {
                                if (!fitsOnPage(page.toString() + c)) {
                                    pages.add(page.toString());
                                    page = new StringBuilder();
                                }
                                page.append(c);
                            }
                            if (fitsOnPage(page + " ")) {
                                page.append(" ");
                            } else {
                                pages.add(page.toString());
                                page = new StringBuilder();
                            }
                        }
                    }
                }
                if (!addPart.toString().isEmpty()) {
                    page.append(addPart);
                }
            } else {
                pages.add(page.toString());
                page = new StringBuilder();
                //noinspection HardcodedLineSeparator
                page.append(line).append("\n");
            }
        }
        if (!page.toString().isEmpty()) {
            pages.add(page.toString());
        }

        return pages;
    }

    public static boolean fitsOnPage(String string) {
        return string.length() < 1024 && MinecraftClient.getInstance().textRenderer.getStringBoundedHeight(string, 114) <= 128;
    }*/
}