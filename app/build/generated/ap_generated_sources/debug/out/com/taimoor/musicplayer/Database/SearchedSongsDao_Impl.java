package com.taimoor.musicplayer.Database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SearchedSongsDao_Impl implements SearchedSongsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SearchedSongs> __insertionAdapterOfSearchedSongs;

  private final EntityDeletionOrUpdateAdapter<SearchedSongs> __deletionAdapterOfSearchedSongs;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SearchedSongsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSearchedSongs = new EntityInsertionAdapter<SearchedSongs>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `SearchedSongs` (`id`,`song`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SearchedSongs value) {
        stmt.bindLong(1, value.id);
        if (value.song == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.song);
        }
      }
    };
    this.__deletionAdapterOfSearchedSongs = new EntityDeletionOrUpdateAdapter<SearchedSongs>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `SearchedSongs` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SearchedSongs value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = " DELETE FROM SearchedSongs";
        return _query;
      }
    };
  }

  @Override
  public void SaveSearchedSong(final SearchedSongs songs) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSearchedSongs.insert(songs);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final SearchedSongs songs) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSearchedSongs.handle(songs);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<SearchedSongs> getSearchedSongs() {
    final String _sql = " Select * From SearchedSongs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSong = CursorUtil.getColumnIndexOrThrow(_cursor, "song");
      final List<SearchedSongs> _result = new ArrayList<SearchedSongs>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SearchedSongs _item;
        _item = new SearchedSongs();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfSong)) {
          _item.song = null;
        } else {
          _item.song = _cursor.getString(_cursorIndexOfSong);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> geSongs() {
    final String _sql = "Select song From SearchedSongs ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
