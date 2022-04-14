package com.company;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class Renamer {
    public static void main(String[] args) {
        Saver saver = new Saver();
        Path directory = Path.of("C:\\Материалы УД ч.4 ст.159 УК\uF028");
        File file = directory.toFile();
        searchFolders(file, saver);
        ArrayList<Path> directories = saver.get();

        for (Path path : directories) {
            File fileS = path.toFile();

            if (contains(fileS.toString(), "comp")) {
                System.out.println(fileS.getParentFile().getName());
                fileS.renameTo(Path.of(fileS.getParentFile().getName() + "Compressed").toFile());
            }
        }


    }

    public static boolean contains(String str, String substr) {
        return str.contains(substr);
    }

    public static void searchFolders(File file, Saver saver) {
        System.out.println(file);
        System.out.println(file.isDirectory());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File value : files) {
                if (value.isFile()) {
                    continue;
                }
                if (saver.isOnList(value)) {
                    return;
                }
                Path path = Path.of(value.getPath());
                saver.add(path);
            }
            File file1 = saver.getNext();
            if (file1.isDirectory()) {
                searchFolders(file1, saver);
            }
            return;
        }
        File file1 = saver.getNext();
        if (file1.isDirectory()) {
            searchFolders(file1, saver);
        }
        return;
    }

    public static boolean isHaveComp(File file) {
        File[] files = file.listFiles();
        for (File value : files) {
            if (value.isFile()) {
                continue;
            }
            if (contains(value.toString(), "comp")) {
                return true;
            }

        }
        return false;
    }

    public static class Saver {
        int counter = 0;
        ArrayList<Path> result = new ArrayList<>();

        public void add(Path path) {
            System.out.println("added " + path);
            result.add(path);

        }

        public ArrayList<Path> get() {
            return result;
        }

        public File getNext() {
            try {
                File end = result.get(counter).toFile();
                System.out.println("returned " + end);
                counter = counter + 1;
                return end;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Path.of("1").toFile();
        }

        public boolean isOnList(File file) {
            Path path = Path.of(file.getPath());
            if (result.contains(path)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
