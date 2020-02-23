package com.app.pipelinesurvey.bean;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by HaiRun on 2018/11/29 0029.
 * 读取手机文件夹bean
 */

public class FileEntity  {


    public enum Type{
        FLODER,FILE
    }
    private String filePath; //文件路径
    private String fileName; //文件名
    private String fileSize; //文件长度
    private Type fileType; //文件类型
    private boolean isCheck; //是否被选中

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileSize() {
        return fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public Type getFileType() {
        return fileType;
    }
    public void setFileType(Type fileType) {
        this.fileType = fileType;
    }


}
