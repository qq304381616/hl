package com.hl.base.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.hl.base.entity.BaseDataEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

    public static String getLetter(String name) {
        if (TextUtils.isEmpty(name)) return "#";
        String pinyin = new Pinyin().getPinYin(name).substring(0, 1).toUpperCase();
        return pinyin.matches("[A-Z]") ? pinyin : "#";
    }

    public static List<BaseDataEntity> getContacts(Context c) {
        List<BaseDataEntity> mAllContactsList = new ArrayList<>();
        Cursor phoneCursor = c.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (phoneCursor == null) return null;
        int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while (phoneCursor.moveToNext()) {
            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
            if (TextUtils.isEmpty(phoneNumber))
                continue;
            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
            BaseDataEntity data = new BaseDataEntity(1, contactName + " " + phoneNumber);
            mAllContactsList.add(data);
        }
        phoneCursor.close();
        for (BaseDataEntity s : mAllContactsList) {
            s.setFirst(Utils.getLetter(s.getInfo()));
        }
        Collections.sort(mAllContactsList, new FirstComparator());
        return mAllContactsList;
    }
}
