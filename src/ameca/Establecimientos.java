package ameca;
 
/**
 *
 * @author manu
 */

import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import manu.utils.*;

public class Establecimientos extends HttpServlet
{  
    HTML htm=new HTML();
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   { doGet(request, response); }    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
   {  
    //htmls.logger.fine("homeOsoc. Carga servlet\n--");
   
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    if (!HTML.getIgnited_zonas())
        HTML.Carga_zonas();
    if (!HTML.getIgnited_localidades())
        HTML.Carga_localidades();
//    if (!HTML.getIgnited_periodo())
//         HTML.Carga_periodo();
    if (!HTML.getIgnited_categorias_autonomo())
         HTML.Carga_categorias_autonomo();
    if (!HTML.getIgnited_categorias_monotributo())
         HTML.Carga_categorias_monotributo();
        
    String operacion  = request.getParameter ("operacion") != null ?  request.getParameter ("operacion") : "nuevo" ;
    String op2  = request.getParameter ("op2") != null ?  request.getParameter ("op2") : "edit" ;

    String nro_cuit  = request.getParameter ("nro_cuit") != null ?  request.getParameter ("nro_cuit") : "--" ;
    String nro_pago_facil  = request.getParameter ("nro_pago_facil") != null ?  request.getParameter ("nro_pago_facil") : "" ;
    String nombre_establecimiento  = request.getParameter ("nombre_establecimiento") != null ?  request.getParameter ("nombre_establecimiento") : "" ;
    String nombre_responsable_establecimiento  = request.getParameter ("nombre_responsable_establecimiento") != null ?  request.getParameter ("nombre_responsable_establecimiento") : "" ;
    String direccion_establecimiento  = request.getParameter ("direccion_establecimiento") != null ?  request.getParameter ("direccion_establecimiento") : "" ;
    String codigo_postal  = request.getParameter ("codigo_postal") != null ?  request.getParameter ("codigo_postal") : "" ;
    String casa_matriz  = request.getParameter ("casa_matriz") != null ?  request.getParameter ("casa_matriz") : "1" ;
    String id_zona  = request.getParameter ("id_zona") != null ?  request.getParameter ("id_zona") : "0" ;
    String id_actividad  = request.getParameter ("id_actividad") != null ?  request.getParameter ("id_actividad") : "" ;
    String id_localidad  = request.getParameter ("id_localidad") != null ?  request.getParameter ("id_localidad") : "0" ;
    String nro_telefono_establecimiento  = request.getParameter ("nro_telefono_establecimiento") != null ?  request.getParameter ("nro_telefono_establecimiento") : "" ;
    String nro_telefono2_establecimiento  = request.getParameter ("nro_telefono2_establecimiento") != null ?  request.getParameter ("nro_telefono2_establecimiento") : "" ;
    String email_establecimiento  = request.getParameter ("email_establecimiento") != null ?  request.getParameter ("email_establecimiento") : "" ;
    String observaciones  = request.getParameter ("observaciones") != null ?  request.getParameter ("observaciones") : "" ;

    String id_comercio  = request.getParameter ("id_comercio") != null ?  request.getParameter ("id_comercio") : "0" ;
    String id_establecimiento  = request.getParameter ("id_establecimiento") != null ?  request.getParameter ("id_establecimiento") : "0" ;
    String id_establecimiento_mes  = request.getParameter ("id_establecimiento_mes") != null ?  request.getParameter ("id_establecimiento_mes") : "0" ;

    String periodo  = request.getParameter ("periodo") != null ?  request.getParameter ("periodo") : htm.getPeriodo_nostatic() ;
    String base_imponible  = request.getParameter ("base_imponible") != null ?  request.getParameter ("base_imponible") : "0" ;
    String alicuota_iva  = request.getParameter ("alicuota_iva") != null ?  request.getParameter ("alicuota_iva") : "21" ;
    String alicuota_iibb  = request.getParameter ("alicuota_iibb") != null ?  request.getParameter ("alicuota_iibb") : "3" ;
    String compra_iva  = request.getParameter ("compra_iva") != null ?  request.getParameter ("compra_iva") : "0" ;
    String percepcion_iva  = request.getParameter ("percepcion_iva") != null ?  request.getParameter ("percepcion_iva") : "0" ;
    String percepcion_iibb  = request.getParameter ("percepcion_iibb") != null ?  request.getParameter ("percepcion_iibb") : "0" ;
    String activo_iva  = request.getParameter ("activo_iva") != null ?  request.getParameter ("activo_iva") : "0" ;
    String activo_iibb  = request.getParameter ("activo_iibb") != null ?  request.getParameter ("activo_iibb") : "0" ;
    String condicion_iva  = request.getParameter ("condicion_iva") != null ?  request.getParameter ("condicion_iva") : "0" ;
    String condicion_iibb  = request.getParameter ("condicion_iibb") != null ?  request.getParameter ("condicion_iibb") : "0" ;

    String op_activar  = request.getParameter ("op_activar") != null ?  request.getParameter ("op_activar") : "" ; // recibe iva o iibb
    String valor_activar  = request.getParameter ("valor_activar") != null ?  request.getParameter ("valor_activar") : "" ; // 0 o 1
    
    String saldo_iibb  = request.getParameter ("saldo_iibb") != null ?  request.getParameter ("saldo_iibb") : "0" ;
    String saldo_iva  = request.getParameter ("saldo_iva") != null ?  request.getParameter ("saldo_iva") : "0" ;
    
    
    if (!StringUtils.isStrictlyNumeric(id_establecimiento))   // caute que a los float los rechaza por tener un punto
        id_establecimiento="0";
    if (!StringUtils.isStrictlyNumeric(id_establecimiento_mes))
        id_establecimiento_mes="0";




    if (operacion.equals("new"))
        {
        if (!HTML.getIgnited_actividades())
            HTML.Carga_actividades();

        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        if (id_establecimiento.equals("0"))
            out.println("\n<br><h2>Nuevo Establecimiento</h2><br>");
        else
            out.println("\n<br><h2>Editar Establecimiento</h2><br>");

        out.println("\n<form action='/ameca/establecimientos' name='establecimiento' method='post'><table cellSpacing='0' cellPadding='0'>\n\t"+
                    "<table>" +
                    "<tr>\n\t\t<td>Nombre Establecimiento: </td><td><input type='text' name='nombre_establecimiento' value='"+nombre_establecimiento+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Direccion: </td><td><input type='text' name='direccion_establecimiento' value='"+direccion_establecimiento+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Codigo Postal: </td><td><input type='text' name='codigo_postal' value='"+codigo_postal+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Tipo de Establecimiento: </td><td><select name='casa_matriz'> <option value='1'>Casa Matriz</option><option value='0'>Sucursal</option></select> </td></tr>\n\t"+
                    "<tr>\n\t\t<td>Actividad: </td><td>"+HTML.getDropActividades()+"</td></tr>\n\t"+
                    "<tr>\n\t\t<td>Localidad: </td><td>"+HTML.getDropLocalidades()+"</td></tr>\n\t"+
                    "<tr>\n\t\t<td>Zona: </td><td>"+HTML.getDropZonas()+"</td></tr>\n\t"+
                    "<tr>\n\t\t<td>Numero Pago Facil: </td><td><input type='text' name='nro_pago_facil' value='"+nro_pago_facil+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Nombre de Contacto: </td><td><input type='text' name='nombre_responsable_establecimiento' value='"+nombre_responsable_establecimiento+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Telefono: </td><td><input type='text' name='nro_telefono_establecimiento' value='"+nro_telefono_establecimiento+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Telefono2: </td><td><input type='text' name='nro_telefono2_establecimiento' value='"+nro_telefono2_establecimiento+"'></td></tr>\n\t"+
                    "<tr>\n\t\t<td>Email: </td><td><input type='email' name='email_establecimiento' value='"+email_establecimiento+"'></td></tr>");

        if (!id_establecimiento.equals("0"))
                out.println("\n <script> document.establecimiento.id_actividad.value='"+id_actividad+"'; \n  document.establecimiento.id_localidad.value='"+id_localidad+"'; \n  document.establecimiento.id_zona.value='"+id_zona+"'; \n document.establecimiento.casa_matriz.value='"+casa_matriz+"'; \n </script>");
        
        out.println("</table><input type='hidden' name='operacion' value='save'>\n<input type='hidden' name='id_comercio' value='"+id_comercio+"'> <input type='hidden' name='nro_cuit' value='"+nro_cuit+"'> "
                +   "<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'><input type='hidden' name='activo_iibb' value='"+activo_iibb+"'> <input type='hidden' name='activo_iva' value='"+activo_iva+"'>"
                +   "\n</form>\n\n");
        out.println("<br><br>");

        //out.println("<a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a> <br>");
        out.println("<table> <tr> <td><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a></td> "
                + "<td width='200px'></td>"
                + "<td><img src=\"/ameca/imgs/ok_big.png\" onclick=\"document.establecimiento.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td></tr></table><br><br><br><br><br><br>");

        out.println(HTML.getTail());

        }
    else if (operacion.equals("save")) //guarda los datos recibidos del formulario ya completo.
        {

        out.println("<html><head><title>Ameca - Save Data</title>\n </head> \n <body>  \n\n ");

        if (id_establecimiento.equals("0"))
              id_establecimiento=this.insertaEstablecimiento (id_comercio, nombre_establecimiento, direccion_establecimiento, id_actividad, id_zona, id_localidad,  
                                                                nro_telefono_establecimiento, email_establecimiento, nro_telefono2_establecimiento, nro_pago_facil, 
                                                                codigo_postal, nombre_responsable_establecimiento, casa_matriz);
        else
              id_establecimiento=this.updateEstablecimiento(id_establecimiento, nombre_establecimiento, direccion_establecimiento, id_actividad, id_zona, id_localidad,  
                                                                nro_telefono_establecimiento, email_establecimiento, nro_telefono2_establecimiento, nro_pago_facil, 
                                                                codigo_postal, nombre_responsable_establecimiento, casa_matriz);
              

        //out.println("INSERT: "+id_establecimiento+"<br><br>"); //hopefully id_establecimiento
       if (StringUtils.isStrictlyNumeric(id_establecimiento))
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script><br>");
       else
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 9000); \n</script><br><br>Query: "+id_establecimiento);




        //out.println("<a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Ver Comercio y Establecimientos</a> <br><br>");
        //out.println("<a href='/ameca/establecimientos?operacion=new&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'>Nuevo Establecimiento</a>\n\n");
        out.println("</body></html>");                    
        }
    else if(operacion.equals("liqui"))   // carga tributos de un establecimiento (editar mostly)
        {
        out.println(HTML.getHead("comercios", htm.getPeriodo_nostatic()));
        out.println("\n<br><h2>Datos para el c&aacute;lculo impositivo</h2> <br> <br>"
                + "CUIT: "+nro_cuit+" &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; Direcci&oacute;n: "+direccion_establecimiento+"<br><br>\n"
                + "<table>"
                + "<tr>\n<td> <a href='/ameca/establecimientos?operacion=liqui&id_comercio=" + id_comercio + "&id_establecimiento=" + id_establecimiento + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_pre(periodo) + "'><img src='/ameca/imgs/back_go.png'></a></td>"
                + "\n<td> <form action='/ameca/establecimientos' name='form_periodo'> "
                + "<input type='text' name='periodo' value='"+periodo+"' size='7'>"
                + "<input type='hidden' name='operacion' value='liqui'>\n<input type='hidden' name='id_comercio' value='"+id_comercio+"'>\n"
                + "<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'> \n<input type='hidden' name='nro_cuit' value='"+nro_cuit+"'> \n"
                + "\n</form></td>"
                + "\n<td><a href='/ameca/establecimientos?operacion=liqui&id_comercio=" + id_comercio + "&id_establecimiento=" + id_establecimiento + "&nro_cuit=" + nro_cuit + "&periodo=" + htm.getPeriodo_prox(periodo) + "'><img src='/ameca/imgs/next_go.png'></a></td></tr>"
                + "</table>"+
                    "<form action='/ameca/establecimientos' name='form_tributo'>\n"+
                    "\n<table cellSpacing='0' cellPadding='0' border='1'>\n<tr>\n<td>"+
                    "<input type='hidden' name='id_comercio' value='"+id_comercio+"'>"+
                    "\n<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'> \n\n"+
                    "\n<input type='hidden' name='direccion_establecimiento' value='"+direccion_establecimiento+"'> \n\n"+
                    "\n<input type='hidden' name='nro_cuit' value='"+nro_cuit+"'> \n\n"+
                    //"\n<input type='hidden' name='periodo' value='"+periodo+"'> \n\n"+                
                    "<input type='hidden' name='operacion' value='liqui_save'>\n");

        out.println(this.getTable_carga_tributo(id_establecimiento, periodo, id_comercio));

        out.println("\n\n</form><br><br>");


        out.println("<table> <tr> <td><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"'><img src='/ameca/imgs/back.png'></a></td> "
                + "<td width='250px'></td>"
                + "<td><img src=\"/ameca/imgs/ok_big.png\" onclick=\"document.form_tributo.debito_iva.value=document.form_tributo.base_imponible.value*document.form_tributo.alicuota_iva.value/100; \n"
                +                           "document.form_tributo.credito_iva.value=parseFloat(document.form_tributo.compra_iva.value)*parseFloat(document.form_tributo.alicuota_iva.value)/100; \n"
                +                           "document.form_tributo.saldo_iva.value=parseFloat(document.form_tributo.debito_iva.value)-parseFloat(document.form_tributo.credito_iva.value)-parseFloat(document.form_tributo.percepcion_iva.value); \n"
                +                           "document.form_tributo.debito_iibb.value=document.form_tributo.base_imponible.value*document.form_tributo.alicuota_iibb.value/100; \n"
                +                           "document.form_tributo.saldo_iibb.value=parseFloat(document.form_tributo.debito_iibb.value)-parseFloat(document.form_tributo.percepcion_iibb.value); \n"
                +                           "document.form_tributo.submit();\" onmouseover=\"this.style.cursor='pointer'\"></td>"
                + "</tr></table>"
                + "<br><br><br><br><br><br>");

        out.println(HTML.getTail());

        }
    else if(operacion.equals("liqui_save"))
        {
        out.println("<html><head><title>Ameca - Guarda Actividad Impositiva</title>\n\n</head> \n <body>  \n\n  ");
        String res;
        if (id_establecimiento_mes.equals("0"))
            res=this.insertaLiquiMes(id_establecimiento, periodo, base_imponible, alicuota_iva, alicuota_iibb, compra_iva, percepcion_iva, percepcion_iibb, saldo_iibb, saldo_iva, condicion_iva, condicion_iibb);
        else
            res=this.updateLiquiMes(id_establecimiento_mes, base_imponible, alicuota_iva, alicuota_iibb, compra_iva, percepcion_iva, percepcion_iibb);
            
 
       if (StringUtils.isStrictlyNumeric(res))
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script><br>");
       else
               out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 9000); \n</script><br><br>Query: "+res);
        
        
        out.println("<br><a href='/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"e&nro_cuit="+nro_cuit+"'>Volver</a>");
        out.println("</body>\n\n</html>");

        }
                
        else if (operacion.equals("activar")) //activa o desactiva al establecimiento para aparecer en listado IVA / IIBB
            {
            out.println("<html><head><title>Ameca - Save Data</title>\n\n</head> \n <body>");

            String res=this.updateEstablecimiento_activar (id_establecimiento, op_activar, valor_activar);
            
            if (StringUtils.isStrictlyNumeric(res))
                    out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} document.body.style.background='green'; \n window.setTimeout(go, 70); \n</script><br>");
            else
                    out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 9500); \n</script><br><br>Query: "+res);
            }   
        else if (operacion.equals("notes")) //guarda los datos recibidos del formulario ya completo
            {
                String resul_insert="";
                long i=0;
                if (op2.equals("save"))
                        {resul_insert=this.UpdateObservaciones (id_establecimiento, observaciones);
                          if (resul_insert.equals("1"))
                               out.println("<!DOCTYPE html>\n<html><head><title>Notas</title>\n</head> \n <body><script>function go() {window.close();} document.body.style.background='green'; \n window.setTimeout(go, 100); \n</script>\n OK<br></body></html>");
                          else
                               out.println("<!DOCTYPE html>\n<html><head><title>Notas</title>\n</head> \n <body><script>function go() {window.close();} document.body.style.background='red'; \n window.setTimeout(go, 10000); \n</script> <br>ERROR al guardar: "+resul_insert+"</body></html>");
                              
                              //out.println("<html><head><title>Notas - "+nro_cuit+"</title>\n </head> \n <body>ERROR al guardar: "+resul_insert+"</body></html>");
                              //out.println("<html><head><title>Notas - "+nro_cuit+"</title>\n </head> \n <body>OK CERRAR</body></html>");
                              
                        }
                else
                        {
                       observaciones=CargaObservaciones (id_establecimiento);

                        out.println("<html><head><title>Notas - "+nro_cuit+"</title>\n </head> \n <body>  \n\n "+
                                 " Observaciones del Establecimiento: "+nombre_responsable_establecimiento+", "+direccion_establecimiento+"<br><br>"+
                                 "<form name='f_observaciones' action='/ameca/establecimientos' method='post'> "
                                + "<input type='hidden' name='id_establecimiento' value='"+id_establecimiento+"'>"+
                                   "<input type='hidden' name='operacion' value='notes'>"+
                                   "<input type='hidden' name='op2' value='save'>"+
                                 "<textarea name='observaciones' cols='70' rows='8'>"+observaciones+"</textarea><br>"
                                + "<input type='submit'>\n"
                                + "</form><br><br></body></html>");
                        }



/*               if (StringUtils.isStrictlyNumeric(id_establecimiento))
                       out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} document.body.style.background='green'; \n window.setTimeout(go, 80); \n</script><br>");
               else
                       out.println("<script>function go() {window.location.replace(\"/ameca/comercios?operacion=detalle&id_comercio="+id_comercio+"&nro_cuit="+nro_cuit+"\");} \n document.body.style.background='red'; \n window.setTimeout(go, 9000); \n</script><br><br>Query: "+id_establecimiento);

  */                         
        }

   
 }



   
     // inserta nuevo establecimiento en tabla Establecimientos y devuelve el id_establecimiento 

    private String insertaEstablecimiento (String id_comercio, String nombre_establecimiento, String direccion_establecimiento, 
                                              String id_actividad, String id_zona, String id_localidad, String nro_telefono_establecimiento, 
                                              String email_establecimiento, String nro_telefono2_establecimiento, String nro_pago_facil, 
                                              String codigo_postal, String responsable, String casa_matriz) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        long resul_insert;
        
        String query="INSERT INTO Establecimientos (id_comercio, nombre_establecimiento, direccion_establecimiento, id_actividad, "
                                    + "id_zona, id_localidad, nro_telefono_establecimiento, email_establecimiento, nro_telefono2_establecimiento, "
                                    + "nro_pago_facil, cod_postal_establecimiento, nombre_responsable_establecimiento, casa_matriz) "
                                    + " VALUES ("+id_comercio+", '"+nombre_establecimiento+"', '"+direccion_establecimiento+"', "+id_actividad+", "+
                                                id_zona+", "+id_localidad+", '"+nro_telefono_establecimiento+"', '"+email_establecimiento+"', '"+
                                                nro_telefono2_establecimiento+"', '"+nro_pago_facil+"', '"+codigo_postal+"', '"+responsable+"', "+casa_matriz+")";
	String resul="";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
                                               
            resul_insert = pst.executeUpdate();
            if (resul_insert>0)
                pst = con.prepareStatement("select last_insert_id()");
            else 
                return "todo mal";
            rs=pst.executeQuery();
            if (rs.next())
		resul=rs.getString(1);

            }
        catch (SQLException ex) {
               return "<br><br>ERROR: "+ex.getMessage()+"<br><br>"+query;
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



    
     // update de establecimiento y devuelve cantidad de registros modificados (1) hopefully 

    private String updateEstablecimiento (String id_establecimiento, String nombre_establecimiento, String direccion_establecimiento, 
                                              String id_actividad, String id_zona, String id_localidad, String nro_telefono_establecimiento, String email_establecimiento,  
                                              String nro_telefono2_establecimiento, String nro_pago_facil, String codigo_postal, String responsable, String casa_matriz) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        long resul_insert;
        
        String query="UPDATE Establecimientos "
                +    "SET nombre_establecimiento='"+nombre_establecimiento+"', direccion_establecimiento='"+direccion_establecimiento+"', "
                       + "id_actividad="+id_actividad+", id_zona="+id_zona+", id_localidad="+id_localidad+", nro_telefono_establecimiento='"+nro_telefono_establecimiento+"', "
                       + "email_establecimiento='"+email_establecimiento+"', nro_telefono2_establecimiento='"+nro_telefono2_establecimiento+"', "
                       + "nro_pago_facil='"+nro_pago_facil+"', cod_postal_establecimiento='"+codigo_postal+"', nombre_responsable_establecimiento='"+responsable+"', "
                       + "casa_matriz="+casa_matriz
                +    " WHERE id_establecimiento="+id_establecimiento;

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
                                               
            resul_insert = pst.executeUpdate();
            if (resul_insert<1)
                query+= "todo mal";
            else
                query=Long.toString(resul_insert);
            }
        catch (SQLException ex) {
               query+= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                query+= ex.getMessage();
                }
            }
        
        return query;  //cantidad de registros modificados (1) hopefully
        }
    
    
    
    

     // inserta nuevo liquidacion mensual para un establecimiento (puede ser snapshot o final) y devuelve 1 o 0 si fue ok o no. 

    private String insertaLiquiMes(String id_establecimiento, String periodo, String base_imponible, String alicuota_iva, String alicuota_iibb, 
                                     String compra_iva, String percepcion_iva, String percepcion_iibb, String saldo_iibb, String saldo_iva, String condicion_iva, String condicion_iibb) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        //ResultSet rs = null;
        long resul_insert;
	String resul="";

        try 
            {
            con=CX.getCx_pool();
        
            if (HTML.getActivo_iva(condicion_iva))
                condicion_iva="1";  // activo_iva=1
            else
                condicion_iva="0";

            if (HTML.getActivo_iibb(condicion_iibb))
                condicion_iibb="1";  // activo_iibb=1
            else
                condicion_iibb="0";  // activo_iibb=1

            
            pst = con.prepareStatement("INSERT INTO EstablecimientosLiquiMes (id_establecimiento, periodo, base_imponible, alicuota_iva, "
                                     + "alicuota_iibb, compra_iva, percepcion_iva, percepcion_iibb, saldo_iibb, saldo_iva, alicuota_pago_facil, "
                                     + "reporte_ameca_comision, activo_iva_periodo, activo_iibb_periodo) "
                                    + " VALUES ("+id_establecimiento+", '"+periodo+"', "+base_imponible+", "+alicuota_iva+", "
                                                +alicuota_iibb+", "+compra_iva+", "+percepcion_iva+", "+percepcion_iibb+", "+saldo_iibb+", "+saldo_iva+", "+HTML.getAlicuotaPF()+", "+
                                                HTML.getComisionPF()+", "+condicion_iva+", "+condicion_iibb+")");
                                               
            resul_insert = pst.executeUpdate();
            resul = Long.toString(resul_insert);

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
                resul= ex.getMessage();
                }
            }
        
        return resul;  //cantidad de registros insertados o msg error
        }


    
     // update liquidacion mensual para un establecimiento y devuelve 1 o 0 si fue ok o no. 

    private String updateLiquiMes (String id_establecimiento_mes, String base_imponible, String alicuota_iva, String alicuota_iibb, 
                                                        String compra_iva, String percepcion_iva, String percepcion_iibb) 
	{
        Connection con = null;
        PreparedStatement pst = null;

        long resul_insert;
        String resul="UPDATE EstablecimientosLiquiMes "+
                    " SET base_imponible="+base_imponible+", alicuota_iva="+alicuota_iva+", alicuota_iibb="+alicuota_iibb+", compra_iva="+
                          compra_iva+", percepcion_iva="+percepcion_iva+", percepcion_iibb="+percepcion_iibb+", saldo_iibb="+htm.getSaldoIIBB_calculo (base_imponible, alicuota_iibb, percepcion_iibb)+
                         ", saldo_iva="+htm.getSaldoIVA_calculo (base_imponible, alicuota_iva, compra_iva, percepcion_iva)+
                    " WHERE id_establecimiento_mes="+id_establecimiento_mes;

        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(resul);

                                               
            resul_insert = pst.executeUpdate();
            resul = Long.toString(resul_insert);

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
                resul= ex.getMessage();
                }
            }
        
        return resul;  //cantidad de registros insertados o msg error
        }
   
    
    
    
     // update de establecimiento y devuelve cantidad de registros modificados (1) hopefully 

    private String updateEstablecimiento_activar (String id_establecimiento, String op_activar, String valor_activar) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        long resul_insert;
        String query;
        
        if (op_activar.equals("iva"))
            query="UPDATE EstablecimientosLiquiMes " +
                  " SET activo_iva_periodo="+valor_activar+
                  " WHERE periodo='"+htm.getPeriodo_nostatic()+"' AND id_establecimiento="+id_establecimiento;
        else
            query="UPDATE EstablecimientosLiquiMes " +
                  " SET activo_iibb_periodo="+valor_activar+
                  " WHERE periodo='"+htm.getPeriodo_nostatic()+"' AND id_establecimiento="+id_establecimiento;
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
                                               
            resul_insert = pst.executeUpdate();
            if (resul_insert<1)
                query+= "todo mal";
            else
                query=Long.toString(resul_insert);
            }
        catch (SQLException ex) {
               query+= "<br><br>ERROR: "+ex.getMessage()+"<br>"+query+"<br>";
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
                query+= "<br><br>ERROR: "+ex.getMessage()+"<br>"+query+"<br>";
                }
            }
        
        return query;  //cantidad de registros modificados (1) hopefully
        }
    

    // Recibe  el id_establecimiento y periodo, y devuelve una tabla html con los datos tributarios del periodo o vacios para que se completen. 

    private String getTable_carga_tributo(String id_establecimiento, String periodo, String id_comercio) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

	String resul="", saldo_iva="", saldo_iibb="";
        
            String condicion_iva;
            String condicion_iibb;        
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT em.base_imponible, em.compra_iva, em.percepcion_iva, em.alicuota_iibb, em.percepcion_iibb, "+  // 5
                                                                    "em.id_establecimiento_mes, em.saldo_iibb, em.saldo_iva, c.id_condicion_iva, c.id_condicion_iibb " +  // 10
                                                             "FROM Establecimientos e, EstablecimientosLiquiMes em, Comercios c " +
                                                             "WHERE e.id_establecimiento=em.id_establecimiento AND c.id_comercio=e.id_comercio " +
                                                             "  AND e.id_establecimiento=" +id_establecimiento+
                                                             "  AND periodo='"+periodo+"' ");

                                               
            rs = pst.executeQuery();
            if (rs.next())
                {  resul="<input type='hidden' name='id_establecimiento_mes' value='"+rs.getString(6)+"'><input type='hidden' name='condicion_iva' value='"+rs.getString(9)+"'><input type='hidden' name='condicion_iibb' value='"+rs.getString(10)+"'>"+
                       "<table cellSpacing='0' cellPadding='0'>\n\t"+
                      "<tr>\n\t\t<td>Periodo (mmyyyy): </td><td> <input type='text' name='periodo' value='"+periodo+"' size='7' readonly='readonly'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Base Imponible: </td><td><input type='number' name='base_imponible' value='"+rs.getString(1)+"'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Alicuota IVA:   </td><td><input type='number' name='alicuota_iva' value='21'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Compra (IVA): </td><td><input type='number' name='compra_iva' value='"+rs.getString(2)+"'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Percepcion IVA: </td><td><input type='number' name='percepcion_iva' value='"+rs.getString(3)+"'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Alicuota IIBB:   </td><td><input type='number' name='alicuota_iibb' step='any' value='"+rs.getString(4)+"'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Percepcion IIBB: </td><td><input type='number' name='percepcion_iibb' step='any' value='"+rs.getString(5)+"'></td></tr>\n\t" +
                      "</table>";
                  saldo_iva=rs.getString(8);
                  saldo_iibb=rs.getString(7);

            condicion_iva=rs.getString(9);
            condicion_iibb=rs.getString(10);

/*            if (HTML.getActivo_iva(condicion_iva))
                resul+="<br>condicion_iva="+condicion_iva+",  va activado en el periodo<br>";  // activo_iva=1
            else
                resul+="<br>condicion_iva="+condicion_iva+",  NO va activado en el periodo<br>";  // activo_iva=1


            if (HTML.getActivo_iibb(condicion_iibb))
                resul+="<br>condicion_iibb="+condicion_iibb+",  va activado en el periodo<br>";  // activo_iva=1
            else
                resul+="<br>condicion_iibb="+condicion_iibb+",  NO va activado en el periodo<br>";  // activo_iva=1
*/
                  
                }
            else
              {
                pst = con.prepareStatement( "SELECT id_condicion_iva, id_condicion_iibb FROM Comercios WHERE id_comercio=" +id_comercio);
                rs = pst.executeQuery();
                if (rs.next())
                  
                  resul="<input type='hidden' name='condicion_iva' value='"+rs.getString(1)+"'><input type='hidden' name='condicion_iibb' value='"+rs.getString(2)+"'>"+
                      "<table cellSpacing='0' cellPadding='0'>\n\t"+
                      "<tr>\n\t\t<td>Periodo (mmyyyy): </td><td><input type='text' name='periodo' value='"+periodo+"' size='7' readonly='readonly'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Base Imponible: </td><td><input type='number' name='base_imponible'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Alicuota IVA:   </td><td><input type='number' name='alicuota_iva' value='"+htm.getIVA_prn()+"'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Compra (IVA): </td><td><input type='number' name='compra_iva'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Percepcion IVA: </td><td><input type='number' name='percepcion_iva'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Alicuota IIBB:   </td><td><input type='number' name='alicuota_iibb' value='"+htm.getIIBB_caba_prn()+"' step='any'></td></tr>\n\t"+
                      "<tr>\n\t\t<td>Percepcion IIBB: </td><td><input type='number' name='percepcion_iibb'></td></tr>\n\t" +
                      "</table>";

/*             condicion_iva=rs.getString(1);
             condicion_iibb=rs.getString(2);
             if (HTML.getActivo_iva(condicion_iva))
                 resul+="<br>condicion_iva="+condicion_iva+",  va activado en el periodo<br>";  // activo_iva=1
             else
                resul+="<br>condicion_iva="+condicion_iva+",  NO va activado en el periodo<br>";  // activo_iva=1

             if (HTML.getActivo_iibb(condicion_iibb))
                 resul+="<br>condicion_iibb="+condicion_iibb+",  va activado en el periodo<br>";  // activo_iva=1
             else
                 resul+="<br>condicion_iibb="+condicion_iibb+",  NO va activado en el periodo<br>";  // activo_iva=1
 */           
              }
            resul+="\n</td>"   //<input type='submit'>\n
                + "\n<td></td>"
                + "\n<td>\n<table>"
                + "\n\t<tr><td>Debito IVA:</td><td><input type='number' style='font-size:14px' name='debito_iva' readonly "
                +                               "onfocus='this.value=document.form_tributo.base_imponible.value*document.form_tributo.alicuota_iva.value/100;  "
                +                                   "document.form_tributo.credito_iva.value=parseFloat(document.form_tributo.compra_iva.value)*parseFloat(document.form_tributo.alicuota_iva.value)/100; "
                +                                   "document.form_tributo.saldo_iva.value=parseFloat(document.form_tributo.debito_iva.value)-parseFloat(document.form_tributo.credito_iva.value)-parseFloat(document.form_tributo.percepcion_iva.value);'></td></tr>"
                + "\n\t<tr><td>IVA Credito:</td><td><input type='number' style='font-size:14px' name='credito_iva' readonly step='any'></td></tr>"
                + "\n\t<tr><td><b>D.D.J.J IVA</b>:</td><td><input size='10' type='text' style='font-size:22px' name='saldo_iva' readonly value='"+saldo_iva+"'></td></tr>"
                + "\n\t<tr><td><br><br></td><td></td></tr>"
                + "\n\t<tr><td>Debito IIBB:</td><td><input type='number' style='font-size:14px' name='debito_iibb' step='any' readonly "
                +                                       "onfocus='this.value=document.form_tributo.base_imponible.value*document.form_tributo.alicuota_iibb.value/100; "
                +                                       "document.form_tributo.saldo_iibb.value=parseFloat(document.form_tributo.debito_iibb.value)-parseFloat(document.form_tributo.percepcion_iibb.value);'></td></tr>"
                + "\n\t<tr><td><b>D.D.J.J IIBB</b>:</td><td><input size='10' type='text' style='font-size:22px' name='saldo_iibb' readonly value='"+saldo_iibb+"'></td></tr>"
                + "\n</table>"
                + "\n\n</td></tr></table>";
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
        
        return resul;  //tabla con datos del comercio
        }    
    

    
    // Recibe  el id_establecimiento y devuelve las observaciones del establecimiento

    private String CargaObservaciones (String id_establecimiento) 
        {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String resul="";
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT observaciones " + 
                                                             "FROM  Establecimientos " +
                                                             "WHERE id_establecimiento="+id_establecimiento);
            rs = pst.executeQuery();
            if (rs.next())
                resul= rs.getString(1) != null ?  rs.getString(1) : "" ;
            else
                   resul="ko";
            
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
        
        return resul;
        }    
    
                    
    
    
     // guarda las observaciones de un establecimiento realizado desde liquidaciones 
    private String UpdateObservaciones (String id_establecimiento, String observaciones) 
	{
        Connection con = null;
        PreparedStatement pst = null;
        long resul_insert=0;
        String query;
        
            query="UPDATE Establecimientos e  " +
                  " SET observaciones='"+observaciones+"' "+
                  " WHERE id_establecimiento="+id_establecimiento;
        
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement(query);
            resul_insert = pst.executeUpdate();
            query=Long.toString(resul_insert);
            }
        catch (SQLException ex) {
               query+= "<br><br>ERROR: "+ex.getMessage()+"<br>"+query+"<br>";
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
                query+= "<br><br>ERROR: "+ex.getMessage()+"<br>"+query+"<br>";
                }
            }
        
        return query;
        }
    


    

    
    
    
    
    
    
}