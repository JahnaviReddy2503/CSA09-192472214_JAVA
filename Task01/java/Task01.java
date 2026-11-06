/**
 * Task01 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task01 {
    private Task01() {}

    /* ===== LoginFrame.java =====
    package parking.ui;
    
    import parking.service.ParkingService;
    import javax.swing.*;
    import java.awt.*;
    
    public final class LoginFrame extends JFrame {
        public LoginFrame(ParkingService service) {
            setTitle("Administrator Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(360, 220);
            setLocationRelativeTo(null);
    
            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JTextField username = new JTextField();
            JPasswordField password = new JPasswordField();
            JButton login = new JButton("Sign in");
            form.add(new JLabel("Username:"));
            form.add(username);
            form.add(new JLabel("Password:"));
            form.add(password);
            form.add(new JLabel());
            form.add(login);
            login.addActionListener(event -> {
                if ("admin".equals(username.getText().trim())
                        && "admin123".equals(new String(password.getPassword()))) {
                    dispose();
                    new DashboardFrame(service).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Use the administrator credentials from the README.",
                            "Login failed", JOptionPane.ERROR_MESSAGE);
                }
            });
            add(form);
        }
    }
    ===== end LoginFrame.java ===== */

    /* ===== Main.java =====
    package parking.app;
    
    import parking.model.*;
    import parking.service.ParkingService;
    import parking.exception.ParkingException;
    import parking.ui.LoginFrame;
    import javax.swing.*;
    
    public final class Main {
        private Main() {}
        public static void main(String[] args) {
            ParkingService service = new ParkingService();
            try {
                Student demoUser = new Student("STU001", "Demo Student");
                service.registerVehicle(new Car("DEMO-001", demoUser));
            } catch (ParkingException | IllegalArgumentException ignored) {
                // Demo data is optional; the UI remains usable if it cannot be loaded.
            }
            SwingUtilities.invokeLater(() -> new LoginFrame(service).setVisible(true));
        }
    }
    ===== end Main.java ===== */

}
