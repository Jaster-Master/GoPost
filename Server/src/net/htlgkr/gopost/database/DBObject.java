package net.htlgkr.gopost.database;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class DBObject implements Serializable {

    private Object object;

    public DBObject() {
    }

    public DBObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public Integer getInteger() {
        return (Integer) object;
    }

    public Long getLong() {
        return (Long) object;
    }

    public Boolean getBoolean() {
        return (Boolean) object;
    }

    public String getString() {
        return (String) object;
    }

    public Double getDouble() {
        return (Double) object;
    }

    public byte[] getBlob() {
        if (object == null) return null;
        Blob blob = (Blob) object;
        try {
            return blob.getBytes(1L, (int) blob.length());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getTimestamp() {
        return (Timestamp) object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBObject dbObject = (DBObject) o;
        return Objects.equals(object, dbObject.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }
}
