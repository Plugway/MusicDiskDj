package plugway.mc.music.disc.dj.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plugway.mc.music.disc.dj.books.TextbookLogic;
import plugway.mc.music.disc.dj.config.ConfigurationManager;

import java.util.List;


@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen implements IBookEditScreen {
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
        TextbookLogic.setBookEditScreen((BookEditScreen)(Object)this);
        importButton = ButtonWidget.builder(Text.translatable("musicdiskdj.name.label.button.import"), (button) -> {

            pages.clear();
            pages.addAll(ConfigurationManager.getAll());
            this.currentPageSelectionManager.insert('#');
            this.keyPressed(GLFW.GLFW_KEY_BACKSPACE, 0, 0);

        }).position(this.doneButton.getX(), this.doneButton.getY()+this.doneButton.getHeight()+4)
                .size(this.doneButton.getWidth(), this.doneButton.getHeight()).build();

        exportButton = ButtonWidget.builder(Text.translatable("musicdiskdj.name.label.button.export"), (button) -> {

            System.out.println("Yay, you exported something(but I don't know what and where)");

            try {
                TextbookLogic.setExported(pages);
            } catch (Exception e){
                exportErrAnim();
            }
        }).position(this.signButton.getX(), this.signButton.getY()+this.signButton.getHeight()+4)
                .size(this.signButton.getWidth(), this.signButton.getHeight()).build();

        this.addDrawableChild(importButton);
        this.addDrawableChild(exportButton);
    }
    private void exportErrAnim(){
        new Thread(() -> {
            exportButton.active = false;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.error"));
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            exportButton.active = true;
            exportButton.setMessage(Text.translatable("musicdiskdj.name.label.button.export"));
        }).start();
    }
    public void disableAllMdDjButtons(){
        exportButton.active = false;
        importButton.active = false;
    }
    public void enableAllMdDjButtons(){
        exportButton.active = true;
        importButton.active = true;
    }
}