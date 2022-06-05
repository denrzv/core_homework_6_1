package io.github.denrzv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {

    @Test
    @DisplayName("Успешное создание каталога")
    public void successCreateFolder() {
        //given
        String folderName = "TestFolder";
        //when
        boolean result = Main.createFolder(folderName);
        //then
        Assertions.assertNotEquals(result, false);
        assertThrows(NullPointerException.class, () -> Main.createFolder(null));
    }

    @Test
    @DisplayName("Успешное удаление каталога")
    public void successDeleteFolder() {
        //given
        String folderName = "TestFolder";
        //when
        boolean result = Main.deleteFolder(new File(folderName));
        //then
        Assertions.assertNotEquals(result, false);
        assertThrows(NullPointerException.class, () -> Main.deleteFolder(null));
    }

    @Test
    @DisplayName("Успешное создание файла")
    public void successCreateFile() {
        //given
        String fileName = "TestFile.txt";
        //when
        boolean result = Main.createFile(fileName);
        //then
        Assertions.assertNotEquals(result, false);
        assertThrows(NullPointerException.class, () -> Main.createFile(null));
    }

    @Test
    @DisplayName("Успешное удаление файла")
    public void successDeleteFile() {
        //given
        String fileName = "TestFile.txt";
        //when
        boolean result = Main.deleteFile(fileName);
        //then
        Assertions.assertNotEquals(result, false);
        assertThrows(NullPointerException.class, () -> {Main.deleteFile(null);});
    }
}
