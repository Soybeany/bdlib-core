package com.soybeany.bdlib.core.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * <br>Created by Ben on 2016/7/8.
 */
public class FileUtils {

    /**
     * 文本格式
     */
    public static final String TYPE_TXT = "txt";

    private static final int STD_TRANS_LENGTH = 1024; // 数据转换的长度

    /**
     * 1K的字节长度
     */
    public static final int STD_KB_LENGTH = STD_TRANS_LENGTH;

    /**
     * 1M的字节长度
     */
    public static final int STD_MB_LENGTH = STD_TRANS_LENGTH * STD_KB_LENGTH;

    private static final String TAG = FileUtils.class.getSimpleName();


    /**
     * 获取取指定路径下指定文件类型文件名列表
     *
     * @param path 路径
     * @param type 文件类型
     */
    public static ArrayList<String> getFileList(String path, String type) {
        ArrayList<String> list = new ArrayList<>();
        File[] files = new File(path).listFiles();

        // 当files不为空时,遍历files，如果是指定格式的文件就加入list
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (getFileType(fileName).equalsIgnoreCase(type))
                    list.add(fileName);
            }
        }

        // 返回该目录下所有文件和文件夹
        return list;
    }

    /**
     * 获得全部文件(不含目录)的大小
     */
    public static long getSize(File file, boolean needCheck) {
        long size = 0;
        if (!needCheck || isFileValid(file)) {
            for (File aFileList : file.listFiles()) {
                size += aFileList.isDirectory() ? getSize(aFileList, false) : aFileList.length();
            }
        }
        return size;
    }

    // //////////////////////////////////流操作//////////////////////////////////

    /**
     * 处理文件输入流
     */
    public static <Result> Result handleFileStream(File file, IInputStreamCallback<Result> callback) {
        Result result = null;
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            result = callback.onHandle(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeStream(fis);
        }
        return result;
    }

    /**
     * 保存流
     */
    public static File saveStream(InputStream input, File file) {
        FileOutputStream output = null;
        try {
            mkParentDirs(file);
            output = new FileOutputStream(file);

            int len;
            byte[] buffer = new byte[STD_KB_LENGTH];
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(input);
            closeStream(output);
        }
        return file;
    }


    // //////////////////////////////////字节操作//////////////////////////////////

    /**
     * 从文件中读取字节流
     */
    public static byte[] loadBytes(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(STD_KB_LENGTH);

            int len;
            byte[] b = new byte[STD_KB_LENGTH];
            while ((len = fis.read(b)) > 0) {
                bos.write(b, 0, len);
            }
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(fis);
            closeStream(bos);
        }
        return buffer;
    }

    /**
     * 将字节流保存到文件
     */
    public static File saveBytes(byte[] data, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            mkParentDirs(file);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(fileOutputStream);
        }
        return file;
    }


    // //////////////////////////////////字符串操作//////////////////////////////////

    /**
     * 从文件中获得字符串
     */
    public static String loadString(File file) {
        return loadReaderString(file, null);
    }

    /**
     * 将字符串保存到文件中
     *
     * @param data   保存的内容
     * @param file   保存的文件
     * @param append 是否拼接
     */
    public static void saveString(String data, File file, boolean append) {
        BufferedWriter bw = null;
        try {
            mkParentDirs(file);
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bw);
        }
    }


    // //////////////////////////////////对象操作//////////////////////////////////

    /**
     * 从文件中获得对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadObject(File file) {
        T data = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            data = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(ois);
        }
        return data;
    }

    /**
     * 将对象保存到文件中
     */
    public static <T> File saveObject(T data, File file) {
        ObjectOutputStream oos = null;
        try {
            //存入数据
            mkParentDirs(file);
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(oos);
        }
        return file;
    }


    // //////////////////////////////////其它操作//////////////////////////////////

    /**
     * 判断文件是否有效
     */
    public static boolean isFileValid(File file) {
        return null != file && file.exists();
    }

    /**
     * 创建指定文件的父目录
     */
    public static boolean mkParentDirs(File file) {
        return mkDirs(file.getParentFile());
    }

    /**
     * 根据路径名创建目录
     *
     * @return 目录是否创建
     */
    public static boolean mkDirs(File file) {
        boolean b = false;
        if (!file.exists()) {
            b = file.mkdirs();
            // TODO: 2019/1/30 添加日志
//            LogUtils.i(TAG, "目录不存在，创建目录" + (b ? "成功" : "失败") + ": " + file.getAbsolutePath());
        }
        return b;
    }

    /**
     * 删除多个文件
     */
    public static boolean deleteFiles(File... files) {
        boolean isSucceed = true;
        for (File file : files) {
            if (!deleteFile(file)) {
                isSucceed = false;
            }
        }
        return isSucceed;
    }

    /**
     * 删除文件（包括目录、子目录及子目录下的文件）
     */
    public static boolean deleteFile(File file) {
        boolean isSucceed = true;
        if (!isFileValid(file)) {
            return false;
        } else if (getSubFileCount(file) > 0) {
            for (File subFile : file.listFiles()) {
                if (!deleteFile(subFile)) {
                    isSucceed = false;
                }
            }
        }
        return file.delete() && isSucceed;
    }

    /**
     * 获得一个文件夹下子文件个数
     */
    public static int getSubFileCount(File folder) {
        int i = -1;
        File[] list;
        if ((list = folder.listFiles()) != null) {
            i = list.length;
        }
        return i;
    }

    /**
     * 根据文件列表，获得文件名列表，如果是文件夹，则获取文件夹名
     */
    public static List<String> getFileNames(List<File> fileList) {
        List<String> fileNames = new ArrayList<>();
        for (File f : fileList) {
            fileNames.add(f.getName());
        }
        return fileNames;
    }

    /**
     * 递归复制文件夹
     */
    public static boolean copyFolder(String oldDirPath, String newDirPath) {
        return copyFolder(new File(oldDirPath), new File(newDirPath));
    }

    /**
     * 递归复制文件夹
     */
    public static boolean copyFolder(File oldDir, File newDir) {
        if (null == oldDir || !oldDir.exists()) {
            return false;
        }

        boolean isSuccess = true; // 标识是否成功

        // 获取源文件夹当前下的文件或目录
        File[] file = oldDir.listFiles();
        for (File aFile : file) {
            File newFile = new File(newDir, aFile.getName());
            if (aFile.isFile()) {
                if (!copyFile(aFile, newFile)) {
                    isSuccess = false;
                }
            } else {
                if (!copyFolder(new File(oldDir, aFile.getName()), newFile)) {
                    isSuccess = false;
                }
            }
        }
        return isSuccess;
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(String oldPath, String newPath) {
        return copyFile(new File(oldPath), new File(newPath));
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(File oldFile, File newFile) {
        // 若源文件无效则不再继续
        if (!isFileValid(oldFile)) {
            return false;
        }
        try {
            return null != saveStream(new FileInputStream(oldFile), newFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据路径、文件名与类型获得文件
     */
    public static File getFile(String dirPath, String fileName, String type) {
        return new File(dirPath, fileName + "." + type);
    }

    /**
     * 获得文件类型
     */
    public static String getFileType(String fileName) {
        String[] str = fileName.split("\\.");
        if (str.length > 1) {
            return str[str.length - 1].toLowerCase();
        }
        return "none";
    }

    /**
     * 检测文件后缀
     */
    public static boolean isImage(String fileName) {
        // 取得图片扩展名
        String end = getFileType(fileName);
        return end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp");
    }

    /**
     * 关闭流
     */
    public static void closeStream(Closeable closeableSteam) {
        if (null != closeableSteam) {
            try {
                closeableSteam.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从读取器中读取字符串
     */
    private static String loadReaderString(File file, InputStream stream) {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(null != file ? new FileReader(file) : new InputStreamReader(stream));
            int len;
            char[] buffer = new char[STD_KB_LENGTH];
            while ((len = br.read(buffer)) > 0) {
                builder.append(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(br);
        }
        return builder.toString();
    }

}
