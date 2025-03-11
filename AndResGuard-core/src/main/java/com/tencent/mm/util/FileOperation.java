package com.tencent.mm.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileOperation {
    private static final int BUFFER = 8192;

    public static boolean fileExists(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }

    public static boolean deleteFile(String filePath) {
        if (filePath == null) {
            return true;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public static long getlist(File f) {
        if (f == null || (!f.exists())) {
            return 0;
        }
        if (!f.isDirectory()) {
            return 1;
        }
        long size;
        File[] flist = f.listFiles();
        assert flist != null;
        size = flist.length;
        for (File file : flist) {
            if (file.isDirectory()) {
                size = size + getlist(file);
                size--;
            }
        }
        return size;
    }

    public static long getFileSizes(File f) {
        long size = 0;
        if (f.exists() && f.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                size = fis.available();
            } catch (IOException e) {
                e.fillInStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
        }
        return size;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean deleteDir(File file) {
        if (file == null || (!file.exists())) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File value : files) {
                deleteDir(value);
            }
        }
        file.delete();
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        FileInputStream is = new FileInputStream(source);
        FileOutputStream os = new FileOutputStream(dest, false);
        try {
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean checkDirectory(String dir) {
        File dirObj = new File(dir);
        deleteDir(dirObj);

        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File checkFile(String dir) {
        deleteFile(dir);
        File file = new File(dir);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.fillInStackTrace();
        }
        return file;
    }

    @SuppressWarnings({"rawtypes", "ResultOfMethodCallIgnored"})
    public static HashMap<String, Integer> unZipAPk(String fileName, String filePath) throws IOException {
        checkDirectory(filePath);
        HashMap<String, Integer> compress = new HashMap<>();
        try (ZipFile zipFile = new ZipFile(fileName)) {
            Enumeration emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) emu.nextElement();
                if (entry.isDirectory()) {
                    new File(filePath, entry.getName()).mkdirs();
                    continue;
                }
                try (InputStream bis = zipFile.getInputStream(entry)) {
                    File file = new File(filePath + File.separator + entry.getName());
                    File parent = file.getParentFile();
                    if (parent != null && (!parent.exists())) {
                        parent.mkdirs();
                    }
                    //要用linux的斜杠
                    String compatibaleresult = entry.getName();
                    if (compatibaleresult.contains("\\")) {
                        compatibaleresult = compatibaleresult.replace("\\", "/");
                    }
                    compress.put(compatibaleresult, entry.getMethod());
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        IOUtils.copy(bis, fos);
                    }
                }
            }
        }
        return compress;
    }

    /**
     * zip list of file
     *
     * @param resFileList  file(dir) list
     * @param baseFolder   file(dir) base folder, we should calc relative path of resFile with base
     * @param zipFile      output zip file
     * @param compressData compress data
     * @throws IOException io exception
     */
    public static void zipFiles(
            Collection<File> resFileList, File baseFolder, File zipFile, HashMap<String, Integer> compressData)
            throws IOException {
        try (FileOutputStream out = new FileOutputStream(zipFile)) {
            try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
                for (File resFile : resFileList) {
                    if (resFile.exists()) {
                        if (resFile.getAbsolutePath().contains(baseFolder.getAbsolutePath())) {
                            String relativePath = baseFolder.toURI().relativize(resFile.getParentFile().toURI()).getPath();
                            // remove slash at end of relativePath
                            if (relativePath.length() > 1) {
                                relativePath = relativePath.substring(0, relativePath.length() - 1);
                            } else {
                                relativePath = "";
                            }
                            zipFile(resFile, zipOut, relativePath, compressData);
                        } else {
                            zipFile(resFile, zipOut, "", compressData);
                        }
                    }
                }
            }
        }
    }

    private static void zipFile(
            File resFile, ZipOutputStream zipout, String rootpath, HashMap<String, Integer> compressData) throws IOException {
        rootpath = rootpath + (rootpath.trim().isEmpty() ? "" : File.separator) + resFile.getName();
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            if (fileList != null)
                for (File file : fileList) {
                    zipFile(file, zipout, rootpath, compressData);
                }
        } else {
            final byte[] fileContents = readContents(resFile);
            //这里需要强转成linux格式，果然坑！！
            if (rootpath.contains("\\")) {
                rootpath = rootpath.replace("\\", "/");
            }
            if (!compressData.containsKey(rootpath)) {
                System.err.printf(String.format("do not have the compress data path =%s in resource.asrc\n", rootpath));
                //throw new IOException(String.format("do not have the compress data path=%s", rootpath));
                return;
            }
            int compressMethod = compressData.get(rootpath);
            ZipEntry entry = new ZipEntry(rootpath);

            if (compressMethod == ZipEntry.DEFLATED) {
                entry.setMethod(ZipEntry.DEFLATED);
            } else {
                entry.setMethod(ZipEntry.STORED);
                entry.setSize(fileContents.length);
                final CRC32 checksumCalculator = new CRC32();
                checksumCalculator.update(fileContents);
                entry.setCrc(checksumCalculator.getValue());
            }
            zipout.putNextEntry(entry);
            zipout.write(fileContents);
            zipout.flush();
            zipout.closeEntry();
        }
    }

    private static byte[] readContents(final File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}