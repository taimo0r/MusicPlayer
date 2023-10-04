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
public final class FavoriteSongsDao_Impl implements FavoriteSongsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FavoriteSongs> __insertionAdapterOfFavoriteSongs;

  private final EntityDeletionOrUpdateAdapter<FavoriteSongs> __deletionAdapterOfFavoriteSongs;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FavoriteSongsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavoriteSongs = new EntityInsertionAdapter<FavoriteSongs>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `FavoriteSongs` (`id`,`song_name`,`duration`,`artist`,`image`,`position`,`url`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FavoriteSongs value) {
        stmt.bindLong(1, value.id);
        if (value.songName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.songName);
        }
        if (value.duration == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.duration);
        }
        if (value.artist == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.artist);
        }
        if (value.image == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.image);
        }
        stmt.bindLong(6, value.position);
        if (value.url == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.url);
        }
      }
    };
    this.__deletionAdapterOfFavoriteSongs = new EntityDeletionOrUpdateAdapter<FavoriteSongs>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `FavoriteSongs` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FavoriteSongs value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = " DELETE FROM FavoriteSongs";
        return _query;
      }
    };
  }

  @Override
  public void SaveFavoriteSong(final FavoriteSongs songs) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFavoriteSongs.insert(songs);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final FavoriteSongs songs) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFavoriteSongs.handle(songs);
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
  public List<FavoriteSongs> getFavoriteSongs() {
    final String _sql = " Select * From FavoriteSongs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSongName = CursorUtil.getColumnIndexOrThrow(_cursor, "song_name");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final List<FavoriteSongs> _result = new ArrayList<FavoriteSongs>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FavoriteSongs _item;
        _item = new FavoriteSongs();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfSongName)) {
          _item.songName = null;
        } else {
          _item.songName = _cursor.getString(_cursorIndexOfSongName);
        }
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _item.duration = null;
        } else {
          _item.duration = _cursor.getString(_cursorIndexOfDuration);
        }
        if (_cursor.isNull(_cursorIndexOfArtist)) {
          _item.artist = null;
        } else {
          _item.artist = _cursor.getString(_cursorIndexOfArtist);
        }
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _item.image = null;
        } else {
          _item.image = _cursor.getString(_cursorIndexOfImage);
        }
        _item.position = _cursor.getInt(_cursorIndexOfPosition);
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _item.url = null;
        } else {
          _item.url = _cursor.getString(_cursorIndexOfUrl);
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
  public String getSong(final String name) {
    final String _sql = "Select url From FavoriteSongs Where FavoriteSongs.song_name == ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
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
