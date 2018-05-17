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
                defaultSort = SqawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_MESSAGES_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }
}
