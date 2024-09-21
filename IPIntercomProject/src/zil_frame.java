import com.google.gson.Gson;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

class BellFrame extends JFrame {

    private List<String> ipAddresses;

    private JPanel secondFramePanel;
    private JTextField textBoxName, textBoxIp, textBoxPort, textBoxMedia, textBoxUsername, textBoxPass, passwordText;
    private JLabel labelName, labelIp, labelPort, labelMedia, labelUsername, labelPass, tarihsaat, passlabel;
    private JButton addCamera, passbutton, delete, kapiac, tpsor, tpsorma,  zamanayarla;
    private JDatePickerImpl datePicker;
    private JSpinner spinner;

    public BellFrame(List<String> ipAddresses) {
        this.ipAddresses = ipAddresses;

        setSize(1000, 400);
        setResizable(false);
        secondFramePanel = new JPanel(new BorderLayout(30, 30));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        secondFramePanel.add(wrapperPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;


        wrapperPanel.add(Box.createVerticalStrut(50), gbc);


        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;


        JPanel leftPanel = new JPanel(new GridLayout(8, 2, 20, 10));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        labelName = new JLabel("                     Görünür İsim : ");
        leftPanel.add(labelName);
        textBoxName = new JTextField(15);
        leftPanel.add(textBoxName);

        labelIp = new JLabel("                                        IP :");
        leftPanel.add(labelIp);
        textBoxIp = new JTextField(15);
        leftPanel.add(textBoxIp);

        labelPort = new JLabel("                                     Port :");
        leftPanel.add(labelPort);
        textBoxPort = new JTextField(15);
        addHintToTextField(textBoxPort, "554");
        leftPanel.add(textBoxPort);

        labelMedia = new JLabel("                            Yayın Adı :");
        leftPanel.add(labelMedia);
        textBoxMedia = new JTextField(15);
        addHintToTextField(textBoxMedia, "media/video2");
        leftPanel.add(textBoxMedia);

        labelUsername = new JLabel("                       Kullanıcı Adı :");
        leftPanel.add(labelUsername);
        textBoxUsername = new JTextField(15);
        leftPanel.add(textBoxUsername);

        labelPass = new JLabel("                                     Şifre :");
        leftPanel.add(labelPass);
        textBoxPass = new JTextField(16);
        leftPanel.add(textBoxPass);

        addCamera = new JButton("Kamera Ekle");
        addCamera.setFont(new Font("Consolas", Font.BOLD, 14));
        addCamera.setBackground(Color.pink);

        wrapperPanel.add(leftPanel, gbc);

        JPanel rightPanel = new JPanel(new BorderLayout());
        secondFramePanel.add(rightPanel, BorderLayout.CENTER);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JPanel righttopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        rightPanel.add(righttopPanel, BorderLayout.NORTH);


        passlabel = new JLabel("Şifreyi Giriniz :");
       // passlabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        righttopPanel.add(passlabel);

        passwordText = new JTextField(15);
        righttopPanel.add(passwordText);

        passbutton = new JButton("Şifre Ekle");
        righttopPanel.add(passbutton);

        delete = new JButton("Şifre Sil");
        righttopPanel.add(delete);

        passbutton.setFont(new Font("Consolas", Font.BOLD, 14));
        passbutton.setBackground(Color.pink);
        delete.setFont(new Font("Consolas", Font.BOLD, 14));
        delete.setBackground(Color.pink);


        JPanel rightcenterPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(rightcenterPanel, BorderLayout.CENTER);


        JPanel datePanel = new JPanel(new GridBagLayout());
         gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;  // Center the components


        tarihsaat = new JLabel("Tarih Saat Ayarı");
        tarihsaat.setFont(new Font("Consolas", Font.BOLD, 20));
        tarihsaat.setOpaque(true);
        gbc.gridy = 0;
        datePanel.add(tarihsaat, gbc);


        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePickerPanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePickerPanel, new DateLabelFormatter());
        gbc.gridy = 1;
        datePanel.add(datePicker, gbc);


        spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        //TimeZone timeZone = TimeZone.getDefault();
        //timeEditor.getFormat().setTimeZone(timeZone);
        spinner.setEditor(timeEditor);
        spinner.setValue(new Date());
        gbc.gridy = 2;
        datePanel.add(spinner, gbc);

        gbc.gridy = 3;
        zamanayarla = new JButton("Zamanı Ayarla");
        zamanayarla.setFont(new Font("Consolas", Font.BOLD, 14));
        zamanayarla.setBackground(Color.pink);
        datePanel.add(zamanayarla, gbc);

        rightcenterPanel.add(datePanel, BorderLayout.CENTER);

        JPanel rightbottomPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        rightPanel.add(rightbottomPanel, BorderLayout.SOUTH);

        rightbottomPanel.add(kapiac = new JButton("Kapı Aç"));
        kapiac.setFont(new Font("Consolas", Font.BOLD, 14));
        kapiac.setBackground(Color.pink);

        tpsor = new JButton("TP Sor");
        tpsor.setFont(new Font("Consolas", Font.BOLD, 14));
        tpsor.setBackground(Color.pink);
        rightbottomPanel.add(tpsor);

        tpsorma = new JButton("TP Sorma");
        tpsorma.setFont(new Font("Consolas", Font.BOLD, 14));
        tpsorma.setBackground(Color.pink);
        rightbottomPanel.add(tpsorma);

        leftPanel.add(new JLabel(""));
        leftPanel.add(addCamera);

        delete.setVisible(true);

        tpsor.addActionListener(e -> tpAsk());
        tpsorma.addActionListener(e -> tpdAsk());
        addCamera.addActionListener(e -> add_camera());
        zamanayarla.addActionListener(e -> setdatetime());
        passbutton.addActionListener(e -> send_password());
        delete.addActionListener(e -> delete_password());
        kapiac.addActionListener(e -> open_door());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(secondFramePanel);
        setLocationRelativeTo(null);
        setVisible(true);
        ImageIcon image = new ImageIcon("netelsan.png");
        setIconImage(image.getImage().getScaledInstance(150, 120, Image.SCALE_DEFAULT));
    }



         private void addHintToTextField(JTextField textField, String hint) {
        textField.setText(hint);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(hint);
                }
            }
        });
    }

    private void setdatetime() {

        for (String serverIp : ipAddresses) {
        try {
            int serverPort = 5432;

            if (spinner == null ) {
                JOptionPane.showMessageDialog(this, "spinner  başlatılmamış.");

            }
            if(datePicker==null)
                {
                    JOptionPane.showMessageDialog(this,"datepicker  baslatılmamıs");

                }

            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir tarih seçin.");
                return;
            }

            Date selectedTime = (Date) spinner.getValue();
            if (selectedTime == null) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir saat seçin.");
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(selectedTime);

            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);

            Date dateTime2 = calendar.getTime();

            Calendar baseCalendar = Calendar.getInstance();
            baseCalendar.set(1970, Calendar.JANUARY, 1, 3, 0, 0);
            Date dateTime1 = baseCalendar.getTime();

            long timeDifference = dateTime2.getTime() - dateTime1.getTime();
            String timeMiliSecond = String.valueOf(timeDifference);

            String message = String.format("{\"ope_type\":8,\"dataLong\":%s}", timeMiliSecond);

            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream out = socket.getOutputStream()) {
                out.write(message.getBytes());
                out.flush();
                JOptionPane.showMessageDialog(this, "Mesaj gönderildi");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }}
    }
    private void delete_password() {
        String passwordk = passwordText.getText();
        if (!passwordk.matches("\\d+") || passwordk.length() != 4) {
            JOptionPane.showMessageDialog(
                    this, "Şifre sadece rakamlardan oluşmalıdır ve 4 haneli olmalıdır");
        } else {
            for (String serverIp : ipAddresses) {
                int serverPort = 5432;
                try (Socket socket = new Socket(serverIp, serverPort);
                     OutputStream outputStream = socket.getOutputStream()) {

                    Map<String, Object> passwordData = new HashMap<>();
                    passwordData.put("ope_type", 15);
                    passwordData.put("dataString", passwordk);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(passwordData);

                    byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(data);
                    outputStream.flush();
                    JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
           // JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");
        }
    }



    private void open_door() {
        for (String serverIp : ipAddresses) {
            int serverPort = 5432;

            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream outputStream = socket.getOutputStream()) {

                Map<String, Object> passwordData = new HashMap<>();
                passwordData.put("ope_type", 12);

                Gson gson = new Gson();
                String jsonString = gson.toJson(passwordData);

                byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(data, 0, data.length);
                outputStream.flush();

                JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
        //JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");
    }

    private void send_password() {
       String  passwordk = passwordText.getText();

        if ( !passwordk.matches("\\d+" ) || passwordk.length()!=4 ) {
            JOptionPane.showMessageDialog(this, "Şifre sadece rakamlardan oluşmalıdır ve 4 haneli olmalıdır");
        }
        else {
            for (String serverIp : ipAddresses) {
            int serverPort = 5432;
            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream outputStream = socket.getOutputStream()) {

                String password=passwordText.getText();

                Map<String, Object> passwordData = new HashMap<>();
                passwordData.put("ope_type", 14);
                passwordData.put("dataString",password);
                passwordData.put("dataBoolean", false);
                passwordData.put("rfid",false);
                Gson gson = new Gson();
                String jsonString = gson.toJson(passwordData);
                byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(data, 0, data.length);
                outputStream.flush();

                JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }}
        }
    }

    private void add_camera() {
        if (textBoxIp.getText().isEmpty() || textBoxMedia.getText().isEmpty() || textBoxName.getText().isEmpty() ||
                textBoxUsername.getText().isEmpty() || textBoxPass.getText().isEmpty() || textBoxPort.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Tüm Alanları Doldurun");
        } else {
            for (String serverIp : ipAddresses) {
            int serverPort = 5432;
            //"admin:123456@172.0.252.22",
            String ip = textBoxIp.getText();
            String name = textBoxName.getText();
            String media = textBoxMedia.getText();
            String port = textBoxPort.getText();
            String username = textBoxUsername.getText();
            String pass = textBoxPass.getText();

            try (Socket socket = new Socket(serverIp, serverPort);
                 OutputStream outputStream = socket.getOutputStream()) {

                Map<String, Object> ipCameraMap = new HashMap<>();
                ipCameraMap.put("ip", username + ":" + pass + "@" + ip);
                ipCameraMap.put("name", name);
                ipCameraMap.put("streamSuffix", media);
                ipCameraMap.put("port", port);
                ipCameraMap.put("id", "1");

                Map<String, Object> datas = new HashMap<>();
                datas.put("ope_type", 45);
                datas.put("ipCamera", ipCameraMap);
                datas.put("dataLong", 0);
                datas.put("isNeedGlobalBroadcast", false);
                datas.put("isNeedResponse", false);
                datas.put("dataInt", 0);
                datas.put("dataBoolean", false);

                Gson gson = new Gson();
                String jsonString = gson.toJson(datas);

                byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(data, 0, data.length);
                outputStream.flush();

                JOptionPane.showMessageDialog(this, "Mesaj gönderildi..");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }}
        }
    }


    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }

    private void technicalServiceSettings(boolean isOpen)
    {
        for (String serverIp : ipAddresses) {
        int serverPort = 5432;
        try (Socket socket = new Socket(serverIp, serverPort);
             OutputStream outputStream = socket.getOutputStream())
        {

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("ope_type", 63);
            dataMap.put("dataBoolean", isOpen);

            Gson gson = new Gson();
            String jsonString = gson.toJson(dataMap);

            byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);

            outputStream.write(data, 0, data.length);
            outputStream.flush();
            JOptionPane.showMessageDialog(this, "Mesaj gönderildi");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }}
    }
    private void tpAsk()
    {
        technicalServiceSettings(false);
    }

    private void tpdAsk()
    {
        technicalServiceSettings(true);
    }



}
