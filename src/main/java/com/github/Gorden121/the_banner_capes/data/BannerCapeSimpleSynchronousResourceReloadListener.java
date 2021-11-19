package com.github.Gorden121.the_banner_capes.data;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BannerCapeSimpleSynchronousResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return new Identifier(BannerCapes.MOD_ID, "banner_cape_materials");
    }

    @Override
    public void reload(ResourceManager manager) {

        List<DecorationMaterialDataItem> decorationMaterialDataItemList = new ArrayList<>();

        for(Identifier id : manager.findResources("banner_materials", path -> path.endsWith(".json"))) {
            try(InputStream stream = manager.getResource(id).getInputStream()) {
                // Consume the stream however you want, medium, rare, or well done.

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                for (int length; (length = stream.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }
                Gson gson = new Gson();
                decorationMaterialDataItemList.add(gson.fromJson(result.toString(), DecorationMaterialDataItem.class));

            } catch(Exception e) {
                BannerCapes.log(Level.ERROR,"Error occurred while loading resource json " + id.toString() + " " + e.toString());
            }
        }

        BannerCapes.bannerCapeMaterialsData.validateNewDataSet(decorationMaterialDataItemList.toArray(DecorationMaterialDataItem[]::new));
    }
}
