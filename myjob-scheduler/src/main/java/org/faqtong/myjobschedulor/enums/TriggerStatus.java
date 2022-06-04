package org.faqtong.myjobschedulor.enums;

/**
 * Registry type
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
public enum TriggerStatus {

    Stop(0), Running(1);

    private int value;

    TriggerStatus(int value) {
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
