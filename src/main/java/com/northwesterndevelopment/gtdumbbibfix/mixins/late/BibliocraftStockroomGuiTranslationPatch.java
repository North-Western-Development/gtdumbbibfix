package com.northwesterndevelopment.gtdumbbibfix.mixins.late;

import com.northwesterndevelopment.gtdumbbibfix.TranslationHelper;
import jds.bibliocraft.gui.GuiStockCatalog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiStockCatalog.class)
public class BibliocraftStockroomGuiTranslationPatch {
    /**
     * Run custom translation logic when translating specifically items in the GUI
     *
     * @author North Western Development
     * @reason see above
     * @since 2025-08-25
     */
    @Redirect(
        method="func_73863_a(IIF)V",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 6
        )
    )
    public String onDrawScreenTranslate1(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }

    /**
     * Run custom translation logic when translating specifically items in the waypoint compass UI page in the GUI
     *
     * @author North Western Development
     * @reason see above
     * @since 2025-08-25
     */
    @Redirect(
        method="func_73863_a(IIF)V",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 9
        )
    )
    public String onDrawScreenTranslate2(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }

    /**
     * Run custom translation logic when translating specifically items in the waypoint compass chat message
     *
     * @author North Western Development
     * @reason see above
     * @since 2025-08-25
     */
    @Redirect(
        method = "sendCompassUpdatePacket(I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 0
        )
    )
    private static String onCompassTranslate(String text) {
        return TranslationHelper.customTranslationLogic(text);
    }
}
