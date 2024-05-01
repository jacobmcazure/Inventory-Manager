package mcewen.mainform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.*;


import java.io.IOException;
/**
 * My Javadocs folder is located in this project under the src file.
 */

/**
 * This class loads the main menu.
 * FUTURE ENHANCEMENT: Consider required alert pop-up boxes for delete actions for more practice with alerts.
 * Consider adding a tutorial on how to manage the associated parts list in relation to existing products.
 * Managing it as a first time java student can be tricky sometimes.
 * @author Jacob McEwen
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Mainform.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 390);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /** This is the main method. This is the first method that gets called when you run your java program. */
    public static void main(String[] args) {

        InHouse p = new InHouse(Inventory.getGenPartId(),"Brakes",15.00,10,9,20,9999);
        Inventory.addPart(p);
        Outsourced o = new Outsourced(Inventory.getGenPartId(),"RX handle",29.99,10,2,16,"AMD");
        Inventory.addPart(o);
        Product pr = new Product(Inventory.getGenProductId(),"energy bar",4.99,3,1,10);
        Inventory.addProduct(pr);


        launch();
    }
}