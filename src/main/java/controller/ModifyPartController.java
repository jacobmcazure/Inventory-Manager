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
 * This class loads the modify part form.
 */
public class ModifyPartController {

    public TextField mpIdTxt;
    public TextField mpNameTxt;
    public TextField mpInvTxt;
    public TextField mpPriceTxt;
    public TextField mpMaxTxt;
    public TextField mpMachTxt;
    public TextField mpMinTxt;
    public Label txtMachId;
    public Label mpError;
    @FXML
    private Button modifyPartCancelB;
    @FXML
    private Button modifyPartSaveB;
    @FXML
    private RadioButton mpInhouseRB;
    @FXML
    private RadioButton mpOutsourcedRB;

    //holds data of selected part from main menu to perform updates and get id
    private static Part partMod = null;

    /**
     * This method sends the parts data into their respective text fields from the main menu.
     * @param parts The selected part passed over from the main menu
     */
    public void sendParts(Part parts) {
        partMod = parts;
        mpIdTxt.setText(String.valueOf(parts.getId()));
        mpNameTxt.setText(parts.getName());
        mpInvTxt.setText(Integer.toString(parts.getStock()));
        mpPriceTxt.setText(String.valueOf(parts.getPrice()));
        mpMaxTxt.setText(Integer.toString(parts.getMax()));
        mpMinTxt.setText(Integer.toString(parts.getMin()));

        if(parts instanceof InHouse) {
            mpInhouseRB.setSelected(true);
            txtMachId.setText("Machine ID");
            mpMachTxt.setText(Integer.toString(((InHouse) parts).getMachineId()));
        }
        else
        {
            mpOutsourcedRB.setSelected(true);
            txtMachId.setText("Company Name");
            mpMachTxt.setText(((Outsourced) parts).getCompanyName());
        }

    }

    /**
     * When called, this method takes the user back to the main menu without saving any changes made.
     * @param event The cancel button
     * @throws IOException input/output exception
     */
    @FXML
    public void onModifyPartCancelB(ActionEvent event) throws IOException {
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene modifyPartFormScene = new Scene(mainForm);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyPartFormScene);
        window.show();
    }

    /**
     * This method grabs data from the text fields and updates a part with the new data.
     * @param event The save button
     * @throws IOException input/output exception
     */
    @FXML
    public void onModifyPartSaveB(ActionEvent event) throws IOException {
        int indexOfPart = Inventory.getAllParts().indexOf(partMod);
        int newId = partMod.getId();

        String name = mpNameTxt.getText();
        String strInv = mpInvTxt.getText();
        String strPrice = mpPriceTxt.getText();
        String strMax = mpMaxTxt.getText();
        String strMin = mpMinTxt.getText();
        String strMach = mpMachTxt.getText();


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

            if (name.isBlank()) {
                mpError.setText("ERROR: Please enter a valid name.");
                return;
            }
            //min > max
            if(min > max) {
                mpError.setText("ERROR: Min value must be less than \nthe max value.");
                return;
            }
            //min <= stock <= max
            if(min > inv || inv > max) {
                mpError.setText("ERROR: Inventory must be between the \nminimum and maximum values.");
                return;
            }

            if (mpInhouseRB.isSelected()) {
                error = "machine ID";
                if(strMach.isBlank()) {
                    mpError.setText("ERROR: Please enter a valid " + error + ".");
                    return;
                }
                int machId = Integer.parseInt(strMach);
                InHouse newInhouse = new InHouse(newId, name, price, inv, max, min, machId);
                Inventory.updatePart(indexOfPart, newInhouse);
            }
            else if (mpOutsourcedRB.isSelected()) {
                error = "company name";
                if(strMach.isBlank()) {
                    mpError.setText("ERROR: Please enter a valid " + error + ".");
                    return;
                }
                Outsourced newOutsourced = new Outsourced(newId,name,price,inv,min,max,strMach);
                Inventory.updatePart(indexOfPart, newOutsourced);
            }
        }
        catch (NumberFormatException e) {
            mpError.setText("ERROR: Please enter a \nvalid " + error + ".");
            return;
        }
        Parent mainForm = FXMLLoader.load(getClass().getResource("/mcewen/mainform/Mainform.fxml"));
        Scene modifyPartFormScene = new Scene(mainForm);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyPartFormScene);
        window.show();
    }

    /**
     * This method selects the InHouse radio button and sets the last text field to Machine id when called.
     * @param event The InHouse radio button
     */
    @FXML
    public void onMpInhouseRB(ActionEvent event) {
        mpInhouseRB.setSelected(true);
        txtMachId.setText("Machine ID");
    }

    /**
     * This method selects the Outsourced radio button and sets the last text field to Company name when called.
     * @param event The Outsourced radio button
     */
    @FXML
    public void onMpOutsourcedRB(ActionEvent event) {
        mpOutsourcedRB.setSelected(true);
        txtMachId.setText("Company Name");
    }


}
