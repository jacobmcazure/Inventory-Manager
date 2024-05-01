package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    private static int genPartId = 1;
    private static int genProductId = 1;

    /**
     * This method auto-generates a unique part ID when called.
     * @return Returns genPartId
     */
    public static int getGenPartId() {
        return genPartId++;
    }

    /**
     * This method auto-generates a unique product ID when called.
     * @return Returns genProductId
     */
    public static int getGenProductId() {
        return genProductId++;
    }

    /**
     * This method adds a part to the allParts ObservableList.
     * @param newPart The new part
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * This method adds a part to the allProducts ObservableList.
     * @param newProduct The new product
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This method searches through the allParts list by ID.
     * @param partId The part ID
     * @return Returns matching parts, otherwise null
     */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (int j = 0; j < allParts.size(); j++) {
            Part p = allParts.get(j);
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    /**
     * This method searches through the allProducts list by ID.
     * @param productId The product ID
     * @return Returns matching products, otherwise null
     */
    public static Product lookupProduct(int productId){
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (int j = 0; j < allProducts.size(); j++) {
            Product pr = allProducts.get(j);
            if (pr.getId() == productId) {
                return pr;
            }
        }
        return null;
    }

    /**
     * This method searches through the allParts list by name.
     * @param partName The part name
     * @return Returns matching parts, otherwise null
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part p : allParts) {
            if (p.getName().contains(partName)){
                namedParts.add(p);
            }
        }
        return namedParts;
    }

    /**
     * This method searches through the allProducts list by name.
     * @param productName The product name
     * @return Returns matching products, otherwise null
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product pr : allProducts) {
            if (pr.getName().contains(productName)){
                namedProducts.add(pr);
            }
        }
        return namedProducts;
    }

    /**
     * This method updates a part in the allParts ObservableList.
     * @param index The index of the part being updated
     * @param selectedPart The selected part replacing the existing part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method updates a product in the allProducts ObservableList.
     * @param index The index of the product being updated
     * @param newProduct The new product replacing the existing product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * This method deletes a part.
     * @param selectedPart The selected part
     * @return Returns true if the selected part was deleted
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * This method deletes a product.
     * @param selectedProduct The selected product
     * @return Returns true if the selected product was deleted
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * This method retrieves the list of all parts
     * @return Returns the allParts ObservableList
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method retrieves the list of all products
     * @return Returns the allProducts ObservableList
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
