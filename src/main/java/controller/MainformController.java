package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class loads the main menu form.
 */
public class MainformController implements Initializable {
    @FXML
    public TextField productsSearch;
    @FXML
    public TextField partsSearch;
    public Label mainMenuError;
    @FXML
    private Button ExitB;
    @FXML
    private Button partsAddB;
    @FXML
    private Button partsDeleteB;
    @FXML
    private Button partsModifyB;
    @FXML
    private Button productsAddB;
    @FXML
    private Button productsDeleteB;
    @FXML
    private Button productsModifyB;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Part, Double> partsCpuCol;
    @FXML
    private TableColumn<Part, Integer> partsInvlevelCol;
    @FXML
    private TableColumn<Part, Integer> partsPartidCol;
    @FXML
    private TableColumn<Part, String> partsPartnameCol;
    @FXML
    private TableColumn<Product, Double> productsCpuCol;
    @FXML
    private TableColumn<Product, Integer> productsInvlevelCol;
    @FXML
    private TableColumn<Product, Integer> productsProdidCol;
    @FXML
    private TableColumn<Product, String> productsProdnameCol;

    /**
     * This method searches through the parts list by name and id and displays the results in the parts table.
     * @param actionEvent The enter key on the parts search bar
     */
    public void onPartsSearch(ActionEvent actionEvent) {
        String pQuery = partsSearch.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pQuery);

        if(parts.isEmpty()) {
            mainMenuError.setText("Part not found.");
        }
        else {
            mainMenuError.setText("");
        }
        if(parts.size() == 0) {
            try {
                int pId = Integer.parseInt(pQuery);
                Part p = Inventory.lookupPart(pId);
                if (p != null) {
                    parts.add(p);
                    mainMenuError.setText("");
                }
                else {
                    mainMenuError.setText("Part not found.");
                }
            }
            catch (NumberFormatException p) {
                //ignore
            }
        }
        partsTable.setItems(parts);
    }

    /**
     * This method searches through the products list by name and id and displays the results in the products table.
     * @param actionEvent The enter key on the products search bar
     */
    public void onProductsSearch(ActionEvent actionEvent) {
        String prQuery = productsSearch.getText();

        ObservableList<Product> products = Inventory.lookupProduct(prQuery);

        if(products.isEmpty()) {
            mainMenuError.setText("Product not found.");
        }
        else {
            mainMenuError.setText("");
        }
        if(products.size() == 0) {
            try {
                int prId = Integer.parseInt(prQuery);
                Product pr = Inventory.lookupProduct(prId);
                if (pr != null) {
                    products.add(pr);
                    mainMenuError.setText("");
                }
                else {
                    mainMenuError.setText("Product not found.");
                }
            }
            catch (NumberFormatException pr) {
                //ignore
            }
        }
        productsTable.setItems(products);
    }

    /**
     * This method initializes data of both parts and products table when the main form is loaded.
     * @param url root path for the main form
     * @param resourceBundle resources used to localize the main menu
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());

        partsPartidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsPartnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvlevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCpuCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(Inventory.getAllProducts());

        productsProdidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsProdnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInvlevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsCpuCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method changes scenes to the AddPartForm.
     * @param actionEvent The add part button
     * @throws IOException input/output exception
     */
    public void onPartsAddB(ActionEvent actionEvent) throws IOException {
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/AddPartForm.fxml"));
        Scene addPartFormScene = new Scene(mainForm);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(addPartFormScene);
        window.show();
    }

    /**
     * This method changes scenes to the ModifyPartForm and sends the data of the selected part to the ModifyPartForm.
     * @param actionEvent The modify part button
     * @throws IOException input/output exception
     */
    public void onPartsModifyB(ActionEvent actionEvent)throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mcewen/mainform/ModifyPartForm.fxml"));
            Parent modifyPartScene = loader.load();

            ModifyPartController MPController = loader.getController();
            MPController.sendParts(partsTable.getSelectionModel().getSelectedItem());

            Scene modifyPartFormScene = new Scene(modifyPartScene);

            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(modifyPartFormScene);
            window.show();
        }
        catch (NullPointerException e) {
            mainMenuError.setText("ERROR: Please select a valid part to modify.");
        }

    }

    /**
     * This method deletes a selected part from the partsTable in the main menu.
     * @param actionEvent The delete button
     */
    public void onPartsDeleteB(ActionEvent actionEvent) {
        //gets selected item
        Part delPart = partsTable.getSelectionModel().getSelectedItem();
        try {
            if (delPart.equals(partsTable.getSelectionModel().getSelectedItem())) {
                //delete confirmation dialog box creation
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected part. Are you sure you want to continue?");
                alert.setTitle("");
                //calls delete part method
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deletePart(delPart);
                    Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "Part deleted successfully.");
                    confirmAlert.setTitle("");
                    confirmAlert.setHeaderText("Part Deleted");
                    confirmAlert.showAndWait();
                }
            }
        }
        catch (NullPointerException e) {
            mainMenuError.setText("ERROR: Please select a valid part for deletion.");
        }
    }

    /**
     * This method switches scenes to the AddProductForm.
     * @param actionEvent The add product button
     * @throws IOException input/output exception
     */
    public void onProductsAddB(ActionEvent actionEvent) throws IOException {
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/AddProductForm.fxml"));
        Scene addProductFormScene = new Scene(mainForm);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(addProductFormScene);
        window.show();
    }

    /**
     * This method changes scenes to the ModifyProductForm and sends the data of the selected product to the ModifyProductForm.
     * @param actionEvent The modify product button
     * @throws IOException input/output exception
     */
    public void onProductsModifyB(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mcewen/mainform/ModifyProductForm.fxml"));
            Parent modifyProductScene = loader.load();

            ModifyProductController MPrController = loader.getController();
            MPrController.sendProduct(productsTable.getSelectionModel().getSelectedItem());

            Scene modifyProductFormScene = new Scene(modifyProductScene);

            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            window.setScene(modifyProductFormScene);
            window.show();
        }
        catch (NullPointerException e) {
            mainMenuError.setText("ERROR: Please select a valid product to modify.");
        }
    }

    /**
     * This method deletes a selected product with no associated parts from the productsTable in the main menu.
     * @param actionEvent The delete button
     */
    public void onProductsDeleteB(ActionEvent actionEvent) {
        Product delProduct = productsTable.getSelectionModel().getSelectedItem();
        try {
            if(!delProduct.getAllAssociatedParts().isEmpty()) {
                mainMenuError.setText("ERROR: Cannot delete a product that contains associated parts.");
                return;
            }
            if (delProduct.getAllAssociatedParts().isEmpty()) {
                if (delProduct.equals(productsTable.getSelectionModel().getSelectedItem())) {
                    //delete confirmation dialog box creation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected product. Are you sure you want to continue?");
                    alert.setTitle("");
                    //calls delete part method
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        Inventory.deleteProduct(delProduct);
                        Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully.");
                        confirmAlert.setTitle("");
                        confirmAlert.setHeaderText("Product Deleted");
                        confirmAlert.showAndWait();
                    }
                }
            }
        }
        catch (NullPointerException e) {
            mainMenuError.setText("ERROR: Please select a valid product for deletion.");
        }

    }

    /**
     * This method exits the program when called.
     * @param actionEvent Exit button
     */
    public void onExitB(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?");
        alert.setTitle("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage exit = (Stage) ExitB.getScene().getWindow();
            exit.close();
        }
    }
}