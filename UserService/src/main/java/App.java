public class App {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(new UserService(new UserDAOImpl()));
        ui.start();
    }
}
