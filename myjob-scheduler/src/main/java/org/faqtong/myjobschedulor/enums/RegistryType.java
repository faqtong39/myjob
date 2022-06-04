package org.faqtong.myjobschedulor.enums;

/**
 * Registry type
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
public enum RegistryType {

    Auto(0), Manual(1);

    private int value;

    RegistryType(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    public int value() {
        return this.value;
    }
}
