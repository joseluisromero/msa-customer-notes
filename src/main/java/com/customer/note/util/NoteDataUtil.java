package com.customer.note.util;

public class NoteDataUtil {


    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}