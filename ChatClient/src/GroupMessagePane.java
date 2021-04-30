import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GroupMessagePane extends JPanel implements MessageListener {
    private final ChatClient client;
    private static String login;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private JTextField inputField = new JTextField();

    public GroupMessagePane(ChatClient client, String login) {
        this.client = client;
        this.login = login;

        client.addMessageListener(this);

        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(messageList);
        scroll.getViewport().getView().setBackground(new Color(35, 39, 42));
        scroll.getViewport().getView().setForeground(Color.WHITE);

        inputField.setBackground(new Color(35, 39, 42));
        inputField.setForeground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        // send message to the group chat
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                try {
                    client.msgGC("#groupchat", text);
                    inputField.setText("");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String fromLogin, String msgBody) {
        String name = fromLogin.substring(11);
        String line = name + ": " + msgBody;
        listModel.addElement(line);
    }
}