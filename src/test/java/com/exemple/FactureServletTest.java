package com.exemple;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.jee.FactureServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.servlet.http.*;
import java.io.*;

public class FactureServletTest {

    @InjectMocks
    private FactureServlet factureServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(writer);
    }
    @Test
    public void testCreateFacture() throws Exception {
        when(request.getParameter("client")).thenReturn("John Doe");
        when(request.getParameter("total")).thenReturn("100");

        factureServlet.doPost(request, response);

        writer.flush();
        String result = outputStream.toString().trim();
        assertTrue(result.startsWith("Facture created with ID:"));
    }
    @Test
    public void testGetAllFactures() throws Exception {
        // D'abord créer une facture pour avoir quelque chose à récupérer
        when(request.getParameter("client")).thenReturn("Jane Doe");
        when(request.getParameter("total")).thenReturn("200");
        factureServlet.doPost(request, response);
        // Réinitialiser le flux de sortie pour GET
        outputStream.reset();
        writer = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(writer);
        // Maintenant tester GET
        factureServlet.doGet(request, response);
        writer.flush();
        String result = outputStream.toString().trim();
        assertTrue(result.contains("Client: Jane Doe"));
    }

    @Test
    public void testUpdateFacture() throws Exception {
        // D'abord créer une facture
        when(request.getParameter("client")).thenReturn("Old Client");
        when(request.getParameter("total")).thenReturn("300");
        factureServlet.doPost(request, response);

        // Réinitialiser le flux de sortie pour PUT
        outputStream.reset();
        writer = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(writer);

        // Maintenant mettre à jour la facture avec id=1
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("client")).thenReturn("New Client");
        when(request.getParameter("total")).thenReturn("500");

        factureServlet.doPut(request, response);

        writer.flush();
        String result = outputStream.toString().trim();
        assertEquals("Facture updated successfully.", result);
    }
    @Test
    public void testDeleteFacture() throws Exception {
        // D'abord créer une facture
        when(request.getParameter("client")).thenReturn("Client To Delete");
        when(request.getParameter("total")).thenReturn("400");
        factureServlet.doPost(request, response);

        // Réinitialiser le flux de sortie pour DELETE
        outputStream.reset();
        writer = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(writer);

        // Maintenant supprimer la facture avec id=1
        when(request.getParameter("id")).thenReturn("1");

        factureServlet.doDelete(request, response);

        writer.flush();
        String result = outputStream.toString().trim();
        assertEquals("Facture deleted successfully.", result);
    }

    @Test
    public void testDeleteFactureNotFound() throws Exception {
        // Essayer de supprimer une facture qui n'existe pas
        when(request.getParameter("id")).thenReturn("99");

        factureServlet.doDelete(request, response);

        writer.flush();
        String result = outputStream.toString().trim();
        assertEquals("Facture not found.", result);
    }
}
