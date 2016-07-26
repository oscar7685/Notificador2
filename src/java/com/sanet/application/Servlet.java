/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanet.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 *
 * @author Oscar
 */
public class Servlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final Logger LOGGER = Logger.getLogger(Servlet.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Calendar f = Calendar.getInstance();
        String dia = (f.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + f.get(Calendar.DAY_OF_MONTH) : "" + f.get(Calendar.DAY_OF_MONTH));
        int mesInt = f.get(Calendar.MONTH) + 1;
        String mes = (mesInt < 10 ? "0" + mesInt : "" + mesInt);
        String anio = "" + f.get(Calendar.YEAR);
        String fecha = dia + "." + mes + "." + anio;
        int ultimoPost = 0;
        String cadena;
        File file  = new File("C:\\ultimopost.txt");
        FileReader fr = new FileReader(file);
        BufferedReader b = new BufferedReader(fr);
        while ((cadena = b.readLine()) != null) {
            ultimoPost = Integer.parseInt(cadena);
        }
        b.close();

        try {
            AlexaSEO obj = new AlexaSEO();
            Document doc = Jsoup.connect("http://softarchive.la/" + fecha + "/allnews/")
                    .data("query", "Java")
                    .userAgent("Mozilla/5.0")
                    .cookie("auth", "token")
                    .timeout(5000)
                    .post();

            //DEJAMOS LAS APLICACIONES DE LA PRIMERA PAGINA
            for (Element element : doc.getElementsByClass("filerow")) {
                if (element.children().first().getElementsByClass("applications-category-icon").isEmpty()
                        || !element.children().first().getElementsByClass("android-subcategory-icon").isEmpty()) {
                    element.remove();
                }
            }
            int numP = 500;


            //AVERIGUA EL RANKING DE ALEXA
            for (Element element : doc.getElementsByClass("date_lister")) {
                while (element != null && !element.tagName().equalsIgnoreCase("h3")) {
                    element = element.nextElementSibling();
                }
                String numPost = element.html().substring(element.html().lastIndexOf(" ")).trim();
                element.html("<h4>Alexa Rank: " + obj.getAlexaRanking("fullprogramasmediafire.blogspot.com") + "</h4>");

                numP = Integer.parseInt(numPost);
            }
            //AVERIGUA EL RANKING DE ALEXA Y AVERIGUA CUANTOS POST HAY


            if (numP % 50 != 0) {
                numP = (numP / 50) + 1;
            } else {
                numP = (numP / 50);
            }
            for (int i = 2; i < numP; i++) {

                Document aux = Jsoup.connect("http://softarchive.la/" + fecha + "/allnews/page-" + i + "/")
                        .data("query", "Java")
                        .userAgent("Mozilla/5.0")
                        .cookie("auth", "token")
                        .timeout(5000)
                        .post();

                int auxInt = 0;

                for (Element element2 : aux.getElementsByClass("filerow")) {
                    if (!element2.children().first().getElementsByClass("applications-category-icon").isEmpty()
                            && element2.children().first().getElementsByClass("android-subcategory-icon").isEmpty()) {
                        auxInt = Integer.parseInt(element2.attr("data-news-id").toString());
                        if (auxInt > ultimoPost) {
                            doc.select("tbody").append(element2.outerHtml());
                        } else {
                            break;
                        }
                    }
                }
            }

            Notificador n = new Notificador();
            String titulo, url;
            int auxInt2 = 0;
            boolean primer = true;
            for (Element element2 : doc.getElementsByClass("filerow")) {
                auxInt2 = Integer.parseInt(element2.attr("data-news-id").toString());
                titulo = element2.children().first().getElementsByClass("cellMainLink").text();
                url = element2.children().first().getElementsByClass("cellMainLink").attr("href");
                if (primer) {
                    //SETEAMOS EL ULTIMO POST VISTO EN EL SERVIDOR
                    // NewServletListener.setUltimoPost(auxInt2);
                    Writer output = new BufferedWriter(new FileWriter(file));
                    try {
                        output.write(""+auxInt2);
                    } finally {
                        output.close();
                    }
                    primer = false;
                }

                if (auxInt2 > ultimoPost) {
                    n.enviarNotificacion(titulo, titulo, "" + auxInt2, url);
                }
            }
            out.print("ULTIMO POST:" + ultimoPost + " androidDevice: " + NewServletListener.getANDROID_DEVICEO());
        } catch (Exception e) {
            out.print("ha ocurrido un error " + e.toString());
            LOGGER.error("ha ocurrido un error", e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
