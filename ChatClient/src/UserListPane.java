import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class UserListPane extends JPanel implements UserStatusListener {
    private final ChatClient client;
    private JList<String> userListUI;
    private DefaultListModel<String> userListModel;

    public UserListPane(ChatClient client) {
        this.client = client;
        this.client.addUserStatusListener(this);

        // create a scrolling panel that will contain all online users
        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(userListUI);
        scroll.getViewport().getView().setBackground(new Color(35, 39, 42));
        scroll.getViewport().getView().setForeground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);

        // on double click, open a direct message window to the user
        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String login = userListUI.getSelectedValue();
                    MessagePane messagePane = new MessagePane(client, login);

                    JFrame f = new JFrame("Message: " + login);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setSize(500, 500);
                    f.getContentPane().add(messagePane, BorderLayout.CENTER);
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);
                }
            }
        });

        // create a button that opens a group chat window
        Button enterGC = new Button("Open Group Chat");
        enterGC.setBackground(new Color(153, 170, 181));
        enterGC.setForeground(Color.WHITE);
        enterGC.setSize(100, 100);
        add(enterGC, BorderLayout.SOUTH);

        // on button click, open group chat window
        enterGC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGC();
            }
        });
    }

    /**
     * Open a group chat window.
     */
    private void openGC() {
        try {
            client.joinGC("#groupchat");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        String login = userListUI.getSelectedValue();
        GroupMessagePane groupMessagePane = new GroupMessagePane(client, login);

        JFrame f = new JFrame("Group Chat");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setBackground(new Color(35, 39, 42));
        f.setSize(400, 600);
        f.getContentPane().add(groupMessagePane, BorderLayout.CENTER);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    @Override
    public void online(String login) {
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 7140);

        UserListPane userListPane = new UserListPane(client);
        JFrame frame = new JFrame("User List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        frame.getContentPane().add(userListPane, BorderLayout.CENTER);
        frame.setVisible(true);

        if (client.connect()) {
            try {
                client.login("guest", "guest");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}