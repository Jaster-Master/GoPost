package net.htlgkr.gopost.server;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.file.FileHandler;
import net.htlgkr.gopost.file.FileObject;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {

    private static FileHandler fileHandler;
    private static final String FILE_NAME = "userLoginData.xml";

    public static void main(String[] args) {
        createFileHandler();
        fileHandler.writeIntoFile();
//        writerRead.removeFromFile(new FileObject("Zecher",false));
        //new Server().startServer(args);
    }

    private static void createFileHandler() {
        try {
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(FILE_NAME, true));
            Profile profile = new Profile("Max", "Kammerer", "adsfasdf");
            FileObject fileObject = new FileObject(profile, false);
            fileHandler = new FileHandler(FILE_NAME, encoder, fileObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
