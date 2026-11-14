package rafael.shineportals.mixin;

import com.bloom.client.shader.BloomShaderInjection;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.shaders.Program;
import java.io.InputStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Shine e Immersive Portals usam @Redirect na mesma leitura do shader
 * (IOUtils.toString em Program.compileShaderInternal) e so um pode vencer.
 * Aqui o Immersive Portals fica com o @Redirect e a transformacao do Shine
 * roda logo depois, sobre o texto ja lido (variavel local 5, apos o STORE).
 * O ProgramMixin do Shine precisa sair do shine.mixins.json.
 */
@Mixin(Program.class)
public abstract class ProgramChainMixin {
    private static final ThreadLocal<Object[]> shinePortals$args = new ThreadLocal<>();

    @Inject(method = "compileShaderInternal", at = @At("HEAD"))
    private static void shinePortals$guardar(Program.Type type, String name, InputStream data,
            String sourceName, GlslPreprocessor processor, CallbackInfoReturnable<Integer> cir) {
        shinePortals$args.set(new Object[] {type, name, sourceName, processor});
    }

    @ModifyVariable(method = "compileShaderInternal", at = @At(value = "STORE", ordinal = 0), index = 5)
    private static String shinePortals$aplicarShine(String source) {
        Object[] a = shinePortals$args.get();
        shinePortals$args.remove();
        if (a == null || source == null) {
            return source;
        }
        return BloomShaderInjection.transformVanillaShader((Program.Type) a[0], (String) a[1], source,
                (GlslPreprocessor) a[3], (String) a[2]);
    }
}
