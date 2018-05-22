package com.example.angel.sqawker.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;


public class InstructorsContract {

    @DataType(TEXT)
    @PrimaryKey
    @Unique
    public static final String COLUMN_AUTHOR_KEY = "author_Key";


    @DataType(TEXT)
    public static final String COLUMN_AUTHOR_NAME = "author_name";

    @DataType(TEXT)
    public static final String COLUMN_AUTHOR_IMAGE = "author_image";

    @DataType(INTEGER)
    public static final String COLUMN_FOLLOWING = "author_following";

}
