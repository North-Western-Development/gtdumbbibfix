package com.northwesterndevelopment.gtdumbbibfix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;

@Mod(modid = MyMod.MODID, version = Tags.VERSION, name = "Bibliocraft Item Translation Patches", acceptedMinecraftVersions = "[1.7.10]", dependencies = "after:BiblioCraft@1.11.7")
public class MyMod {
    public static final String MODID = "gtdumbbibfix";
    public static final Logger LOG = LogManager.getLogger(MODID);
}
