package com.example.angel.sqawker.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = SqawkProvider.AUTHORITY, database = SqawkDatabase.class)

public class SqawkProvider {

    public static final String AUTHORITY = "android.example.com.squawker.provider";


    @TableEndpoint(table = SqawkDatabase.SQUAWK_MESSAGES)
    public static class SqawkMessages {

        @ContentUri(

                path = "messages",
                type = "vnd.android.cursor.dir/list",
                join = "JOIN " + SqawkDatabase.INSTRUCTORS + " ON " + SqawkDatabase.SQUAWK_MESSAGES + "." + SqawkContract.COLUMN_AUTOR_KEY + " = " + SqawkDatabase.INSTRUCTORS + "." + InstructorsContract.COLUMN_AUTHOR_KEY,
                where = InstructorsContract.COLUMN_FOLLOWING + " = 1",
                defaultSort = SqawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }


    @TableEndpoint(table = SqawkDatabase.INSTRUCTORS)
    public static class Instructors {

        @ContentUri(
                path = "instructors",
                type = "vnd.android.cursor.dir/list",
                defaultSort = InstructorsContract.COLUMN_AUTHOR_NAME + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/instructors");
    }
}
