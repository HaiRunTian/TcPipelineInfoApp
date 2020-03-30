package com.app.pipelinesurvey.utils;

import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.bean.FileEntity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件工具类
 */
public class FileUtils {
    private static FileUtils sInstance = null;

    private FileUtils() {

    }
    /**
     * 判断是否是文件并且返回
     * @param name
     * @return
     */
    public boolean isFileExsit(String name) {
        File file = new File(name);
        if (file.isFile() && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是文件夹，并且返回
     */
    public boolean isDirExsit(String dir) {
        File file = new File(dir);
        if (file.isDirectory() && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 单例 获取FileUtils
     *
     * @return
     */
    public static FileUtils getInstance() {
        if (sInstance == null) {
            sInstance = new FileUtils();
        }
        return sInstance;
    }

    /***
     * 创建文件夹
     * @param path
     * @return
     */
    public boolean mkdirs(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    /**
     * 返回该路径下的文件夹数组
     *
     * @param path
     * @return
     */
    public File[] opendir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            return dir.listFiles();
        }
        return null;
    }

    /**
     * 复制文件夹
     *
     * @param from
     * @param to
     * @return
     */
    public boolean copy(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        if (fromFile.isFile() && fromFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(fromFile);
                return copyFile(fis, toFile, true);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 复制文件夹
     *
     * @param from
     * @param to
     * @return
     */
    public boolean copy(InputStream from, String to) {
        File toFile = new File(to);
        return copyFile(from, toFile, true);
    }

    /**
     * 删除文件夹
     *
     * @param file
     * @return
     */
    public boolean deleteFile(String file) {
        return deleteFile(new File(file));
    }

    /**
     * 删除文件夹
     *
     * @param file
     * @return
     */
    public boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹会删除文件夹下的文件
     *
     * @param dir
     * @return
     */
    public boolean deleteDir(String dir) {
        return deleteDir(new File(dir));
    }

    /**
     * 删除文件夹
     *
     * @param dir
     * @return
     */
    public boolean deleteDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            return delete(dir);
        }
        return false;
    }

    /**
     * 无论是文件还是文件夹递归删除
     *
     * @param file
     * @return
     */
    private boolean delete(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
            //现在是空文件夹了，可以正常删除了
            return file.delete();

        } else if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 复制文件夹
     *
     * @param src
     * @param des
     * @param rewrite
     * @return
     */
    private boolean copyFile(InputStream src, File des, boolean rewrite) {
        //目标路径不存在的话就创建一个
        if (!des.getParentFile().exists()) {
            des.getParentFile().mkdirs();
        }
        if (des.exists()) {
            if (rewrite) {
                des.delete();
            } else {
                return false;
            }
        }
        try {
            InputStream fis = src;
            FileOutputStream fos = new FileOutputStream(des);
            //1kb
            byte[] bytes = new byte[1024];
            int readlength = -1;
            while ((readlength = fis.read(bytes)) > 0) {
                fos.write(bytes, 0, readlength);
            }
            fos.flush();
            fos.close();
            fis.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param filename 文件名
     * @描述 Java文件操作 获取不带扩展名的文件名
     * @作者 Kevin.
     * @创建日期 2017/11/23  9:22.
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


//    public static void reNameFile(String tempPath, String data) {
//        File filePath = new File(tempPath);
//        for (File _file : filePath.listFiles()) {
//            String fName = _file.getName();
//            if (fName.substring(fName.lastIndexOf(".") + 1).equals("smwu")) {
//                _file.renameTo(new File(SuperMapConfig.MAP_DATA_PATH + FileUtils.getFileNameNoEx(data) + ".smwu"));
//            }
//            if (fName.substring(fName.lastIndexOf(".") + 1).equals("udb")) {
//                _file.renameTo(new File(SuperMapConfig.MAP_DATA_PATH + FileUtils.getFileNameNoEx(data) + ".udb"));
//            }
//            if (fName.substring(fName.lastIndexOf(".") + 1).equals("udd")) {
//                _file.renameTo(new File(SuperMapConfig.MAP_DATA_PATH + FileUtils.getFileNameNoEx(data) + ".udd"));
//            }
//        }
//    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static boolean fileSizeOverLimit(File file) {
        try {
            long fileSize = file.length();
            if (fileSize > 8388608)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 正则表达  文件名是否合法
     *
     * @param fileName
     * @return
     */
    public static boolean isFileNameIllegal(String fileName) {
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查找当前文件夹里所有文件夹和文件
     *
     * @param path
     * @param list
     * @return
     */
    public static List<FileEntity> findAllFile(String path, List<FileEntity> list) {
        list.clear();
        if (path == null || path.equals("")) {
            return null;
        }
        File _rootFile = new File(path);
        if (!_rootFile.exists()) {
            return list;
        } ;
        File[] files = _rootFile.listFiles();
        List<File> _list = Arrays.asList(files);
        //按名称排序
        Collections.sort(_list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });


        if (files != null && files.length > 0) {
            for (int i = 0; i < _list.size(); i++) {
                FileEntity entity = new FileEntity();
                boolean isDirectory = _list.get(i).isDirectory();
                entity.setFileName(_list.get(i).getName().toString());
                entity.setFilePath(_list.get(i).getAbsolutePath());
                entity.setFileSize(_list.get(i).length() + "");
                entity.setCheck(false);
                if (isDirectory) {
                    entity.setFileType(FileEntity.Type.FLODER);
                    list.add(entity);
                    //					entity.setFileName(files[i].getPath());
                } else {
                    entity.setFileType(FileEntity.Type.FILE);
                    String s = _list.get(i).getName().toString();

                    if (s.endsWith(".xls") || s.endsWith(".sci") || s.endsWith(".zip")){
                        list.add(entity);
                    }
                }



            }
        }
        return list;
    }

    /**
     * 重命名文件名
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/3  16:56
     */
    private static boolean updateFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) {
            return false;
        }
        newFileName = newFileName.trim();
        // 文件名不能为空
        if ("".equals(newFileName) || newFileName == null) {
            return false;
        }
        String newFilePath = null;
        // 判断是否为文件夹
        if (f.isDirectory()) {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            // 重命名文件名
            f.renameTo(nf);
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取里面有多少个文件夹
     *
     * @param fileName
     * @return
     */
    public int getFileCount(String fileName) {
        int count = 0;
        if (isDirExsit(fileName)) {
            File file = new File(fileName);
            File[] files = file.listFiles();
            for (int _i = 0; _i < files.length; _i++) {
                if (files[_i].isFile()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 获取文件夹里名字下标最大的一个
     *
     * @param fileName
     * @return
     */
    public int getFileIndexMax(String fileName) {
        int count = 0;
        if (isDirExsit(fileName)) {
            File file = new File(fileName);
            File[] files = file.listFiles();
            for (int _i = 0; _i < files.length; _i++) {
                if (files[_i].isFile()) {
                    String s = files[_i].getName().substring(files[_i].getName().lastIndexOf("-") + 1,files[_i].getName().lastIndexOf("."));
                    int temp = 0;
                    try {
                        temp = Integer.valueOf(s);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    count = temp > count ? temp : count;
                }
            }
        }
        return count;
    }

}
