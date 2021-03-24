public class Main {

    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalStateException("\nError!\nYou need to pass path to json file with figures coordinates!\n");

        Engine engine = new Engine();
        engine.start(args[0]);
    }
}
