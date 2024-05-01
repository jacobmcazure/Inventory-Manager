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
 * This class loads the add product form.
 */
public class AddProductController implements Initializable {
    public TextField aprIdTxt;
    public TextField aprNameTxt;
    public TextField aprInvTxt;
    public TextField aprPriceTxt;
    public TextField aprMaxTxt;
    public TextField aprMinTxt;
    public TextField apPartsSearch;
    public TableView<Part> aprPartsTable;
    public TableView<Part> aprPartsTable1;
    public Label aprError;
    @FXML
    private Button apAddB;
    @FXML
    private Button apCancelB;
    @FXML
    private Button apSaveB;
    @FXML
    private Button removeApB1;
    @FXML
    private TableColumn<Part, Double> partsCpuCol;
    @FXML
    private TableColumn<Part, Double> aprCpuCol1;
    @FXML
    private TableColumn<Part, Integer> partsInvlevelCol;
    @FXML
    private TableColumn<Part, Integer> aprInvlevelCol1;
    @FXML
    private TableColumn<Part, Integer> partsPartidCol;
    @FXML
    private TableColumn<Part, Integer> aprPartidCol1;
    @FXML
    private TableColumn<Part, String> partsPartnameCol;
    @FXML
    private TableColumn<Part, String> aprPartnameCol1;

    //list for the bottom table
    private ObservableList<Part> apList = FXCollections.observableArrayList();

    /**
     * This method adds a selected part from the top table to the bottom table's ObservableList and displays it.
     * @param event The add part button
     */
    @FXML
    public void onApAddB(ActionEvent event) {
        //gets selected item from top table
        Part selPart = aprPartsTable.getSelectionModel().getSelectedItem();
        //null selection check
        if (selPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid part.");
            alert.setTitle("");
            alert.show();
        }
        else {
            apList.add(selPart);
        }

        aprPartsTable1.setItems(apList);
    }

    /**
     * When called, this method takes the user back to the main menu without saving any changes made.
     * @param event The cancel button
     * @throws IOException
     */
    @FXML
    public void onApCancelB(ActionEvent event) throws IOException {

        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene addProductFormScene = new Scene(mainForm);

        /*
         * gets the Stage information
         */
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(addProductFormScene);
        window.show();
    }

    /**
     * This method saves the new product and its data to an ObservableList.
     * RUNTIME ERROR: NullPointerException, did not initialize associatedParts obsList in Products class. Added initialization to fix the problem.
     * @param event The save button
     * @throws IOException
     */
    @FXML
    public void onApSaveB(ActionEvent event) throws IOException {
        //puts parts from the bottom table into temp obsList
        apList = aprPartsTable1.getItems();

        //gets data from text fields
//        String name = aprNameTxt.getText();
//        int stock = Integer.parseInt(aprInvTxt.getText());
//        double price = Double.parseDouble(aprPriceTxt.getText());
//        int max = Integer.parseInt(aprMaxTxt.getText());
//        int min = Integer.parseInt(aprMinTxt.getText());
        String strName = aprNameTxt.getText();
        String strInv = aprInvTxt.getText();
        String strPrice = aprPriceTxt.getText();
        String strMax = aprMaxTxt.getText();
        String strMin = aprMinTxt.getText();

        String error = "";
        try {
            error = "inventory amount";
            int inv = Integer.parseInt(strInv);
            error = "price amount";
            double price = Double.parseDouble(strPrice);
            error = "maximum number";
            int max = Integer.parseInt(strMax);
            error = "minimum number";
            int min = Integer.parseInt(strMin);

            if (strName.isBlank()) {
                aprError.setVisible(true);
                aprError.setText("ERROR: Please enter a valid name.");
                return;
            }
            //min > max
            if(min > max) {
                aprError.setText("ERROR: Min value must be less than \nthe max value.");
                return;
            }
            //min <= stock <= max
            if(min > inv || inv > max) {
                aprError.setText("ERROR: Inventory must be between the \nminimum and maximum values.");
                return;
            }

            Product newPr = new Product(Inventory.getGenProductId(), strName, price, inv, min, max);

            for(Part p : apList)
                newPr.addAssociatedPart(p);

            Inventory.addProduct(newPr);
        }
        catch (NumberFormatException e) {
            aprError.setText("ERROR: Please enter a \nvalid " + error + ".");
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
    public void onRemoveApB1(ActionEvent event) {
        Part removePart = aprPartsTable1.getSelectionModel().getSelectedItem();
        //delete confirmation dialog box creation
        Alert removeAp = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        removeAp.setTitle("");
        removeAp.setHeaderText("Remove associated part");
        //calls delete part method
        Optional<ButtonType> result = removeAp.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            //removes part
            apList.remove(removePart);
            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "Associated part deleted successfully.");
            confirmAlert.setTitle("");
            confirmAlert.setHeaderText("Part Deleted");
            confirmAlert.showAndWait();
        }

    }

    /**
     * This method initializes data of both parts and associated parts table when the add product form is loaded.
     * @param url root path for the main form
     * @param resourceBundle resources used to localize the main menu
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        aprPartsTable.setItems(Inventory.getAllParts());

        partsPartidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsPartnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvlevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCpuCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        aprPartidCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        aprPartnameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        aprInvlevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aprCpuCol1.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method searches through the parts list in the top table by name and id and displays the results.
     * @param actionEvent The enter key on the parts search bar
     */
    public void onApPartsSearch(ActionEvent actionEvent) {
        String pQuery = apPartsSearch.getText();

        ObservableList<Part> parts = Inventory.lookupPart(pQuery);

        if(parts.isEmpty()) {
            aprError.setText("Part not found.");
        }
        else {
            aprError.setText("");
        }
        if(parts.size() == 0) {
            try {
                int pId = Integer.parseInt(pQuery);
                Part p = Inventory.lookupPart(pId);
                if (p != null) {
                    parts.add(p);
                aprError.setText("");
                }
                else {
                    aprError.setText("Part not found.");
                }
            }
            catch (NumberFormatException p) {
                //ignore
            }
        }
        aprPartsTable.setItems(parts);
    }
}
