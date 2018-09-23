/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.spreadsheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.poi.ss.usermodel.DataValidation;
//import org.apache.poi.ss.usermodel.DataValidationConstraint;
//import org.apache.poi.ss.usermodel.DataValidationHelper;
//import org.apache.poi.ss.util.CellRangeAddressList;
//import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;

/**
 * @author Khushal
 */
public class XLSAggregator {

    private String path;
    private FileOutputStream fileOutputStream;
    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;

    public XLSAggregator() {
        this.path = "AT.xlsx";

        try {

            FileOutputStream xlsAggregate = new FileOutputStream(this.path);

            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet("Sheet1");
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XLSAggregator(String path) {
        this.path = path;

        try {

            FileOutputStream xlsAggregate = new FileOutputStream(this.path);

            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet("Sheet1");
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XLSAggregator(String path, String sheet) {
        this.path = path;

        try {

            FileOutputStream xlsAggregate = new FileOutputStream(this.path);

            workbook = new XSSFWorkbook();
            worksheet = workbook.createSheet(sheet);
        } catch (IOException ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    /*
     public boolean createAggregateXLSX(Album album) {

     DataValidation dataValidation = null;
     DataValidationConstraint constraint = null;
     DataValidationHelper validationHelper = null;

     CellRangeAddressList cellRangeAddressList = new CellRangeAddressList();
     validationHelper = new XSSFDataValidationHelper(worksheet);
     constraint = validationHelper.createExplicitListConstraint(new String[]{"Album", "Single", "EP", "MaxiSingle", "Track"});

     for (XSSFRow t : album.tracks) {
     XSSFRow track = worksheet.createRow(worksheet.getLastRowNum());
     copyRow(track, t, workbook, worksheet);


     cellRangeAddressList.addCellRangeAddress(track.getCell(0).getRowIndex(), track.getCell(0).getColumnIndex(), track.getCell(0).getRowIndex(), track.getCell(0).getColumnIndex());
     dataValidation = validationHelper.createValidation(constraint, cellRangeAddressList);
     dataValidation.setSuppressDropDownArrow(true);
     worksheet.addValidationData(dataValidation);

     }
     try {
     //heading = (XSSFRow)header;
     workbook.write(fileOutputStream);
     return true;
     } catch (IOException ex) {
     Logger.getLogger(XLSAggregator.class.getName()).log(Level.SEVERE, null, ex);
     return false;
     }
     }

     */

    public boolean closeFile() {
        try {
            fileOutputStream.close();
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
