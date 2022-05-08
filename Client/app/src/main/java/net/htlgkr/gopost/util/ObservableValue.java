package net.htlgkr.gopost.util;

import java.io.Serializable;

public class ObservableValue<T> implements Serializable {

    private T observableValue;
    private ChangeListener<T> onValueChange;
    private SetListener<T> onValueSet;

    public ObservableValue() {
    }

    public ObservableValue(T observableValue) {
        this.observableValue = observableValue;
    }

    public ObservableValue(ChangeListener<T> onValueChange) {
        this.onValueChange = onValueChange;
    }

    public ObservableValue(SetListener<T> onValueSet) {
        this.onValueSet = onValueSet;
    }

    public ObservableValue(T observableValue, SetListener<T> onValueSet) {
        setValue(observableValue);
        this.onValueSet = onValueSet;
    }

    public ObservableValue(T observableValue, SetListener<T> onValueSet, ChangeListener<T> onValueChange) {
        setValue(observableValue);
        this.onValueSet = onValueSet;
        this.onValueChange = onValueChange;
    }

    public ObservableValue(SetListener<T> onValueSet, T observableValue) {
        this.onValueSet = onValueSet;
        setValue(observableValue);
    }

    public ObservableValue(T observableValue, ChangeListener<T> onValueChange) {
        setValue(observableValue);
        this.onValueChange = onValueChange;
    }

    public ObservableValue(ChangeListener<T> onValueChange, T observableValue) {
        this.onValueChange = onValueChange;
        setValue(observableValue);
    }

    public boolean isEmpty() {
        return observableValue == null;
    }

    public boolean hasChangeListener() {
        return onValueChange != null;
    }

    public boolean hasSetListener() {
        return onValueSet != null;
    }

    public synchronized void setValue(T observableValue) {
        if (hasChangeListener() && this.observableValue != observableValue) {
            onValueChange.onValueChange(this.observableValue, observableValue);
        }
        if (hasSetListener()) {
            onValueSet.onValueSet(observableValue);
        }
        if (boundValue != null && !boundCall) {
            boundValue.boundCall = true;
            boundValue.setValue(observableValue);
            boundValue.boundCall = false;
        }
        this.observableValue = observableValue;
    }

    public T getValue() {
        return observableValue;
    }

    public synchronized void setOnValueChange(ChangeListener<T> onValueChange) {
        this.onValueChange = onValueChange;
    }

    public ChangeListener<T> getOnValueChange() {
        return onValueChange;
    }

    public synchronized void setOnValueSet(SetListener<T> onValueSet) {
        this.onValueSet = onValueSet;
    }

    public SetListener<T> getOnValueSet() {
        return onValueSet;
    }

    public interface ChangeListener<T> extends Serializable {
        void onValueChange(T oldValue, T newValue);
    }

    public interface SetListener<T> extends Serializable {
        void onValueSet(T value);
    }

    private ObservableValue<T> boundValue;
    private ObservableValue<T> boundedValue;
    private boolean boundCall;

    public void bind(ObservableValue<T> boundValue) {
        boundCall = true;
        boundValue.boundValue = this;
        this.boundedValue = boundValue;
    }

    public void bindBidirectional(ObservableValue<T> boundValue) {
        boundCall = true;
        boundValue.boundValue = this;
        boundValue.boundedValue = this;
        this.boundValue = boundValue;
        this.boundedValue = boundValue;
    }

    public void unbind() {
        if (boundedValue == null) return;
        boundValue = null;
        boundedValue.boundValue = null;
        boundedValue.boundedValue = null;
        boundedValue = null;
    }

    public T getObservableValue() {
        return observableValue;
    }

    public void setObservableValue(T observableValue) {
        this.observableValue = observableValue;
    }

    public ObservableValue<T> getBoundValue() {
        return boundValue;
    }

    public void setBoundValue(ObservableValue<T> boundValue) {
        this.boundValue = boundValue;
    }

    public ObservableValue<T> getBoundedValue() {
        return boundedValue;
    }

    public void setBoundedValue(ObservableValue<T> boundedValue) {
        this.boundedValue = boundedValue;
    }

    public boolean isBoundCall() {
        return boundCall;
    }

    public void setBoundCall(boolean boundCall) {
        this.boundCall = boundCall;
    }
}
