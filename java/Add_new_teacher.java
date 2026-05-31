package Assignment_2026;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Add_new_teacher extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField textTeacherId;
    private JTextField textSurname;
    private JTextField textFirstName;
    private JTextField textAvailability;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Add_new_teacher frame = new Add_new_teacher();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Add_new_teacher() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(
            "C:\\Users\\billy\\OneDrive\\Υπολογιστής\\BSc Computing\\1st Year\\2nd Semester\\Application Development\\photos\\logo.png"
        ));

        setTitle("SchoolDesk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Add New Teacher");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(131, 10, 183, 22);
        contentPane.add(lblTitle);

        JLabel lblTeacherId = new JLabel("Teacher ID");
        lblTeacherId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTeacherId.setBounds(54, 50, 85, 22);
        contentPane.add(lblTeacherId);

        JLabel lblSurname = new JLabel("Surname");
        lblSurname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSurname.setBounds(54, 92, 85, 22);
        contentPane.add(lblSurname);

        JLabel lblFirstName = new JLabel("First Name");
        lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblFirstName.setBounds(54, 135, 85, 22);
        contentPane.add(lblFirstName);

        JLabel lblAvailability = new JLabel("Availability");
        lblAvailability.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblAvailability.setBounds(54, 179, 85, 22);
        contentPane.add(lblAvailability);

        JButton btnSave = new JButton("Save");
        btnSave.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSave.setBounds(39, 218, 84, 20);
        contentPane.add(btnSave);

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnClear.setBounds(176, 218, 84, 20);
        contentPane.add(btnClear);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.setBounds(304, 218, 84, 20);
        contentPane.add(btnBack);

        textTeacherId = new JTextField();
        textTeacherId.setBounds(166, 54, 222, 18);
        textTeacherId.setColumns(10);
        textTeacherId.setEditable(false);
        textTeacherId.setText("Auto generated");
        contentPane.add(textTeacherId);

        textSurname = new JTextField();
        textSurname.setBounds(166, 96, 222, 18);
        textSurname.setColumns(10);
        contentPane.add(textSurname);

        textFirstName = new JTextField();
        textFirstName.setBounds(166, 139, 222, 18);
        textFirstName.setColumns(10);
        contentPane.add(textFirstName);

        textAvailability = new JTextField();
        textAvailability.setBounds(166, 183, 222, 18);
        textAvailability.setColumns(10);
        contentPane.add(textAvailability);

        btnSave.addActionListener(e -> {
            saveTeacher();
        });

        btnClear.addActionListener(e -> {
            clearFields();
        });

        btnBack.addActionListener(e -> {
            Introduction_Form intro = new Introduction_Form();
            intro.setVisible(true);
            this.dispose();
        });
    }

    private void saveTeacher() {
        String firstName = textFirstName.getText().trim();
        String lastName = textSurname.getText().trim();
        String availabilityText = textAvailability.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || availabilityText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int availability;

        try {
            availability = Integer.parseInt(availabilityText);

            if (availability <= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Availability must be a positive number.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Availability must be a valid number.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "INSERT INTO teachers (first_name, last_name, availability) "
                   + "VALUES (?, ?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, availability);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Teacher added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Teacher was not added.",
                    "Insert Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while adding teacher:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {
        textSurname.setText("");
        textFirstName.setText("");
        textAvailability.setText("");
        textTeacherId.setText("Auto generated");
    }
}