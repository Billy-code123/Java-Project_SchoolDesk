package Assignment_2026;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

public class Introduction_Form extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textLastname;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Introduction_Form frame = new Introduction_Form();
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
	public Introduction_Form() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\billy\\OneDrive\\Υπολογιστής\\BSc Computing\\1st Year\\2nd Semester\\Application Development\\photos\\logo.png"));
		setTitle("SchoolDesk");
		setBackground(new Color(255, 255, 255));
		setFont(new Font("Dialog", Font.BOLD, 18));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(new Color(0, 0, 0));
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearch.setBounds(265, 140, 134, 20);
		contentPane.add(btnSearch);
		
		JLabel lblNewLabel = new JLabel("Teacher Search");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(148, 83, 134, 20);
		contentPane.add(lblNewLabel);
		
		textLastname = new JTextField();
		textLastname.setBounds(87, 143, 142, 18);
		contentPane.add(textLastname);
		textLastname.setColumns(10);
		
		JButton btnAddNewTeacher = new JButton("Add New Teacher");
		btnAddNewTeacher.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAddNewTeacher.setBounds(110, 219, 237, 20);
		contentPane.add(btnAddNewTeacher);
		
		JLabel lblNewLabel_1 = new JLabel("Welcome to SchoolDesk");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1.setBounds(93, 10, 254, 29);
		contentPane.add(lblNewLabel_1);
		
		btnSearch.addActionListener(e -> {
		    String lastName = textLastname.getText().trim();

		    if (lastName.isEmpty()) {
		        JOptionPane.showMessageDialog(
		            this,
		            "Please enter a teacher last name.",
		            "Input Error",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		    }

		    Results_Form resultsForm = new Results_Form(lastName);
		    resultsForm.setVisible(true);
		    this.dispose();
		});

		btnAddNewTeacher.addActionListener(e -> {
		    Add_new_teacher addTeacherForm = new Add_new_teacher();
		    addTeacherForm.setVisible(true);
		    this.dispose();
		});

	}
}
