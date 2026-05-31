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
import java.sql.ResultSet;
import java.util.ArrayList;

public class Results_Form extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField textTeacherId;
    private JTextField textSurname;
    private JTextField textFirstName;
    private JTextField textAvailability;

    private ArrayList<Teacher> teachers = new ArrayList<>();
    private int currentIndex = 0;

    /**
     * This constructor is only useful for testing the form alone.
     */
    public Results_Form() {
        this("");
    }

    /**
     * Main constructor used by Introduction_Form.
     */
    public Results_Form(String lastName) {
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

        JPanel contentPane_1 = new JPanel();
        contentPane_1.setLayout(null);
        contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane_1.setBounds(0, 0, 436, 263);
        contentPane.add(contentPane_1);

        JLabel lblResults = new JLabel("Results");
        lblResults.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblResults.setBounds(180, 10, 70, 22);
        contentPane_1.add(lblResults);

        JLabel lblTeacherId = new JLabel("Teacher ID");
        lblTeacherId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTeacherId.setBounds(54, 34, 85, 22);
        contentPane_1.add(lblTeacherId);

        JLabel lblSurname = new JLabel("Surname");
        lblSurname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSurname.setBounds(54, 66, 85, 22);
        contentPane_1.add(lblSurname);

        JLabel lblFirstName = new JLabel("First Name");
        lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblFirstName.setBounds(54, 98, 85, 22);
        contentPane_1.add(lblFirstName);

        JLabel lblAvailability = new JLabel("Availability");
        lblAvailability.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblAvailability.setBounds(54, 135, 85, 22);
        contentPane_1.add(lblAvailability);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnUpdate.setBounds(21, 218, 100, 20);
        contentPane_1.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnDelete.setBounds(131, 218, 84, 20);
        contentPane_1.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.setBounds(325, 218, 84, 20);
        contentPane_1.add(btnBack);

        textTeacherId = new JTextField();
        textTeacherId.setColumns(10);
        textTeacherId.setBounds(166, 38, 222, 18);
        textTeacherId.setEditable(false);
        contentPane_1.add(textTeacherId);

        textSurname = new JTextField();
        textSurname.setColumns(10);
        textSurname.setBounds(166, 70, 222, 18);
        contentPane_1.add(textSurname);

        textFirstName = new JTextField();
        textFirstName.setColumns(10);
        textFirstName.setBounds(166, 102, 222, 18);
        contentPane_1.add(textFirstName);

        textAvailability = new JTextField();
        textAvailability.setColumns(10);
        textAvailability.setBounds(166, 139, 222, 18);
        contentPane_1.add(textAvailability);

        JButton btnPrevious = new JButton("Previous");
        btnPrevious.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnPrevious.setBounds(95, 180, 100, 20);
        contentPane_1.add(btnPrevious);

        JButton btnNext = new JButton("Next");
        btnNext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnNext.setBounds(247, 180, 84, 20);
        contentPane_1.add(btnNext);

        JButton btnCourses = new JButton("Courses");
        btnCourses.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCourses.setBounds(224, 218, 91, 20);
        contentPane_1.add(btnCourses);

        // Load teachers according to searched surname
        loadTeachers(lastName);
        displayCurrentTeacher();

        // Previous result
        btnPrevious.addActionListener(e -> {
            if (teachers.size() == 0) {
                return;
            }

            if (currentIndex > 0) {
                currentIndex--;
                displayCurrentTeacher();
            } else {
                JOptionPane.showMessageDialog(this, "This is the first result.");
            }
        });

        // Next result
        btnNext.addActionListener(e -> {
            if (teachers.size() == 0) {
                return;
            }

            if (currentIndex < teachers.size() - 1) {
                currentIndex++;
                displayCurrentTeacher();
            } else {
                JOptionPane.showMessageDialog(this, "This is the last result.");
            }
        });

        // Back to introduction form
        btnBack.addActionListener(e -> {
            Introduction_Form intro = new Introduction_Form();
            intro.setVisible(true);
            this.dispose();
        });

        btnUpdate.addActionListener(e -> {
            updateTeacher();
        });

        btnDelete.addActionListener(e -> {
            deleteTeacher();
        });
        
        btnCourses.addActionListener(e -> {
            if (teachers.size() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "There is no teacher selected.",
                    "Courses Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int teacherId = Integer.parseInt(textTeacherId.getText());

            Courses_Form coursesForm = new Courses_Form(teacherId);
            coursesForm.setVisible(true);
            this.dispose();
        });
    }

    private void loadTeachers(String lastName) {
        teachers.clear();

        if (lastName == null || lastName.trim().isEmpty()) {
            return;
        }

        String sql = "SELECT teacher_id, first_name, last_name, availability "
                   + "FROM teachers "
                   + "WHERE last_name LIKE ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "%" + lastName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("availability")
                    );

                    teachers.add(teacher);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while loading teacher results:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void displayCurrentTeacher() {
        if (teachers.size() == 0) {
            textTeacherId.setText("");
            textSurname.setText("");
            textFirstName.setText("");
            textAvailability.setText("");

            JOptionPane.showMessageDialog(
                this,
                "No teachers found.",
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Teacher teacher = teachers.get(currentIndex);

        textTeacherId.setText(String.valueOf(teacher.getTeacherId()));
        textSurname.setText(teacher.getLastName());
        textFirstName.setText(teacher.getFirstName());
        textAvailability.setText(String.valueOf(teacher.getAvailability()));
    }

    private void updateTeacher() {
        if (teachers.size() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "There is no teacher to update.",
                "Update Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

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

        int teacherId = Integer.parseInt(textTeacherId.getText());

        String sql = "UPDATE teachers "
                   + "SET first_name = ?, last_name = ?, availability = ? "
                   + "WHERE teacher_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, availability);
            stmt.setInt(4, teacherId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                Teacher currentTeacher = teachers.get(currentIndex);
                currentTeacher.setFirstName(firstName);
                currentTeacher.setLastName(lastName);
                currentTeacher.setAvailability(availability);

                JOptionPane.showMessageDialog(
                    this,
                    "Teacher updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No teacher was updated.",
                    "Update Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while updating teacher:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void deleteTeacher() {
        if (teachers.size() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "There is no teacher to delete.",
                "Delete Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this teacher?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int teacherId = Integer.parseInt(textTeacherId.getText());

        String sql = "DELETE FROM teachers WHERE teacher_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, teacherId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                teachers.remove(currentIndex);

                JOptionPane.showMessageDialog(
                    this,
                    "Teacher deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                if (teachers.size() == 0) {
                    textTeacherId.setText("");
                    textSurname.setText("");
                    textFirstName.setText("");
                    textAvailability.setText("");

                    Introduction_Form intro = new Introduction_Form();
                    intro.setVisible(true);
                    this.dispose();
                } else {
                    if (currentIndex >= teachers.size()) {
                        currentIndex = teachers.size() - 1;
                    }

                    displayCurrentTeacher();
                }

            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No teacher was deleted.",
                    "Delete Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while deleting teacher:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Simple internal Teacher class for storing search results.
     */
    private static class Teacher {
        private int teacherId;
        private String firstName;
        private String lastName;
        private int availability;

        public Teacher(int teacherId, String firstName, String lastName, int availability) {
            this.teacherId = teacherId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.availability = availability;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setAvailability(int availability) {
            this.availability = availability;
        }
        public int getTeacherId() {
            return teacherId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAvailability() {
            return availability;
        }
    }

    /**
     * Launch the application for testing only.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Results_Form frame = new Results_Form("Papadopoulos");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}