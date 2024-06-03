package com.ced.app.other;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Tools {
    public static String Excel_type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static String NumberFormat(Double d){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String format = nf.format(d);
        return format;
    }
    
    public static String NumberFormat(Long l){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String format = nf.format(l);
        return format;
    }
    
    public static String NumberFormat(Integer i){
        NumberFormat nf = NumberFormat.getInstance();
        String format = nf.format(i);
        return format;
    }
    
    public static void uploadFile(String uploadDir,String fileName,MultipartFile mfile) throws IOException{
        Path uPath=Paths.get(uploadDir);
        if(!Files.exists(uPath)){
            Files.createDirectories(uPath);
        }
        try(InputStream iStream=mfile.getInputStream()){
            Path filePath=uPath.resolve(fileName);
            Files.copy(iStream, filePath,StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException ex){
            throw new IOException("Could not save image",ex);
        }
    }
    
    public static void ExportCSV(Connection connect, String csvFilePath, String tableName) throws SQLException, IOException{
        try (Statement stmt = connect.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM "+tableName); CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeAll(rs, true);
        }       
    }

    private static void writeHeaderLine(ResultSet result, XSSFSheet sheet) throws SQLException {
        // write header line containing column names
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
 
        Row headerRow = sheet.createRow(0);
 
        // exclude the first column which is the ID field
        for (int i = 2; i <= numberOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            Cell headerCell = headerRow.createCell(i - 2);
            headerCell.setCellValue(columnName);
        }
    }
 
    private static void writeDataLines(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet)
            throws SQLException {
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
 
        int rowCount = 1;
 
        while (result.next()) {
            Row row = sheet.createRow(rowCount++);
 
            for (int i = 2; i <= numberOfColumns; i++) {
                Object valueObject = result.getObject(i);
 
                Cell cell = row.createCell(i - 2);
 
                if (valueObject instanceof Boolean)
                    cell.setCellValue((Boolean) valueObject);
                else if (valueObject instanceof Double)
                    cell.setCellValue((double) valueObject);
                else if (valueObject instanceof Float)
                    cell.setCellValue((float) valueObject);
                else if (valueObject instanceof Date) {
                    cell.setCellValue((Date) valueObject);
                    formatDateCell(workbook, cell);
                } else cell.setCellValue((String) valueObject);
 
            }
 
        }
    }
 
    private static void formatDateCell(XSSFWorkbook workbook, Cell cell) {
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        cell.setCellStyle(cellStyle);
    }

    public static void ExportExcel(Connection connection, String filePath, String tablename) throws Exception
    {   
        Statement stmt = null;
        ResultSet rslt = null;
        String query = "select * from " + tablename;
        try {
            stmt = connection.createStatement();
            rslt = stmt.executeQuery(query);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(tablename);
            
            writeHeaderLine(rslt, sheet);
            writeDataLines(rslt, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
 

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            stmt.close();
            rslt.close();
            if(!connection.isClosed())
            {
                connection.close();
            }
        }
        
    }
    
    public static void ImportCSV(Connection connect, String csvFilePath, String tableName, String delimiter) throws SQLException{
        try (Statement stmt = connect.createStatement()) {
            System.out.println("COPY "+tableName+" FROM '"+csvFilePath+"' DELIMITER '"+delimiter+"' CSV HEADER");
            stmt.executeUpdate("COPY "+tableName+" FROM '"+csvFilePath+"' DELIMITER '"+delimiter+"' CSV HEADER");
            stmt.close();
        }   
    }

    // private static String extractColumnInfo(String errorMessage) {
    //     Pattern pattern = Pattern.compile("Où : COPY [^,]+, ligne \\d+, colonne ([^:]+) : .*");
    //     Matcher matcher = pattern.matcher(errorMessage);
    //     if (matcher.find()) {
    //         return "Error occurred in column: " + matcher.group(1);
    //     }
    //     return null;
    // }

    public static HashMap<Integer, String> ImportCsvByLine(Connection connect, String csvFilePath, String tablename, String delimiter, boolean withHeader) throws SQLException, IOException
    {
        HashMap<Integer, String> errors = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            int linenumber = 1;
            if(withHeader)
            {
                reader.readNext(); // Skip the header line
                linenumber = 2;
            }
            
            while ((line = reader.readNext()) != null) {
                try {
                    for (int i = 0; i < line.length; i++) {
                        line[i] = line[i].replace("'", "''"); // Escape single quotes
                        if (line[i].matches("\\d+,\\d+%")) { // Check if the value is a number with a comma
                            System.out.println("mahita % sy virgule");
                            line[i] = line[i].replace(",", ".").replace("%", ""); // Replace comma with dot
                        }
                        if (line[i].matches("\\d+,\\d+")) { // Check if the value is a number with a comma
                            System.out.println("mahita % sy virgule");
                            line[i] = line[i].replace(",", "."); // Replace comma with dot
                        }
                    }
                    String query = "INSERT INTO " + tablename + " VALUES ('" + String.join("','", line) + "')";
                    System.out.println(query);
                    Statement stmt = connect.createStatement();
                    stmt.executeUpdate(query);
                    stmt.close();

                } catch (SQLException e) {
                    // Handle the SQLException
                    // e.printStackTrace();
                    errors.put(linenumber, e.getMessage().replaceAll("Position\\s*", "").replaceAll(":\\s*\\s*\\d+", "")); // Add the error message to the list
                    e.printStackTrace();
                    // continue;

                    //tsy atao throw satria taperina ilay line rehetra
                }
                linenumber++;

            }
        }

        
        System.out.println("size : " + errors.size());
        return errors;
    }

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!Excel_type.equals(file.getContentType())) {
          return false;
        }
    
        return true;
    }

    public static void SendEmail(String from, String to, String subject, String content, String filePath) throws IOException{
        // Paramètres de connexion au serveur SMTP
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "evalcinepax@gmail.com";
        String password = "nqwsvoymzwmwrkwl";

        // Propriétés de configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
            
            // Partie fichier attaché
            if(filePath != null){
                Multipart multipart = new MimeMultipart();
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(new File(filePath));
                multipart.addBodyPart(attachmentPart);
                message.setContent(multipart); 
            }

            // Envoi du message
            Transport.send(message);

            System.out.println("L'e-mail a été envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static boolean columnExists(Connection connect, String tableName, String columnName) throws SQLException
    {
        String checkColumnQuery = "select 1 from information_schema.columns where table_name = ? AND column_name = ?";
        try (PreparedStatement stmt = connect.prepareStatement(checkColumnQuery)){
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            try (ResultSet rs = stmt.executeQuery())
            {
                return rs.next();
            }
        }
    }
    
}
