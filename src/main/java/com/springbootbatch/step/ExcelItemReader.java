package com.springbootbatch.step;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.springbootbatch.entities.Person;

public class ExcelItemReader implements ItemReader<Person> {

    private String filename;

    /**
     * Sets the filename of the resource to be read.
     * 
     * @param filename the name of the file to be read
     * @throws IllegalArgumentException if the file cannot be opened or closed
     */
    public void setFilename(String filename) {
        this.filename = filename;
        Resource resource = new ClassPathResource(filename);
        try {
            resource.getInputStream().close(); // Intenta abrir y cerrar el recurso para verificar si existe
        } catch (IOException e) {
            throw new IllegalArgumentException("No se puede abrir el archivo: " + filename, e);
        }
    }

    private Iterator<Row> rowIterator;
    private Map<String, Integer> columnMap = new HashMap<>();
    private boolean headerProcessed = false;

    /**
     * Represents a person with cardId, name, lastName, and age.
     */
    @Override
    public Person read() throws Exception {
        if (rowIterator == null) {
            init();
        }

        if (!rowIterator.hasNext()) {
            return null;
        }

        Row row = rowIterator.next();

        if (!headerProcessed) {
            for (Cell cell : row) {
                columnMap.put(cell.getStringCellValue(), cell.getColumnIndex());
            }
            headerProcessed = true;
            if (!rowIterator.hasNext()) {
                return null;
            }
            row = rowIterator.next();
        }

        Person person = new Person();
        person.setCardId(getCellValueAsString(row.getCell(columnMap.get("cardId"))));
        person.setName(getCellValueAsString(row.getCell(columnMap.get("name"))));
        person.setLastName(getCellValueAsString(row.getCell(columnMap.get("lastName"))));
        person.setAge(getCellValueAsInt(row.getCell(columnMap.get("age"))));

        return person;
    }

    /**
     * Initializes the ExcelItemReader by loading the specified Excel file and
     * setting up the row iterator.
     *
     * @throws Exception if the filename is null or empty, or if there is an error
     *                   while reading the Excel file.
     */
    private void init() throws Exception {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalStateException("Filename is null");
        }
        try (InputStream is = new ClassPathResource(filename).getInputStream();
                Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            this.rowIterator = sheet.iterator();
        }
    }

    /**
     * Returns the cell value as a String.
     * 
     * @param cell the cell to retrieve the value from
     * @return the cell value as a String, or null if the cell is null or the cell
     *         type is not supported
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                // Convert the numeric value to String
                return String.valueOf((int) cell.getNumericCellValue());
            }
        } else {
            // Handle other cell types if necessary
            return null;
        }
    }

    /**
     * Returns the cell value as an integer.
     * If the cell is null, returns 0.
     * If the cell type is numeric, returns the numeric value as an integer.
     * If the cell type is not numeric, returns 0.
     *
     * @param cell the cell to get the value from
     * @return the cell value as an integer
     */
    private int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else {
            // Handle other cell types if necessary
            return 0;
        }
    }
}
