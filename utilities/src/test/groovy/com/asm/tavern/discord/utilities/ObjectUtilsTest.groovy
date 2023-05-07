package com.asm.tavern.discord.utilities

import com.asm.tavern.discord.utilities.stream.StreamChunkCollector
import spock.lang.Specification

import java.util.stream.Collectors

class ObjectUtilsTest extends Specification {

    def "Test identity"() {
        expect:
        ObjectUtils.identity(a) == a

        where:
        a << ["str", 42, null]
    }
}
