package com.hl.greendao.example;

import com.hl.base.BaseApplication;
import com.hl.greendao.core.database.Database;
import com.hl.greendao.gen.DaoMaster;
import com.hl.greendao.gen.DaoSession;

public class GreenDaoUtil extends BaseApplication {

    private static GreenDaoUtil instance;
    private DaoSession daoSession;

    private GreenDaoUtil() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), "test_db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static GreenDaoUtil getInstance() {
        if (instance == null) {
            instance = new GreenDaoUtil();
        }
        return instance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
