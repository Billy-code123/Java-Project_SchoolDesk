package Assignment_2026;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Courses_Form extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private int teacherId;
    private String teacherName;
    private int availability;
    private int assignedHours;

    private JLabel lblTeacherInfo;
    private JLabel lblHoursInfo;

    private JList<Course> listAssignedCourses;
    private DefaultListModel<Course> assignedCoursesModel;

    private JComboBox<Course> comboCourses;
    private DefaultComboBoxModel<Course> coursesComboModel;

    public Courses_Form(int teacherId) {
        this.teacherId = teacherId;

        setIconImage(Toolkit.getDefaultToolkit().getImage(
            "C:\\Users\\billy\\OneDrive\\Υπολογιστής\\BSc Computing\\1st Year\\2nd Semester\\Application Development\\photos\\logo.png"
        ));

        setTitle("SchoolDesk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 420);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Teacher Courses");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(215, 10, 180, 25);
        contentPane.add(lblTitle);

        lblTeacherInfo = new JLabel("Teacher:");
        lblTeacherInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblTeacherInfo.setBounds(30, 50, 520, 25);
        contentPane.add(lblTeacherInfo);

        lblHoursInfo = new JLabel("Hours:");
        lblHoursInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblHoursInfo.setBounds(30, 80, 520, 25);
        contentPane.add(lblHoursInfo);

        JLabel lblAssigned = new JLabel("Assigned Courses");
        lblAssigned.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblAssigned.setBounds(30, 120, 180, 25);
        contentPane.add(lblAssigned);

        assignedCoursesModel = new DefaultListModel<>();
        listAssignedCourses = new JList<>(assignedCoursesModel);

        JScrollPane scrollPane = new JScrollPane(listAssignedCourses);
        scrollPane.setBounds(30, 150, 250, 140);
        contentPane.add(scrollPane);

        JLabel lblAvailableCourses = new JLabel("Select Course");
        lblAvailableCourses.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblAvailableCourses.setBounds(320, 120, 180, 25);
        contentPane.add(lblAvailableCourses);

        coursesComboModel = new DefaultComboBoxModel<>();
        comboCourses = new JComboBox<>(coursesComboModel);
        comboCourses.setBounds(320, 150, 230, 25);
        contentPane.add(comboCourses);

        JButton btnAssign = new JButton("Assign Course");
        btnAssign.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnAssign.setBounds(320, 195, 160, 25);
        contentPane.add(btnAssign);

        JButton btnRemove = new JButton("Remove Selected");
        btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnRemove.setBounds(30, 305, 170, 25);
        contentPane.add(btnRemove);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnRefresh.setBounds(220, 305, 120, 25);
        contentPane.add(btnRefresh);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnBack.setBounds(430, 335, 120, 25);
        contentPane.add(btnBack);

        loadData();

        btnAssign.addActionListener(e -> {
            assignCourse();
        });

        btnRemove.addActionListener(e -> {
            removeCourse();
        });

        btnRefresh.addActionListener(e -> {
            loadData();
        });

        btnBack.addActionListener(e -> {
            Introduction_Form intro = new Introduction_Form();
            intro.setVisible(true);
            this.dispose();
        });
    }

    private void loadData() {
        loadTeacherInfo();
        loadAssignedCourses();
        loadAllCourses();

        lblTeacherInfo.setText("Teacher: " + teacherName + " (ID: " + teacherId + ")");
        lblHoursInfo.setText("Assigned Hours: " + assignedHours + " / Availability: " + availability);
    }

    private void loadTeacherInfo() {
        String sql = "SELECT first_name, last_name, availability FROM teachers WHERE teacher_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    teacherName = rs.getString("first_name") + " " + rs.getString("last_name");
                    availability = rs.getInt("availability");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while loading teacher information:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAssignedCourses() {
        assignedCoursesModel.clear();
        assignedHours = 0;

        String sql = "SELECT c_id, title, semester, hours, teacher_id "
                   + "FROM courses "
                   + "WHERE teacher_id = ? "
                   + "ORDER BY semester, title";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getInt("c_id"),
                        rs.getString("title"),
                        rs.getInt("semester"),
                        rs.getInt("hours"),
                        rs.getObject("teacher_id") == null ? null : rs.getInt("teacher_id")
                    );

                    assignedCoursesModel.addElement(course);
                    assignedHours += course.getHours();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while loading assigned courses:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAllCourses() {
        coursesComboModel.removeAllElements();

        String sql = "SELECT c_id, title, semester, hours, teacher_id "
                   + "FROM courses "
                   + "ORDER BY semester, title";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("c_id"),
                    rs.getString("title"),
                    rs.getInt("semester"),
                    rs.getInt("hours"),
                    rs.getObject("teacher_id") == null ? null : rs.getInt("teacher_id")
                );

                coursesComboModel.addElement(course);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while loading courses:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void assignCourse() {
        Course selectedCourse = (Course) comboCourses.getSelectedItem();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a course.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (selectedCourse.getTeacherId() != null && selectedCourse.getTeacherId() == teacherId) {
            JOptionPane.showMessageDialog(
                this,
                "This course is already assigned to this teacher.",
                "Assignment Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (assignedHours + selectedCourse.getHours() > availability) {
            JOptionPane.showMessageDialog(
                this,
                "The teacher does not have enough available hours for this course.",
                "Assignment Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (selectedCourse.getTeacherId() != null && selectedCourse.getTeacherId() != teacherId) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "This course is already assigned to another teacher.\nDo you want to reassign it?",
                "Confirm Reassignment",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        String sql = "UPDATE courses SET teacher_id = ? WHERE c_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, teacherId);
            stmt.setInt(2, selectedCourse.getCourseId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Course assigned successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Course was not assigned.",
                    "Assignment Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while assigning course:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removeCourse() {
        Course selectedCourse = listAssignedCourses.getSelectedValue();

        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an assigned course to remove.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this course from the teacher?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE courses SET teacher_id = NULL WHERE c_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, selectedCourse.getCourseId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Course removed successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Course was not removed.",
                    "Remove Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error while removing course:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class Course {
        private int courseId;
        private String title;
        private int semester;
        private int hours;
        private Integer teacherId;

        public Course(int courseId, String title, int semester, int hours, Integer teacherId) {
            this.courseId = courseId;
            this.title = title;
            this.semester = semester;
            this.hours = hours;
            this.teacherId = teacherId;
        }

        public int getCourseId() {
            return courseId;
        }

        public int getHours() {
            return hours;
        }

        public Integer getTeacherId() {
            return teacherId;
        }

        @Override
        public String toString() {
            String status = teacherId == null ? "Unassigned" : "Assigned";
            return title + " | Sem. " + semester + " | " + hours + "h | " + status;
        }
    }
}