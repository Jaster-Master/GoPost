package com.server.file;

public class FileObject {
    private Object object;
    private boolean encrypted;

    public FileObject(Object object, boolean encrypted) {
        this.object = object;
        this.encrypted = encrypted;
    }

    public FileObject() {
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
