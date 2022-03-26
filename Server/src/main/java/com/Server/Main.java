package com.Server;

import com.Server.file.FileObject;
import com.Server.file.FileHandler;
import com.Server.user.Profile;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        FileHandler writerRead = null;
        try {
            writerRead = new FileHandler("userLoginData.xml",new XMLEncoder(new FileOutputStream("userLoginData.xml",true)),
                    new FileObject(new Profile("Max","Kammerer","adsfasdf"),false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writerRead.writeIntoFile();
//        writerRead.removeFromFile(new FileObject("Zecher",false));
        //new Server().startServer(args);
    }
}
