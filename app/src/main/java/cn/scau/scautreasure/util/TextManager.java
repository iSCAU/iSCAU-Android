package cn.scau.scautreasure.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

/**
 * Android私有TXT文件操作类
 *
 * @author LTC
 *
 */
public class TextManager {

    private Context context;
    private String fileName;

    /**
     * 构造函数
     *
     * @param context
     * @param fileName
     */
    public TextManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public boolean isExist() {
        String[] fileNameArray = context.fileList();

        for (int i = 0; i < fileNameArray.length; i++) {
            if (fileNameArray[i].equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @return
     */
    public boolean create() {
        boolean result = false;

        if (!isExist()) {
            FileOutputStream fos = null;

            try {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (fos != null) {
                result = true;
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 写入
     *
     * @param str
     * @return
     */
    public boolean write(String str) {
        boolean result = true;
        FileOutputStream fos = null;

        if (isExist()) {
            try {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = false;
            }

            byte bt[] = (str + "\n").getBytes();

            if (fos != null) {
                try {
                    fos.write(bt);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                }
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * 读取一行
     *
     * @return
     */
    public String readLine() {

        String str = "";
        FileInputStream fis = null;

        if (isExist()) {
            try {
                fis = context.openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            try {
                str = br.readLine();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 读取全部
     *
     * @return
     */
    public String readAll() {
        String str = "";
        String line = "";
        FileInputStream fis = null;

        if (isExist()) {
            try {
                fis = context.openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            try {
                while ((line = br.readLine()) != null) {
                    str += line;
                }
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return str;
    }

    /**
     * 清空文件
     */
    public void clean() {
        delete();
        create();
    }

    /**
     * 删除文件
     */
    public void delete() {
        context.deleteFile(fileName);
    }
}