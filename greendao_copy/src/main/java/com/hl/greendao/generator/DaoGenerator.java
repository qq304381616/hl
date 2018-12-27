package com.hl.greendao.generator;

public class DaoGenerator {

    public static void main(String [] args) {
        Schema schema = new Schema(1, "com.greenrobot.testdao");
        Entity addressEntity = schema.addEntity("Addresse");
        Property idProperty = addressEntity.addIdProperty().getProperty();
        addressEntity.addIntProperty("count").index();
        addressEntity.addIntProperty("dummy").notNull();

    }
}