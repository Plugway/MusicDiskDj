package plugway.mc.music.disc.dj.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.books.ExportException;
import plugway.mc.music.disc.dj.books.TextbookLogic;
import plugway.mc.music.disc.dj.config.ConfigurationManager;
import plugway.mc.music.disc.dj.gui.animation.AnimCollection;

import java.util.List;


@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {
    @Shadow
    private int currentPage;
    @Shadow
    private final List<String> pages = Lists.newArrayList();
    @Shadow
    private String title;


    @Shadow @Final private SelectionManager currentPageSelectionManager;
    @Shadow private ButtonWidget signButton;
    @Shadow private ButtonWidget doneButton;
    private ButtonWidget importButton;
    private ButtonWidget exportButton;

    public BookEditScreenMixin(Text title) {
        super(title);
    }


    @Inject(at = @At("RETURN"), method = "init")
    public void init(CallbackInfo ci) {
        importButton = ButtonWidget.builder(Text.translatable("musicdiskdj.name.label.button.import"), (button) -> {

            pages.clear();
            pages.addAll(ConfigurationManager.getAll());
            this.currentPageSelectionManager.insert('#');
            this.keyPressed(GLFW.GLFW_KEY_BACKSPACE, 0, 0);

        }).position(this.doneButton.getX(), this.doneButton.getY()+this.doneButton.getHeight()+4)
                .size(this.doneButton.getWidth(), this.doneButton.getHeight()).build();
        exportButton = ButtonWidget.builder(Text.translatable("musicdiskdj.name.label.button.export"), (button) -> {
            try {
                TextbookLogic.setExported(pages);
                AnimCollection.exportDoneAnimAsync(exportButton);
            } catch (ExportException e){
                MusicDiskDj.LOGGER.info("Error when exporting ids from book: " + e);
                AnimCollection.exportErrAnimAsync(exportButton);
            }
        }).position(this.signButton.getX(), this.signButton.getY()+this.signButton.getHeight()+4)
                .size(this.signButton.getWidth(), this.signButton.getHeight()).build();
        this.addDrawableChild(importButton);
        this.addDrawableChild(exportButton);
        TextbookLogic.setBookButtons(importButton, exportButton);
    }
}