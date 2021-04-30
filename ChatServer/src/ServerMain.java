import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates a server object and starts the server
 */
public class ServerMain extends JFrame {

    public ServerMain() {
        super("Server");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 80));
        panel.setBackground(new Color(35, 39, 42));

        add(panel);

        JLabel label = new JLabel("Server is running...");
        label.setForeground(Color.WHITE);

        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createVerticalGlue());

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        Button close = new Button("Stop");
        close.setBackground(new Color(153, 170, 181));
        close.setForeground(Color.white);
        panel.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        int port = 7140;

        ServerMain sm = new ServerMain();
        sm.setVisible(true);

        Server server = new Server(port);
        server.start();
    }
}