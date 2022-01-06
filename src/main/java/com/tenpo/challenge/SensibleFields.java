package com.tenpo.challenge;

import java.util.stream.Stream;

public enum SensibleFields {

    PASSWORD("password"),
    CONFIRMED_PASSWORD("passwordConfirm");

    private String fieldName;

    SensibleFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Stream<SensibleFields> stream() {
        return Stream.of(SensibleFields.values());
    }

    public String getFieldName() {
        return fieldName;
    }

}
