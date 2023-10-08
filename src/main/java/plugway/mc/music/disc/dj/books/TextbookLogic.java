package plugway.mc.music.disc.dj.books;


import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import plugway.mc.music.disc.dj.mixin.IBookEditScreen;
import plugway.mc.music.disc.dj.search.LinkValidator;


import java.util.ArrayList;
import java.util.List;

public class TextbookLogic {
    private static List<String> exported = new ArrayList<>();
    private static boolean awaitingExport = false;
    private static BookEditScreen bookEditScreen;
    public static void setBookEditScreen(BookEditScreen screen){
        bookEditScreen = screen;
    }
    public static void disableAllButtons(){
        ((IBookEditScreen) bookEditScreen).disableAllMdDjButtons();
    }
    public static void enableAllButtons(){
        ((IBookEditScreen) bookEditScreen).enableAllMdDjButtons();
    }
    public static boolean isAwaitingExport(){
        return awaitingExport;
    }
    public static void clearState(){
        exported = new ArrayList<>();
        awaitingExport = false;
    }
    public static void setExported(List<String> exported) throws Exception {
        if (!isValid(exported))
            throw new Exception();
        TextbookLogic.exported = exported;
        awaitingExport = true;
    }
    public static List<String> getExported(){
        return exported;
    }
    private static boolean isValid(List<String> exported){
        boolean res = true;
        for (String id:exported) {
            if (id.equals(""))
                continue;
            if (!LinkValidator.isValidYTId(id)) {
                res = false;
                break;
            }
        }
        return res;
    }

}