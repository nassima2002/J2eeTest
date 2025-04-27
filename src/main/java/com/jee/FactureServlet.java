package com.jee;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class FactureServlet extends HttpServlet {

    // Simuler un stockage avec juste une Map simple en mémoire (ID -> Texte)
    private static Map<Integer, String> factures = new HashMap<>();
    private static int nextId = 1;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String client = request.getParameter("client");
        String total = request.getParameter("total");

        if (client == null || total == null || client.isEmpty() || total.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Client and Total are required.");
            return;
        }

        // Stocker la facture sous forme de texte
        factures.put(nextId, "Client: " + client + ", Total: " + total);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Facture created with ID: " + nextId);
        nextId++;
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (factures.isEmpty()) {
            out.write("No factures found.");
            return;
        }

        for (Map.Entry<Integer, String> entry : factures.entrySet()) {
            out.println("ID: " + entry.getKey() + ", " + entry.getValue());
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String client = request.getParameter("client");
        String total = request.getParameter("total");

        if (idStr == null || client == null || total == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID, client and total are required.");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            if (factures.containsKey(id)) {
                factures.put(id, "Client: " + client + ", Total: " + total);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Facture updated successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Facture not found.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid ID format.");
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID is required.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            if (factures.remove(id) != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Facture deleted successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Facture not found.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid ID format.");
        }
    }
}
