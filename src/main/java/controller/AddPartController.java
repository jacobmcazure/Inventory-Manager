package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class loads the add part form.
 */
public class AddPartController {
    public Label txtMachineId;
    public TextField apIdTxt;
    public TextField apNameTxt;
    public TextField apInvTxt;
    public TextField apPriceTxt;
    public TextField apMaxTxt;
    public TextField apMachCompTxt;
    public TextField apMinTxt;
    public Label apError;
    @FXML
    private Button addPartCancelB;
    @FXML
    private Button addPartSaveB;
    @FXML
    private RadioButton apInhouseRB;
    @FXML
    private RadioButton apOutsourcedRB;

    /**
     * This method changes scenes to the Mainform.
     * @param event The cancel button
     * @throws IOException
     */
    @FXML
    public void onAddPartCancelB(ActionEvent event) throws IOException {

        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene addPartFormScene = new Scene(mainForm);

        /*
         * gets the Stage information
         */
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartFormScene);
        window.show();
    }

    /**
     * This method saves the new part's data as either an InHouse or Outsourced part depending on the type of data entered.
     * @param event The save button
     * @throws IOException
     */
    @FXML
    public void onAddPartSaveB(ActionEvent event) throws IOException {
        String name = apNameTxt.getText();
        String strInv = apInvTxt.getText();
        String strPrice = apPriceTxt.getText();
        String strMax = apMaxTxt.getText();
        String strMin = apMinTxt.getText();
        String strMach = apMachCompTxt.getText();

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

        if (apInhouseRB.isSelected()) {
            error = "machine ID";
            if(strMach.isBlank()) {
                apError.setText("ERROR: Please enter a valid " + error + ".");
                return;
            }
            int machId = Integer.parseInt(strMach);
            Inventory.addPart(new InHouse(Inventory.getGenPartId(), name, price, inv, min, max, machId));
        }
        else if (apOutsourcedRB.isSelected()) {
            error = "company name";
            if(strMach.isBlank()) {
                apError.setText("ERROR: Please enter a valid " + error + ".");
                return;
            }
            Inventory.addPart(new Outsourced(Inventory.getGenPartId(), name, price, inv, min, max, strMach));
        }

            if (name.isBlank()) {
                apError.setText("ERROR: Please enter a valid name.");
                return;
            }
            //min > max
            if(min > max) {
                apError.setText("ERROR: Min value must be less than \nthe max value.");
                return;
            }
            //min <= stock <= max
            if(min > inv || inv > max) {
                apError.setText("ERROR: Inventory must be between the \nminimum and maximum values.");
                return;
            }

        }
        catch (NumberFormatException e) {
            apError.setText("ERROR: Please enter a \nvalid " + error + ".");
            return;
        }

        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene addPartFormScene = new Scene(mainForm);
        /*
         * gets the Stage information
         */
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartFormScene);
        window.show();
    }

    /**
     * This method selects the InHouse radio button and sets the last text field to Machine id when called.
     * @param actionEvent The InHouse radio button
     */
    @FXML
    public void onApInhouseRB(ActionEvent actionEvent) {
        apInhouseRB.setSelected(true);
        txtMachineId.setText("Machine ID");
    }

    /**
     * This method selects the Outsourced radio button and sets the last text field to Company name when called.
     * @param actionEvent The Outsourced radio button
     */
    @FXML
    public void onApOutsourcedRB(ActionEvent actionEvent) {
        apOutsourcedRB.setSelected(true);
        txtMachineId.setText("Company Name");
    }


}
