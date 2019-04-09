package com.soybeany.bdlib.core.util.file;

import com.soybeany.bdlib.core.java8.Optional;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;

/**
 * 文件操作工具类
 * <br>Created by Soybeany on 2018/5/30.
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

    /**
     * 默认的分段尺寸
     */
    public static final int DEFAULT_BLOCK_SIZE = 25 * 1024;

    private static final File[] EMPTY_FILE_ARR = new File[0];

    /**
     * 创建指定文件的父目录
     * Created by Soybeany on 2018/5/14 10:41
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean mkParentDirs(File file) {
        return mkDirs(file.getParentFile());
    }

    /**
     * 根据路径名创建目录
     *
     * @return 目录是否创建
     * Created by Soybeany on 2018/5/14 10:41
     */
    public static boolean mkDirs(File file) {
        boolean b = false;
        if (!file.exists()) {
            b = file.mkdirs();
        }
        return b;
    }

    public static void writeToFile(String content, File file, Long total, boolean append, IProgressListener listener) throws IOException {
        writeToFile(new StringReader(content), file, total, append, listener);
    }

    public static void writeToFile(Reader in, File file, Long total, boolean append, IProgressListener listener) throws IOException {
        mkParentDirs(file);
        readWriteText(in, new FileWriter(file, append), total, listener);
    }

    /**
     * 输出流到文件
     * Created by Soybeany on 2018/5/14 10:41
     */
    public static void writeToFile(InputStream in, File file, Long total, IProgressListener listener) throws IOException {
        mkParentDirs(file);
        readWriteStream(in, new FileOutputStream(file), total, listener);
    }

    /**
     * 输出流到字节数组
     */
    public static byte[] writeToMem(InputStream in, Long total, IProgressListener listener) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        readWriteStream(in, stream, total, listener);
        return stream.toByteArray();
    }

    public static String readString(Reader in, Long total, IProgressListener listener) throws IOException {
        StringWriter writer = new StringWriter();
        readWriteText(in, writer, total, listener);
        return writer.toString();
    }

    public static String readString(InputStream in, String charsetName, Long total, IProgressListener listener) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        readWriteStream(in, stream, total, listener);
        return stream.toString(charsetName);
    }

    /**
     * 从流中读取UTF-8字符串
     */
    public static String readUTF8String(InputStream in, Long total, IProgressListener listener) throws IOException {
        return readString(in, "utf-8", total, listener);
    }

    public static void readWriteText(Reader in, Writer out, Long total, IProgressListener listener) throws IOException {
        int len;
        char[] buffer = new char[DEFAULT_BLOCK_SIZE];
        try (Reader reader = in instanceof BufferedReader ? in : new BufferedReader(in);
             Writer writer = out instanceof BufferedWriter ? out : new BufferedWriter(out)) {
            ProgressRecorder recorder = new ProgressRecorder().add(listener).start(total);
            while ((len = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, len);
                recorder.record(len);
            }
            recorder.stop();
        }
    }

    /**
     * 读写流操作
     * Created by Soybeany on 2018/5/14 10:41
     */
    public static void readWriteStream(InputStream in, OutputStream out, Long total, IProgressListener listener) throws IOException {
        int len;
        byte[] buffer = new byte[DEFAULT_BLOCK_SIZE];
        try (InputStream input = in instanceof BufferedInputStream ? in : new BufferedInputStream(in);
             OutputStream output = out instanceof BufferedOutputStream ? out : new BufferedOutputStream(out)) {
            ProgressRecorder recorder = new ProgressRecorder().add(listener).start(total);
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
                recorder.record(len);
            }
            recorder.stop();
        }
    }

    /**
     * 获得UUID
     * Created by Soybeany on 2018/5/30 9:44
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
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
        for (File aFile : Optional.ofNullable(oldDir.listFiles()).orElse(EMPTY_FILE_ARR)) {
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
        // 若源文件无效或与目标相同则不再继续
        if (!isFileValid(oldFile) || oldFile == newFile) {
            return false;
        }
        try {
            writeToFile(new FileInputStream(oldFile), newFile, null, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean deleteFile(File file) {
        boolean isSucceed = true;
        if (!isFileValid(file)) {
            return false;
        } else if (getSubFileCount(file) > 0) {
            for (File subFile : Optional.ofNullable(file.listFiles()).orElse(EMPTY_FILE_ARR)) {
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
     * 判断文件是否有效
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isFileValid(File file) {
        return null != file && file.exists();
    }
}
