package com.github.Gorden121.the_banner_capes.utils;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ColorUtils {
    public static int RGBtoDecimalColor(int r, int g, int b) {

        return (r << 16) + (g << 8) + (b);
    }

    public static int RGBtoDecimalColor(int[] rgb) {
        return (rgb[0] << 16) + (rgb[1] << 8) + (rgb[2]);
    }

    public static final boolean useSaturation = true;

    public static int[] getSpecificColor (InputStream inputStream) throws IOException {

        ImageInputStream is = ImageIO.createImageInputStream(inputStream);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(is);

        ImageReader imageReader = iter.next();
        imageReader.setInput(is);

        BufferedImage image = imageReader.read(0);

        int height = image.getHeight();
        int width = image.getWidth();

        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0; i < width ; i++)
        {
            for(int j=0; j < height ; j++)
            {
                int rgb = image.getRGB(i, j);


                    if(map.containsKey(rgb)) {
                        int counter = map.get(rgb);
                        counter++;
                        map.replace(rgb,counter);
                    }
                    else
                    {
                        map.put(rgb, 1);
                    }
            }
        }
        List<Pair<Integer,Integer>> rgbCompareList = new ArrayList<>();
        if(useSaturation) {
            map.forEach((integer, integer2) -> rgbCompareList.add(new MutablePair<>(integer,integer2)));
            return getMostSaturatedColor(rgbCompareList);
        }
        else {
            map.forEach((integer, integer2) -> rgbCompareList.add(new MutablePair<>(integer,getSaturation(getRGBArr(integer)))));
            return getMostCommonColor(rgbCompareList);
        }
    }

    private static int[] getMostCommonColor(List<Pair<Integer,Integer>> rgbCompareList) {

        rgbCompareList.sort(Map.Entry.comparingByValue());

        if(rgbCompareList.size() > 0) {
        Pair<Integer, Integer> me = rgbCompareList.get(rgbCompareList.size()-1);
        return getRGBArr(me.getKey());
        }
        else
            return  getRGBArr(RGBtoDecimalColor(255,20,147));
    }

    private static int[] getMostSaturatedColor(List<Pair<Integer,Integer>> rgbCompareList) {

        rgbCompareList.sort(Map.Entry.comparingByValue());

        if(rgbCompareList.size() > 0) {
            Pair<Integer, Integer> me = rgbCompareList.get(rgbCompareList.size()-1);
            return getRGBArr(me.getKey());
        }
        else
            return  getRGBArr(RGBtoDecimalColor(255,20,147));
    }

    public static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};
    }

    public static int getSaturation(int[] rgb) {
        return (getMaxColor(rgb)-getMinColor(rgb)) / (getMaxColor(rgb)+getMinColor(rgb));
    }

    public static int getMaxColor(int[] rgb) {
        int val = Math.max(rgb[0], rgb[1]);
        val = Math.max(val, rgb[2]);

        return  val;
    }

    public static int getMinColor(int[] rgb) {
        int val = Math.min(rgb[0], rgb[1]);
        val = Math.min(val, rgb[2]);

        return  val;
    }
}
