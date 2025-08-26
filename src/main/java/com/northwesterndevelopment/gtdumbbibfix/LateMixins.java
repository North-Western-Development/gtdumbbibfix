package com.northwesterndevelopment.gtdumbbibfix;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class LateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.gtdumbbibfix.late.json";
    }

    @Nonnull
    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();

        if (loadedMods != null && !loadedMods.isEmpty() && loadedMods.contains("BiblioCraft")) {
            mixins.add("late.BibliocraftStockroomGuiTranslationPatch");
            mixins.add("late.BibliocraftSortingHelperTranslationPatch");
            MyMod.LOG.info("Bibliocraft present, patching.");
        }

        return mixins;
    }
}
