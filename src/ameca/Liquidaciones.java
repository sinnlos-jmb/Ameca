package ameca; 

/*
 *
 * @author manu
 */

//import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//import manu.utils.*;

public class Liquidaciones extends HttpServlet        
   {  
    
    HTML htm=new HTML();
    public String user_level="0";

    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException

   {  //	htmls.logger.fine("homeOsoc. Carga servlet\n--");
   
       

	HttpSession session = request.getSession(false)!= null ? request.getSession(false): request.getSession();
	user_level  = session.getAttribute("user_level") != null ?  (String)session.getAttribute("user_level") : "0" ;
	String user_name  = session.getAttribute("user_name") != null ?  (String)session.getAttribute("user_name") : "" ;
            

       
    if (!HTML.getIgnited_zonas())
        HTML.Carga_zonas();
    if (!HTML.getIgnited_condiciones_iva())
         HTML.Carga_condiciones_iva();
    if (!HTML.getIgnited_condiciones_iibb())
         HTML.Carga_condiciones_iibb();
//    if (!HTML.getIgnited_periodo())
//         HTML.Carga_periodo();
    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    
  String base_imponible, compra_iva, percepcion_iva, percepcion_iibb;
    
    
    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String do_masive  = request.getParameter ("do_masive") != null ?  request.getParameter ("do_masive") : "0" ; // 1 para insert masivo de establecimientos en un periodo que estaba vacio
                                                                                                                 // 2 para update masivo del periodo actual
    String id_comercio  = request.getParameter ("id_comercio") != null ?  request.getParameter ("id_comercio") : "0" ;
    String nro_cuit  = request.getParameter ("nro_cuit") != null ?  request.getParameter ("nro_cuit") : "0" ;
    String id_establecimiento  = request.getParameter ("id_establecimiento") != null ?  request.getParameter ("id_establecimiento") : "0" ;
    String direccion_establecimiento  = request.getParameter ("direccion_establecimiento") != null ?  request.getParameter ("direccion_establecimiento") : "" ;
    String id_zona  = request.getParameter ("id_zona") != null ?  request.getParameter ("id_zona") : "" ;
    if (id_zona.equals("1"))
        id_zona="";



String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : htm.getPeriodo_nostatic() ;
 //   if (!StringUtils.isStrictlyNumeric(periodo))
       if (!isNumeric(periodo))
           periodo=htm.getPeriodo_nostatic();
String periodo2  = request.getParameter ("periodo2") != null ?  request.getParameter ("periodo2") : "" ;
    if (!isNumeric(periodo2))
        periodo2="";



    base_imponible  = request.getParameter ("base_imponible") != null ?  request.getParameter ("base_imponible") : "0" ;
    //alicuota_iva  = request.getParameter ("alicuota_iva") != null ?  request.getParameter ("alicuota_iva") : "21" ;
    //alicuota_iibb  = request.getParameter ("alicuota_iibb") != null ?  request.getParameter ("alicuota_iibb") : "3" ;
    compra_iva  = request.getParameter ("compra_iva") != null ?  request.getParameter ("compra_iva") : "0" ;
    percepcion_iva  = request.getParameter ("percepcion_iva") != null ?  request.getParameter ("percepcion_iva") : "0" ;
    percepcion_iibb  = request.getParameter ("percepcion_iibb") != null ?  request.getParameter ("percepcion_iibb") : "0" ;

	if (user_level.equals("0"))
	{
     out.println("<!DOCTYPE html><html><head><title>Ameca - Password</title>\n </head> \n <body>  \n\n <br><br> \n");
	 out.println("<script>"
		+   "function go() { window.document.location.replace(\"/ameca/inicio\"); \n ;} \n "
		+   "document.body.style.background='brown'; \n window.setTimeout(go, 10); \n"  // acá habría que poner cartel de logueo
		+ "</script><br>Ingrese al sistema!<br>"
		+ "</body></html>");
	}
	else
	{
    switch (operacion) {
           case "ver_e":
//ver establecimiento, es una sola ficha o agregar de todos los periodos (liqui historica).

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("\n<br><h1>Saldos DDJJ</h1><br><br>" +
                       "\nCUIT: " + nro_cuit + "&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; Direcci&oacute;n: " + direccion_establecimiento +
                       "<br><br><table>"
                       + "<tr><td colspan='3' align='center'>Periodo desde / hasta:</td>"
                       + "<tr><td colspan='3' height='2px'></td>"
                       + "<tr><td><img src='/ameca/imgs/back_go.png'></td>"
                       + "<td><form action='/ameca/liquidaciones' name='f_periodo'>"
                       + "<input type='text' name='periodo' value='" + periodo + "' size='7'><input type='text' name='periodo2' value='" + periodo2 + "' size='7'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_e'>\n" +
                       "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>\n" +
                       "<input type='hidden' name='nro_cuit' value='" + nro_cuit + "'>\n" +
                       "<input type='hidden' name='id_establecimiento' value='" + id_establecimiento + "'>\n" +
                       "<input type='hidden' name='direccion_establecimiento' value='" + direccion_establecimiento + "'>\n" +
                       "</form></td>"
                       + "<td><img src='/ameca/imgs/next_go.png'></td></tr>"
                       + "<tr><td colspan='3' align='center' valign='top'><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.f_periodo.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table>");

               out.println("<br><br>\n" + this.LiquiEstablecimientoTable(id_establecimiento, periodo, periodo2));

               out.println("<br><br><br><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'><img src='/ameca/imgs/back.png'></a> <br><br><br><br>");

               out.println(HTML.getTail());

               break;
           case "ver_c":   //ver las DDJJs de un comercio dada su id_comercio
               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("\n<h2>Saldos DDJJ Comercio: " + nro_cuit + "</h2><br>" +
                       "\n<form action='/ameca/liquidaciones' name='f_periodo'>" +
                       "<table>"
                       + "<tr><td><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"
                       + "<td><input type='text' name='periodo' value='" + periodo + "' size='7'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_c'>\n" +
                       "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>\n" +
                       "<input type='hidden' name='nro_cuit' value'" + nro_cuit + "'>" +
                       "<input type='hidden' name='id_establecimiento' value='" + id_establecimiento + "'>\n" +
                       "</form></td>"
                       + "<td><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>"
                       + "</table>");
               out.println(this.LiquiComercioTable(id_comercio, periodo));
//        out.println("<br><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_pre(periodo)+"'>Mes Anterior</a>"
               //              + " <br><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_prox(periodo)+"'>Mes Siguiente</a><br><br>\n"+this.LiquiComercioTable(id_comercio, periodo));

               out.println("<br><br><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'><img src='/ameca/imgs/back.png'></a> <br>");

               out.println("\n\n" + HTML.getTail());

               break;
           case "ver_m":  //ver liquidaciones masiva total (iva e iibb)

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("<br><h2>Liquidaci&oacute;n Mensual General (IVA e IIBB): " + htm.getPeriodo_prn(periodo) + "</h2><br>" +
                       "\n<form action='/ameca/liquidaciones'>" +
                       "Periodo: <input type='number' name='periodo' value='" + periodo + "' min='201900' max='204311'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_m'>\n" +
                       "<input type='submit'></form>");

               out.println("<br><br>\n" + this.LiquiMesTable(periodo));
               out.println("\n\n<br><br><a href='/ameca/inicio'>Inicio</a><br>\n\n");

               out.println(HTML.getTail());

               break;
           case "ver_mv":   //ver liquidaciones masiva iva

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
//        out.println("\n<br><h2>Liquidaci&oacute;n I.V.A. Mensual: </h2><br>");
               out.println("<br><br>\n"
                       + "<table cellspacing='0'>"
                       + "<tr><td height='11px'> </td></tr>\n"
                       + "<tr><td style='width:5px; height:64px'></td><td><img src='/ameca/imgs/liqui64.svg' style='width:64px;height:64px;'></td><td width='15px'></td> "
                       + "<td style='width:800px; font-family:Arial; font-size:40px; font-weight: bold;'>Liquidaci&oacute;n I.V.A. Mensual</td> <td width='277px'> </td> </tr>"
                       + "<tr><td height='55px'> </td></tr>\n"
                       + ""
                       + "\n\n "
                       + "<tr><td colspan='5'> ");


// aca estaba  la tabla form de prox periodo y generador de excels

               out.println("</td></tr></table>\n\n ");


//        if( (!base_imponible.equals("0") && !base_imponible.equals("+0")) || (!percepcion_iva.equals("0") && !percepcion_iva.equals("+0")) || (!compra_iva.equals("0") && !compra_iva.equals("+0")) )
//            out.println("<br><br>registros modificados: \n"+this.LiquiMesIVA_update(periodo, base_imponible, percepcion_iva, compra_iva));

//        if(do_masive.equals("1"))
//            out.println("<br><br>registros agregados: \n"+this.LiquiMes_masive(periodo, base_imponible, percepcion_iva, percepcion_iibb, compra_iva));
               switch (do_masive) {
                   case "2":
                       out.println("<br><br>registros modificados: \n" + this.LiquiMesIVA_update(periodo, base_imponible, percepcion_iva, compra_iva));
                       break;
                   case "1":
                       out.println("<br><br>registros agregados: \n" + this.LiquiMes_masive(periodo, base_imponible));  // bi es porcentaje gral
                       HTML.setIgnited_periodo(Boolean.FALSE);
                       out.println(":" + htm.getPeriodo_nostatic());
                       break;
               }

               out.println("<br>\n" + this.LiquiMesIVATable(periodo, id_zona));

               out.println("\n\n<br><br><a href='/ameca/inicio'>Inicio</a><br>");
               out.println(HTML.getTail());

               break;
           case "ver_mb":   //liquidaciones masiva iibb
               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("<br><br>\n"
                       + "<table cellspacing='0'>"
                       + "<tr><td height='11px'> </td></tr>\n"
                       + "<tr><td style='width:5px; height:64px'></td><td><img src='/ameca/imgs/liqui64.svg' style='width:64px;height:64px;'></td><td width='15px'></td> "
                       + "<td style='width:800px; font-family:Arial; font-size:40px; font-weight: bold;'>Liquidaci&oacute;n I.I.B.B. Mensual</td> <td width='277px'> </td> </tr>"
                       + "<tr><td height='55px'> </td></tr>\n");
               out.println("\n\n " +
                       "<tr><td colspan='5'> " +
                       "</td></tr></table>\n\n");


//        if( (!base_imponible.equals("0") && !base_imponible.equals("+0")) || (!percepcion_iibb.equals("0") && !percepcion_iibb.equals("+0")))
               switch (do_masive) {
					case "2":
                       out.println("<br><br>registros modificados: \n" + this.LiquiMesIIBB_update(periodo, base_imponible, percepcion_iibb));
                       break;
					case "1":
                       out.println("<br><br>registros agregados: \n" + this.LiquiMes_masive(periodo, base_imponible));  // bi tiene el valor del porcentaje para todos los campos
                       break;
					}
               out.println("<br><br>\n" + this.LiquiMesIIBBTable(periodo, id_zona));

               out.println("\n<br><br><a href='/ameca/inicio'>Inicio</a>\n\n");

               out.println(HTML.getTail());

               break;
       }
       }  // fin del if que pregunta por user_level (ejecuta el bloque de arriba se es 1 o 2)    
   
 }



    
    
    
   
// Recibe  el id_establecimiento y devuelve una tabla de los saldos de sus establecimientos 

    public static String LiquiEstablecimientoTable(String id_establecimiento) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total, percepcion_iva, 
                percepcion_iibb, saldo_ddjj_iva, saldo_ddjj_iibb, alicuota_iva, alicuota_iibb;
	String resul;
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT FORMAT (base_imponible, 2, 'de_DE'), " +
                                                "@debitoF_iva := (base_imponible*alicuota_iva/100), " +   //debito fiscal IVA
                                                "@debitoF_iibb := (base_imponible*alicuota_iibb/100), " +  // 3.  debito fiscal IIBB
                                                "FORMAT (base_imponible+@debitoF_iva, 2, 'de_DE'), " + //venta total
                                                "FORMAT (compra_iva, 2, 'de_DE'), " +
                                                "@iva_credito := (compra_iva*alicuota_iva/100), " +
                                                "FORMAT (compra_iva+@iva_credito, 2, 'de_DE'), " + // 7.  compra total
                                                "percepcion_iva, percepcion_iibb, " +
                                                "FORMAT (@debitoF_iva-@iva_credito-percepcion_iva, 2, 'de_DE'), " +  // 10. SALDO IVA
                                                "FORMAT (@debitoF_iibb-percepcion_iibb, 2, 'de_DE'), "+   // SALDO IIBB
                                                "alicuota_iva, alicuota_iibb, "+ 
                                                "FORMAT (@debitoF_iva, 2, 'de_DE'), "+     // 14.  debito_iva
                                                "FORMAT (@debitoF_iibb, 2, 'de_DE'), "+ 
                                                "FORMAT (@iva_credito, 2, 'de_DE') "+    // 16.  credito_iva
                                       " FROM EstablecimientosLiquiMes " +
                                       " WHERE EstablecimientosLiquiMes.id_establecimiento ="+id_establecimiento);
            
            resul="<table class='bicolor'>\n\t<tr>\n\t\t<th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO D.D.J.J. IVA</th> \n"+
                    "<th>Alic. IIBB</th> <th>Percepcion IIBB</th> <th>Debito IIBB</th> <th>SALDO D.D.J.J. IIBB</th> \n</tr>\n\n";

            rs = pst.executeQuery();
            while (rs.next())
                {
                 base_imponible=rs.getString(1);
                 alicuota_iva=rs.getString(12);
                 alicuota_iibb=rs.getString(13);
                 debito_iva=rs.getString(14);
                 debito_iibb=rs.getString(15);
                 venta_total=rs.getString(4);
                 compra_iva=rs.getString(5);
                 credito_iva=rs.getString(16);
                 compra_total=rs.getString(7);
                 percepcion_iva=rs.getString(8);
                 percepcion_iibb=rs.getString(9);
                 saldo_ddjj_iva=rs.getString(10);
                 saldo_ddjj_iibb=rs.getString(11);

                resul+="\n\t<tr> <td>"+base_imponible+"</td> <td>"+alicuota_iva+"</td> <td>"+debito_iva+"</td> <td>"+venta_total+"</td> <td>"+compra_iva+"</td> "+
                    "<td>"+credito_iva+"</td> <td>"+compra_total+"</td> <td>"+percepcion_iva+"</td> <td>"+saldo_ddjj_iva+"</td> " +
                    "<td>"+alicuota_iibb+"</td> <td>"+percepcion_iibb+"</td> <td>"+debito_iibb+"</td> <td>"+saldo_ddjj_iibb+"</td>\n\t</tr>";
                }
            resul+="\n\t</table>\n";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }



// Recibe  el id_establecimiento + periodo y devuelve una tabla de sus saldos DDJJ (IVA e IIBB) para el periodo 

    private String LiquiEstablecimientoTable(String id_establecimiento, String periodo, String periodo2) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        
	String resul, base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total,
                         percepcion_iva, percepcion_iibb, saldo_ddjj_iva, saldo_ddjj_iibb, alicuota_iva, alicuota_iibb;
        String query="SELECT FORMAT (base_imponible, 2, 'de_DE'), " +
                                                "@debitoF_iva := (base_imponible*alicuota_iva/100), " +   // 2. debito fiscal IVA
                                                "@debitoF_iibb := (base_imponible*alicuota_iibb/100), " +  // debito fiscal IIBB
                                                "FORMAT (base_imponible+@debitoF_iva, 2, 'de_DE'), " + //venta total
                                                "FORMAT (compra_iva, 2, 'de_DE'), " +      // 5. compra iva
                                                "@iva_credito := (compra_iva*alicuota_iva/100), " +
                                                "FORMAT (compra_iva+@iva_credito, 2, 'de_DE'), " + //compra total
                                                "percepcion_iva, percepcion_iibb, " +
                                                "FORMAT (@debitoF_iva-@iva_credito-percepcion_iva, 2, 'de_DE'), " +  // 10. SALDO DDJJ IVA
                                                "FORMAT (@debitoF_iibb-percepcion_iibb, 2, 'de_DE'), "+   // SALDO DDJJ IIBB
                                                "alicuota_iva, alicuota_iibb, "+ 
                                                "FORMAT (@debitoF_iva, 2, 'de_DE'), "+  // 14. debito iva formateada
                                                "FORMAT (@debitoF_iibb, 2, 'de_DE'), "+ 
                                                "FORMAT (@iva_credito, 2, 'de_DE'), periodo "+    // 16.  credito_iva    17. periodo
                                       " FROM EstablecimientosLiquiMes "+
                                       " WHERE  id_establecimiento ="+id_establecimiento ;
         if (periodo2.length()>4)
             query +=" AND periodo >='"+periodo+"' AND periodo <='" + periodo2 + "'   ORDER BY periodo DESC " ;
         else
             query +=" ORDER BY periodo DESC  LIMIT 10" ;
             
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            

            resul="<table class='bicolor'>\n\t<tr>\n\t\t<th>Periodo</th> <th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO IVA</th> "+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> </tr>";

            rs = pst.executeQuery();
            while (rs.next())
                {
                 base_imponible=rs.getString(1);
                 alicuota_iva=rs.getString(12);
                 alicuota_iibb=rs.getString(13);
                 debito_iva=rs.getString(14);
                 debito_iibb=rs.getString(15);
                 venta_total=rs.getString(4);
                 compra_iva=rs.getString(5);
                 credito_iva=rs.getString(16);
                 compra_total=rs.getString(7);
                 percepcion_iva=rs.getString(8);
                 percepcion_iibb=rs.getString(9);
                 saldo_ddjj_iva=rs.getString(10);
                 saldo_ddjj_iibb=rs.getString(11);
                 periodo=rs.getString(17);

                resul+="\n\t<tr><td>" + periodo + "</td><td>"+base_imponible+"</td> <td>"+alicuota_iva+"</td> <td>"+debito_iva+"</td> <td>"+venta_total+"</td> <td>"+compra_iva+"</td> "+
                    "<td>"+credito_iva+"</td> <td>"+compra_total+"</td> <td>"+percepcion_iva+"</td> <td><b>"+saldo_ddjj_iva+"</b></td> " +
                    "<td>"+alicuota_iibb+"</td> <td>"+debito_iibb+"</td> <td>"+percepcion_iibb+"</td> <td><b>"+saldo_ddjj_iibb+"</b></td>\n\t</tr>";
                }
            resul+="\n\t</table>\n";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
    

    
    
    
    
// Recibe  el id_comercio + periodo y devuelve una tabla de sus saldos DDJJ (IVA e IIBB) para el periodo 

    private String LiquiComercioTable(String id_comercio, String periodo) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int i=0;
        String resul, nombre_establecimiento, query;
        float base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total,
                percepcion_iva, percepcion_iibb, saldo_iva, saldo_iibb, alicuota_iva, alicuota_iibb,
                sum_bi=0f, sum_debito_iva=0f, sum_debito_iibb=0f, sum_venta_total=0f, sum_compra_iva=0f, sum_credito_iva=0f, sum_compra_total=0f,
                sum_percepcion_iva=0f, sum_percepcion_iibb=0f, sum_saldo_iva=0f, sum_saldo_iibb=0f;
        query="SELECT  elm.base_imponible, elm.compra_iva, elm.percepcion_iva, elm.percepcion_iibb, "+
                      "e.nombre_establecimiento, e.direccion_establecimiento, e.id_zona, e.id_localidad, " +   
                      "elm.alicuota_iva, elm.alicuota_iibb, elm.saldo_iva, elm.saldo_iibb " +   
             " FROM EstablecimientosLiquiMes elm, Establecimientos e " +
             " WHERE elm.periodo = '"+periodo+"' AND elm.id_establecimiento = e.id_establecimiento AND e.id_comercio="+id_comercio;
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            

            resul="<table class='grupos'>\n\t<tr>\n\t\t <th>Nombre Establecimiento</th> <th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO IVA</th> <th style=\"background-color: #000000; padding: 1px;\"> </th>"+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> </tr>";

            rs = pst.executeQuery();
            
            while (rs.next())
                {
                 i++;

                 base_imponible=rs.getFloat(1);
                 alicuota_iva=rs.getFloat(9);
                 alicuota_iibb=rs.getFloat(10);
                 debito_iva=base_imponible*alicuota_iva/100;
                 debito_iibb=base_imponible*alicuota_iibb/100;
                 venta_total=base_imponible+debito_iva;
                 compra_iva=rs.getFloat(2);
                 credito_iva=compra_iva*alicuota_iva/100;
                 compra_total=compra_iva+credito_iva;
                 percepcion_iva=rs.getFloat(3);
                 percepcion_iibb=rs.getFloat(4);
                 saldo_iva=rs.getFloat(11);
                 saldo_iibb=rs.getFloat(12);
                 nombre_establecimiento=rs.getString(5);
                 //direccion_establecimiento=rs.getString(5);

                 sum_bi+=base_imponible;
                 sum_debito_iva+=debito_iva;
                 sum_debito_iibb+=debito_iibb;
                 sum_venta_total+=venta_total;
                 sum_compra_iva+=compra_iva;
                 sum_credito_iva+=credito_iva;
                 sum_compra_total+=compra_total;
                 sum_percepcion_iva+=percepcion_iva;
                 sum_percepcion_iibb+=percepcion_iibb;
                 sum_saldo_iva+=saldo_iva;
                 sum_saldo_iibb+=saldo_iibb;
                 

                resul+="\n\t<tr><td>"+nombre_establecimiento+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",base_imponible)+"</td> <td>"+alicuota_iva+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",venta_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",compra_iva)+"</td> "+
                    "<td>"+String.format(Locale.GERMAN, "%,.2f",credito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",compra_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iva)+"</td> <td style=\"background-color: #000000; padding: 1px;\"></td>" +
                    "<td>"+alicuota_iibb+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iibb)+"</td>\n\t</tr>";
                }
            
            if (i>1)
             resul+="\n\t<tr><td colspan='15' height='1px'  style=\"background-color: #000000; padding: 0px;\"></td></tr>"+
                      "<tr><td colspan='10' height='1px'></td> <td style=\"background-color: #000000; padding: 1px;\"></td> <td colspan='4' height='2px'></td>  </tr>  <tr><td>TOTAL</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_debito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_venta_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_iva)+"</td> "+
                     "<td>"+String.format(Locale.GERMAN, "%,.2f",sum_credito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo_iva)+"</td> <td style=\"background-color: #000000; padding: 1px;\"></td>" +
                     "<td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo_iibb)+"</td>\n\t</tr>";            

            resul+="\n\t</table>";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
    
    
    
    


// Recibe  el periodo y devuelve una tabla con los saldos DDJJ (IVA e IIBB) de los comercios para el periodo 

    private String LiquiMesTable(String periodo) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int z, i=0, k=0;
        String resul;
        Float cmrc_bi, cmrc_compra=0f, cmrc_percepcion_iva=0f, cmrc_percepcion_iibb=0f, debito_iibb=0f,  saldo_iibb, saldo_iva;
        Float[][] flotantes=new Float[6][10];  // base imponible, alic_iva, alic_iibb, compra_iva, percepcion_iva, percepcion_iibbi
        int[][] enteros=new int[6][6];  // id_comercio, id_zona, id_condicion_iva (siempre resp. insc=2), id_condicion_iibb, id_establecimiento, id_establecimiento_mes
        String [][] cadenas = new String [6][3];  // cuit, nombre, direccion
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT c.nro_cuit, c.nombre_responsable, e.direccion_establecimiento, " +
                                                                    "em.base_imponible, " + //4  base imponible
                                                                    "em.compra_iva, " +  // 5. comopra
                                                                    "em.percepcion_iva, " +    // 6. percepcion IVA
                                                                    "em.percepcion_iibb, " +   //  7.  percepcion IIBB
                                                                    "em.alicuota_iibb, " +  //   8. alicuota IIBB
                                                                    "c.id_comercio, " + //  9.  id_comercio
                                                                    "e.id_establecimiento, " + //  10
                                                                    "em.id_establecimiento_mes, " + //  11   id_establecimiento_mes     para observaciones y exclusiones
                                                                    "c.id_condicion_iva, " + //  12   id_condicion_iva
                                                                    "c.id_condicion_iibb, " + //  13   id_condicion_iibb
                                                                    "e.id_zona, em.saldo_iva, em.saldo_iibb, "  + // 16
                                                                    "em.compra_iva*em.alicuota_iva/100 as 'credito iva',  " + //  17    credito iva
                                                                    "em.base_imponible*em.alicuota_iva/100 as 'd_iva',  " + //  18   debito iva
                                                                    "em.base_imponible*em.alicuota_iibb/100 as 'd_iibb'  " + //  19  debito iibb
                                                           " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                                           " WHERE c.id_comercio = e.id_comercio " + 
                                                               " AND e.id_establecimiento=em.id_establecimiento " +
                                                               " AND em.periodo ='"+periodo+"' AND (activo_iva_periodo OR activo_iibb_periodo) "+ 
                                                           " ORDER BY c.nro_cuit");  
            

            resul="\n\n<table class='grupos' width='1800px'>\n\t<tr>\n\t\t"+
                    "<th>CUIT</th> <th>Nombre</th> <th>Direccion Establecimiento</th> \n"+
                    "<th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> \n"+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO  IVA</th> \n"+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> <th>Observaciones</th> \n</tr>\n\n";

            rs = pst.executeQuery();

            while (rs.next())
                {
                k++;
                enteros[i][0]=rs.getInt(9);    // id_comercio
                
                if (enteros[i][0]!=enteros[0][0])   // otro establecimiento dle mismo comercio 
                     {
                        if (i==1)   // unico establecimiento  en [0], en [1] tengo otro establecimiento que no se si es unico o no
                           {
                            resul+="\n\t<tr> <td>"+cadenas[0][0]+"</td> <td>"+cadenas[0][1]+"</td> <td>"+cadenas[0][2]+", "+htm.getZona(enteros[0][5])+"</td> "+
                                   "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td>"+htm.getIVA_prn()+"</td> "+
                                   "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*htm.getIVA()+flotantes[0][0])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1])+"</td> "+
                                   "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1]*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1]*htm.getIVA()+flotantes[0][1])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][2])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5])+"</td> " +
                                   "<td>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*flotantes[0][4]/100)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][3])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][6])+"</td> " +
                                   " \n\t <td></td>  </tr> \n ";
                          }
                        else // mas de un establecimiento del mismo comercio, ie, con mismo id_comercio
                           { 
                            z=0;
                            resul+="\n\t<tr> <td rowspan='"+Integer.toString(i)+"'>"+cadenas[0][0]+"</td> <td rowspan='"+Integer.toString(i)+"'>"+cadenas[z][1]+"</td> <td class='t2'>"+cadenas[z][2]+", "+htm.getZona(enteros[z][5])+"</td> "+
                                 "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td class='t2'>"+htm.getIVA_prn()+"</td> "+
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][8])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][8]+flotantes[z][0])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][1])+"</td> "+
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][7]+flotantes[z][1])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][2])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][5])+"</td> " +
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][9])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][3])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][6])+"</td> " +
                                 " \n\t <td class='t2'></td></tr>";
                            cmrc_bi=flotantes[0][0];
                            saldo_iva=flotantes[0][5];
                            saldo_iibb=flotantes[0][6];
                            debito_iibb+=flotantes[0][9];

                            for (z=1;z<i;z++)
                                    {resul+="\n\t<tr> <td class='t2'>"+cadenas[z][2]+" </td> "+
                                          "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0])+"</td> <td class='t2'>"+htm.getIVA_prn()+"</td> "+
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0]*htm.getIVA())+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0]*htm.getIVA()+flotantes[z][0])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][1])+"</td> "+
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][7])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f", flotantes[z][7]+flotantes[z][1])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][2])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][5])+"</td> " +
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",flotantes[z][4])+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f", debito_iibb)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][3])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][6])+"</td> " +
                                          " \n\t <td class='t2'></td></tr>";
                                      cmrc_bi+=flotantes[z][0];
                                      saldo_iva+=flotantes[z][5];
                                      saldo_iibb+=flotantes[z][6];
                                      debito_iibb+=flotantes[z][9];
                                    }

                           resul+="\n\t<tr> <td colspan='3'></td> "+  
                             "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td> </td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi*htm.getIVA())+"</td> "+
                                  "<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi+cmrc_bi*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
                                  "<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra+cmrc_compra*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iva)+"</td> " +
                                  "<td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iibb)+"</td> " +
                                  " \n\t <td></td></tr> " ;

                     }
                   // esto lo ejecuta despues de haber impreso un establecimiento, sea sucursal o unico.
                    enteros[0][0]=enteros[i][0];  // id_establecimiento
                   i=0;
                   }   //  fin del if-else que pregunta por si el nuevo id_comercio es igual que el que esta almacenado en flotantes[0][0]
                
                    flotantes[i][0]=rs.getFloat(4);  // base imponible
                    flotantes[i][1]=rs.getFloat(5);  // compra_iva
                    flotantes[i][2]=rs.getFloat(6);  // percepcion_iva
                    flotantes[i][3]=rs.getFloat(7);  // percepcion_iibb
                    flotantes[i][4]=rs.getFloat(8);  // alicuota_iibb
                    flotantes[i][5]=rs.getFloat(15);  // saldo_iva
                    flotantes[i][6]=rs.getFloat(16);  // saldo_iibb
                    flotantes[i][7]=rs.getFloat(17);  // credito_iva x compras
                    flotantes[i][8]=rs.getFloat(18);  // debito_iva
                    flotantes[i][9]=rs.getFloat(19);  // debito_iibb

                    enteros[i][1]=rs.getInt(10);  // id_establecimiento
                    enteros[i][2]=rs.getInt(11);  // id_establecimiento_mes
                    enteros[i][3]=rs.getInt(12);  // id_condicion_iva
                    enteros[i][4]=rs.getInt(13);  // id_condicion_iibb
                    enteros[i][5]=rs.getInt(14);  // id_zona

                    cadenas[i][0]=rs.getString(1);  // nro_cuit
                    cadenas[i][1]=rs.getString(2);  // nombre_responsable
                    cadenas[i][2]=rs.getString(3);  // direccion establecimiento
                    
                    i++;

        }   // fin del while que recorre recordset

            
        // el ultimo establecimiento queda en la posicion 0
        if(k>1)
            resul+="\n\t<tr> <td>"+cadenas[0][0]+"</td> <td>"+cadenas[0][1]+"</td> <td>"+cadenas[0][2]+", "+htm.getZona(enteros[0][5])+"</td> "+
               "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td>"+htm.getIVA_prn()+"</td> "+
               "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5]+flotantes[0][0])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1])+"</td> "+
               "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7]+flotantes[0][1])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][2])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][8])+"</td> " +
               "<td>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][9])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][3])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][9])+"</td> " +
               " \n\t <td></td>  </tr> \n "+
               "\n\t</table><br><br>Cantidad de Establecimientos: "+ k;
        else
            resul="<br><br>No hay actividades en el periodo.<br><br><br><br><br>";

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
                resul= ex.getMessage();
                }
            }
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
 

    
// Recibe  el periodo y devuelve una tabla con los saldos DDJJ IVA de los comercios para el periodo 

    private String LiquiMesIVATable(String periodo, String id_zona)
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

	String resul, row_span, zona, query, nombre_responsable, nro_cuit, direccion_establecimiento, id_establecimiento, id_comercio, perseus_resul="";
        float cmrc_bi, cmrc_debito, cmrc_venta_total, cmrc_compra, cmrc_credito, cmrc_compra_total, cmrc_percepcion, cmrc_saldo, cmrc_alic,
                sum_bi=0f, sum_debito=0f, sum_venta_total=0f, sum_compra=0f, sum_percepcion=0f, sum_saldo=0f, sum_credito=0f, sum_compra_total=0f;
        int rows=0, int_zona, length_obs;
        try 
            {
            con=CX.getCx_pool();
            query="SELECT c.nro_cuit, c.nombre_responsable, CASE " +
							"    WHEN casa_matriz =1 THEN CONCAT(\"(CM) \",e.direccion_establecimiento)\n" +
							"    ELSE CONCAT(\"(suc) \",e.direccion_establecimiento)\n" +
							"END as 'direccion', " +
                            "em.base_imponible, " + //4
                            "em.compra_iva, " +  //5
                            "em.percepcion_iva, " +   // 6  percepciojn IVA
                            "e.id_zona, "+   //  7  zona
                            "(SELECT COUNT(*) " +
                            " FROM EstablecimientosLiquiMes eem, Establecimientos ee ";

            if (id_zona.equals(""))
                query+=" WHERE periodo='"+periodo+"' AND eem.id_establecimiento=ee.id_establecimiento " +
                                "   AND ee.id_comercio=e.id_comercio AND eem.activo_iva_periodo=1 AND c.id_condicion_iva!=1), " +  //  8   COUNT
                                "em.saldo_iva, em.alicuota_iva, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +  //9  saldo_iva  10  alicuota   11 id_establecimiento    12 id_comercio     13  length (observaciones)
                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iva_periodo=1 AND c.id_condicion_iva!=1 " + 
                           " AND e.id_establecimiento=em.id_establecimiento " +
                           " AND em.periodo ='"+periodo+"' " +
                       " ORDER BY c.nro_cuit, e.id_zona";
            else
                query+=" WHERE periodo='"+periodo+"'  AND ee.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+id_zona+") " +
                                "   AND eem.id_establecimiento=ee.id_establecimiento  " +
                                "   AND ee.id_comercio=e.id_comercio AND eem.activo_iva_periodo=1 AND c.id_condicion_iva!=1), " +  //  8   COUNT
                                "em.saldo_iva, " +  //9  saldo_iva
                                "em.alicuota_iva, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +  //10  alicuota    11 id_establecimiento    12  id_comercio
                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iva_periodo=1 AND c.id_condicion_iva!=1 " + 
                           " AND e.id_establecimiento=em.id_establecimiento " +
                           " AND em.periodo ='"+periodo+"'  AND e.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+id_zona+") "+
                       " ORDER BY c.nro_cuit";
                
                
                
            pst = con.prepareStatement(query);  
            rs = pst.executeQuery();
            
            resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                    "<input type='hidden' name='operacion' value='ver_mv'>"+
            "<table >"+
            "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
            "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
            "\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
            "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
            "<td width='40px'> </td>"
        + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>";

        if (id_zona.equals(""))
        {
                resul+= "<td width='500px'></td>"+
                              "\n<td><a href='/ameca/reportes?operacion=excel&ref=iva&periodo="+periodo+"'><img src='/ameca/imgs/guarda_xls.png' style='width:48px;height:48px;'></a>"+
                              "</td>";

                File tempFile = new File(HTML.folder+"/Reportes/"+htm.getPeriodo_year()+"/liqui_iva_"+periodo+".xls");

                if (tempFile.exists())
                    {
                        Random rand = new Random();
                        int n = rand.nextInt(5988);                
                        resul+="\n<td width='25px'></td><td><a href='/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iva_"+periodo+".xls?a="+n+"'><img src='/ameca/imgs/excel.png' style='width:48px;height:48px;'></a></td>";

                    }
        }
        resul+="</tr>"+
                 "</table> \n"+
                 "</form> \n"+
                 "<br><br>";

            if (HTML.perseus_bool_iva && id_zona.equals("") && periodo.equals(htm.getPeriodo_nostatic()))   //que verifique también que sea periodo actual y poner variables booleanas si está la tabla en string o no (puede que esté sólo tabla de iva o iibb)
						return resul+HTML.perseus_iva;
						
                 
        perseus_resul="<table class='grupos' width='1910px'>\n\t<tr>\n\t\t"+
								"<th width='150px'>CUIT</th> <th width='250px'>Nombre</th>  <th width='310px'>Direccion Establecimiento</th> <th width='120px'>Zona</th> \n "+
								"<th width='120px'>Base Imponible</th> <th width='70px'>Alic. IVA</th> <th width='120px'>Debito IVA</th> <th width='120px'>Venta Total</th> " +
								"<th width='120px'>Compra IVA</th>\n <th width='110px'>Credito IVA</th> <th width='120px'>Compra Total</th> "+
								"<th width='120px'>Percepcion IVA</th> <th width='140px'>SALDO IVA</th> <th width='40px'>Nota</th>\n "+
								"</tr>\n";

            int i=-1;
            while (rs.next())
                {
                int_zona= rs.getInt(7);
                zona=htm.getZona(int_zona);
                length_obs=rs.getInt(13);

                cmrc_alic=rs.getFloat(10);

                cmrc_bi=rs.getFloat(4);
                cmrc_compra=rs.getFloat(5);
                cmrc_percepcion=rs.getFloat(6);
                cmrc_debito=cmrc_bi*cmrc_alic/100;
                cmrc_venta_total=cmrc_bi+cmrc_debito;
                cmrc_credito=cmrc_compra*cmrc_alic/100;
                cmrc_compra_total=cmrc_compra+cmrc_credito;
                cmrc_saldo=rs.getFloat(9);

                id_establecimiento= rs.getString(11);
                id_comercio= rs.getString(12);
                nro_cuit=rs.getString(1);
                nombre_responsable=rs.getString(2);
                direccion_establecimiento=rs.getString(3);

                rows=rs.getRow();
                
                if (i<0)    //entra la primera vez y cuando se terminó con los establecimientos de un comercio
                    {
                     i=rs.getInt(8);
                     row_span=Integer.toString(i+1);                      
                     
                     if (i==1)  // unico establecimiento del comercio     /ameca/comercios?operacion=detalle&nro_cuit=23-24963650-9&id_comercio=326
                        {
                         perseus_resul+="\n\t<tr> <td class='t11' height='30px'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>"+nro_cuit+"</a></td> <td class='t11'>"+nombre_responsable+"</td>   <td class='t11'>"+direccion_establecimiento.substring(5)+"</td> <td class='t11'> "+zona+"</td> "+
												"\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
												"<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
												"<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                         if (length_obs>0)
                                perseus_resul+= "<td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr> \n";
                         else
                                perseus_resul+= "<td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr> \n";

                             
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;                         
                         sum_compra=0f;                         
                         sum_venta_total=0f;                         
                         sum_credito=0f;                         
                         sum_compra_total=0f;                         
                        }
                     else   // al menos dos, imprimo el primer establecimiento de la serie
                        {
                         perseus_resul+="\n\t<tr> <td rowspan='"+row_span+"' class='t11' valign='middle'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>" + nro_cuit + "</a></td> <td rowspan='"+row_span+"' class='t11' valign='middle'>"+nombre_responsable+"</td>  <td class='t2'> <b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+zona+"</b></td> "+
                                "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
                                "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
                                "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr>";
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr>";
                             
 
                         i--;
                         sum_bi+=cmrc_bi;
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;                   
                         sum_compra+=cmrc_compra;                   
                         sum_venta_total+=cmrc_venta_total;                     
                         sum_credito+=cmrc_credito;       
                         sum_compra_total+=cmrc_compra_total;                    
                        }
                    }
                else if (i>0)   // desde el segundo hasta la ultimo establecimiento
                    {
                     perseus_resul+="\n\t<tr> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+zona+"</b></td> "+
											"\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
											"<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
											"<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                               
                     if (length_obs>0)                     
                            perseus_resul+="<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n  \n\t</tr>";
                     else
                            perseus_resul+="<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n  \n\t</tr>";
                         

                         i--;
                         sum_bi+=cmrc_bi;
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;                   
                         sum_compra+=cmrc_compra;                   
                         sum_venta_total+=cmrc_venta_total;                     
                         sum_credito+=cmrc_credito;       
                         sum_compra_total+=cmrc_compra_total;                    
                     if (i==0)  // ya imprimio` todos los establecimientos del comercio, por lo que debe imprimir el total
                        {
    //                     resul+="\n\t<tr class='t1'> <td colspan='3'></td> "+  //imprimo 'totales' o no.
                           perseus_resul+="\n\t<tr>  <td class='t11' height='25px'></td> <td class='t11'></td>  "+  //imprimo 'totales' o no.
                                "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td class='t11'> </td> "+
                                "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_debito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_venta_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_compra)+"</td> "+
                                "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_credito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo)+"</td> " +
                                " <td class='t11'></td>\n\n\t</tr>  " ;
 
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;                         
                         sum_compra=0f;                         
                         sum_venta_total=0f;                         
                         sum_credito=0f;                         
                         sum_compra_total=0f;                         
                        }

                    }
                
                }   // fin del while que recorre recordset

            perseus_resul+="\n\t</table>";

            
// cookie y llave para habilitar esta seccion (no cookie .:. llave que pide pwd con javascript prompt x ahora y despues pop up)
            // un pwd ingresa cookie por semana y otro por dia. (el pwd por dia, es ddyyyymm --> HEX)
            
            // pop de inicio para verificar el pwd que ingreso desde un prompt de javascript. 
            // Si esta ok, guarda el cookie y reinicia la ventana que lo abrio y se cierra. En caso que este ko da mensaje pwd equivocado y se cierra. 
            
            if (user_level.equals("1") || user_level.equals("2") )
                    {
                        //resul+="<br><br><br>HAY COOKIE !!!: "+htm.getPWD_day()+"<br><br><br>";
                     if(rows<1)  //no hay resultados en el periodo (pregunta cargar los del periodo anterior)
                                {
									perseus_resul="";
                                    if(!periodo.equals(htm.getPeriodo_nostatic()))
                                            resul="<table >"+
														"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
														"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
														"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
														"</tr>"+
														"</table> \n"+
														"<br><br>"
													+ ""
													+ "<br><br><br><br>No se encontro actividad para el periodo. <br>"
													+ "Carga el nuevo periodo con los establecimientos del periodo anterior? <br><br><br>"
													+ "<form name='masive' action='/ameca/liquidaciones'> \n <input type='hidden' name='operacion' value='ver_mv'> <input type='hidden' name='do_masive' value='1'> \n <input type='hidden' name='periodo' value='"+periodo+"'> \n "
													+ "<table><tr><td>Porcentaje a aplicar: <input type='text' name='base_imponible' value='+0' size='4'></td> <td width='35px'></td> <td><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.masive.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"
													+ "</tr></table>\n"
													+ "</form>\n";

                                    else
                                            resul=  "<form action='/ameca/liquidaciones' name='periodo_zona'>"+
															"<input type='hidden' name='operacion' value='ver_mv'>"+
															"<table >"+
															"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
															"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
															"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
															"<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
															"<td width='40px'> </td>"+
															"<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"+
															"</tr>"+
															"</table> \n"+
															"</form> \n"+
															"<br>"
														+ ""
														+ "<br><br><br><br>No se encontro actividad para el periodo. <br>";
                                        
                                    resul+="<br><br><br><br>";
                                }

                    else 
                              {
                                  if(id_zona.equals(""))
                                      perseus_resul+="<br><br><br><br>Actualizacion de todos los establecimientos. Ingrese porcentajes.<br>\n"+
															  "<form action='/ameca/liquidaciones' name='porcentajes'> \n <input type='hidden' name='operacion' value='ver_mv'>  \n"
															  + "<input type='hidden' name='do_masive' value='2'> "
															  + "<input type='hidden' name='periodo' value='"+periodo+"'>"+
															  "<table>\n<tr><td>Base imponible: <input type='text' name='base_imponible' value='+0' size='4'></td>\n"+
															  "<td>Compras: <input type='text' name='compra_iva' value='+0' size='4'></td>\n"+
															  "<td>Percepciones: <input type='text' name='percepcion_iva' value='+0' size='4'></td> <td> &nbsp;&nbsp;&nbsp; <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.porcentajes.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> \n"+
															  "</tr>\n</table>\n"+
															  "</form>";
                                  perseus_resul+="<br><br><br> registros: "+ rows;
                              }

                    }
            else
                { 
                    if(rows<1)
                                {
									resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
												"<input type='hidden' name='operacion' value='ver_mv'>"+
												"<table>"+
												"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
												"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
												"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
												"</tr>"+
												"</table> \n"+
												"</form> \n"+
												"<br><br>"+
												""+
												"<br><br><br><br>No se encontro actividad para el periodo. <br>";
									perseus_resul="";
								}
                    resul+="\n<br><br><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mv','MyWindow','width=600,height=300'); return false;\">"
								+ "<table><tr><td> PWD </td> <td> <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</td>"
								+ "</tr></table>"
								+ "</a><br><br> <br><br><br>";
                }
       //     resul+="\n<script>function go() {window.location.replace(\"/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mv\");} \n document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script>  ";
         //   resul+="<br><br><a href='/ameca/inicio?operacion=pwd&place=liquidaciones&dire=ver_mv&periodo="+periodo+"'> PWD <img src='/ameca/imgs/red_no.png' style='width:11px;height:11px;'></a><br><br>";
            
            
                


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
                resul= ex.getMessage();
                }
            }
        
		if (id_zona.equals("") && periodo.equals(htm.getPeriodo_nostatic())) 	// perseus sigue siendo verdadero como lo establecí al inicio, ie, no hubo updates en lo que dura el armado de la tabla de liquidaciones
				{ 	HTML.perseus_iva=perseus_resul+"<br><br><br> version perseus";
					HTML.perseus_bool_iva=true;
				}

		
				
        return resul+perseus_resul;  //tabla con datos de liquidacion del establecimiento... guardar la tabla en variable static en HTML, y borrar la variable cuando se hace un update o insert, o mejor sólo cuando se cambia algun campo (agregar comercio, editar comercio, nuevo mes "masivo", etc.). Mientras no haya cambios, será super rápida la visualizacion de las liquidaciones (sólo las del periodo actual [IIBB e IVA]).

        }
 
    
    

//    Recibe  el periodo y devuelve una tabla con los saldos DDJJ IIBB de los comercios para el periodo 
    private String LiquiMesIIBBTable(String periodo, String zona) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        int i=-1, id_zona, condicion_iibb, rows=0, length_obs ;
        String resul, row_span, query, nom_zona, id_establecimiento, direccion_establecimiento, nombre_responsable, nro_cuit, id_comercio, perseus_resul="";
        float cmrc_bi, cmrc_alic, cmrc_debito, cmrc_percepcion, cmrc_saldo, sum_bi=0.0f, sum_debito=0f, sum_percepcion=0f, sum_saldo=0f;

        try 
            {
			con=CX.getCx_pool();

           query="SELECT c.nro_cuit, c.nombre_responsable, CASE " +
                                                                                        "    WHEN casa_matriz =1 THEN CONCAT(\"(CM) \",e.direccion_establecimiento)\n" +
                                                                                        "    ELSE CONCAT(\"(suc) \",e.direccion_establecimiento)\n" +
                                                                                        "END as 'direccion', " +
                                                 "em.base_imponible, " + // 4
                                                "em.percepcion_iibb, " + // 5
                                                "em.saldo_iibb, " +     // 6
                                                "em.alicuota_iibb, " +  // 7
                                                "e.id_zona, " +     // 8  zona
                                                "(SELECT COUNT(*) "+
                                                  "FROM EstablecimientosLiquiMes eem, Establecimientos ee ";
            if(zona.equals(""))
                 query+="WHERE eem.periodo='"+periodo+"' AND eem.id_establecimiento=ee.id_establecimiento "+ //AND c.id_condicion_iibb not in (2, 4, 5) "+
                                                        " AND ee.id_comercio=e.id_comercio and eem.activo_iibb_periodo=1) as 'sucursales', c.id_condicion_iibb, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones) " +   // 9. count  10. cond iibb   11. id_establecimiento  12. id_comercio    13. length observaciones (si es cero muestro notepad_gris.png).
                                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                       " WHERE c.id_comercio = e.id_comercio AND em.activo_iibb_periodo=1" + 
                                           " AND e.id_establecimiento=em.id_establecimiento AND em.periodo='"+periodo+"' " +
                                       " ORDER BY nro_cuit, e.id_zona";
            else
                 query+="WHERE eem.periodo='"+periodo+"' AND ee.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+zona+") AND eem.id_establecimiento=ee.id_establecimiento"+
                                                        " AND ee.id_comercio=e.id_comercio and eem.activo_iibb_periodo=1) as 'sucursales', c.id_condicion_iibb, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +   // 9. count 10. cond iibb   11. id_establecimiento   12. id_comercio   13. length observaciones (si es cero muestro notepad_gris.png).
                                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iibb_periodo=1 "+ //AND c.id_condicion_iibb not in (2, 4, 5) " + 
                                           " AND e.id_establecimiento=em.id_establecimiento AND em.periodo='"+periodo+"' AND e.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+zona+") "+
                                       " ORDER BY nro_cuit";
                                                  

                
            pst = con.prepareStatement(query); 

            resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                        "<input type='hidden' name='operacion' value='ver_mb'>"+
                        "<table >"+
                        "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                        "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
                        "\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
                        "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
                        "<td width='40px'> </td>"
                    + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>";

        if (zona.equals(""))
            {
                resul+= "<td width='500px'></td>"+
                              "\n<td><a href='/ameca/reportes?operacion=excel&ref=iibb&periodo="+periodo+"'><img src='/ameca/imgs/guarda_xls.png' style='width:48px;height:48px;'></a>"+
                              "</td>";

                File tempFile = new File(HTML.folder+"/Reportes/"+htm.getPeriodo_year()+"/liqui_iibb_"+periodo+".xls");

                if (tempFile.exists())
                        {
                        Random rand = new Random();
                        int n = rand.nextInt(5988);                
                        resul+="\n<td width='25px'></td><td><a href='/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iibb_"+periodo+".xls?a="+n+"'><img src='/ameca/imgs/excel.png' style='width:48px;height:48px;'></a></td>";

                        }
            }
        resul+="</tr>"+
                 "</table> \n"+
                 "</form> \n"+
                 "<br><br>";


            if (HTML.perseus_bool_iibb && zona.equals("") && periodo.equals(htm.getPeriodo_nostatic()))   //que verifique también que sea periodo actual y poner variables booleanas si está la tabla en string o no (puede que esté sólo tabla de iva o iibb)
						return resul+HTML.perseus_iibb;
						
						                 
         perseus_resul="<table class='grupos' width='1420px'>\n\t<tr>\n\t\t"+
                    "<th width='150px'>CUIT</th> <th width='300px'>Nombre</th>  <th width='150px'>Condicion IIBB</th> <th width='310px'>Direccion Establecimiento</th> <th width='120px'>Zona</th> "+
                    "<th width='120px'>Base Imponible</th> <th width='120px'>Alic. IIBB</th> <th width='120px'>Debito IIBB</th>  "+
                    "<th>Percepcion IIBB</th> <th>SALDO IIBB</th> <th width='40px'>Nota</th> "+
                    "    </tr>";

            rs = pst.executeQuery();
            
            
            while (rs.next())
                {
                id_zona= rs.getInt(8);
                nom_zona=htm.getZona(id_zona);
                condicion_iibb=rs.getInt(10);
                id_establecimiento=rs.getString(11);
                id_comercio=rs.getString(12);
                length_obs=rs.getInt(13);
                

                cmrc_bi=rs.getFloat(4);
                cmrc_percepcion=rs.getFloat(5);
                cmrc_alic=rs.getFloat(7);
                cmrc_saldo=rs.getFloat(6);
                cmrc_debito= cmrc_bi*cmrc_alic/100;
                rows=rs.getRow(); 

                nro_cuit=rs.getString(1);
                nombre_responsable=rs.getString(2);
                direccion_establecimiento=rs.getString(3);

                if (i<0)  //entra la primera vez y cuando se termino` con los establecimientos de un comercio
                    {
//                     row_span=rs.getString(9);
                     i=rs.getInt(9);
                     row_span=Integer.toString(i+1);   // cantidad de establecimientos del comercio
                     
                     
                     if (i==1)  // unico establecimiento del comercio
                        {
                         perseus_resul+="\n\t<tr> <td class='t11' height='30px'><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'>" + nro_cuit + "</a></td> <td class='t11'>"+nombre_responsable+"</td>  <td class='t11'>"+HTML.getCondicionIIBB(condicion_iibb)+"</td> <td class='t11'>"+direccion_establecimiento.substring(5)+"</td> <td class='t11'> "+nom_zona+"</td> "+
                            "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> <td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\"  onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>\n ";
                         else
                                perseus_resul+= "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> <td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\"  onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>\n ";
 //                           "\n\t</tr>\n <tr> <td colspan='8' bgcolor='#E8E7C1' height='1px'> </td></tr>";
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;
                        }
                     else     // hay al menos dos establecimientos del comercio... imprimo el primero y decremento el count de sucursales
                        {
                         perseus_resul+="\n\t<tr> <td rowspan='"+row_span+"' class='t11' valign='middle'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>"+nro_cuit+"</a></td> <td rowspan='"+row_span+"' class='t11' valign='middle'>"+nombre_responsable+"</td>  <td rowspan='"+row_span+"' class='t11' valign='middle'>"+HTML.getCondicionIIBB(condicion_iibb)+"</td> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+nom_zona+"</b></td> "+
                            "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> "+
                            "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td>  ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;

                         
                            
                         i--;
                         sum_bi+=cmrc_bi;
                         
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;
                        }
                         
                    }
                else if (i>0)   // proceso de la segunda sucursal en adelante
                    {
                     perseus_resul+="\n\t<tr> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+nom_zona+"</b></td> "+
											   "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+cmrc_alic+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> "+
											   "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td>  ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;

                     
                     
                       
                     i--;
                     //acumular floats, me parece que mejor sumar tambien saldo iibb por si el comercio tiene establecimientos de distinta provincia
                     sum_bi+=cmrc_bi;
                   
                     sum_percepcion+=cmrc_percepcion;
                     sum_debito+=cmrc_debito;
                     sum_saldo+=cmrc_saldo;

                     if (i==0)  // ya imprimio` todos los establecimientos del comercio, por lo que debe imprimir el total
                        {
                         perseus_resul+="\n\t<tr>  <td class='t11' height='25px'></td> <td class='t11'></td>  "+  //imprimo 'totales' o no.
                           "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td class='t11'> </td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_debito)+"</td> "+
                           "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f", sum_saldo)+"</td> <td class='t11'></td></tr>\n";

                         i=-1;
                         sum_bi=0f;
                 
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;
                        }
                    }
                
                }
            perseus_resul+="\n\t</table>";
            
            
            
            if (user_level.equals("1") || user_level.equals("2") )
                    {
                     if(rows<1 )
                                {
									perseus_resul="";
                                    if(!periodo.equals(htm.getPeriodo_nostatic()))
                                            resul="<table >"+
														"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
														"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
														"\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
														"</tr>"+
														"</table> \n"+
														"<br><br>"
                                                        + ""
                                                        + "<br><br><br><br>No se encontro actividad para el periodo. <br>"+
                                                                "Carga el nuevo periodo con los establecimientos del periodo anterior? <br><br><br>"
                                                                + "<form name='masive' action='/ameca/liquidaciones'> \n "
                                                                + "<input type='hidden' name='operacion' value='ver_mb'> "
                                                                + "<input type='hidden' name='do_masive' value='1'> \n "
                                                                + "<input type='hidden' name='periodo' value='"+periodo+"'> \n "
                                                                + "<table><tr><td>Porcentaje a aplicar: <input type='text' name='base_imponible' value='+0' size='4'></td> <td width='35px'></td> "
                                                                + "<td><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.masive.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"
                                                                + "</tr></table>\n"
                                                                + "</form>";
                                    
                                    else
                                            resul=  "<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                                                        "<input type='hidden' name='operacion' value='ver_mb'>"+
                                                        "<table >"+
                                                        "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                                                        "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
                                                        "\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
                                                        "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
                                                        "<td width='40px'> </td>"
                                                    + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"+
                                                        "</tr>"+
                                                        "</table> \n"+
                                                        "</form> \n"+
                                                        "<br>"
                                                    + ""
                                                    + "<br><br><br><br>No se encontro actividad para el periodo. <br>";
                                        
                                    resul+="<br><br><br><br>";
                                    
                                }

                    else 
                              {
                                  if(zona.equals(""))
                                      perseus_resul+="<br><br><br><br>Actualizacion de todos los establecimientos. Ingrese porcentajes.<br>\n"+
                                              "<form action='/ameca/liquidaciones' name='porcentajes'> \n "
                                              + "<input type='hidden' name='operacion' value='ver_mb'>  \n"
                                              + "<input type='hidden' name='do_masive' value='2'> "
                                              + "<input type='hidden' name='periodo' value='"+periodo+"'>"+
                                              "<table>\n<tr><td>Base imponible: <input type='text' name='base_imponible' value='+0' size='4'></td>\n"+
                                              "<td>Percepciones: <input type='text' name='percepcion_iibb' value='+0' size='4'></td> <td> &nbsp;&nbsp;&nbsp; <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.porcentajes.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> \n"+
                                              "</tr>\n</table>\n"+
                                              "</form>";
                                  perseus_resul+="<br><br><br> registros: "+ rows;
                              }

                    }
            else
                { 
                    if(rows<1)
                               {
								 resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
											"<input type='hidden' name='operacion' value='ver_mb'>"+
											"<table>"+
											"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
											"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
											"\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
											"</tr>"+
											"</table> \n"+
											"</form> \n"+
											"<br><br>"+
											""+
											"<br><br><br><br>No se encontro actividad para el periodo. <br>";
								perseus_resul="";
								}
                    resul+="\n<br><br><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mb','MyWindow','width=600,height=300'); return false;\">"
                    + "<table><tr><td> PWD </td> <td> <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</td>"
                    + "</tr></table>"
                    + "</a><br><br> <br><br><br>";
                }
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
                resul= ex.getMessage();
                }
            }
            
            
            
            
		if (zona.equals("") && periodo.equals(htm.getPeriodo_nostatic())) 
				{ 	HTML.perseus_iibb=perseus_resul+"<br><br><br> version perseus";
					HTML.perseus_bool_iibb=true;
				}

		
				
        return resul+perseus_resul;  
     }


    
    // update masivo de los establecimientos en liquidacion iva.
    // Esta fumcion está repetida, es la misma para iibb e iva, ya que actualiza todos los establecimientos activos de EstablecimientoLiquiMes.
    // Invocar con parametro "V" o "B" segun sea iibb o iva y "0" en parametros que no cambian o no corresponden (eg desde iibb "0" en compra_iva). Cambiar el nombre de la funcion! (updateMasivo)
    // Los parametros llegan con formato: +10 o -5 que representa el porcentaje a adhisionar o sustraer (validar esto en javascript).
    private String LiquiMesIVA_update(String periodo, String base_imponible, String percepcion_iva, String compra_iva) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        boolean query_ok=false;
        String res="Update EstablecimientosLiquiMes SET ";

        if (!base_imponible.equals("+0"))
            {
             res+="base_imponible = base_imponible*(1"+base_imponible+"/100) ";
             query_ok=true;
            }

        if (!percepcion_iva.equals("+0"))
            {
             if (query_ok)
                res+=", percepcion_iva = percepcion_iva*(1"+percepcion_iva+"/100) ";
            else
                res+=" percepcion_iva = percepcion_iva*(1"+percepcion_iva+"/100) ";
             query_ok=true;
            }

        if (!compra_iva.equals("+0"))
            {
             if (query_ok)
                 res+=", compra_iva = compra_iva*(1"+compra_iva+"/100) ";
             else
                 res+=" compra_iva = compra_iva*(1"+compra_iva+"/100) ";
             query_ok=true;
            }

        if (query_ok)
            res+=" WHERE periodo ='"+periodo+"'";
        else
            return "<br>no se ingresaron porcentajes validos<br>";
        try 
            {
            con=CX.getCx_pool();
            HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;

            pst = con.prepareStatement(res);  
 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);
            
            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
            pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                    + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                    +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                    + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            }
        catch (SQLException ex) {
             res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


    // update masivo de los establecimientos en liquidacion iibb.
    // Los parametros llegan con formato: +10 o -5 que representa el porcentaje a adhisionar o sustraer (validar esto en javascript).
    private String LiquiMesIIBB_update(String periodo, String base_imponible, String percepcion_iibb) 
	{
		HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        boolean query_ok=false;
        String res="Update EstablecimientosLiquiMes SET ";
        if (!base_imponible.equals("+0"))
            {
             res+="base_imponible = base_imponible*(1"+base_imponible+"/100) ";
             query_ok=true;
            }

        if (!percepcion_iibb.equals("+0"))
            {
             if (query_ok)
                res+=", percepcion_iibb = percepcion_iibb*(1"+percepcion_iibb+"/100) ";
             else
                res+=" percepcion_iibb = percepcion_iibb*(1"+percepcion_iibb+"/100) ";
             query_ok=true;
            }

        if (query_ok)
            res+=" WHERE periodo ='"+periodo+"'";
        else
            return "<br>no se ingresaron porcentajes validos<br>";
        try 
            {
            con=CX.getCx_pool();

            pst = con.prepareStatement(res);  
 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);

            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
           pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            }
        catch (SQLException ex) {
               res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


    
     // insert masivo IVA de los establecimientos en EstablecimientoLiquiMes para nuevo periodo sin actividades todavia.
//    private String LiquiMes_masive (String periodo, String base_imponible, String percepcion_iva, String percepcion_iibb, String compra_iva) 
    private String LiquiMes_masive (String periodo, String porcentaje) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        String res="INSERT INTO EstablecimientosLiquiMes (id_establecimiento, base_imponible, compra_iva, percepcion_iva, percepcion_iibb, "+
                                                         "periodo, alicuota_iibb, alicuota_iva,  "+
                                                         "alicuota_pago_facil, reporte_ameca_comision, activo_iva_periodo, activo_iibb_periodo) "+
                    "SELECT em.id_establecimiento, base_imponible*(1"+porcentaje+"/100), compra_iva*(1"+porcentaje+"/100), percepcion_iva*(1"+porcentaje+"/100), percepcion_iibb*(1"+porcentaje+"/100), '" +
                          periodo+"', em.alicuota_iibb, em.alicuota_iva, " +
                         "em.alicuota_pago_facil, em.reporte_ameca_comision, em.activo_iva_periodo, em.activo_iibb_periodo " +
                    "FROM  EstablecimientosLiquiMes em, Establecimientos e " +
                    "WHERE periodo ='"+htm.getPeriodo_nostatic()+"' and e.id_establecimiento=em.id_establecimiento and (em.activo_iibb_periodo or em.activo_iva_periodo) ";
try 
            {
			HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;
            con=CX.getCx_pool();

            pst = con.prepareStatement(res); 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);

            HTML.setIgnited_parametros(false);
            HTML.cargaParametros();
            HTML.setIgnited_periodo(false);
            htm.Carga_periodo();

            
            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
            pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                    + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                    +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                    + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            
            
            
            // los incrementos porcentuales van en update principal y por ultimo otro update que recalcule los saldos.
            // en saldo_iva, en vez del +1, se deja el 1 solo xq se recibe +10 o -20: TRUNCATE(base_imponible*(1+"+base_imponible+"/100)*alicuota_iva/100-percepcion_iva*(1+"+percepcion_iva+"/100)-compra_iva*(1+"+compra_iva+"/100)*alicuota_iva/100,2) as 'saldo2'
            }
        catch (SQLException ex) {
               res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


       public static boolean isNumeric(String strNum) {
           if (strNum == null) {
               return false;
           }
           try {
               int i = Integer.parseInt(strNum);
           } catch (NumberFormatException nfe) {
               return false;
           }
           return true;
       }
    
}package ameca; 


/*
 *
 * @author manu
 */

//import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//import manu.utils.*;

public class Liquidaciones extends HttpServlet        
   {  
    
    HTML htm=new HTML();
    public String user_level="0";

    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException

   {  //	htmls.logger.fine("homeOsoc. Carga servlet\n--");
   
       

	HttpSession session = request.getSession(false)!= null ? request.getSession(false): request.getSession();
	user_level  = session.getAttribute("user_level") != null ?  (String)session.getAttribute("user_level") : "0" ;
	String user_name  = session.getAttribute("user_name") != null ?  (String)session.getAttribute("user_name") : "" ;
            

       
    if (!HTML.getIgnited_zonas())
        HTML.Carga_zonas();
    if (!HTML.getIgnited_condiciones_iva())
         HTML.Carga_condiciones_iva();
    if (!HTML.getIgnited_condiciones_iibb())
         HTML.Carga_condiciones_iibb();
//    if (!HTML.getIgnited_periodo())
//         HTML.Carga_periodo();
    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    
  String base_imponible, compra_iva, percepcion_iva, percepcion_iibb;
    
    
    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String do_masive  = request.getParameter ("do_masive") != null ?  request.getParameter ("do_masive") : "0" ; // 1 para insert masivo de establecimientos en un periodo que estaba vacio
                                                                                                                 // 2 para update masivo del periodo actual
    String id_comercio  = request.getParameter ("id_comercio") != null ?  request.getParameter ("id_comercio") : "0" ;
    String nro_cuit  = request.getParameter ("nro_cuit") != null ?  request.getParameter ("nro_cuit") : "0" ;
    String id_establecimiento  = request.getParameter ("id_establecimiento") != null ?  request.getParameter ("id_establecimiento") : "0" ;
    String direccion_establecimiento  = request.getParameter ("direccion_establecimiento") != null ?  request.getParameter ("direccion_establecimiento") : "" ;
    String id_zona  = request.getParameter ("id_zona") != null ?  request.getParameter ("id_zona") : "" ;
    if (id_zona.equals("1"))
        id_zona="";



String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : htm.getPeriodo_nostatic() ;
 //   if (!StringUtils.isStrictlyNumeric(periodo))
       if (!isNumeric(periodo))
           periodo=htm.getPeriodo_nostatic();
String periodo2  = request.getParameter ("periodo2") != null ?  request.getParameter ("periodo2") : "" ;
    if (!isNumeric(periodo2))
        periodo2="";



    base_imponible  = request.getParameter ("base_imponible") != null ?  request.getParameter ("base_imponible") : "0" ;
    //alicuota_iva  = request.getParameter ("alicuota_iva") != null ?  request.getParameter ("alicuota_iva") : "21" ;
    //alicuota_iibb  = request.getParameter ("alicuota_iibb") != null ?  request.getParameter ("alicuota_iibb") : "3" ;
    compra_iva  = request.getParameter ("compra_iva") != null ?  request.getParameter ("compra_iva") : "0" ;
    percepcion_iva  = request.getParameter ("percepcion_iva") != null ?  request.getParameter ("percepcion_iva") : "0" ;
    percepcion_iibb  = request.getParameter ("percepcion_iibb") != null ?  request.getParameter ("percepcion_iibb") : "0" ;

	if (user_level.equals("0"))
	{
     out.println("<!DOCTYPE html><html><head><title>Ameca - Password</title>\n </head> \n <body>  \n\n <br><br> \n");
	 out.println("<script>"
		+   "function go() { window.document.location.replace(\"/ameca/inicio\"); \n ;} \n "
		+   "document.body.style.background='brown'; \n window.setTimeout(go, 10); \n"  // acá habría que poner cartel de logueo
		+ "</script><br>Ingrese al sistema!<br>"
		+ "</body></html>");
	}
	else
	{
    switch (operacion) {
           case "ver_e":
//ver establecimiento, es una sola ficha o agregar de todos los periodos (lqui historica).

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("\n<br><h1>Saldos DDJJ</h1><br><br>" +
                       "\nCUIT: " + nro_cuit + "&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; Direcci&oacute;n: " + direccion_establecimiento +
                       "<br><br><table>"
                       + "<tr><td colspan='3' align='center'>Periodo desde / hasta:</td>"
                       + "<tr><td colspan='3' height='2px'></td>"
                       + "<tr><td><img src='/ameca/imgs/back_go.png'></td>"
                       + "<td><form action='/ameca/liquidaciones' name='f_periodo'>"
                       + "<input type='text' name='periodo' value='" + periodo + "' size='7'><input type='text' name='periodo2' value='" + periodo2 + "' size='7'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_e'>\n" +
                       "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>\n" +
                       "<input type='hidden' name='nro_cuit' value='" + nro_cuit + "'>\n" +
                       "<input type='hidden' name='id_establecimiento' value='" + id_establecimiento + "'>\n" +
                       "<input type='hidden' name='direccion_establecimiento' value='" + direccion_establecimiento + "'>\n" +
                       "</form></td>"
                       + "<td><img src='/ameca/imgs/next_go.png'></td></tr>"
                       + "<tr><td colspan='3' align='center' valign='top'><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.f_periodo.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table>");

               out.println("<br><br>\n" + this.LiquiEstablecimientoTable(id_establecimiento, periodo, periodo2));

               out.println("<br><br><br><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'><img src='/ameca/imgs/back.png'></a> <br><br><br><br>");

               out.println(HTML.getTail());

               break;
           case "ver_c":   //ver las DDJJs de un comercio dada su id_comercio
               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("\n<h2>Saldos DDJJ Comercio: " + nro_cuit + "</h2><br>" +
                       "\n<form action='/ameca/liquidaciones' name='f_periodo'>" +
                       "<table>"
                       + "<tr><td><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"
                       + "<td><input type='text' name='periodo' value='" + periodo + "' size='7'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_c'>\n" +
                       "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>\n" +
                       "<input type='hidden' name='nro_cuit' value'" + nro_cuit + "'>" +
                       "<input type='hidden' name='id_establecimiento' value='" + id_establecimiento + "'>\n" +
                       "</form></td>"
                       + "<td><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>"
                       + "</table>");
               out.println(this.LiquiComercioTable(id_comercio, periodo));
//        out.println("<br><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_pre(periodo)+"'>Mes Anterior</a>"
               //              + " <br><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_prox(periodo)+"'>Mes Siguiente</a><br><br>\n"+this.LiquiComercioTable(id_comercio, periodo));

               out.println("<br><br><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'><img src='/ameca/imgs/back.png'></a> <br>");

               out.println("\n\n" + HTML.getTail());

               break;
           case "ver_m":  //ver liquidaciones masiva total (iva e iibb)

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("<br><h2>Liquidaci&oacute;n Mensual General (IVA e IIBB): " + htm.getPeriodo_prn(periodo) + "</h2><br>" +
                       "\n<form action='/ameca/liquidaciones'>" +
                       "Periodo: <input type='number' name='periodo' value='" + periodo + "' min='201900' max='204311'>\n\t" +
                       "<input type='hidden' name='operacion' value='ver_m'>\n" +
                       "<input type='submit'></form>");

               out.println("<br><br>\n" + this.LiquiMesTable(periodo));
               out.println("\n\n<br><br><a href='/ameca/inicio'>Inicio</a><br>\n\n");

               out.println(HTML.getTail());

               break;
           case "ver_mv":   //ver liquidaciones masiva iva

               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
//        out.println("\n<br><h2>Liquidaci&oacute;n I.V.A. Mensual: </h2><br>");
               out.println("<br><br>\n"
                       + "<table cellspacing='0'>"
                       + "<tr><td height='11px'> </td></tr>\n"
                       + "<tr><td style='width:5px; height:64px'></td><td><img src='/ameca/imgs/liqui64.svg' style='width:64px;height:64px;'></td><td width='15px'></td> "
                       + "<td style='width:800px; font-family:Arial; font-size:40px; font-weight: bold;'>Liquidaci&oacute;n I.V.A. Mensual</td> <td width='277px'> </td> </tr>"
                       + "<tr><td height='55px'> </td></tr>\n"
                       + ""
                       + "\n\n "
                       + "<tr><td colspan='5'> ");


// aca estaba  la tabla form de prox periodo y generador de excels

               out.println("</td></tr></table>\n\n ");


//        if( (!base_imponible.equals("0") && !base_imponible.equals("+0")) || (!percepcion_iva.equals("0") && !percepcion_iva.equals("+0")) || (!compra_iva.equals("0") && !compra_iva.equals("+0")) )
//            out.println("<br><br>registros modificados: \n"+this.LiquiMesIVA_update(periodo, base_imponible, percepcion_iva, compra_iva));

//        if(do_masive.equals("1"))
//            out.println("<br><br>registros agregados: \n"+this.LiquiMes_masive(periodo, base_imponible, percepcion_iva, percepcion_iibb, compra_iva));
               switch (do_masive) {
                   case "2":
                       out.println("<br><br>registros modificados: \n" + this.LiquiMesIVA_update(periodo, base_imponible, percepcion_iva, compra_iva));
                       break;
                   case "1":
                       out.println("<br><br>registros agregados: \n" + this.LiquiMes_masive(periodo, base_imponible));  // bi es porcentaje gral
                       HTML.setIgnited_periodo(Boolean.FALSE);
                       out.println(":" + htm.getPeriodo_nostatic());
                       break;
               }

               out.println("<br>\n" + this.LiquiMesIVATable(periodo, id_zona));

               out.println("\n\n<br><br><a href='/ameca/inicio'>Inicio</a><br>");
               out.println(HTML.getTail());

               break;
           case "ver_mb":   //liquidaciones masiva iibb
               out.println(HTML.getHead("liquidaciones", htm.getPeriodo_nostatic()));
               out.println("<br><br>\n"
                       + "<table cellspacing='0'>"
                       + "<tr><td height='11px'> </td></tr>\n"
                       + "<tr><td style='width:5px; height:64px'></td><td><img src='/ameca/imgs/liqui64.svg' style='width:64px;height:64px;'></td><td width='15px'></td> "
                       + "<td style='width:800px; font-family:Arial; font-size:40px; font-weight: bold;'>Liquidaci&oacute;n I.I.B.B. Mensual</td> <td width='277px'> </td> </tr>"
                       + "<tr><td height='55px'> </td></tr>\n");
               out.println("\n\n " +
                       "<tr><td colspan='5'> " +
                       "</td></tr></table>\n\n");


//        if( (!base_imponible.equals("0") && !base_imponible.equals("+0")) || (!percepcion_iibb.equals("0") && !percepcion_iibb.equals("+0")))
               switch (do_masive) {
					case "2":
                       out.println("<br><br>registros modificados: \n" + this.LiquiMesIIBB_update(periodo, base_imponible, percepcion_iibb));
                       break;
					case "1":
                       out.println("<br><br>registros agregados: \n" + this.LiquiMes_masive(periodo, base_imponible));  // bi tiene el valor del porcentaje para todos los campos
                       break;
					}
               out.println("<br><br>\n" + this.LiquiMesIIBBTable(periodo, id_zona));

               out.println("\n<br><br><a href='/ameca/inicio'>Inicio</a>\n\n");

               out.println(HTML.getTail());

               break;
       }
       }  // fin del if que pregunta por user_level (ejecuta el bloque de arriba se es 1 o 2)    
   
 }



    
    
    
   
// Recibe  el id_establecimiento y devuelve una tabla de los saldos de sus establecimientos 

    public static String LiquiEstablecimientoTable(String id_establecimiento) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total, percepcion_iva, 
                percepcion_iibb, saldo_ddjj_iva, saldo_ddjj_iibb, alicuota_iva, alicuota_iibb;
	String resul;
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT FORMAT (base_imponible, 2, 'de_DE'), " +
                                                "@debitoF_iva := (base_imponible*alicuota_iva/100), " +   //debito fiscal IVA
                                                "@debitoF_iibb := (base_imponible*alicuota_iibb/100), " +  // 3.  debito fiscal IIBB
                                                "FORMAT (base_imponible+@debitoF_iva, 2, 'de_DE'), " + //venta total
                                                "FORMAT (compra_iva, 2, 'de_DE'), " +
                                                "@iva_credito := (compra_iva*alicuota_iva/100), " +
                                                "FORMAT (compra_iva+@iva_credito, 2, 'de_DE'), " + // 7.  compra total
                                                "percepcion_iva, percepcion_iibb, " +
                                                "FORMAT (@debitoF_iva-@iva_credito-percepcion_iva, 2, 'de_DE'), " +  // 10. SALDO IVA
                                                "FORMAT (@debitoF_iibb-percepcion_iibb, 2, 'de_DE'), "+   // SALDO IIBB
                                                "alicuota_iva, alicuota_iibb, "+ 
                                                "FORMAT (@debitoF_iva, 2, 'de_DE'), "+     // 14.  debito_iva
                                                "FORMAT (@debitoF_iibb, 2, 'de_DE'), "+ 
                                                "FORMAT (@iva_credito, 2, 'de_DE') "+    // 16.  credito_iva
                                       " FROM EstablecimientosLiquiMes " +
                                       " WHERE EstablecimientosLiquiMes.id_establecimiento ="+id_establecimiento);
            
            resul="<table class='bicolor'>\n\t<tr>\n\t\t<th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO D.D.J.J. IVA</th> \n"+
                    "<th>Alic. IIBB</th> <th>Percepcion IIBB</th> <th>Debito IIBB</th> <th>SALDO D.D.J.J. IIBB</th> \n</tr>\n\n";

            rs = pst.executeQuery();
            while (rs.next())
                {
                 base_imponible=rs.getString(1);
                 alicuota_iva=rs.getString(12);
                 alicuota_iibb=rs.getString(13);
                 debito_iva=rs.getString(14);
                 debito_iibb=rs.getString(15);
                 venta_total=rs.getString(4);
                 compra_iva=rs.getString(5);
                 credito_iva=rs.getString(16);
                 compra_total=rs.getString(7);
                 percepcion_iva=rs.getString(8);
                 percepcion_iibb=rs.getString(9);
                 saldo_ddjj_iva=rs.getString(10);
                 saldo_ddjj_iibb=rs.getString(11);

                resul+="\n\t<tr> <td>"+base_imponible+"</td> <td>"+alicuota_iva+"</td> <td>"+debito_iva+"</td> <td>"+venta_total+"</td> <td>"+compra_iva+"</td> "+
                    "<td>"+credito_iva+"</td> <td>"+compra_total+"</td> <td>"+percepcion_iva+"</td> <td>"+saldo_ddjj_iva+"</td> " +
                    "<td>"+alicuota_iibb+"</td> <td>"+percepcion_iibb+"</td> <td>"+debito_iibb+"</td> <td>"+saldo_ddjj_iibb+"</td>\n\t</tr>";
                }
            resul+="\n\t</table>\n";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }



// Recibe  el id_establecimiento + periodo y devuelve una tabla de sus saldos DDJJ (IVA e IIBB) para el periodo 

    private String LiquiEstablecimientoTable(String id_establecimiento, String periodo, String periodo2) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        
	String resul, base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total,
                         percepcion_iva, percepcion_iibb, saldo_ddjj_iva, saldo_ddjj_iibb, alicuota_iva, alicuota_iibb;
        String query="SELECT FORMAT (base_imponible, 2, 'de_DE'), " +
                                                "@debitoF_iva := (base_imponible*alicuota_iva/100), " +   // 2. debito fiscal IVA
                                                "@debitoF_iibb := (base_imponible*alicuota_iibb/100), " +  // debito fiscal IIBB
                                                "FORMAT (base_imponible+@debitoF_iva, 2, 'de_DE'), " + //venta total
                                                "FORMAT (compra_iva, 2, 'de_DE'), " +      // 5. compra iva
                                                "@iva_credito := (compra_iva*alicuota_iva/100), " +
                                                "FORMAT (compra_iva+@iva_credito, 2, 'de_DE'), " + //compra total
                                                "percepcion_iva, percepcion_iibb, " +
                                                "FORMAT (@debitoF_iva-@iva_credito-percepcion_iva, 2, 'de_DE'), " +  // 10. SALDO DDJJ IVA
                                                "FORMAT (@debitoF_iibb-percepcion_iibb, 2, 'de_DE'), "+   // SALDO DDJJ IIBB
                                                "alicuota_iva, alicuota_iibb, "+ 
                                                "FORMAT (@debitoF_iva, 2, 'de_DE'), "+  // 14. debito iva formateada
                                                "FORMAT (@debitoF_iibb, 2, 'de_DE'), "+ 
                                                "FORMAT (@iva_credito, 2, 'de_DE'), periodo "+    // 16.  credito_iva    17. periodo
                                       " FROM EstablecimientosLiquiMes "+
                                       " WHERE  id_establecimiento ="+id_establecimiento ;
         if (periodo2.length()>4)
             query +=" AND periodo >='"+periodo+"' AND periodo <='" + periodo2 + "'   ORDER BY periodo DESC " ;
         else
             query +=" ORDER BY periodo DESC  LIMIT 10" ;
             
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            

            resul="<table class='bicolor'>\n\t<tr>\n\t\t<th>Periodo</th> <th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO IVA</th> "+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> </tr>";

            rs = pst.executeQuery();
            while (rs.next())
                {
                 base_imponible=rs.getString(1);
                 alicuota_iva=rs.getString(12);
                 alicuota_iibb=rs.getString(13);
                 debito_iva=rs.getString(14);
                 debito_iibb=rs.getString(15);
                 venta_total=rs.getString(4);
                 compra_iva=rs.getString(5);
                 credito_iva=rs.getString(16);
                 compra_total=rs.getString(7);
                 percepcion_iva=rs.getString(8);
                 percepcion_iibb=rs.getString(9);
                 saldo_ddjj_iva=rs.getString(10);
                 saldo_ddjj_iibb=rs.getString(11);
                 periodo=rs.getString(17);

                resul+="\n\t<tr><td>" + periodo + "</td><td>"+base_imponible+"</td> <td>"+alicuota_iva+"</td> <td>"+debito_iva+"</td> <td>"+venta_total+"</td> <td>"+compra_iva+"</td> "+
                    "<td>"+credito_iva+"</td> <td>"+compra_total+"</td> <td>"+percepcion_iva+"</td> <td><b>"+saldo_ddjj_iva+"</b></td> " +
                    "<td>"+alicuota_iibb+"</td> <td>"+debito_iibb+"</td> <td>"+percepcion_iibb+"</td> <td><b>"+saldo_ddjj_iibb+"</b></td>\n\t</tr>";
                }
            resul+="\n\t</table>\n";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
    

    
    
    
    
// Recibe  el id_comercio + periodo y devuelve una tabla de sus saldos DDJJ (IVA e IIBB) para el periodo 

    private String LiquiComercioTable(String id_comercio, String periodo) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int i=0;
        String resul, nombre_establecimiento, query;
        float base_imponible, debito_iva, debito_iibb, venta_total, compra_iva, credito_iva, compra_total,
                percepcion_iva, percepcion_iibb, saldo_iva, saldo_iibb, alicuota_iva, alicuota_iibb,
                sum_bi=0f, sum_debito_iva=0f, sum_debito_iibb=0f, sum_venta_total=0f, sum_compra_iva=0f, sum_credito_iva=0f, sum_compra_total=0f,
                sum_percepcion_iva=0f, sum_percepcion_iibb=0f, sum_saldo_iva=0f, sum_saldo_iibb=0f;
        query="SELECT  elm.base_imponible, elm.compra_iva, elm.percepcion_iva, elm.percepcion_iibb, "+
                      "e.nombre_establecimiento, e.direccion_establecimiento, e.id_zona, e.id_localidad, " +   
                      "elm.alicuota_iva, elm.alicuota_iibb, elm.saldo_iva, elm.saldo_iibb " +   
             " FROM EstablecimientosLiquiMes elm, Establecimientos e " +
             " WHERE elm.periodo = '"+periodo+"' AND elm.id_establecimiento = e.id_establecimiento AND e.id_comercio="+id_comercio;
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            

            resul="<table class='grupos'>\n\t<tr>\n\t\t <th>Nombre Establecimiento</th> <th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> "+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO IVA</th> <th style=\"background-color: #000000; padding: 1px;\"> </th>"+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> </tr>";

            rs = pst.executeQuery();
            
            while (rs.next())
                {
                 i++;

                 base_imponible=rs.getFloat(1);
                 alicuota_iva=rs.getFloat(9);
                 alicuota_iibb=rs.getFloat(10);
                 debito_iva=base_imponible*alicuota_iva/100;
                 debito_iibb=base_imponible*alicuota_iibb/100;
                 venta_total=base_imponible+debito_iva;
                 compra_iva=rs.getFloat(2);
                 credito_iva=compra_iva*alicuota_iva/100;
                 compra_total=compra_iva+credito_iva;
                 percepcion_iva=rs.getFloat(3);
                 percepcion_iibb=rs.getFloat(4);
                 saldo_iva=rs.getFloat(11);
                 saldo_iibb=rs.getFloat(12);
                 nombre_establecimiento=rs.getString(5);
                 //direccion_establecimiento=rs.getString(5);

                 sum_bi+=base_imponible;
                 sum_debito_iva+=debito_iva;
                 sum_debito_iibb+=debito_iibb;
                 sum_venta_total+=venta_total;
                 sum_compra_iva+=compra_iva;
                 sum_credito_iva+=credito_iva;
                 sum_compra_total+=compra_total;
                 sum_percepcion_iva+=percepcion_iva;
                 sum_percepcion_iibb+=percepcion_iibb;
                 sum_saldo_iva+=saldo_iva;
                 sum_saldo_iibb+=saldo_iibb;
                 

                resul+="\n\t<tr><td>"+nombre_establecimiento+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",base_imponible)+"</td> <td>"+alicuota_iva+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",venta_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",compra_iva)+"</td> "+
                    "<td>"+String.format(Locale.GERMAN, "%,.2f",credito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",compra_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iva)+"</td> <td style=\"background-color: #000000; padding: 1px;\"></td>" +
                    "<td>"+alicuota_iibb+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iibb)+"</td>\n\t</tr>";
                }
            
            if (i>1)
             resul+="\n\t<tr><td colspan='15' height='1px'  style=\"background-color: #000000; padding: 0px;\"></td></tr>"+
                      "<tr><td colspan='10' height='1px'></td> <td style=\"background-color: #000000; padding: 1px;\"></td> <td colspan='4' height='2px'></td>  </tr>  <tr><td>TOTAL</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_debito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_venta_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_iva)+"</td> "+
                     "<td>"+String.format(Locale.GERMAN, "%,.2f",sum_credito_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_total)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo_iva)+"</td> <td style=\"background-color: #000000; padding: 1px;\"></td>" +
                     "<td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo_iibb)+"</td>\n\t</tr>";            

            resul+="\n\t</table>";


            
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
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
    
    
    
    


// Recibe  el periodo y devuelve una tabla con los saldos DDJJ (IVA e IIBB) de los comercios para el periodo 

    private String LiquiMesTable(String periodo) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int z, i=0, k=0;
        String resul;
        Float cmrc_bi, cmrc_compra=0f, cmrc_percepcion_iva=0f, cmrc_percepcion_iibb=0f, debito_iibb=0f,  saldo_iibb, saldo_iva;
        Float[][] flotantes=new Float[6][10];  // base imponible, alic_iva, alic_iibb, compra_iva, percepcion_iva, percepcion_iibbi
        int[][] enteros=new int[6][6];  // id_comercio, id_zona, id_condicion_iva (siempre resp. insc=2), id_condicion_iibb, id_establecimiento, id_establecimiento_mes
        String [][] cadenas = new String [6][3];  // cuit, nombre, direccion
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT c.nro_cuit, c.nombre_responsable, e.direccion_establecimiento, " +
                                                                    "em.base_imponible, " + //4  base imponible
                                                                    "em.compra_iva, " +  // 5. comopra
                                                                    "em.percepcion_iva, " +    // 6. percepcion IVA
                                                                    "em.percepcion_iibb, " +   //  7.  percepcion IIBB
                                                                    "em.alicuota_iibb, " +  //   8. alicuota IIBB
                                                                    "c.id_comercio, " + //  9.  id_comercio
                                                                    "e.id_establecimiento, " + //  10
                                                                    "em.id_establecimiento_mes, " + //  11   id_establecimiento_mes     para observaciones y exclusiones
                                                                    "c.id_condicion_iva, " + //  12   id_condicion_iva
                                                                    "c.id_condicion_iibb, " + //  13   id_condicion_iibb
                                                                    "e.id_zona, em.saldo_iva, em.saldo_iibb, "  + // 16
                                                                    "em.compra_iva*em.alicuota_iva/100 as 'credito iva',  " + //  17    credito iva
                                                                    "em.base_imponible*em.alicuota_iva/100 as 'd_iva',  " + //  18   debito iva
                                                                    "em.base_imponible*em.alicuota_iibb/100 as 'd_iibb'  " + //  19  debito iibb
                                                           " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                                           " WHERE c.id_comercio = e.id_comercio " + 
                                                               " AND e.id_establecimiento=em.id_establecimiento " +
                                                               " AND em.periodo ='"+periodo+"' AND (activo_iva_periodo OR activo_iibb_periodo) "+ 
                                                           " ORDER BY c.nro_cuit");  
            

            resul="\n\n<table class='grupos' width='1800px'>\n\t<tr>\n\t\t"+
                    "<th>CUIT</th> <th>Nombre</th> <th>Direccion Establecimiento</th> \n"+
                    "<th>Base Imponible</th> <th>Alic. IVA</th> <th>Debito IVA</th> <th>Venta Total</th> <th>Compra IVA</th> \n"+
                    "<th>Credito IVA</th> <th>Compra Total</th> <th>Percepcion IVA</th> <th>SALDO  IVA</th> \n"+
                    "<th>Alic. IIBB</th> <th>Debito IIBB</th> <th>Percepcion IIBB</th> <th>SALDO IIBB</th> <th>Observaciones</th> \n</tr>\n\n";

            rs = pst.executeQuery();

            while (rs.next())
                {
                k++;
                enteros[i][0]=rs.getInt(9);    // id_comercio
                
                if (enteros[i][0]!=enteros[0][0])   // otro establecimiento dle mismo comercio 
                     {
                        if (i==1)   // unico establecimiento  en [0], en [1] tengo otro establecimiento que no se si es unico o no
                           {
                            resul+="\n\t<tr> <td>"+cadenas[0][0]+"</td> <td>"+cadenas[0][1]+"</td> <td>"+cadenas[0][2]+", "+htm.getZona(enteros[0][5])+"</td> "+
                                   "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td>"+htm.getIVA_prn()+"</td> "+
                                   "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*htm.getIVA()+flotantes[0][0])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1])+"</td> "+
                                   "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1]*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1]*htm.getIVA()+flotantes[0][1])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][2])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5])+"</td> " +
                                   "<td>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0]*flotantes[0][4]/100)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][3])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][6])+"</td> " +
                                   " \n\t <td></td>  </tr> \n ";
                          }
                        else // mas de un establecimiento del mismo comercio, ie, con mismo id_comercio
                           { 
                            z=0;
                            resul+="\n\t<tr> <td rowspan='"+Integer.toString(i)+"'>"+cadenas[0][0]+"</td> <td rowspan='"+Integer.toString(i)+"'>"+cadenas[z][1]+"</td> <td class='t2'>"+cadenas[z][2]+", "+htm.getZona(enteros[z][5])+"</td> "+
                                 "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td class='t2'>"+htm.getIVA_prn()+"</td> "+
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][8])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][8]+flotantes[z][0])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][1])+"</td> "+
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][7]+flotantes[z][1])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][2])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][5])+"</td> " +
                                 "<td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][9])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][3])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][6])+"</td> " +
                                 " \n\t <td class='t2'></td></tr>";
                            cmrc_bi=flotantes[0][0];
                            saldo_iva=flotantes[0][5];
                            saldo_iibb=flotantes[0][6];
                            debito_iibb+=flotantes[0][9];

                            for (z=1;z<i;z++)
                                    {resul+="\n\t<tr> <td class='t2'>"+cadenas[z][2]+" </td> "+
                                          "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0])+"</td> <td class='t2'>"+htm.getIVA_prn()+"</td> "+
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0]*htm.getIVA())+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][0]*htm.getIVA()+flotantes[z][0])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][1])+"</td> "+
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][7])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f", flotantes[z][7]+flotantes[z][1])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][2])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][5])+"</td> " +
                                          "<td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",flotantes[z][4])+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f", debito_iibb)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][3])+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",flotantes[z][6])+"</td> " +
                                          " \n\t <td class='t2'></td></tr>";
                                      cmrc_bi+=flotantes[z][0];
                                      saldo_iva+=flotantes[z][5];
                                      saldo_iibb+=flotantes[z][6];
                                      debito_iibb+=flotantes[z][9];
                                    }

                           resul+="\n\t<tr> <td colspan='3'></td> "+  
                             "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td> </td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi*htm.getIVA())+"</td> "+
                                  "<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi+cmrc_bi*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
                                  "<td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra+cmrc_compra*htm.getIVA())+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion_iva)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iva)+"</td> " +
                                  "<td></td> <td>"+String.format(Locale.GERMAN, "%,.2f",debito_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion_iibb)+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",saldo_iibb)+"</td> " +
                                  " \n\t <td></td></tr> " ;

                     }
                   // esto lo ejecuta despues de haber impreso un establecimiento, sea sucursal o unico.
                    enteros[0][0]=enteros[i][0];  // id_establecimiento
                   i=0;
                   }   //  fin del if-else que pregunta por si el nuevo id_comercio es igual que el que esta almacenado en flotantes[0][0]
                
                    flotantes[i][0]=rs.getFloat(4);  // base imponible
                    flotantes[i][1]=rs.getFloat(5);  // compra_iva
                    flotantes[i][2]=rs.getFloat(6);  // percepcion_iva
                    flotantes[i][3]=rs.getFloat(7);  // percepcion_iibb
                    flotantes[i][4]=rs.getFloat(8);  // alicuota_iibb
                    flotantes[i][5]=rs.getFloat(15);  // saldo_iva
                    flotantes[i][6]=rs.getFloat(16);  // saldo_iibb
                    flotantes[i][7]=rs.getFloat(17);  // credito_iva x compras
                    flotantes[i][8]=rs.getFloat(18);  // debito_iva
                    flotantes[i][9]=rs.getFloat(19);  // debito_iibb

                    enteros[i][1]=rs.getInt(10);  // id_establecimiento
                    enteros[i][2]=rs.getInt(11);  // id_establecimiento_mes
                    enteros[i][3]=rs.getInt(12);  // id_condicion_iva
                    enteros[i][4]=rs.getInt(13);  // id_condicion_iibb
                    enteros[i][5]=rs.getInt(14);  // id_zona

                    cadenas[i][0]=rs.getString(1);  // nro_cuit
                    cadenas[i][1]=rs.getString(2);  // nombre_responsable
                    cadenas[i][2]=rs.getString(3);  // direccion establecimiento
                    
                    i++;

        }   // fin del while que recorre recordset

            
        // el ultimo establecimiento queda en la posicion 0
        if(k>1)
            resul+="\n\t<tr> <td>"+cadenas[0][0]+"</td> <td>"+cadenas[0][1]+"</td> <td>"+cadenas[0][2]+", "+htm.getZona(enteros[0][5])+"</td> "+
               "\n\t<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][0])+"</td> <td>"+htm.getIVA_prn()+"</td> "+
               "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][5]+flotantes[0][0])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][1])+"</td> "+
               "<td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][7]+flotantes[0][1])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][2])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][8])+"</td> " +
               "<td>"+String.format(Locale.GERMAN, "%,.1f",flotantes[0][4])+"%</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][9])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][3])+"</td> <td>"+String.format(Locale.GERMAN, "%,.2f",flotantes[0][9])+"</td> " +
               " \n\t <td></td>  </tr> \n "+
               "\n\t</table><br><br>Cantidad de Establecimientos: "+ k;
        else
            resul="<br><br>No hay actividades en el periodo.<br><br><br><br><br>";

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
                resul= ex.getMessage();
                }
            }
        
        return resul;  //tabla con datos de liquidacion del establecimiento
        }
 

    
// Recibe  el periodo y devuelve una tabla con los saldos DDJJ IVA de los comercios para el periodo 

    private String LiquiMesIVATable(String periodo, String id_zona)
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

	String resul, row_span, zona, query, nombre_responsable, nro_cuit, direccion_establecimiento, id_establecimiento, id_comercio, perseus_resul="";
        float cmrc_bi, cmrc_debito, cmrc_venta_total, cmrc_compra, cmrc_credito, cmrc_compra_total, cmrc_percepcion, cmrc_saldo, cmrc_alic,
                sum_bi=0f, sum_debito=0f, sum_venta_total=0f, sum_compra=0f, sum_percepcion=0f, sum_saldo=0f, sum_credito=0f, sum_compra_total=0f;
        int rows=0, int_zona, length_obs;
        try 
            {
            con=CX.getCx_pool();
            query="SELECT c.nro_cuit, c.nombre_responsable, CASE " +
							"    WHEN casa_matriz =1 THEN CONCAT(\"(CM) \",e.direccion_establecimiento)\n" +
							"    ELSE CONCAT(\"(suc) \",e.direccion_establecimiento)\n" +
							"END as 'direccion', " +
                            "em.base_imponible, " + //4
                            "em.compra_iva, " +  //5
                            "em.percepcion_iva, " +   // 6  percepciojn IVA
                            "e.id_zona, "+   //  7  zona
                            "(SELECT COUNT(*) " +
                            " FROM EstablecimientosLiquiMes eem, Establecimientos ee ";

            if (id_zona.equals(""))
                query+=" WHERE periodo='"+periodo+"' AND eem.id_establecimiento=ee.id_establecimiento " +
                                "   AND ee.id_comercio=e.id_comercio AND eem.activo_iva_periodo=1 AND c.id_condicion_iva!=1), " +  //  8   COUNT
                                "em.saldo_iva, em.alicuota_iva, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +  //9  saldo_iva  10  alicuota   11 id_establecimiento    12 id_comercio     13  length (observaciones)
                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iva_periodo=1 AND c.id_condicion_iva!=1 " + 
                           " AND e.id_establecimiento=em.id_establecimiento " +
                           " AND em.periodo ='"+periodo+"' " +
                       " ORDER BY c.nro_cuit, e.id_zona";
            else
                query+=" WHERE periodo='"+periodo+"'  AND ee.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+id_zona+") " +
                                "   AND eem.id_establecimiento=ee.id_establecimiento  " +
                                "   AND ee.id_comercio=e.id_comercio AND eem.activo_iva_periodo=1 AND c.id_condicion_iva!=1), " +  //  8   COUNT
                                "em.saldo_iva, " +  //9  saldo_iva
                                "em.alicuota_iva, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +  //10  alicuota    11 id_establecimiento    12  id_comercio
                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iva_periodo=1 AND c.id_condicion_iva!=1 " + 
                           " AND e.id_establecimiento=em.id_establecimiento " +
                           " AND em.periodo ='"+periodo+"'  AND e.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+id_zona+") "+
                       " ORDER BY c.nro_cuit";
                
                
                
            pst = con.prepareStatement(query);  
            rs = pst.executeQuery();
            
            resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                    "<input type='hidden' name='operacion' value='ver_mv'>"+
            "<table >"+
            "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
            "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
            "\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
            "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
            "<td width='40px'> </td>"
        + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>";

        if (id_zona.equals(""))
        {
                resul+= "<td width='500px'></td>"+
                              "\n<td><a href='/ameca/reportes?operacion=excel&ref=iva&periodo="+periodo+"'><img src='/ameca/imgs/guarda_xls.png' style='width:48px;height:48px;'></a>"+
                              "</td>";

                File tempFile = new File(HTML.folder+"/Reportes/"+htm.getPeriodo_year()+"/liqui_iva_"+periodo+".xls");

                if (tempFile.exists())
                    {
                        Random rand = new Random();
                        int n = rand.nextInt(5988);                
                        resul+="\n<td width='25px'></td><td><a href='/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iva_"+periodo+".xls?a="+n+"'><img src='/ameca/imgs/excel.png' style='width:48px;height:48px;'></a></td>";

                    }
        }
        resul+="</tr>"+
                 "</table> \n"+
                 "</form> \n"+
                 "<br><br>";

            if (HTML.perseus_bool_iva && id_zona.equals("") && periodo.equals(htm.getPeriodo_nostatic()))   //que verifique también que sea periodo actual y poner variables booleanas si está la tabla en string o no (puede que esté sólo tabla de iva o iibb)
						return resul+HTML.perseus_iva;
						
                 
        perseus_resul="<table class='grupos' width='1910px'>\n\t<tr>\n\t\t"+
								"<th width='150px'>CUIT</th> <th width='250px'>Nombre</th>  <th width='310px'>Direccion Establecimiento</th> <th width='120px'>Zona</th> \n "+
								"<th width='120px'>Base Imponible</th> <th width='70px'>Alic. IVA</th> <th width='120px'>Debito IVA</th> <th width='120px'>Venta Total</th> " +
								"<th width='120px'>Compra IVA</th>\n <th width='110px'>Credito IVA</th> <th width='120px'>Compra Total</th> "+
								"<th width='120px'>Percepcion IVA</th> <th width='140px'>SALDO IVA</th> <th width='40px'>Nota</th>\n "+
								"</tr>\n";

            int i=-1;
            while (rs.next())
                {
                int_zona= rs.getInt(7);
                zona=htm.getZona(int_zona);
                length_obs=rs.getInt(13);

                cmrc_alic=rs.getFloat(10);

                cmrc_bi=rs.getFloat(4);
                cmrc_compra=rs.getFloat(5);
                cmrc_percepcion=rs.getFloat(6);
                cmrc_debito=cmrc_bi*cmrc_alic/100;
                cmrc_venta_total=cmrc_bi+cmrc_debito;
                cmrc_credito=cmrc_compra*cmrc_alic/100;
                cmrc_compra_total=cmrc_compra+cmrc_credito;
                cmrc_saldo=rs.getFloat(9);

                id_establecimiento= rs.getString(11);
                id_comercio= rs.getString(12);
                nro_cuit=rs.getString(1);
                nombre_responsable=rs.getString(2);
                direccion_establecimiento=rs.getString(3);

                rows=rs.getRow();
                
                if (i<0)    //entra la primera vez y cuando se terminó con los establecimientos de un comercio
                    {
                     i=rs.getInt(8);
                     row_span=Integer.toString(i+1);                      
                     
                     if (i==1)  // unico establecimiento del comercio     /ameca/comercios?operacion=detalle&nro_cuit=23-24963650-9&id_comercio=326
                        {
                         perseus_resul+="\n\t<tr> <td class='t11' height='30px'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>"+nro_cuit+"</a></td> <td class='t11'>"+nombre_responsable+"</td>   <td class='t11'>"+direccion_establecimiento.substring(5)+"</td> <td class='t11'> "+zona+"</td> "+
												"\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
												"<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
												"<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                         if (length_obs>0)
                                perseus_resul+= "<td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr> \n";
                         else
                                perseus_resul+= "<td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr> \n";

                             
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;                         
                         sum_compra=0f;                         
                         sum_venta_total=0f;                         
                         sum_credito=0f;                         
                         sum_compra_total=0f;                         
                        }
                     else   // al menos dos, imprimo el primer establecimiento de la serie
                        {
                         perseus_resul+="\n\t<tr> <td rowspan='"+row_span+"' class='t11' valign='middle'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>" + nro_cuit + "</a></td> <td rowspan='"+row_span+"' class='t11' valign='middle'>"+nombre_responsable+"</td>  <td class='t2'> <b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+zona+"</b></td> "+
                                "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
                                "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
                                "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr>";
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>  \n\t</tr>";
                             
 
                         i--;
                         sum_bi+=cmrc_bi;
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;                   
                         sum_compra+=cmrc_compra;                   
                         sum_venta_total+=cmrc_venta_total;                     
                         sum_credito+=cmrc_credito;       
                         sum_compra_total+=cmrc_compra_total;                    
                        }
                    }
                else if (i>0)   // desde el segundo hasta la ultimo establecimiento
                    {
                     perseus_resul+="\n\t<tr> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+zona+"</b></td> "+
											"\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> "+
											"<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_venta_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra)+"</td> "+
											"<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_credito)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_compra_total)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> " ;
                               
                     if (length_obs>0)                     
                            perseus_resul+="<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n  \n\t</tr>";
                     else
                            perseus_resul+="<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n  \n\t</tr>";
                         

                         i--;
                         sum_bi+=cmrc_bi;
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;                   
                         sum_compra+=cmrc_compra;                   
                         sum_venta_total+=cmrc_venta_total;                     
                         sum_credito+=cmrc_credito;       
                         sum_compra_total+=cmrc_compra_total;                    
                     if (i==0)  // ya imprimio` todos los establecimientos del comercio, por lo que debe imprimir el total
                        {
    //                     resul+="\n\t<tr class='t1'> <td colspan='3'></td> "+  //imprimo 'totales' o no.
                           perseus_resul+="\n\t<tr>  <td class='t11' height='25px'></td> <td class='t11'></td>  "+  //imprimo 'totales' o no.
                                "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td class='t11'> </td> "+
                                "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_debito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_venta_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_compra)+"</td> "+
                                "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_credito)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_compra_total)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_saldo)+"</td> " +
                                " <td class='t11'></td>\n\n\t</tr>  " ;
 
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;                         
                         sum_compra=0f;                         
                         sum_venta_total=0f;                         
                         sum_credito=0f;                         
                         sum_compra_total=0f;                         
                        }

                    }
                
                }   // fin del while que recorre recordset

            perseus_resul+="\n\t</table>";

            
// cookie y llave para habilitar esta seccion (no cookie .:. llave que pide pwd con javascript prompt x ahora y despues pop up)
            // un pwd ingresa cookie por semana y otro por dia. (el pwd por dia, es ddyyyymm --> HEX)
            
            // pop de inicio para verificar el pwd que ingreso desde un prompt de javascript. 
            // Si esta ok, guarda el cookie y reinicia la ventana que lo abrio y se cierra. En caso que este ko da mensaje pwd equivocado y se cierra. 
            
            if (user_level.equals("1") || user_level.equals("2") )
                    {
                        //resul+="<br><br><br>HAY COOKIE !!!: "+htm.getPWD_day()+"<br><br><br>";
                     if(rows<1)  //no hay resultados en el periodo (pregunta cargar los del periodo anterior)
                                {
									perseus_resul="";
                                    if(!periodo.equals(htm.getPeriodo_nostatic()))
                                            resul="<table >"+
														"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
														"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
														"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
														"</tr>"+
														"</table> \n"+
														"<br><br>"
													+ ""
													+ "<br><br><br><br>No se encontro actividad para el periodo. <br>"
													+ "Carga el nuevo periodo con los establecimientos del periodo anterior? <br><br><br>"
													+ "<form name='masive' action='/ameca/liquidaciones'> \n <input type='hidden' name='operacion' value='ver_mv'> <input type='hidden' name='do_masive' value='1'> \n <input type='hidden' name='periodo' value='"+periodo+"'> \n "
													+ "<table><tr><td>Porcentaje a aplicar: <input type='text' name='base_imponible' value='+0' size='4'></td> <td width='35px'></td> <td><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.masive.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"
													+ "</tr></table>\n"
													+ "</form>\n";

                                    else
                                            resul=  "<form action='/ameca/liquidaciones' name='periodo_zona'>"+
															"<input type='hidden' name='operacion' value='ver_mv'>"+
															"<table >"+
															"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
															"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
															"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
															"<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
															"<td width='40px'> </td>"+
															"<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"+
															"</tr>"+
															"</table> \n"+
															"</form> \n"+
															"<br>"
														+ ""
														+ "<br><br><br><br>No se encontro actividad para el periodo. <br>";
                                        
                                    resul+="<br><br><br><br>";
                                }

                    else 
                              {
                                  if(id_zona.equals(""))
                                      perseus_resul+="<br><br><br><br>Actualizacion de todos los establecimientos. Ingrese porcentajes.<br>\n"+
															  "<form action='/ameca/liquidaciones' name='porcentajes'> \n <input type='hidden' name='operacion' value='ver_mv'>  \n"
															  + "<input type='hidden' name='do_masive' value='2'> "
															  + "<input type='hidden' name='periodo' value='"+periodo+"'>"+
															  "<table>\n<tr><td>Base imponible: <input type='text' name='base_imponible' value='+0' size='4'></td>\n"+
															  "<td>Compras: <input type='text' name='compra_iva' value='+0' size='4'></td>\n"+
															  "<td>Percepciones: <input type='text' name='percepcion_iva' value='+0' size='4'></td> <td> &nbsp;&nbsp;&nbsp; <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.porcentajes.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> \n"+
															  "</tr>\n</table>\n"+
															  "</form>";
                                  perseus_resul+="<br><br><br> registros: "+ rows;
                              }

                    }
            else
                { 
                    if(rows<1)
                                {
									resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
												"<input type='hidden' name='operacion' value='ver_mv'>"+
												"<table>"+
												"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
												"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
												"\n<td><a href='/ameca/liquidaciones?operacion=ver_mv&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
												"</tr>"+
												"</table> \n"+
												"</form> \n"+
												"<br><br>"+
												""+
												"<br><br><br><br>No se encontro actividad para el periodo. <br>";
									perseus_resul="";
								}
                    resul+="\n<br><br><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mv','MyWindow','width=600,height=300'); return false;\">"
								+ "<table><tr><td> PWD </td> <td> <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</td>"
								+ "</tr></table>"
								+ "</a><br><br> <br><br><br>";
                }
       //     resul+="\n<script>function go() {window.location.replace(\"/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mv\");} \n document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script>  ";
         //   resul+="<br><br><a href='/ameca/inicio?operacion=pwd&place=liquidaciones&dire=ver_mv&periodo="+periodo+"'> PWD <img src='/ameca/imgs/red_no.png' style='width:11px;height:11px;'></a><br><br>";
            
            
                


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
                resul= ex.getMessage();
                }
            }
        
		if (id_zona.equals("") && periodo.equals(htm.getPeriodo_nostatic())) 	// perseus sigue siendo verdadero como lo establecí al inicio, ie, no hubo updates en lo que dura el armado de la tabla de liquidaciones
				{ 	HTML.perseus_iva=perseus_resul+"<br><br><br> version perseus";
					HTML.perseus_bool_iva=true;
				}

		
				
        return resul+perseus_resul;  //tabla con datos de liquidacion del establecimiento... guardar la tabla en variable static en HTML, y borrar la variable cuando se hace un update o insert, o mejor sólo cuando se cambia algun campo (agregar comercio, editar comercio, nuevo mes "masivo", etc.). Mientras no haya cambios, será super rápida la visualizacion de las liquidaciones (sólo las del periodo actual [IIBB e IVA]).

        }
 
    
    

//    Recibe  el periodo y devuelve una tabla con los saldos DDJJ IIBB de los comercios para el periodo 
    private String LiquiMesIIBBTable(String periodo, String zona) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        int i=-1, id_zona, condicion_iibb, rows=0, length_obs ;
        String resul, row_span, query, nom_zona, id_establecimiento, direccion_establecimiento, nombre_responsable, nro_cuit, id_comercio, perseus_resul="";
        float cmrc_bi, cmrc_alic, cmrc_debito, cmrc_percepcion, cmrc_saldo, sum_bi=0.0f, sum_debito=0f, sum_percepcion=0f, sum_saldo=0f;

        try 
            {
			con=CX.getCx_pool();

           query="SELECT c.nro_cuit, c.nombre_responsable, CASE " +
                                                                                        "    WHEN casa_matriz =1 THEN CONCAT(\"(CM) \",e.direccion_establecimiento)\n" +
                                                                                        "    ELSE CONCAT(\"(suc) \",e.direccion_establecimiento)\n" +
                                                                                        "END as 'direccion', " +
                                                 "em.base_imponible, " + // 4
                                                "em.percepcion_iibb, " + // 5
                                                "em.saldo_iibb, " +     // 6
                                                "em.alicuota_iibb, " +  // 7
                                                "e.id_zona, " +     // 8  zona
                                                "(SELECT COUNT(*) "+
                                                  "FROM EstablecimientosLiquiMes eem, Establecimientos ee ";
            if(zona.equals(""))
                 query+="WHERE eem.periodo='"+periodo+"' AND eem.id_establecimiento=ee.id_establecimiento "+ //AND c.id_condicion_iibb not in (2, 4, 5) "+
                                                        " AND ee.id_comercio=e.id_comercio and eem.activo_iibb_periodo=1) as 'sucursales', c.id_condicion_iibb, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones) " +   // 9. count  10. cond iibb   11. id_establecimiento  12. id_comercio    13. length observaciones (si es cero muestro notepad_gris.png).
                                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                       " WHERE c.id_comercio = e.id_comercio AND em.activo_iibb_periodo=1" + 
                                           " AND e.id_establecimiento=em.id_establecimiento AND em.periodo='"+periodo+"' " +
                                       " ORDER BY nro_cuit, e.id_zona";
            else
                 query+="WHERE eem.periodo='"+periodo+"' AND ee.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+zona+") AND eem.id_establecimiento=ee.id_establecimiento"+
                                                        " AND ee.id_comercio=e.id_comercio and eem.activo_iibb_periodo=1) as 'sucursales', c.id_condicion_iibb, em.id_establecimiento, e.id_comercio, LENGTH (e.observaciones)  " +   // 9. count 10. cond iibb   11. id_establecimiento   12. id_comercio   13. length observaciones (si es cero muestro notepad_gris.png).
                                       " FROM Comercios c, Establecimientos e, EstablecimientosLiquiMes em " +
                                       " WHERE c.id_comercio = e.id_comercio  AND em.activo_iibb_periodo=1 "+ //AND c.id_condicion_iibb not in (2, 4, 5) " + 
                                           " AND e.id_establecimiento=em.id_establecimiento AND em.periodo='"+periodo+"' AND e.id_comercio IN (select eeee.id_comercio FROM Establecimientos eeee WHERE eeee.id_zona="+zona+") "+
                                       " ORDER BY nro_cuit";
                                                  

                
            pst = con.prepareStatement(query); 

            resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                        "<input type='hidden' name='operacion' value='ver_mb'>"+
                        "<table >"+
                        "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                        "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
                        "\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
                        "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
                        "<td width='40px'> </td>"
                    + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>";

        if (zona.equals(""))
            {
                resul+= "<td width='500px'></td>"+
                              "\n<td><a href='/ameca/reportes?operacion=excel&ref=iibb&periodo="+periodo+"'><img src='/ameca/imgs/guarda_xls.png' style='width:48px;height:48px;'></a>"+
                              "</td>";

                File tempFile = new File(HTML.folder+"/Reportes/"+htm.getPeriodo_year()+"/liqui_iibb_"+periodo+".xls");

                if (tempFile.exists())
                        {
                        Random rand = new Random();
                        int n = rand.nextInt(5988);                
                        resul+="\n<td width='25px'></td><td><a href='/ameca/Reportes/"+htm.getPeriodo_year()+"/liqui_iibb_"+periodo+".xls?a="+n+"'><img src='/ameca/imgs/excel.png' style='width:48px;height:48px;'></a></td>";

                        }
            }
        resul+="</tr>"+
                 "</table> \n"+
                 "</form> \n"+
                 "<br><br>";


            if (HTML.perseus_bool_iibb && zona.equals("") && periodo.equals(htm.getPeriodo_nostatic()))   //que verifique también que sea periodo actual y poner variables booleanas si está la tabla en string o no (puede que esté sólo tabla de iva o iibb)
						return resul+HTML.perseus_iibb;
						
						                 
         perseus_resul="<table class='grupos' width='1420px'>\n\t<tr>\n\t\t"+
                    "<th width='150px'>CUIT</th> <th width='300px'>Nombre</th>  <th width='150px'>Condicion IIBB</th> <th width='310px'>Direccion Establecimiento</th> <th width='120px'>Zona</th> "+
                    "<th width='120px'>Base Imponible</th> <th width='120px'>Alic. IIBB</th> <th width='120px'>Debito IIBB</th>  "+
                    "<th>Percepcion IIBB</th> <th>SALDO IIBB</th> <th width='40px'>Nota</th> "+
                    "    </tr>";

            rs = pst.executeQuery();
            
            
            while (rs.next())
                {
                id_zona= rs.getInt(8);
                nom_zona=htm.getZona(id_zona);
                condicion_iibb=rs.getInt(10);
                id_establecimiento=rs.getString(11);
                id_comercio=rs.getString(12);
                length_obs=rs.getInt(13);
                

                cmrc_bi=rs.getFloat(4);
                cmrc_percepcion=rs.getFloat(5);
                cmrc_alic=rs.getFloat(7);
                cmrc_saldo=rs.getFloat(6);
                cmrc_debito= cmrc_bi*cmrc_alic/100;
                rows=rs.getRow(); 

                nro_cuit=rs.getString(1);
                nombre_responsable=rs.getString(2);
                direccion_establecimiento=rs.getString(3);

                if (i<0)  //entra la primera vez y cuando se termino` con los establecimientos de un comercio
                    {
//                     row_span=rs.getString(9);
                     i=rs.getInt(9);
                     row_span=Integer.toString(i+1);   // cantidad de establecimientos del comercio
                     
                     
                     if (i==1)  // unico establecimiento del comercio
                        {
                         perseus_resul+="\n\t<tr> <td class='t11' height='30px'><a href='/ameca/comercios?operacion=detalle&id_comercio=" + id_comercio + "&nro_cuit=" + nro_cuit + "'>" + nro_cuit + "</a></td> <td class='t11'>"+nombre_responsable+"</td>  <td class='t11'>"+HTML.getCondicionIIBB(condicion_iibb)+"</td> <td class='t11'>"+direccion_establecimiento.substring(5)+"</td> <td class='t11'> "+nom_zona+"</td> "+
                            "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> <td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\"  onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>\n ";
                         else
                                perseus_resul+= "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td> <td class='t11' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\"  onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>\n ";
 //                           "\n\t</tr>\n <tr> <td colspan='8' bgcolor='#E8E7C1' height='1px'> </td></tr>";
                         i=-1;
                         sum_bi=0f;
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;
                        }
                     else     // hay al menos dos establecimientos del comercio... imprimo el primero y decremento el count de sucursales
                        {
                         perseus_resul+="\n\t<tr> <td rowspan='"+row_span+"' class='t11' valign='middle'><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>"+nro_cuit+"</a></td> <td rowspan='"+row_span+"' class='t11' valign='middle'>"+nombre_responsable+"</td>  <td rowspan='"+row_span+"' class='t11' valign='middle'>"+HTML.getCondicionIIBB(condicion_iibb)+"</td> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+nom_zona+"</b></td> "+
                            "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.1f",cmrc_alic)+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> "+
                            "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td>  ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;

                         
                            
                         i--;
                         sum_bi+=cmrc_bi;
                         
                         sum_percepcion+=cmrc_percepcion;
                         sum_debito+=cmrc_debito;
                         sum_saldo+=cmrc_saldo;
                        }
                         
                    }
                else if (i>0)   // proceso de la segunda sucursal en adelante
                    {
                     perseus_resul+="\n\t<tr> <td class='t2'><b>"+direccion_establecimiento+"</b></td> <td class='t2'><b> "+nom_zona+"</b></td> "+
											   "\n\t<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_bi)+"</td> <td class='t2'>"+cmrc_alic+"%</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_debito)+"</td> "+
											   "<td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_percepcion)+"</td> <td class='t2'>"+String.format(Locale.GERMAN, "%,.2f",cmrc_saldo)+"</td>  ";
                         if (length_obs>0)
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;
                         else
                                perseus_resul+= "<td class='t2' align='right'> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable, StandardCharsets.UTF_8)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento, StandardCharsets.UTF_8)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad_gris.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td>\n\t</tr>" ;

                     
                     
                       
                     i--;
                     //acumular floats, me parece que mejor sumar tambien saldo iibb por si el comercio tiene establecimientos de distinta provincia
                     sum_bi+=cmrc_bi;
                   
                     sum_percepcion+=cmrc_percepcion;
                     sum_debito+=cmrc_debito;
                     sum_saldo+=cmrc_saldo;

                     if (i==0)  // ya imprimio` todos los establecimientos del comercio, por lo que debe imprimir el total
                        {
                         perseus_resul+="\n\t<tr>  <td class='t11' height='25px'></td> <td class='t11'></td>  "+  //imprimo 'totales' o no.
                           "\n\t<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_bi)+"</td> <td class='t11'> </td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_debito)+"</td> "+
                           "<td class='t11'>"+String.format(Locale.GERMAN, "%,.2f",sum_percepcion)+"</td> <td class='t11'>"+String.format(Locale.GERMAN, "%,.2f", sum_saldo)+"</td> <td class='t11'></td></tr>\n";

                         i=-1;
                         sum_bi=0f;
                 
                         sum_percepcion=0f;
                         sum_debito=0f;
                         sum_saldo=0f;
                        }
                    }
                
                }
            perseus_resul+="\n\t</table>";
            
            
            
            if (user_level.equals("1") || user_level.equals("2") )
                    {
                     if(rows<1 )
                                {
									perseus_resul="";
                                    if(!periodo.equals(htm.getPeriodo_nostatic()))
                                            resul="<table >"+
														"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
														"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
														"\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
														"</tr>"+
														"</table> \n"+
														"<br><br>"
                                                        + ""
                                                        + "<br><br><br><br>No se encontro actividad para el periodo. <br>"+
                                                                "Carga el nuevo periodo con los establecimientos del periodo anterior? <br><br><br>"
                                                                + "<form name='masive' action='/ameca/liquidaciones'> \n "
                                                                + "<input type='hidden' name='operacion' value='ver_mb'> "
                                                                + "<input type='hidden' name='do_masive' value='1'> \n "
                                                                + "<input type='hidden' name='periodo' value='"+periodo+"'> \n "
                                                                + "<table><tr><td>Porcentaje a aplicar: <input type='text' name='base_imponible' value='+0' size='4'></td> <td width='35px'></td> "
                                                                + "<td><img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.masive.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"
                                                                + "</tr></table>\n"
                                                                + "</form>";
                                    
                                    else
                                            resul=  "<form action='/ameca/liquidaciones' name='periodo_zona'>"+
                                                        "<input type='hidden' name='operacion' value='ver_mb'>"+
                                                        "<table >"+
                                                        "<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                                                        "\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
                                                        "\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
                                                        "<td width='40px'></td><td>Zona: "+HTML.getDropZonas()+"</td>\n"+
                                                        "<td width='40px'> </td>"
                                                    + "<td> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.periodo_zona.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"+
                                                        "</tr>"+
                                                        "</table> \n"+
                                                        "</form> \n"+
                                                        "<br>"
                                                    + ""
                                                    + "<br><br><br><br>No se encontro actividad para el periodo. <br>";
                                        
                                    resul+="<br><br><br><br>";
                                    
                                }

                    else 
                              {
                                  if(zona.equals(""))
                                      perseus_resul+="<br><br><br><br>Actualizacion de todos los establecimientos. Ingrese porcentajes.<br>\n"+
                                              "<form action='/ameca/liquidaciones' name='porcentajes'> \n "
                                              + "<input type='hidden' name='operacion' value='ver_mb'>  \n"
                                              + "<input type='hidden' name='do_masive' value='2'> "
                                              + "<input type='hidden' name='periodo' value='"+periodo+"'>"+
                                              "<table>\n<tr><td>Base imponible: <input type='text' name='base_imponible' value='+0' size='4'></td>\n"+
                                              "<td>Percepciones: <input type='text' name='percepcion_iibb' value='+0' size='4'></td> <td> &nbsp;&nbsp;&nbsp; <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.porcentajes.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> \n"+
                                              "</tr>\n</table>\n"+
                                              "</form>";
                                  perseus_resul+="<br><br><br> registros: "+ rows;
                              }

                    }
            else
                { 
                    if(rows<1)
                               {
								 resul="<form action='/ameca/liquidaciones' name='periodo_zona'>"+
											"<input type='hidden' name='operacion' value='ver_mb'>"+
											"<table>"+
											"<tr>\n<td> <a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
											"\n<td align='center' valign='middle'> <input type='text' name='periodo' value='"+periodo+"' size='7'><br>" + htm.getPeriodo_prn(periodo) +"</td>"+
											"\n<td><a href='/ameca/liquidaciones?operacion=ver_mb&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td>"+
											"</tr>"+
											"</table> \n"+
											"</form> \n"+
											"<br><br>"+
											""+
											"<br><br><br><br>No se encontro actividad para el periodo. <br>";
								perseus_resul="";
								}
                    resul+="\n<br><br><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&periodo="+periodo+"&place=ver_mb','MyWindow','width=600,height=300'); return false;\">"
                    + "<table><tr><td> PWD </td> <td> <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</td>"
                    + "</tr></table>"
                    + "</a><br><br> <br><br><br>";
                }
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
                resul= ex.getMessage();
                }
            }
            
            
            
            
		if (zona.equals("") && periodo.equals(htm.getPeriodo_nostatic())) 
				{ 	HTML.perseus_iibb=perseus_resul+"<br><br><br> version perseus";
					HTML.perseus_bool_iibb=true;
				}

		
				
        return resul+perseus_resul;  
     }


    
    // update masivo de los establecimientos en liquidacion iva.
    // Esta fumcion está repetida, es la misma para iibb e iva, ya que actualiza todos los establecimientos activos de EstablecimientoLiquiMes.
    // Invocar con parametro "V" o "B" segun sea iibb o iva y "0" en parametros que no cambian o no corresponden (eg desde iibb "0" en compra_iva). Cambiar el nombre de la funcion! (updateMasivo)
    // Los parametros llegan con formato: +10 o -5 que representa el porcentaje a adhisionar o sustraer (validar esto en javascript).
    private String LiquiMesIVA_update(String periodo, String base_imponible, String percepcion_iva, String compra_iva) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        boolean query_ok=false;
        String res="Update EstablecimientosLiquiMes SET ";

        if (!base_imponible.equals("+0"))
            {
             res+="base_imponible = base_imponible*(1"+base_imponible+"/100) ";
             query_ok=true;
            }

        if (!percepcion_iva.equals("+0"))
            {
             if (query_ok)
                res+=", percepcion_iva = percepcion_iva*(1"+percepcion_iva+"/100) ";
            else
                res+=" percepcion_iva = percepcion_iva*(1"+percepcion_iva+"/100) ";
             query_ok=true;
            }

        if (!compra_iva.equals("+0"))
            {
             if (query_ok)
                 res+=", compra_iva = compra_iva*(1"+compra_iva+"/100) ";
             else
                 res+=" compra_iva = compra_iva*(1"+compra_iva+"/100) ";
             query_ok=true;
            }

        if (query_ok)
            res+=" WHERE periodo ='"+periodo+"'";
        else
            return "<br>no se ingresaron porcentajes validos<br>";
        try 
            {
            con=CX.getCx_pool();
            HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;

            pst = con.prepareStatement(res);  
 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);
            
            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
            pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                    + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                    +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                    + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            }
        catch (SQLException ex) {
             res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


    // update masivo de los establecimientos en liquidacion iibb.
    // Los parametros llegan con formato: +10 o -5 que representa el porcentaje a adhisionar o sustraer (validar esto en javascript).
    private String LiquiMesIIBB_update(String periodo, String base_imponible, String percepcion_iibb) 
	{
		HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        boolean query_ok=false;
        String res="Update EstablecimientosLiquiMes SET ";
        if (!base_imponible.equals("+0"))
            {
             res+="base_imponible = base_imponible*(1"+base_imponible+"/100) ";
             query_ok=true;
            }

        if (!percepcion_iibb.equals("+0"))
            {
             if (query_ok)
                res+=", percepcion_iibb = percepcion_iibb*(1"+percepcion_iibb+"/100) ";
             else
                res+=" percepcion_iibb = percepcion_iibb*(1"+percepcion_iibb+"/100) ";
             query_ok=true;
            }

        if (query_ok)
            res+=" WHERE periodo ='"+periodo+"'";
        else
            return "<br>no se ingresaron porcentajes validos<br>";
        try 
            {
            con=CX.getCx_pool();

            pst = con.prepareStatement(res);  
 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);

            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
           pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            }
        catch (SQLException ex) {
               res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


    
     // insert masivo IIBB de los establecimientos en EstablecimientoLiquiMes para nuevo periodo sin actividades todavia.
//    private String LiquiMes_masive (String periodo, String base_imponible, String percepcion_iva, String percepcion_iibb, String compra_iva) 
    private String LiquiMes_masive (String periodo, String porcentaje) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        int resul;
        String res="INSERT INTO EstablecimientosLiquiMes (id_establecimiento, base_imponible, compra_iva, percepcion_iva, percepcion_iibb, "+
                                                         "periodo, alicuota_iibb, alicuota_iva,  "+
                                                         "alicuota_pago_facil, reporte_ameca_comision, activo_iva_periodo, activo_iibb_periodo) "+
                    "SELECT em.id_establecimiento, base_imponible*(1"+porcentaje+"/100), compra_iva*(1"+porcentaje+"/100), percepcion_iva*(1"+porcentaje+"/100), percepcion_iibb*(1"+porcentaje+"/100), '" +
                          periodo+"', em.alicuota_iibb, em.alicuota_iva, " +
                         "em.alicuota_pago_facil, em.reporte_ameca_comision, em.activo_iva_periodo, em.activo_iibb_periodo " +
                    "FROM  EstablecimientosLiquiMes em, Establecimientos e " +
                    "WHERE periodo ='"+htm.getPeriodo_nostatic()+"' and e.id_establecimiento=em.id_establecimiento and (em.activo_iibb_periodo or em.activo_iva_periodo) ";
try 
            {
			HTML.perseus_bool_iibb=false; HTML.perseus_bool_iva=false;
            con=CX.getCx_pool();

            pst = con.prepareStatement(res); 
            resul= pst.executeUpdate();
            res= Integer.toString(resul);

            HTML.setIgnited_parametros(false);
            HTML.cargaParametros();
            HTML.setIgnited_periodo(false);
            htm.Carga_periodo();

            
            // actualizo el saldo iva e iibb con los nuevos valores de base imp, compra_iva, percepciones...   mejor seria trigger
            pst = con.prepareStatement("UPDATE EstablecimientosLiquiMes "
                    + "SET saldo_iibb=base_imponible*alicuota_iibb/100 - percepcion_iibb, "
                    +       " saldo_iva=base_imponible*alicuota_iva/100 - percepcion_iva - compra_iva*alicuota_iva/100 "
                    + "where periodo='"+periodo+"'");
            resul= pst.executeUpdate();
            res+= Integer.toString(resul);

            
            
            
            // los incrementos porcentuales van en update principal y por ultimo otro update que recalcule los saldos.
            // en saldo_iva, en vez del +1, se deja el 1 solo xq se recibe +10 o -20: TRUNCATE(base_imponible*(1+"+base_imponible+"/100)*alicuota_iva/100-percepcion_iva*(1+"+percepcion_iva+"/100)-compra_iva*(1+"+compra_iva+"/100)*alicuota_iva/100,2) as 'saldo2'
            }
        catch (SQLException ex) {
               res= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                res= ex.getMessage();
                }
            }
        
        return res; // cant de registros modificados o msg de la excepcion
        }


       public static boolean isNumeric(String strNum) {
           if (strNum == null) {
               return false;
           }
           try {
               int i = Integer.parseInt(strNum);
           } catch (NumberFormatException nfe) {
               return false;
           }
           return true;
       }
    
}