/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacymanagementsystem;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author WINDOWS 10
 */
public class dashboardContoller implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button addMed_btn;

    @FXML
    private Button pruchase_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private Label dashboard_availableMed;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Label dashboard_totalCustomers;

    @FXML
    private AnchorPane addMedicines_form;

    @FXML
    private TextField addMedicines_medicineID;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private TextField addMedicines_productName;

    @FXML
    private ComboBox<?> addMedicines_type;

    @FXML
    private ComboBox<?> addMedicines_status;

    @FXML
    private TextField addMedicines_price;

    @FXML
    private ImageView addMedicines_imageView;

    @FXML
    private Button addMedicines_importBtn;

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private Button addMedicines_updateBtn;

    @FXML
    private Button addMedicines_clearBtn;

    @FXML
    private Button addMedicines_deleteBtn;

    @FXML
    private TextField addMedicines_search;

    @FXML
    private TableView<medicineData> addMedicines_tableView;
    
    @FXML
    private TableColumn<medicineData, String> addMedicines_col_medicineID;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_brand;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_productName;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_type;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_price;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_status;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_date;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_type;
    
    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private ComboBox<?> purchase_medicineID;

    @FXML
    private ComboBox<?> purchase_brand;

    @FXML
    private ComboBox<?> purchase_productName;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private Label purchase_total;

    @FXML
    private TextField purchase_amount;

    @FXML
    private Label purchase_balance;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private TableColumn<customerData, String> purchase_col_medicineId;

    @FXML
    private TableColumn<customerData, String> purchase_col_brand;

    @FXML
    private TableColumn<customerData, String> purchase_col_productName;

    @FXML
    private TableColumn<customerData, String> purchase_col_type;

    @FXML
    private TableColumn<customerData, String> purchase_col_qty;

    @FXML
    private TableColumn<customerData, String> purchase_col_price;

//    DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    private Image image;
    
    public void homeChart(){
        dashboard_chart.getData().clear();
        
        String sql = "SELECT date, SUM(total) FROM customer_info"
                + " GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 9";
        
        connect = database.connectDb();
        
        try{
            XYChart.Series chart = new XYChart.Series();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }
            
            dashboard_chart.getData().add(chart);
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void homeAM(){
        
        String sql = "SELECT COUNT(id) FROM medicine WHERE status = 'Available'";
        
        connect = database.connectDb();
        int countAM = 0;
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                countAM = result.getInt("COUNT(id)");
            }
            
            dashboard_availableMed.setText(String.valueOf(countAM));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void homeTI(){
        String sql = "SELECT SUM(total) FROM customer_info";
        
        connect = database.connectDb();
        double totalDisplay = 0;
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                totalDisplay = result.getDouble("SUM(total)");
            }
            
            dashboard_totalIncome.setText("$" + String.valueOf(totalDisplay));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    
    
    public void homeTC(){
        
        String sql = "SELECT COUNT(id) FROM customer_info";
        
        connect = database.connectDb();
        int countTC = 0;
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                countTC = result.getInt("COUNT(id)");
            }
            
            dashboard_totalCustomers.setText(String.valueOf(countTC));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void addMedicinesAdd(){
        
        String sql = "INSERT INTO medicine (medicine_id, brand, productName, type, status, price, image, date) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        
        connect = database.connectDb();
        
        try{
            
            Alert alert;
            
            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                // CHECK IF THE MEDICINE ID YOU WANT TO INSERT EXIST
                String checkData = "SELECT medicine_id FROM medicine WHERE medicine_id = '"
                        +addMedicines_medicineID.getText()+"'";
                
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                
                if(result.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Medicine ID: " + addMedicines_medicineID.getText() + " was already exist!");
                    alert.showAndWait();
                }else{
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addMedicines_medicineID.getText());
                    prepare.setString(2, addMedicines_brand.getText());
                    prepare.setString(3, addMedicines_productName.getText());
                    prepare.setString(4, (String)addMedicines_type.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String)addMedicines_status.getSelectionModel().getSelectedItem());
                    prepare.setString(6, addMedicines_price.getText());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8, String.valueOf(sqlDate));
                    
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    
                    addMedicineShowListData();
                    addMedicineReset();
                    
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void addMedicineUpdate(){
        
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");
        
        String sql = "UPDATE medicine SET brand = '"
                +addMedicines_brand.getText()+"', productName = '"
                +addMedicines_productName.getText()+"', type = '"
                +addMedicines_type.getSelectionModel().getSelectedItem()+"', status = '"
                +addMedicines_status.getSelectionModel().getSelectedItem()+"', price = '"
                +addMedicines_price.getText()+"', image = '"+uri+"' WHERE medicine_id = '"
                +addMedicines_medicineID.getText()+"'";
        
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Medicine ID:" + addMedicines_medicineID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                    
                    addMedicineShowListData();
                    addMedicineReset();
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void addMedicineDelete(){
        
        String sql = "DELETE FROM medicine WHERE medicine_id = '"+addMedicines_medicineID.getText()+"'";
        
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Medicine ID:" + addMedicines_medicineID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    
                    addMedicineShowListData();
                    addMedicineReset();
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void addMedicineReset(){
        addMedicines_medicineID.setText("");
        addMedicines_brand.setText("");
        addMedicines_productName.setText("");
        addMedicines_price.setText("");
        addMedicines_type.getSelectionModel().clearSelection();
        addMedicines_status.getSelectionModel().clearSelection();
        
        addMedicines_imageView.setImage(null);
        
        getData.path = "";
        
    }
    
    private String[] addMedicineListT = {"Hydrocodone", "Antibiotics", "Metformin", "Losartan", "Albuterol"};
    public void addMedicineListType(){
        List<String> listT = new ArrayList<>();
        
        for(String data: addMedicineListT){
            listT.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listT);
        addMedicines_type.setItems(listData);
        
    }
    
    
    
    private String[] addMedicineStatus = {"Available", "Not Available"};
    public void addMedicineListStatus(){
        List<String> listS = new ArrayList<>();
        
        for(String data: addMedicineStatus){
            listS.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listS);
        addMedicines_status.setItems(listData);
    }
    
    public void addMedicineImportImage(){
        
        FileChooser open = new FileChooser();
        open.setTitle("Import Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));
        
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        
        if(file != null){
            image = new Image(file.toURI().toString(), 118, 147, false, true);
            
            addMedicines_imageView.setImage(image);
            
            getData.path = file.getAbsolutePath();
        }
        
    }
    
    public ObservableList<medicineData> addMedicinesListData(){
        
        String sql = "SELECT * FROM medicine";
        
        ObservableList<medicineData> listData = FXCollections.observableArrayList();
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            medicineData medData;
            
            while(result.next()){
                medData = new medicineData(result.getInt("medicine_id"), result.getString("brand")
                        , result.getString("productName"), result.getString("type")
                        , result.getString("status"), result.getDouble("price")
                        , result.getString("image"), result.getDate("date"));
                
                listData.add(medData);
            }
            
        }catch(Exception e){e.printStackTrace();}
        return listData;
    }
    
    private ObservableList<medicineData> addMedicineList;
    public void addMedicineShowListData(){
        addMedicineList = addMedicinesListData();
        
        addMedicines_col_medicineID.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        addMedicines_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addMedicines_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addMedicines_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addMedicines_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addMedicines_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addMedicines_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        addMedicines_tableView.setItems(addMedicineList);
        
    }
    
    public void addMedicineSearch(){
        
        FilteredList<medicineData> filter = new FilteredList<>(addMedicineList, e-> true);
        
        addMedicines_search.textProperty().addListener((Observable, oldValue, newValue) ->{
            
            filter.setPredicate(predicateMedicineData ->{
                
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                
                String searchKey = newValue.toLowerCase();
                
                if(predicateMedicineData.getMedicineId().toString().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getBrand().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getProductName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getType().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getStatus().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getPrice().toString().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getDate().toString().contains(searchKey)){
                    return true;
                }else return false;
            });
        });
        
        SortedList<medicineData> sortList = new SortedList<>(filter);
        
        sortList.comparatorProperty().bind(addMedicines_tableView.comparatorProperty());
        addMedicines_tableView.setItems(sortList);
        
    }
    
    public void addMedicineSelect(){
        medicineData medData = addMedicines_tableView.getSelectionModel().getSelectedItem();
        int num = addMedicines_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < - 1){return;}
        
        addMedicines_medicineID.setText(String.valueOf(medData.getMedicineId()));
        addMedicines_brand.setText(medData.getBrand());
        addMedicines_productName.setText(medData.getProductName());
        addMedicines_price.setText(String.valueOf(medData.getPrice()));
        
        String uri = "file:" + medData.getImage();
        
        image = new Image(uri, 118, 147, false, true);
        addMedicines_imageView.setImage(image);
        
        getData.path = medData.getImage();
        
    }
    
    private double totalP;
    public void purchaseAdd(){
        purchaseCustomerId();
        
        String sql = "INSERT INTO customer (customer_id,type,medicine_id,brand,productName,quantity,price,date)"
                + " VALUES(?,?,?,?,?,?,?,?)";
        
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(purchase_type.getSelectionModel().getSelectedItem() == null
                    || purchase_medicineID.getSelectionModel().getSelectedItem() == null
                    || purchase_brand.getSelectionModel().getSelectedItem() == null
                    || purchase_productName.getSelectionModel().getSelectedItem() == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerId));
                prepare.setString(2, (String)purchase_type.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String)purchase_medicineID.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String)purchase_brand.getSelectionModel().getSelectedItem());
                prepare.setString(5, (String)purchase_productName.getSelectionModel().getSelectedItem());
                prepare.setString(6, String.valueOf(qty));
                
                String checkData = "SELECT price FROM medicine WHERE medicine_id = '"
                        +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";
                
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                double priceD = 0;
                if(result.next()){
                    priceD = result.getDouble("price");
                }
                
                totalP = (priceD * qty);
                
                prepare.setString(7, String.valueOf(totalP));
                
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(8, String.valueOf(sqlDate));
                
                prepare.executeUpdate();
                
                purchaseShowListData();
                purchaseDisplayTotal();
            }
            
            prepare = connect.prepareStatement(sql);
        }catch(Exception e){e.printStackTrace();}
    }
    private double totalPriceD;
    public void purchaseDisplayTotal(){
        
        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '"+customerId+"'";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                totalPriceD = result.getDouble("SUM(price)");
            }
            purchase_total.setText("$" + String.valueOf(totalPriceD));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    private double balance;
    private double amount;
    public void purchaseAmount(){
        
        if(purchase_amount.getText().isEmpty() || totalPriceD == 0){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        }else{
            amount = Double.parseDouble(purchase_amount.getText());
            if(totalPriceD > amount){
                purchase_amount.setText("");
            }else{
                balance = (amount - totalPriceD);
                
                purchase_balance.setText("$"+String.valueOf(balance));
            }
        }
        
    }
    
    public void purchasePay(){
        purchaseCustomerId();
        String sql = "INSERT INTO customer_info (customer_id, total, date) "
                + "VALUES(?,?,?)";
        
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(totalPriceD == 0){
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Something wrong :3");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(totalPriceD));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();
                    
                    purchase_amount.setText("");
                    purchase_balance.setText("$0.0");
                }
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    private SpinnerValueFactory<Integer> spinner;
    public void purchaseShowValue(){
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);
        purchase_quantity.setValueFactory(spinner);
    }
    
    private int qty;
    public void purchaseQuantity(){
        qty = purchase_quantity.getValue();
    }
    
    public ObservableList<customerData> purchaseListData(){
        purchaseCustomerId();
        
        String sql = "SELECT * FROM customer WHERE customer_id = '"+customerId+"'";
        
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        
        connect = database.connectDb();
        
        try{
            customerData customerD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                customerD = new customerData(result.getInt("customer_id")
                        , result.getString("type"), result.getInt("medicine_id")
                        , result.getString("brand"), result.getString("productName")
                        , result.getInt("quantity"), result.getDouble("price")
                        , result.getDate("date"));
                
                listData.add(customerD);
            }
            
        }catch(Exception e){e.printStackTrace();}
        return listData;
    }
    
    private ObservableList<customerData> purchaseList;
    public void purchaseShowListData(){
        purchaseList = purchaseListData();
        
        purchase_col_medicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        purchase_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        purchase_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        purchase_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        purchase_col_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        purchase_tableView.setItems(purchaseList);
        
    }
    
    private int customerId;
    public void purchaseCustomerId(){
        
        String sql = "SELECT customer_id FROM customer";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                customerId = result.getInt("customer_id");
            }
            int cID = 0;
            String checkData = "SELECT customer_id FROM customer_info";
            
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);
            
            while(result.next()){
                cID = result.getInt("customer_id");
            }
            
            if(customerId == 0){
                customerId+=1;
            }else if(cID == customerId){
                customerId = cID+1;
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void purchaseType(){
        
        String sql = "SELECT type FROM medicine WHERE status = 'Available'";
        
        connect = database.connectDb();
        
        try{
            ObservableList listData = FXCollections.observableArrayList();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("type"));
            }
            purchase_type.setItems(listData);
            
            purchaseMedicineId();
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void purchaseMedicineId(){
        
        String sql = "SELECT * FROM medicine WHERE type = '"
                +purchase_type.getSelectionModel().getSelectedItem()+"'";
        
        connect = database.connectDb();
        
        try{
            ObservableList listData = FXCollections.observableArrayList();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("medicine_id"));
            }
            purchase_medicineID.setItems(listData);
            
            purchaseBrand();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void purchaseBrand(){
        
        String sql = "SELECT * FROM medicine WHERE medicine_id = '"
                +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";
        
        connect = database.connectDb();
        
        try{
            ObservableList listData = FXCollections.observableArrayList();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("brand"));
            }
            purchase_brand.setItems(listData);
            
            purchaseProductName();
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void purchaseProductName(){
        
        String sql = "SELECT * FROM medicine WHERE brand = '"
                +purchase_brand.getSelectionModel().getSelectedItem()+"'";
        
        connect = database.connectDb();
        
        try{
            ObservableList listData = FXCollections.observableArrayList();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("productName"));
            }
            purchase_productName.setItems(listData);
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void defaultNav(){
        dashboard_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
    }
    
    public void switchForm(ActionEvent event){
        
        if(event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(false);
            
            dashboard_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            addMed_btn.setStyle("-fx-background-color:transparent");
            pruchase_btn.setStyle("-fx-background-color:transparent");
            
            homeChart();
            homeAM();
            homeTI();
            homeTC();
            
        }else if(event.getSource() == addMed_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(true);
            purchase_form.setVisible(false);
            
            addMed_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            dashboard_btn.setStyle("-fx-background-color:transparent");
            pruchase_btn.setStyle("-fx-background-color:transparent");
            
            addMedicineShowListData();
            addMedicineListStatus();
            addMedicineListType();
            addMedicineSearch();
            
        }else if(event.getSource() == pruchase_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(true);
            
            pruchase_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            dashboard_btn.setStyle("-fx-background-color:transparent");
            addMed_btn.setStyle("-fx-background-color:transparent");
            
            purchaseType();
            purchaseMedicineId();
            purchaseBrand();
            purchaseProductName();
            purchaseShowListData();
            purchaseShowValue();
            purchaseDisplayTotal();
            
        }
        
    }
    
    public void displayUsername(){
        String user = getData.username;
                        // BIG LETTER THE FIRST LETTER THEN THE REST ARE SMALL LETTER
        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
        
    }
    
    private double x = 0;
    private double y = 0;

    public void logout() {

        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                // HIDE THE DASHBOARD FORM
                logout.getScene().getWindow().hide();
                // LINK YOUR LOGIN FORM
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        defaultNav();
//        THATS IT, THANKS FOR WATCHING! HOPE YOU LIKE THIS VIDEO : ) 
//        SUBSCRIBE AND HIT LIKE BUTTON FOR THE SUPPORT : ) 
//        THANKS GUYS! <3 
        homeAM();
        homeTI();
        homeTC();
        homeChart();
        
        addMedicineShowListData();
        addMedicineListStatus();
        addMedicineListType();
        
        purchaseType();
        purchaseMedicineId();
        purchaseBrand();
        purchaseProductName();
        purchaseShowListData();
        purchaseShowValue();
        purchaseDisplayTotal();
        
    }

}
