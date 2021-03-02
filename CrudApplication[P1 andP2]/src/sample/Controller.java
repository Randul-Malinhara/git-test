package sample;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable {

    @FXML
    private TextField tfid;
    @FXML
    private TextField tftitle;
    @FXML
    private TextField tfauthor;
    @FXML
    private TextField tfyear;
    @FXML
    private TextField tfpages;
    @FXML
    private TableView<Books> tvbooks;
    @FXML
    private TableColumn<Books, Integer> colid;
    @FXML
    private TableColumn<Books, String> coltitle;
    @FXML
    private TableColumn<Books, String> colauthor;
    @FXML
    private TableColumn<Books, Integer> colyear;
    @FXML
    private TableColumn<Books, Integer> colpages;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    public void handleButtonAction(ActionEvent event){


        if(event.getSource() == btnInsert){
            insertRecord();
            System.out.println("You clicked Insert");
        }else if (event.getSource() == btnUpdate){
            updateRecord();
            System.out.println("You clicked update");
        }else if(event.getSource() == btnDelete){
            deleteButton();
            System.out.println("You clicked delete");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
    }
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
            return conn;

        }catch (Exception ex){
            System.out.println("Error: " +ex.getMessage());
            return null;
        }
    }
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM books";
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()){
                books = new Books(rs.getInt("id"),rs.getString("title"),rs.getString("author"),rs.getInt("year"),rs.getInt("pages"));
                bookList.add(books);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bookList;
    }
    public void showBooks(){
        ObservableList<Books> list = getBooksList();

        colid.setCellValueFactory(new PropertyValueFactory<Books,Integer>("id"));
        coltitle.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
        colauthor.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
        colyear.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
        colpages.setCellValueFactory(new PropertyValueFactory<Books,Integer>("pages"));

        tvbooks.setItems(list);
    }
    private void insertRecord(){
        String query = "INSERT INTO books VALUES (" + tfid.getText() + ",'" + tftitle.getText() + "','" + tfauthor.getText() + "',"
                + tfyear.getText() + "," + tfpages.getText() + ")";
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
        String query = "UPDATE  books SET title  = '" + tftitle.getText() + "', author = '" + tfauthor.getText() + "', year = " +
                tfyear.getText() + ", pages = " + tfpages.getText() + " WHERE id = " + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }
    private void deleteButton(){
        String query = "DELETE FROM books WHERE id =" + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();

        }
    }
    @FXML
    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        Books book = tvbooks.getSelectionModel().getSelectedItem();
        tfid.setText(""+ book.getId());
        tftitle.setText(book.getTitle());
        tfauthor.setText(book.getAuthor());
        tfyear.setText(""+book.getYear());
        tfpages.setText(""+book.getPages());
    }
}
