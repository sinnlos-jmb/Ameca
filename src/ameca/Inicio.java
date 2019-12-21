package ameca;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;




/**
 *
 * @author manu
 */
public class Inicio extends HttpServlet {

        HTML htm=new HTML();

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }
    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
    response.setContentType("text/html");
    
    
    Cookie cook = null;
    Cookie[] cookies = request.getCookies();
    String pwd_cookie="";            

    if( cookies != null )
            {
                cook = cookies[0];
                pwd_cookie=cook.getValue();
            }

    
    String user_pwd  = request.getParameter ("user_pwd") != null ?  request.getParameter ("user_pwd") : "" ;
    Boolean pwd_ok=false;
    Boolean pwd_admin=false;
    
     if (user_pwd.equals(htm.getPWD_day()))
                    {
                        Cookie user = new Cookie("ok", "yes");
                        user.setMaxAge(60*60*12);
                        response.addCookie(user);
                        pwd_ok=true;
                    }    
     if (user_pwd.equals("1234"))
                    {
                        Cookie user = new Cookie("ok", "yes_admin");
                        user.setMaxAge(60*60*24*7);
                        response.addCookie(user);
                        pwd_admin=true;
                    }    

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
    if (!HTML.getIgnited_parametros())
         HTML.cargaParametros();
    
        
    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String op2  = request.getParameter ("op2") != null ?  request.getParameter ("op2") : "" ;
    String new_periodo  = request.getParameter ("new_periodo") != null ?  request.getParameter ("new_periodo") : "0" ;
    String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : "" ;
    String set_periodo  = request.getParameter ("set_periodo") != null ?  request.getParameter ("set_periodo") : "0" ;
    String place  = request.getParameter ("place") != null ?  request.getParameter ("place") : "" ;

    String alic_iva  = request.getParameter ("alic_iva") != null ?  request.getParameter ("alic_iva") : htm.getIVA_prn() ;
    String alic_iibb_caba  = request.getParameter ("alic_iibb_caba") != null ?  request.getParameter ("alic_iibb_caba") : htm.getIIBB_caba_prn() ;
    String alic_iibb_ba  = request.getParameter ("alic_iibb_ba") != null ?  request.getParameter ("alic_iibb_ba") : htm.getIIBB_ba_prn() ;
    String alic_pf  = request.getParameter ("alic_pf") != null ?  request.getParameter ("alic_pf") : HTML.getAlicuotaPF_prn() ;
    String ameca_pf  = request.getParameter ("ameca_pf") != null ?  request.getParameter ("ameca_pf") : HTML.getComisionPF_prn() ;

    String alic_iva_new  = request.getParameter ("alic_iva_new") != null ?  request.getParameter ("alic_iva_new") : "" ;
    String alic_iibb_caba_new  = request.getParameter ("alic_iibb_caba_new") != null ?  request.getParameter ("alic_iibb_caba_new") : "" ;
    String alic_iibb_ba_new  = request.getParameter ("alic_iibb_ba_new") != null ?  request.getParameter ("alic_iibb_ba_new") : "" ;
    String alic_pf_new  = request.getParameter ("alic_pf_new") != null ?  request.getParameter ("alic_pf_new") : "" ;
    String ameca_pf_new  = request.getParameter ("ameca_pf_new") != null ?  request.getParameter ("ameca_pf_new") : "" ;


    


    switch (operacion)
        {
        case "pwd": out.println("<br><br>"
                + "<form action='/ameca/inicio' name='pwds' method='get'>"
                + "ingrese contrasena:<input type='text' name='user_pwd' value=''> <br>"
                + "<input type='hidden' name='operacion' value='pwd2'>"
                + "<input type='hidden' name='place' value='"+place+"'>"
                + "<input type='hidden' name='periodo' value='"+periodo+"'>"
                + "<input type='submit'>"
                + "</form>");
                break;
        case "pwd2": if (pwd_ok || pwd_admin)
                                    {
                                        out.println("<!DOCTYPE html><html><head><title>Ameca - Password</title>\n </head> \n <body>  \n\n <br><br> \n");  
                                        out.println("<script>"
                                                        + "function go() ");
                                        if (place.substring(0,3).equals("ver"))
                                             out.println( "{window.opener.document.location.replace(\"/ameca/liquidaciones?operacion="+place+"&periodo="+periodo+"\"); \n ");
                                        else
                                             out.println( "{window.opener.document.location.replace(\"/ameca/"+place+"\"); \n ");
                                            
                                       out.println(   "window.close();"
                                                            + "} \n "
                                                        + "document.body.style.background='green'; \n window.setTimeout(go, 40); \n"
                                                    + "</script>"
                                                + "</body></html>");
                                    }
                            else 
                                    {
                                        out.println("<!DOCTYPE html><html><head><title>Ameca - Password</title>\n </head> \n <body>  \n\n <br><br> \n");  
                                        out.println("<script>"
                                                        +   "function go() { window.close();} \n "
                                                        +   "document.body.style.background='red'; \n window.setTimeout(go, 6000); \n"
                                                        + "</script><br>Contrasena equivocada!<br>"
//                                                + "USER_PW: "+user_pwd+"<br>"
//                                                + "pw: "+htm.getPWD_day()+"</body></html>");
                                                + "</body></html>");
                                    }
                                        
                break;
        case "bk_clientes": out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
                     out.println("<table>"
                                    + "<tr><td>"+htm.getPeriodo_prn_long()+"</td></tr>");
                     if (pwd_cookie.equals("yes_admin"))
                            out.println("<tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>" 
                                     + "<tr><td><br><a href='/ameca/inicio?operacion=edit_alics'>Actualizar Al&iacute;cuotas</a></td></tr>"); 

                   out.println("<tr><td height='333px'></td></tr>"
                                        + "</table>");
          out.println("<script>function go() {window.location.replace(\"/ameca//Reportes/"+htm.getPeriodo_year()+"/clientes_"+htm.getPeriodo_nostatic()+".xls\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                    out.println(HTML.getTail());
                break;
        case "edit_alics": 
                    switch (op2)
                        {
                        case "save_alic_iva":
                                out.println(updateAlic_iva (alic_iva_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_alic_iibb_caba":
                                out.println(updateAlic_iibb_caba (alic_iibb_caba, alic_iibb_caba_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_alic_iibb_ba":
                                out.println(updateAlic_iibb_ba (alic_iibb_ba, alic_iibb_ba_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_alic_pf":
                                out.println(updateAlic_pf (alic_pf_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_ameca_pf":
                                out.println(updateAlic_ameca_pf (ameca_pf_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        default:
                     out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
                     out.println("<table>"
                                    + "<tr><td>"+htm.getPeriodo_prn_long()+"</td></tr>");
                     if (pwd_cookie.equals("yes_admin"))
                            out.println("<tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>"); 
                    
                            out.println("<tr><td height='55px'></td></tr>"
                                        + "<tr><td><b>Actualizaci&oacute;n de Al&iacute;cuotas</b><br></td></tr>"
                                        + "<tr><td height='3px'></td></tr>"
                                        + "<tr><td><form action='/ameca/inicio' name='alics' method='post'> \n"
                                        + "<table cellSpacing='2' cellPadding='0'>\n\t"
                                        + "<tr>\n\t\t<td colspan=2 align='center'>Valores Actuales<td></td>  <td align='center'>Nuevos Valores</td><td></td></tr>\n\t"
                                        + "<tr><td height='3px'></td></tr>"
                                        + "<tr>\n\t\t<td>IVA: </td><td><input type='text' size='4' name='alic_iva' value='"+alic_iva+"' disabled>%</td>  <td width='28px'></td> <td align='center'><input type='text' size='4' name='alic_iva_new' value=''>%</td>  <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_alic_iva'; document.alics.alic_iva.value=document.alics.alic_iva_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td></tr>\n\t"
                                        + "<tr>\n\t\t<td>IIBB (CABA): </td><td><input type='text' size='4' name='alic_iibb_caba' value='"+alic_iibb_caba+"' disabled>%</td>  <td width='28px'></td><td align='center'><input type='text' size='4' name='alic_iibb_caba_new' value=''>%</td>  <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_alic_iibb_caba';  document.alics.alic_iibb_caba.value=document.alics.alic_iibb_caba_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td></tr>\n\t"
                                        + "<tr>\n\t\t<td>IIBB (Pcia. BA): </td><td><input type='text' size='4' name='alic_iibb_ba' value='"+alic_iibb_ba+"' disabled>%</td> <td width='28px'></td><td align='center'><input type='text' size='4' name='alic_iibb_ba_new' value=''>%</td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_alic_iibb_ba'; document.alics.alic_iibb_ba.value=document.alics.alic_iibb_ba_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td></tr>\n\t"
                                        + "<tr>\n\t\t<td>Pago Facil: </td><td><input type='text' size='4' name='alic_pf' value='"+alic_pf+"' disabled>%</td>  <td width='28px'></td><td align='center'><input type='text' size='4' name='alic_pf_new' value=''>%</td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_alic_pf'; document.alics.alic_pf.value=document.alics.alic_pf_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
                                        + "<tr>\n\t\t<td>Comisi&oacute;n Ameca: $</td><td><input type='text' size='6' name='ameca_pf' value='"+ameca_pf+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='ameca_pf_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_ameca_pf'; document.alics.ameca_pf.value=document.alics.ameca_pf_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
                                        + "<tr>\n\t\t<td colspan='2'><input type='hidden' name='operacion' value='edit_alics'><input type='hidden' name='op2' value='save'>"
                                        + "\n </td> </tr></table>\n"
                                        + "</form>\n\n"
                                        + ""
                                        + "</table><br><br>alic_pf: "+HTML.getAlicuotaPF_prn());
                    out.println(HTML.getTail());
                        }
                break;
        default: out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
                     out.println("<table>"
                    //                + "<tr><td>"+htm.getPeriodo_prn()+"</td></tr>"
                                    + "<tr><td>"+htm.getPeriodo_prn_long()+"</td></tr>");
                     if (pwd_cookie.equals("yes_admin"))
                            out.println("<tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>" 
                                    + "<tr><td><br><a href='/ameca/inicio?operacion=edit_alics'>Actualizar Al&iacute;cuotas</a></td></tr>"); 
                    //                + "<tr><td>periodo: "+htm.getPeriodo_nostatic()+"</td></tr>"
                    //                + "<tr><td>periodo_com: <a href='/ameca/inicio?new_periodo=1'>UPDATE PERIODO</a> </td></tr>"
                    //                + "<tr><td>periodo_com: <a href='/ameca/inicio?new_periodo=2&set_periodo=201912'>SET PERIODO 201912</a> </td></tr>"
                     else
                            out.println("\n<tr><td><table><tr><td><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&place=inicio','MyWindow','width=600,height=300'); return false;\">"
                                    + " <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\"></a></td></tr></table>\n</td></tr>\n");
                     out.println("<tr><td height='333px'></td></tr></table>");
                    
                    out.println(HTML.getTail());
        }
        
/*        
        
        switch (new_periodo)
            {
            case "1": htm.Carga_periodo();
                    break;
            case "2": htm.setPeriodo_nostatic(set_periodo);
                    break;
        }
            
        out.println("<table>"
                + "<tr><td height='50px'></td></tr>"
//                + "<tr><td>carga manual realizada</td></tr>"
 //               + "<tr><td>ignited periodo?: "+HTML.getIgnited_periodo()+"</td></tr>"
  //              + "<tr><td>periodo: "+htm.getPeriodo_nostatic()+"</td></tr>"
//                + "<tr><td>periodo_com: "+HTML.getPeriodo_com()+"</td></tr>"
                + "</table>"); 
*/
    
    
    }
    
      // update alicuotas en tabla Parametros 
    private String updateAlics (String alic_iva, String alic_iibb_caba, String alic_iibb_ba, String alic_pf, String ameca_pf) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="";
        resul="UPDATE Parametros SET alicuota_iva="+alic_iva+", alicuota_iibb_caba="+alic_iibb_caba+", alicuota_iibb_provincia="+alic_iibb_ba+", alicuota_pago_facil="+alic_pf+", comision_pago_facil="+ameca_pf;

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update comercio";
            HTML.setIgnited_parametros(Boolean.FALSE);

            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
    



// update alicuota IVA 
    private String updateAlic_iva (String alic_iva_new) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE Parametros SET alicuota_iva="+alic_iva_new; 
        String query="UPDATE EstablecimientosLiquiMes SET alicuota_iva="+alic_iva_new+" , saldo_iva=base_imponible*"+alic_iva_new+"/100 - percepcion_iva - compra_iva*"+alic_iva_new+"/100 "
                    + "WHERE periodo= '"+htm.getPeriodo_nostatic()+"' ";


        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_parametros(Boolean.FALSE);

            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();

            if(resul_insert<1)
                resul="problema en update saldo_iva";
            
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
       



// update alicuota IIBB CABA 
    private String updateAlic_iibb_caba (String alic_iibb_caba, String alic_iibb_caba_new) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE Parametros SET alicuota_iibb_caba="+alic_iibb_caba_new; 
        String query="UPDATE EstablecimientosLiquiMes SET alicuota_iibb="+alic_iibb_caba_new+" , saldo_iibb=base_imponible*"+alic_iibb_caba_new+"/100 - percepcion_iibb "
                    + "WHERE alicuota_iibb="+alic_iibb_caba+" AND periodo= '"+htm.getPeriodo_nostatic()+"' ";


        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_parametros(Boolean.FALSE);

            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();

            if(resul_insert<1)
                resul="problema en update saldo_iibb_caba";
            
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
       

// update alicuota IIBB BA 
    private String updateAlic_iibb_ba (String alic_iibb_ba, String alic_iibb_ba_new) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE Parametros SET alicuota_iibb_provincia="+alic_iibb_ba_new; 
        String query="UPDATE EstablecimientosLiquiMes SET alicuota_iibb="+alic_iibb_ba_new+" , saldo_iibb=base_imponible*"+alic_iibb_ba_new+"/100 - percepcion_iibb "
                    + "WHERE alicuota_iibb="+alic_iibb_ba+" AND periodo= '"+htm.getPeriodo_nostatic()+"' ";

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_parametros(Boolean.FALSE);

            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();

            if(resul_insert<1)
                resul="problema en update saldo_iibb_ba";
            
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota PAGO FACIL 
    private String updateAlic_pf (String alic_pf_new) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE Parametros SET alicuota_pago_facil="+alic_pf_new; 
        String query="UPDATE EstablecimientosLiquiMes SET alicuota_pago_facil="+alic_pf_new
                    + " WHERE periodo= '"+htm.getPeriodo_nostatic()+"' ";

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_parametros(Boolean.FALSE);

            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();

            if(resul_insert<1)
                resul="problema en update alic pago facil";
            
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
    

// update comision ameca PAGO FACIL 
    private String updateAlic_ameca_pf (String ameca_pf_new) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE Parametros SET comision_pago_facil="+ameca_pf_new; 
        String query="UPDATE EstablecimientosLiquiMes SET reporte_ameca_comision="+ameca_pf_new
                    + " WHERE periodo= '"+htm.getPeriodo_nostatic()+"' ";

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_parametros(Boolean.FALSE);

            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();

            if(resul_insert<1)
                resul="problema en update alic pago facil";
            
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
    
    
    
    
}