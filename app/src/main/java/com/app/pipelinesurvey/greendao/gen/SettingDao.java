package com.app.pipelinesurvey.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.app.pipelinesurvey.dbbean.Setting;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SETTING".
*/
public class SettingDao extends AbstractDao<Setting, Long> {

    public static final String TABLENAME = "SETTING";

    /**
     * Properties of entity Setting.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property GroupName = new Property(1, String.class, "groupName", false, "GROUP_NAME");
        public final static Property PipeLength = new Property(2, int.class, "pipeLength", false, "PIPE_LENGTH");
        public final static Property GroupLocal = new Property(3, int.class, "groupLocal", false, "GROUP_LOCAL");
        public final static Property NumLength = new Property(4, int.class, "numLength", false, "NUM_LENGTH");
    }


    public SettingDao(DaoConfig config) {
        super(config);
    }
    
    public SettingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SETTING\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"GROUP_NAME\" TEXT," + // 1: groupName
                "\"PIPE_LENGTH\" INTEGER NOT NULL ," + // 2: pipeLength
                "\"GROUP_LOCAL\" INTEGER NOT NULL ," + // 3: groupLocal
                "\"NUM_LENGTH\" INTEGER NOT NULL );"); // 4: numLength
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SETTING\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Setting entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(2, groupName);
        }
        stmt.bindLong(3, entity.getPipeLength());
        stmt.bindLong(4, entity.getGroupLocal());
        stmt.bindLong(5, entity.getNumLength());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Setting entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(2, groupName);
        }
        stmt.bindLong(3, entity.getPipeLength());
        stmt.bindLong(4, entity.getGroupLocal());
        stmt.bindLong(5, entity.getNumLength());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Setting readEntity(Cursor cursor, int offset) {
        Setting entity = new Setting( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // groupName
            cursor.getInt(offset + 2), // pipeLength
            cursor.getInt(offset + 3), // groupLocal
            cursor.getInt(offset + 4) // numLength
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Setting entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setGroupName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPipeLength(cursor.getInt(offset + 2));
        entity.setGroupLocal(cursor.getInt(offset + 3));
        entity.setNumLength(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Setting entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Setting entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Setting entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
