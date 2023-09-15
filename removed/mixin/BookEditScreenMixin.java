package plugway.mc.music.disc.dj.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.books.TextbookLogic;

import java.util.List;
import java.util.regex.Pattern;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {
    private static final Pattern CARRIAGE_RETURN = Pattern.compile("\\R");
    @Mutable
    @Shadow @Final private List<String> pages;

    @Shadow private String title;

    @Shadow private boolean dirty;

    @Shadow protected abstract void removeEmptyPages();

    @Shadow protected abstract void invalidatePageContent();

    @Shadow private boolean signing;

    @Shadow protected abstract void updateButtons();

    @Shadow @Final private SelectionManager field_24269;
    @Shadow private int currentPage;
    private ButtonWidget importClipboardButton;
    private boolean textbookButtonsInitialized = false;

    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    /*@Inject(at = @At(value="TAIL"), method = "init")
    private void init(CallbackInfo info) {
        importClipboardButton = this.addButton(new ButtonWidget(this.width / 2 - 100, 196 + 20 + 2, 98, 20,
                new TranslatableText("musicdiskdj.name.label.import"), this::importClipboardText));
        textbookButtonsInitialized = true;
        updateButtons();
    }

    @Inject(at = @At("TAIL"), method = "updateButtons")
    private void updateButtons(CallbackInfo ci) {
        if (textbookButtonsInitialized) {
            importClipboardButton.visible = !this.signing;
        }
    }

    private void importClipboardText(ButtonWidget buttonWidget) {
        assert this.client != null;
        updateButtons();
        //importText(Lists.newArrayList(CARRIAGE_RETURN.split(this.client.keyboard.getClipboard())));
    }

    private void importText(List<String> lines) {
        setPages(TextbookLogic.toPages(lines));
    }

    private void setPages(List<String> pages) {
        this.currentPage = 0;
        this.pages = pages;
        this.removeEmptyPages();
        this.field_24269.moveCaretToEnd();
        this.dirty = true;
        this.invalidatePageContent();
        updateButtons();
    }*/
}