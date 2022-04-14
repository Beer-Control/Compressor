package com.company;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;


public class Compressor {
    public static void main(String[] args) {
        try {
            Compresssion.main();
        }
        catch (Exception e){
            System.out.println("Все ок");
        }
    }
    public static class Compresssion {

        public static void main() throws IOException {
            int countDir = 0;
            boolean isDirAdded = true;
            Saver saver = new Saver();
            Path directory = Path.of("C:\\Цыбизов ознакомление");
            File file = directory.toFile();
            searchFolders(file, saver);
            ArrayList<Path> directories1 = saver.get();
            HashMap<Path, Path> directories = new HashMap<>();
            for (Path path : directories1) {
                directories.put(path, path);
            }
//            directories.put(directory, directory);
//            for (Path key : directories.keySet()) {
//                Path dir = directories.get(key);
//                try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
//                    for (Path path : files) {
//                        if (Files.isDirectory(path)) {
//                            directories.put(path, path);
//                            System.out.println("Получена директория " + path);
//                        }
//                    }
//                }
//            }
//            while (isDirAdded) {
//                if (directories.size() == 0){
//                    isDirAdded = false;
//                }
//                for (int i = 0; i < directories.size(); i++) {
//                    try (DirectoryStream<Path> files = Files.newDirectoryStream(directories.get(i))) {
//                        for (Path path : files) {
//                            if(Files.isRegularFile(path)){
//                                continue;
//                            }
//                            if (contains(path.toString(), "comp")){
//                                directories.remove(i);
//                                break;
//                            }
//                            if (Files.isDirectory(path)) {
//                                directories.add(path);
//                                System.out.println("Получена директория " + path);
//                                countDir++;
//                                continue;
//                            }
//
//                            isDirAdded = false;
//                        }
//
//                    }
//                }
//            }
            isDirAdded = true;
//            while (isDirAdded) {
//                if (directories.size() == 0){
//                    isDirAdded = false;
//                }
//                for (int i = countDir; i < directories.size(); i++) {
//                    try (DirectoryStream<Path> files = Files.newDirectoryStream(directories.get(i))) {
//                        for (Path path : files) {
//                            if(Files.isRegularFile(path)){
//                                continue;
//                            }
//                            if (contains(path.toString(), "comp")){
//                                directories.remove(i);
//                                return;
//                            }
//                            if (Files.isDirectory(path)) {
//                                directories.add(path);
//                                countDir++;
//                                System.out.println("Получена директория " + path.toString());
//                                isDirAdded = true;
//                                continue;
//                            }
//                            isDirAdded = false;
//                        }
//
//                    }
//                }
//            }
            for (Path key : directories.keySet()) {
//                File dir = new File(directories.get(i).toString());
//              deleteRecursively(dir);
                Path dir = directories.get(key);
                System.out.println("В списке присутствует директория " + dir);
                if (contains(dir.toString(), "comp")) {
                    System.out.println("Найдена директория с результатами " + dir);
                    Path path = dir.getParent();
                    directories.remove(path);
                    System.out.println("Директория " + path + " удалена из списка");
                    directories.remove(dir);
                }
            }
            for (Path key : directories.keySet()) {
                Path dir = directories.get(key);
                Path out = Files.createDirectories(Path.of(dir.getParent() + "\\comp\\" + dir.getFileName()));
                try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
                    for (Path path : files) {
                        System.out.println(path);
                        if (Files.isDirectory(path)) {
                            continue;
                        }

                        String path1 = path.toString();
                        String path2 = out.toString();
                        String ext = getFileExtension(path1);
                        System.out.println(ext);
                        boolean a = ext.equalsIgnoreCase("JPG");
                        boolean b = ext.equalsIgnoreCase("jpeg");
                        if (!a && !b) {
                            continue;
                        }


                        File input = new File(path1);
                        BufferedImage image = ImageIO.read(input);

                        File compressedImageFile = new File(path2 + "\\" + path.getFileName());
                        OutputStream os = new FileOutputStream(compressedImageFile);

                        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                        ImageWriter writer = writers.next();

                        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                        writer.setOutput(ios);

                        ImageWriteParam param = writer.getDefaultWriteParam();

                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.1f);  // Change the quality value you prefer
                        writer.write(null, new IIOImage(image, null, null), param);
                        System.out.println(compressedImageFile);
                        os.close();
                        ios.close();
                        writer.dispose();
                    }
                }
            }
        }
    }

    private static String getFileExtension(String fileName) {
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }

    private static void deleteRecursively(File file) {
        if (file.isDirectory() && file.listFiles().length == 0) {
            file.delete();
        } else if (file.isDirectory() && file.list().length > 0) {
            for (File file1 : file.listFiles()) {
                deleteRecursively(file1);
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
                if (isHaveComp(value)){
                    continue;
                }
                if (saver.isOnList(value)){
                    return;
                }
                Path path = Path.of(value.getPath());
                saver.add(path);
            }
//            File file1 = saver.getNext();
//            if (file1.isDirectory()) {
//                searchFolders(file1, saver);
//            }
//            return;
        }
        File file1 = saver.getNext();
        if (file1.isDirectory()) {
            searchFolders(file, saver);
        }
        return;
    }

    public static class Saver {
        public int counter = 0;
        ArrayList<Path> result = new ArrayList<>();

        public void add(Path path) {
            System.out.println("added " + path);
            result.add(path);
            counter++;
        }

        public ArrayList<Path> get() {
            return result;
        }

        public File getNext() {
            try {
//                int getter = counter;
//                if (counter == 0){
//                    counter++;
//                }
                File end = result.get(counter - 1).toFile();
                System.out.println("returned " + end);
                counter--;
                return end;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Path.of("1").toFile();
        }

        public boolean isOnList (File file){
            Path path = Path.of(file.getPath());
            if (result.contains(path)){
                return true;
            }
            else {
                return false;
            }
        }
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


}
