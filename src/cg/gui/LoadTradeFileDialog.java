package cg.gui;

import javax.swing.JDialog; 

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.io.File;

public class LoadTradeFileDialog extends JDialog implements ActionListener
{
    private JPanel _panel = null;

    /** Ok and Cancel buttons. */
    private JButton _okButton = null;
    private JButton _cancelButton = null;

    private JTextField _filenameField = null;
    private JButton _chooseFileButton = null;

    private JComboBox<String> _accountComboBox = null;
    private boolean _pressedOk = false;
    private String _fileDir = null;

    public LoadTradeFileDialog(JFrame frame, boolean modal,
    		Vector<String> aAccountNames, String aFileDir, String myMessage)
    {
        super(frame, modal);
        
        _fileDir = aFileDir;

        _panel = new JPanel();
        getContentPane().add(_panel);
        
        _accountComboBox = new JComboBox<String>(aAccountNames);
        _panel.add(_accountComboBox);

        _filenameField = new JTextField(40);
        _panel.add(_filenameField);

        _chooseFileButton = new JButton("File...");
        _chooseFileButton.addActionListener(this);
        _panel.add(_chooseFileButton); 

        _panel.add(new JLabel(myMessage));

        _okButton = new JButton("OK");
        _okButton.addActionListener(this);
        _panel.add(_okButton); 

        _cancelButton = new JButton("Cancel");
        _cancelButton.addActionListener(this);
        _panel.add(_cancelButton);  

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(_okButton == e.getSource()) {
            System.err.println("User chose OK.");
            _pressedOk = true;
            setVisible(false);
        }
        else if(_cancelButton == e.getSource()) {
            System.err.println("User chose Cancel.");
            _pressedOk = false;
            setVisible(false);
        }
        else if(_chooseFileButton == e.getSource()) {
            System.err.println("User pressed file button.");
            chooseFile();
            _pressedOk = false;
        }
    }
    
    private void chooseFile()
    {

    	JFileChooser chooser = new JFileChooser();
    	chooser.setCurrentDirectory(new File(_fileDir));
    	int tOption = chooser.showOpenDialog(this);
    	if (tOption == JFileChooser.APPROVE_OPTION) {
    		String tFile = chooser.getSelectedFile().getPath();
    		System.out.println("File selected: " + tFile);
    		// load the trades
    		_filenameField.setText(tFile);
    	} else {
    		System.out.println("File selection canceled");
    	}
    }

    public boolean pressedOk()
    {
    	return _pressedOk;
    }
    
    public String getAccountName()
    {
    	return (String) _accountComboBox.getSelectedItem();
    }
    
    public String getTradeFileName()
    {
    	return _filenameField.getText();
    }
}
