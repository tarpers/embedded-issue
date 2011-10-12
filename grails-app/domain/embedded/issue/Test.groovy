package embedded.issue

class Test {

    Date dateCreated
    String title
    int number
    TestEmbedded testEmbedded

    static constraints = {
    }

    static embedded = ['testEmbedded']

    String toString() {
        this.dump()
    }
}
