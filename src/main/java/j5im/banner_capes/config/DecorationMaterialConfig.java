package j5im.banner_capes.config;

import j5im.banner_capes.utils.ColorUtils;

public class DecorationMaterialConfig {

    public final String texturePath;
    public final String itemIdentifier;
    public final int colorR, colorG, colorB;
    public final boolean isNubbin, isCollar;

    public DecorationMaterialConfig(String texturePath, String itemIdentifier, int colorR, int colorG, int colorB, boolean isNubbin, boolean isCollar) {
        this.texturePath = texturePath;
        this.itemIdentifier = itemIdentifier;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.isNubbin = isNubbin;
        this.isCollar = isCollar;
    }
}
