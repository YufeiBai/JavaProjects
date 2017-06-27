import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame {
	
	private RSA myRSA;
	private JLabel entryLabel;
	private JTextField entryField;
	private JTextArea outputArea;
	private JButton goButton;
	private JScrollPane outputScroller;
	
	public View(RSA controller) {
		// initialization
		myRSA = controller;
		
		entryLabel = new JLabel("Text: ");
		entryField = new JTextField();
		
		outputArea = new JTextArea("Enter some text you wish to encrypt, then press Do It.");
		outputArea.setEnabled(false);
		Color bgColor = UIManager.getColor("TextField.background");
		outputArea.setBackground(bgColor);
		Color fgColor = UIManager.getColor("TextField.foreground");
		outputArea.setDisabledTextColor(fgColor);
		outputArea.setBorder(BorderFactory.createEtchedBorder());
		outputArea.setLineWrap(true);
		outputScroller = new JScrollPane(outputArea);
		
		goButton = new JButton("Do It");
		
		this.getContentPane().setLayout(null); // absolute positioning is fun
											   // you probably shouldn't do it tbh
		
		// add objects to window
		this.add(entryLabel);
		this.add(entryField);
		this.add(goButton);
		this.add(outputScroller);
		
		// now set them up
		outputScroller.setBounds(0,0,640,430);
		goButton.setBounds(550, 428, 80, 20);
		entryField.setBounds(50, 429, 400, 20);
		entryLabel.setBounds(10, 428, 50, 20);
		
		// honestly reflection would be overkill for this project
		// so let's do it the old fashion way
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				outputArea.setText(myRSA.toString() +
								"Original Text: " + entryField.getText() + "\n"
								+ "Original ASCII: " + myRSA.bytesToString(entryField.getText().getBytes()) + "\n"
								+ "Encrypted ASCII: " + myRSA.bytesToString(myRSA.encrypt(entryField.getText().getBytes())) + "\n"
								+ "Unecrypted ASCII: " + myRSA.bytesToString(myRSA.decrypt(myRSA.encrypt(entryField.getText().getBytes()))) + "\n"
								+ "Unecryped String: " + new String(myRSA.decrypt(myRSA.encrypt(entryField.getText().getBytes()))) + "\n"
								);
			}
		});
		
		// let's get the JFrame up
		this.setSize(new Dimension(645,477));
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("RSA Encrpytion/Decryption");
		
		// and finally, set visible
		this.setVisible(true);
	}
	
	
}
