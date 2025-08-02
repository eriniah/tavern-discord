package com.tavern.domain.model;

import com.tavern.utilities.StringUtils;

public final class TavernMetadata {
    private final String version;

    public TavernMetadata(String version) {
        if (StringUtils.isNullOrBlank(version)) {
            throw new IllegalArgumentException("Version cannot be null or blank");
        }
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
