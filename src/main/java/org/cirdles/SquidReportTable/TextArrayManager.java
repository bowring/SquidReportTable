/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.SquidReportTable;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.cirdles.SquidReportTable.utilities.StringComparer;

/**
 *
 * @author ryanb
 */
public class TextArrayManager {

    private TableView<ObservableList<String>> table;
    private TableView<ObservableList<String>> boundCol;
    private String[][] array;
    private ObservableList<ObservableList<String>> accepted;
    private ObservableList<ObservableList<String>> rejected;
    private String colStyle;
    private final int characterSize;

    public TextArrayManager(TableView<ObservableList<String>> boundCol, TableView<ObservableList<String>> table, String[][] array) {
        this.boundCol = boundCol;
        this.table = table;
        this.array = array;
        accepted = FXCollections.observableArrayList();
        rejected = FXCollections.observableArrayList();
        colStyle = "-fx-font-family: \"Courier New\";"
                + "\n -fx-font-size: 18";
        characterSize = 12;
    }

    public void setHeaders() {
        table.getColumns().clear();
        TableColumn<ObservableList<String>, String> header = new TableColumn<>("");
        for (int i = 3; i < array[0].length - 1; i++) {
            if (!array[0][i - 1].equals(array[0][i])) {
                header = new TableColumn<>(array[0][i].trim());
            }
            String colName = getColumnName(i, array);
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName.trim());
            col.setComparator(new StringComparer());
            col.setPrefWidth(col.getPrefWidth() + 20);
            final int colNum = i - 2;
            col.setStyle(colStyle);
            col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(colNum)));
            header.getColumns().add(col);
            if (!array[0][i].equals(array[0][i + 1])) {
                table.getColumns().add(header);
            }
        }
        setUpBoundCol();
    }

    public String getColumnName(int col, String[][] textArray) {
        String retVal = textArray[1][col] + "\n" + textArray[2][col] + "\n" + textArray[3][col];
        
        retVal = retVal.replaceAll("\\?", "σ");

        return retVal;
    }

    public void setTableItems() {
        accepted.clear();
        rejected.clear();
        int startSpot = Integer.parseInt(array[0][0]);
        for (int i = startSpot; i < array.length; i++) {
            ObservableList<String> data = FXCollections.observableArrayList();
            for (int j = 2; j < array[0].length - 1; j++) {
                data.add(array[i][j].trim());
            }
            if (Boolean.parseBoolean(array[i][0])) {
                accepted.add(data);
            } else {
                rejected.add(data);
            }
        }
    }

    private void setUpBoundCol() {
        boundCol.getColumns().clear();
        TableColumn<ObservableList<String>, String> header = new TableColumn<>("Squid");
        TableColumn<ObservableList<String>, String> col = new TableColumn<>(getColumnName(2, array));
        col.setComparator(new StringComparer());
        col.setPrefWidth(col.getPrefWidth() + 76);
        col.setStyle(colStyle);
        final int num = 0;
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(num)));
        header.getColumns().add(col);
        header.setPrefWidth(header.getMaxWidth());
        boundCol.getColumns().add(header);
    }

    public void setAccepted() {
        boundCol.setItems(accepted);
        table.setItems(accepted);
        setUpWidths(accepted);
    }

    public void setRejected() {
        boundCol.setItems(rejected);
        table.setItems(rejected);
        setUpWidths(rejected);
    }

    public void setUpWidths(ObservableList<ObservableList<String>> data) {
        if (!data.isEmpty()) {
            int header = 0, counter = 0;
            for (int j = 1; j < data.get(0).size(); j++) {
                int max = 0;
                for (int i = 0; i < data.size(); i++) {
                    int length = data.get(i).get(j).length();
                    if (length > max) {
                        max = length;
                    }
                }
                if (table.getColumns().get(header).getColumns().size() <= counter) {
                    header++;
                    counter = 0;
                }
                Object col = table.getColumns().get(header).getColumns().get(counter);
                double prefWidth = max * characterSize;
                if (prefWidth > ((TableColumn) col).getPrefWidth()) {
                    ((TableColumn) col).setPrefWidth(prefWidth);
                }
                counter++;
            }
        }
    }

    public TableView<ObservableList<String>> getBoundCol() {
        return boundCol;
    }

    public void setBoundCol(TableView<ObservableList<String>> boundCol) {
        this.boundCol = boundCol;
    }

    public TableView<ObservableList<String>> getTable() {
        return table;
    }

    public void setTable(TableView<ObservableList<String>> table) {
        this.table = table;
    }

    public String[][] getArray() {
        return array;
    }

    public void setArray(String[][] array) {
        this.array = array;
    }

    public ObservableList<ObservableList<String>> getAccepted() {
        return accepted;
    }

    public void setAccepted(ObservableList<ObservableList<String>> accepted) {
        this.accepted = accepted;
    }

    public ObservableList<ObservableList<String>> getRejected() {
        return rejected;
    }

    public void setRejected(ObservableList<ObservableList<String>> rejected) {
        this.rejected = rejected;
    }

    public String getColStyle() {
        return colStyle;
    }

    public void setColStyle(String colStyle) {
        this.colStyle = colStyle;
    }
}
