import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SubeFrame extends JFrame {
    private List<String> ipAddresses;
    private int blok;
    private String cihazTuru;
    private JPanel subeframePanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton kamerasil;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JLabel subjectLabel;
    private JTextField subjectText;
    private JLabel messageLabel;
    private JTextArea messageText;
    private JButton sendMessage;
    private JList<String> messageList;
    private DefaultListModel<String> messageListModel;

    public SubeFrame(List<String> ipAddresses, int blok, String cihazTuru) {
        this.ipAddresses = ipAddresses;
        this.blok = blok;
        this.cihazTuru = cihazTuru != null && !cihazTuru.isEmpty() ? cihazTuru : "Şube";


        setSize(800, 600);
        setResizable(false);
        subeframePanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Split into two equal panels
        subeframePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        leftPanel = new JPanel(new BorderLayout(30, 30));
        subeframePanel.add(leftPanel);

        topPanel = new JPanel(new GridBagLayout());
        leftPanel.add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(centerPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        subjectLabel = new JLabel("Konu:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(subjectLabel, createGridBagConstraints(0, 0));

        subjectText = new JTextField(20);
        topPanel.add(subjectText, createGridBagConstraints(1, 0));

        messageLabel = new JLabel("Mesaj:");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(messageLabel, BorderLayout.NORTH);

        messageText = new JTextArea(5, 5);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageText);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        sendMessage = new JButton("Mesajı Gönder");
        sendMessage.setFont(new Font("Consolas", Font.BOLD, 14));
        sendMessage.setBackground(Color.pink);
        sendMessage.setFocusPainted(false);
        bottomPanel.add(sendMessage);

        sendMessage.addActionListener(e -> SendMessage());

        kamerasil = new JButton(" Kameraları Sil");
        kamerasil.setFont(new Font("Consolas", Font.BOLD, 14));
        kamerasil.setBackground(Color.pink);
        bottomPanel.add(kamerasil);
        kamerasil.addActionListener(e -> delete_camera());


        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Eski Mesajlar"));
        subeframePanel.add(rightPanel);

        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        messageList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane messageScrollPane = new JScrollPane(messageList);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);


        loadMessagesFromDatabase();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(subeframePanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void delete_camera() {
        int serverPort = 5432;
        for (String serverIp : ipAddresses) {
            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream outputStream = socket.getOutputStream()) {

                String command = "/remote 100";
                byte[] data = command.getBytes(StandardCharsets.UTF_8);

                outputStream.write(data, 0, data.length);
                outputStream.flush();

                JOptionPane.showMessageDialog(this, "Mesaj gönderildi");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void SendMessage() {
        String message = messageText.getText();
        String subject = subjectText.getText();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen mesajınızı giriniz");
            return;
        }
        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen konuyu giriniz");
            return;
        }
        int increment=0;
         if(cihazTuru=="Şube")
           {increment = 4;}
           else{increment=1;}


        for (String serverIp : ipAddresses) {
            int daireNo = calculateApartmentNumber(serverIp, increment);

            if (daireNo == -1) {
                JOptionPane.showMessageDialog(this, "Geçersiz IP adresi: " + serverIp, "Hata", JOptionPane.ERROR_MESSAGE);
                continue;
            }


            saveMessageToDatabase(subject, message, blok, cihazTuru, daireNo);

            messageListModel.addElement("Blok: " + blok + ", Daire: " + daireNo + ", Konu: " + subject + " - Mesaj: " + message);

            int serverPort = 5432;

            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream outputStream = socket.getOutputStream()) {

                Map<String, Object> messageData = new HashMap<>();
                messageData.put("opetype", 12);
                messageData.put("message", message);

                Gson gson = new Gson();
                String jsonString = gson.toJson(messageData);

                byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(data, 0, data.length);
                outputStream.flush();

              //  JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
         JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");
    }

    private void saveMessageToDatabase(String subject, String message, int blok, String cihazTuru, int daireNo) {
        String sql = "INSERT INTO message (subject, message, blok, cihaz_turu, daire_no) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subject);
            pstmt.setString(2, message);
            pstmt.setInt(3, blok);
            pstmt.setString(4, cihazTuru);
            pstmt.setInt(5, daireNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Mesaj ekleme hatası: " + e.getMessage());
        }
    }

    private int calculateApartmentNumber(String ipAddress, int increment) {
        String[] octets = ipAddress.split("\\.");
        if (octets.length == 4) {
            try {
                int lastOctet = Integer.parseInt(octets[3]);

                return ((lastOctet - 1) / increment) + 1;
            } catch (NumberFormatException e) {
                System.err.println("IP adresindeki son okteti işleme hatası: " + e.getMessage());
            }
        }
        return -1;
    }


    private void loadMessagesFromDatabase() {
        String sql = "SELECT subject, message, blok, cihaz_turu, daire_no FROM message";

        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            messageListModel.clear();

            while (rs.next()) {
                String subject = rs.getString("subject");
                String message = rs.getString("message");
                int blok = rs.getInt("blok");
                String cihazTuru = rs.getString("cihaz_turu");
                int daireNo = rs.getInt("daire_no");


                String formattedMessage = String.format(
                        "Konu: %s - Mesaj: %s - Blok: %d - Cihaz Türü: %s - Daire: %d",
                        subject, message, blok, cihazTuru, daireNo
                );

                messageListModel.addElement(formattedMessage);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanından mesajları yükleme hatası: " + e.getMessage());
        }
    }
}