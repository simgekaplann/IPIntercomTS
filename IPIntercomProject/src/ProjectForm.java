import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.swing.ImageIcon;



public class ProjectForm extends JFrame   {
    private JPanel mainPanel;
    private JComboBox<String> comboBox;
    private final JTextField textBoxBlok;
    private JButton scanButton;
    private JButton sendButton;
    private JTable ipTable;
    private DefaultTableModel tableModel;
    private ImageIcon image;
    private JPanel topPanel;
    private JButton button;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private  JLabel daire1;
    private JTextField daire1t;
    private JLabel daire2;
    private JTextField daire2t;



    public ProjectForm() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        String[] cihazTurleri = {"Şube", "Zil", "Site Zili", "Güvenlik"};
        comboBox = new JComboBox<>(cihazTurleri);
        comboBox.addActionListener(e -> updateIPFields());
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                change_img();
            }
        });


        textBoxBlok = new JTextField();
        daire1t=new JTextField();
        daire2t=new JTextField();
        textBoxBlok.setPreferredSize(new Dimension(60, 30));
        textBoxBlok.addActionListener(e -> updateIPFields());
        daire1t.setPreferredSize(new Dimension(60, 30));
        daire2t.setPreferredSize(new Dimension(60, 30));


        daire1=new JLabel("Daire 1: ");
        daire2=new JLabel("Daire 2:");


        scanButton = new JButton("Scan");
        scanButton.setFont(new Font("Consolas", Font.BOLD, 14));
        scanButton.setBackground(Color.pink);


        sendButton = new JButton("Send Message");
        sendButton.setFont(new Font("Consolas", Font.BOLD, 14));
        sendButton.setBackground(Color.pink);


        tableModel = new DefaultTableModel(new Object[]{"Select", "Reachable IPs"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        ipTable = new JTable(tableModel);
        ipTable.getColumnModel().getColumn(0).setPreferredWidth(1);
        ipTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        JScrollPane tableScrollPane = new JScrollPane(ipTable);

        // Üst panel
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.add(new JLabel("Cihaz Türü:"));
        topPanel.add(comboBox);
        topPanel.add(new JLabel("Blok:"));
        topPanel.add(textBoxBlok);
        topPanel.add(daire1);
        topPanel.add(daire1t);
        topPanel.add(daire2);
        topPanel.add(daire2t);



        textBoxBlok.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateIPFields();
            }
        });


        daire1t.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateIPFields();
            }
        });

        daire2t.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateIPFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateIPFields();
            }
        });


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(scanButton);



        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Project Form");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        ImageIcon imageIcon = new ImageIcon("netelsan.png");
        Image originalImage = imageIcon.getImage();


        Image newimg = originalImage.getScaledInstance(200, 180, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(newimg);
        setIconImage(scaledIcon.getImage());


        scanButton.addActionListener(e -> scanIPs());
         BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(new File("şube.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image scaledImage = buttonIcon.getScaledInstance(200, 200, Image.SCALE_SMOOTH);

         button = new JButton(new ImageIcon(scaledImage));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(200, 200));

        topPanel.add(button);


        BufferedImage buttonIcon1 = null;
        try {
            buttonIcon1 = ImageIO.read(new File("video-doorbell.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image scaledImage1 = buttonIcon1.getScaledInstance(150, 150,Image.SCALE_SMOOTH);

        button1 = new JButton(new ImageIcon(scaledImage1));
        button1.setBorderPainted(false);
        button1.setFocusPainted(false);
        button1.setContentAreaFilled(false);
        button1.setPreferredSize(new Dimension(200, 200));


        BufferedImage buttonIcon2=null;
        try{
            buttonIcon2=ImageIO.read(new File("video-doorbell.png"));
        }catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        Image scaledImage2 = buttonIcon2.getScaledInstance(150, 150,Image.SCALE_SMOOTH);
        button2 = new JButton(new ImageIcon(scaledImage2));
        button2.setBorderPainted(false);
        button2.setFocusPainted(false);
        button2.setContentAreaFilled(false);
        button2.setPreferredSize(new Dimension(200, 200));

        BufferedImage buttonIcon3 = null;
        try {
            buttonIcon3 = ImageIO.read(new File("güvenlik.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledImage3 = buttonIcon3.getScaledInstance(150, 150,Image.SCALE_SMOOTH);
        button3 = new JButton(new ImageIcon(scaledImage3));
        button3.setBorderPainted(false);
        button3.setFocusPainted(false);
        button3.setContentAreaFilled(false);
        button3.setPreferredSize(new Dimension(200, 200));
    }
    private void change_img() {
        int selectedIndex = comboBox.getSelectedIndex();

        Component[] components = topPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                topPanel.remove(component);
            }
        }

        JButton selectedButton;
        switch (selectedIndex) {
            case 0:
                selectedButton = button;
                break;
            case 1:
                selectedButton = button1;
                break;
            case 2:
                selectedButton = button2;
                break;
            default:
                selectedButton = button3;
                break;
        }

        topPanel.add(selectedButton);
        topPanel.revalidate();
        topPanel.repaint();


        for (ActionListener al : selectedButton.getActionListeners()) {
            selectedButton.removeActionListener(al);
        }

        if (selectedIndex == 0) {


            selectedButton.addActionListener(e -> {

                openSubeFrame();
            });
        } else if(selectedIndex==1)
        {
            selectedButton.addActionListener(e -> openBellFrame());

        }

        else if(selectedIndex==2)
        {
            selectedButton.addActionListener(e -> openBellFrame());
        }

        else if(selectedIndex==3)
        {
            selectedButton.addActionListener(e -> openSubeFrame());
        }
        else{

            selectedButton.addActionListener(e -> {
                System.out.println("Button clicked, but it's not index 0.");
            });
        }
    }

    private void openBellFrame() {
        List<String> selectedIPs = getSelectedIPs();
        int blok;
        int dairek;
        int dairek2;

        try {
            blok = Integer.parseInt(textBoxBlok.getText());
            dairek=Integer.parseInt(daire1t.getText());
            dairek2=Integer.parseInt(daire2t.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gerekli alanları giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        BellFrame bf = new BellFrame(selectedIPs);
        bf.setVisible(true);
    }


    private void openSubeFrame() {
        List<String> selectedIPs = getSelectedIPs();
        int blok;
        int dairek;
        int dairek2;

        try {
            blok = Integer.parseInt(textBoxBlok.getText());
            dairek=Integer.parseInt(daire1t.getText());
            dairek2=Integer.parseInt(daire2t.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gerekli alanları giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cihazTuru = comboBox.getSelectedItem() != null ? comboBox.getSelectedItem().toString() : "Şube";

        SubeFrame sf = new SubeFrame(selectedIPs, blok, cihazTuru);
        sf.setVisible(true);
    }





    private void updateIPFields() {
        int selectedIndex = comboBox.getSelectedIndex();
        String blok = textBoxBlok.getText();
        String daire1 = daire1t.getText();
        String daire2 = daire2t.getText();
        if (blok.isEmpty() || daire1.isEmpty() || daire2.isEmpty()) {
            return;
        }

        int startDaire = Integer.parseInt(daire1);
        int endDaire = Integer.parseInt(daire2);

        String startIP = "";
        String endIP = "";

        switch (selectedIndex) {
            case 0:
                int startLastOctet = 4 + (startDaire - 1) * 4;
                int endLastOctet = 4 + (endDaire - 1) * 4;
                startIP = "172." + blok + ".0." + startLastOctet;
                endIP = "172." + blok + ".0." + endLastOctet;
                break;
            case 1:
                int startLastOctet1=startDaire;
                int endLastOctet1=endDaire;
                startIP = "172." + blok + ".255."+startLastOctet1;
                endIP = "172." + blok + ".255."+endLastOctet1;
                break;
            case 2:
                int startLastOctet2=startDaire;
                int endLastOctet2=endDaire;
                startIP = "172.252.0."+startLastOctet2;
                endIP = "172.252.0."+endLastOctet2;
                break;
            case 3:
                int startLastOctet3=startDaire;
                int endLastOctet3=endDaire;
                startIP = "172.255.0."+startLastOctet3;
                endIP = "172.255.0."+endLastOctet3;
                break;
        }
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{false, "Start IP: " + startIP});
        tableModel.addRow(new Object[]{false, "End IP: " + endIP});
    }

    private void scanIPs() {
        if(textBoxBlok.getText().isEmpty() || !textBoxBlok.getText().matches("\\d+"))
        {
            JOptionPane.showMessageDialog(this,"Lütfen blok bilgisini giriniz.");
        }
        else{
        String startIP = tableModel.getValueAt(0, 1).toString().split(" ")[2];
        String endIP = tableModel.getValueAt(1, 1).toString().split(" ")[2];
        String subnet = startIP.substring(0, startIP.lastIndexOf("."));
        int start = Integer.parseInt(startIP.substring(startIP.lastIndexOf(".") + 1));
        int end = Integer.parseInt(endIP.substring(endIP.lastIndexOf(".") + 1));

        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{false, "Reachable IPs:"});
        for (int i = start; i <= end; i++) {
            String ip = subnet + "." + i;

            try {
                InetAddress address = InetAddress.getByName(ip);
                if (address.isReachable(1000)) {
                    tableModel.addRow(new Object[]{false, ip});
                    System.out.println(ip + " is reachable");
                } else {
                    System.out.println(ip + " is not reachable");
                }
            } catch (Exception err) {

                System.err.println("Exception: " + err.getMessage());
            }
        }}
    }

    private List<String> getSelectedIPs() {
        List<String> selectedIPs = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                selectedIPs.add((String) tableModel.getValueAt(i, 1));
            }
        }
        return selectedIPs;
    }

    private void sendMessages() {
        int serverPort = 5432;
          //manuel server adress yerini silip ip yerını ekledım
        for (int j = 0; j < tableModel.getRowCount(); j++) {
            boolean select = (boolean) tableModel.getValueAt(j, 0);
            if (select) {
                String ip = (String) tableModel.getValueAt(j, 1);
                String serverAddress = ip;
                sendMessage(serverAddress, serverPort, true); // veya false
            }
        }
    }

    public static void sendMessage(String serverAddress, int serverPort, boolean isOpen) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream outputStream = socket.getOutputStream()) {

            String message = String.format("{\"ope_type\":63,\"dataBoolean\":%s}", isOpen);

            Cipher cipher = Cipher.getInstance("AES");
            byte[] keyBytes = Constants.ENCRYPTION_PASSWORD.getBytes();
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);


            byte[] messageBytes = encryptedText.getBytes();
            outputStream.write(messageBytes);
            outputStream.flush();
            System.out.println("message sent");

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(ProjectForm::new);

    }
}
