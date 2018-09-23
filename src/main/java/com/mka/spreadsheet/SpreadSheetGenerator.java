/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.spreadsheet;

import com.mka.utils.Constants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Khushal
 */
public class SpreadSheetGenerator {

    private String path;
    private FileOutputStream fileHandle;
    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;

    public SpreadSheetGenerator() {
        this.path = "Meta.xlsx";

        try {

            fileHandle = new FileOutputStream(this.path);
            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet("Sheet1");
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SpreadSheetGenerator(String path) {
        this.path = path;

        try {

            fileHandle = new FileOutputStream(this.path);

            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet("Sheet1");
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SpreadSheetGenerator(String path, String sheet) {
        this.path = path;

        try {

            fileHandle = new FileOutputStream(this.path);

            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet(sheet);
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SpreadSheetGenerator(boolean isStream) {
        workbook = new XSSFWorkbook();
        worksheet = workbook.createSheet("Sheet1");
    }

    public boolean selectSheet(String sheetName) {
        try {
            worksheet = workbook.getSheet(sheetName);
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileOutputStream getFileHandle() {
        return fileHandle;
    }

    public void setFileHandle(FileOutputStream fileHandle) {
        this.fileHandle = fileHandle;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public XSSFSheet getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(XSSFSheet worksheet) {
        this.worksheet = worksheet;
    }

    public void createRowAtEnd(Object[] rowData) {
        createRowAtIthPosition(worksheet.getLastRowNum() + 1, rowData);
    }

    public void createRowAtIthPosition(int position, Object[] rowData) {
        //System.out.println("Position Cursor : " + position);
        XSSFRow row = worksheet.createRow(position);
        for (Object cellData : rowData) {
            createCellAtRowEnd(row, cellData);
        }
    }

    public void createCellAtRowEnd(XSSFRow row, Object cellData) {
        createCellAtIthPosition(row.getLastCellNum(), row, cellData);
    }

    public void createCellAtIthPosition(int position, XSSFRow row, Object cellData) {
        XSSFCell cell;
        if (row.getLastCellNum() == -1) {
            cell = row.createCell(0);
        } else {
            cell = row.createCell(position);
        }

        switch (cellData.getClass().getSimpleName()) {
            case "Integer":
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Integer) cellData);
                break;
            case "Double":
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Double) cellData);
                break;
            case "String":
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue((String) cellData);
                break;
            case "Boolean":
                cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
                cell.setCellValue((Boolean) cellData);
                break;
            default:
                cell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                cell.setCellValue((String) cellData);
        }

    }

    public boolean flushOutput() {
        try {
            workbook.write(fileHandle);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SpreadSheetGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean closeFile() {
        try {
            fileHandle.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private static void copyRow(XSSFRow newRow, XSSFRow sourceRow, XSSFWorkbook wb, XSSFSheet ws) {

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }
    }

}
