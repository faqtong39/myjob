package org.faqtong.myjobschedulor.enums;

/**
 * Registry type
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
public enum Result {

    Success("Success"), Failure("Failure");

    private String value;

    Result(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    public String value() {
        return this.value;
    }
}
