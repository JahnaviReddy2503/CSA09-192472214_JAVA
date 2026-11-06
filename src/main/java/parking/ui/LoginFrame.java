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
