package com.example.angel.sqawker.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public class SqawkContract {

    // https://github.com/SimonVT/schematic

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(TEXT)
    @NotNull
    public static final String COLUMN_AUTOR = "author";

    @DataType(TEXT)
    @NotNull
    public static final String COLUMN_AUTOR_KEY = "key_author";

    @DataType(TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";


}
