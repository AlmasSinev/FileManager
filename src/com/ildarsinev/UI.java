package com.ildarsinev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class UI extends JFrame {

    private JPanel catalogPanel = new JPanel();
    private JList filesList = new JList();
    private JScrollPane filesScroll = new JScrollPane(filesList);
    private JPanel buttonsPanel = new JPanel();
    private JButton addButton = new JButton("Создать папку");
    private JButton backButton = new JButton("Назад");
    private JButton deleteButton = new JButton("Удалить");
    private JButton renameButton = new JButton("Переименовать");
    private ArrayList<String> dirsCache = new ArrayList<>();

    public UI() {
        super("Проводник");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        catalogPanel.setLayout(new BorderLayout(5, 5));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonsPanel.setLayout(new GridLayout(1, 4, 5, 5));
        JDialog createNewDirDialog = new JDialog(UI.this, "Создание папки", true);
        JPanel createNewDirPanel = new JPanel();
        createNewDirDialog.add(createNewDirPanel);
        File[] discs = File.listRoots();
        filesScroll.setPreferredSize(new Dimension(400, 500));
        filesList.setListData(discs);
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        filesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    DefaultListModel model = new DefaultListModel();
                    String selectedObject = filesList.getSelectedValue().toString();
                    String fullPath = toFullPath(dirsCache);
                    File selectedFile;

                    if (dirsCache.size() > 1)
                        selectedFile = new File(fullPath, selectedObject);
                    else
                        selectedFile = new File(fullPath + selectedObject);

                    if (selectedFile.isDirectory()) {
                        String[] rootStr = selectedFile.list();
                        for (String str : rootStr){
                            File checkObject = new File(selectedFile.getPath(), str);
                            if (!checkObject.isHidden())
                                if (checkObject.isDirectory())
                                    model.addElement(str);
                                else
                                    model.addElement("Файл - " + str);

                        }
                        dirsCache.add(selectedObject);
                        filesList.setModel(model);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dirsCache.isEmpty()) {
                    String currentPath;
                    File newFolder;
                    CreateNewFolderJDialog newFolderJDialog = new CreateNewFolderJDialog(UI.this);

                    if (newFolderJDialog.getReady()) {
                        currentPath = toFullPath(dirsCache);
                        newFolder = new File(currentPath, newFolderJDialog.getNewName());
                        if (!newFolder.exists()){
                            newFolder.mkdir();
                            System.out.println("ПИИИСИТЬ");
                        }
                        File updateDir = new File(currentPath);
                        String updateMas[] = updateDir.list();
                        DefaultListModel updateModel = new DefaultListModel();
                        for (String str : updateMas) {
                            File check  = new File(updateDir.getPath(), str);
                            if (!check.isHidden()){
                                if (check.isDirectory())
                                    updateModel.addElement(str);
                                else
                                    updateModel.addElement("Файл - " + str);
                            }
                        }
                        filesList.setModel(updateModel);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedObject = filesList.getSelectedValue().toString();
                String currentPath = toFullPath(dirsCache);
                if (!selectedObject.isEmpty()) {
                    deleteDir(new File(currentPath, selectedObject));

                    File updateDir = new File(currentPath);
                    String[] updateMas = updateDir.list();
                    DefaultListModel updateModel = new DefaultListModel();
                    for (String str : updateMas){
                        File check  = new File(updateDir.getPath(), str);
                        if (!check.isHidden()){
                            if (check.isDirectory())
                                updateModel.addElement(str);
                            else
                                updateModel.addElement("Файл - " + str);
                        }
                    }
                    filesList.setModel(updateModel);
                }
            }
        });

        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dirsCache.isEmpty() & filesList.getSelectedValue() != null) {
                    String currentPath = toFullPath(dirsCache);
                    String selectedObject = filesList.getSelectedValue().toString();
                    RenameJDialog renamer = new RenameJDialog(UI.this);

                    if (renamer.getReady()){
                        File renameFile = new File(currentPath, selectedObject);
                        renameFile.renameTo(new File(currentPath, renamer.getNewName()));
                        System.out.println("ПИИИСИТЬ");
                        File updateDir = new File(currentPath);
                        String updateMas[] = updateDir.list();
                        DefaultListModel updateModel = new DefaultListModel();
                        for (String str : updateMas){
                            File check  = new File(updateDir.getPath(), str);
                            if (!check.isHidden()){
                                if (check.isDirectory())
                                    updateModel.addElement(str);
                                else
                                    updateModel.addElement("Файл - " + str);
                            }
                        }
                        filesList.setModel(updateModel);
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dirsCache.size() > 1){
                    dirsCache.remove(dirsCache.size() - 1);
                    String backDir = toFullPath(dirsCache);
                    String[] objects = new File(backDir).list();
                    DefaultListModel backRootModel = new DefaultListModel();

                    for (String str : objects) {
                        File checkFile = new File(backDir, str);
                        if (!checkFile.isHidden()) {
                            if (checkFile.isDirectory())
                                backRootModel.addElement(str);
                            else
                                backRootModel.addElement("Файл - " + str);
                        }
                    }
                    filesList.setModel(backRootModel);
                }
                else {
                    dirsCache.removeAll(dirsCache);
                    filesList.setListData(discs);
                }
            }
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(renameButton);
        catalogPanel.setLayout(new BorderLayout());
        catalogPanel.add(filesScroll, BorderLayout.CENTER);
        catalogPanel.add(buttonsPanel, BorderLayout.SOUTH);

        getContentPane().add(catalogPanel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String toFullPath (ArrayList<String> file) {
        StringBuilder listPart = new StringBuilder();
        for (String str : file)
            listPart.append(str);
        return listPart.toString();
    }

    public void deleteDir (File file) {
        File[] objects = file.listFiles();
        if (objects != null){
            for (File f : objects)
                deleteDir(f);
        }
        file.delete();
    }

}
