package com.hl.greendao.core;

import android.database.sqlite.SQLiteException;

import com.hl.utils.L;

public class DaoException extends SQLiteException {

    private static final long serialVersionUID = -5877937327907457779L;

    public DaoException(String error) {
        super(error);
    }

    public DaoException(String error, Throwable cause) {
        super(error);
        safeInitCause(cause);
    }

    public DaoException(Throwable th) {
        safeInitCause(th);
    }

    protected void safeInitCause(Throwable cause) {
        try {
            initCause(cause);
        } catch (Throwable e) {
            L.e("Could not set initial cause", e);
            L.e("Initial couse is:", cause);
        }
    }
}
