package com.lzhn.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库Helper
 * 
 * @author lzhn
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	/** 默认数据库名称 */
	public static final String DEFAULT_DATABASE_NAME = "DB_DEFAULT";
	/** 默认初始版本号 */
	public static final int DEFAULT_VERSION = 1;

	/** 数据库名称 */
	private String databaseName;
	/** 版本号 */
	private int version;
	/** 创建数据表字符串 */
	private String createTableSql = null;

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, String createTableSql) {
		this(context, name, factory, version);
		setCreateTableSql(createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String createTableSql) {
		return newInstance(context, DEFAULT_DATABASE_NAME, createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String databaseName, String createTableSql) {
		return newInstance(context, databaseName, DEFAULT_VERSION,
				createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String databaseName, int version, String createTableSql) {
		if (databaseName == null || databaseName.trim().isEmpty()) {
			return null;
		}
		if (version < DEFAULT_VERSION) {
			return null;
		}
		return new DatabaseHelper(context, databaseName, null, version,
				createTableSql);
	}

	public void setCreateTableSql(String createTableSql) {
		this.createTableSql = createTableSql;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (createTableSql != null)
			db.execSQL(createTableSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
