package com.exemple;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.jee.HelloServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.*;
import java.io.*;

@ExtendWith(MockitoExtension.class)
class HelloServletTest {

    @InjectMocks
    private HelloServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testHelloServlet() throws Exception {
        // Préparer le PrintWriter simulé
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        // Quand response.getWriter() est appelé, retourne notre writer simulé
        when(response.getWriter()).thenReturn(writer);

        // Appeler la méthode doGet ou doPost de ton servlet
        servlet.doGet(request, response);

        writer.flush(); // important !

        // Vérifie si le contenu est celui attendu
        assertTrue(stringWriter.toString().toLowerCase().contains("hello world"));
    }
}
