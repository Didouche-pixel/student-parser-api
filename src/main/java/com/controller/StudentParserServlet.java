package com.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Student;
// Retirez cette importation si vous ne l'utilisez plus
// import com.repository.StudentRepository;
import com.service.ExcelParserService;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig
public class StudentParserServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StudentParserServlet.class.getName());
    // Utilisez:
    private final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy")
            .serializeNulls()
            .create();
    private final ExcelParserService parserService = new ExcelParserService();

    // Supprimez cette ligne qui initialise le StudentRepository
    // private final StudentRepository studentRepository = new StudentRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        try {
            // Réponse simplifiée puisque nous n'avons plus de base de données
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(
                    new ApiResponse(true, "API de parsing d'étudiants est opérationnelle!")));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la requête", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(
                    new ApiResponse(false, "Erreur serveur: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        try {
            // Récupérer le fichier Excel envoyé
            Part filePart = request.getPart("file");

            if (filePart == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ApiResponse(false, "Aucun fichier fourni")));
                return;
            }

            String fileName = getSubmittedFileName(filePart);
            if (fileName == null || !(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(
                        new ApiResponse(false, "Format de fichier non supporté. Utilisez .xlsx ou .xls")));
                return;
            }

            try (InputStream fileContent = filePart.getInputStream()) {
                // Parser le fichier
                List<Student> students = parserService.parseExcelFile(fileContent);

                // Supprimer cette section qui tente de sauvegarder en base de données
                /*
                // Sauvegarder les étudiants dans la base de données
                boolean saveSuccess = studentRepository.saveAll(students);

                if (!saveSuccess) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write(gson.toJson(
                            new ApiResponse(false, "Erreur lors de l'enregistrement des étudiants en base de données")));
                    return;
                }
                */

                // Retourner la liste des étudiants en JSON
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(
                        new ApiResponse(true, "Fichier parsé avec succès", students)));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors du parsing du fichier Excel", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson(
                        new ApiResponse(false, "Erreur lors du parsing du fichier: " + e.getMessage())));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la requête", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(
                    new ApiResponse(false, "Erreur serveur: " + e.getMessage())));
        }
    }

    // Classe pour la réponse API
    private static class ApiResponse {
        private final boolean success;
        private final String message;
        private final Object data;

        public ApiResponse(boolean success, String message) {
            this(success, message, null);
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }

    // Méthode pour obtenir le nom du fichier soumis
    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return null;
    }
}