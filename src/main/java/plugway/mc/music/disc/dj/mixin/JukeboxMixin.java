package plugway.mc.music.disc.dj.mixin;

import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.MusicDiscItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxMixin {
    @Inject(method = "isSongFinished", at = @At("HEAD"), cancellable = true)
    private void preventMusicStop(MusicDiscItem musicDisc, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}