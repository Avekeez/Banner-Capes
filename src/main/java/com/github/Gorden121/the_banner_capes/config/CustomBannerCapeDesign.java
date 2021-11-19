package com.github.Gorden121.the_banner_capes.config;

public class CustomBannerCapeDesign {

    private final float customBannerSizeX;// = 8;
    private final float customBannerSizeY;// = 8;
    private final float customCollarOffset;// = 7F;
    private final float customCapeOffset;// = 6.5F;

    public CustomBannerCapeDesign(float customBannerSizeX, float customBannerSizeY, float customCollarOffset, float customCapeOffset) {
        this.customBannerSizeX = customBannerSizeX;
        this.customBannerSizeY = customBannerSizeY;
        this.customCollarOffset = customCollarOffset;
        this.customCapeOffset = customCapeOffset;
    }

    public CustomBannerCapeDesign(BannerCapeDesign bannerCapeDesign) {
        this.customBannerSizeX = bannerCapeDesign.getBannerSizeX();
        this.customBannerSizeY = bannerCapeDesign.getBannerSizeY();
        this.customCollarOffset = bannerCapeDesign.getCapeOffset();
        this.customCapeOffset = bannerCapeDesign.getCollarOffset();
    }

    public float getCustomBannerSizeX() {
        return customBannerSizeX;
    }

    public float getCustomBannerSizeY() {
        return customBannerSizeY;
    }

    public float getCustomCollarOffset() {
        return customCollarOffset;
    }

    public float getCustomCapeOffset() {
        return customCapeOffset;
    }
}
