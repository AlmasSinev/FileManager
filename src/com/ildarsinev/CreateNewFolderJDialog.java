package com.ildarsinev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewFolderJDialog extends JDialog {

    private JTextField nameOfNewFolder = new JTextField(10);
    private JButton okButton = new JButton("Ок");
    private JButton cancelButton = new JButton("Отмена");
    private String newFolderName;
    private JLabel nameFolderWait = new JLabel("Имя новой папки: ");
    private boolean ready = false;


    public CreateNewFolderJDialog(JFrame frame) {
        super(frame, "Создать папку", true);
        setLayout(new GridLayout(2, 2, 5, 5));
        setSize(400, 200);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFolderName = nameFolderWait.getText();
                setVisible(false);
                ready = true;
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ready = false;
            }
        });

        getContentPane().add(nameFolderWait);
        getContentPane().add(nameOfNewFolder);
        getContentPane().add(okButton);
        getContentPane().add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public boolean getReady() {
        return ready;
    }

    public String getNewName() {
        return newFolderName;
    }

}
