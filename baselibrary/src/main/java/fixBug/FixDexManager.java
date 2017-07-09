package fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by xinzhang on 7/9/17.
 */

public class FixDexManager {
    private Context mContext;
    private File mDexDir;
    private String TAG = "FixDexManager";

    public FixDexManager(Context context){
        this.mContext = context;
        //get the dex dir which can be accessed by system
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }
    /**
     * fix dex file
     */
    public void fixDex(String fixDexPath) throws Exception{
        //2. get new dexElements which are fixed
        //2.1 put new dexElements to dexPath which can be accessed by system
        File srcFile = new File(fixDexPath);
        if(!srcFile.exists()){
            throw new FileNotFoundException(fixDexPath);
        }
        File destFile = new File(mDexDir, srcFile.getName());

        if(destFile.exists()){
            Log.d(TAG, "patch [" + fixDexPath + "] has be loaded.");
            return;
        }
        copyFile(srcFile, destFile);
        //2.2
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);
        fixDexFiles(fixDexFiles);


    }

    /**
     * inject new combined dexElements into applicationClassLoader
     * @param classLoader
     * @param dexElements
     */
    private void inject(ClassLoader classLoader, Object dexElements) throws Exception{
        //1. get PathList by reflection
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("PathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //2. get dexElements in pathList
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList,dexElements);
    }

    /**
     * combine two arrays
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }



    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        //1. get PathList by reflection
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("PathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //2. get dexElements in pathList
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }

    /**
     *
     * copy file
     *
     * @param src
     *            source file
     * @param dest
     *            target file
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }


    /**
     * load all previous fixDex file
     */
    public void loadFixDex() throws Exception{
        File[] DexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for(File dexFile : DexFiles){
            if(dexFile.getName().endsWith(".dex")){
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    private void fixDexFiles(List<File> fixDexFiles) throws Exception {
        //1. get original dexElements in running app
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object dexElements = getDexElementsByClassLoader(applicationClassLoader);

        File optimizedDirectory = new File(mDexDir,"odex");
        if (!optimizedDirectory.exists()){
            optimizedDirectory.mkdir();
        }
        for(File fixDexFile : fixDexFiles){
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(), // dex path
                    optimizedDirectory,
                    null, //library path of .os file
                    applicationClassLoader // parent classLoader;
            );
            Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);

            //3. put new dexElements in the front of original dexElements.
            dexElements = combineArray(fixDexElements, dexElements);

        }
        //inject new combined dexElements into applicationClassLoader
        inject(applicationClassLoader, dexElements);

    }
}
