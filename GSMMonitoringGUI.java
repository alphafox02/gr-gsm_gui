import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GSMMonitoringGUI {
    private JFrame window;
    private JComboBox<String> deviceDropdown;
    private JComboBox<String> bandDropdown;
    private JTextField freqField;

    public GSMMonitoringGUI() {
        window = new JFrame("GSM Monitoring GUI");
        window.setSize(500, 400);

        // Dropdown for GSM Devices
        JLabel deviceLabel = new JLabel("Select GSM Device:");
        deviceLabel.setBounds(10, 10, 120, 20);
        window.add(deviceLabel);

        deviceDropdown = new JComboBox<>(getDevices());
        deviceDropdown.setBounds(140, 10, 120, 20);
        window.add(deviceDropdown);

        // Dropdown for GSM Bands
        JLabel bandLabel = new JLabel("Select GSM Band:");
        bandLabel.setBounds(10, 40, 120, 20);
        window.add(bandLabel);

        String[] bandList = {"GSM900", "DCS1800", "GSM850", "PCS1900", "GSM450", "GSM480", "GSM-R"};
        bandDropdown = new JComboBox<>(bandList);
        bandDropdown.setBounds(140, 40, 120, 20);
        window.add(bandDropdown);

        // Textbox for Frequency
        JLabel freqLabel = new JLabel("Enter Frequency:");
        freqLabel.setBounds(10, 70, 120, 20);
        window.add(freqLabel);

        freqField = new JTextField();
        freqField.setBounds(140, 70, 120, 20);
        window.add(freqField);

        // Buttons
        JButton scanButton = new JButton("Scan");
        scanButton.setBounds(10, 100, 70, 20);
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gsmScan(deviceDropdown.getSelectedItem().toString(), bandDropdown.getSelectedItem().toString());
            }
        });
        window.add(scanButton);

        JButton livemonButton = new JButton("Livemon");
        livemonButton.setBounds(90, 100, 70, 20);
        livemonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                livemon(deviceDropdown.getSelectedItem().toString(), freqField.getText());
            }
        });
        window.add(livemonButton);

        JButton imsiCatcherButton = new JButton("IMSI Catcher");
        imsiCatcherButton.setBounds(170, 100, 120, 20);
        imsiCatcherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imsiCatcher();
            }
        });
        window.add(imsiCatcherButton);

        // Button to refresh device list
        JButton refreshButton = new JButton("Refresh Device List");
        refreshButton.setBounds(10, 130, 150, 20);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deviceDropdown.setModel(new DefaultComboBoxModel<>(getDevices()));
            }
        });
        window.add(refreshButton);
    }

    public static void main(String[] args) {
        GSMMonitoringGUI gui = new GSMMonitoringGUI();
        gui.window.setLayout(null);
        gui.window.setVisible(true);
    }

    // Function to get list of devices
    private String[] getDevices() {
        String[] deviceList = new String[0];

        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"grgsm_scanner", "-l"});

            Scanner scanner = new Scanner(proc.getInputStream());
            ArrayList<String> devices = new ArrayList<>();

            while (scanner.hasNextLine()) {
                devices.add(scanner.nextLine().split(",")[0]);
            }

            deviceList = devices.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceList;
    }

    // Function to start GSM Scan
    private void gsmScan(String device, String band) {
        try {
            Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e",
                    "bash -c \"grgsm_scanner --args " + device + " -b " + band + " -v -d; bash\""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to start livemon
    private void livemon(String device, String freq) {
        try {
            Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e",
                    "bash -c \"grgsm_livemon --args " + device + " -f " + freq + "; bash\""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to start IMSI Catcher
    private void imsiCatcher() {
        try {
            Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e",
                    "bash -c \"cd /usr/src/IMSI-catcher/; sudo python3 simple_IMSI-catcher.py --sniff; bash\""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
