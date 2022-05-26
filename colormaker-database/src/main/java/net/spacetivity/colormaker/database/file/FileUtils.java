package net.spacetivity.colormaker.database.file;

import net.spacetivity.colormaker.database.DatabaseRepository;

import java.io.*;

public class FileUtils {

    public <T> T readFile(File file, Class<T> cls) {
        try {
            return DatabaseRepository.GSON.fromJson(new FileReader(file), cls);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveFile(File file, Object object) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            DatabaseRepository.GSON.toJson(object, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
