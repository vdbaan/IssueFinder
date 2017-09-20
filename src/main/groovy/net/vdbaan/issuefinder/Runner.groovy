package net.vdbaan.issuefinder

class Runner {
    static void main(String... args) {
        testJavaFX()
        MainApp app = new MainApp()
        app.run(args)
    }

    static void testJavaFX() {
        try {
            Class.forName('javafx.stage.Stage')
        } catch(ClassNotFoundException e) {
            System.err.println 'JavaFX8 Missing'
            System.err.println 'Please install JavaFX, for example:'
            System.err.println '- sudo apt-get install openjfx'
            System.exit(1)
        }
    }
}
