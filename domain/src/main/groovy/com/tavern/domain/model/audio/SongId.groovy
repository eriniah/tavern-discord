package com.tavern.domain.model.audio


import com.tavern.domain.model.Identifier
import groovy.transform.InheritConstructors

public class SongId extends Identifier {

    private SongId() {
    }

    public SongId(String id) {
        super(id)
    }
}
