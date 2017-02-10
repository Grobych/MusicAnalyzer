package model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 12.01.2017.
 */
public class FileWorker {
    public static void getFilesFromFolder(File f, List<File> list){
        File[] files;
        if (f.isDirectory() && (files = f.listFiles()) != null) {
            for (File file : files) {
                getFilesFromFolder(file, list);
            }
        }
        else {
            String path = f.getPath();
            if (path.substring(path.length()-4, path.length()).equals(".mp3")) {
                list.add(f);
            }
        }
    }
}
