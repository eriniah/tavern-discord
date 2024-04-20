package com.tavern.utilities


import spock.lang.Specification

class ObjectUtilsTest extends Specification {

    def "Test identity"() {
        expect:
        ObjectUtils.identity(a) == a

        where:
        a << ["str", 42, null]
    }
}
