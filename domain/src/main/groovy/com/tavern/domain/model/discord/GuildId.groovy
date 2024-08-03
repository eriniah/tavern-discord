package com.tavern.domain.model.discord


import com.tavern.domain.model.Identifier
import groovy.transform.InheritConstructors

class GuildId extends Identifier {

    private GuildId() { }

    public GuildId(String id) {
        super(id);
    }

}
