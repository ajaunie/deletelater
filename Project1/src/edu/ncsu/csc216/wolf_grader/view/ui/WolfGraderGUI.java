package edu.ncsu.csc216.wolf_grader.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.manager.WolfGrader;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Container for the WolfGrader system that has the menu options for new assignment 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
 */
public class WolfGraderGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Wolf Grader";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the add assignment menu item. */
	private static final String ADD_TITLE = "Add Assignment";
	/** Text for the Load menu item. */
	private static final String LOAD_TITLE = "Load Assignments File";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save Assignments";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new assignment. */
	private JMenuItem itemAddAssignment;
	/** Menu item for loading a file containing one or more assignments and their submissions. */
	private JMenuItem itemLoadAssignment;
	/** Menu item for saving the assignments and their submissions. */
	private JMenuItem itemSaveAssignment;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the submission. */
	private JPanel panel;
	/** Constant to identify AssignmentPanel for CardLayout. */
	private static final String ASSIGNMENT_LIST_PANEL = "AssignmentPanel";
	/** Constant to identify SubmittedPanel for CardLayout. */
	private static final String UPLOAD_PANEL = "UploadPanel";
	/** Constant to identify SubmittedPanel for CardLayout. */
	private static final String FEEDBACK_PANEL = "FeedbackPanel";
	/** Constant to identify SubmittedPanel for CardLayout. */
	private static final String SUBMITTED_PANEL = "SubmittedPanel";
	/** Constant to identify RegisteringPanel for CardLayout. */
	private static final String GRADE_PANEL = "GradePanel";
	/** Constant to identify ReviewingPanel for CardLayout. */
	private static final String CHECK_PANEL = "CheckPanel";
	/** Constant to identify RevisingPanel for CardLayout. */
	private static final String REGRADE_PANEL = "RegradePanel";
	/** Constant to identify ClosedPanel for CardLayout. */
	private static final String RETURN_PANEL = "ReturnPanel";
	/** Constant to identify AddPaperPanel for CardLayout. */
	private static final String ADD_SUBMISSION_PANEL = "AddSubmission";
	/** Constant to identify AddAssignmentPanel for CardLayout. */
	private static final String ADD_ASSIGNMENT_PANEL = "AddAssignment";
	
	/** Assignment panel - we only need one instance, so it's final. */
	private final AssignmentListPanel pnlAssignmentList = new AssignmentListPanel();
	/** Upload panel - we only need one instance, so it's final. */
	private final UploadPanel pnlUpload = new UploadPanel();
	/** Feedback panel - we only need one instance, so it's final. */
	private final FeedbackPanel pnlFeedback = new FeedbackPanel();
	/** Submitted panel - we only need one instance, so it's final. */
	private final SubmittedPanel pnlSubmitted = new SubmittedPanel();
	/** Grade panel - we only need one instance, so it's final. */
	private final GradePanel pnlGrade = new GradePanel();
	/** Check panel - we only need one instance, so it's final. */
	private final CheckPanel pnlCheck = new CheckPanel();
	/** Regrade panel - we only need one instance, so it's final. */
	private final RegradePanel pnlRegrade = new RegradePanel();
	/** Return panel - we only need one instance, so it's final. */
	private final ReturnPanel pnlReturn = new ReturnPanel();

	/** Add Submission panel - we only need one instance, so it's final. */
	private final AddSubmissionPanel pnlAddSubmission = new AddSubmissionPanel();
	/** Add Assignment panel - we only need one instance, so it's final. */
	private final AddAssignmentPanel pnlAddAssignment = new AddAssignmentPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a WolfGraderGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the WolfGrader system.
	 */
	public WolfGraderGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlAssignmentList, ASSIGNMENT_LIST_PANEL);
		panel.add(pnlUpload, UPLOAD_PANEL);
		panel.add(pnlFeedback, FEEDBACK_PANEL);
		panel.add(pnlSubmitted, SUBMITTED_PANEL);
		panel.add(pnlGrade, GRADE_PANEL);
		panel.add(pnlCheck, CHECK_PANEL);
		panel.add(pnlRegrade, REGRADE_PANEL);
		panel.add(pnlReturn, RETURN_PANEL);
		panel.add(pnlAddSubmission, ADD_SUBMISSION_PANEL);
		panel.add(pnlAddAssignment, ADD_ASSIGNMENT_PANEL);
		cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Add window listener to save when closing
		addWindowListener(new WindowAdapter() {

			/**
			 * Ask the user to save the Assignments file when closing GUI.
			 * @param e WindowEvent leading to GUI closing
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				WolfGrader model = WolfGrader.getInstance();
				try {
					model.saveAssignmentsToFile(getFileName(false));
				} catch (IllegalArgumentException exp) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, exp.getMessage());
				} catch (IllegalStateException exp) {
					//Don't do anything - user canceled (or error)
				}
			}
			
		});
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading/saving an Assignment
	 * file containing submissions or for quitting the submission.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemAddAssignment = new JMenuItem(ADD_TITLE);
		itemLoadAssignment = new JMenuItem(LOAD_TITLE);
		itemSaveAssignment = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemAddAssignment.addActionListener(this);
		itemLoadAssignment.addActionListener(this);
		itemSaveAssignment.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveAssignment.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemAddAssignment);
		menu.add(itemLoadAssignment);
		menu.add(itemSaveAssignment);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Use singleton to create/get the sole instance.
		WolfGrader model = WolfGrader.getInstance();
		if (e.getSource() == itemAddAssignment) {
			//Create a new Assignment
			try {
				cardLayout.show(panel, ADD_ASSIGNMENT_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			}
		} else if (e.getSource() == itemLoadAssignment) {
			//Load an existing assignment list
			try {
				model.loadAssignmentsFromFile(getFileName(true));
				itemSaveAssignment.setEnabled(true);
				pnlAssignmentList.updateAssignment();
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveAssignment) {
			//Save Assignments and submissions
			try {
				model.saveAssignmentsToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveAssignmentsToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the WolfGrader submission.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new WolfGraderGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the Assignment and it's list of submissions
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AssignmentListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label for selecting active Assignment */
		private JLabel lblActiveAssignment;
		/** Combo box for Assignment list */
		private JComboBox<String> comboAssignmentList;
		/** Button to update to the select Assignment */
		private JButton btnUpdateAssignment;
		
		/** Button for creating a new submission */
		private JButton btnAdd;
		/** Button for deleting the selected submission in the list */
		private JButton btnDelete;
		/** Button for editing the selected submission in the list */
		private JButton btnEdit;
		
		/** Label for filter by state */
		private JLabel lblFilterState;
		/** Combo box for states */
		private JComboBox<String> comboFilterState;
		/** Button to update to filter by state */
		private JButton btnFilterState;
		
		/** JTable for displaying the list of submissions */
		private JTable tableSubmissions;
		/** TableModel for submissions */
		private SubmissionTableModel tableModel;
		
		/**
		 * Creates the Assignment panel with submission list.
		 */
		public AssignmentListPanel() {
			super(new BorderLayout());
			
			//Set up JPanel for Assignments
			lblActiveAssignment = new JLabel("Assignments");
			comboAssignmentList = new JComboBox<String>();
			btnUpdateAssignment = new JButton("Select Assignment");
			btnUpdateAssignment.addActionListener(this);
						
			//Set up the JPanel that will hold action buttons		
			btnAdd = new JButton("Add Submission");
			btnAdd.addActionListener(this);
			btnDelete = new JButton("Delete Submission");
			btnDelete.addActionListener(this);
			btnEdit = new JButton("Edit Submission");
			btnEdit.addActionListener(this);
			
			//Set up JPanel for filter
			lblFilterState = new JLabel("Filter by State");
			comboFilterState = new JComboBox<String>();
			btnFilterState = new JButton("Filter");
			btnFilterState.addActionListener(this);
			
			comboFilterState.addItem("All");
			comboFilterState.addItem(Submission.UPLOAD_NAME);
			comboFilterState.addItem(Submission.FEEDBACK_NAME);
			comboFilterState.addItem(Submission.SUBMITTED_NAME);
			comboFilterState.addItem(Submission.GRADE_NAME);
			comboFilterState.addItem(Submission.CHECK_NAME);
			comboFilterState.addItem(Submission.REGRADE_NAME);			
			comboFilterState.addItem(Submission.RETURN_NAME);
						
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(3, 3));
			pnlActions.add(lblActiveAssignment);
			pnlActions.add(comboAssignmentList);
			pnlActions.add(btnUpdateAssignment);
			pnlActions.add(btnAdd);
			pnlActions.add(btnDelete);
			pnlActions.add(btnEdit);
			pnlActions.add(lblFilterState);
			pnlActions.add(comboFilterState);
			pnlActions.add(btnFilterState);
						
			//Set up table
			tableModel = new SubmissionTableModel();
			tableSubmissions = new JTable(tableModel);
			tableSubmissions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableSubmissions.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableSubmissions.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(tableSubmissions);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
			
			updateAssignment();
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnUpdateAssignment) {
				int idx = comboAssignmentList.getSelectedIndex();
				
				if (idx == -1) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No Assignment selected");
				} else {				
					String assignmentName = comboAssignmentList.getItemAt(idx);
					WolfGrader.getInstance().loadAssignment(assignmentName);
				}
				updateAssignment();
			} else if (e.getSource() == btnFilterState) {
				int idx = comboFilterState.getSelectedIndex();
				
				if (idx == -1) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No state selected");
				} else {				
					String stateName = comboFilterState.getItemAt(idx);
					tableModel.updateData(stateName);
				}
			} else if (e.getSource() == btnAdd) {
				//If the add button is clicked switch to the AddPaperPanel
				cardLayout.show(panel,  ADD_SUBMISSION_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is clicked, delete the submission
				int row = tableSubmissions.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No submission selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						WolfGrader.getInstance().deleteSubmissionById(id);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid id");
					}
				}
				updateAssignment();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is clicked, switch panel based on state
				int row = tableSubmissions.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No submission selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						String stateName = WolfGrader.getInstance().getSubmissionById(id).getState();
						if (stateName.equals(Submission.UPLOAD_NAME)) {
							cardLayout.show(panel, UPLOAD_PANEL);
							pnlUpload.setInfo(id);
						}
						if (stateName.equals(Submission.FEEDBACK_NAME)) {
							cardLayout.show(panel, FEEDBACK_PANEL);
							pnlFeedback.setInfo(id);
						}
						if (stateName.equals(Submission.SUBMITTED_NAME)) {
							cardLayout.show(panel, SUBMITTED_PANEL);
							pnlSubmitted.setInfo(id);
						}
						if (stateName.equals(Submission.GRADE_NAME)) {
							cardLayout.show(panel, GRADE_PANEL);
							pnlGrade.setInfo(id);
						}
						if (stateName.equals(Submission.CHECK_NAME)) {
							cardLayout.show(panel, CHECK_PANEL);
							pnlCheck.setInfo(id);
						}  
						if (stateName.equals(Submission.REGRADE_NAME)) {
							cardLayout.show(panel, REGRADE_PANEL);
							pnlRegrade.setInfo(id);
						} 						
						if (stateName.equals(Submission.RETURN_NAME)) {
							cardLayout.show(panel, RETURN_PANEL);
							pnlReturn.setInfo(id);
						} 

						
					} catch (NumberFormatException | NullPointerException  nfe) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid id");
					} 
				}
			} 
			WolfGraderGUI.this.repaint();
			WolfGraderGUI.this.validate();
		}
		
		/**
		 * Update the assignment information. 
		 */
		public void updateAssignment() {
			tableModel.updateData("All");
			
			String assignmentName = WolfGrader.getInstance().getActiveAssignmentName();
			
			if (assignmentName == null) {
				assignmentName = "Create an Assignment";
				btnAdd.setEnabled(false);
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				btnUpdateAssignment.setEnabled(false);
				btnFilterState.setEnabled(false);
			} else {
				btnAdd.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
				btnUpdateAssignment.setEnabled(true);
				btnFilterState.setEnabled(true);
			}
			
			comboAssignmentList.removeAllItems();
			String [] assignmentList = WolfGrader.getInstance().getAssignmentList();
			for (int i = 0; i < assignmentList.length; i++) {
				comboAssignmentList.addItem(assignmentList[i]);
			}
			comboAssignmentList.setSelectedItem(WolfGrader.getInstance().getActiveAssignmentName());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Assignment: " + assignmentName);
			setBorder(border);
			setToolTipText("Assignment: " + assignmentName);
		}
		
		/**
		 * PaperTableModel is the object underlying the JTable object that displays
		 * the list of submissions to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
		 */
		private class SubmissionTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"ID", "State", "Name(UnityId)", "PlagiarismCheck"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the SubmissionTableModel by requesting the latest information
			 * from the SubmissionTableModel.
			 */
			public SubmissionTableModel() {
				updateData("All");
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			@Override
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			@Override
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			@Override
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with submission information from the WolfGrader system.
			 * @param stateName name of state to filter on. 
			 */
			private void updateData(String stateName) {
				WolfGrader m = WolfGrader.getInstance();
				data = m.getSubmissionsAsArray(stateName);
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a uploaded submission.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class UploadPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Label - grader */
		private JLabel lblGrader;
		/** Text Field - grader */
		private JTextField txtGrader;
		/** Button - Assign w/ grader */
		private JButton btnAssign;
		
		/** Button - Re-upload  */
		private JButton btnReUpload;
		/** Button - Submit */
		private JButton btnSubmit;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a submission in the SubmittedState
		 */
		public UploadPanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			lblGrader = new JLabel("Grader Id:");
			txtGrader = new JTextField(25);
			btnAssign = new JButton("Assign");
				
			btnReUpload = new JButton("ReUpload");
			btnSubmit = new JButton("Submit");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnSubmit.addActionListener(this);
			btnReUpload.addActionListener(this);
			btnCancel.addActionListener(this);
						
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAssign = new JPanel();
			pnlAssign.setLayout(new GridLayout(1, 3));
			pnlAssign.add(lblGrader);
			pnlAssign.add(txtGrader);
			pnlAssign.add(btnAssign);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnReUpload);
			pnlBtnRow.add(btnSubmit);
			pnlBtnRow.add(btnCancel);
			
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlAssign, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
					
			add(pnlInfo, BorderLayout.NORTH);
					
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the SubmissionInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {				
				String grader = txtGrader.getText();
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, grader);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnReUpload) {				
				
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.REUPLOAD, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnSubmit) {
				try {
					Command c = new Command(Command.CommandValue.MOVE, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			
			if (reset) {
				//All buttons lead back to Assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
				txtGrader.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a feedback submission.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class FeedbackPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Button - Submit */
		private JButton btnSubmit;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a submission in the SubmittedState
		 */
		public FeedbackPanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			btnSubmit = new JButton("Submit");
			btnCancel = new JButton("Cancel");
			
			btnCancel.addActionListener(this);
			btnSubmit.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnSubmit);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
					
			add(pnlInfo, BorderLayout.NORTH);
					
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the SubmissionInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnSubmit) {
				try {
					Command c = new Command(Command.CommandValue.PROVIDE, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead back to Assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}

	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a submitted submission.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class SubmittedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Label - grader */
		private JLabel lblGrader;
		/** Text Field - grader */
		private JTextField txtGrader;
		/** Button - Assign w/ grader */
		private JButton btnAssign;		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a submission in the SubmittedState
		 */
		public SubmittedPanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			lblGrader = new JLabel("Grader Id:");
			txtGrader = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			btnCancel = new JButton("Cancel");

			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAssign = new JPanel();
			pnlAssign.setLayout(new GridLayout(1, 3));
			pnlAssign.add(lblGrader);
			pnlAssign.add(txtGrader);
			pnlAssign.add(btnAssign);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnCancel);
			
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlAssign, c);
				
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
					
			add(pnlInfo, BorderLayout.NORTH);
					
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the SubmissionInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {				
				String grader = txtGrader.getText();
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, grader);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to Assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
				txtGrader.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an submission in the grade state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class GradePanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Button - Submit*/
		private JButton btnSubmit;
		/** Button - Analyze*/
		private JButton btnAnalyze;
		
		/** Label - grade */
		private JLabel lblGrade;
		/** ComboBox - grade */
		private JComboBox<String> comboGrade;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an submission in the GradeState
		 */
		public GradePanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			btnSubmit = new JButton("Submit");
			btnAnalyze = new JButton("Analyze");
			btnCancel = new JButton("Cancel");
			
			btnSubmit.addActionListener(this);
			btnAnalyze.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			
			lblGrade = new JLabel("Grade:");
			comboGrade = new JComboBox<String>();
			comboGrade.addItem("A");
			comboGrade.addItem("B");
			comboGrade.addItem("C");
			comboGrade.addItem("F");
			comboGrade.addItem("F-AIV");
			comboGrade.addItem("IN");

			JPanel pnlGrades = new JPanel();
			pnlGrades.setLayout(new GridLayout(1, 2));
			pnlGrades.add(lblGrade);
			pnlGrades.add(comboGrade);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnAnalyze);
			pnlBtnRow.add(btnSubmit);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlGrades, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the SubmissionInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnSubmit) {
				int idx = comboGrade.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No grade selected");
				} else {				
					String grade = comboGrade.getItemAt(idx);
					//Try a command.  If problem, go back to submission list.
					
					try {
						Command c = new Command(Command.CommandValue.SUBMIT, grade);
						WolfGrader.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnAnalyze) {
				int idx = comboGrade.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No grade selected");
				} else {				
					String grade = comboGrade.getItemAt(idx);
					try {
						Command c = new Command(Command.CommandValue.ANALYZE, grade);
						WolfGrader.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} 
			if (reset) {
				//All buttons lead back to assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an submission in the CheckState.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class CheckPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Button - Fail */
		private JButton btnFail;
		/** Button - Pass */
		private JButton btnPass;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an submission in the Check state
		 */
		public CheckPanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			btnFail = new JButton("Fail");
			btnPass = new JButton("Pass");	
			btnCancel = new JButton("Cancel");
			
			btnFail.addActionListener(this);
			btnPass.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnFail);
			pnlBtnRow.add(btnPass);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnFail) {				
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.FAIL, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnPass) {
				try {
					Command c = new Command(Command.CommandValue.PASS, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an submission in the RegradeState.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class RegradePanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current submission's id */
		private int id;
		
		/** Button - Process */
		private JButton btnProcess;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an submission in the Regrade state
		 */
		public RegradePanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		

			btnProcess = new JButton("Process");
			btnCancel = new JButton("Cancel");

			btnProcess.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnProcess);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
			
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnProcess) {
					try {
						Command c = new Command(Command.CommandValue.PROCESS, null);
						WolfGrader.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
						reset = false;
					}
			}
			if (reset) {
				//All buttons lead back to assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the submission in return state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class ReturnPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** SubmissionPanel that presents the submission's information to the user */
		private SubmissionPanel pnlInfo;
		/** Current Submission's id */
		private int id;
		
		/** Button - Resolve */
		private JButton btnResolve;
		/** Button - Publish */
		private JButton btnPublish;
		/** Button - Request */
		private JButton btnRequest;
		
		/** Label - grade */
		private JLabel lblGrade;
		/** ComboBox - grade */
		private JComboBox<String> comboGrade;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an submission in the Return state
		 */
		public ReturnPanel() {
			super(new BorderLayout());
			
			pnlInfo = new SubmissionPanel();		
			
			btnCancel = new JButton("Cancel");
			
			btnPublish = new JButton("Publish");
			btnRequest = new JButton("Request");
			
			lblGrade = new JLabel("Grade:");
			comboGrade = new JComboBox<String>();
			comboGrade.addItem("A");
			comboGrade.addItem("B");
			comboGrade.addItem("C");
			comboGrade.addItem("F");
			comboGrade.addItem("F-AIV");
			btnResolve = new JButton("Resolve");
			
			btnResolve.addActionListener(this);
			btnRequest.addActionListener(this);
			btnPublish.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlGrades = new JPanel();
			pnlGrades.setLayout(new GridLayout(1, 3));
			pnlGrades.add(lblGrade);
			pnlGrades.add(comboGrade);
			pnlGrades.add(btnResolve);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnRequest);
			pnlBtnRow.add(btnPublish);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlGrades, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);

						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the SubmissionInfoPanel with the given submission data.
		 * @param id id of the submission
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setSubmissionInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnRequest) {				
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.REQUEST, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnResolve) {
				int idx = comboGrade.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "No grade selected");
				} else {				
					String grade = comboGrade.getItemAt(idx);
					//Try a command.  If problem, go back to submission list.
					
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, grade);
						WolfGrader.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnPublish) {				
				//Try a command.  If problem, go back to submission list.
				try {
					Command c = new Command(Command.CommandValue.PUBLISH, null);
					WolfGrader.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to assignment
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the submission.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class SubmissionPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
				
		/** Label - state */
		private JLabel lblState;
		/** JTextField - state */
		private JTextField txtState;
		
		/** Label - name */
		private JLabel lblName;
		/** JTextField - name */
		private JTextField txtName;
		
		/** Label - unityId */
		private JLabel lblUnityId;
		/** JTextField - unityId */
		private JTextField txtUnityId;
		
		/** Label - processed value */
		private JLabel lblProcessed;
		/** JTextField - processed value */
		private JTextField txtProcessed;
		
		/** Label - plagiarism check result */
		private JLabel lblCheckResult;
		/** JTextField - plagiarism check result */
		private JTextField txtCheckResult;
		
		/** Label - published */
		private JLabel lblPublished;
		/** JTextField - published */
		private JTextField txtPublished;
		
		/** Label - grader */
		private JLabel lblGrader;
		/** JTextField - grader */
		private JTextField txtGrader;
		
		/** Label - grade */
		private JLabel lblGrade;
		/** JTextField - grade */
		private JTextField txtGrade;
		
		/** 
		 * Construct the panel for the information.
		 */
		public SubmissionPanel() {
			super(new GridBagLayout());
			
			lblState = new JLabel("State: ");
			lblName = new JLabel("Name: ");
			lblUnityId = new JLabel("Unity Id: ");
			lblProcessed = new JLabel("Processed: ");
			lblCheckResult = new JLabel("Check Result: ");
			lblPublished = new JLabel("Published: ");
			lblGrader = new JLabel("Grader Id: ");
			lblGrade = new JLabel("Grade: ");

			txtState = new JTextField(15);
			txtName = new JTextField(15);
			txtUnityId = new JTextField(15);
			txtProcessed = new JTextField(15);
			txtCheckResult = new JTextField(15);
			txtPublished = new JTextField(15);
			txtGrader = new JTextField(15);
			txtGrade = new JTextField(15);
			
			txtState.setEditable(false);
			txtName.setEditable(false);
			txtUnityId.setEditable(false);
			txtProcessed.setEditable(false);
			txtCheckResult.setEditable(false);
			txtPublished.setEditable(false);
			txtGrader.setEditable(false);
			txtGrade.setEditable(false);	
			
			GridBagConstraints c = new GridBagConstraints();
						
					
			//Row 1 - state
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblState);
			row1.add(txtState);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row1, c);
			
			//Row 2 - submittor name
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblName);
			row2.add(txtName);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row2, c);
			
			//Row 3 - unityId
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblUnityId);
			row3.add(txtUnityId);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row3, c);
			
			//Row 4 - feedback processed value
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblProcessed);
			row4.add(txtProcessed);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row4, c);
			
			//Row 5 - plagiarism check result 
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(lblCheckResult);
			row5.add(txtCheckResult);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row5, c);
			
			//Row 6 - published value
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 2));
			row6.add(lblPublished);
			row6.add(txtPublished);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row6, c);
			
			//Row 7 - grader id
			JPanel row7 = new JPanel();
			row7.setLayout(new GridLayout(1, 2));
			row7.add(lblGrader);
			row7.add(txtGrader);
			
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row7, c);
			
			//Row 8 - grade
			JPanel row8 = new JPanel();
			row8.setLayout(new GridLayout(1, 2));
			row8.add(lblGrade);
			row8.add(txtGrade);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row8, c);
		}
		
		/**
		 * Adds information about the submission to the display.  
		 * @param id the id for the submission to display information about.
		 */
		public void setSubmissionInfo(int id) {
			//Get the submission from the model
			Submission submission = WolfGrader.getInstance().getSubmissionById(id);
			if (submission == null) {
				//If the submission doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(WolfGraderGUI.this, "Invalid id");
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
			} else {
				//Set the border information with the assignment name and submission id
				String assignmentName = WolfGrader.getInstance().getActiveAssignmentName();
				
				Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, assignmentName + "-  Submission #" + submission.getId());
				setBorder(border);
				setToolTipText(assignmentName + " submission " + submission.getId() + " - " + submission.getState());
				
				//Set all of the fields with the information
				txtState.setText(submission.getState());
				txtName.setText(submission.getName());
				txtUnityId.setText(submission.getUnityId());
				txtCheckResult.setText(submission.getCheckResult());
				if(submission.isFeedbackProcessed()) {
					txtProcessed.setText("Yes");
				} else {
					txtProcessed.setText("No");
				}
				if(submission.isPublished()) {
					txtPublished.setText("Yes");
				} else {
					txtPublished.setText("No");
				}
				txtGrader.setText(submission.getGrader());
				txtGrade.setText(submission.getGrade());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new submission.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AddSubmissionPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;

		/** Label - submittor names */
		private JLabel lblName;
		/** JTextField - submittor names */
		private JTextField txtName;
		
		/** Label - unityId */
		private JLabel lblUnityId;
		/** JTextField - unityId */
		private JTextField txtUnityId;
		
		/** Button to add a submission */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the JPanel for adding new submissions to the 
		 * assignment.
		 */
		public AddSubmissionPanel() {
			super(new BorderLayout());  
			
			//Construct widgets
			lblName = new JLabel("Name: ");
			lblUnityId = new JLabel("Unity Id: ");
						
			txtName = new JTextField(15);
			txtUnityId = new JTextField(15);
						
			btnAdd = new JButton("Submit");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlAdd = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Add Submission");
			pnlAdd.setBorder(border);
			pnlAdd.setToolTipText("Add Submission");
			
			pnlAdd.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - Name
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblName);
			row1.add(txtName);
			
			//Row 2 - UnityId
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblUnityId);
			row2.add(txtUnityId);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row1, c);
			
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row2, c);
			
			//Row 3 - Buttons
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(btnAdd);
			row3.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row3, c);
			
			add(pnlAdd, BorderLayout.NORTH);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				String name = txtName.getText();
				String unityId = txtUnityId.getText();

				//Get instance of model and add submission
				try {
					WolfGrader.getInstance().addSubmissionToAssignment(name, unityId);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Submission cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back submission list
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
				//Reset fields
				txtName.setText("");
				txtUnityId.setText("");
			}
		}
	}
	
	
	
	/**
	 * Inner class that creates the JPanel for creating a new assignment.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AddAssignmentPanel extends JPanel implements ActionListener {
		/** Serial Version UID */
		private static final long serialVersionUID = 1L;
		
		/** Label - assignment name */
		private JLabel lblAssignmentName;
		/** Text Field - assignment name */
		private JTextField txtAssignmentName;
		
		/** Label - category */
		private JLabel lblAssignmentCategory;
		/** Text Field - category */
		private JTextField txtAssignmentCategory;
		
		/** Label - max points */
		private JLabel lblMaxPoints;
		/** Text Field - max points */
		private JTextField txtMaxPoints;
		
		/** Button - add assignment */
		private JButton btnAdd;
		/** Button - cancel */
		private JButton btnCancel;
		
		/**
		 * Constructs the new assignment panel
		 */
		public AddAssignmentPanel() {
			super(new BorderLayout()); 
			
			lblAssignmentName = new JLabel("Assignment Name: ");
			lblAssignmentCategory = new JLabel("Assignment Category: ");
			lblMaxPoints = new JLabel("Maximum Points: ");
			
			txtAssignmentName = new JTextField(25);
			txtAssignmentCategory = new JTextField(5);
			txtMaxPoints = new JTextField(5);
			
			btnAdd = new JButton("Add Assignment");
			btnCancel = new JButton("Cancel");
			
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlAdd = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Add Assignment");
			pnlAdd.setBorder(border);
			pnlAdd.setToolTipText("Add Assignment");
			
			pnlAdd.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Row 1 - assignment name
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblAssignmentName);
			row1.add(txtAssignmentName);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row1, c);
			
			//Row 2 - assignment category
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblAssignmentCategory);
			row2.add(txtAssignmentCategory);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row2, c);
			
			//Row 3 - max points 
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblMaxPoints);
			row3.add(txtMaxPoints);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row3, c);
			
			//Row 4 - buttons
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(btnAdd);
			row4.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row4, c);
			
			add(pnlAdd, BorderLayout.NORTH);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override 
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				try {
					String assignmentName = txtAssignmentName.getText();
					String assignmentCategory = txtAssignmentCategory.getText();
					int maxPoints = Integer.parseInt(txtMaxPoints.getText());
					
					//Get instance of model and add submission
					WolfGrader.getInstance().addNewAssignment(assignmentName, assignmentCategory, maxPoints);
					itemSaveAssignment.setEnabled(true);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(WolfGraderGUI.this, "Assignment cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back submission list
				cardLayout.show(panel, ASSIGNMENT_LIST_PANEL);
				pnlAssignmentList.updateAssignment();
				WolfGraderGUI.this.repaint();
				WolfGraderGUI.this.validate();
				//Reset fields
				txtAssignmentName.setText("");
				txtAssignmentCategory.setText("");
				txtMaxPoints.setText("");
			}
		}
		
		
	}
}