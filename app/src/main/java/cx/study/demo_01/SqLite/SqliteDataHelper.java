package cx.study.demo_01.SqLite;

import android.content.Context;
import android.content.SyncAdapterType;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.SqLife
 * @ClassName: DataHelper
 * @Author: ZYH
 * @CreateDate: 2020/3/20 0020 15:20
 * @Version: 1.0
 * @Description: java类作用描述
 */


public class SqliteDataHelper extends SQLiteOpenHelper {

    //数据库名字
    static final String DB_NAME = "USER_DATA.db";
    //数据库表名
    static final String TABLE_NAME = "registration_Message";
    //数据库版本
    static final int DB_VERSION = 1;
    //用户名
    static final String USER_NAME = "User_Name";
    //密码
    static final String USER_PWD = "User_Pwd";
    //创建时间
    static final String CREATE_TIME = "Create_Time";
    //头像图片地址
    static final String USER_PIC = "User_Pic";

    public SqliteDataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        String sql =
                "create table " + TABLE_NAME + "(_id integer primary key autoincrement," +
                        USER_NAME + " text not null," +
                        USER_PWD + " text not null," +
                        CREATE_TIME + " INTEGER," +
                        USER_PIC + " text" + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
