package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import model.ItemData;

public class ItemManagementFormController {

    @FXML
    private TableColumn<Item, String> col_Desc;

    @FXML
    private TableColumn<Item, String> col_ItemId;

    @FXML
    private TableColumn<Item, String> col_ItemName;

    @FXML
    private TableColumn<Item, Double> col_Price;

    @FXML
    private TableColumn<Item, Integer> col_Qty;

    @FXML
    private TextField txt_Desc;

    @FXML
    private TextField txt_ItemId;

    @FXML
    private TextField txt_ItemName;

    @FXML
    private TextField txt_Price;

    @FXML
    private TextField txt_Qty;

    @FXML
    private TextField txt_Search;

    @FXML
    private TableView<Item> tbl_Items;

    private ObservableList<Item> itemObservableList;

    @FXML
    void btn_AddOnAction(ActionEvent event) {

        String name = txt_ItemName.getText();
        int qty = Integer.parseInt(txt_Qty.getText());
        double price = Double.parseDouble(txt_Price.getText());
        String desc = txt_Desc.getText();

        Item item = new Item(name, qty, price, desc);
        ItemData.itemList.add(item);
        itemObservableList.add(item);

        clearFields();
        txt_ItemId.setText(generateItemId());
        alert("Item added successfully.", Alert.AlertType.INFORMATION);

    }

    @FXML
    void btn_ClearOnAction(ActionEvent event) {

        clearFields();
        txt_ItemId.setText(generateItemId());

    }

    @FXML
    void btn_RefreshOnAction(ActionEvent event) {
        // Table shows all items from the original list
        itemObservableList = FXCollections.observableArrayList(ItemData.itemList);
        tbl_Items.setItems(itemObservableList);

        // Clear selection and input fields
        tbl_Items.getSelectionModel().clearSelection();
        txt_ItemId.clear();
        txt_ItemName.clear();
        txt_Qty.clear();
        txt_Price.clear();
        txt_Desc.clear();
        txt_Search.clear();
    }


    @FXML
    void btn_RemoveOnAction(ActionEvent event) {

        Item selected = tbl_Items.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ItemData.itemList.remove(selected);
            itemObservableList.remove(selected);
            clearFields();
            alert("Item removed.", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void btn_UpdateOnAction(ActionEvent event) {

        String id = txt_ItemId.getText();
        for (Item item : ItemData.itemList) {
            if (String.valueOf(item.getId()).equals(id)) {
                item.setName(txt_ItemName.getText());
                item.setQty(Integer.parseInt(txt_Qty.getText()));
                item.setPrice(Double.parseDouble(txt_Price.getText()));
                item.setDesc(txt_Desc.getText());
                tbl_Items.refresh();
                alert("Item updated.", Alert.AlertType.INFORMATION);
                break;
            }
        }

    }

    @FXML
    void btn_SearchOnAction(ActionEvent event) {
        String searchText = txt_Search.getText().toLowerCase();
        ObservableList<Item> filteredList = FXCollections.observableArrayList();

        for (Item item : ItemData.itemList) {
            if (item.getName().toLowerCase().contains(searchText)) {
                filteredList.add(item);
            }
        }

        tbl_Items.setItems(filteredList);

        // Select the first matching item to trigger text field update
        if (!filteredList.isEmpty()) {
            tbl_Items.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void initialize() {
        col_ItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_ItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_Qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_Desc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        itemObservableList = FXCollections.observableArrayList(ItemData.itemList);
        tbl_Items.setItems(itemObservableList);

        txt_ItemId.setText(generateItemId());

        tbl_Items.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txt_ItemId.setText(String.valueOf(newSelection.getId()));
                txt_ItemName.setText(newSelection.getName());
                txt_Qty.setText(String.valueOf(newSelection.getQty()));
                txt_Price.setText(String.valueOf(newSelection.getPrice()));
                txt_Desc.setText(newSelection.getDesc());
            }
        });

    }

    private void clearFields() {
        txt_ItemId.clear();
        txt_ItemName.clear();
        txt_Qty.clear();
        txt_Price.clear();
        txt_Desc.clear();
        txt_Search.clear();
    }

    private void fillForm(Item item) {
        txt_ItemId.setText(String.format("%04d", item.getId()));
        txt_ItemName.setText(item.getName());
        txt_Qty.setText(String.valueOf(item.getQty()));
        txt_Price.setText(String.valueOf(item.getPrice()));
        txt_Desc.setText(item.getDesc());
    }

    private void alert(String msg, Alert.AlertType type){
        new Alert(type, msg).show();
    }

    private String generateItemId(){
        return String.format("%04d", Item.getNextId());
    }

}
