package ameca;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * @author manu
 */

public class Inicio extends HttpServlet {

        HTML htm=new HTML();

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws IOException //ServletException,
   { doGet(request, response); }
    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException//, ServletException
    {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
	HttpSession session = request.getSession(false)!= null ? request.getSession(false): request.getSession();
		// en las otras paginas si no hay sesion o expiró, deben enviar a inicio.

	String user_level  = session.getAttribute("user_level") != null ?  (String)session.getAttribute("user_level") : "0" ;
	String user_name  = session.getAttribute("user_name") != null ?  (String)session.getAttribute("user_name") : "" ;

    String user_nom  = request.getParameter ("user_nom") != null ?  request.getParameter ("user_nom") : "" ;  
    String user_pwd  = request.getParameter ("user_pwd") != null ?  request.getParameter ("user_pwd") : "" ;  

        
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
    String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : "" ;
    String place  = request.getParameter ("place") != null ?  request.getParameter ("place") : "" ;
    String alic_iva  = request.getParameter ("alic_iva") != null ?  request.getParameter ("alic_iva") : htm.getIVA_prn() ;
    String alic_iibb_caba  = request.getParameter ("alic_iibb_caba") != null ?  request.getParameter ("alic_iibb_caba") : htm.getIIBB_caba_prn() ;
    String alic_iibb_ba  = request.getParameter ("alic_iibb_ba") != null ?  request.getParameter ("alic_iibb_ba") : htm.getIIBB_ba_prn() ;
    String alic_pf  = request.getParameter ("alic_pf") != null ?  request.getParameter ("alic_pf") : HTML.getAlicuotaPF_prn() ;
    String ameca_pf  = request.getParameter ("ameca_pf") != null ?  request.getParameter ("ameca_pf") : HTML.getComisionPF_prn() ;
    String alic_autonomo_30  = request.getParameter ("alic_autonomo_30") != null ?  request.getParameter ("alic_autonomo_30") : htm.getMontoAutonomo(1) ;
    String alic_autonomo_1530  = request.getParameter ("alic_autonomo_1530") != null ?  request.getParameter ("alic_autonomo_1530") : htm.getMontoAutonomo(2) ;
    String alic_autonomo_15  = request.getParameter ("alic_autonomo_15") != null ?  request.getParameter ("alic_autonomo_15") : htm.getMontoAutonomo(3) ;
    String alic_autonomo_20mas  = request.getParameter ("alic_autonomo_20mas") != null ?  request.getParameter ("alic_autonomo_20mas") : htm.getMontoAutonomo(4) ;
    String alic_autonomo_20menos  = request.getParameter ("alic_autonomo_20menos") != null ?  request.getParameter ("alic_autonomo_20menos") : htm.getMontoAutonomo(5) ;
    String alic_autonomo_25mas  = request.getParameter ("alic_autonomo_25mas") != null ?  request.getParameter ("alic_autonomo_25mas") : htm.getMontoAutonomo(6) ;
    String alic_autonomo_25menos  = request.getParameter ("alic_autonomo_25menos") != null ?  request.getParameter ("alic_autonomo_25menos") : htm.getMontoAutonomo(7) ;

    String alic_afiliaciones  = request.getParameter ("alic_afiliaciones") != null ?  request.getParameter ("alic_afiliaciones") : htm.getMontoAutonomo(8) ;
    String alic_menores  = request.getParameter ("alic_menores") != null ?  request.getParameter ("alic_menores") : htm.getMontoAutonomo(9) ;
    String alic_jubilados  = request.getParameter ("alic_jubilados") != null ?  request.getParameter ("alic_jubilados") : htm.getMontoAutonomo(10) ;
    String alic_amas  = request.getParameter ("alic_amas") != null ?  request.getParameter ("alic_amas") : htm.getMontoAutonomo(11) ;

	// borrar '.' y cambiar ',' por '.'
	alic_autonomo_30=alic_autonomo_30.replace(".", "");
    alic_autonomo_1530=alic_autonomo_1530.replace(".", "");
    alic_autonomo_15=alic_autonomo_15.replace(".", "");
    alic_autonomo_20mas=alic_autonomo_20mas.replace(".", "");
    alic_autonomo_20menos=alic_autonomo_20menos.replace(".", "");
    alic_autonomo_25mas=alic_autonomo_25mas.replace(".", "");
    alic_autonomo_25menos=alic_autonomo_25menos.replace(".", "");
    alic_afiliaciones=alic_afiliaciones.replace(".", "");
    alic_menores=alic_menores.replace(".", "");
    alic_jubilados=alic_jubilados.replace(".", "");
    alic_amas=alic_amas.replace(".", "");

    alic_autonomo_30=alic_autonomo_30.replace(",", ".");
    alic_autonomo_1530=alic_autonomo_1530.replace(",", ".");
    alic_autonomo_15=alic_autonomo_15.replace(",", ".");
    alic_autonomo_20mas=alic_autonomo_20mas.replace(",", ".");
    alic_autonomo_20menos=alic_autonomo_20menos.replace(",", ".");
    alic_autonomo_25mas=alic_autonomo_25mas.replace(",", ".");
    alic_autonomo_25menos=alic_autonomo_25menos.replace(",", ".");
    alic_afiliaciones=alic_afiliaciones.replace(",", ".");
    alic_menores=alic_menores.replace(",", ".");
    alic_jubilados=alic_jubilados.replace(",", ".");
    alic_amas=alic_amas.replace(",", ".");	


    String alic_iva_new  = request.getParameter ("alic_iva_new") != null ?  request.getParameter ("alic_iva_new") : "" ;
    String alic_iibb_caba_new  = request.getParameter ("alic_iibb_caba_new") != null ?  request.getParameter ("alic_iibb_caba_new") : "" ;
    String alic_iibb_ba_new  = request.getParameter ("alic_iibb_ba_new") != null ?  request.getParameter ("alic_iibb_ba_new") : "" ;
    String alic_pf_new  = request.getParameter ("alic_pf_new") != null ?  request.getParameter ("alic_pf_new") : "" ;
    String ameca_pf_new  = request.getParameter ("ameca_pf_new") != null ?  request.getParameter ("ameca_pf_new") : "" ;
    String alic_autonomo_30_new  = request.getParameter ("alic_autonomo_30_new") != null ?  request.getParameter ("alic_autonomo_30_new") : "0" ;
    String alic_autonomo_1530_new  = request.getParameter ("alic_autonomo_1530_new") != null ?  request.getParameter ("alic_autonomo_1530_new") : "0" ;
    String alic_autonomo_15_new  = request.getParameter ("alic_autonomo_15_new") != null ?  request.getParameter ("alic_autonomo_15_new") : "0" ;
    String alic_autonomo_20mas_new  = request.getParameter ("alic_autonomo_20mas_new") != null ?  request.getParameter ("alic_autonomo_20mas_new") : "0" ;
    String alic_autonomo_20menos_new  = request.getParameter ("alic_autonomo_20menos_new") != null ?  request.getParameter ("alic_autonomo_20menos_new") : "0" ;
    String alic_autonomo_25mas_new  = request.getParameter ("alic_autonomo_25mas_new") != null ?  request.getParameter ("alic_autonomo_25mas_new") : "0" ;
    String alic_autonomo_25menos_new  = request.getParameter ("alic_autonomo_25menos_new") != null ?  request.getParameter ("alic_autonomo_25menos_new") : "0" ;

    String alic_afiliaciones_new  = request.getParameter ("alic_afiliaciones_new") != null ?  request.getParameter ("alic_afiliaciones_new") : "0" ;
    String alic_menores_new  = request.getParameter ("alic_menores_new") != null ?  request.getParameter ("alic_menores_new") : "0" ;
    String alic_jubilados_new  = request.getParameter ("alic_jubilados_new") != null ?  request.getParameter ("alic_jubilados_new") : "0" ;
    String alic_amas_new  = request.getParameter ("alic_amas_new") != null ?  request.getParameter ("alic_amas_new") : "0" ;

    

    switch (operacion)
        {
        case "pwd": out.println("<br><br>" // formulario del popup para log-in; la primera vez voy a caer en el default de este switch que muestra la llave para loguearse (y vuelve con user_pwd y user_nom definidos y op=pwd & place=inicio como parametros)
                + "<form action='/ameca/inicio' name='pwds' method='post'>"
                + "ingrese usuario:<input type='text' name='user_nom' value=''> <br>"
                + "ingrese contrase&ntilde;a:<input type='password' name='user_pwd' value=''> <br>"
                + "<input type='hidden' name='operacion' value='pwd2'>"
                + "<input type='hidden' name='place' value='"+place+"'>"  // en place le dice al popup a que pagina ir despues del log-in (eg inicio, liquidaciones, etc).
                + "<input type='hidden' name='periodo' value='"+periodo+"'>"
                + "<input type='submit'>"
                + "</form>");
                break;
        case "pwd2": user_level= check (user_pwd, user_nom);   // pop up con fondo verde o rojo según usuario existente o no.
					 if (user_level.equals("1") || user_level.equals("2") )  // acá sencillamente vuelve a la página desde donde se logueó el usuario (place)
						   {session.setMaxInactiveInterval(36000);  // diez horas.
							session.setAttribute("user_level", user_level);
							session.setAttribute("user_name", user_nom);
							out.println("<!DOCTYPE html><html><head><title>Ameca - Password</title>\n </head> \n <body>  \n\n <br><br> \n");
							out.println("<script>"
											+ "function go() ");
							if (place.startsWith("ver"))
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
											+ "function go() { window.close();} \n "
											+ "document.body.style.background='red'; \n window.setTimeout(go, 6000); \n"
											+ "</script><br>Contrase&ntilde;a equivocada!<br>"
											+ "</body></html>");
						}
							
       break;
       case "bk_clientes": out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
                     out.println("<table>"
                                    + "<tr><td>"+htm.getPeriodo_prn_long()+"</td></tr>");
                     if (user_level.equals("2"))
                            out.println("<tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>" 
                                    + "<tr><td><br><a href='/ameca/inicio?operacion=edit_alics'>Actualizar Al&iacute;cuotas</a></td></tr>"); 

                   out.println("<tr><td height='333px'></td></tr>"
                                        + "</table>");
          out.println("<script>function go() {window.location.replace(\"/ameca//Reportes/"+htm.getPeriodo_year()+"/clientes_"+htm.getPeriodo_nostatic()+".xls\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                    out.println(HTML.getTail());
                break;
        case "edit_alics":    // esta es la opción principal de editar alícuotas (entra por acá cuando usuario ya completó algún text_box y dio ok.
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
						case "save_alic_pf":  //pago facil
                                out.println(updateAlic_pf (alic_pf_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_ameca_pf":
                                out.println(updateAlic_ameca_pf (ameca_pf_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
//autonomos 
                        case "save_autonomo_30":
                                out.println(updateAlic_autonomo_30 (alic_autonomo_30_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_1530":
                                out.println(updateAlic_autonomo_1530 (alic_autonomo_1530_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_15":
                                out.println(updateAlic_autonomo_15 (alic_autonomo_15_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_20mas":
                                out.println(updateAlic_autonomo_20mas (alic_autonomo_20mas_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_20menos":
                                out.println(updateAlic_autonomo_20menos (alic_autonomo_20menos_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_25mas":
                                out.println(updateAlic_autonomo_25mas (alic_autonomo_25mas_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_autonomo_25menos":
                                out.println(updateAlic_autonomo_25menos (alic_autonomo_25menos_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_afiliaciones":
                                out.println(updateAlic_afiliaciones (alic_afiliaciones_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_menores":
                                out.println(updateAlic_menores (alic_menores_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_jubilados":
                                out.println(updateAlic_jubilados (alic_jubilados_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        case "save_amas":
                                out.println(updateAlic_amas (alic_amas_new));
                                out.println("<script>function go() {window.location.replace(\"/ameca/inicio?operacion=edit_alics\"); } document.body.style.background='green'; \n window.setTimeout(go, 10); \n</script><br>");
                            break;
                        default:   // del switch on op2 dentro de edit_alics (ie el usuario no modificó ninguna alícuota pero llegó el parámetro op=edit_alics)
                     out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
                     out.println("<table>"
                                    + "<tr><td>"+htm.getPeriodo_prn_long()+"</td></tr>");
                     if (user_level.equals("2"))
                            out.println("<tr><td>Hola: "+user_name+"</td></tr>"
									+ "<tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>"); 
                     else if (user_level.equals("1"))
                            out.println("<tr><td>Hola: "+user_name+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>"); 
                    
					out.println(" <tr><td height='55px'></td></tr>"
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
								+ "</table> <br><br>Aut&oacute;nomos:<br>\n\t"
					// autonomos
								+ "<table><tr>\n\t\t<td>Socs. Comerciales (> a 30.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_30' value='"+alic_autonomo_30+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_30_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_30'; document.alics.alic_autonomo_30.value=document.alics.alic_autonomo_30_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Socs. Comerciales (>15.000 y <= a 30.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_1530' value='"+alic_autonomo_1530+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_1530_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_1530'; document.alics.alic_autonomo_1530.value=document.alics.alic_autonomo_1530_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Socs. Comerciales (<=15.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_15' value='"+alic_autonomo_15+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_15_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_15'; document.alics.alic_autonomo_15.value=document.alics.alic_autonomo_15_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Activs. No Incluidas en 1. (>20.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_20mas' value='"+alic_autonomo_20mas+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_20mas_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_20mas'; document.alics.alic_autonomo_20mas.value=document.alics.alic_autonomo_20mas_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Activs. No Incluidas en 1. (<=20.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_20menos' value='"+alic_autonomo_20menos+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_20menos_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_20menos'; document.alics.alic_autonomo_20menos.value=document.alics.alic_autonomo_20menos_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Otras Activs. (>25.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_25mas' value='"+alic_autonomo_25mas+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_25mas_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_25mas'; document.alics.alic_autonomo_25mas.value=document.alics.alic_autonomo_25mas_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Otras Activs. (<=25.000): </td><td>$ <input type='text' size='6' name='alic_autonomo_25menos' value='"+alic_autonomo_25menos+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_autonomo_25menos_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_autonomo_25menos'; document.alics.alic_autonomo_25menos.value=document.alics.alic_autonomo_25menos_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"

								+ "<tr>\n\t\t<td>Afiliaciones Voluntarias: </td><td>$ <input type='text' size='6' name='alic_afiliaciones' value='"+alic_afiliaciones+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_afiliaciones_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_afiliaciones'; document.alics.alic_afiliaciones.value=document.alics.alic_afiliaciones_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Menores (de 18 a 21 años): </td><td>$ <input type='text' size='6' name='alic_menores' value='"+alic_menores+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_menores_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_menores'; document.alics.alic_menores.value=document.alics.alic_menores_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Jubilados: </td><td>$ <input type='text' size='6' name='alic_jubilados' value='"+alic_jubilados+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_jubilados_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_jubilados'; document.alics.alic_jubilados.value=document.alics.alic_jubilados_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td>Amas de Casa: </td><td>$ <input type='text' size='6' name='alic_amas' value='"+alic_amas+"' disabled></td> <td width='28px'></td><td align='center'>$ <input type='text' size='6' name='alic_amas_new' value=''></td> <td> <img src=\"/ameca/imgs/ok_32.png\" style=\"width:32px;height:32px;\" onclick=\"document.alics.op2.value='save_amas'; document.alics.alic_amas.value=document.alics.alic_amas_new.value; document.alics.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td> </tr>\n\t"
								+ "<tr>\n\t\t<td colspan='2'><input type='hidden' name='operacion' value='edit_alics'><input type='hidden' name='op2' value='save'>"
								+ "\n </td> </tr></table>\n"
								+ "</form>\n\n"
								+ ""
								+ "</table>");
                    out.println(HTML.getTail());
                        }
                break;
				default: out.println(HTML.getHead("inicio", htm.getPeriodo_nostatic())); // entro al servlet inicio sin definir la op
                     out.println("<table>"
                                    + "<tr><td>Periodo vigente: "+htm.getPeriodo_prn_long()+"</td></tr>");
					//if (pwd_cookie.equals("yes_admin"))   
                     switch (user_level) 
							{
							 case "2": out.println("<tr><td>Hola: "+user_name+"</td></tr> \n <tr><td>password de hoy: "+htm.getPWD_day()+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/reportes?operacion=bk_establecimientos'>Listado de Clientes (excel)</a></td></tr>" 
                                    + "<tr><td><br><a href='/ameca/inicio?operacion=edit_alics'>Actualizar Al&iacute;cuotas</a></td></tr>");
							 break;							
							 case "1": out.println("<tr><td>Hola: "+user_name+"</td></tr>"
                                    + "<tr><td><br><a href='/ameca/inicio?operacion=edit_alics'>Actualizar Al&iacute;cuotas</a></td></tr>");
							 break;	// en user_level cero caigo cuando entro a inicio sin definir op y no hay session con user_level y user_name definidos. Muestro la llave para loguearse.
							 case "0": out.println("\n<tr><td><table><tr><td><a href=\"#\" onClick=\"MyWindow=window.open('/ameca/inicio?operacion=pwd&place=inicio','MyWindow','width=600,height=300'); return false;\">"
                                    + " <img src=\"/ameca/imgs/key32.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\"></a></td></tr></table>\n</td></tr>\n");
							 break;							
							}
                     
                 out.println("<tr><td height='333px'></td></tr></table>");
                    
                 out.println(HTML.getTail());
        }


    }


    
      // update alicuotas en tabla Parametros 
    private String updateAlics (String alic_iva, String alic_iibb_caba, String alic_iibb_ba, String alic_pf, String ameca_pf) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul;
        resul="UPDATE Parametros SET alicuota_iva="+alic_iva+", alicuota_iibb_caba="+alic_iibb_caba+", alicuota_iibb_provincia="+alic_iibb_ba+", alicuota_pago_facil="+alic_pf+", comision_pago_facil="+ameca_pf;

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
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
                assert con != null;
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
                assert con != null;
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
                assert con != null;
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
                assert con != null;
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
                assert con != null;
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
    
// update alicuota autonomo >30
    private String updateAlic_autonomo_30 (String alic_autonomo_30) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_30+" where id_categoria_autonomo=1"; 


        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota autonomo entre 15 y 30
    private String updateAlic_autonomo_1530 (String alic_autonomo_1530) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_1530+" where id_categoria_autonomo=2"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
// update alicuota autonomo < 15 
    private String updateAlic_autonomo_15 (String alic_autonomo_15) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_15+" where id_categoria_autonomo=3"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota autonomo activos no incluidas >20
    private String updateAlic_autonomo_20mas (String alic_autonomo_20mas) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_20mas+" where id_categoria_autonomo=4"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota autonomo activos no incluidas <=20
    private String updateAlic_autonomo_20menos (String alic_autonomo_20menos) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_20menos+" where id_categoria_autonomo=5"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota autonomo otras activs no incluidas >25
    private String updateAlic_autonomo_25mas (String alic_autonomo_25mas)
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_25mas+" where id_categoria_autonomo=6"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) {
               resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

// update alicuota autonomo otras activ no incluidas <=25
    private String updateAlic_autonomo_25menos (String alic_autonomo_25menos) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_autonomo_25menos+" where id_categoria_autonomo=7"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
     }


// update alicuota autonomo de afiliaciones
    private String updateAlic_afiliaciones (String alic_afiliaciones) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_afiliaciones+" where id_categoria_autonomo=8"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }

    private String updateAlic_menores (String alic_menores) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_menores+" where id_categoria_autonomo=9"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }


    private String updateAlic_jubilados (String alic_jubilados) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_jubilados+" where id_categoria_autonomo=10"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
                
    private String updateAlic_amas (String alic_amas) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
        String resul="UPDATE CategoriasAutonomo SET monto_mensual_autonomo="+alic_amas+" where id_categoria_autonomo=11"; 

        try 
            {
            con=CX.getCx_pool();
                assert con != null;
                pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update parametros";
            HTML.setIgnited_categorias_autonomo(Boolean.FALSE);
       
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
        finally 
            {
            try {
                if (pst != null)
                  pst.close();
                if (con != null) 
                  con.close();
                }
            catch (SQLException ex) {
                resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
                }
            }
        
        return resul;  
        }
        
        
    private String getHash (String pwd) 
    {
    try {	MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pwd.getBytes());

			byte byteData[] = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++)
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    pwd= sb.toString();
    }
    catch (NoSuchAlgorithmException ex) {    pwd= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
    
    return pwd;
    }
        
    private String check (String user_pwd, String user_nom)
        {
	    Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String resul="";
 
        String query="SELECT seguridad_individuo FROM Individuos WHERE apodo_individuo='"+user_nom+"' and  password_individuo='"+getHash(user_pwd)+"' "; 

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            query="0";
            if(rs.next())
                query= rs.getString(1);
            }
        catch (SQLException ex ) { resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";   }
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
            catch (SQLException ex) {  resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";  }
            }
            
		return query;
	
	}
    

    
}
