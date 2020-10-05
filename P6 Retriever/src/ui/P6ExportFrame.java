package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import control.Control;

public class P6ExportFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Border s_defaultCardBorder = new EmptyBorder(20, 10, 10, 10);
	private static final int s_horizontalStrutWidth = 10;
	private static final int s_verticalStrutHeight = 10;
	private static final int s_pnlMinimumHeight = 25;

	private static final String CARD_CONNECTION_INFO = "ConnectionInfo";

	private JTextField txtfldUserName;
	private JPasswordField txtfldPassword;
	private JTextField txtfldHostName;
	private JTextField txtfldPort;
	private JPanel pnlCards;
	private JButton btnNext;
	private JButton btnCancel;
	
	/**
	 * Class to create a simple UI for logging into P6 and exporting timesheet information.
	 * User able to enter username and password and choose a directory to export the files to.
	 */
	public P6ExportFrame () {
		initComponents();
		setTitle("P6 LIVE - Timesheet Export Wizard");
		setVisible(true);
		
	}
	
	 private void initComponents()
	    {
	        pnlCards = new JPanel();
	        pnlCards.setLayout(new CardLayout());
	        pnlCards.setPreferredSize(new Dimension(470, 290));
	        pnlCards.setMaximumSize(new Dimension(470, 290));

	        // Initialize cards
	        initConnectionInfoCard();
	        getContentPane().setLayout(new BorderLayout());
	        getContentPane().add(pnlCards, BorderLayout.NORTH);

	        // General controls
	        {
	            btnNext = new JButton("Choose Export Directory");
	            btnNext.setMnemonic('N');
	            btnCancel = new JButton("Cancel");

	            // Add ActionListeners to buttons
	           
	            btnNext.addActionListener(new ActionListener()
	                {
	                    public void actionPerformed(ActionEvent e)
	                    {
	                         String path = chooseFiles();
	                         if(path !=null) {
		                         Control control = new Control();
		                         boolean success = control.control(path, txtfldUserName.getText(), txtfldPassword.getText(),
		                        		 txtfldHostName.getText(), txtfldPort.getText());
		                         if(success) {
		                        	 JOptionPane.showMessageDialog(btnNext, "Files Exported Successfully", "Great Success", JOptionPane.INFORMATION_MESSAGE);
		                         }else {
		                        	 JOptionPane.showMessageDialog(btnNext, "Something went horribly wrong", "Epic Fail", JOptionPane.ERROR_MESSAGE);
		                         }
		                         System.exit(0);
	                         }
	                    }
	                });
	            btnCancel.addActionListener(new ActionListener()
	                {
	                    public void actionPerformed(ActionEvent e)
	                    {
	                        System.exit(0);
	                    }
	                });

	            JPanel pnlSouth = new JPanel();
	            pnlSouth.setBorder(new EmptyBorder(0, 5, 5, 5));
	            ((FlowLayout)pnlSouth.getLayout()).setAlignment(FlowLayout.RIGHT);
	            pnlSouth.add(btnNext);
	            pnlSouth.add(btnCancel);
	            getRootPane().setDefaultButton(btnNext);
	            getContentPane().add(pnlSouth, BorderLayout.SOUTH);
	        }

	        setResizable(false);
	        pack();
	        setLocationRelativeTo(null);
	    }

	

	private void initConnectionInfoCard()
	{
		txtfldUserName = new JTextField();
		txtfldPassword = new JPasswordField();
		txtfldHostName = new JTextField();
		txtfldPort = new JTextField();

		JPanel pnlCardCenter = new JPanel();
		pnlCardCenter.setBorder(s_defaultCardBorder);
		pnlCardCenter.setLayout(new BoxLayout(pnlCardCenter, BoxLayout.Y_AXIS));

		JPanel pnlUserName = new JPanel();
		pnlUserName.setLayout(new BoxLayout(pnlUserName, BoxLayout.X_AXIS));
		pnlUserName.setMinimumSize(new Dimension(Integer.MAX_VALUE, s_pnlMinimumHeight));

		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setDisplayedMnemonic('U');
		lblUserName.setLabelFor(txtfldUserName);
		pnlUserName.add(lblUserName);
		pnlUserName.add(Box.createHorizontalStrut(s_horizontalStrutWidth));
		pnlUserName.add(txtfldUserName);
		pnlCardCenter.add(pnlUserName);
		pnlCardCenter.add(Box.createVerticalStrut(s_verticalStrutHeight));

		JPanel pnlPassword = new JPanel();
		pnlPassword.setLayout(new BoxLayout(pnlPassword, BoxLayout.X_AXIS));
		pnlPassword.setMinimumSize(new Dimension(Integer.MAX_VALUE, s_pnlMinimumHeight));

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setDisplayedMnemonic('P');
		lblPassword.setLabelFor(txtfldPassword);
		pnlPassword.add(lblPassword);
		pnlPassword.add(Box.createHorizontalStrut(s_horizontalStrutWidth));
		pnlPassword.add(txtfldPassword);
		pnlCardCenter.add(pnlPassword);
		pnlCardCenter.add(Box.createVerticalStrut(s_verticalStrutHeight));

		JPanel pnlHostName = new JPanel();
		pnlHostName.setLayout(new BoxLayout(pnlHostName, BoxLayout.X_AXIS));
		pnlHostName.setMinimumSize(new Dimension(Integer.MAX_VALUE, s_pnlMinimumHeight));

		JLabel lblHostName = new JLabel("Host name:");
		lblHostName.setDisplayedMnemonic('H');
		lblHostName.setLabelFor(txtfldHostName);
		pnlHostName.add(lblHostName);
		pnlHostName.add(Box.createHorizontalStrut(s_horizontalStrutWidth));
		txtfldHostName.setText("firstgroup-ws.milestoneuk.com");
		txtfldHostName.setEditable(false);
		pnlHostName.add(txtfldHostName);
		pnlCardCenter.add(pnlHostName);
		pnlCardCenter.add(Box.createVerticalStrut(s_verticalStrutHeight));

		JPanel pnlPort = new JPanel();
		pnlPort.setLayout(new BoxLayout(pnlPort, BoxLayout.X_AXIS));
		pnlPort.setMinimumSize(new Dimension(Integer.MAX_VALUE, s_pnlMinimumHeight));

		JLabel lblPort = new JLabel("Port:");
		lblPort.setDisplayedMnemonic('o');
		txtfldPort.setText("443");
		txtfldPort.setEditable(false);
		lblPort.setLabelFor(txtfldPort);
		pnlPort.add(lblPort);
		pnlPort.add(Box.createHorizontalStrut(s_horizontalStrutWidth));
		pnlPort.add(txtfldPort);
		pnlCardCenter.add(pnlPort);
		pnlCardCenter.add(Box.createVerticalStrut(Integer.MAX_VALUE));
		sizeUniformly(lblUserName, new JComponent[] {lblPassword, lblHostName, lblPort});
		pnlCards.add(pnlCardCenter, CARD_CONNECTION_INFO);
	}

	private static void sizeUniformly(JComponent mainComponent, JComponent[] otherComponents){
		Dimension dim = mainComponent.getPreferredSize();

		for (int i = 0; i < otherComponents.length; i++){
			JComponent comp = otherComponents[i];
			comp.setMinimumSize(dim);
			comp.setMaximumSize(dim);
			comp.setPreferredSize(dim);
		}
	}
	
	private String chooseFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int val = chooser.showSaveDialog(this);
		if(val == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			return file.getAbsolutePath();
		}
		return null;
	}
}
