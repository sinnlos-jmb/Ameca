package ameca;



/**
 *
 * @author manu
 */

//import com.mysql.cj.util.StringUtils;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// PDF

import java.net.URLEncoder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.pdfl;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.draw.LineSeparator;
//import com.itextpdf.text.pdf.draw.DottedLineSeparator;
//import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.codec.PngImage;
//import com.itextpdf.text.pdf.codec.wmf.;
import com.itextpdf.text.pdf.BaseFont;
import java.io.File;
//import com.itextpdf.awt.AsianFontMapper;
//import com.itextpdf.awt.DefaultFontMapper;







// Excel
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;


//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;



//import manu.utils.*;

public class Reportes extends HttpServlet
{  

    HTML htm=new HTML();
    
    private static final String FILE = "/usr/share/tomcat8-ameca/ameca/Reportes/";
    public static final String IMG = "/usr/share/tomcat8-ameca/ameca/imgs/ok_big.png"; 
    public static final String IMG_logo = "/usr/share/tomcat8-ameca/ameca/imgs/ameca1_ok.png";  
    public static final String IMG_pf = "/usr/share/tomcat8-ameca/ameca/imgs/pagofacilOK2.png";  
    
    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font small = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    //private static final Font chino = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD);
//        BaseFont bfComic = BaseFont.createFont("C:\\Windows\\Fonts\\arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
 //       private static final Font chino =new Font ("/usr/share/tomcat8-ameca/ameca/Styles/ARIALUNI.TTF", 12, BaseFont.EMBEDDED);
    
 //   DefaultFontMapper mapper = new com.itextpdf.awt.AsianFontMapper(
   //        AsianFontMapper.ChineseTraditionalFont_MSung,
   //        AsianFontMapper.ChineseTraditionalEncoding_H ); 
    
    private static BaseFont bfComic ;
    private static Font chino;
    private static Font chino_tit;
    private static Font chino_b;
    private static Font chino_s;
    private static Font chino_ss;
    

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }
    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException

   {  //    htmls.logger.fine("homeOsoc. Carga servlet\n--");
   
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    
    if (!HTML.getIgnited_localidades())
         HTML.Carga_localidades();
    if (!HTML.getIgnited_condiciones_iva())
         HTML.Carga_condiciones_iva();
    if (!HTML.getIgnited_condiciones_iibb())
         HTML.Carga_condiciones_iibb();
    if (!HTML.getIgnited_periodo())
         htm.Carga_periodo();
    if (!HTML.getIgnited_categorias_autonomo())
         HTML.Carga_categorias_autonomo();    
    if (!HTML.getIgnited_categorias_monotributo())
         HTML.Carga_categorias_monotributo();

    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String op2  = request.getParameter ("op2") != null ?  request.getParameter ("op2") : "" ;

    String id_establecimiento  = request.getParameter ("id_establecimiento") != null ?  request.getParameter ("id_establecimiento") : "0" ;
    String nombre_responsable  = request.getParameter ("nombre_responsable") != null ?  request.getParameter ("nombre_responsable") : "" ;
    String nro_cuit  = request.getParameter ("nro_cuit") != null ?  request.getParameter ("nro_cuit") : "--" ;
    if (nro_cuit.equals(""))
          nro_cuit="--";
    String direccion_establecimiento  = request.getParameter ("direccion_establecimiento") != null ?  request.getParameter ("direccion_establecimiento") : "" ;
    String calle_comercio  = request.getParameter ("calle_comercio") != null ?  request.getParameter ("calle_comercio") : "" ;
    String cuit_comercio  = request.getParameter ("cuit_comercio") != null ?  request.getParameter ("cuit_comercio") : "" ;

    String nom_localidad  = request.getParameter ("nom_localidad") != null ?  request.getParameter ("nom_localidad") : "" ;
    String nom_provincia  = request.getParameter ("nom_provincia") != null ?  request.getParameter ("nom_provincia") : "" ;

    String monotributo  = request.getParameter ("monotributo") != null ?  request.getParameter ("monotributo") : "" ;
    String autonomo  = request.getParameter ("autonomo") != null ?  request.getParameter ("autonomo") : "" ;
    String check_monotributo  = request.getParameter ("check_monotributo") != null ?  request.getParameter ("check_monotributo") : "0" ;
    String check_autonomo  = request.getParameter ("check_autonomo") != null ?  request.getParameter ("check_autonomo") : "0" ;

    String nro_pago_facil  = request.getParameter ("nro_pago_facil") != null ?  request.getParameter ("nro_pago_facil") : "";
    String alicuota_pago_facil  = request.getParameter ("alicuota_pago_facil") != null ?  request.getParameter ("alicuota_pago_facil") : HTML.getAlicuotaPF_prn() ;
    String total_pago_facil  = request.getParameter ("total_pago_facil") != null ?  request.getParameter ("total_pago_facil") : "0" ;
    String saldo_pago_facil  = request.getParameter ("saldo_pago_facil") != null ?  request.getParameter ("saldo_pago_facil") : "0" ;
    String comision_pago_facil  = request.getParameter ("comision_pago_facil") != null ?  request.getParameter ("comision_pago_facil") : HTML.getComisionPF_prn() ;
    String subtotal  = request.getParameter ("subtotal") != null ?  request.getParameter ("subtotal") : "0" ;  // esto es total_vep
    String subtotal2  = request.getParameter ("subtotal2") != null ?  request.getParameter ("subtotal2") : "0" ;
    
    String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : htm.getPeriodo_nostatic() ;
    String saldo_iva  = request.getParameter ("saldo_iva") != null ?  request.getParameter ("saldo_iva") : "0" ;
    String saldo_iibb  = request.getParameter ("saldo_iibb") != null ?  request.getParameter ("saldo_iibb") : "0" ;
    String saldo_iva_reporte  = request.getParameter ("saldo_iva_reporte") != null ?  request.getParameter ("saldo_iva_reporte") : "0" ;
    String saldo_iibb_reporte  = request.getParameter ("saldo_iibb_reporte") != null ?  request.getParameter ("saldo_iibb_reporte") : "0" ;
    
    String reporte_gan  = request.getParameter ("reporte_gan") != null ?  request.getParameter ("reporte_gan") : "0" ;
    String reporte_esp  = request.getParameter ("reporte_esp") != null ?  request.getParameter ("reporte_esp") : "0" ;
    String reporte_suss  = request.getParameter ("reporte_suss") != null ?  request.getParameter ("reporte_suss") : "0" ;

    String base_imponible  = request.getParameter ("base_imponible") != null ?  request.getParameter ("base_imponible") : "0" ;
    String reporte_higiene  = request.getParameter ("reporte_higiene") != null ?  request.getParameter ("reporte_higiene") : "0" ;

    String reporte_observacion  = request.getParameter ("reporte_observacion") != null ?  request.getParameter ("reporte_observacion") : "" ;

    String ref = request.getParameter ("ref") != null ?  request.getParameter ("ref") : "" ; // recibe iva o iibb para saber a que' pag volver.


 switch (operacion)
 {
  case "find": 
        out.println(HTML.getHead("reportes", htm.getPeriodo_nostatic()));
        out.println("<br><br>\n<h2>Ver Reportes</h2>"+
                    "\n<form action='/ameca/reportes' method='post'>\n\t" +
                    "\n\tIngrese CUIT del Comercio: <input type='text' name='nro_cuit'>\n\t");
        out.println("\n<br>Ingrese Calle del Establecimiento: "+ 
                    "<input type='text' name='direccion_establecimiento'><br><br>");        
        out.println("<input type='hidden' name='operacion' value='find'> "+ 
                    "<input type='submit'>\n</form><br><br>");

        if(!nro_cuit.equals("--") || !direccion_establecimiento.equals("--"))
             out.println(this.TablaFindComercios(nro_cuit, direccion_establecimiento, "edit"));


        out.println("<table><tr><td height='377px'></td></tr></table>"); 
        out.println(HTML.getTail());

        break;    
   case "edit":
                
                 out.println(HTML.getHead("reportes", htm.getPeriodo_nostatic()));

                 out.println("<br><br>\n"
                         + "<table cellspacing='0' bgcolor='#E8E7C1'>"
                         + "<tr bgcolor='#ccc793'><td colspan='2'><img src='/ameca/imgs/reporteOK.png' style='width:64px;height:64px;'></td><td width='15px'></td> "
                         + "<td style='width:500px; height:64px; font-family:Arial; font-size:40px; font-weight: bold;'>Reporte Impuestos</td><td width='277px'> </td> </tr>"
                         + "<tr><td height='33px'> </td></tr>"
                         +"<tr><td width='25px'></td><td colspan='3' align='center'><table>"+
                                "<tr>\n<td> <a href='/ameca/reportes?operacion=edit&id_establecimiento=" + id_establecimiento + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                                     "<td> <input type='text' name='periodo' value='"+periodo+"' size='6' disabled> </td>"+
                                     "\n<td> <a href='/ameca/reportes?operacion=edit&id_establecimiento=" + id_establecimiento + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>\n"+
                                    "</table></td></tr>\n\n"
                         + "<tr><td></td><td colspan='3' align='center'> "+getReporte(id_establecimiento, periodo)+" </td></tr>"
                         + "<tr><td height='30px'> </td></tr>"
                         + "");

                out.println("<tr><td></td><td colspan='3'><table> <tr> <td><a href='/ameca/reportes?operacion=find&nro_cuit="+cuit_comercio+"&direccion_establecimiento="+calle_comercio+"'><img src='/ameca/imgs/back.png'></a></td> "
                        + "<td width='200px'></td>"  //cuit_comercio
                        + "<td><img src=\"/ameca/imgs/ok_big.png\" onclick=\"document.reportes.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table>"
                        + "</td></tr></table><br><br><br><br><br><br><br>");

                out.println(HTML.getTail());

        break;    
   case "save":       //guarda los datos recibidos del formulario ya completo
                
                String res;
                out.println("<!DOCTYPE html>\n<html><head><title>Ameca - Save Data</title>\n\n</head>\n\n  <body>\n");

                res=updateReporte(id_establecimiento, periodo, reporte_gan, reporte_esp, reporte_suss, comision_pago_facil, reporte_observacion, 
                                                alicuota_pago_facil, saldo_iva_reporte, saldo_iibb_reporte, check_autonomo, check_monotributo, reporte_higiene); //hopefully id_establecimiento

           /*         if (res == null)
                       out.println("res es null<br>");
                else
                    out.println("res NO es null<br>");
            if (res.matches("[-+]?\\d+(\\.\\d+)?"))
                       out.println("match<br>");
                else
                       out.println("UN match<br>");
                out.println("res: "+res+", length: "+res.length()+"<br>");

                java.util.Scanner sc = new java.util.Scanner(res);
                   if (sc.hasNextInt())
                       out.println("NUMBER<br>");
                else
                       out.println("NOT NUMBER<br>");

                   if (res.chars().allMatch(Character::isDigit))
                       out.println("NUMBER<br>");
                else
                       out.println("NOT NUMBER<br>");

                out.println("res: "+res+", length: "+res.length()+"<br>");

               */     
                if (res != null && res.chars().allMatch(Character::isDigit))
                       out.println("<script>function go() {window.location.replace(\"/ameca/reportes?operacion=edit&id_establecimiento="+id_establecimiento+"\");} document.body.style.background='green'; \n window.setTimeout(go, 33); \n</script><br>");
               else
                       out.println("<script>function go() {window.location.replace(\"/ameca/reportes?operacion=edit&id_establecimiento="+id_establecimiento+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 10000); \n</script><br><br>Query: "+res);

                out.println("<a href='/ameca/reportes?operacion=edit&id_establecimiento="+id_establecimiento+"'>Ver Establecimiento</a> <br><br>");
                //out.println("<a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Nuevo Establecimiento</a>\n\n");
                out.println("</body></html>");                    
        break;    
  case "excel":      //guarda los datos recibidos del formulario ya completo
               
                out.println("<html><head><title>EXCEL</title>\n\n</head>" +
                            "<body> hi<br> \n\n ");


                 out.println("<br>Creating excel .... ");
                 out.println("EXCEL iibb: "+this.doExcel_iibb(periodo)+"<br><br>"); 
                 out.println("EXCEL iva: "+this.doExcel_iva(periodo)+"<br><br>"); 
        //         out.println("Formato a float (3456777.4432): "+htm.getTrunc_string(3777.4432f)+"<br><br>"); 


        //        out.println("<a href='/ameca/Reportes/2019/liqui_iibb_"+htm.getPeriodo_nostatic()+".xls'>Volver</a> <br><br>");
                if (ref.equals("iva"))
                        out.println("<script>function go() {window.location.replace(\"/ameca/liquidaciones?operacion=ver_mv&periodo="+periodo+"\");} document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script><br>");
                else
                        out.println("<script>function go() {window.location.replace(\"/ameca/liquidaciones?operacion=ver_mb&periodo="+periodo+"\");} document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script><br>");
                //out.println("<a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Nuevo Establecimiento</a>\n\n");
                out.println("</body></html>");                    
        break;    

 case "pdf":
               out.println("<html><head><title>PDF</title>\n\n</head>" +
                            "<body> hi<br> \n\n ");

             //    out.println("Formato a float (3456777.4432): "+htm.getTrunc_string(3777.4432f)+"<br><br>");  ?operacion=edit&id_establecimiento=367&periodo=201914

                 out.println("<script>function go() {window.location.replace(\"/ameca/reportes?operacion=edit&periodo="+periodo+"&id_establecimiento="+id_establecimiento+"\");} document.body.style.background='green'; \n window.setTimeout(go, 40); \n</script><br>");

        //        out.println("<a href='/ameca/liquidaciones?operacion=edit&periodo="+periodo+"'>Volver</a> <br><br>");

                 out.println("<br>Creating pdf .... ");
        //         out.println("PDF reporte: "+this.doPDF_reporte(periodo, id_establecimiento)+"<br><br>"); 
                 out.println("PDF reporte: "+this.doPDF_reporte(periodo, id_establecimiento, nombre_responsable, nro_cuit, direccion_establecimiento, 
                                                                                                nom_localidad, nom_provincia, nro_pago_facil, monotributo, autonomo, saldo_iva_reporte,
                                                                                                saldo_iibb_reporte, alicuota_pago_facil, subtotal, total_pago_facil, reporte_gan, reporte_esp, 
                                                                                                reporte_suss, reporte_observacion, comision_pago_facil, saldo_pago_facil, 
                                                                                                check_autonomo, check_monotributo, subtotal2, reporte_higiene)+"<br><br>"); 
                //out.println("<a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Nuevo Establecimiento</a>\n\n");
                // out.println("file: "+FILE+periodo.substring(0,4)+"/"+periodo.substring(4, 6)+"/"+periodo+"_"+id_establecimiento+".pdf<br><br>"); 
                out.println("</body></html>");                    
        break;    
case "bk_establecimientos":    //   crea una planilla con todos los comercios y sus establecimientos
                
                out.println("<html><head><title>EXCEL</title>\n\n</head>" +
                            "<body> hi<br> \n\n ");

                 out.println("<br>Creating excel .... ");
                 out.println("EXCEL iibb: "+this.doExcel_establecimientos()+"<br><br>"); 

        //         out.println("Formato a float (3456777.4432): "+htm.getTrunc_string(3777.4432f)+"<br><br>");  /ameca/Reportes/"+htm.getPeriodo_year()+"/clientes_"+htm.getPeriodo_nostatic()+".xls"


        //        out.println("<a href='/ameca/Reportes/2019/liqui_iibb_"+htm.getPeriodo_nostatic()+".xls'>Volver</a> <br><br>");
                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=bk_clientes\");} document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
       //          out.println("<script>function go() {window.location.replace(\"/ameca//Reportes/"+htm.getPeriodo_year()+"/clientes_"+htm.getPeriodo_nostatic()+".xls\"); } document.body.style.background='green'; \n window.setTimeout(go, 40); \n</script><br>");
                //out.println("<a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Nuevo Establecimiento</a>\n\n");
                out.println("</body></html>");                    
        break;

case "bk_reportes":    //   crea una planilla con todos los comercios y sus establecimientos
        
        String result="";
        out.println(HTML.getHead("reportes", htm.getPeriodo_nostatic()));
        out.println("<br><br>\n<h2>Ver Historial Reportes</h2> <br><br>"+
                    "\n<form action='/ameca/reportes' method='post'>\n\t"+
                    "<input type='hidden' name='operacion' value='bk_reportes'>\n");
        if(!id_establecimiento.equals("0"))
             out.println("Periodo: <select name='periodo'> <option value='2019'>2019</option><option value='2020'>2020</option> <option value='2021'>2021</option><option value='2022'>2022</option><option value='2023'>2023</option><option value='2024'>2024</option><option value='2025'>2025</option><option value='2026'>2026</option><option value='2027'>2027</option><option value='2028'>2028</option><option value='2029'>2029</option></select> \n "+
                                "<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'>"+
                                "<input type='hidden' name='nro_cuit' value='"+nro_cuit+"'>"+
                                "<input type='hidden' name='nombre_responsable' value='"+nombre_responsable+"'>"+
                                "<input type='hidden' name='direccion_establecimiento' value='"+direccion_establecimiento+"'>");
        else
            {
                out.println("\n\tIngrese CUIT del Comercio: <input type='text' name='nro_cuit'>\n\t");
                out.println("\n<br>Ingrese Calle del Establecimiento: "+ 
                            "<input type='text' name='direccion_establecimiento'><br><br>");        
            }
        out.println(" "+ 
                    "<input type='submit'>\n</form><br><br>");
        

        if(!id_establecimiento.equals("0"))
             out.println("<table><tr><td>CUIT del Comercio: </td><td>"+nro_cuit+"</td></tr>  <tr> <td>Nombre Responsable: </td><td>"+nombre_responsable+"</td></tr> <tr>  <td>Direccion Establecimiento: </td> <td>"+direccion_establecimiento+"</td></tr></table><br><br><br>"+ 
                                this.TablaHistComercios(id_establecimiento, periodo, direccion_establecimiento, nro_cuit, nombre_responsable));  
        else
                if(!nro_cuit.equals("--") || !direccion_establecimiento.equals("--"))   // hacer pregunta con id_establecimiento (si hay nro, muestro tabla de los ultimos diez y formulario para que arme excel con periodo determinado por user
                     out.println(this.TablaFindComercios(nro_cuit, direccion_establecimiento, "bk_reportes"));  // agregar parametro 'operacion' a la funcion, aqui lleva bk_reportes
            
        if (op2.equals("xls"))
                { 
                    result=doExcel_reporte (id_establecimiento, periodo, autonomo, saldo_iva, saldo_iva_reporte, saldo_iibb, saldo_iibb_reporte, reporte_suss, reporte_higiene, nombre_responsable, direccion_establecimiento, nro_cuit);
                    out.println("<script>function go() {window.location.replace(\"/ameca"+result+"\");   } document.body.style.background='green'; \n window.setTimeout(go, 33); \n</script><br>");
                    // window.location.replace(\"/ameca/reportes?operacion=bk_reportes&id_establecimiento="+id_establecimiento+"&nro_cuit="+nro_cuit+"\"); 
                }
                       out.println("<table><tr><td height='377px'></td></tr></table>"); 
        out.println(HTML.getTail());

        break;
            
           
default: out.println("<html><body>No operacion</body></html>"); 

   
        }
   
 }


    
    

   
     // inserta nuevo establecimiento en tabla Establecimientos y devuelve el id_establecimiento 

    private String updateReporte (String id_establecimiento, String periodo, String reporte_gan, String reporte_esp, String reporte_suss, 
                                                        String comision_pago_facil, String reporte_observacion, String alicuota_pago_facil, String saldo_iva_reporte, 
                                                        String saldo_iibb_reporte, String check_autonomo, String check_monotributo, String reporte_higiene) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
        String res="";
        
        String query="UPDATE EstablecimientosLiquiMes "+
                     " SET reporte_gan="+reporte_gan+", reporte_afip_esp="+reporte_esp+", reporte_suss="+reporte_suss+", reporte_ameca_comision="+comision_pago_facil+
                     ", reporte_observacion='"+reporte_observacion+"', alicuota_pago_facil='"+alicuota_pago_facil+"', saldo_iva_reporte= "+saldo_iva_reporte+
                     ", saldo_iibb_reporte="+saldo_iibb_reporte+", autonomo_check="+check_autonomo+", monotributo_check="+check_monotributo+", reporte_higiene="+reporte_higiene+
                     " WHERE id_establecimiento="+id_establecimiento+" AND periodo='"+periodo+"'";
  
        try 
            {
              con=CX.getCx_pool();
              pst = con.prepareStatement(query);
                                               
              resul_insert = pst.executeUpdate();
              Integer.toString(resul_insert);
            }
        catch (SQLException ex) {
             res+="<br><br>ERROR: "+ex.getMessage()+"<br><br>"+query;
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
               res+=query+ ex.getMessage();
                }
            }
      
        return res;
        }



    

    // Recibe  el nro_cuit y / o calle, y devuelve una tabla html con los establecimientos que cumplen. 
private String TablaFindComercios (String nro_cuit, String calle_comercio, String op) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
    String  query="", resul="\n\n<table class='bicolor' align='center'><tr><th>CUIT</th><th>Razon Social</th><th>Responsable</th><th>Direccion Establecimiento</th><th></th></tr>\n";
        if (nro_cuit.equals("--") && calle_comercio.equals(""))
                return "";
        else if (!nro_cuit.equals("--") && calle_comercio.equals(""))
                query= "SELECT e.id_establecimiento, razon_social, c.nombre_responsable, c.nro_cuit, direccion_establecimiento "+
                                       "FROM Comercios c, Establecimientos e WHERE c.id_comercio=e.id_comercio  AND  c.nro_cuit like '"+nro_cuit+"%' ";
        else if (nro_cuit.equals("--") && !calle_comercio.equals(""))
                query= "SELECT id_establecimiento, razon_social, c.nombre_responsable, c.nro_cuit, direccion_establecimiento "+
                                       "FROM Comercios c, Establecimientos e WHERE c.id_comercio=e.id_comercio AND direccion_establecimiento like '%"+calle_comercio+"%' " ;
        else if (!nro_cuit.equals("--") && !calle_comercio.equals(""))
                query= "SELECT id_establecimiento, razon_social, c.nombre_responsable, c.nro_cuit, e.direccion_establecimiento "+
                                       "FROM Comercios c, Establecimientos e WHERE c.id_comercio=e.id_comercio  AND c.nro_cuit like '"+nro_cuit+"%' AND direccion_establecimiento like '%"+calle_comercio+"%' ";
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next())
                if(op.equals("edit")) 
                        resul+="<tr><td>"+rs.getString(4)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3) +"</td><td>"+rs.getString(5) +"</td><td><a href='/ameca/reportes?operacion=edit&id_establecimiento="+rs.getString(1)+"&periodo="+htm.getPeriodo_nostatic()+"&calle_comercio="+calle_comercio+"&cuit_comercio="+nro_cuit+"'>Editar Reporte</a></td></tr>\n";
                else
                        resul+="<tr><td>"+rs.getString(4)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3) +"</td><td>"+rs.getString(5) +"</td><td><a href='/ameca/reportes?operacion=bk_reportes&id_establecimiento="+rs.getString(1)+"&periodo="+htm.getPeriodo_year()+"&direccion_establecimiento="+URLEncoder.encode(rs.getString(5))+"&nro_cuit="+rs.getString(4)+"&nombre_responsable="+URLEncoder.encode(rs.getString(3))+"'>Ver Reporte</a></td></tr>\n";
                    
            resul+="</table>";
            }
            
        catch (SQLException ex) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        finally 
            {
            try {
                if (rs != null) 
                  rs.close();
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
                resul= ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }


    // Recibe  el id_establecimiento y muestra los ultimos doce reportes, con dos menues desplegables para elegir periodo, y en cada uno la opcion de producir una planilla. 
private String TablaHistComercios (String id_establecimiento, String periodo, String direccion_establecimiento, String nro_cuit, String nombre_responsable) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String periodo2=periodo;
        if (periodo.length()>4)
            periodo2=periodo.substring(0, 4);
        String  query="", resul="\n\n<table class='bicolor' align='center'><tr><th>Periodo</th><th>Monto Autonomo</th><th width='166px'>Saldo IVA</th><th>Saldo IVA reporte</th><th width='166px'>Saldo IIBB</th><th>Saldo IIBB reporte</th> <th>F. 931 SUSS </th> <th>Seguridad e Higiene </th> <th></th></tr>\n";

//        query= "SELECT periodo, base_imponible, saldo_iva, saldo_iva_reporte, saldo_iibb, saldo_iibb_reporte, reporte_suss, reporte_higiene  "+
  //                   "FROM EstablecimientosLiquiMes  " +
    //                 "WHERE id_establecimiento= "+id_establecimiento+" AND SUBSTRING(periodo,1, 4)='"+periodo2+"' " +
      //              "ORDER BY 1 desc";
        query= "SELECT em.periodo, c.id_categ_autonomo, em.saldo_iva, em.saldo_iva_reporte, em.saldo_iibb, em.saldo_iibb_reporte, em.reporte_suss, em.reporte_higiene  "+ // 8
                     "FROM Establecimientos e LEFT JOIN EstablecimientosLiquiMes em USING (id_establecimiento) LEFT JOIN Comercios c USING (id_comercio)  " +
                     "WHERE e.id_establecimiento= "+id_establecimiento+" AND SUBSTRING(em.periodo,1, 4)='"+periodo2+"' " +
                     "ORDER BY 1 desc";
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next())     // formato aleman de doubles
                resul+="<tr><td>"+rs.getString(1)+"</td><td> $ "+htm.getMontoAutonomo(rs.getInt(2))+"</td><td> $ "+String.format(Locale.GERMAN, "%,.2f", rs.getDouble(3)) +"</td><td>"+rs.getString(4) +"</td><td> $ "+String.format(Locale.GERMAN, "%,.2f", rs.getDouble(5)) +"</td> <td> "+rs.getString(6) +" </td><td>  "+String.format(Locale.GERMAN, "%,.2f", rs.getDouble(7)) +"</td> <td>  "+String.format(Locale.GERMAN, "%,.2f", rs.getDouble(8)) +" </td> "
                        + "<td><a href='/ameca/reportes?operacion=bk_reportes&op2=xls&id_establecimiento="+id_establecimiento+"&periodo="+rs.getString(1)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento)+"&nro_cuit="+nro_cuit+"&nombre_responsable="+URLEncoder.encode(nombre_responsable)+"&autonomo="+htm.getMontoAutonomo(rs.getInt(2))+"&saldo_iva="+rs.getString(3)+"&saldo_iva_reporte="+rs.getString(4)+"&saldo_iibb="+rs.getString(5)+"&saldo_iibb_reporte="+rs.getString(6)+"&reporte_suss="+rs.getDouble(7)+"&reporte_higiene="+rs.getDouble(8)+"'><img src=\"/ameca/imgs/msexcel_32.png\" style=\"width:32px;height:32px;\"  onmouseover=\"this.style.cursor='pointer'\"></a></td> </tr>\n";

            resul+="</table>";
            }
            
        catch (SQLException ex) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        finally 
            {
            try {
                if (rs != null) 
                  rs.close();
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
                resul= ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }



    // Recibe  el id_establecimiento y carga las variables del reporte del establecimiento. 
private String getReporte (String id_establecimiento, String periodo) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
   int cond_iva=0, cond_iibb=0, categ_autonomo=0, categ_monotributo=0, check_autonomo=0, check_monotributo=0, 
           id_zona=0, id_localidad=0, id_comercio=0;
   Double comision_pago_facil=0d, alicuota_pago_facil=0d, saldo_pago_facil=0d, saldo_iva=0d, saldo_iibb=0d, saldo_iva_reporte=0d, 
           saldo_iibb_reporte=0d, reporte_gan=0d, reporte_esp=0d, reporte_suss=0d, total_vep=0d, total=0d, subtotal2=0d, vepmasiva=0d, reporte_higiene=0d;   
   
   String  resul="", alicuota_pago_facil_prn="", nro_pago_facil="", nombre_responsable="", nro_cuit="", direccion_establecimiento="", reporte_observacion="";
   String period="";   // para ver si trajo un establecimiento sin base cargada (ie period==null is true)
    
   String query="SELECT  c.id_categ_monotributo, c.id_categ_autonomo, c.id_condicion_iibb, c.id_condicion_iva, "+  //4
                                    " c.nro_cuit, c.nombre_responsable, e.direccion_establecimiento, e.id_zona, e.nro_pago_facil, em.alicuota_pago_facil, "+  //10
                                    " em.saldo_iva, em.saldo_iibb, em.reporte_gan, em.reporte_afip_esp, em.reporte_suss, e.id_localidad, em.periodo, "+  //17
                                    " em.reporte_ameca_comision, em.reporte_observacion, c.id_comercio, em.saldo_iva_reporte, em.saldo_iibb_reporte, "+  //22
                                    " em.autonomo_check, em.monotributo_check, em.reporte_higiene "+  //25
                        " FROM Establecimientos e LEFT JOIN Comercios c USING (id_comercio) LEFT JOIN EstablecimientosLiquiMes em ON e.id_establecimiento=em.id_establecimiento AND  periodo='"+periodo+"'"+ 
                        " WHERE e.id_establecimiento="+id_establecimiento;

    try 
        {
          con=CX.getCx_pool();
          pst = con.prepareStatement(query);
          rs = pst.executeQuery();

          if (rs.next())
                {
                 categ_monotributo=rs.getInt(1);
                 categ_autonomo=rs.getInt(2);
                 check_autonomo=rs.getInt(23);
                 check_monotributo=rs.getInt(24);
                 
                 cond_iibb=rs.getInt(3);
                 cond_iva=rs.getInt(4);
                 comision_pago_facil=rs.getDouble(18);

                 nro_cuit=rs.getString(5);
                 id_comercio=rs.getInt(20);
                 nombre_responsable=rs.getString(6);
                 direccion_establecimiento=rs.getString(7);
                 nro_pago_facil=rs.getString(9);
               //  this.periodo=rs.getString(17);
                 reporte_observacion=rs.getString(19)!= null ?  rs.getString(19): "" ;
                 period=rs.getString(17)!= null ?  rs.getString(17): "-" ;

                 if (period.length()<5)
                     return resul="<br><br><br>\nEl establecimiento: "+direccion_establecimiento+", CUIT: "+nro_cuit+" no tuvo actividad para el periodo "+periodo+".<br> <a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Cargar Base</a><br><br><br><br><br><br><br>";


                 id_zona=rs.getInt(8);
                 id_localidad=rs.getInt(16);

                 alicuota_pago_facil=rs.getDouble(10);
                 alicuota_pago_facil_prn=Double.toString(alicuota_pago_facil);
                 saldo_iva=rs.getDouble(11);
                 saldo_iibb=rs.getDouble(12);
                 saldo_iva_reporte=rs.getDouble(21);
                 saldo_iibb_reporte=rs.getDouble(22);
                 reporte_gan=rs.getDouble(13);
                 reporte_esp=rs.getDouble(14);
                 reporte_suss=rs.getDouble(15);
                 reporte_higiene=rs.getDouble(25);

                 if(saldo_iva_reporte==0)
                     saldo_iva_reporte=saldo_iva;
                 if(saldo_iibb_reporte==0)
                     saldo_iibb_reporte=saldo_iibb;

                 total_vep=saldo_iva_reporte+ saldo_iibb_reporte+ reporte_gan+ reporte_esp+ reporte_suss;
                 
                 subtotal2=0d;  // si llega a doPdf subtotal2 == cero , ya se que no se suman en tabla final
                 if(check_autonomo>0)
                     subtotal2=htm.getMontoAutonomo_f(categ_autonomo);
                 if(check_monotributo>0)
                    subtotal2=htm.getMontoMonotributo_f(categ_monotributo);
               // vepmasiva=subtotal2+total_vep;
                saldo_pago_facil=(total_vep+subtotal2) * alicuota_pago_facil/100;
                total=total_vep + subtotal2 + saldo_pago_facil + comision_pago_facil;
                 //saldo_subtotal2=subtotal2*alicuota_pago_facil/100;
                }
        File tempFile = new File(FILE+periodo.substring(0,4)+"/"+periodo.substring(4, 6)+"/");
        Boolean ok=tempFile.mkdirs();

        tempFile = new File(FILE+periodo.substring(0,4)+"/"+periodo.substring(4, 6)+"/"+periodo+"_"+id_establecimiento+".pdf");

        resul="\n<br><table border='0'><tr><td>Titular: "+nombre_responsable+"</td><td width='111'></td><td>"+
                       "<a href='/ameca/reportes?operacion=pdf"+
                       "&id_establecimiento="+id_establecimiento+
                       "&nombre_responsable="+URLEncoder.encode(nombre_responsable)+
                       "&nro_cuit="+nro_cuit+
                       "&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento)+
                       "&nom_localidad="+htm.getLocalidad(id_localidad)+
                       "&nom_provincia="+htm.getProvincia_localidad(id_localidad)+
                       "&nro_pago_facil="+nro_pago_facil+
                       "&monotributo="+htm.getMontoMonotributo(categ_monotributo)+
                       "&autonomo="+htm.getMontoAutonomo(categ_autonomo)+
                       "&check_monotributo="+check_monotributo+
                       "&check_autonomo="+check_autonomo+
                       "&subtotal2="+String.format(Locale.GERMAN, "%,.2f", total_vep+subtotal2)+
                       "&saldo_iva_reporte="+String.format(Locale.GERMAN, "%,.2f", saldo_iva_reporte)+
                       "&saldo_iibb_reporte="+String.format(Locale.GERMAN, "%,.2f", saldo_iibb_reporte)+
                       "&alicuota_pago_facil="+alicuota_pago_facil+
                       "&subtotal="+String.format(Locale.GERMAN, "%,.2f", total_vep)+
                       "&total_pago_facil="+String.format(Locale.GERMAN, "%,.2f", total)+
                       "&saldo_pago_facil="+String.format(Locale.GERMAN, "%,.2f", saldo_pago_facil)+
                       "&reporte_gan="+String.format(Locale.GERMAN, "%,.2f", reporte_gan)+
                       "&reporte_esp="+String.format(Locale.GERMAN, "%,.2f", reporte_esp)+
                       "&reporte_suss="+String.format(Locale.GERMAN, "%,.2f", reporte_suss)+
                       "&reporte_observacion="+URLEncoder.encode(reporte_observacion)+
                       "&comision_pago_facil="+String.format(Locale.GERMAN, "%,.2f", comision_pago_facil)+
                       "&reporte_higiene="+String.format(Locale.GERMAN, "%,.2f", reporte_higiene)+
                       "&periodo="+periodo+"'><img src='/ameca/imgs/create_pdf.png' style='width:48px;height:48px;'></a>";

        if (tempFile.exists())
            {        
                Random rand = new Random();
                int n = rand.nextInt(5988);
                resul+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href='/ameca/Reportes/"+periodo.substring(0,4)+"/"+periodo.substring(4, 6)+"/"+periodo+"_"+id_establecimiento+".pdf?a="+n+"'><img src='/ameca/imgs/pdf_download.png' style='width:48px;height:48px;'></a></td></tr>";
            }
        else
              resul+="\n</td></tr>\n";


        resul+="<tr><td>Calle: "+direccion_establecimiento+"</td><td></td><td>CUIT: <a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>"+nro_cuit+"</a></td></tr>"+
                "<tr><td>Localidad: "+htm.getLocalidad(id_localidad)+"</td><td></td><td>Provincia: "+htm.getProvincia_localidad(id_localidad)+"</td></tr>"+
                "<tr><td>Nro. Pago Facil: "+nro_pago_facil+"</td><td></td> <td></td>    </tr>"+
                "<tr><td colspan='3' height='15px'></td></tr>"+
                "   </table><br>"
                + ""
                + "<script>\n" +
                        "function recalc() "+
                        "{ "+
                        "if (document.reportes.check_monotributo.checked || document.reportes.check_autonomo.checked)"+
                            "{ "+
                                "document.reportes.subtotal.value=(parseFloat(document.reportes.saldo_iibb_reporte.value)+parseFloat(document.reportes.saldo_iva_reporte.value)+parseFloat(document.reportes.reporte_gan.value)+parseFloat(document.reportes.reporte_suss.value)+parseFloat(document.reportes.reporte_esp.value)).toFixed(2); \n" +
                                "document.reportes.saldo_pago_facil.value=(( parseFloat(document.reportes.subtotal.value) +  parseFloat(document.reportes.subtotal2.value))/100*"+alicuota_pago_facil+").toFixed(2); " +
                                "document.reportes.subtotal_pago_facil.value=( parseFloat(document.reportes.subtotal.value) +  parseFloat(document.reportes.subtotal2.value)).toFixed(2); " +
                                "document.reportes.total.value=(parseFloat(document.reportes.subtotal.value) + parseFloat(document.reportes.saldo_pago_facil.value) + parseFloat(document.reportes.comision_pago_facil.value) + parseFloat(document.reportes.subtotal2.value)).toFixed(2); "+
                            "} \n" +
                        "else "+
                            "{ "+
                                "document.reportes.subtotal.value=(parseFloat(document.reportes.saldo_iibb_reporte.value)+parseFloat(document.reportes.saldo_iva_reporte.value)+parseFloat(document.reportes.reporte_gan.value)+parseFloat(document.reportes.reporte_suss.value)+parseFloat(document.reportes.reporte_esp.value)).toFixed(2); \n" +
                                "document.reportes.saldo_pago_facil.value=(parseFloat (document.reportes.subtotal.value)/100*"+alicuota_pago_facil+").toFixed(2); " +
                                "document.reportes.subtotal_pago_facil.value=document.reportes.subtotal.value; " +
                                "document.reportes.total.value=(parseFloat(document.reportes.subtotal.value) + parseFloat(document.reportes.saldo_pago_facil.value) + parseFloat(document.reportes.comision_pago_facil.value)).toFixed(2); "+
                            "} \n" +
                        "} \n" +
                    "</script>"+
                "\n<form action='/ameca/reportes' name='reportes' method='post'>\n\t"+
                "\n <input type='hidden' name='operacion' value='save'>\n "+
                "<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'> "+
                "<input type='hidden' name='periodo' value='"+periodo+"'>"+
                "<input type='hidden' name='subtotal2' value='"+subtotal2+"'>"
                + ""
                + ""+
                "<table cellSpacing='0' cellPadding='0'>";
        if (check_monotributo>0)
                resul+="<tr>\n\t\t<td>Monotributo: "+htm.getMontoMonotributo(categ_monotributo)+"</td><td><input type='checkbox' name='check_monotributo' value='1' checked> </td></tr>\n\t";
        else
                resul+="<tr>\n\t\t<td>Monotributo: "+htm.getMontoMonotributo(categ_monotributo)+"</td><td><input type='checkbox' name='check_monotributo' value='1'> </td></tr>\n\t";
        if (check_autonomo>0)
                resul+="<tr>\n\t\t<td>Autonomo: "+htm.getMontoAutonomo(categ_autonomo)+"</td><td><input type='checkbox' name='check_autonomo' value='1' checked> </td></tr>\n\t";
        else
                resul+="<tr>\n\t\t<td>Autonomo: "+htm.getMontoAutonomo(categ_autonomo)+"</td><td><input type='checkbox' name='check_autonomo' value='1'> </td></tr>\n\t";
        resul+="<tr>\n\t\t<td>IVA: </td><td><input type='text' size='10' name='saldo_iva_reporte' value='"+saldo_iva_reporte+"'> ("+String.format(Locale.GERMAN, "%,.2f", saldo_iva)+")</td></tr>\n\t"+
                "<tr>\n\t\t<td>IIBB: </td><td><input type='text' size='10' name='saldo_iibb_reporte' value='"+saldo_iibb_reporte+"'> (" + String.format(Locale.GERMAN, "%,.2f", saldo_iibb) + ")</td></tr>\n\t"+
                "<tr>\n\t\t<td>Gan.: </td><td><input type='text' name='reporte_gan' value='"+reporte_gan+"'></td></tr>\n\t"+
                "<tr>\n\t\t<td>AFIP Esp.: </td><td><input type='text' name='reporte_esp' value='"+reporte_esp+"'></td></tr>\n\t"+
                "<tr>\n\t\t<td>Sus.: </td><td><input type='text' name='reporte_suss' value='"+reporte_suss+"'>&nbsp;&nbsp;&nbsp;\n\t"+
                    "<button type='button' onclick=\"recalc();\">"+
                    "Recalcular</button>\n </td></tr>"+
                "<tr><td colspan='3' height='10px'></td></tr>"+
               "<tr>\n\t\t<td>Total VEP: </td><td><input type='text' disabled name='subtotal' value='"+String.format(Locale.GERMAN, "%,.2f",total_vep)+"'> Alic.:<input type='text' disabled name='alicuota_pago_facil' value='"+alicuota_pago_facil_prn+"' size='3'>% </td></tr>\n\t"+
               "<tr>\n\t\t<td>Subtotal: </td><td><input type='text' disabled name='subtotal_pago_facil' value='"+String.format(Locale.GERMAN, "%,.2f", subtotal2+total_vep)+"'></td></tr>\n\t"+ 
               "<tr>\n\t\t<td>Com. Pago Facil: </td><td><input type='text' disabled name='saldo_pago_facil' value='"+String.format(Locale.GERMAN, "%,.2f",saldo_pago_facil)+"'></td></tr>\n\t"+  // comision pago facil: (total vep + auton/monot)*0.08
               "<tr>\n\t\t<td>Ameca: </td><td><input type='text' name='comision_pago_facil' value='"+comision_pago_facil+"'></td></tr>\n\t"+
               "<tr>\n\t\t<td>Seguridad e Higiene: </td><td><input type='text' name='reporte_higiene' value='"+reporte_higiene+"'></td></tr>\n\t"+
                "<tr><td colspan='3' height='10px'></td></tr>"+
               "<tr>\n\t\t<td>Total Pago Facil: </td><td><input type='text' disabled name='total' value='"+String.format(Locale.GERMAN, "%,.2f",total)+"'></td></tr>\n\t"+
                "<tr><td colspan='3' height='20px'></td></tr>"+
                   "<tr>\n\t\t<td>Observaciones: </td><td><textarea name='reporte_observacion' cols='50' rows='3'>"+reporte_observacion+"</textarea></td></tr>\n\t"+
            "</table>"+
                    "</form>\n\n";


        }

    catch (SQLException ex) {
          resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
        //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
        //lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    catch (Exception ex) {
          resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
        //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
        //lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    finally 
        {
        try {
            if (rs != null) 
              rs.close();
            if (pst != null)
              pst.close();
            if (con != null) 
              con.close();
            }
        catch (SQLException ex) {
          // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
          //  resul= ex.getMessage();
            }
        }

       return resul;

    }
        




    // Recibe  el periodo y crea un archivo excel en /usr/share/tomcat8-ameca/ameca/Reportes/2019/liqui_iibb_201901.xls. 
private String doExcel_iibb (String periodo) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String resul="inicio excel", row_span, query="", nom_zona, cuit, direccion, nombre, condicion_iibb;
        Float cmrc_bi=0.0f, cmrc_alic=0.0f, cmrc_debito=0.0f, cmrc_percepcion=0.0f, cmrc_saldo=0.0f,  
                sum_bi=0.0f, sum_debito=0f, sum_percepcion=0f, sum_saldo=0f;
        int i=-1, id_zona=0, rows=0, count=0, cm=0;
        int id_comercio=0, kcmrc=1;
        Float bi=0f,  debit=0f, percep=0f, saldo=0f;
        
        String  FILE_NAME = "/usr/share/tomcat8-ameca/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iibb_"+periodo+".xls";

        int rowNum = 0;
        Boolean sucursales=true;  // crea la segunda sheet solo la primera vez que se encuentra un count >1
        try 
            {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("UNICOS");
            Cell celda;
            Row   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("CUIT");
            celda = row.createCell(1);
            celda.setCellValue("NOMBRE_RESPONSABLE");
            celda = row.createCell(2);
            celda.setCellValue("COND. IIBB");
            celda = row.createCell(3);
            celda.setCellValue("DIRECCION");
            celda = row.createCell(4);
            celda.setCellValue("C.M. / suc");
            celda = row.createCell(5);
            celda.setCellValue("ZONA");
            celda = row.createCell(6);
            celda.setCellValue("BASE IMPONIBLE");
            celda = row.createCell(7);
            celda.setCellValue("ALIC. IIBB");
            celda = row.createCell(8);
            celda.setCellValue("DEBITO IIBB");
            celda = row.createCell(9);
            celda.setCellValue("PERCEPCION IIBB");
            celda = row.createCell(10);
            celda.setCellValue("SALDO IIBB");
            celda = row.createCell(11);
            celda.setCellValue("PERIODO");

            query="SELECT c.nro_cuit, c.nombre_responsable, e.direccion_establecimiento, em.base_imponible, e.id_zona, c.id_comercio, "+ // 6
		  "(SELECT COUNT(*) FROM EstablecimientosLiquiMes eem, Establecimientos ee "+
                                           " WHERE periodo='"+periodo+"'  AND eem.id_establecimiento=ee.id_establecimiento "+
			 	"AND ee.id_comercio=e.id_comercio AND eem.activo_iibb_periodo=1) as 'count', "+  // 7
                                    "id_condicion_iibb, em.percepcion_iibb, em.alicuota_iibb, em.saldo_iibb, e.casa_matriz "+ //  12
                        "FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em "+
                        "WHERE c.id_comercio = e.id_comercio  AND em.activo_iibb_periodo=1 AND e.id_establecimiento=em.id_establecimiento "+
                                "AND em.periodo ='"+periodo+"' "+
                        "ORDER BY count, c.nro_cuit";
            con=CX.getCx_pool();
            pst = con.prepareStatement(query); 
            rs = pst.executeQuery();
        
            while (rs.next()) 
                {
                 count=rs.getInt(7);
                 

                if (count>1 && sucursales)   // acumular en vector subtotales del mismo id_comercio y agregar row del total al cambiar id_comercio.
                   {
                   sucursales=false;
                   id_comercio=rs.getInt(6);
                   rowNum=0;
                   sheet = workbook.createSheet("SUCURSALES"); 
                   row = sheet.createRow(rowNum++);

                   celda = row.createCell(0);
                   celda.setCellValue("CUIT");
                   celda = row.createCell(1);
                   celda.setCellValue("NOMBRE_RESPONSABLE");
                   celda = row.createCell(2);
                   celda.setCellValue("COND. IIBB");
                   celda = row.createCell(3);
                   celda.setCellValue("DIRECCION");
                   celda = row.createCell(4);
                   celda.setCellValue("C.M. / suc");
                   celda = row.createCell(5);
                   celda.setCellValue("ZONA");
                   celda = row.createCell(6);
                   celda.setCellValue("BASE IMPONIBLE");
                   celda = row.createCell(7);
                   celda.setCellValue("ALIC. IIBB");
                   celda = row.createCell(8);
                   celda.setCellValue("DEBITO IIBB");
                   celda = row.createCell(9);
                   celda.setCellValue("PERCEPCION IIBB");
                   celda = row.createCell(10);
                   celda.setCellValue("SALDO IIBB");
                   celda = row.createCell(11);
                   celda.setCellValue("PERIODO");

                   }

                   cuit=rs.getString(1);
                   nombre=rs.getString(2);
                   direccion=rs.getString(3);
                   cm=rs.getInt(12);
                   

                   nom_zona=htm.getZona(rs.getInt(5));
                   condicion_iibb=HTML.getCondicionIIBB(rs.getInt(8));

                   cmrc_bi=rs.getFloat(4);
                   cmrc_percepcion=rs.getFloat(9);
                   cmrc_alic=rs.getFloat(10);
                   cmrc_saldo=rs.getFloat(11);
                   cmrc_debito= cmrc_bi*cmrc_alic/100;

                    row = sheet.createRow(rowNum++);
                    celda = row.createCell(0);
                    celda.setCellValue(cuit);
                    celda = row.createCell(1);
                    celda.setCellValue(nombre);
                    celda = row.createCell(2);
                    celda.setCellValue(condicion_iibb);
                    celda = row.createCell(3);
                    celda.setCellValue(direccion);

                    celda = row.createCell(4);
                    if (cm==0)
                            celda.setCellValue("sucursal");
                    else
                            celda.setCellValue("Casa Matriz");

                    celda = row.createCell(5);
                    celda.setCellValue(nom_zona);
                    celda = row.createCell(6);
                    celda.setCellValue(htm.getTrunc(cmrc_bi)); //cmrc_bi
                    celda = row.createCell(7);
                    celda.setCellValue(cmrc_alic); //cmrc_alic
                    celda = row.createCell(8);
                    celda.setCellValue(htm.getTrunc(cmrc_debito)); //cmrc_debito
                    celda = row.createCell(9);
                    celda.setCellValue(htm.getTrunc(cmrc_percepcion)); //cmrc_percepcion
                    celda = row.createCell(10);
                    celda.setCellValue(htm.getTrunc(cmrc_saldo)); ///cmrc_saldo 
                    celda = row.createCell(11);
                    celda.setCellValue(periodo);  
                    
                   if (!sucursales)
                       {
                        bi+=cmrc_bi;
                        percep+=cmrc_percepcion;
                        debit+=cmrc_debito;
                        saldo+=cmrc_saldo;
                            
                        if (kcmrc<count) //                 
                            kcmrc++;
                         else    // imprimo totales
                            {
                            row = sheet.createRow(rowNum++);
                            celda = row.createCell(0);
                            celda.setCellValue("TOTAL");
                            celda = row.createCell(1);
                            celda.setCellValue("");
                            celda = row.createCell(2);
                            celda.setCellValue("");
                            celda = row.createCell(3);
                            celda.setCellValue("");

                            celda = row.createCell(4);
                            celda.setCellValue("");
                            
                            celda = row.createCell(5);
                            celda.setCellValue("");
                            celda = row.createCell(6);
                            celda.setCellValue(htm.getTrunc(bi)); //total_bi
                            celda = row.createCell(7);
                            celda.setCellValue(""); //cmrc_alic
                            celda = row.createCell(8);
                            celda.setCellValue(htm.getTrunc(debit)); //total_debito
                            celda = row.createCell(9);
                            celda.setCellValue(htm.getTrunc(percep)); //total_percepcion
                            celda = row.createCell(10);
                            celda.setCellValue(htm.getTrunc(saldo)); ///total_saldo 
                            celda = row.createCell(11);
                            celda.setCellValue(periodo);  
                             
                             kcmrc=1;
                             bi=0f;
                             percep=0f;
                             debit=0f;
                             saldo=0f;
                            }
                        }

                  }
  /* */ 
    try 
            { 
              FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
              workbook.write(outputStream); 
            }
        catch (IOException io_ex) {
               resul+= "<br><br>ERROR IO: "+io_ex.getMessage()+"<br><br>"+query;
            }      
              workbook.close();
               resul+="<br>escribo en archivo .... ";

            
            }
        catch (Exception ex) {
               resul+= "<br><br>ERROR GRAL: "+ex.getMessage()+"<br><br>"+query;
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        
        finally 
            {
            try {
                if (rs != null) 
                  rs.close();
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
                resul= ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }
    


    // Recibe  el periodo y crea un archivo excel en /usr/share/tomcat8-ameca/ameca/Reportes/2019/liqui_iva_201901.xls. 
private String doExcel_iva (String periodo) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String resul="inicio excel", row_span, query="", nom_zona, cuit, direccion, nombre, condicion_iva;
        Float cmrc_bi=0f, cmrc_alic=0f, cmrc_debito=0f, cmrc_percepcion=0f, cmrc_saldo=0f, cmrc_compra=0f, cmrc_credito=0f, cmrc_compra_t=0f, 
                sum_bi=0.0f, sum_debito=0f, sum_percepcion=0f, sum_saldo=0f, sum_compra=0f, sum_credito=0f, sum_compra_t=0f;
        int i=-1, id_zona=0, rows=0, count=0, cm=0 ;
        int id_comercio=0, kcmrc=1;
        
        String  FILE_NAME = "/usr/share/tomcat8-ameca/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iva_"+periodo+".xls";

        int rowNum = 0;
        Boolean sucursales=true;  // crea la segunda sheet solo la primera vez que se encuentra un count >1
        try 
            {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("UNICOS");
            Cell celda;
            Row   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("CUIT");
            celda = row.createCell(1);
            celda.setCellValue("NOMBRE_RESPONSABLE");
            celda = row.createCell(2);
            celda.setCellValue("COND. IVA");
            celda = row.createCell(3);
            celda.setCellValue("DIRECCION");

            celda = row.createCell(4);
            celda.setCellValue("C.M. / suc");

            celda = row.createCell(5);
            celda.setCellValue("ZONA");
            celda = row.createCell(6);
            celda.setCellValue("BASE IMPONIBLE");
            celda = row.createCell(7);
            celda.setCellValue("ALIC. IVA");
            celda = row.createCell(8);
            celda.setCellValue("DEBITO IVA");
            celda = row.createCell(9);
            celda.setCellValue("VENTA TOTAL");
            celda = row.createCell(10);
            celda.setCellValue("COMPRA IVA");
            celda = row.createCell(11);
            celda.setCellValue("CREDITO IVA");
            celda = row.createCell(12);
            celda.setCellValue("COMPRA TOTAL");
            celda = row.createCell(13);
            celda.setCellValue("PERCEPCION IVA");
            celda = row.createCell(14);
            celda.setCellValue("SALDO IVA");
            celda = row.createCell(15);
            celda.setCellValue("PERIODO");

            query="SELECT c.nro_cuit, c.nombre_responsable, e.direccion_establecimiento, em.base_imponible, e.id_zona, c.id_comercio, "+ // 6
		  "(SELECT COUNT(*) FROM EstablecimientosLiquiMes eem, Establecimientos ee "+
                                           " WHERE periodo='"+periodo+"'  AND eem.id_establecimiento=ee.id_establecimiento "+
			 	"AND ee.id_comercio=e.id_comercio AND eem.activo_iva_periodo=1) as 'count', "+  // 7
                                    "id_condicion_iva, em.percepcion_iva, em.alicuota_iva, em.saldo_iva, em.compra_iva, "   +  //12
                                    "truncate (compra_iva*alicuota_iva/100, 2), truncate (compra_iva*alicuota_iva/100+compra_iva, 2), e.casa_matriz  "+ //  15
                        "FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em "+
                        "WHERE c.id_comercio = e.id_comercio  AND em.activo_iva_periodo=1 AND e.id_establecimiento=em.id_establecimiento "+
                                "AND em.periodo ='"+periodo+"' "+
                        "ORDER BY count, c.nro_cuit";
            con=CX.getCx_pool();
            pst = con.prepareStatement(query); 
            rs = pst.executeQuery();
        
            while (rs.next()) 
                {
                 count=rs.getInt(7);
                 

                if (count>1 && sucursales)   // acumular en vector subtotales del mismo id_comercio y agregar row del total al cambiar id_comercio.
                   {
                   sucursales=false;
                   id_comercio=rs.getInt(6);
                   rowNum=0;
                   sheet = workbook.createSheet("SUCURSALES"); 
                   row = sheet.createRow(rowNum++);

                   celda = row.createCell(0);
                   celda.setCellValue("CUIT");
                   celda = row.createCell(1);
                   celda.setCellValue("NOMBRE_RESPONSABLE");
                   celda = row.createCell(2);
                   celda.setCellValue("COND. IVA");
                   celda = row.createCell(3);
                   celda.setCellValue("DIRECCION");

                   celda = row.createCell(4);
                   celda.setCellValue("C.M. / suc");
                   
                   celda = row.createCell(5);
                   celda.setCellValue("ZONA");
                   celda = row.createCell(6);
                   celda.setCellValue("BASE IMPONIBLE");
                   celda = row.createCell(7);
                   celda.setCellValue("ALIC. IVA");
                   celda = row.createCell(8);
                   celda.setCellValue("DEBITO IVA");
                   celda = row.createCell(9);
                   celda.setCellValue("VENTA TOTAL");
                   celda = row.createCell(10);
                   celda.setCellValue("COMPRA IVA");
                   celda = row.createCell(11);
                   celda.setCellValue("CREDITO IVA");
                   celda = row.createCell(12);
                   celda.setCellValue("COMPRA TOTAL");
                   celda = row.createCell(13);
                   celda.setCellValue("PERCEPCION IVA");
                   celda = row.createCell(14);
                   celda.setCellValue("SALDO IVA");
                   celda = row.createCell(15);
                   celda.setCellValue("PERIODO");

                   }

                   cuit=rs.getString(1);
                   nombre=rs.getString(2);
                   direccion=rs.getString(3);

                   cm=rs.getInt(15);

                   nom_zona=htm.getZona(rs.getInt(5));
                   condicion_iva=HTML.getCondicionIVA(rs.getInt(8));

                   cmrc_alic=rs.getFloat(10);
                   cmrc_bi=rs.getFloat(4);
                   cmrc_debito= cmrc_bi*cmrc_alic/100;
                   cmrc_compra=rs.getFloat(12);
                   cmrc_credito=rs.getFloat(13);
                   cmrc_compra_t=rs.getFloat(14);
//                   cmrc_credito= cmrc_compra*cmrc_alic/100;
                   cmrc_percepcion=rs.getFloat(9);
                   cmrc_saldo=rs.getFloat(11);

                    row = sheet.createRow(rowNum++);
                    celda = row.createCell(0);
                    celda.setCellValue(cuit);
                    celda = row.createCell(1);
                    celda.setCellValue(nombre);
                    celda = row.createCell(2);
                    celda.setCellValue(condicion_iva);
                    celda = row.createCell(3);
                    celda.setCellValue(direccion);

                    celda = row.createCell(4);
                    if (cm==0)
                            celda.setCellValue("sucursal");
                    else
                            celda.setCellValue("Casa Matriz");

                    celda = row.createCell(5);
                    celda.setCellValue(nom_zona);

                    celda = row.createCell(6);
                    celda.setCellValue(htm.getTrunc(cmrc_bi)); //cmrc_bi
                    celda = row.createCell(7);
                    celda.setCellValue(cmrc_alic); //cmrc_alic
                    celda = row.createCell(8);
                    celda.setCellValue(htm.getTrunc(cmrc_debito)); //cmrc_debito
                    celda = row.createCell(9);
                    celda.setCellValue(htm.getTrunc(cmrc_bi+cmrc_debito)); //venta_total
                    celda = row.createCell(10);
                    celda.setCellValue(cmrc_compra); //cmrc_compra
                    celda = row.createCell(11);
                    celda.setCellValue(htm.getTrunc(cmrc_credito)); //cmrc_credito
                    celda = row.createCell(12);
                    celda.setCellValue(htm.getTrunc(cmrc_compra_t)); //compra total
                    celda = row.createCell(13);
                    celda.setCellValue(htm.getTrunc(cmrc_percepcion)); //cmrc_percepcion
                    celda = row.createCell(14);
                    celda.setCellValue(htm.getTrunc(cmrc_saldo)); //cmrc_saldo_iva
                    celda = row.createCell(15);
                    celda.setCellValue(periodo); 

                   if (!sucursales)
                       {
                        sum_bi+=cmrc_bi;
                        sum_debito+=cmrc_debito;
                        sum_compra+=cmrc_compra;
                        sum_compra_t+=cmrc_compra_t;
                        sum_credito+=cmrc_credito;
                        sum_percepcion+=cmrc_percepcion;
                        sum_saldo+=cmrc_saldo;
                            
                        if (kcmrc<count) //                 
                            kcmrc++;
                         else    // imprimo totales
                            {
                            row = sheet.createRow(rowNum++);
                            celda = row.createCell(0);
                            celda.setCellValue("TOTAL");
                            celda = row.createCell(1);
                            celda.setCellValue("");
                            celda = row.createCell(2);
                            celda.setCellValue("");
                            celda = row.createCell(3);
                            celda.setCellValue("");
                            celda = row.createCell(4);
                            celda.setCellValue("");
                            celda = row.createCell(5);
                            celda.setCellValue("");
                            celda = row.createCell(6);
                            celda.setCellValue(htm.getTrunc(sum_bi)); //cmrc_bi
                            celda = row.createCell(7);
                            celda.setCellValue(""); //cmrc_alic
                            celda = row.createCell(8);
                            celda.setCellValue(htm.getTrunc(sum_debito)); //cmrc_debito
                            celda = row.createCell(9);
                            celda.setCellValue(htm.getTrunc(sum_bi+sum_debito)); //venta total
                            celda = row.createCell(10);
                            celda.setCellValue(htm.getTrunc(sum_compra)); //cmrc_compra
                            celda = row.createCell(11);
                            celda.setCellValue(htm.getTrunc(sum_credito)); //cmrc_credito
                            celda = row.createCell(12);
                            celda.setCellValue(htm.getTrunc(sum_compra_t)); //compra total
                            celda = row.createCell(13);
                            celda.setCellValue(htm.getTrunc(sum_percepcion)); //cmrc_percepcion
                            celda = row.createCell(14);
                            celda.setCellValue(htm.getTrunc(sum_saldo)); //cmrc_saldo iva
                            celda = row.createCell(15);
                            celda.setCellValue(periodo); 
                             
                             kcmrc=1;
                             sum_bi=0f;
                             sum_debito=0f;
                             sum_compra=0f;
                             sum_compra_t=0f;
                             sum_credito=0f;
                             sum_percepcion=0f;
                             sum_saldo=0f;
                            }
                        }

                  }
    try 
            { 
              FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
              workbook.write(outputStream);
            }
        catch (IOException io_ex) {
               resul+= "<br><br>ERROR IO: "+io_ex.getMessage()+"<br><br>"+query;
            }      
              workbook.close();
               resul+="<br>escribo en archivo .... ";

            
            }
        catch (Exception ex) {
               resul+= "<br><br>ERROR GRAL: "+ex.getMessage()+"<br><br>"+query;
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        
        finally 
            {
            try {
                if (rs != null) 
                  rs.close();
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
                resul= ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }
    


private String doPDF_reporte (String periodo, String id_establecimiento, String nombre_responsable, String nro_cuit, String direccion_establecimiento, 
                                                    String nom_localidad, String nom_provincia, String nro_pago_facil, String monotributo, String autonomo,
                                                    String saldo_iva_reporte, String saldo_iibb_reporte, String alicuota_pago_facil, String total_vep, String total_pago_facil,
                                                    String reporte_gan, String reporte_esp, String reporte_suss, String reporte_observacion, String comision_pago_facil, 
                                                    String saldo_pago_facil, String check_autonomo, String check_monotributo, String subtotal2, String reporte_higiene)
    {

        String resul="";
        
try{
//            Document document = new Document();
            Document document = new Document(PageSize.A4, 12, 20, 20, 8 );
            PdfWriter.getInstance(document, new FileOutputStream(FILE+periodo.substring(0,4)+"/"+periodo.substring(4, 6)+"/"+periodo+"_"+id_establecimiento+".pdf"));
            document.open();
            addMetaData(document);
            addTitlePage (document, periodo, id_establecimiento, nombre_responsable, nro_cuit, direccion_establecimiento, 
                                    nom_localidad, nom_provincia, nro_pago_facil, monotributo, autonomo, saldo_iva_reporte,
                                    saldo_iibb_reporte, alicuota_pago_facil, total_vep, total_pago_facil, reporte_gan, reporte_esp, 
                                    reporte_suss, reporte_observacion, comision_pago_facil, saldo_pago_facil, check_autonomo, 
                                    check_monotributo, subtotal2, reporte_higiene);
 //           addContent(document);
            document.close();
        } catch (Exception e) {
            resul= e.getMessage();
        }  

return resul;
    }



 private static void addMetaData(Document document) {
        document.addTitle("Reporte Mensual");
        document.addSubject("Ameca-Facturacion");
//        document.addKeywords("Reporte, PDF, iText");
        document.addAuthor("Ameca");
//        document.addCreator("Ameca");
    }

    private  void addTitlePage(Document document, String periodo, String id_establecimiento, String nombre_responsable, String nro_cuit, String direccion_establecimiento, 
                                                    String nom_localidad, String nom_provincia, String nro_pago_facil, String monotributo, String autonomo,
                                                    String saldo_iva_reporte, String saldo_iibb_reporte, String alicuota_pago_facil, String total_vep, String total_pago_facil,
                                                    String reporte_gan, String reporte_esp, String reporte_suss, String reporte_observacion, String comision_pago_facil, 
                                                    String saldo_pago_facil, String check_autonomo, String check_monotributo, String subtotal2, String reporte_higiene)
            throws DocumentException, IOException {
     //   Paragraph preface = new Paragraph();
        // We add one empty line
        //addEmptyLine(preface, 1);   // 
  //      preface.add(new Paragraph(" "));
        // Lets write a big header
        
//        preface.add(new Paragraph("Reporte Impuestos - 税金报告表", catFont));
//        document.add(new LineSeparator(0.5f, 100, null, 0, -5));
    //    Chunk linebreak_dotted = new Chunk(new DottedLineSeparator());
       Chunk linebreak2 = new Chunk(new LineSeparator());
        
       LineSeparator separator = new LineSeparator();
        separator.setLineWidth(2);
        
        
 //       document.add(new Phrase("\n"));
  //      document.add(line_thick);
 //       document.add(new Phrase("\n"));

  //      document.add(linebreak_dotted);
  //      document.add(new Phrase("\n"));
  //      document.add(linebreak2);
  //      document.add(new Phrase("\n"));
   //     document.add(linebreak_dotted);
    //    preface.add(new Paragraph(" "));    // no aparece porque preface lo agrega al final, y estoy escribiendo las lineas sobre document.

        //  document.add( new Paragraph( "Hello, World!" ) );

        // add a couple of blank lines
   //     document.add(new Phrase("\n"));
   //     document.add(linebreak2);
    //    preface.add(new Paragraph(" "));

               // Will create: Report generated by: _name, _date
    //    preface.add(new Paragraph(
    //            "Report generated by: " + System.getProperty("user.name") + ", " +HTML.getPeriodo(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   //             smallBold));
   //     preface.add(new Paragraph(" "));
 //       preface.add(new Paragraph( "This document describes something which is very important ",  smallBold));
  //      preface.add(new Paragraph(" "));
//        preface.add(new Paragraph( "CAUTE:  This document is a preliminary version).",   redFont));

//        document.add(preface);
       bfComic = BaseFont.createFont("/usr/share/tomcat8-ameca/ameca/Styles/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
       chino_tit = new Font(bfComic, 16, Font.BOLD);
       chino = new Font(bfComic, 14, Font.NORMAL);
       chino_b = new Font(bfComic, 14, Font.BOLD);
       chino_s = new Font(bfComic, 12, Font.NORMAL);
       chino_ss = new Font(bfComic, 10, Font.NORMAL);
       Chunk line_thick = new Chunk(separator);
       document.add(new Paragraph("Reporte Impuestos - 税金报告表                      Periodo: "+htm.getPeriodo_prn_long (periodo), chino_tit));
       document.add(line_thick);
  //     document.add(new Phrase("\nTABLE"));
        float [] cols = {230F,350F, 50F, 350F}; 
        
        PdfPTable table = new PdfPTable(cols);
        table.setWidthPercentage(100);
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(Image.getInstance(IMG_logo));
      //  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setRowspan(4);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Titular:   "+nombre_responsable, chino));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""));
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setRowspan(4);
        table.addCell(c1);

//        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);


//  fila2
        c1 = new PdfPCell(new Phrase("Calle:  "+direccion_establecimiento, chino));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CUIT:  "+nro_cuit, chino));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

//  fila3
        c1 = new PdfPCell(new Phrase("Localidad:  "+nom_localidad, chino));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Provincia:  "+nom_provincia, chino));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

//  fila4
        c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""));
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);
       // table.addCell("");


//        c1.setBackgroundColor(new BaseColor(255,255,45));

        document.add(table);
        document.add(line_thick);
        document.add(new Phrase("\n"));
       document.add(new Phrase("\n"));
       //document.add(new Phrase("\n"));
       
        document.add( new Paragraph( "Opción 1: VEP. Los impuestos ya están declarados en la AFIP / ARBA / AGIP. Use su cuenta bancaria para abonar.", chino_s ) );
        document.add( new Paragraph( "选项 1: 税款在AfIP/Arba/AGIP系统中申報了，請使用银行账户支付", chino_s  ) );
//        document.add(new Phrase("\n"));
//        document.add(linebreak2);
        document.add(new Phrase("\n"));
        
        float [] cols2 = {350F, 350F}; 
        table = new PdfPTable(cols2);
        table.setWidthPercentage(100);

        c1 = new PdfPCell(new Phrase("IVA (货物增值税): "+saldo_iva_reporte, chino));
        c1.setFixedHeight(22);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("AFIP Esp. (特别的税金): "+reporte_esp, chino));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.TOP | Rectangle.LEFT);
        c1.setPaddingLeft(8);
        table.addCell(c1);

// fila2
        c1 = new PdfPCell(new Phrase("ARBA / AGIP / IB (营业税): "+saldo_iibb_reporte, chino));
        c1.setFixedHeight(20);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("F.931 SUSS. (员工养老金): "+reporte_suss, chino));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.LEFT);
        c1.setPaddingLeft(8);
        table.addCell(c1);

   // fila3
        c1 = new PdfPCell(new Phrase("GAN. (所得税): "+reporte_gan, chino));
        c1.setFixedHeight(22);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" ", chino));
        c1.setBorder(Rectangle.BOTTOM | Rectangle.LEFT);
        table.addCell(c1);

// fila4  linea doble
        c1 = new PdfPCell(new Phrase(""));
        c1.setFixedHeight(2);
        c1.setColspan(2);
        c1.setBorder(Rectangle.BOTTOM);
        table.addCell(c1);

        
   // fila5   subtotal
        c1 = new PdfPCell(new Phrase("", chino));
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total VEP (总额): $ "+total_vep, chino_b));
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setFixedHeight(22);
        c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);
        
        if (monotributo==null)
            monotributo="";
        if (check_monotributo==null)
            check_monotributo="";

        document.add(table);
        document.add(new Phrase("\n"));
        document.add(new Phrase("Observaciones: "+reporte_observacion, chino_s));
        document.add(new Phrase("\n", chino_s));
        if (monotributo.length()>1)   
                document.add(new Phrase("Monotributo (单一税): $ "+monotributo, chino_s));
        document.add(new Phrase("\n", chino_s));
        if (autonomo.length()>1)
                document.add(new Phrase("Autónomo (老板养老金): $ "+autonomo, chino_s));
        document.add(new Phrase("\n", chino_s));
        document.add(new Phrase("Seguridad e Higiene (卫生安全税): $ "+reporte_higiene, chino_s));
        document.add(new Phrase("\n"));
//        document.add(new Phrase("\n"));
        document.add(linebreak2);
        
        cols2[0] = 610F;
        cols2[1]=90F;  
        
        table = new PdfPTable(cols2);
        table.setWidthPercentage(100);
        
        c1 = new PdfPCell(new Phrase ( "Opción 2: Pago utilizando la plataforma Pago Fácil (sin usar cuenta bancaria propia)." , chino_s ) );
        c1.setBorder(PdfPCell.NO_BORDER);
  
        //document.add( new Paragraph( "Opción 2: Pago utilizando la plataforma Pago Fácil (sin usar cuenta bancaria propia)." , chino_s ) );
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
//         c1 = new PdfPCell(new Phrase(""));
         table.addCell(c1);
         
         
         
         c1 = new PdfPCell(Image.getInstance(IMG_pf));
      //  c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setRowspan(3);
        table.addCell(c1);
  

        c1 = new PdfPCell(new Phrase( "") );
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setFixedHeight(2f);
        table.addCell(c1);
        

        c1 = new PdfPCell(new Phrase( "选项 2: 使用pagofacil平台进行纳税支付，无需使用自己的银行账户." , chino_s ) );
        c1.setBorder(PdfPCell.NO_BORDER);
  
        //document.add( new Paragraph( "Opción 2: Pago utilizando la plataforma Pago Fácil (sin usar cuenta bancaria propia)." , chino_s ) );
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
//         c1 = new PdfPCell(new Phrase(""));
         table.addCell(c1);
        
  //    document.add( new Paragraph( "选项 2: 使用pagofacil平台进行纳税支付，无需使用自己的银行账户." , chino_s ) );
        
        
        
        document.add(table);
        
        document.add(new Phrase("\n"));
        //document.add(new Phrase("\n"));

        float [] cols3= {200F, 200F, 200F, 200F}; 
        float [] cols4= {150F, 150F, 150F, 150F, 150F}; 
        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
            table = new PdfPTable(cols4);
        else
            table = new PdfPTable(cols3);
        
        table.setWidthPercentage(100);

// fila 1: chars chinos
        c1 = new PdfPCell(new Phrase("总额", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(c1);

        if (check_autonomo.equals("1") )
            c1 = new PdfPCell(new Phrase("老板养老金", chino_s));
        if (check_monotributo.equals("1") )
            c1 = new PdfPCell(new Phrase("单一税", chino_s));
        if (check_autonomo.equals("0") && check_monotributo.equals("0"))
            c1 = new PdfPCell(new Phrase("老板养老金 / 单一税", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(c1);
        
        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
               {
                c1 = new PdfPCell(new Phrase("总额", chino_s));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.disableBorderSide(Rectangle.BOTTOM);
                table.addCell(c1);
                
               }

        c1 = new PdfPCell(new Phrase("税", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("费用", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(c1);
        
        
// fila2        
        c1 = new PdfPCell(new Phrase("Total VEP", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.TOP);
        table.addCell(c1);

        if (check_autonomo.equals("1") )
                c1 = new PdfPCell(new Phrase("Autonomo", chino_s));
        if (check_monotributo.equals("1") )
                c1 = new PdfPCell(new Phrase("Monotributo", chino_s));
        if (check_autonomo.equals("0") && check_monotributo.equals("0"))
                c1 = new PdfPCell(new Phrase("Autonomo / Monotributo", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.TOP);
        table.addCell(c1);
        
        
        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
               {
                c1 = new PdfPCell(new Phrase("Subtotal", chino_s));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.disableBorderSide(Rectangle.TOP);
                table.addCell(c1);
               }
        

        c1 = new PdfPCell(new Phrase("Com. Pago Fácil", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.TOP);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("AMECA", chino_s));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.disableBorderSide(Rectangle.TOP);
        table.addCell(c1);


// valores, fila3
        c1 = new PdfPCell(new Phrase("$ "+total_vep, chino_s)); // total vep
        c1.setFixedHeight(24);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c1.setBorder(Rectangle.TOP);
        table.addCell(c1);

//3.2        
        if (check_autonomo.equals("1") )
                c1 = new PdfPCell(new Phrase("$ "+autonomo, chino_s));  // auton / monot
        if (check_monotributo.equals("1") )
                c1 = new PdfPCell(new Phrase("$ "+monotributo, chino_s));  
        if (check_autonomo.equals("0") && check_monotributo.equals("0"))
                c1 = new PdfPCell(new Phrase(" -- ", chino_s));  // auton / monot

        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c1.setBorder(Rectangle.TOP);
        table.addCell(c1);
        
//3.3

        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
               {
                c1 = new PdfPCell(new Phrase("$"+subtotal2, chino_s));    // total vep + auton/monot
                c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
               }

//3.4
        
        
        c1 = new PdfPCell(new Phrase("$ "+saldo_pago_facil, chino_s));  // comision pago facil  (8% de total vep o total vep + auton / monot)
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c1.setBorder(Rectangle.TOP);
        table.addCell(c1);

        
//3.5
        
        c1 = new PdfPCell(new Phrase("$ "+comision_pago_facil, chino_s));  // comision ameca
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c1.setBorder(Rectangle.TOP);
        table.addCell(c1);
        
         
//filas 3y4  doble linea
        c1 = new PdfPCell(new Phrase(""));
        c1.setFixedHeight(2);
        c1.setBorder(Rectangle.BOTTOM);
        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
            c1.setColspan(5);
        else
            c1.setColspan(4);
            
        table.addCell(c1);
/*
        c1 = new PdfPCell(new Phrase(""));
        c1.setFixedHeight(4);
        c1.setBorder(Rectangle.TOP);
        c1.setColspan(4);
        table.addCell(c1);
   */     
 //fila 5
        c1 = new PdfPCell(new Phrase("Total a Depositar: (总共要存金额): $ "+total_pago_facil, chino_b));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setFixedHeight(22);
        c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
        c1.setBorder(PdfPCell.NO_BORDER);
        if (check_autonomo.equals("1") || check_monotributo.equals("1"))
            c1.setColspan(5);
        else
            c1.setColspan(4);
        table.addCell(c1);

        document.add(table);

        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Paragraph ("Le informamos que en caso de notar diferencia en el monto  entre este reporte y el volante de Pagopyme-PagoFacil, eso se debe al interés de ingresos brutos que ya está incluido en su volante de pagopyme.", chino_ss));
        //document.add(new Phrase("\n"));
        document.add(new Phrase("尊敬的顾客们，特别通知如果您发现我们做出来的pagopyme 的缴费单中的金额与原来的金额有所不同，那是因为其中包含着每月20号后交的营业税的利息.", chino_ss));

        
       // document.add(Image.getInstance(IMG_logo));
      //  Image img = PngImage.getImage(IMG);
    //    img.setBorder(Image.NO_BORDER);
    //    img.setAlignment(Element.ALIGN_CENTER);
        //img.scaleAbsolute(width,height);
   //     img.setAbsolutePosition(300f, 450f);
  //      document.add(img);
       
       // document.newPage();   // Start a new page
    }

    


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }



private String doExcel_establecimientos () 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String resul="inicio excel", row_span, query="", nom_zona, cuit, direccion, nombre, condicion_iibb, condicion_iva, direccion_establecimiento, 
                                periodo_alta, periodo_baja;
        int i=-1, id_zona=0, rows=0, count=0, casa_matriz, id_comercio=0, id_anterior=0, cond_iva=0, id_categ_auton, id_categ_monot;
        
        String  FILE_NAME = "/usr/share/tomcat8-ameca/ameca/Reportes/"+htm.getPeriodo_year()+"/clientes_"+htm.getPeriodo_nostatic()+".xls";

        int rowNum = 0;
        try 
            {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("CLIENTES");
            Cell celda;
            Row   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("CUIT");
            celda = row.createCell(1);
            celda.setCellValue("NOMBRE RESPONSABLE");
            celda = row.createCell(2);
            celda.setCellValue("DOMICILIO FISCAL");
            celda = row.createCell(3);
            celda.setCellValue("PERIODO ALTA");
            celda = row.createCell(4);
            celda.setCellValue("PERIODO BAJA");
            celda = row.createCell(5);
            celda.setCellValue("COND. IVA");
            celda = row.createCell(6);
            celda.setCellValue("CATEG. IVA");  // DE AUTONOMO O MONOTRIBUTISTA
            celda = row.createCell(7);
            celda.setCellValue("COND. IIBB");
            celda = row.createCell(8);
            celda.setCellValue("DIRECCION ESTABLECIMIENTO");
            celda = row.createCell(9);
            celda.setCellValue("CATEG. ESTABLECIMIENTO");
            celda = row.createCell(10);
            celda.setCellValue("LOCALIDAD");
            celda = row.createCell(11);
            celda.setCellValue("ZONA");
            
            

            query="SELECT  c.nro_cuit, c.nombre_responsable, c.domicilio_fiscal, c.periodo_alta, c.periodo_baja, " +  // 5
                            "c.id_condicion_iva, c.id_condicion_iibb, c.id_categ_autonomo, c.id_categ_monotributo, " +  // 9
                            "e.direccion_establecimiento, e.casa_matriz, e.id_localidad, e.id_zona, c.id_comercio " +  // 14
                        " FROM Establecimientos e LEFT JOIN Comercios c USING (id_comercio)   " +
                        " ORDER BY 1 ";
            con=CX.getCx_pool();
            pst = con.prepareStatement(query); 
            rs = pst.executeQuery();
        
            while (rs.next()) 
                {

                   cuit=rs.getString(1);
                   nombre=rs.getString(2);
                   direccion=rs.getString(3);
                   direccion_establecimiento=rs.getString(10);
                   periodo_alta=rs.getString(4);
                   periodo_baja=rs.getString(5);

                   nom_zona=htm.getZona(rs.getInt(13));
                   condicion_iibb=HTML.getCondicionIIBB(rs.getInt(7));
                   cond_iva=rs.getInt(6);
                   condicion_iva=HTML.getCondicionIVA(cond_iva);
                   casa_matriz=rs.getInt(11);
                   id_comercio=rs.getInt(14);
                   id_categ_auton=rs.getInt(8);
                   id_categ_monot=rs.getInt(9);

                    row = sheet.createRow(rowNum++);
                    
                    if (id_anterior==id_comercio)
                        {
                            celda = row.createCell(0);
                            celda.setCellValue("");
                            celda = row.createCell(1);
                            celda.setCellValue(nombre);
                            celda = row.createCell(2);
                            celda.setCellValue("");
                            celda = row.createCell(3);
                            celda.setCellValue("");
                            celda = row.createCell(4);
                            celda.setCellValue("");
                            celda = row.createCell(5);
                            celda.setCellValue(""); 
                            celda = row.createCell(6);
                            celda.setCellValue(""); 
                            celda = row.createCell(7);
                            celda.setCellValue(""); 
                            celda = row.createCell(8);
                            
                        }                    
                else
                        {
                            id_anterior=id_comercio;
                            celda = row.createCell(0);
                            celda.setCellValue(cuit);
                            celda = row.createCell(1);
                            celda.setCellValue(nombre);
                            celda = row.createCell(2);
                            celda.setCellValue(direccion);
                            celda = row.createCell(3);
                            celda.setCellValue(periodo_alta);
                            celda = row.createCell(4);
                            celda.setCellValue(periodo_baja);
                            celda = row.createCell(5);
                            celda.setCellValue(condicion_iva); 
                            celda = row.createCell(6);
                            celda.setCellValue(""); 
                            if (cond_iva==1)
                                celda.setCellValue(htm.getCategoriaMonotributo_min(id_categ_monot)); 
                            else
                                celda.setCellValue(htm.getCategoriaAutonomo_min(id_categ_auton)); 
                                
                            celda = row.createCell(7);
                            celda.setCellValue(condicion_iibb); 
                            celda = row.createCell(8);
                        }                    
                    
                    celda.setCellValue(direccion_establecimiento); 
                    celda = row.createCell(9);
                    if (casa_matriz==1)
                            celda.setCellValue("Casa Matriz"); 
                    else
                            celda.setCellValue("Sucursal"); 
                        
                    celda = row.createCell(10);
                    celda.setCellValue(htm.getLocalidad(rs.getInt(12))); 
                    celda = row.createCell(11);
                    celda.setCellValue(nom_zona); 
                    


                  }
  /* */ 
    try 
            { 
              FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
              workbook.write(outputStream); 
            }
        catch (IOException io_ex) {
               resul+= "<br><br>ERROR IO: "+io_ex.getMessage()+"<br><br>"+query;
            }      
              workbook.close();
               resul+="<br>escribo en archivo .... ";

            
            }
        catch (Exception ex) {
               resul+= "<br><br>ERROR GRAL: "+ex.getMessage()+"<br><br>"+query;
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        
        finally 
            {
            try {
                if (rs != null) 
                  rs.close();
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
              // Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
                resul= ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }


    // Recibe  el periodo y crea un archivo excel en /usr/share/tomcat8-ameca/ameca/Reportes/2019/liqui_iibb_201901.xls. 
//             doExcel_reporte (id_establecimiento, periodo, base_imponible, saldo_iva, saldo_iva_reporte, saldo_iibb, saldo_iibb_reporte, reporte_suss, reporte_higiene, nombre_responsable, direccion_establecimiento, nro_cuit)

private String doExcel_reporte (String id_establecimiento, String periodo, String autonomo, String saldo_iva, String saldo_iva_reporte, 
                                                    String saldo_iibb, String saldo_iibb_reporte, String reporte_suss, String reporte_higiene, String nombre_responsable, String direccion_establecimiento, String nro_cuit) 
    {
        int rowNum=0;
        String resul="",  FILE_NAME = "/usr/share/tomcat8-ameca/ameca/Reportes/"+periodo.substring(0,4)+"/"+periodo.substring(4,6)+"/reporte_historico_"+id_establecimiento+"_"+periodo+".xls";

        try 
            {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("REPORTE");
            Cell celda;
            Row   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("CUIT:");
                   celda = row.createCell(1);
                   celda.setCellValue(nro_cuit);

            row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("NOMBRE_RESPONSABLE");
                   celda = row.createCell(1);
                   celda.setCellValue(nombre_responsable);
            
            row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("DIRECCION ESTABLECIMIENTO");
                   celda = row.createCell(1);
                   celda.setCellValue(direccion_establecimiento);

            row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("PERIODO");
                   celda = row.createCell(1);
                   celda.setCellValue(periodo);
                   

                   row = sheet.createRow(rowNum++);
                   row = sheet.createRow(rowNum++);
                   row = sheet.createRow(rowNum++);
                   
                   
            celda = row.createCell(0);
            celda.setCellValue("IMPUESTOS");
            celda = row.createCell(1);
            celda.setCellValue("MONTO");
            celda = row.createCell(2);
            celda.setCellValue("MONTO REPORTADO");
  
                   row = sheet.createRow(rowNum++);
            
            celda = row.createCell(0);
            celda.setCellValue("IVA (货物增值税)");
            celda = row.createCell(1);
            celda.setCellValue(saldo_iva);
            celda = row.createCell(2);
            celda.setCellValue(saldo_iva_reporte);

                   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("ARBA/AGIP/IB (营业税)");
            celda = row.createCell(1);
            celda.setCellValue(saldo_iibb);
            celda = row.createCell(2);
            celda.setCellValue(saldo_iibb_reporte);
                   
                   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("AUTONOMO (老板养老金)");
            celda = row.createCell(1);
            celda.setCellValue(autonomo);

                   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("F.931 SUSS (员工养老金)");
            celda = row.createCell(1);
            celda.setCellValue(reporte_suss);

                   row = sheet.createRow(rowNum++);

            celda = row.createCell(0);
            celda.setCellValue("SEGURIDAD E HIGIENE (卫生安全税)");
            celda = row.createCell(1);
            celda.setCellValue(reporte_higiene);

                   

              FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
              workbook.write(outputStream); 
              workbook.close();
              outputStream.close();

               resul="/Reportes/"+periodo.substring(0,4)+"/"+periodo.substring(4,6)+"/reporte_historico_"+id_establecimiento+"_"+periodo+".xls";
            }
        catch (IOException io_ex) {
               resul= "<br><br>ERROR IO: "+io_ex.getMessage()+"<br><br>";
            }      

        catch (Exception ex) {
               resul= "<br><br>ERROR GRAL: "+ex.getMessage()+"<br><br>";
            //Logger lgr = Logger.getLogger(HikariCPEx.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        
        
        return resul;
        }



}
