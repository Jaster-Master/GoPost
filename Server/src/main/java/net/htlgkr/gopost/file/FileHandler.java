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
        for (FileObject entry : entries) {
            if (entry.getObject() == null) continue;
            if (entry.isEncrypted() && entry.getObject() instanceof String string) {
                xmlEncoder.writeObject(Encrypt.SHA512(string));
            } else {
                xmlEncoder.writeObject(entry.getObject());
            }
            xmlEncoder.flush();
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
    }

    public Object readFromFile(FileObject readObject) {
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream(filename))) {
            Object object;
            while ((object = xmlDecoder.readObject()) != null) {
                if (readObject.isEncrypted()) {
                    if (Encrypt.SHA512(String.valueOf(readObject.getObject())).equals(object)) {
                        return object;
                    }
                } else {
                    if (object.equals(readObject.getObject())) {
                        return object;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeFromFile(FileObject remove) {
        if (remove.isEncrypted()) {
            xmlEncoder.remove(Encrypt.SHA512((String) remove.getObject()));
        } else {
            xmlEncoder.remove(remove.getObject());
        }
    }
}
