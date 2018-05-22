package com.example.angel.sqawker.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = SqawkDatabase.VERSION, fileName = "squak")

public class SqawkDatabase {

    public static final int VERSION = 2;

    @Table(SqawkContract.class)
    public static final String SQUAWK_MESSAGES = "squawk_messages";

    @Table(InstructorsContract.class)
    public static final String INSTRUCTORS = "instructors";


}
