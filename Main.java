
public class Main {
    public static void main(String...args) throws InterruptedException {
        System.out.println("TDLIb shared PoC");
        new MultitenantClient("gocertius");
        new MultitenantClient("eadcustody");
        while (true) {
            Thread.sleep(1_000);
        }
    }
}
