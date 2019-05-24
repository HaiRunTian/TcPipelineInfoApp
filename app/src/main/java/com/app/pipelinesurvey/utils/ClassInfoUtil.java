package com.app.pipelinesurvey.utils;

/**
 * Created by Los on 2019-03-08 11:56.
 *
 */

public class ClassInfoUtil {

    public static String getClassInfo(){
        return Thread.currentThread().getStackTrace()[2].getClassName()+",method: "+Thread.currentThread().getStackTrace()[2].getMethodName()+" ";
    }
}
