/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import entity.Validate;
import entity.Book;
import GUI.BookManager;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author MSI GF63
 */
public class Controller {

    private JTextField txtBookCode, txtBookName, txtAuthor, txtPublisher;
    private JButton btnSave, btnRemove, btnExit;
    private JCheckBox forrent;
    private JComboBox cboYear;
    private JList bookList;
    ArrayList<Book> books = new ArrayList<>();
    DefaultListModel model = new DefaultListModel();
    private Validate validate = new Validate();

    public Controller() {
        // Run interface
        BookManager bookManager = new BookManager();
        bookManager.setVisible(true);
        btnExit = bookManager.getBtnExit();
        btnSave = bookManager.getBtnSave();
        btnRemove = bookManager.getBtnRemove();
        bookList = bookManager.getjList1();
        txtBookCode = bookManager.getTxtBookCode();
        txtBookName = bookManager.getTxtBookName();
        txtAuthor = bookManager.getTxtAuthor();
        txtPublisher = bookManager.getTxtPublisher();
        cboYear = bookManager.getCboYear();
        forrent = bookManager.getForrent();
        bookList = bookManager.getjList1();
        // load combox year
        loadCombox();
        // create event for show info of book
        bookList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (bookList.getSelectedIndex() >= 0) {
                    showInfo();
                }
            }
        });
        // Button action
        // button exit
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressExit();
            }
        });
        // button save
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save();
                loadList();
            }
        });
        // button remove
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressRemove();
            }
        });

    }

    //
    public void pressExit() {
        if (JOptionPane.showConfirmDialog(null, "Do you want to exit?", 
                "Alert", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void showInfo() {
        // get book is selected index
        int index = bookList.getSelectedIndex();
        // set info to form
        txtBookCode.setText(books.get(index).getCode());
        txtBookName.setText(books.get(index).getName());
        txtAuthor.setText(books.get(index).getAuthor());
        txtPublisher.setText(books.get(index).getPublisher());
        cboYear.setSelectedIndex(books.get(index).getIndexYear());
        if (books.get(index).isForrent()) {
            forrent.setSelected(true);
        }
    }

    public void Save() {
        String checkInput = checkInput();
        // if full input checkInput = ""
        if (checkInput.isEmpty()) {
            int index = bookList.getSelectedIndex();
            Book book = getInfoForm();
            // index < 0 -> none book is selected
            if (index < 0) {
                // check code is or not exist. if exist -> notification else add to list
                if (!validate.isExistCode(books, book.getCode())) {
                    add(book);
                } else {
                    JOptionPane.showMessageDialog(null, "Existed code. Cann't add.");
                }
            } // a book is selected
            else {
                // code is existed 
                if (validate.isExistCode(books, book.getCode())
                        && !books.get(index).getCode().equalsIgnoreCase(book.getCode())) {
                    JOptionPane.showMessageDialog(null, "Code is existed in other book.");
                } else {
                    update(book, index);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Empty input at: " + checkInput);
        }

    }

    public void update(Book book, int index) {
        if (JOptionPane.showConfirmDialog(null, "Do you want to save change?", "Alert",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            books.get(index).setCode(book.getCode());
            books.get(index).setName(book.getName());
            books.get(index).setAuthor(book.getAuthor());
            books.get(index).setPublisher(book.getPublisher());
            books.get(index).setIndexYear(cboYear.getSelectedIndex());
            books.get(index).setForrent(book.isForrent());
        }
    }

    public void add(Book book) {
        books.add(book);
        JOptionPane.showMessageDialog(null, "Add successfully!");
    }

    public String checkInput() {
        String check = "";
        if (validate.checkString(txtBookCode.getText().trim())) {
            check += "bookCode ";
        }
        if (validate.checkString(txtBookName.getText().trim())) {
            check += "bookName ";
        }
        if (validate.checkString(txtAuthor.getText().trim())) {
            check += "Author ";
        }
        if (validate.checkString(txtPublisher.getText().trim())) {
            check += "Publisher ";
        }
        return check.trim();
    }

    public Book getInfoForm() {
        String code = validate.formatString(txtBookCode.getText().trim());
        String name = validate.formatString(txtBookName.getText().trim());
        String author = validate.formatString(txtAuthor.getText().trim());
        String publisher = validate.formatString(txtPublisher.getText().trim());
        int indexYear = cboYear.getSelectedIndex();
        boolean isForrent = forrent.isSelected();
        return new Book(code, name, author, publisher, indexYear, isForrent);
    }

    public void pressRemove() {
        // list none book
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "List book is empty");
            return;
        }
        // not choose a book
        if (bookList.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(null, "Please choose a book to remove.");
            return;
        }
        // list have book and a book choosed
        if (!model.isEmpty() && bookList.getSelectedIndex() >= 0) {
            int index = bookList.getSelectedIndex();
            books.remove(books.get(index));
            model.remove(bookList.getSelectedIndex());
            bookList.setModel(model);
            bookList.setSelectedIndex(0);
        }

    }

    public void loadCombox() {
        // get time now
        LocalDate now = LocalDate.now();
        // split string "yyyy-MM-dd" -> string[0] = year now
        String[] dateNow = String.valueOf(now).split("-");
        // add time to combobox
        for (int i = 1990; i < Integer.parseInt(dateNow[0].trim()); i++) {
            cboYear.addItem(i);
        }
    }

    public void loadList() {
        // remove all items in list
        model.removeAllElements();
        bookList.setModel(model);
        // add all item in list book
        for (Book book : books) {
            model.addElement(book.getName());
        }
        bookList.setModel(model);
    }

}
