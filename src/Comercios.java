package ameca;
 

/**
 *
 * @author manu
 */

//import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
//import org.apache.http.client.utils.URIBuilder;




//import manu.utils.*;

public class Comercios extends HttpServlet
{  
    HTML htm=new HTML();
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException

   {  //    htmls.logger.fine("homeOsoc. Carga servlet\n--");
    if (!HTML.getIgnited_actividades())
         HTML.Carga_actividades();
    if (!HTML.getIgnited_zonas())
        HTML.Carga_zonas();
    if (!HTML.getIgnited_localidades())
        HTML.Carga_localidades();
    if (!HTML.getIgnited_condiciones_iva())
         HTML.Carga_condiciones_iva();
    if (!HTML.getIgnited_condiciones_iibb())
         HTML.Carga_condiciones_iibb();
//    if (!HTML.getIgnited_periodo())      periodo es el unico parametro que verifica estado de actualizacion en el metodo getPeriodo()
//         HTML.Carga_periodo();
    if (!HTML.getIgnited_categorias_autonomo())
         HTML.Carga_categorias_autonomo();
    if (!HTML.getIgnited_categorias_monotributo())
         HTML.Carga_categorias_monotributo();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : htm.getPeriodo_nostatic() ;
    String nro_cuit  = request.getParameter ("nro_cuit") != null ?  request.getParameter ("nro_cuit") : "--" ;
            if (nro_cuit.equals(""))
                nro_cuit="--";
    String nro_cuit_admin  = request.getParameter ("nro_cuit_admin") != null ?  request.getParameter ("nro_cuit_admin") : "" ;
            
    String razon_social  = request.getParameter ("razon_social") != null ?  request.getParameter ("razon_social") : "" ;
    String codigo_postal  = request.getParameter ("codigo_postal") != null ?  request.getParameter ("codigo_postal") : "" ;
    String nombre_responsable  = request.getParameter ("nombre_responsable") != null ?  request.getParameter ("nombre_responsable") : "" ;
    String observaciones = request.getParameter ("observaciones") != null ?  request.getParameter ("observaciones") : "" ;
    String id_zona  = request.getParameter ("id_zona") != null ?  request.getParameter ("id_zona") : "0" ;
    String id_localidad  = request.getParameter ("id_localidad") != null ?  request.getParameter ("id_localidad") : "" ;
    String nro_telefono  = request.getParameter ("nro_telefono") != null ?  request.getParameter ("nro_telefono") : "" ;
    String nro_telefono2  = request.getParameter ("nro_telefono2") != null ?  request.getParameter ("nro_telefono2") : "" ;
    String email  = request.getParameter ("email") != null ?  request.getParameter ("email") : "" ;
    String periodo_alta  = request.getParameter ("periodo_alta") != null ?  request.getParameter ("periodo_alta") : "" ;
    String periodo_baja  = request.getParameter ("periodo_baja") != null ?  request.getParameter ("periodo_baja") : "" ;

    String id_comercio  = request.getParameter ("id_comercio") != null ?  request.getParameter ("id_comercio") : "0" ;
    String domicilio_fiscal  = request.getParameter ("domicilio_fiscal") != null ?  request.getParameter ("domicilio_fiscal") : "" ;
    String cuit_calle  = request.getParameter ("cuit_calle") != null ?  request.getParameter ("cuit_calle") : "" ;
    String direccion_establecimiento = request.getParameter ("direccion_establecimiento") != null ?  request.getParameter ("direccion_establecimiento") : "" ;

    String condicion_iibb  = request.getParameter ("condicion_iibb") != null ?  request.getParameter ("condicion_iibb") : "" ;
    String condicion_iva  = request.getParameter ("condicion_iva") != null ?  request.getParameter ("condicion_iva") : "" ;
    String categ_monotributo  = request.getParameter ("categ_monotributo") != null ?  request.getParameter ("categ_monotributo") : "" ;
    String categ_autonomo  = request.getParameter ("categ_autonomo") != null ?  request.getParameter ("categ_autonomo") : "" ;


    String op2  = request.getParameter ("op2") != null ?  request.getParameter ("op2") : "" ;
    String imp_neto_g  = request.getParameter ("imp_neto_g") != null ?  request.getParameter ("imp_neto_g") : "" ;
    String imp_neto_ng  = request.getParameter ("imp_neto_ng") != null ?  request.getParameter ("imp_neto_ng") : "" ;
    String imp_op_ex  = request.getParameter ("imp_op_ex") != null ?  request.getParameter ("imp_op_ex") : "" ;
    String imp_iva  = request.getParameter ("imp_iva") != null ?  request.getParameter ("imp_iva") : "" ;
    String imp_tot  = request.getParameter ("imp_tot") != null ?  request.getParameter ("imp_tot") : "" ;

    
    if(operacion.equals("find"))
        {
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        out.println("<br>\n<h1>Buscar Comercio</h1>"+
                    "\n<form action='/ameca/comercios' name='busca'>\n\t" +
                    "\n\t<table><tr><td>Ingrese CUIT del Comercio: </td><td><input type='text' name='nro_cuit'></td><td rowspan='2' valign='middle' align='center' width='90px'> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.busca.submit();\" onmouseover=\"this.style.cursor='pointer'\">\n</td></tr>\n\t");
        out.println("\n<tr><td>Ingrese Calle del Establecimiento: </td><td><input type='text' name='direccion_establecimiento'></td></tr></table>");       
        out.println("<input type='hidden' name='operacion' value='find'> "+ 
                    "\n</form><br><br><br>");


        if (!cuit_calle.equals(""))
            out.println(this.TablaFindComercios(cuit_calle));
        else if(!nro_cuit.equals("--") || !direccion_establecimiento.equals(""))
             out.println(this.TablaFindComercios(nro_cuit, direccion_establecimiento));


        out.println("<table><tr><td height='377px'></td></tr></table>"); 
        out.println(HTML.getTail());

        }
    else if (operacion.equals("new"))
        {
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic())); //devuelve la mitad de la tabla con <tr>s hasta las catgegs del menu (inicio, comercios, liquidaciones, facturacion).
        if (id_comercio.equals("0"))
            out.println("<br><h2>Nuevo Comercio:</h2> <br>");
        else
            out.println("<br><h2>Editar Comercio:</h2> <br>");
        out.println("\n<form action='/ameca/comercios' name='comercio' method='post'><table cellSpacing='0' cellPadding='0'>\n\t"+
                    "<tr>\n\t\t<td>CUIT: </td><td><input type='text' name='nro_cuit' value='"+nro_cuit+"'></td>  <td>CUIT Admin.: </td><td><input type='text' name='nro_cuit_admin' value='"+nro_cuit_admin+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Razon Social: </td><td><input type='text' name='razon_social' value='"+razon_social+"'></td>  <td></td><td></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Nombre Responsable: </td><td><input type='text' name='nombre_responsable' value='"+nombre_responsable+"'></td>  <td></td><td></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Domicilio Fiscal: </td><td><input type='text' name='domicilio_fiscal' value='"+domicilio_fiscal+"'></td> <td>C&oacute;digo Postal: </td> <td><input type='text' name='codigo_postal' value='"+codigo_postal+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Localidad: </td><td>"+HTML.getDropLocalidades()+"</td>\t"+
                    "\t<td></td> <td></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Telefono: </td><td><input type='text' name='nro_telefono' value='"+nro_telefono+"'></td> <td>Telefono2: </td> <td><input type='text' name='nro_telefono2' value='"+nro_telefono2+"'></td> </tr>\n\t"+
                    "<tr>\n\t\t<td>Email: </td><td><input type='email' name='email' value='"+email+"'></td><td></td><td></td></tr>");
        if (id_comercio.equals("0"))
            out.println("<tr>\n\t\t<td>Fecha de Alta: </td><td><input type='text' name='periodo_alta' disabled></td> <td>Fecha de Baja</td> <td><input type='text' name='periodo_baja' disabled></td></tr>");
        else
            {out.println("<tr>\n\t\t<td>Fecha de Alta: </td><td><input type='text' name='periodo_alta' value='"+periodo_alta+"' size='8'></td> <td>Fecha de Baja</td> <td><input type='text' name='periodo_baja' value='"+periodo_baja+"' disabled size='8'>");
              if (periodo_baja.equals("-"))
                    out.println(" <a href='/ameca/comercios?operacion=ab&op2=baja&id_comercio="+id_comercio+"'> Dar de baja </a> </td></tr>");
              else
                    out.println(" <a href='/ameca/comercios?operacion=ab&op2=alta&id_comercio="+id_comercio+"'> Dar de alta </a> </td></tr>");
             out.println("\n <script> document.comercio.id_localidad.value='"+id_localidad+"'; \n </script>");
            }

        out.println("<tr>\n\t\t<td valign='top'>Observaciones: </td><td colspan='3'><textarea name='observaciones' cols='60' rows='6'>"+observaciones+"</textarea></td> </tr>");

        out.println("</table><input type='hidden' name='operacion' value='save'> <input type='hidden' name='id_comercio' value='"+id_comercio+"'> </form>\n\n");

        out.println("<table><tr><td height='30px'></td></tr></table>"); // aca va el contenido del cuerpo bajo, mas claro

        if (id_comercio.equals("0"))
            out.println("<table><tr><td><img src='/ameca/imgs/back.png'></td> ");
        else
            out.println("<table><tr><td><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a></td> ");

        out.println("<td width='320px'></td>"
                + "<td><img src=\"/ameca/imgs/ok_big.png\" onclick=\"document.comercio.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table> <br><br>");

        out.println(HTML.getTail());                   

        }
    else if (operacion.equals("save")) //guarda (insert o update) los datos recibidos del formulario del comercio ya completo
        {
        String res="";
        if (id_comercio == null || !id_comercio.matches("-?\\d+(\\.\\d+)?"))
            id_comercio="0";

        out.println("<html><head><title>Ameca - Save Data</title>\n </head> \n <body>  \n\n ");
        
/*        out.println("charset: "+Charset.defaultCharset().displayName()+"<br> ");
        out.println("locale: "+Locale.getDefault()+"<br> ");
        out.println("nombre1: "+nombre_responsable+"<br> ");
        out.println("nombre2 decode: "+URLDecoder.decode(nombre_responsable)+"<br> ");
        out.println("nombre3 decode, ISO-8859-1: "+URLDecoder.decode(nombre_responsable, "ISO-8859-1")+"<br> ");
        out.println("nombre4 decode, US-ASCII: "+URLDecoder.decode(nombre_responsable, "US-ASCII")+"<br> ");
        out.println("nombre5 decode, UTF-8: "+URLDecoder.decode(nombre_responsable, "UTF-8")+"<br> ");
        
*/
        if (id_comercio.equals("0"))
            res=this.insertaComercio(nro_cuit, razon_social, nombre_responsable, domicilio_fiscal, id_localidad, nro_telefono, email, codigo_postal, nro_telefono2, observaciones, nro_cuit_admin);
        else
            res=this.updateComercio(id_comercio, nro_cuit, razon_social, nombre_responsable, domicilio_fiscal, id_localidad, nro_telefono, email, periodo_alta, nro_cuit_admin, codigo_postal, nro_telefono2, observaciones);

        if (res != null && res.matches("-?\\d+(\\.\\d+)?"))
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+res+"\");} document.body.style.background='green'; \n window.setTimeout(go, 40); \n</script><br>");
       else
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 9000); \n</script><br><br>Query: "+res);

        out.println("</body></html>");

        }
    else if (operacion.equals("detalle"))
        {
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        out.println("<br><br><br>");


        out.println("<table width='1820px' bgcolor='#E8E7C1' cellspacing='0'>\n\t"
                + "<tr><th bgcolor='#ccc793' width='5px'></th> <th align='left' colspan='3' bgcolor='#ccc793' width='1810px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<table cellspacing='0'><tr><td><a href='/ameca/comercios?operacion=new'><img src='/ameca/imgs/cmrc_google48.png'></a></td><td width='10px'></td> <td style='height:64px; font-family:Arial; font-size:40px; font-weight: bold;'>Administrar Comercio</td></tr></table></th> <th bgcolor='#ccc793' width='5px'> </th></tr>"
                + "<tr><td colspan='5' height='15px'></td></tr>"
                + "<tr><td></td><td rowspan='2' valign='top' width='510px'>"+this.DatosComercioTable(id_comercio)+"</td> <td width='20px'></td> <td valign='top' align='left' width=1010'><table cellspacing='0'><tr><td style='height:56px; font-family:Arial; font-size:30px; font-weight: bold;'>Establecimientos</td> <td width='8px'></td><td><a href='/ameca/establecimientos?operacion=new&id_comercio=" + id_comercio + "&nro_cuit="+nro_cuit+"'> <img src='/ameca/imgs/sucu48.png'></a></td></tr></table></td><td></td>");

        out.println("<tr><td></td><td></td>"
             //   + "<td valign='top'>"+this.getPerfilImpositivo(id_comercio)+"</td><td></td>"
                + "<td valign='top'>"+this.DatosEstablecimientoTable(id_comercio, nro_cuit)+"</td><td></td>\n\t</tr>\t\n\n");


        out.println("<tr><td colspan='5' height='15px'></td></tr></table><br><br><br><br>");

        out.println(HTML.getTail());                    

        }
    else if (operacion.equals("perfil"))
        {
        String categ=categ_monotributo.equals("0")?categ_autonomo:categ_monotributo;
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        out.println("<br><h2>Editar Perfil Impositivo:</h2><br><br>"+
                 "\n\n<script>\n");
        out.println(htm.getScriptCategsIVA());
        out.println("\n\n</script>");

        out.println("<form name='perfil' action='/ameca/comercios'>"
                + "<table width='1100px' bgcolor='#E8E7C1' cellspacing='0' border='0'>\n\t"
                + "<tr> <td style='width:250px;position:sticky;'> CUIT: </td> <td style='width:900px;position:sticky;'> "+nro_cuit+"</td> </tr> "
                + "<tr><td>NOMBRE: </td> <td>"+nombre_responsable+"</td></tr> "
                + "<tr><td>DOMICILIO FISCAL: </td> <td>"+domicilio_fiscal+"</td></tr>"
                + "<tr><td height='15px' colspan='2'></td></tr> "
                + "<tr><td>Condicion IIBB: </td><td align='left'>"+HTML.getForm_iibb()+"</td> </tr>"
                + "<tr><td>Condicion IVA: </td><td align='left'>"+HTML.getForm_iva()+"</td> </tr>"
                + "<tr><td>Categoria IVA: </td><td align='left'><select name='categ'></select></td> </tr>");


        out.println( "<tr><td height='35px' colspan='2'><input type='hidden' name='id_comercio' value='"+id_comercio+"'> <input type='hidden' name='operacion' value='save_perfil'> <input type='hidden' name='categ_autonomo'> <input type='hidden' name='categ_monotributo'>  </td></tr> ");

        out.println("<tr><td colspan='2'> <table width='650px'> <tr> <td><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a></td> "
                + "<td width='160px'></td>"
                + "<td><img src=\"/ameca/imgs/ok_big.png\" onclick=\"if(document.perfil.condicion_iva.value==1) {document.perfil.categ_autonomo.value=0; document.perfil.categ_monotributo.value=document.perfil.categ.value; } else {document.perfil.categ_autonomo.value=document.perfil.categ.value; document.perfil.categ_monotributo.value=0; } document.perfil.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table> </td> </tr> </table><br><br>");
        
 //       out.println("<a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a> <br>");

        out.println("\n </form> \n <script>"
                + "document.perfil.condicion_iibb.value='"+condicion_iibb+"'; "
                + "document.perfil.condicion_iva.value='"+condicion_iva+"';"
                + "make("+condicion_iva+", "+categ+");"
                + "</script><br><br><br>");


        out.println(HTML.getTail());                    

        }
    else if (operacion.equals("save_perfil")) //guarda los datos recibidos del formulario ya completo
        {
        out.println("<html><head><title>Ameca - Save Data</title>\n\n</head>" +
                    "<body>  \n\n  "+
                    "\nValores recibidos del formulario: <br>"+
                    "\n<table cellSpacing='0' cellPadding='0'>\n\t"+
                    "<tr>\n\t\t<td>CUIT: "+nro_cuit+"</td></tr>"+
                    "<tr>\n\t\t<td>Condicion iibb: "+condicion_iibb+"</td></tr>"+
                    "\n\t<tr>\n\t\t<td>Condicion iva: "+condicion_iva+"</td></tr>"+
                    "\n\t<tr>\n\t\t<td>Categoria autonomo: "+categ_autonomo+"</td></tr>"+
                    "\n\t<tr>\n\t\t<td>Categoria monotributo: "+categ_monotributo+"</td></tr>"+
                    "</table>");

        String res=this.updateComercio_perfil (id_comercio, condicion_iibb, condicion_iva, categ_autonomo, categ_monotributo); 
        //out.println("<br><br>Insert: "+res);
        if (res != null && res.matches("-?\\d+(\\.\\d+)?"))
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='green'; window.setTimeout(go, 100); \n</script>\n</body>\n</html>");
        else
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='red'; window.setTimeout(go, 9000); \n</script><br><br>query: "+res+"\n\n</body>\n</html>");

        //out.println("<script>function go() {window.location.href=\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\";} \n window.setTimeout(go, 9000); \n</script>");

        //out.println("<a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"'>Volver</a> <br><br>");

        }
    else if (operacion.equals("compras")) // ultimo requerimiento
        {
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        out.println("<br><h2>Resumen de Compras:</h2><br><br>");
       
        if (op2.equals("insert"))
        {       //out.println("pre update: false, "+periodo+", "+id_comercio+", "+imp_neto_g+", "+imp_neto_ng+", "+imp_op_ex+", "+imp_iva+", "+imp_tot);
                out.println (this.updateResumenCompras (false, periodo, id_comercio, imp_neto_g, imp_neto_ng, imp_op_ex, imp_iva, imp_tot));
                //out.println("post update ");
        }
        if (op2.equals("update"))
               out.println (this.updateResumenCompras (true, periodo, id_comercio, imp_neto_g, imp_neto_ng, imp_op_ex, imp_iva, imp_tot));
        
        out.println(this.ResumenCompras(id_comercio, nro_cuit, nombre_responsable, periodo)); 
        
        out.println("<br><br><br><br><br><br><br><br>");

        out.println(HTML.getTail());            
        /*
        if (res != null && res.matches("-?\\d+(\\.\\d+)?"))
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='green'; window.setTimeout(go, 100); \n</script>\n</body>\n</html>");
        else
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='red'; window.setTimeout(go, 9000); \n</script><br><br>query: "+res+"\n\n</body>\n</html>");
*/

        }
    else if (operacion.equals("ab")) //guarda los datos recibidos del formulario ya completo
        {
        String res="";
        out.println("<html><head><title>Ameca - Save Data</title>\n\n</head>" +
                    "<body>  \n\n  "+
                    "\nValores recibidos del formulario: <br>"+
                    "\n<table cellSpacing='0' cellPadding='0'>\n\t"+
                    "<tr>\n\t\t<td>CUIT: "+nro_cuit+"</td></tr>"+
                    "\n\t<tr>\n\t\t<td>op2: "+op2+"</td></tr>"+
                    "</table>");

            res=this.abComercio (id_comercio, op2); 
        //out.println("<br><br>Insert: "+res);
        if (res != null && res.equals("1"))
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='green'; window.setTimeout(go, 40); \n</script>\n</body>\n</html>");
        else
                out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='red'; window.setTimeout(go, 9000); \n</script><br><br>query: "+res+"\n\n</body>\n</html>");


        }

        
                
  }



    // Recibe  el id_comercio y devuelve una tabla html con sus datos 

    private String DatosComercioTable(String id_comercio) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int id_localidad=0;
        String razon_social="", nombre_responsable="", domicilio_fiscal="", nro_telefono="", email="", nro_cuit="";
    String resul="", codigo_postal="", periodo_baja="", observaciones="", periodo_alta="", nro_telefono2="", nro_cuit_admin="";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT nro_cuit, razon_social, nombre_responsable, domicilio_fiscal, periodo_alta, id_localidad, nro_telefono, email, " +
                                                "codigo_postal, periodo_baja, observaciones_comercio, nro_telefono2, nro_cuit_admin "+  // 13
                                        " FROM Comercios"+
                                        " WHERE id_comercio="+id_comercio);

                                               
            rs = pst.executeQuery();
            if (rs.next())
                {
                 nro_cuit=rs.getString(1);
                 razon_social=rs.getString(2);
                 nombre_responsable=rs.getString(3);
                 domicilio_fiscal=rs.getString(4);
                 periodo_alta=rs.getString(5)!= null ?  rs.getString(5) : "" ;
                 id_localidad=rs.getInt(6);
                 nro_telefono=rs.getString(7);
                 email=rs.getString(8);
                 codigo_postal=rs.getString(9)!= null ?  rs.getString(9) : "" ;
                 periodo_baja=rs.getString(10)!= null ?  rs.getString(10) : "" ;
                 
                 observaciones=rs.getString(11);
                 nro_telefono2=rs.getString(12);
                 nro_cuit_admin=rs.getString(13);

                }  
            
            resul="<table><tr>\n\t\t<td>CUIT: <b>"+nro_cuit+"</b></td></tr>"+
                    "<tr>\n\t\t<td>CUIT Admin.: <b>"+nro_cuit_admin+"</b></td></tr>"+
                    "<tr>\n\t\t<td>Razon Social: <b>"+razon_social+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td>Nombre Responsable: <b>"+nombre_responsable+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td height='2px'></td></tr> "+
                    "\n\t<tr>\n\t\t<td>Domicilio Fiscal: <b>"+domicilio_fiscal+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td>"+this.getPerfilImpositivo(id_comercio)+"</td></tr>"+
                    "\n\t<tr>\n\t\t<td>Provincia: <b>"+htm.getProvincia_localidad(id_localidad)+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td>C&oacute;digo Postal: <b>"+codigo_postal+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td>Localidad: <b>"+htm.getLocalidad(id_localidad)+"</b></td></tr>"+
                    "\n\t<tr>\n\t\t<td>Telefono: <b>"+nro_telefono+"</b></td></tr>" +
                    "\n\t<tr>\n\t\t<td>Telefono2: <b>"+nro_telefono2+"</b></td></tr>" +
                    "\n\t<tr>\n\t\t<td>E-Mail: <b>"+email+"</b></td></tr>" +
                    "\n\t<tr>\n\t\t<td>Fecha de Alta: <b>"+periodo_alta+"</b></td></tr>" +
                    "\n\t<tr>\n\t\t<td>Fecha de Baja: <b>"+periodo_baja+"</b></td></tr>" +
                    "\n\t<tr>\n\t\t<td>Observaciones:</td></tr> "+
                    "\n\t<tr>\n\t\t<td height='2px'></td></tr> "+
                    "<tr><td><textarea name='observaciones' cols='60' rows='8'>"+observaciones+"</textarea></td></tr>" +
                    "\n\t<tr>\n\t\t<td align='center'><br><a href='/ameca/liquidaciones?operacion=ver_c&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_nostatic()+"'>Ver D.D.J.J s</a></td></tr>" +
                    "\n\t<tr>\n\t\t<td align='center'><br><a href='/ameca/comercios?operacion=compras&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&nombre_responsable="+URLEncoder.encode(nombre_responsable, "UTF-8")+"&periodo="+htm.getPeriodo_nostatic()+"'>Resumen Compras</a></td></tr>" +
                    "\n\t<tr>\n\t\t<td align='center'><br><a href='/ameca/comercios?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&razon_social="+URLEncoder.encode(razon_social)+"&nombre_responsable="+URLEncoder.encode(nombre_responsable)+
                                                            "&domicilio_fiscal="+URLEncoder.encode(domicilio_fiscal)+"&id_localidad="+id_localidad+"&codigo_postal="+codigo_postal+"&nro_telefono="+nro_telefono+"&nro_telefono2="+nro_telefono2+
                                                            "&email="+URLEncoder.encode(email)+"&observaciones="+URLEncoder.encode(observaciones)+"&periodo_alta="+periodo_alta+"&periodo_baja="+periodo_baja+"&nro_cuit_admin="+nro_cuit_admin+"'>Editar Comercio</a></td></tr>" +
                    "</table>";
            
            
            
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
        
        return resul;  //tabla con datos del comercio
        }

 
    

    // Recibe  el id_comercio y devuelve una tabla html con los datos de sus establecimientos 

    private String DatosEstablecimientoTable(String id_comercio, String nro_cuit) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

/*        String query="SELECT e.id_establecimiento, nombre_establecimiento, direccion_establecimiento, " +// 3
                                                                "id_zona, e.id_localidad, id_actividad, nro_telefono_establecimiento, email_establecimiento, "+  // 8
                                                                "nombre_responsable_establecimiento, nro_telefono2_establecimiento, nro_pago_facil, "+   // 11
                                                                "cod_postal_establecimiento, em.alicuota_pago_facil, em.saldo_iva, em.saldo_iibb, "+  // 15
                                                                "em.reporte_gan, em.reporte_afip_esp, em.reporte_suss, em.reporte_ameca_comision," +   // 19
                                                                "activo_iibb_periodo, activo_iva_periodo, c.id_condicion_iva, id_condicion_iibb " +  //23
                                                           " FROM Establecimientos e, EstablecimientosLiquiMes em, Comercios c "+
                                                           " WHERE e.id_establecimiento=em.id_establecimiento AND  e.id_comercio="+id_comercio+
                                                                " AND c.id_comercio=e.id_comercio AND periodo= '"+HTML.getPeriodo()+"'   ";
   */     String query="SELECT e.id_establecimiento, nombre_establecimiento, direccion_establecimiento, " +// 3
                                            "id_zona, e.id_localidad, id_actividad, nro_telefono_establecimiento, email_establecimiento, "+  // 8
                                            "nombre_responsable_establecimiento, nro_telefono2_establecimiento, nro_pago_facil, "+   // 11
                                            "cod_postal_establecimiento, em.alicuota_pago_facil, em.saldo_iva, em.saldo_iibb, "+  // 15
                                            "em.reporte_gan, em.reporte_afip_esp, em.reporte_suss, em.reporte_ameca_comision," +   // 19
                                            "activo_iibb_periodo, activo_iva_periodo, c.id_condicion_iva, id_condicion_iibb, periodo,  " +  //24
                                            "(SELECT COUNT(*) from Establecimientos ee where ee.id_comercio="+id_comercio+") as 'count', " + //25
                                            "em.base_imponible, em.compra_iva, em.percepcion_iva, percepcion_iibb, em.saldo_iva_reporte, em.saldo_iibb_reporte, "  + // 31
                                            "c.nombre_responsable, e.casa_matriz, c.nro_cuit " + // 34
                            " FROM Establecimientos e LEFT JOIN EstablecimientosLiquiMes em USING(id_establecimiento)  LEFT JOIN Comercios c USING (id_comercio) "+
                            " WHERE e.id_comercio="+id_comercio+
                            " ORDER BY periodo DESC";                                                                
    String resul="", nombre_establecimiento, direccion_establecimiento, tel1, tel2, email, periodo, 
                     responsable, nro_pago_facil, cod_postal, debug="", nombre_responsable="", casa_matriz;
        int id_establecimiento=0, id_zona, id_localidad, id_actividad, activo_iibb=0, activo_iva=0, condicion_iva=0, condicion_iibb=0;
        Double  alicpf=0d, saldoivaf=0d, saldoiibbf=0d, ganf=0d, afipf=0d, sussf=0d, comisionf=0d, subtotalf=0d, saldopf=0d, totalf=0d, 
                    bi=0d, compra_iva=0d, pcp_iva=0d, pcp_iibb=0d, saldoiva_reportef=0d, saldoiibb_reportef=0d;
        //Double bi_d=0d;
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            resul="\n<table cellpadding='2' cellspacing='2' border='0' width='100%'><tr>\n\n";
            int i=0, tot_establecs=0, vueltas=0;  // contador de establecimientos impresos y cant de establecimientos
            int [] establecs=new int[9];
            Boolean salgo=false, salteo=false;   // cuando obtengo todos los establecimientos salgo del query

            while (rs.next() && !salgo)
                {  vueltas++;
                        // preguntar si periodo is null, y mostrar solo los id_establecimientos distintos una vez   , query me dice la cant de establecimientos del comercio (salir del while cuando encuentro los tres)
                        // while (rs.next() && i<count )
                 tot_establecs=rs.getInt(25);
                 id_establecimiento=rs.getInt(1);
                 periodo  = rs.getString(24) != null ?  rs.getString(24) : "0" ;


                 salteo=false;
                 for (int z=0;z<i;z++)
                     if (establecs[z]==id_establecimiento)
                         {salteo=true;
                           z=i;
                         //debug+="salteo<br>";
                         }  


                 
                 if(!salteo)
                        {
                        // debug+="entro a imprimir... vuelta: "+Integer.toString(vueltas)+", i="+Integer.toString(i)+"<br>";
                        establecs[i++]=id_establecimiento;
                        nombre_establecimiento=rs.getString(2);
                        direccion_establecimiento=rs.getString(3);
                        id_zona=rs.getInt(4);
                        id_localidad=rs.getInt(5);
                        id_actividad=rs.getInt(6);
                        tel1=rs.getString(7);
                        tel2=rs.getString(10);
                        email=rs.getString(8);
                        responsable=rs.getString(9);
                        nombre_responsable=rs.getString(32);
                        casa_matriz=rs.getString(33);
                        nro_cuit=rs.getString(34);
                        nro_pago_facil=rs.getString(11);
                        cod_postal=rs.getString(12);

                       if (periodo.equals(htm.getPeriodo_nostatic()))
                          {
                            alicpf=rs.getDouble(13);
                            saldoivaf=rs.getDouble(14);
                            saldoiibbf=rs.getDouble(15);
                            saldoiva_reportef=rs.getDouble(30);
                            saldoiibb_reportef=rs.getDouble(31);
                            ganf=rs.getDouble(16);
                            afipf=rs.getDouble(17);
                            sussf=rs.getDouble(18);
                            comisionf=rs.getDouble(19);
                            bi=rs.getDouble(26);
                          
                            compra_iva=rs.getDouble(27);
                            pcp_iva=rs.getDouble(28);
                            pcp_iibb=rs.getDouble(29);

                            subtotalf=saldoivaf + saldoiibbf + ganf + afipf + sussf;   // total vep
                            saldopf=subtotalf * alicpf/100;
                            totalf=subtotalf + saldopf + comisionf;   // total pago facil  -- faltaria sumar autonomo / monotributo si hay tilde

                             activo_iibb=rs.getInt(20);
                             activo_iva=rs.getInt(21);
                             condicion_iva=rs.getInt(22);
                             condicion_iibb=rs.getInt(23);
                          }
                       else
                          {
                            alicpf=0d; 
                            saldoivaf=0d; 
                            saldoiibbf=0d; 
                            ganf=0d; 
                            afipf=0d; 
                            sussf=0d; 
                            comisionf=0d;

                            subtotalf=0d;
                            saldopf=0d;
                            totalf=0d;
                            bi=0d;
                            compra_iva=0d;
                            pcp_iva=0d;
                            pcp_iibb=0d;

                             activo_iibb=0;
                             activo_iva=0;
                             condicion_iva=0;
                             condicion_iibb=0;
                          }
                           

            // nuevo saldo del reporte            saldoivaf=rs.getFloat(3);
            //  nuevo campo          saldoiibbf=rs.getFloat(4);


                        resul+="<td valign='top'>\n"+
                               "\n <table border='1' cellpadding='2' cellspacing='0'> "+
                               "\n <tr> <td width='380px' height='25'> <table width='100%'><tr><td> Direcci&oacute;n: "+direccion_establecimiento+"  </td><td valign='right'><a href='/ameca/establecimientos?operacion=new&id_establecimiento=" + id_establecimiento + "&id_comercio=" + id_comercio + "&nombre_establecimiento=" + URLEncoder.encode(nombre_establecimiento) + "&direccion_establecimiento=" + URLEncoder.encode(direccion_establecimiento)
                                + "&codigo_postal=" + cod_postal + "&id_zona=" + id_zona + "&id_localidad=" + id_localidad + "&id_actividad=" + id_actividad + "&email_establecimiento=" + URLEncoder.encode(email)
                                + "&nro_telefono_establecimiento=" + tel1 + "&nro_telefono2_establecimiento=" + tel2 + "&nro_pago_facil=" + nro_pago_facil + "&casa_matriz="+casa_matriz+"'><img src=\"/ameca/imgs/edit32.png\" alt='search' align='bottom'></a>&nbsp; </td> </tr></table> </td> </tr>"+
                           //    "\n\t<tr> <td height='1px'> </td></tr> \n"+
                               "\n\t<tr> <td> \n";
                        if (casa_matriz.equals("1"))
                                resul+="<p style='color:black; background-color:lightblue; margin-top: 1px; font-family: Arial, Helvetica, sans-serif;'>&nbsp;&nbsp;&nbsp;Casa Matriz</p>\n\t";
                        else
                                resul+="<p style='color:black; background-color:lightgrey; margin-top: 1px; font-family: Arial, Helvetica, sans-serif;'>&nbsp;&nbsp;&nbsp;Sucursal</p>\n\t";
                            
                            
                        resul+="\t\t&nbsp;&nbsp;&nbsp;Nombre: "+nombre_establecimiento+"<br>\n\t"+
                               "\n\t&nbsp;&nbsp;&nbsp;C&oacute;digo Postal: "+cod_postal+"<br>\n\t"+
                               "\t\t&nbsp;&nbsp;&nbsp;Actividad: "+htm.getActividad(id_actividad)+"<br>\n\t"+
                               "\t\t&nbsp;&nbsp;&nbsp;Provincia: "+htm.getProvincia_localidad(id_localidad)+"<br>\n\t"+
                               "\t\t&nbsp;&nbsp;&nbsp;Localidad: "+htm.getLocalidad(id_localidad)+"<br>\n\t" +
                               "\t\t&nbsp;&nbsp;&nbsp;Zona: "+htm.getZona(id_zona)+"<br>\n\t" +
                               "\n\t&nbsp;&nbsp;&nbsp;Nombre Contacto: "+responsable+"<br>\n\t"+
                               "\t\t&nbsp;&nbsp;&nbsp;Tel&eacute;fono: "+tel1+"<br>\n\t" +
                               "\t\t&nbsp;&nbsp;&nbsp;Tel&eacute;fono2: "+tel2+"<br>\n\t" +
                               "\t\t&nbsp;&nbsp;&nbsp;E-mail: "+email+"\n\t <br>"+
                               "\t\t&nbsp;&nbsp;&nbsp;N&uacute;mero Pago F&aacute;cil: "+nro_pago_facil+"\n\t "+
                               "\t\t<table cellspacing='0'><tr><td>&nbsp;&nbsp;&nbsp;Observaciones:</td><td width='20'></td><td> <a href=\"#\" onClick=\"MyWindow=window.open('/ameca/establecimientos?operacion=notes&nombre_responsable_establecimiento="+URLEncoder.encode(nombre_responsable)+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento)+"&nro_cuit="+nro_cuit+"&id_establecimiento="+id_establecimiento+"','MyWindow','width=600,height=300'); return false;\"><img src=\"/ameca/imgs/notepad.png\" style=\"width:32px;height:32px;\" onmouseover=\"this.style.cursor='pointer'\">\n</a></td></tr></table>  <br>\n\t <br>";

                        if (activo_iva>0 )   // && condicion_iva!=1 solo sacar el href que permite incluirlo incluye
                                {resul+="<img src='/ameca/imgs/green_yes.png' style='width:21px;height:21px;'>&nbsp;&nbsp;<b>IVA</b>: ACTIVO. <a href='/ameca/establecimientos?operacion=activar&op_activar=iva&valor_activar=0&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'> Excluir <img src='/ameca/imgs/red_no.png' style='width:11px;height:11px;'></a>"+
                                            "\n <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Base: $"+String.format(Locale.GERMAN, "%,.2f", bi)+
                                            "\n <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Compra: $"+String.format(Locale.GERMAN, "%,.2f", compra_iva)+
                                            "\n <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Percepcion: $"+String.format(Locale.GERMAN, "%,.2f", pcp_iva)+
                                           "\n\t <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Saldo IVA: $ "+String.format(Locale.GERMAN, "%,.2f", saldoivaf)+"<br>\n\t";
                                }
                        else
                                {
                                 resul+="<img src='/ameca/imgs/red_no.png' style='width:21px;height:21px;'>IVA: INACTIVO. ";
                                 if (HTML.getActivo_iva(condicion_iva))
                                     resul+="<a href='/ameca/establecimientos?operacion=activar&op_activar=iva&valor_activar=1&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'> Incluir <img src='/ameca/imgs/green_yes.png' style='width:11px;height:11px;'></a>";
                                }

                        if (activo_iibb>0)
                                {resul+="<br><img src='/ameca/imgs/green_yes.png' style='width:21px;height:21px;'>&nbsp;&nbsp;<b>IIBB</b>: ACTIVO. <a href='/ameca/establecimientos?operacion=activar&op_activar=iibb&valor_activar=0&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'> Excluir <img src='/ameca/imgs/red_no.png' style='width:11px;height:11px;'></a>";
                                  if (activo_iva==0 )
                                            resul+="\n <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Base: $"+String.format(Locale.GERMAN, "%,.2f", bi);
                                  resul+="\n <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Percepcion: $"+String.format(Locale.GERMAN, "%,.2f", pcp_iibb)+
                                           "\n\t <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Saldo IIBB: $ "+String.format(Locale.GERMAN, "%,.2f", saldoiibbf)+"<br>\n\t";

                                }
                        else
                           {
                               resul+="<br><img src='/ameca/imgs/red_no.png' style='width:21px;height:21px;'>&nbsp;&nbsp;IIBB: INACTIVO. ";
                               if (HTML.getActivo_iibb(condicion_iibb))
                                   resul+="<a href='/ameca/establecimientos?operacion=activar&op_activar=iibb&valor_activar=1&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'> Incluir <img src='/ameca/imgs/green_yes.png' style='width:11px;height:11px;'></a>";
                           }

                      resul+="\n <br><br> <b>Reporte:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='/ameca/reportes?operacion=edit&id_establecimiento=" + id_establecimiento + "&periodo=" + periodo + "&direccion_establecimiento=" + URLEncoder.encode(direccion_establecimiento) + "&nro_cuit="+nro_cuit+"'>Editar</a><br> "+
                                   "\n<table>";
                      
                      if (saldoiva_reportef>0f &&  Math.round(saldoiva_reportef - saldoivaf) !=0)
                          resul+="\n\t<tr>\n\t\t<td>Saldo IVA: $ "+String.format(Locale.GERMAN, "%,.2f",saldoiva_reportef)+"<br>\n\t";
                      if (saldoiibb_reportef>0f &&  Math.round(saldoiibb_reportef - saldoiibbf )!=0)
                          resul+="\n\t<tr>\n\t\t<td>Saldo IIBB: $ "+String.format(Locale.GERMAN, "%,.2f",saldoiibb_reportef)+"<br>\n\t";
                      
                      resul+="\t\tGAN.: $ "+String.format(Locale.GERMAN, "%,.2f",ganf)+"<br>\n\t"+
                                   "\t\tAFIP esp.: $ "+String.format(Locale.GERMAN, "%,.2f",afipf)+"<br>\n\t"+
                                   "\t\tSUSS.: $ "+String.format(Locale.GERMAN, "%,.2f",sussf)+"<br>\n\t"+
                                   "\t\tTotal VEP:  $ "+String.format(Locale.GERMAN, "%,.2f",subtotalf)+"<br>\n\t"+
                                   "\t\tSaldo Pago Facil.: $ "+String.format(Locale.GERMAN, "%,.2f",saldopf)+"<br>\n\t"+
                                   "\t\tTotal Pago Facil: $ "+String.format(Locale.GERMAN, "%,.2f",totalf)+"<br>\n\t"+
                                   "\n\t</td></tr><tr><td height=10px></td></tr>"+
                                   "</table>"+
                                   "</td> </tr> "+
                          //       "\n\t<tr> <td height='1px'> </td></tr> \n"+
                                 "<tr> <td> "+
                                 "<table border='0'><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n</td>"+
                                 "<td><a href='/ameca/establecimientos?operacion=liqui&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&periodo="+htm.getPeriodo_nostatic()+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento)+"'>Editar Base</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\n"+
                                 "<td><a href='/ameca/liquidaciones?operacion=ver_e&id_establecimiento="+id_establecimiento+"&id_comercio="+id_comercio+"&direccion_establecimiento="+URLEncoder.encode(direccion_establecimiento)+"&periodo="+htm.getPeriodo_nostatic()+"&nro_cuit="+nro_cuit+"'>Ver DDJJs</a> </td></tr>"+
                                 "</table>\n";
                        resul+="\n\t</td></tr></table>\n\n";
                        resul+="\n\t</td><td width='10px'></td>";

                        } // fin del if del salteo
                 if(i==tot_establecs)
                    {salgo=true;
                      debug+="<br>salgo:"+Boolean.toString(salgo)+"<br>";
                    }
                } // fin while

            
//    resul+="\n\t<tr>\n\t\t<td align='center'><br><a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"&activo_iibb="+Integer.toString(activo_iibb)+"&activo_iva="+Integer.toString(activo_iva)+"'>Agregar Nuevo Establecimiento</a></td>\n\t</tr>";
  
//            resul+="\n</tr><tr><td>debuging="+debug+"</td></tr></table>";
            if (vueltas>2)  // agrego columna si  hubo menos de tres establecimientos
                    resul+="\n</tr></table>";
            else
                    resul+="\n\n\t</td><td width='200px'> <br></td></tr></table>";
            
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
        
        return resul;  //tabla con establecimientos del comercio activo
        }

    


        private String getPerfilImpositivo(String id_comercio) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

    String resul="";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT id_condicion_iibb, id_condicion_iva, id_categ_monotributo, id_categ_autonomo, nro_cuit, nombre_responsable, domicilio_fiscal "+
                                        " FROM Comercios "+
                                        " WHERE id_comercio="+id_comercio);

            rs = pst.executeQuery();
            if (rs.next())
                resul="\n<table border='1' cellpadding='3' cellspacing='0' width='400px'>"+
                        "\n\t<tr>\n\t\t<td><table><tr><td width='300px' align='center'><b> Perfil Impositivo</b></td>    "
                    + "<td align='right' width='100px'><a href='/ameca/comercios?operacion=perfil&id_comercio=" + id_comercio + "&nro_cuit=" + rs.getString(5) + "&condicion_iibb=" + rs.getString(1) + "&condicion_iva=" + rs.getString(2) + "&categ_monotributo=" + rs.getString(3) + "&categ_autonomo=" + rs.getString(4) + "&nombre_responsable=" + URLEncoder.encode(rs.getString(6)) + "&domicilio_fiscal=" + URLEncoder.encode(rs.getString(7) )+ "'>  "
                    + "<img src=\"/ameca/imgs/edit32.png\" alt='search' align='bottom'></a>&nbsp; </td> </tr></table> "
                    + "</td></tr> \n\t"
                     +   "\n\t<tr>\n\t\t<td>Condicion I.I.B.B.: <b>"+HTML.getCondicionIIBB(rs.getInt(1))+"</b><br>\n\t"+
                        "\t\tCondicion I.V.A.: <b>"+HTML.getCondicionIVA(rs.getInt(2))+"</b><br>\n\t"+
                        "<br>\t\t"+HTML.getCategoriaMonotributo(rs.getInt(3))+"\n\t"+
                        "\t\t"+HTML.getCategoriaAutonomo(rs.getInt(4))+"<br>\n\t"+
                        "\n\t</td></tr></table><br>\n";
            else
                resul="\n<table border='1' cellpadding='0' cellspacing='0'>"+
                        "\n\t<tr>\n\t\t<td><table width='400px'><tr><td width='300px'><b> Perfil Impositivo</b></td>    "
                    + "<td align='right' width='100px'><a href='/ameca/comercios?operacion=perfil&id_comercio=" + id_comercio+ "'>  "
                    + "<img src=\"/ameca/imgs/edit32.png\" alt='search' align='bottom'></a>&nbsp; </td> </tr></table> "
                    + "</td></tr> \n\t"
                    +   "\n\t<tr>\n\t\t<td>Condicion I.I.B.B.: <br>\n\t"+
                        "\t\tCondicion I.V.A.: <br>\n\t"+
                        "<br>\n\t"+
                        "\n\t</td></tr></table><br>\n";
                
              //  resul+="\n\t<tr>\n\t\t<td align='center'><br><a href='/ameca/comercios?operacion=perfil&id_comercio="+id_comercio+"&nro_cuit="+rs.getString(5)+"&condicion_iibb="+rs.getString(1)+"&condicion_iva="+rs.getString(2)+"&categ_monotributo="+rs.getString(3)+"&categ_autonomo="+rs.getString(4)+"&nombre_responsable="+rs.getString(6)+"&domicilio_fiscal="+rs.getString(7)+"'>Editar</a></td>\n\t</tr>";
  
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
        
        return resul;  //tabla con establecimientos del comercio activo
        }


        
        
        private String getReportes_table(String id_comercio) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Float ganf=0f, afipf=0f, sussf=0f, saldoivaf=0f, saldoiibbf=0f, alicpf=0f, comisionf=0f, subtotalf=0f, saldopf=0f, totalf=0f;
        String resul="";
    
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT em.alicuota_pago_facil, em.saldo_iva, em.saldo_iibb, em.reporte_gan, em.reporte_afip_esp, "+ // 5
                                                                "em.reporte_suss, em.reporte_ameca_comision, e.direccion_establecimiento, e.id_establecimiento  "+ // 9
                                                            " FROM EstablecimientosLiquiMes em, Establecimientos e "+
                                                            " WHERE e.id_establecimiento=em.id_establecimiento AND  e.id_comercio="+id_comercio+
                                                                    " AND periodo='"+htm.getPeriodo_nostatic()+"' ");

            rs = pst.executeQuery();

            resul="\n<table cellpadding='0' cellspacing='0'> \n\n";
            while (rs.next())
            {
                alicpf=rs.getFloat(1);
                saldoivaf=rs.getFloat(2);
                saldoiibbf=rs.getFloat(3);
                ganf=rs.getFloat(4);
                afipf=rs.getFloat(5);
                sussf=rs.getFloat(6);
                comisionf=rs.getFloat(7);
                
//                subtotal=htm.getSubtotalReporte_calculo (saldo_iva, saldo_iibb, gan, afip, suss);
                subtotalf=saldoivaf + saldoiibbf + ganf + afipf + sussf;
                saldopf=subtotalf * alicpf/100;
                totalf=subtotalf + subtotalf + comisionf;
                 resul+="<tr><td>\n<table border='1' cellpadding='0' cellspacing='0'>"+
                        "\n\t<tr>\n\t\t<td>Dir..: <b>"+rs.getString(8)+"</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='/ameca/reporte?operacion=perfil&id_establecimiento="+rs.getString(9)+"'>Editar</a></td> </tr>"+
                        "\n\t<tr>\n\t\t<td>Alicuota PF.: <b>"+alicpf+"%</b><br>\n\t"+
                        "\n\t\n\t\tSaldo IIBB. (puede ser diferente que saldo): <b>"+String.format(Locale.GERMAN, "%,.2f",saldoiibbf)+"</b>$<br>\n\t"+
                        "\n\t\n\t\tSaldo IVA.: <b>"+String.format(Locale.GERMAN, "%,.2f",saldoivaf)+"</b>$<br>\n\t"+
                        "\t\tGAN.: <b>"+String.format(Locale.GERMAN, "%,.2f",ganf)+"</b>$<br>\n\t"+
                        "\t\tAFIP esp.: <b>"+String.format(Locale.GERMAN, "%,.2f",afipf)+"</b>$<br>\n\t"+
                        "\t\tSUSS.: <b>"+String.format(Locale.GERMAN, "%,.2f",sussf)+"</b>$<br>\n\t"+
                        "\t\tComision Ameca.: <b>"+String.format(Locale.GERMAN, "%,.2f",comisionf)+"</b>$<br><br>\n\t"+
                        "\t\tSubtotal.:  <b>"+String.format(Locale.GERMAN, "%,.2f",subtotalf)+"</b>$<br>\n\t"+
                        "\t\tSaldo Pago Facil.: <b>"+String.format(Locale.GERMAN, "%,.2f",saldopf)+"</b>$<br>\n\t"+
                        "\t\t<b>TOTAL.: "+String.format(Locale.GERMAN, "%,.2f",totalf)+"</b>$<br>\n\t"+
                        "\n\t</td></tr></table><br>\n\n "+
                        "\n\t</td></tr>"+
                        "\n\t<tr>\n\t\t<td align='center'><br></td>\n\t</tr>";
            }
            resul+="\n</table>";
            }
        catch (Exception ex) {
               resul+= "<br><br>ERROR gral: "+ex.getMessage()+"<br><br>";
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
                resul+= ex.getMessage();
                }
            }
        
        return resul;  //tabla con establecimientos del comercio activo
        }
        
        
        
        

     // update del perfil impositivo sobre tabla Comercios y devuelve los registros modificados (1 si todo ok) 

    private String updateComercio_perfil (String id_comercio, String condicion_iibb, String condicion_iva, String categ_autonomo, String categ_monotributo) 
    {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int resul_insert;
 
    String resul="UPDATE Comercios set id_condicion_iibb="+condicion_iibb+", id_condicion_iva="+condicion_iva+", "
                                    +       "id_categ_monotributo="+categ_monotributo+", id_categ_autonomo="+categ_autonomo+" "
                                    + " WHERE id_comercio="+id_comercio;
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
                resul=Integer.toString(resul_insert);
            if(resul_insert<1)
                resul="problema en update comercio";

            pst = con.prepareStatement("Select count(*) from Establecimientos WHERE id_comercio="+id_comercio);
            rs=pst.executeQuery();
            if (rs.next())
                resul_insert=rs.getInt(1);
            
            if(resul_insert>0)
                {
                /*  esto queda afuera porque activo_iva y activo_iibb es manual, igual no va a aparecer en el listado si condicion iva es monotributo... */           
                if (condicion_iva.equals("1"))
                     pst = con.prepareStatement("UPDATE Establecimientos e, EstablecimientosLiquiMes em SET em.activo_iva_periodo=0 WHERE em.id_establecimiento=e.id_establecimiento AND em.periodo='"+htm.getPeriodo_nostatic()+"' AND e.id_comercio="+id_comercio);
                else if (condicion_iva.equals("2"))
                     pst = con.prepareStatement("UPDATE Establecimientos e, EstablecimientosLiquiMes em SET em.activo_iva_periodo=1 WHERE em.id_establecimiento=e.id_establecimiento AND em.periodo='"+htm.getPeriodo_nostatic()+"' AND e.id_comercio="+id_comercio);

                resul_insert = pst.executeUpdate();
                if(resul_insert<1)
                    resul+="problema en update activo_iva";
                else
                    resul+=Integer.toString(resul_insert);

                if (condicion_iibb.equals("2") || condicion_iibb.equals("4") || condicion_iibb.equals("5") )
                     pst = con.prepareStatement("UPDATE Establecimientos e, EstablecimientosLiquiMes em SET em.activo_iibb_periodo=0 WHERE em.id_establecimiento=e.id_establecimiento AND em.periodo='"+htm.getPeriodo_nostatic()+"' AND e.id_comercio="+id_comercio);
                else if (condicion_iva.equals("2"))
                     pst = con.prepareStatement("UPDATE Establecimientos e, EstablecimientosLiquiMes em SET em.activo_iibb_periodo=1 WHERE em.id_establecimiento=e.id_establecimiento AND em.periodo='"+htm.getPeriodo_nostatic()+"' AND e.id_comercio="+id_comercio);

                resul_insert = pst.executeUpdate();
                if(resul_insert<1)
                    resul+="problema en update activo_iibb";
                else
                    resul+=Integer.toString(resul_insert);
                }
            }
        catch (SQLException ex) {
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
        
        return resul;  //id_comercio del nuevo registro
        }
        


    
    
    
     // inserta nuevo comercio en tabla Comercios y devuelve el id_comercio 

    private String insertaComercio(String nro_cuit, String razon_social, String nombre_responsable, String domicilio_fiscal, String id_localidad,   
                                     String nro_telefono, String email, String codigo_postal, String nro_telefono2, String observaciones, String nro_cuit_admin) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        long resul_insert=0;
 
    String resul="INSERT INTO Comercios (nro_cuit, razon_social, nombre_responsable, domicilio_fiscal, id_localidad, nro_telefono, email, "
                                    + " periodo_alta, periodo_baja, codigo_postal, nro_telefono2, observaciones_comercio, nro_cuit_admin) "
                                    + " VALUES ('"+nro_cuit+"', '"+razon_social+"', '"+nombre_responsable+"', '"+domicilio_fiscal+"', "+
                                                  id_localidad+", '"+nro_telefono+"', '"+email+"', DATE_FORMAT(CURRENT_DATE, '%Y-%m'), '-', '" +
                                                  codigo_postal+"', '"+nro_telefono2+"', '"+observaciones+"', '"+nro_cuit_admin+"')";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
                                               
            resul_insert = pst.executeUpdate();
            if (resul_insert>0)
                pst = con.prepareStatement("select last_insert_id()");
            else 
                return "ERROR: "+resul;
            rs=pst.executeQuery();
            if (rs.next())
                resul=rs.getString(1);

            }
        catch (SQLException ex) {
               return "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                return ex.getMessage();
                }
            }
        
        return resul;  //id_comercio del nuevo registro
        }


    
     // update comercio en tabla Comercios y devuelve el id_comercio o cero si hubo error en update.

    private String updateComercio(String id_comercio, String nro_cuit, String razon_social, String nombre_responsable, String domicilio_fiscal,   
                                    String id_localidad, String nro_telefono, String email, String periodo_alta, String nro_cuit_admin, String codigo_postal, 
                                    String nro_telefono2, String observaciones) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        long resul_insert;
 
        String resul="UPDATE Comercios "+
                     "SET nro_cuit='"+nro_cuit+"', razon_social='"+razon_social+"', nombre_responsable='"+nombre_responsable+"', "+
                         "domicilio_fiscal='"+domicilio_fiscal+"', id_localidad="+id_localidad+", nro_telefono='"+nro_telefono+"', "+
                         "email='"+email+"', nro_cuit_admin='"+nro_cuit_admin+"', "+
                         "codigo_postal='"+codigo_postal+"', nro_telefono2='"+nro_telefono2+"', observaciones_comercio='"+observaciones+"' "+
                    "WHERE id_comercio="+id_comercio;
try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
                                               
            resul_insert = pst.executeUpdate();
            if (resul_insert>0)
                resul=id_comercio;

            }
        catch (SQLException ex) {
               resul="<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                resul=ex.getMessage();
                }
            }
        
        return resul;  //id_comercio editado,  0 si no hizo el update o el mensaje de error
        }
    
    
    
    
     
     // recibe CUIT, entero o parte, y devuelve tabla con loc comercios que cumplen con link para editarlos
    
   private String TablaFindComercios (String cuit_calle)   // cuando busco desde la barra de tareas entra por aca
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
    String query, resul="\n\n<table class='bicolor' align='center'><tr><th>CUIT</th><th>Razon Social</th><th>Responsable</th><th>Domicilio Fiscal (Comercio)</th><th>Direcci&oacute;n Establecimiento</th><th></th></tr>\n";

//        if (StringUtils.isStrictlyNumeric(cuit_calle.substring(0,2)))
        if (cuit_calle != null && cuit_calle.substring(0,2).matches("-?\\d+(\\.\\d+)?"))
            query="SELECT c.id_comercio, razon_social, nombre_responsable, c.nro_cuit, domicilio_fiscal, '--' "+
                   "FROM Comercios c "+
                   "WHERE c.nro_cuit like '"+cuit_calle+"%' "+
                   "ORDER BY c.nro_cuit";
        else
            query= "SELECT c.id_comercio, razon_social, nombre_responsable, c.nro_cuit, domicilio_fiscal, direccion_establecimiento "+
                   "FROM Comercios c, Establecimientos e "+
                   "WHERE c.id_comercio=e.id_comercio AND (domicilio_fiscal like '%"+cuit_calle+"%' OR direccion_establecimiento like '%"+cuit_calle+"%') "+
                   "ORDER BY c.nro_cuit" ;
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next())
        resul+="<tr><td>"+rs.getString(4)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3) +"</td><td>"+rs.getString(5) +"</td><td>"+rs.getString(6) +"</td><td><a href=\"/ameca/comercios?operacion=detalle&nro_cuit="+rs.getString(4)+"&id_comercio="+rs.getString(1)+"\">Ver Comercio</a></tr>\n";

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

   
   
     // recibe CUIT y calle (de comercio o establecimiento), entero o parte, y devuelve tabla con loc comercios que cumplen con link para editarlos
    
   private String TablaFindComercios (String nro_cuit, String calle_comercio) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
    String  query="", resul="\n\n<table class='bicolor' align='center'><tr><th>CUIT</th><th>Razon Social</th><th>Responsable</th><th>Domicilio Fiscal (Comercio)</th><th>Direcci&oacute;n Establecimiento</th><th></th></tr>\n";
        if (nro_cuit.equals("--") && calle_comercio.equals(""))
                return "";
        else if (!nro_cuit.equals("--") && calle_comercio.equals(""))
                query= "SELECT id_comercio, razon_social, nombre_responsable, nro_cuit, domicilio_fiscal, '--' "+
                                       "FROM Comercios  WHERE nro_cuit like '"+nro_cuit+"%' ";
        else if (nro_cuit.equals("--") && !calle_comercio.equals(""))
                query= "SELECT c.id_comercio, razon_social, nombre_responsable, c.nro_cuit, domicilio_fiscal, direccion_establecimiento "+
                                       "FROM Comercios c, Establecimientos e WHERE c.id_comercio=e.id_comercio AND (domicilio_fiscal like '%"+calle_comercio+"%' OR direccion_establecimiento like '%"+calle_comercio+"%')" ;
        else if (!nro_cuit.equals("--") && !calle_comercio.equals(""))
                query= "SELECT c.id_comercio, razon_social, nombre_responsable, c.nro_cuit, domicilio_fiscal, direccion_establecimiento' "+
                                       "FROM Comercios c, Establecimientos e WHERE c.id_comercio=e.id_comercio  AND c.nro_cuit like '"+nro_cuit+"%' AND (direccion_establecimiento like '%"+calle_comercio+"%' OR domicilio_fiscal like '%"+calle_comercio+"%')";
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next())
        resul+="<tr><td>"+rs.getString(4)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3) +"</td><td>"+rs.getString(5) +"</td><td>"+rs.getString(6) +"</td><td><a href=\"/ameca/comercios?operacion=detalle&nro_cuit="+rs.getString(4)+"&id_comercio="+rs.getString(1)+"\">Ver Comercio</a></tr>\n";

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
 

 
   
     // id_comercio, nro_cuit, nombre_responsable, periodo); 
    
   private String ResumenCompras (String id_comercio, String nro_cuit, String nombre_responsable, String periodo)   // cuando busco desde la barra de tareas entra por aca
    {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
    String query, imp_neto_g="", imp_neto_ng="", imp_op_ex="", imp_iva="", imp_tot="", resul="";

            query="SELECT imp_neto_gravado, imp_neto_no_gravado, imp_op_exentas, iva, imp_total "+
                   "FROM ComerciosCompras "+
                   "WHERE id_comercio = "+id_comercio+" AND periodo='"+periodo+"' ";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            
            if (rs.next())
                {// formulario
                    
                    resul= "\n<form action='/ameca/comercios' name='f_compras'> "+
                    "\n\n<table cellSpacing='0' cellPadding='0'>\n\t<tr><td colspan='2' align='center'>"+
                    "<table>"+
                    "<tr>\n<td> <a href='/ameca/comercios?operacion=compras&id_comercio=" + id_comercio + "&nombre_responsable=" + URLEncoder.encode(nombre_responsable) + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                             "<td> <input type='text' name='periodo' value='"+periodo+"' size='7'> </td>"+
                             "\n<td> <a href='/ameca/comercios?operacion=compras&id_comercio=" + id_comercio + "&nombre_responsable=" + URLEncoder.encode(nombre_responsable) + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>\n"+
                            "</table></td></tr>\n\n"+
                             "<br>" +
                             "<input type='hidden' name='operacion' value='compras'> "+
                             "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>"+
                             "<input type='hidden' name='op2' value='update'>"+
                    "\n\n"+
                    "<tr>\n\t\t<td>Impuesto Neto Gravado: </td><td><input type='text' name='imp_neto_g' value='"+rs.getFloat(1)+"'></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Neto No Gravado: </td><td><input type='text' name='imp_neto_ng' value='"+rs.getFloat(2)+"'></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Op. Exentas: </td><td><input type='text' name='imp_op_ex' value='"+rs.getFloat(3)+"'></td></tr>"+
                    "<tr>\n\t\t<td>IVA: </td><td><input type='text' name='imp_iva' value='"+rs.getFloat(4)+"'></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Total: </td><td><input type='text' name='imp_tot' value='"+rs.getFloat(5)+"'></td></tr>"+
                    "<tr><td colspan=2 align='center'> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.f_compras.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td></tr>" +
                    "</table>" +
                    "</form>";

               
                }  
            else  // formulario vacio
                {// formulario
                    
                    resul= "\n<form action='/ameca/comercios' name='f_compras'> "+
                    "\n\n<table cellSpacing='0' cellPadding='0'>\n\t<tr><td colspan='2' align='center'>"+
                    "<table>"+
                    "<tr>\n<td> <a href='/ameca/comercios?operacion=compras&id_comercio=" + id_comercio + "&nombre_responsable=" + URLEncoder.encode(nombre_responsable) + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"+
                             "<td> <input type='text' name='periodo' value='"+periodo+"' size='7'> </td>"+
                             "\n<td> <a href='/ameca/comercios?operacion=compras&id_comercio=" + id_comercio + "&nombre_responsable=" + URLEncoder.encode(nombre_responsable) + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>\n"+
                            "</table></td></tr>\n\n"+
                             "<br>" +
                             "<input type='hidden' name='operacion' value='compras'> "+
                             "<input type='hidden' name='id_comercio' value='" + id_comercio + "'>"+
                             "<input type='hidden' name='op2' value='insert'>"+
                    ""+
                    "<tr>\n\t\t<td>Impuesto Neto Gravado: </td><td><input type='text' name='imp_neto_g' ></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Neto No Gravado: </td><td><input type='text' name='imp_neto_ng' ></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Op. Exentas: </td><td><input type='text' name='imp_op_ex' ></td></tr>"+
                    "<tr>\n\t\t<td>IVA: </td><td><input type='text' name='imp_iva' ></td></tr>"+
                    "<tr>\n\t\t<td>Impuesto Total: </td><td><input type='text' name='imp_tot' ></td></tr>"+
                    "<tr><td colspan=2 align='center'> <img src=\"/ameca/imgs/ok.png\" style=\"width:48px;height:48px;\" onclick=\"document.f_compras.submit();\" onmouseover=\"this.style.cursor='pointer'\"> </td></tr>" +
                    "</table>" +
                    "</form>";
               
                }  

            
            query="SELECT periodo, imp_neto_gravado, imp_neto_no_gravado, imp_op_exentas, iva, imp_total "+
                   "FROM ComerciosCompras "+
                   "WHERE id_comercio = "+id_comercio+
                   " ORDER BY periodo DESC"+
                   " LIMIT 10";
            resul+="\n\n<br><br><br><br><br><br><table class='bicolor' align='center'>"
                    + "<tr><th>PERIODO</th><th>IMPUESTO NETO GRAVADO</th><th>IMPUESTO NETO NO GRAVADO</th><th>IMPUESTO OP. EXENTAS</th><th>IVA</th><th>IMPUESTO TOTAL</th>"
                    + "</tr>\n";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            
            while (rs.next())
                resul+="<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3) +"</td><td>"+rs.getString(4) +"</td><td>"+rs.getString(5) +"</td><td>"+rs.getString(6)+"</td></tr>\n";

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
   
   

     // update del perfil impositivo sobre tabla Comercios y devuelve los registros modificados (1 si todo ok) 
    private String updateResumenCompras (Boolean op2, String periodo, String id_comercio, String imp_neto_gravado, String imp_neto_no_gravado, String imp_op_exentas, String imp_iva, String imp_total) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
    String resul="";
    if (op2)
          resul="UPDATE ComerciosCompras SET imp_neto_gravado="+imp_neto_gravado+", imp_neto_no_gravado="+imp_neto_no_gravado+", "
                                    +       "imp_op_exentas="+imp_op_exentas+", iva="+imp_iva+", imp_total= "+imp_total
                                    + " WHERE id_comercio="+id_comercio +" AND periodo='"+periodo+"' ";
    else
          resul="INSERT INTO ComerciosCompras ( imp_neto_gravado, imp_neto_no_gravado, imp_op_exentas, iva, imp_total, id_comercio, periodo)"
                  + " VALUES ( "+imp_neto_gravado+", "+imp_neto_no_gravado+", "+imp_op_exentas+", "+imp_iva+", "+imp_total+ ", "+id_comercio+", '"+periodo+"' )";

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            //resul=Integer.toString(resul_insert);
            resul="";
            if(resul_insert<1)
                resul="problema en update comercio";


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
        


     // update periodo_baja en tabla Comercios y devuelve 1 si operacion ok o 'hubo problemas' si no 
    private String abComercio (String id_comercio, String op2) 
    {
        Connection con = null;
        PreparedStatement pst = null;
        int resul_insert;
 
    String resul="";
    if (op2.equals("alta"))
          resul="UPDATE Comercios SET periodo_baja='-' "+
                   " WHERE id_comercio="+id_comercio;
    else
          resul="UPDATE Comercios SET periodo_baja=DATE_FORMAT(CURRENT_DATE, '%Y-%m')"+
                   " WHERE id_comercio="+id_comercio;

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);
            resul_insert = pst.executeUpdate();
            resul="1";
            if(resul_insert<1)
                resul="problema en update comercio";


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
