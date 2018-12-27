package com.hl.greendao.generator;

import java.io.File;

public class DaoGenerator {

    public static void main(String [] args) {
        Schema schema = new Schema(1, "com.greenrobot.testdao");
        Entity addressEntity = schema.addEntity("Addresse");
        Property idProperty = addressEntity.addIdProperty().getProperty();
        addressEntity.addIntProperty("count").index();
        addressEntity.addIntProperty("dummy").notNull();

        new DaoGenerator().generateAll(schema, "");
    }

    public void generateAll( Schema schema, String outDir){
        generateAll(schema, outDir, null, null);
    }

    public void generateAll(Schema schema, String outDir, String outDirEntity, String outDirTest) {

        File outDirFile = new File(outDir);
        if (!outDirFile.exists()) {
            outDirFile.mkdirs();
        }

        schema.init2ndPass();
        schema.init3rdPass();
    }
}