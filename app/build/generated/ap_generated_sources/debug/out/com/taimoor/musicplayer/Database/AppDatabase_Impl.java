package com.taimoor.musicplayer.Database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SearchedSongsDao _searchedSongsDao;

  private volatile FavoriteSongsDao _favoriteSongsDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SearchedSongs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `song` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_SearchedSongs_song` ON `SearchedSongs` (`song`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `FavoriteSongs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `song_name` TEXT, `duration` TEXT, `artist` TEXT, `image` TEXT, `position` INTEGER NOT NULL, `url` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_FavoriteSongs_url` ON `FavoriteSongs` (`url`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '66467bd52c4e489da237d2f14b4fefaf')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `SearchedSongs`");
        _db.execSQL("DROP TABLE IF EXISTS `FavoriteSongs`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSearchedSongs = new HashMap<String, TableInfo.Column>(2);
        _columnsSearchedSongs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchedSongs.put("song", new TableInfo.Column("song", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSearchedSongs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSearchedSongs = new HashSet<TableInfo.Index>(1);
        _indicesSearchedSongs.add(new TableInfo.Index("index_SearchedSongs_song", true, Arrays.asList("song"), Arrays.asList("ASC")));
        final TableInfo _infoSearchedSongs = new TableInfo("SearchedSongs", _columnsSearchedSongs, _foreignKeysSearchedSongs, _indicesSearchedSongs);
        final TableInfo _existingSearchedSongs = TableInfo.read(_db, "SearchedSongs");
        if (! _infoSearchedSongs.equals(_existingSearchedSongs)) {
          return new RoomOpenHelper.ValidationResult(false, "SearchedSongs(com.taimoor.musicplayer.Database.SearchedSongs).\n"
                  + " Expected:\n" + _infoSearchedSongs + "\n"
                  + " Found:\n" + _existingSearchedSongs);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoriteSongs = new HashMap<String, TableInfo.Column>(7);
        _columnsFavoriteSongs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("song_name", new TableInfo.Column("song_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("duration", new TableInfo.Column("duration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("artist", new TableInfo.Column("artist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("image", new TableInfo.Column("image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteSongs.put("url", new TableInfo.Column("url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteSongs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteSongs = new HashSet<TableInfo.Index>(1);
        _indicesFavoriteSongs.add(new TableInfo.Index("index_FavoriteSongs_url", true, Arrays.asList("url"), Arrays.asList("ASC")));
        final TableInfo _infoFavoriteSongs = new TableInfo("FavoriteSongs", _columnsFavoriteSongs, _foreignKeysFavoriteSongs, _indicesFavoriteSongs);
        final TableInfo _existingFavoriteSongs = TableInfo.read(_db, "FavoriteSongs");
        if (! _infoFavoriteSongs.equals(_existingFavoriteSongs)) {
          return new RoomOpenHelper.ValidationResult(false, "FavoriteSongs(com.taimoor.musicplayer.Database.FavoriteSongs).\n"
                  + " Expected:\n" + _infoFavoriteSongs + "\n"
                  + " Found:\n" + _existingFavoriteSongs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "66467bd52c4e489da237d2f14b4fefaf", "9821e8b8b41b1f7d747225561fe5b379");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "SearchedSongs","FavoriteSongs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `SearchedSongs`");
      _db.execSQL("DELETE FROM `FavoriteSongs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SearchedSongsDao.class, SearchedSongsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoriteSongsDao.class, FavoriteSongsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public SearchedSongsDao searchedSongsDao() {
    if (_searchedSongsDao != null) {
      return _searchedSongsDao;
    } else {
      synchronized(this) {
        if(_searchedSongsDao == null) {
          _searchedSongsDao = new SearchedSongsDao_Impl(this);
        }
        return _searchedSongsDao;
      }
    }
  }

  @Override
  public FavoriteSongsDao favoriteSongsDao() {
    if (_favoriteSongsDao != null) {
      return _favoriteSongsDao;
    } else {
      synchronized(this) {
        if(_favoriteSongsDao == null) {
          _favoriteSongsDao = new FavoriteSongsDao_Impl(this);
        }
        return _favoriteSongsDao;
      }
    }
  }
}
