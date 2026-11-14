package rafael.shineportals.mixin;

import net.minecraft.client.Minecraft;
import org.enginehub.worldeditcui.WorldEditCUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * O WorldEditCUI so cria o "controller" no FMLClientSetupEvent, mas ja escuta o tick do cliente.
 * Com muitos mods o jogo da ticks antes do fim do setup e o CUI cai com NullPointerException.
 * Aqui o tick e pulado enquanto o controller nao existe.
 */
@Pseudo
@Mixin(targets = "org.enginehub.worldeditcui.neoforge.NeoForgeModWorldEditCUI", remap = false)
public abstract class WorldEditCuiTickGuardMixin {
    @Shadow(remap = false)
    private WorldEditCUI controller;

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void shinePortals$esperarSetup(Minecraft mc, CallbackInfo ci) {
        if (this.controller == null) {
            ci.cancel();
        }
    }
}
