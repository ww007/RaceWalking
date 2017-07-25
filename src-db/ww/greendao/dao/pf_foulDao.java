package ww.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import ww.greendao.dao.pf_foul;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PF_FOUL.
*/
public class pf_foulDao extends AbstractDao<pf_foul, Long> {

    public static final String TABLENAME = "PF_FOUL";

    /**
     * Properties of entity pf_foul.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Foul_ID = new Property(0, Long.class, "foul_ID", true, "FOUL__ID");
        public final static Property Group_ID = new Property(1, Long.class, "group_ID", false, "GROUP__ID");
        public final static Property Referee_ID = new Property(2, Long.class, "referee_ID", false, "REFEREE__ID");
        public final static Property Foul_athlete_ID = new Property(3, String.class, "foul_athlete_ID", false, "FOUL_ATHLETE__ID");
        public final static Property Foul_time = new Property(4, String.class, "foul_time", false, "FOUL_TIME");
        public final static Property Foul_card = new Property(5, Integer.class, "foul_card", false, "FOUL_CARD");
        public final static Property Foul_type = new Property(6, Integer.class, "foul_type", false, "FOUL_TYPE");
        public final static Property Foul_upload = new Property(7, String.class, "foul_upload", false, "FOUL_UPLOAD");
        public final static Property Foul_referee_name = new Property(8, String.class, "foul_referee_name", false, "FOUL_REFEREE_NAME");
        public final static Property Foul_MAC = new Property(9, String.class, "foul_MAC", false, "FOUL__MAC");
        public final static Property Foul_description = new Property(10, String.class, "foul_description", false, "FOUL_DESCRIPTION");
    };


    public pf_foulDao(DaoConfig config) {
        super(config);
    }
    
    public pf_foulDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PF_FOUL' (" + //
                "'FOUL__ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: foul_ID
                "'GROUP__ID' INTEGER," + // 1: group_ID
                "'REFEREE__ID' INTEGER," + // 2: referee_ID
                "'FOUL_ATHLETE__ID' TEXT," + // 3: foul_athlete_ID
                "'FOUL_TIME' TEXT," + // 4: foul_time
                "'FOUL_CARD' INTEGER," + // 5: foul_card
                "'FOUL_TYPE' INTEGER," + // 6: foul_type
                "'FOUL_UPLOAD' TEXT," + // 7: foul_upload
                "'FOUL_REFEREE_NAME' TEXT," + // 8: foul_referee_name
                "'FOUL__MAC' TEXT," + // 9: foul_MAC
                "'FOUL_DESCRIPTION' TEXT);"); // 10: foul_description
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PF_FOUL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, pf_foul entity) {
        stmt.clearBindings();
 
        Long foul_ID = entity.getFoul_ID();
        if (foul_ID != null) {
            stmt.bindLong(1, foul_ID);
        }
 
        Long group_ID = entity.getGroup_ID();
        if (group_ID != null) {
            stmt.bindLong(2, group_ID);
        }
 
        Long referee_ID = entity.getReferee_ID();
        if (referee_ID != null) {
            stmt.bindLong(3, referee_ID);
        }
 
        String foul_athlete_ID = entity.getFoul_athlete_ID();
        if (foul_athlete_ID != null) {
            stmt.bindString(4, foul_athlete_ID);
        }
 
        String foul_time = entity.getFoul_time();
        if (foul_time != null) {
            stmt.bindString(5, foul_time);
        }
 
        Integer foul_card = entity.getFoul_card();
        if (foul_card != null) {
            stmt.bindLong(6, foul_card);
        }
 
        Integer foul_type = entity.getFoul_type();
        if (foul_type != null) {
            stmt.bindLong(7, foul_type);
        }
 
        String foul_upload = entity.getFoul_upload();
        if (foul_upload != null) {
            stmt.bindString(8, foul_upload);
        }
 
        String foul_referee_name = entity.getFoul_referee_name();
        if (foul_referee_name != null) {
            stmt.bindString(9, foul_referee_name);
        }
 
        String foul_MAC = entity.getFoul_MAC();
        if (foul_MAC != null) {
            stmt.bindString(10, foul_MAC);
        }
 
        String foul_description = entity.getFoul_description();
        if (foul_description != null) {
            stmt.bindString(11, foul_description);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public pf_foul readEntity(Cursor cursor, int offset) {
        pf_foul entity = new pf_foul( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // foul_ID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // group_ID
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // referee_ID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // foul_athlete_ID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // foul_time
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // foul_card
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // foul_type
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // foul_upload
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // foul_referee_name
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // foul_MAC
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // foul_description
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, pf_foul entity, int offset) {
        entity.setFoul_ID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGroup_ID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setReferee_ID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setFoul_athlete_ID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFoul_time(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFoul_card(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setFoul_type(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setFoul_upload(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFoul_referee_name(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFoul_MAC(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFoul_description(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(pf_foul entity, long rowId) {
        entity.setFoul_ID(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(pf_foul entity) {
        if(entity != null) {
            return entity.getFoul_ID();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
