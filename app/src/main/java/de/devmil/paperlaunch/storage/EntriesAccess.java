/*
 * Copyright 2015 Devmil Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devmil.paperlaunch.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EntriesAccess {

    private static final String[] entriesColumns = new String[]
            {
                EntriesSQLiteOpenHelper.COLUMN_ID,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_FOLDERID,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_LAUNCHID,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_PARENTFOLDERID,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_ORDERINDEX
            };

    private static final int INDEX_COLUMN_ID = 0;
    private static final int INDEX_COLUMN_FOLDERID = 1;
    private static final int INDEX_COLUMN_LAUNCHID = 2;
    private static final int INDEX_COLUMN_PARENTFOLDERID = 3;
    private static final int INDEX_COLUMN_ORDERINDEX = 4;

    private SQLiteDatabase mDatabase;

    public EntriesAccess(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<EntryDTO> queryAllEntries(long parentFolderId) {
        String selection = EntriesSQLiteOpenHelper.COLUMN_ENTRIES_PARENTFOLDERID + " = " + parentFolderId;

        Cursor c = mDatabase.query(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                entriesColumns,
                selection,
                null,
                null,
                null,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_ORDERINDEX
        );

        List<EntryDTO> result = new ArrayList<>();

        if(c.moveToFirst()) {
            do {
                EntryDTO entry = cursorToEntry(c);
                result.add(entry);
            }
            while (c.moveToNext());
        }

        c.close();

        return result;
    }

    public EntryDTO queryEntry(long entryId) {
        Cursor c = mDatabase.query(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                entriesColumns,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + entryId,
                null,
                null,
                null,
                null
        );

        EntryDTO result = null;

        if(c.moveToFirst()) {
            result = cursorToEntry(c);
        }
        c.close();
        return result;
    }

    public EntryDTO queryEntryForFolder(long folderId) {
        Cursor c = mDatabase.query(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                entriesColumns,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_FOLDERID + " = " + folderId,
                null,
                null,
                null,
                null
        );

        EntryDTO result = null;

        if(c.moveToFirst()) {
            result = cursorToEntry(c);
        }
        c.close();
        return result;
    }

    public EntryDTO queryEntryForLaunch(long launchId) {
        Cursor c = mDatabase.query(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                entriesColumns,
                EntriesSQLiteOpenHelper.COLUMN_ENTRIES_LAUNCHID + " = " + launchId,
                null,
                null,
                null,
                null
        );

        EntryDTO result = null;
        if(c.moveToFirst()) {
            result = cursorToEntry(c);
        }
        c.close();
        return result;
    }

    public EntryDTO createNew(int orderIndex) {
        ContentValues values = new ContentValues();
        values.put(EntriesSQLiteOpenHelper.COLUMN_ENTRIES_ORDERINDEX, orderIndex);
        long id = mDatabase.insert(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                null,
                values);

        return queryEntry(id);
    }

    public void update(EntryDTO entry) {
        ContentValues values = new ContentValues();

        entryToValues(entry, values);

        mDatabase.update(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                values,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + entry.getId(),
                null
        );
    }

    public void delete(EntryDTO entry) {
        mDatabase.delete(
                EntriesSQLiteOpenHelper.TABLE_ENTRIES,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + entry.getId(),
                null
        );
    }

    private EntryDTO cursorToEntry(Cursor cursor) {
        return new EntryDTO(
                cursor.getInt(INDEX_COLUMN_ID),
                cursor.getInt(INDEX_COLUMN_ORDERINDEX),
                cursor.getInt(INDEX_COLUMN_LAUNCHID),
                cursor.getInt(INDEX_COLUMN_FOLDERID),
                cursor.getInt(INDEX_COLUMN_PARENTFOLDERID)
        );
    }

    private void entryToValues(EntryDTO entry, ContentValues values) {
        values.put(EntriesSQLiteOpenHelper.COLUMN_ENTRIES_FOLDERID, entry.getFolderId());
        values.put(EntriesSQLiteOpenHelper.COLUMN_ENTRIES_LAUNCHID, entry.getLaunchId());
        values.put(EntriesSQLiteOpenHelper.COLUMN_ENTRIES_ORDERINDEX, entry.getOrderIndex());
        values.put(EntriesSQLiteOpenHelper.COLUMN_ENTRIES_PARENTFOLDERID, entry.getParentFolderId());
    }
}
