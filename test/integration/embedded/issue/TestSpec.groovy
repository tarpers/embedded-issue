package embedded.issue

import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

class TestSpec extends IntegrationSpec {

    @Unroll
    def 'list with criteria'() {
        setup:
        def embedded = new TestEmbedded(title: 'embedded', number: 1, dateCreated: new Date() + 2)
        def test = new Test(title: 'parent', number: -1, testEmbedded: embedded).save(failOnError: true)

        when:
        def list = Test.withCriteria {
            if (startDate) ge 'dateCreated', startDate
            if (endDate) le 'dateCreated', endDate
            if (min != null) ge 'number', min
            if (max != null) le 'number', max
            if (title) eq 'title', title
            if (eqTestDate) eq 'dateCreated', test.dateCreated
            if (eqEmDate) eq 'dateCreated', embedded.dateCreated
            if (number) eq 'number', number
        }
        println()

        then:
        list.size() == expectedSize

        where:
        startDate      | endDate        | min  | max  | title      | eqTestDate | eqEmDate | number | expectedSize
        null           | null           | null | null | null       | false      | false    | null   | 1              // dateCreated ge/le tests
        new Date() - 1 | new Date() + 1 | null | null | null       | false      | false    | null   | 1
        new Date() + 1 | null           | null | null | null       | false      | false    | null   | 0
        null           | null           | -1   | 1    | null       | false      | false    | null   | 1              // number ge/le tests
        null           | null           | -1   | 0    | null       | false      | false    | null   | 1
        null           | null           | 0    | 1    | null       | false      | false    | null   | 0
        null           | null           | null | null | 'parent'   | false      | false    | null   | 1              // title eq tests
        null           | null           | null | null | 'embedded' | false      | false    | null   | 0
        null           | null           | null | null | null       | true       | false    | null   | 1              // dateCreated eq test
        null           | null           | null | null | null       | false      | true     | null   | 0
        null           | null           | null | null | null       | false      | false    | -1     | 1              // number eq test
        null           | null           | null | null | null       | false      | false    | 1      | 0
    }

    @Unroll
    def 'find all by date created'() {
        setup:
        def embedded = new TestEmbedded(title: 'embedded', number: 1, dateCreated: new Date() + 2)
        def test = new Test(title: 'parent', number: -1, testEmbedded: embedded).save(failOnError: true)

        when:
        def list = Test.findAllByDateCreated(useTestDom ? test.dateCreated : embedded.dateCreated)
        println()

        then:
        list.size() == expectedSize

        where:
        useTestDom | expectedSize
        true       | 1
        false      | 0
    }

    @Unroll
    def 'find all by number'() {
        setup:
        def embedded = new TestEmbedded(title: 'embedded', number: 1, dateCreated: new Date() + 2)
        new Test(title: 'parent', number: -1, testEmbedded: embedded).save(failOnError: true)

        when:
        def list = Test.findAllByNumber(number)
        println()

        then:
        list.size() == expectedSize

        where:
        number | expectedSize
        -1     | 1
        1      | 0
    }

    @Unroll
    def 'find all by title'() {
        setup:
        def embedded = new TestEmbedded(title: 'embedded', number: 1, dateCreated: new Date() + 2)
        new Test(title: 'parent', number: -1, testEmbedded: embedded).save(failOnError: true)

        when:
        def list = Test.findAllByTitle(title)
        println()

        then:
        list.size() == expectedSize

        where:
        title      | expectedSize
        'parent'   | 1
        'embedded' | 0
    }
}
