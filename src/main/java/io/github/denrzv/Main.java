package io.github.denrzv;

import java.io.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static StringBuilder logger = new StringBuilder();

    public static void main(String[] args) {

        //первая часть задания
        addLog("Запуск программы");
        addLog("Очистка старых каталогов");
        deleteFolder(new File("Games"));

        createFolder("Games");
        createFolder("Games/src");
        createFolder("Games/res");
        createFolder("Games/savegames");
        createFolder("Games/temp");
        createFolder("Games/src/main");
        createFolder("Games/src/test");
        createFolder("Games/res/drawables");
        createFolder("Games/res/vectors");
        createFolder("Games/res/icons");
        createFile("Games/src/Main.java");
        createFile("Games/src/Utils.java");
        createFile("Games/temp/temp.txt");

        writeFile("Games/temp/temp.txt", logger.toString());

        //вторая часть задания
        GameProgress gameProgress1 = new GameProgress(94, 10, 2, 254.32);
        GameProgress gameProgress2 = new GameProgress(85, 12, 5, 453);
        GameProgress gameProgress3 = new GameProgress(50, 14, 10, 873.1);

        String save1 = "Games/savegames/save1.dat";
        String save2 = "Games/savegames/save2.dat";
        String save3 = "Games/savegames/save3.dat";

        saveGame(save1, gameProgress1);
        saveGame(save2, gameProgress2);
        saveGame(save3, gameProgress3);

        String[] save = {save1, save2, save3};

        zipFiles("Games/savegames/save.zip", save);

        Arrays.stream(save).forEach(Main::deleteFile);

        writeFile("Games/temp/temp.txt", logger.toString());

        //третья часть задания

        openZip("Games/savegames/save.zip", "Games/savegames");
        addLog(openProgress("Games/savegames/save2.dat").toString());
        addLog("Завершение программы");
        writeFile("Games/temp/temp.txt", logger.toString());

        System.out.print(logger);
    }

    public static void addLog(String msg) {
        logger.append(new Timestamp(System.currentTimeMillis()))
                .append(" ")
                .append(msg)
                .append("\n");
    }

    public static boolean createFile(String fileName) {
        boolean result = false;
        if (fileName != null && fileName.length() > 0) {
            File file = new File(fileName);
            try {
                if(file.createNewFile()) {
                    addLog("Файл " + file.getAbsolutePath() + " создан");
                    result = true;
                } else {
                    addLog("ОШИБКА! Не удалось создать файл " + fileName);
                }
            } catch (IOException e) {
                addLog(e.getMessage());
            }
        } else {
            addLog("ОШИБКА! Файл не задан");
            throw new NullPointerException("ОШИБКА! Файл не задан");
        }
        return result;
    }

    public static boolean createFolder(String folderName) {
        boolean result = false;
        if (folderName != null && folderName.length() > 0) {
            File folder = new File(folderName);
            if (folder.mkdir()) {
                addLog("Каталог " + folder.getAbsolutePath() + " создан");
                result = true;
            } else {
                addLog("ОШИБКА! Не удалось создать каталог " + folderName);
            }
        } else {
            addLog("ОШИБКА! Имя каталога для создания не задано");
            throw new NullPointerException("ОШИБКА! Имя каталога для создания не задано");
        }
        return result;
    }

    public static boolean deleteFile(String file) {
        boolean result = false;
        if (file != null && file.length() > 0) {
            File fileToDelete = new File(file);
            if (fileToDelete.delete()) {
                addLog("Удалён файл " + fileToDelete.getAbsolutePath());
                result = true;
            } else {
                addLog("ОШИБКА! Не удалось удалить файл " + fileToDelete.getAbsolutePath());
            }
        } else {
            addLog("ОШИБКА! Имя файла для удаления не задано");
            throw new NullPointerException("ОШИБКА! Имя файла для удаления не задано");
        }
        return result;
    }

    public static boolean deleteFolder(File folderToBeDeleted) {
        if (folderToBeDeleted == null) {
            throw new NullPointerException("ОШИБКА! folderToBeDeleted не существует");
        }
        File[] allContents = folderToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFolder(file);
            }
        }
        return folderToBeDeleted.delete();
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        if (path != null && path.length() > 0) {
            try (FileInputStream fis = new FileInputStream(path);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                gameProgress = (GameProgress) ois.readObject();
                addLog("Десериализован " + path + " в объект класса " + gameProgress.getClass().getName());
            } catch (Exception e) {
                addLog(e.getMessage());
            }
        } else {
            addLog("ОШИБКА! Имя файла для открытия не задан");
        }
        return gameProgress;
    }

    public static void openZip(String zipFile, String path) {
        if (zipFile != null && zipFile.length() > 0 && path != null && path.length() > 0) {
            try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
                ZipEntry entry;
                String name;
                byte[] buffer = new byte[1024];
                while ((entry = zin.getNextEntry()) != null) {
                    name = entry.getName();
                    FileOutputStream fout = new FileOutputStream(name);
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    int len;
                    while ((len = zin.read(buffer)) > 0) {
                        fout.write(buffer, 0, len);
                    }
                    fout.flush();
                    zin.closeEntry();
                    fout.close();
                    addLog("Разархивирован файл " + new File(name).getAbsolutePath());
                }
            } catch (IOException e) {
                addLog(e.getMessage());
            }
        } else {
            addLog("ОШИБКА! Имя файла или путь для распаковки не заданы");
        }
    }

    public static void saveGame(String path, GameProgress game) {
        if (path != null && path.length() > 0 && game != null) {
            try (FileOutputStream fos = new FileOutputStream(path);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(game);
            } catch (IOException e) {
                addLog(e.getMessage());
            }
        } else {
            addLog("ОШИБКА! Путь для сохранения игры недоступен или не создан объект прогресса игры");
        }
    }

    public static void writeFile(String fileName, String text) {
        if (fileName != null && fileName.length() > 0 && text != null && text.length() > 0) {
            File file = new File(fileName);
            if (file.exists() && file.canWrite()) {
                try(FileWriter fw = new FileWriter(file)) {
                    fw.write(text);
                    fw.flush();
                } catch (IOException e) {
                    addLog(e.getMessage());
                }
            } else {
                addLog("ОШИБКА! Файл " + fileName + " не доступен для записи");
            }
        } else {
            addLog("ОШИБКА! Не указано имя файла для записи или не задан текст");
        }
    }

    public static void zipFiles(String path, String[] files) {
        if (path != null && path.length() > 0 && files != null && files.length > 0) {

            try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
                for (String file : files) {
                    try(FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry entry = new ZipEntry(file);
                        zout.putNextEntry(entry);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        zout.write(buffer);
                        zout.closeEntry();
                        addLog("Архивирован файл " + new File(file).getAbsolutePath() + " в " +
                                new File(path).getAbsolutePath());
                    } catch (IOException e) {
                        addLog(e.getMessage());
                    }
                }
            } catch (IOException e) {
                addLog(e.getMessage());
            }
        } else {
            addLog("ОШИБКА! Не указано имя архива для записи или не заданы файлы для архивации");
        }
    }
}