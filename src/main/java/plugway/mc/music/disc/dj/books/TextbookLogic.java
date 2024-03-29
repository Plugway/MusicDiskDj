package plugway.mc.music.disc.dj.books;

import net.minecraft.client.gui.widget.ButtonWidget;
import plugway.mc.music.disc.dj.search.LinkValidator;

import java.util.ArrayList;
import java.util.List;

public class TextbookLogic {
    private static List<String> exported = new ArrayList<>();
    private static boolean awaitingExport = false;
    private static ButtonWidget importButton;
    private static boolean importButtonActive = true;
    private static ButtonWidget exportButton;
    private static boolean exportButtonActive = true;
    public static void setBookButtons(ButtonWidget widget1, ButtonWidget widget2){
        importButton = widget1;
        exportButton = widget2;
        importButton.active = importButtonActive;
        exportButton.active = exportButtonActive;
    }
    public static void disableAllButtons(){
        importButtonActive = false;
        exportButtonActive = false;
        if (importButton == null || exportButton == null)
            return;
        importButton.active = false;
        exportButton.active = false;
    }
    public static void enableAllButtons(){
        importButtonActive = true;
        exportButtonActive = true;
        if (importButton == null || exportButton == null)
            return;
        importButton.active = true;
        exportButton.active = true;
    }
    public static boolean isAwaitingExport(){
        return awaitingExport;
    }
    public static void clearState(){
        exported = new ArrayList<>();
        awaitingExport = false;
    }
    public static void setExported(List<String> exported) throws ExportException {
        var invalidId = isValid(exported);
        if (!invalidId.equals(""))
            throw new ExportException("Invalid Id: " + invalidId);
        TextbookLogic.exported = exported;
        awaitingExport = true;
    }
    public static List<String> getExported(){
        return exported;
    }
    private static String isValid(List<String> exported){
        String res = "";
        for (String id:exported) {
            if (id.equals(""))
                continue;
            if (!LinkValidator.isValidYTId(id)) {
                res = id;
                break;
            }
        }
        return res;
    }

}