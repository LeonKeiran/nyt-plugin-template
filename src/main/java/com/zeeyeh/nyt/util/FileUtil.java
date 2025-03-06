package com.zeeyeh.nyt.util;

import java.io.File;
import java.io.IOException;

/**
 * @author LeonKeiran
 * @description 文件工具类
 * @date 2025/3/6 20:09
 */
public class FileUtil {
    /**
     * 删除指定的目录及其包含的所有文件和子目录。
     *
     * @param directory 指定要删除的目录。
     * @return 如果删除成功或目录为空，则返回true；如果删除失败，则返回false。
     * @throws IOException 如果删除过程中发生IO异常。
     */
    public static boolean deleteDirectory(File directory) throws IOException {
        // 检查目录是否为空
        if (directory == null) {
            return true;
        }
        // 如果指定的是文件而非目录，则尝试安全删除该文件
        if (directory.isFile()) {
            return safeDelete(directory);
        }
        // 尝试获取目录下的文件列表
        String[] list = directory.list();
        // 如果无法获取列表，视为删除成功
        if (list == null) {
            return true;
        }
        // 如果目录下没有文件或子目录，则尝试安全删除该目录
        if (list.length == 0) {
            return safeDelete(directory);
        }
        // 获取目录下的所有文件和子目录，并递归删除它们
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!deleteDirectory(file)) {
                    return false;
                }
            }
        }
        // 在删除所有文件和子目录后，尝试安全删除该目录
        return safeDelete(directory);
    }

    /**
     * 安全删除文件或目录。
     * 该方法会尝试删除指定的文件或目录，如果遇到安全权限问题会捕获并抛出IOException。
     *
     * @param file 要删除的文件或目录。
     * @return 返回删除操作是否成功。成功返回true，失败返回false。
     * @throws IOException 如果删除操作因权限不足而失败，则抛出此异常。
     */
    private static boolean safeDelete(File file) throws IOException {
        try {
            return file.delete(); // 尝试删除文件或目录
        } catch (SecurityException se) {
            // 处理删除时的权限不足异常
            throw new IOException("Failed to delete file/directory: " + file.getPath(), se);
        }
    }
}
