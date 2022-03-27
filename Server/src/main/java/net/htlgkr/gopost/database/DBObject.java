package net.htlgkr.gopost.database;

import java.io.Serializable;
import java.util.Objects;

public class DBObject implements Serializable {
    private Object object;
    private boolean isEncrypted;

    public DBObject() {
    }

    public DBObject(Object object, boolean isEncrypted) {
        this.object = object;
        this.isEncrypted = isEncrypted;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.isEncrypted = encrypted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBObject that = (DBObject) o;
        return isEncrypted == that.isEncrypted && Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, isEncrypted);
    }
}
