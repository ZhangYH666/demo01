package cx.study.demo_01.SqLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.SqLite
 * @ClassName: SQLDate
 * @Author: ZYH
 * @CreateDate: 2020/3/20 0020 16:02
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class SQLDate {
    private SqliteDataHelper SqliteDataHelper;
    private SQLiteDatabase db;
    private User_Data user_data;

    public SQLDate(Context context) {
        SqliteDataHelper = new SqliteDataHelper(context);
        db = SqliteDataHelper.getWritableDatabase();
        user_data = new User_Data();
    }


    public long Insert(boolean setPic, User_Data user_data) {
        ContentValues values = new ContentValues();
        values.put(SqliteDataHelper.USER_NAME, user_data.getName());
        values.put(SqliteDataHelper.USER_PWD, user_data.getPwd());
        values.put(SqliteDataHelper.CREATE_TIME, user_data.getCreateTime());
        if (setPic) {
            values.put(SqliteDataHelper.USER_PIC, user_data.getPic());
        }

        return db.insert(SqliteDataHelper.TABLE_NAME, null, values);
    }


    //登录判断  首先判断用户名是否存在  再判断输入的密码和数据库中用户名对应的密码是否相同
    public boolean query_login(User_Data user_data) {
        //首先名字要存在
        if (query_Name(user_data.getName())) {
            //判断密码是否相同
            Cursor cursor = db.query(SqliteDataHelper.TABLE_NAME, new String[]{"User_Pwd"},
                    "User_Name ='" + user_data.getName() + "'", null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex(SqliteDataHelper.USER_PWD)).equals(user_data.getPwd())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int updata(User_Data user_data) {
        ContentValues values = new ContentValues();
        values.put(SqliteDataHelper.USER_PWD, user_data.getPwd());
        if (user_data.getPic() != null) {
            values.put(SqliteDataHelper.USER_PIC, user_data.getPic());
        }
        return db.update(SqliteDataHelper.TABLE_NAME, values, "User_Name=?",
                new String[]{user_data.getName()});
    }

    //判断名字是否存在
    public boolean query_Name(String name) {
        @SuppressLint("Recycle") Cursor cursor = db.query(SqliteDataHelper.TABLE_NAME,
                null,
                null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(SqliteDataHelper.USER_NAME)).equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    //查询头像
    public String query_Path(String name) {
        //首先名字要存在
        String path = null;
        //先判断用户名是否存在
        if (query_Name(name)) {
            //判断密码是否相同

            Cursor cursor = db.query(SqliteDataHelper.TABLE_NAME, new String[]{"User_Pic"},
                    "User_Name ='" + name + "'", null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    path = cursor.getString(cursor.getColumnIndex(SqliteDataHelper.USER_PIC));
                }
            }
        }
        return path;
    }

}
