package com.Server.file;

import com.Server.encryption.Encrypt;

import java.beans.*;
import java.io.FileInputStream;

public class FileHandler {
    FileObject[] entries;
    private XMLEncoder xmlEncoder;
    private String filename;
    public FileHandler(String filename) {
    }

    public FileHandler() {
    }

    public FileHandler(String filename, XMLEncoder xmlEncoder, FileObject... entries) {
        this.entries = entries;
        this.xmlEncoder=xmlEncoder;
        this.filename=filename;
    }

    public void writeIntoFile() {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].getObject() == null) continue;
            if (entries[i].isEncrypted() && entries[i].getObject() instanceof String string) {
                xmlEncoder.writeObject(Encrypt.SHA512(string));
                xmlEncoder.flush();
            } else {
                xmlEncoder.writeObject(entries[i].getObject());
                xmlEncoder.flush();
            }
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

    public Object readFromFile(FileObject read) {
        try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream(filename))) {
            Object line;
            while ((line = xmlDecoder.readObject()) != null) {
                if (read.isEncrypted()) {
                    if (Encrypt.SHA512(String.valueOf(read.getObject())).equals(line)) {
                        return line;
                    }
                } else {
                    if (line.equals(read.getObject())) {
                        return line;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeFromFile(FileObject remove) {
        if (remove.isEncrypted())
            xmlEncoder.remove(Encrypt.SHA512((String) remove.getObject()));
        else
            xmlEncoder.remove(remove.getObject());
    }
}
