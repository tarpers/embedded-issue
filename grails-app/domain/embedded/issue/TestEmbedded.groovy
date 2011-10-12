package embedded.issue

class TestEmbedded {

    Date dateCreated
    String title
    int number

    static constraints = {
    }

    String toString() {
        this.dump()
    }
}
