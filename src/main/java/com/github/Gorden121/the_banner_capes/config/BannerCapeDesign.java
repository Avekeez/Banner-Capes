package com.github.Gorden121.the_banner_capes.config;

import com.github.Gorden121.the_banner_capes.BannerCapes;

public enum BannerCapeDesign {
    BANNER_CAPES_DESIGN(8, 8, 7, 6.5F),
    VANILLA_CAPE_DESIGN(7, 6, 6.5F, 6),
    CUSTOM_DESIGN(0, 0, 0, 0);

    private final float bannerSizeX;// = 8;
    private final float bannerSizeY;// = 8;
    private final float collarOffset;// = 7F;
    private final float capeOffset;// = 6.5F;

    BannerCapeDesign(float bannerSizeX, float bannerSizeY, float collarOffset, float capeOffset) {
        this.bannerSizeX = bannerSizeX;
        this.bannerSizeY = bannerSizeY;
        this.collarOffset = collarOffset;
        this.capeOffset = capeOffset;
    }

    public float getBannerSizeX() {
        if (this == CUSTOM_DESIGN) {
            return BannerCapes.config().customBannerCapeDesign.getCustomBannerSizeX();
        }
        return bannerSizeX;
    }

    public float getBannerSizeY() {
        if (this == CUSTOM_DESIGN) {
            return BannerCapes.config().customBannerCapeDesign.getCustomBannerSizeY();
        }
        return bannerSizeY;
    }

    public float getCollarOffset() {
        if (this == CUSTOM_DESIGN) {
            return BannerCapes.config().customBannerCapeDesign.getCustomCollarOffset();
        }
        return collarOffset;
    }

    public float getCapeOffset() {
        if (this == CUSTOM_DESIGN) {
            return BannerCapes.config().customBannerCapeDesign.getCustomCapeOffset();
        }
        return capeOffset;
    }
}


