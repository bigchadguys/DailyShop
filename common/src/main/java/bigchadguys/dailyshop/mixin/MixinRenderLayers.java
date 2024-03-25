package bigchadguys.dailyshop.mixin;

import bigchadguys.dailyshop.init.ModRenderers;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RenderLayers.class)
public class MixinRenderLayers {

    @Shadow @Final private static Map<Block, RenderLayer> BLOCKS;

    @Inject(method = "setFancyGraphicsOrBetter", at = @At("TAIL"))
    private static void setFancyGraphicsOrBetter(boolean fancyGraphicsOrBetter, CallbackInfo ci) {
        ModRenderers.RenderLayers.register(BLOCKS, fancyGraphicsOrBetter);
    }

}
