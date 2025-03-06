package com.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.model.Student;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelParserService {
    private static final Logger LOGGER = Logger.getLogger(ExcelParserService.class.getName());
    private String defaultExcelPath;

    public ExcelParserService() {
        // Chargement du chemin du fichier Excel à partir de sql.properties
        try (InputStream input = ExcelParserService.class.getClassLoader().getResourceAsStream("sql.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                LOGGER.log(Level.WARNING, "Impossible de trouver sql.properties");
                return;
            }

            prop.load(input);
            defaultExcelPath = prop.getProperty("excel.file.path");
            LOGGER.log(Level.INFO, "Chemin du fichier Excel par défaut chargé: " + defaultExcelPath);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de sql.properties", ex);
        }
    }

    // Méthode pour parser le fichier Excel par défaut
    public List<Student> parseDefaultExcelFile() throws Exception {
        if (defaultExcelPath == null || defaultExcelPath.isEmpty()) {
            throw new FileNotFoundException("Chemin du fichier Excel par défaut non spécifié");
        }

        // Essayer de charger depuis les ressources
        try (InputStream inputStream = ExcelParserService.class.getClassLoader().getResourceAsStream(defaultExcelPath)) {
            if (inputStream != null) {
                return parseExcelFile(inputStream);
            }
        }

        // Si ça échoue, essayer de charger depuis le système de fichiers
        File file = new File(defaultExcelPath);
        if (!file.exists()) {
            throw new FileNotFoundException("Fichier Excel par défaut non trouvé: " + defaultExcelPath);
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return parseExcelFile(fileInputStream);
        }
    }

    public List<Student> parseExcelFile(InputStream excelFileStream) throws Exception {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(excelFileStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Première feuille

            // Récupérer la ligne d'en-tête
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());

            // Trouver les indices des colonnes nécessaires
            int nomIndex = -1;
            int prenomIndex = -1;
            int dateNaissanceIndex = -1;
            int adresseIndex = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String headerValue = cell.getStringCellValue().trim().toLowerCase();
                    if (headerValue.equals("nom")) {
                        nomIndex = i;
                    } else if (headerValue.equals("prénom")) {
                        prenomIndex = i;
                    } else if (headerValue.equals("date de naissance")) {
                        dateNaissanceIndex = i;
                    } else if (headerValue.equals("adresse")) {
                        adresseIndex = i;
                    }
                }
            }

            // Parcourir toutes les lignes de données (en ignorant l'en-tête)
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Student student = new Student();

                // Extraire les données
                if (nomIndex >= 0) {
                    student.setLastName(getCellValueAsString(row.getCell(nomIndex)));
                }

                if (prenomIndex >= 0) {
                    student.setFirstName(getCellValueAsString(row.getCell(prenomIndex)));
                }

                if (dateNaissanceIndex >= 0) {
                    Cell birthDateCell = row.getCell(dateNaissanceIndex);
                    if (birthDateCell != null) {
                        try {
                            if (birthDateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(birthDateCell)) {
                                student.setBirthDate(birthDateCell.getDateCellValue());
                            } else {
                                String dateStr = getCellValueAsString(birthDateCell);
                                if (dateStr != null && !dateStr.isEmpty()) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    student.setBirthDate(dateFormat.parse(dateStr));
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Erreur lors de la conversion de la date de naissance", e);
                        }
                    }
                }

                if (adresseIndex >= 0) {
                    student.setAddress(getCellValueAsString(row.getCell(adresseIndex)));
                }

                // Génération d'un identifiant unique
                String studentNumber = "E" + System.currentTimeMillis() % 10000 + i;
                student.setStudentNumber(studentNumber);

                students.add(student);
            }
        }

        return students;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}