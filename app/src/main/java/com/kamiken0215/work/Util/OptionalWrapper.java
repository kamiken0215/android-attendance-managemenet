package com.kamiken0215.work.Util;

import java.util.Optional;

public class OptionalWrapper {

    public Optional<String> getOptionalString(String str) {
        return Optional.ofNullable(str);
    }
}
