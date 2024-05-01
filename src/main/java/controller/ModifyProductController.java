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

import static javafx.collections.FXCollections.observableArrayList;

/**
 * This class loads the modify product form.
 */
public class ModifyProductController implements Initializable {

    public TextField mprIdTxt;
    public TextField mprNameTxt;
    public TextField mprInvTxt;
    public TextField mprPriceTxt;
    public TextField mprMaxTxt;
    public TextField mprMinTxt;
    public TextField mpPartsSearch;
    public TableView<Part> mprPartsTable;
    public TableView<Part> mprPartsTable1;
    public Label mprError;
    @FXML
    private Button mpAddB;
    @FXML
    private Button mpCancelB;
    @FXML
    private Button mpSaveB;
    @FXML
    private Button removeApB2;
    @FXML
    private TableColumn<Part, Double> partsCpuCol;
    @FXML
    private TableColumn<Part, Double> partsCpuCol1;
    @FXML
    private TableColumn<Part, Integer> partsInvlevelCol;
    @FXML
    private TableColumn<Part, Integer> partsInvlevelCol1;
    @FXML
    private TableColumn<Part, Integer> partsPartidCol;
    @FXML
    private TableColumn<Part, Integer> partsPartidCol1;
    @FXML
    private TableColumn<Part, String> partsPartnameCol;
    @FXML
    private TableColumn<Part, String> partsPartnameCol1;

    //list for the bottom table
    private ObservableList<Part> mpList = observableArrayList();

    //holds data of selected part from main menu to perform updates and get id
    private static Product productMod = null;

    /**
     * This method initializes data of both parts and products table when the modify product form is loaded.
     * @param url root path for the main form
     * @param resourceBundle resources used to localize the main menu
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mprPartsTable.setItems(Inventory.getAllParts());

        partsPartidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsPartnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvlevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCpuCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsPartidCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsPartnameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvlevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCpuCol1.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method sends the parts data into their respective text fields from the main menu.
     * LOGICAL ERROR: productMod was null when used in initialize. Moved it to sendProduct method to fix.
     * @param selectedItem The selected product passed over from the main menu
     */
    public void sendProduct(Product selectedItem) {
        productMod = selectedItem;

        mpList = observableArrayList(productMod.getAllAssociatedParts());
        mprPartsTable1.setItems(mpList);

        mprIdTxt.setText(String.valueOf(productMod.getId()));
        mprNameTxt.setText(productMod.getName());
        mprInvTxt.setText(Integer.toString(productMod.getStock()));
        mprPriceTxt.setText(String.valueOf(productMod.getPrice()));
        mprMaxTxt.setText(Integer.toString(productMod.getMax()));
        mprMinTxt.setText(Integer.toString(productMod.getMin()));
    }

    /**
     * This method adds a selected part from the top table to the bottom table's ObservableList and displays it.
     * @param event The add button
     */
    @FXML
    public void onMpAddB(ActionEvent event) {
        //gets selected item from top table
        Part selPart = mprPartsTable.getSelectionModel().getSelectedItem();
        //null selection check
        if (selPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid part.");
            alert.setTitle("");
            alert.show();
        } else {
            mpList.add(selPart);
        }

        mprPartsTable1.setItems(mpList);
    }

    /**
     * When called, this method takes the user back to the main menu without saving any changes made.
     * @param event The cancel button
     * @throws IOException input/output exception
     */
    @FXML
    public void onMpCancelB(ActionEvent event) throws IOException {
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene modifyProductFormScene = new Scene(mainForm);

        /*
         * gets the Stage information
         */
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(modifyProductFormScene);
        window.show();
    }

    /**
     * This method saves the new product and its data to an ObservableList.
     * RUNTIME ERROR: NullPointerException, did not initialize associatedParts obsList in Products class. Added initialization to fix the problem.
     * @param event The save button
     * @throws IOException input/output exception
     */
    @FXML
    public void onMpSaveB(ActionEvent event) throws IOException {
        int indexOfPart = Inventory.getAllProducts().indexOf(productMod);
        int newId = productMod.getId();
        String strName = mprNameTxt.getText();
        String strInv = mprInvTxt.getText();
        String strPrice = mprPriceTxt.getText();
        String strMax = mprMaxTxt.getText();
        String strMin = mprMinTxt.getText();

        if (strName.isBlank()) {
            mprError.setVisible(true);
            mprError.setText("ERROR: Please enter a valid name.");
            return;
        }
        String error = "";
        try {
            error = "inventory amount";
            int inv = Integer.parseInt(strInv);
            error = "price";
            double price = Double.parseDouble(strPrice);
            error = "maximum number";
            int max = Integer.parseInt(strMax);
            error = "minimum number";
            int min = Integer.parseInt(strMin);

            //min > max
            if(min > max) {
                mprError.setText("ERROR: Min value must be less than \nthe max value.");
                return;
            }
            //min <= stock <= max
            if(min > inv || inv > max) {
                mprError.setText("ERROR: Inventory must be between the \nminimum and maximum values.");
                return;
            }

            Product newProduct = new Product(newId, strName, price, inv, min, max, mpList);
            Inventory.updateProduct(indexOfPart, newProduct);

        }
        catch (NumberFormatException e) {
            mprError.setText("ERROR: Please enter a valid " + error + ".");
            return;
        }
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene addPartFormScene = new Scene(mainForm);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartFormScene);
        window.show();
    }

    /**
     * This method removes the selected associated part from the bottom table and displays a pop-up confirmation box.
     * @param event The remove associated part button
     */
    @FXML
    public void onRemoveApB2(ActionEvent event) {
        Part removePart = mprPartsTable1.getSelectionModel().getSelectedItem();
        //delete confirmation dialog box creation
        Alert removeAp = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        removeAp.setTitle("");
        removeAp.setHeaderText("Remove associated part");
        //calls delete part method
        Optional<ButtonType> result = removeAp.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //removes part
            mpList.remove(removePart);

            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "Associated part deleted successfully.");
            confirmAlert.setTitle("");
            confirmAlert.setHeaderText("Part Deleted");
            confirmAlert.showAndWait();
        }
    }

    /**
     * This method searches through the parts list in the top table by name and id and displays the results.
     * @param actionEvent The enter key on the parts search bar
     */
    public void onMpPartsSearch(ActionEvent actionEvent) {
        String pQuery = mpPartsSearch.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pQuery);

        if(parts.isEmpty()) {
            mprError.setText("Part not found.");
        }
        else {
            mprError.setText("");
        }
        if (parts.size() == 0) {
            try {
                int pId = Integer.parseInt(pQuery);
                Part p = Inventory.lookupPart(pId);
                if (p != null) {
                    parts.add(p);
                mprError.setText("");
                }
                else {
                    mprError.setText("Product not found.");
                }
            } catch (NumberFormatException p) {
                //ignore
            }
        }
        mprPartsTable.setItems(parts);
    }
}

