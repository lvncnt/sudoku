package com.fgl.sudoku;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class provides a view for Sudoku puzzles.
 * Digit positions are specified by column and
 * row indices between 1 (top / left)
 * and 9 (bottom / right).
 *
 * @author Ruediger Lunde
 */
public class SudokuViewCtrl {
    private List<ComboBox<String>> combos = new ArrayList<>(81);

    /**
     * Adds a grid pane with combo boxes to the provided root pane and returns
     * a controller class instance containing application logic.
     */
    public SudokuViewCtrl(BorderPane viewRoot) {
        // create grid layout
        GridPane gridPane = new GridPane();
        viewRoot.setCenter(gridPane);
        gridPane.maxWidthProperty().bind(viewRoot.widthProperty().subtract(20));
        gridPane.maxHeightProperty().bind(viewRoot.heightProperty().subtract(10));
        for (int i = 0; i < 9; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / 9);
            rc.setValignment(i % 3 == 1 ? VPos.CENTER : (i % 3) == 0 ? VPos.BOTTOM : VPos.TOP);
            gridPane.getRowConstraints().add(rc);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 9);
            cc.setHalignment(i % 3 == 1 ? HPos.CENTER : (i % 3) == 0 ? HPos.RIGHT : HPos.LEFT);
            gridPane.getColumnConstraints().add(cc);
        }

        List<String> values = new ArrayList<>();
        values.add("");
        IntStream.range(1, 10).boxed().map(String::valueOf).forEach(values::add);

        for (int i = 0; i < 81; i++) {
            ComboBox<String> combo = new ComboBox<>();
            combo.getItems().addAll(values);
            combo.getSelectionModel().select(0);
            combos.add(combo);
            gridPane.add(combo, i % 9, i / 9);
        } 
    }

    public void clear(boolean allDigits) {
        for (int i = 0; i < 81; i++) {
            ComboBox<String> combo = combos.get(i);
            if (allDigits){
                combo.getSelectionModel().select(0);
            }
        }
    }

    public void clearDigit(int row, int col) {
        getCombo(row, col).getSelectionModel().select(0);
    }

    public void setDigit(int row, int col, int digit) {
        getCombo(row, col).getSelectionModel().select(digit);
    }

    public void fixDigit(int row, int col, int digit) {
        getCombo(row, col).getSelectionModel().select(digit + 9);
    }

    /** Return a digit between 1 and 9 or -1 if no value has been selected yet. */
    public int getDigit(int row, int col) {
        int selIdx = getCombo(row, col).getSelectionModel().getSelectedIndex();
        if (selIdx == 0){
            return -1;
        } else if (selIdx < 10){
            return selIdx;
        }else{
            return selIdx - 9;
        }

    }

    public void setBoard(String board) {
        clear(true);
        for (int i = 0; i < board.length(); i++) {
            char ch = board.charAt(i);
            if (ch >= '1' && ch <= '9'){
                setDigit(i / 9 + 1, i % 9 + 1, ch - '0');
            }
        }
    }

    public String getBoard(){
        String board = "";
        for (int i = 1; i <= 9; i ++){
            for (int j = 1; j <= 9; j ++){
                int digit = getDigit(i, j);
                board += (digit == -1 ? ".":digit);
            }
        }
        return board;
    } 

    private ComboBox<String> getCombo( int row, int col) {
        assert col >= 1 && col <= 9;
        assert row >= 1 && row <= 9;
        return combos.get((row-1) * 9 + (col-1));
    }
}
