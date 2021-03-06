package com.example.angel.sqawker.provider;

import android.content.Context;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = SqawkProvider.AUTHORITY, database = SqawkDatabase.class)

public class SqawkProvider {

    private static final String pathInstructors = "instructors";
    private static final String pathMessages = "messages";

    public static final String AUTHORITY = "android.example.com.squawker.provider";


    @TableEndpoint(table = SqawkDatabase.SQUAWK_MESSAGES)
    public static class SqawkMessages {

        @ContentUri(
                path = pathMessages + "/followed",
                type = "vnd.android.cursor.dir/list",
                join = "JOIN " + SqawkDatabase.INSTRUCTORS + " ON " +
                        SqawkDatabase.SQUAWK_MESSAGES + "." + SqawkContract.COLUMN_AUTOR_KEY + " = " + SqawkDatabase.INSTRUCTORS + "." + InstructorsContract.COLUMN_AUTHOR_KEY,
                where = InstructorsContract.COLUMN_FOLLOWING + " = 1",
                defaultSort = SqawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI_FOLLOWED = Uri.parse("content://" + AUTHORITY + "/" + pathMessages + "/followed");


        @ContentUri(
                path = pathMessages,
                type = "vnd.android.cursor.dir/list",
                defaultSort = SqawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://" + AUTHORITY + "/" + pathMessages);

        @NotifyBulkInsert
        public static Uri[] onBulkInsert(Uri uri, String where, String[] whereArgs) {
            return new Uri[]{SqawkMessages.CONTENT_URI_FOLLOWED};
        }

        @NotifyInsert
        public static Uri[] onInsert(Uri uri, String where, String[] whereArgs) {
            return new Uri[]{SqawkMessages.CONTENT_URI_FOLLOWED};
        }
    }


    @TableEndpoint(table = SqawkDatabase.INSTRUCTORS)
    public static class Instructors {

        @ContentUri(
                path = pathInstructors,
                type = "vnd.android.cursor.dir/list",
                defaultSort = InstructorsContract.COLUMN_AUTHOR_NAME + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + pathInstructors);

        @NotifyUpdate(paths = pathInstructors)
        public static Uri[] onUpdate(Uri uri, String where, String[] whereArgs) {
            return new Uri[]{uri, SqawkMessages.CONTENT_URI_FOLLOWED};
        }


        @InexactContentUri(
                path = pathInstructors + "/#",
                name = "LIST_ID",
                type = "vnd.android.cursor.item",
                whereColumn = InstructorsContract.COLUMN_AUTHOR_KEY,
                pathSegment = 1)
        public static Uri withKey(String key) {
            return CONTENT_URI.buildUpon().appendEncodedPath(key).build();
        }

        @NotifyUpdate(paths = pathInstructors + "/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where, String[] whereArgs) {
            final String instructorKey = uri.getPathSegments().get(1);
            /*Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            final long listId = c.getLong(c.getColumnIndex(NoteColumns.LIST_ID));
            c.close();*/

            return new Uri[]{withKey(instructorKey), SqawkMessages.CONTENT_URI_FOLLOWED};
        }
    }
}
