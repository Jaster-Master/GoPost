package net.htlgkr.gopost.file;

import net.htlgkr.gopost.util.Encrypt;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;

public class FileHandler {
    private FileObject[] entries;
    private XMLEncoder xmlEncoder;
    private String filename;

    public FileHandler() {
    }

    public FileHandler(String filename) {
        this.filename = filename;
    }

    public FileHandler(String filename, XMLEncoder xmlEncoder, FileObject... entries) {
        this.entries = entries;
        this.xmlEncoder = xmlEncoder;
        this.filename = filename;
    }

    public void writeIntoFile() {

    }
//        try {
//            XMLEncoder writer = new XMLEncoder(new FileOutputStream("fileName"));
//            writer.setPersistenceDelegate(LocalDate.class,
//                    new PersistenceDelegate() {
//                        @Override
//                        protected Expression instantiate(Object localDate, Encoder encoder) {
//                            return new Expression(localDate, LocalDate.class, "parse", new Object[]{localDate.toString()});
//                        }
//                    });
//            writer.setPersistenceDelegate(BigDecimal.class,
//                    new PersistenceDelegate() {
//                        @Override
//                        protected Expression instantiate(Object bigDecimal, Encoder encoder) {
//                            return new Expression(bigDecimal, BigDecimal.class, "new", new Object[]{bigDecimal.toString()});
//                        }
//                    });
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    public Object readFromFile(FileObject readObject) {
        return null;
    }

    public void removeFromFile(FileObject remove) {

    }
}
