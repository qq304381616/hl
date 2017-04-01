package com.hl.systeminfo.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/3/16.
 * 获取通讯录 。推荐方式二。
 */
public class ContactUtils {


    static String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public static SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            // 其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    // 拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }

    /**
     * 加载联系人数据 方式一。
     */
    public static void loadContacts(final Context c) {
        try {
            // 插叙
            String queryTye[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key", "phonebook_label",
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
            ContentResolver resolver = c.getContentResolver();
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, queryTye, null, null,
                    "sort_key COLLATE LOCALIZED ASC");
            if (phoneCursor == null || phoneCursor.getCount() == 0) {
                Toast.makeText(c, "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
                return;
            }
            int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
            int PHONEBOOK_LABEL = phoneCursor.getColumnIndex("phonebook_label");
            int PHOTO_ID = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            if (phoneCursor.getCount() > 0) {
                List<SortModel> mAllContactsList = new ArrayList<SortModel>();
                while (phoneCursor.moveToNext()) {
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 头像id
                    long photoId = phoneCursor.getLong(PHOTO_ID);
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                    String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
                    String book = phoneCursor.getString(PHONEBOOK_LABEL);
                    SortModel sortModel = new SortModel(contactName, phoneNumber, sortKey);
                    // //优先使用系统sortkey取,取不到再使用工具取
                    // String sortLetters =
                    // getSortLetterBySortKey(sortKey);
                    // Log.i("main", "sortLetters:"+sortLetters);
                    // if (sortLetters == null) {
                    // sortLetters = getSortLetter(contactName);
                    // }
                    if (book == null) {
                        book = "#";
                    } else if (book.equals("#")) {
                        book = "#";
                    } else if (book.equals("")) {
                        book = "#";
                    }
                    sortModel.sortLetters = book;
                    sortModel.sortToken = parseSortKey(book);
                    mAllContactsList.add(sortModel);

                }
            }
            phoneCursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载联系人数据 方式二。
     */
    public void loadContacts2(final Context c) {
        ArrayList<SortModel> mAllContactsList = new ArrayList<SortModel>();

        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver resolver = c.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while (cursor.moveToNext()) {
            int contractID = cursor.getInt(0);
            StringBuilder sb = new StringBuilder("contractID=");
            sb.append(contractID);

            uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2", "phonebook_label"}, null, null, null);

            A a = new A();
            List<String> l = new ArrayList<String>();
            while (cursor1.moveToNext()) {
                String book = cursor1.getString(cursor1.getColumnIndex("phonebook_label"));
                a.setBook(book);
                a.setSortToken(parseSortKey(book));

                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
                    sb.append(",name=" + data1);
                    a.setName(data1);
                    a.setSortKey(data1);
                } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { //邮箱
                    sb.append(",email=" + data1);
                    l.add(data1);
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
                    sb.append(",phone=" + data1);
                    l.add(data1);
                }
            }
            if (l.size() > 0) {
                for (String s : l) {
                    SortModel sortModel = new SortModel(a.getName(), s, a.getName());
                    sortModel.sortLetters = a.getBook();
                    sortModel.sortToken = a.getSortToken();
                    mAllContactsList.add(sortModel);
                }
            }
            cursor1.close();
        }
        cursor.close();
    }

    class A {
        private String name;
        private List<String> account;
        private String sortKey;
        private String book;
        public SortToken sortToken;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAccount() {
            return account;
        }

        public void setAccount(List<String> account) {
            this.account = account;
        }

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public SortToken getSortToken() {
            return sortToken;
        }

        public void setSortToken(SortToken sortToken) {
            this.sortToken = sortToken;
        }
    }

}
