package ameca; 
 
import com.mysql.cj.util.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

//import java.text.DateFormat;  
//import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;  


/**
 *
 * @author manu
 */
public  class HTML {
    

public static final String dbase="dbAmeca";

private static final DecimalFormat newFormat = new DecimalFormat("#.##");
private static final DecimalFormat imprime = new DecimalFormat("#,###.##", DecimalFormatSymbols.getInstance(Locale.ITALIAN));


private static Boolean ignited_actividades=false;
private static Boolean ignited_zonas=false;
private static Boolean ignited_localidades=false;
private static Boolean ignited_condiciones_iva=false;
private static Boolean ignited_condiciones_iibb=false;
private static Boolean ignited_categorias_autonomo=false;
private static Boolean ignited_categorias_monotributo=false;
private static Boolean ignited_periodo=false;

private static Boolean ignited_parametros=false;  // tabla de parametros.
  
private static final int cod_localidad_caba=1;
private static final int cod_prov_caba=25;
private static final int cod_prov_ba=2;

private static Float comision_pago_facil=500f;  
private static Float alicuota_pago_facil=8.0f;  
    
private static Float alicuota_iibb_ba=0.035f;  
private static String alicuota_prn_iibb_ba="3.5";  
private static Float alicuota_iibb_caba=0.03f;  
private static String alicuota_prn_iibb_caba="3";  

private static Float alicuota_iva=0.21f;  
private static String prn_iva="21"; 

private static String periodo="201909";
//private static String periodo_com="201909";
private static final String[] meses = {"", "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
private static final String[] meses_l = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

private static String[] zonas = new String[44];
private static String form_zonas;

private static String[] localidades = new String [333];
private static String form_localidades;

private static String[] condiciones_iibb= new String [15];
private static String form_condiciones_iibb;

private static  String[] condiciones_iva=new String [15];
private static  String form_condiciones_iva;

private static  String[] actividades = new String[44];
private static String form_actividades;


private static Date date ;  
private static String strDate ;


private static String[][] categs_autonomo= new String [15][6] ;     //11.
private static Double[] montos_autonomo= new Double [15];     //11.
private static String form_categorias_autonomo;
private static int long_autonomos=11;  // longitud del vector

private static final String script_categorias_iva1="function make(cond_iva, categ) \n { var i; \n for(i = document.perfil.categ.options.length - 1 ; i >= 0 ; i--) \n { document.perfil.categ.remove(i); } \n if(cond_iva==2) \n { \n \n var option = document.createElement(\"option\"); option.value = '0'; option.text = 'Seleccione Categoria Autonomo'; document.perfil.categ.appendChild(option); \n ";
private static final String script_categorias_iva2="if (categ==0) document.perfil.categ_autonomo.value=1; \n else   document.perfil.categ.value=categ;  \n } \n\n else \n { \n var option = document.createElement(\"option\");  option.value = '0';  option.text = 'Seleccione Categoria Monotributo';  document.perfil.categ.appendChild(option); \n ";
private static final String script_categorias_iva3="if (categ==0) document.perfil.categ_monotributo.value=1; \n else  document.perfil.categ.value=categ;\n } \n\n }";


private static final String[][] categs_monotributo= new String [25][5];    //22
private static Double[] montos_monotributo= new Double [25];   //22
private static String form_categorias_monotributo; 
private static int long_monotributos=22;  // longitud del vector
    
private static final String head1="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n<title>Ameca</title>\n" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>\n" +
                    "<link rel='icon' href='/ameca/imgs/rocket_ico.png' type='image/png'/>\n" +
                    "<link href='/ameca/Styles/ameca.css'  type='text/css' rel='stylesheet' />\n" +
                    "</head>\n" +
                    "\n\n<body bgcolor='black' leftmargin='0' rightmargin='0' topmargin='5'>\n" +
                    "\n<table width='100%' cellspacing='0' cellpadding='0' border='0'  bgcolor='#2C383B'>\n" + // color de navbar
                    "\n<tr>\n" +
                    " <td height='150px' bgcolor='black' colspan='7'>\n" +
                    "  <img src='/ameca/imgs/ameca2.png' style='width:1290px;height:150px;'>\n" +
                    " </td>\n" +
                    " <td height='150px' bgcolor='black'>\n" +
                    "  <img src='/ameca/imgs/ameca2_crop.png' style='width:100%;height:150px;'>\n" +
                    " </td>\n" +
                    "</tr>\n" +
                    "\n<tr>\n\n\n";


private static final String head2= "<div class='navbar'>\n" +
                    "  <a href='/ameca/inicio'>Inicio</a>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "<td width='150px'></td>\n" +
                    "<td width='188px'>\n" +
                    "  <div class=\"dropdown\">\n" ;
                    //aca va comercios
private static final String head3= "      </button>\n" +
                    "    <div class='dropdown-content1'>\n" +
                    "      <a href='/ameca/comercios?operacion=new'>Nuevo</a>\n" +
                    "      <a href='/ameca/comercios?operacion=find'>Administrar</a>\n" +
                    "    </div>\n" +
                    "  </div> \n" +
                    "</td>\n" +
                    "<td width='222px'>\n" +
                    "  <div class=\"dropdown\">\n" ;
                    // aca va categoria Liquidaciones
/*public static  String head4= "      </button>\n" +
                    "    <div class=\"dropdown-content2\">\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_mv&periodo="+getPeriodo()+"'>IVA Mensual</a>\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_mb&periodo="+getPeriodo()+"'>IIBB Mensual</a>\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_m&periodo="+getPeriodo()+"'>Completa</a>\n" +
                    "    </div>\n" +
                    "  </div> \n" +
                    "</td>\n " ;
*/
private static final String head5= "</button>\n <div class='dropdown-content2'>\n   " +
                    "<a href='/ameca/reportes?operacion=find'>General </a>\n" +
                    "<a href='/ameca/reportes?operacion=bk_reportes'>Historico </a>\n" +
                    "  </div> \n" +
                    "  </div> \n" +
                    "</td>\n <td width='30px'></td>" +
                    "<td width='340px' align='right'><form action='/ameca/comercios' method='post'>\n <input type='hidden' name='operacion' value='find'>\n" +
                    "<div><img src='/ameca/imgs/search12.png' style='width:21px;height:21px;' ondblclick='document.forms[0].submit();'>\n" +
                    "<input type='text' name='cuit_calle' class='search'>&nbsp;\n" +
                    "</div>\n" +
                    "</form></td>\n" +
                    "<td></td>\n" +  //added movable
                    "</tr>\n"+
                    "\n<tr><td class='hole' colspan='8'>\n\n\n"+  // color de fondo
                    "<table>\n<tr><td>&nbsp;&nbsp;&nbsp;</td>  \n  <td>\n";   // tabla con margenes


private static final String tail="</td><td>&nbsp;&nbsp;&nbsp;</td></tr></table>"+   // cierro tabla con margenes
                                "\n</td></tr>\n\n"+
                                "<tr><td height='10px' bgcolor='#FCFCFC' colspan='8'>\n" +
                               "\n</td></tr>\n\n<tr><td height='99px' bgcolor='#45543D' colspan='8'></td></tr>\n" +
                                "\n\n</table>\n" +    // cierro tabla principal
                                "</body>\n" +
                                "</html>";






public static String getHead(String categ, String v_periodo)
    {
     String resul=head1;
     if (categ.equals("inicio"))
        resul +="<td bgcolor='#45543D' width='85px'>\n";
     else
        resul +="<td width='85px'>\n";
     resul +=head2;

     if (categ.equals("comercios"))
        resul +="    <button class=\"dropbtn_s\">Comercios \n";
     else
        resul +="    <button class=\"dropbtn\">Comercios \n";
     resul +=head3;

     if (categ.equals("liquidaciones"))
        resul +="    <button class=\"dropbtn_s\">Liquidaciones \n";
     else
        resul +="    <button class=\"dropbtn\">Liquidaciones \n";
    
     resul +="      </button>\n" +
                    "    <div class=\"dropdown-content2\">\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_mv&periodo="+v_periodo+"'>IVA Mensual</a>\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_mb&periodo="+v_periodo+"'>IIBB Mensual</a>\n" +
                    "      <a href='/ameca/liquidaciones?operacion=ver_m&periodo="+v_periodo+"'>Completa</a>\n" +
                    "    </div>\n" +
                    "  </div> \n" +
                    "</td>\n   <td width='222px'>\n <div class=\"dropdown\"> \n" ;
     
     if (categ.equals("reportes"))
        resul +="    <button class=\"dropbtn_s\">Reportes \n";
     else
        resul +="    <button class=\"dropbtn\">Reportes \n";

     resul +=head5;
     
     return resul;

    }






public static String getTail()
    { return tail; }



public  String getProvincia_localidad(int id_localidad)
    { 
    if(id_localidad==cod_localidad_caba)
    return "CABA"; 
    
    return "Buenos Aires";
}

public  String getProvincia(int id_provincia)
    { 
    if(id_provincia==cod_prov_caba)
    return "CABA"; 
    
    return "Buenos Aires";
}

public Double getTrunc  (Float nro)    // excel solo acepta double, no floats
{     if (nro == null || Float.isNaN(nro))
        return 0d;
   return  Double.parseDouble(newFormat.format(nro));
}

public String getTrunc_string  (Float nro)    
{     if (nro == null || Float.isNaN(nro))
    return "0";
   return  imprime.format(nro);
}

public String getTrunc_string  (Double nro)    
{     if (nro == null || Double.isNaN(nro))
    return "0";
   return  imprime.format(nro);
}

public String getPWD_day  ()    
{    
    date = Calendar.getInstance().getTime();  
   // strDate=date.toString();
   
    return Integer.toHexString(date.getDay()*28*date.getMonth()+date.getDay()*93 +date.getYear()*date.getMonth());
}



///////////  Localidades

public static Boolean getIgnited_localidades()
    { return ignited_localidades; }

public static void setIgnited_localidades(Boolean state)
    { ignited_localidades=state; }

public  String getLocalidad(int id_localidad)
    { 
    if(id_localidad < 1 || id_localidad>333)
        return "";
  
    return localidades[id_localidad]; 

    }

public static String getDropLocalidades()
    { return form_localidades; }



public static void Carga_localidades() 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    form_localidades="<select name='id_localidad'> <option value='0'>Seleccionar</option> ";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT id_localidad, nombre_localidad FROM Localidades WHERE id_provincia=25 OR id_provincia=2 ORDER BY 1");
        rs = pst.executeQuery();
        while (rs.next())
            {  localidades[Integer.valueOf(rs.getString(1))]=rs.getString(2); 
               form_localidades+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
               }
        form_localidades+="</select>";
        setIgnited_localidades(true);
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }
        

   }   






///////////  Zonas

public static Boolean getIgnited_zonas()
    { return ignited_zonas; }

public static void setIgnited_zonas(Boolean state)
    { ignited_zonas=state; }

public  String getZona(int id_zona)
    { 
    if(id_zona < 1 || id_zona>44)
        return "";
    if (!getIgnited_zonas())
        Carga_zonas();
    setIgnited_zonas(true);
  
    return zonas[id_zona]; 

    }

public static String getDropZonas()
    { return form_zonas; }


public static void Carga_zonas() 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    form_zonas="<select name='id_zona'> <option value=''>Seleccionar</option> ";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT * FROM Zonas");
        rs = pst.executeQuery();
        while (rs.next())
            {  zonas[Integer.valueOf(rs.getString(1))]=rs.getString(2); 
               form_zonas+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
               }
        form_zonas+="</select>";
        setIgnited_zonas(true);
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }
        

        }   



//////////  Actividades
    
public static Boolean getIgnited_actividades()
    { return ignited_actividades; }

public static void setIgnited_actividades(Boolean state)
    { ignited_actividades=state; }

public  String getActividad(int id_actividad)
    { 
    if(id_actividad < 1 || id_actividad>44)
        return "";
  
    return actividades[id_actividad]; 

    }

public static String getDropActividades()
    { return form_actividades; }

    public static void Carga_actividades () 
	{
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        form_actividades="<select name='id_actividad'> <option value='0'>Seleccionar</option>";
        try 
            {
            con=CX.getCx_pool();
            pst = con.prepareStatement( "SELECT * FROM Actividades");
            rs = pst.executeQuery();
            while (rs.next())
                {  actividades[Integer.valueOf(rs.getString(1))]=rs.getString(2); 
                   form_actividades+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
                   }
            form_actividades+="</select>";
            setIgnited_actividades(true);
            }
        catch (SQLException ex) {
              // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
                //resul= ex.getMessage();
                }
            }
        

        }    

    
    
    
//////////  Condiciones IIBB
    
public static Boolean getIgnited_condiciones_iibb()
    { return ignited_condiciones_iibb; }

public static void setIgnited_condiciones_iibb (Boolean state)
    { ignited_condiciones_iibb=state; }

public static String getCondicionIIBB(String id_cond_iibb)
    { 

        if (StringUtils.isStrictlyNumeric(id_cond_iibb))
            return condiciones_iibb [Integer.parseInt(id_cond_iibb)];
            
        return ""; 
    }

public static String getCondicionIIBB(int id_cond_iibb)
    { 

        if (id_cond_iibb>0 )
            return condiciones_iibb [id_cond_iibb];
            
        return ""; 
    }


public static String getForm_iibb()
    {   return form_condiciones_iibb;     }


public static void Carga_condiciones_iibb () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    form_condiciones_iibb="<select name='condicion_iibb'>  <option value='0'>Seleccionar</option>";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT * FROM CondicionesIIBB");
        rs = pst.executeQuery();
        while (rs.next())
            {  condiciones_iibb[Integer.valueOf(rs.getString(1))]=rs.getString(2); 
               form_condiciones_iibb+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
               }
        form_condiciones_iibb+="</select>";
        setIgnited_condiciones_iibb(true);
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    
    

public static Boolean getActivo_iibb(String condicion_iibb)   // not in (2, 4, 5)
    {   
     if (StringUtils.isStrictlyNumeric(condicion_iibb) && (condicion_iibb.equals("1") || condicion_iibb.equals("3") || condicion_iibb.equals("7")))
         return true;
     else
         return false;
    }

public static Boolean getActivo_iibb(int condicion)   // not in (2, 4, 5)
    {   
     if (condicion==1 || condicion==3 || condicion==7 )
         return true;
     else
         return false;
    }



public String getSaldoIIBB_calculo(String bi, String alic, String percepcion)
    {   Float bif, compraf, percepcionf, alicf;
        if (bi == null || !bi.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (percepcion == null || !percepcion.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (alic == null || !alic.matches("-?\\d+(\\.\\d+)?"))
            return "0";

        bif=Float.parseFloat(bi);
        alicf=Float.parseFloat(alic)/100;
        percepcionf=Float.parseFloat(percepcion);
        
        return Float.toString( bif * alicf - percepcionf);
    
    }








//////////  Condiciones IVA
   
public static Boolean getIgnited_condiciones_iva()
    { return ignited_condiciones_iva; }

public static void setIgnited_condiciones_iva (Boolean state)
    { ignited_condiciones_iva=state; }

public static String getCondicionIVA(String id_cond_iva)
    { 

        if (StringUtils.isStrictlyNumeric(id_cond_iva))
            return condiciones_iva [Integer.parseInt(id_cond_iva)];
            
        return ""; 
    }

public static String getCondicionIVA(int id_cond_iva)
    { 

        if (id_cond_iva > 0)
            return condiciones_iva [id_cond_iva];
            
        return ""; 
    }


public static String getForm_iva()
    {   return form_condiciones_iva;     }


public static void Carga_condiciones_iva () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    form_condiciones_iva="<select name='condicion_iva' onchange='make(this.value, 0);'> <option value='0'>Seleccionar</option>";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT * FROM CondicionesIVA");
        rs = pst.executeQuery();
        while (rs.next())
            {  condiciones_iva[Integer.valueOf(rs.getString(1))]=rs.getString(2); 
               form_condiciones_iva+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
               }
        form_condiciones_iva+="</select>";
        setIgnited_condiciones_iva(true);
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    

public static Boolean getActivo_iva(String condicion_iva)    // solo resp. inscripto
    {   
     if (StringUtils.isStrictlyNumeric(condicion_iva) && condicion_iva.equals("2") )
         return true;
     else
         return false;
    }

public static Boolean getActivo_iva(int condicion_iva)    // solo resp. inscripto
    {   
     if (condicion_iva==2 )
         return true;
     else
         return false;
    }



public String getSaldoIVA_calculo(String bi, String alic, String compra, String percepcion)
    {   Float bif, compraf, percepcionf, alicf;
        if (bi == null || !bi.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (compra == null || !compra.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (percepcion == null || !percepcion.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (alic == null || !alic.matches("-?\\d+(\\.\\d+)?"))
            return "0";

        bif=Float.parseFloat(bi);
        alicf=Float.parseFloat(alic)/100;
        compraf=Float.parseFloat(compra);
        percepcionf=Float.parseFloat(percepcion);
        
        return Float.toString( bif * alicf - compraf * alicf - percepcionf);
    
    }





//////////  Periodo
   
/*public static Boolean getIgnited_periodo()
    { return ignited_periodo; }

public static void setIgnited_periodo (Boolean state)
    { ignited_periodo=state; }

*/
public static Boolean getIgnited_periodo()
    { return ignited_periodo; }

public static  void setIgnited_periodo (Boolean state)
    { ignited_periodo=state; }

/*
public static String getPeriodo_com()
    {     return periodo_com;    }
public static void setPeriodo_com(String period)
    {     periodo_com=period;    }
*/
public String getPeriodo_nostatic()
    { 
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return periodo;
    }

public String getPeriodo_year ()
    {  
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return periodo.substring(0,4);
    }

public String getPeriodo_prn ()
    { 
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return periodo.substring(0,4)+" - "+meses[Integer.parseInt(periodo.substring(4,6))];
    }

public String getPeriodo_prn (String per)
    { 
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return per.substring(0,4)+" - "+meses[Integer.parseInt(per.substring(4,6))];
    }

public String getPeriodo_prn_long ()
    { 
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return periodo.substring(0,4)+" - "+meses_l[Integer.parseInt(periodo.substring(4,6))];
    }
public String getPeriodo_prn_long (String per)
    { 
      if (!getIgnited_periodo() )
         {
          Carga_periodo();
          setIgnited_periodo(true);
          }
    return per.substring(0,4)+" - "+meses_l[Integer.parseInt(per.substring(4,6))];
    }


public void setPeriodo_nostatic(String period)
    { 
    periodo=period;
    }



public String getPeriodo_prox(String p_periodo)
    { 
    int yy=Integer.valueOf(p_periodo.substring(0, 4));
    int mm=Integer.valueOf(p_periodo.substring(4));
    if (mm==12 )
        return Integer.toString(++yy)+"01";
    else
         p_periodo=++mm< 10 ? "0"+Integer.toString(mm) : Integer.toString(mm);
      
    return Integer.toString(yy)+p_periodo;
    }

public String getPeriodo_pre (String p_periodo)
    { 
    int yy=Integer.valueOf(p_periodo.substring(0, 4));
    int mm=Integer.valueOf(p_periodo.substring(4));
    if (mm==1 )
        return Integer.toString(--yy)+"12";
    else
       p_periodo=--mm< 10 ? "0"+Integer.toString(mm) : Integer.toString(mm);
      
    return Integer.toString(yy)+p_periodo;
    }




public void Carga_periodo () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT MAX(periodo) FROM EstablecimientosLiquiMes");
        rs = pst.executeQuery();
        if (rs.next())
              periodo=rs.getString(1); 
        ignited_periodo=true;
       // periodo_com=periodo;
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    




//////////  Categorias autonomo (de los responsables inscriptos)
    
public static Boolean getIgnited_categorias_autonomo()
    { return ignited_categorias_autonomo; }

public static void setIgnited_categorias_autonomo (Boolean state)
    { ignited_categorias_autonomo=state; }


public static String getCategoriaAutonomo(int id_categ_autonomo)
    { 

        if (id_categ_autonomo>0)
            return "Categoria Autonomo:<br><b> "+categs_autonomo [id_categ_autonomo][0]+
                    "</b><br>Tope Categoria: <b>"+categs_autonomo [id_categ_autonomo][1]+
                    "</b><br> Monto Mensual: <b>"+categs_autonomo [id_categ_autonomo][2]+
                    "</b><br>Tabla: <b>"+categs_autonomo [id_categ_autonomo][3]+"</b>. Categ: <b>"+categs_autonomo [id_categ_autonomo][4]+"</b>";
            
        return ""; 
    }

public  String getCategoriaAutonomo_min(int id_categ_autonomo)
    { 

        if (id_categ_autonomo>0)
            return categs_autonomo [id_categ_autonomo][0].substring(0,13)+
                    "... , Monto:"+categs_autonomo [id_categ_autonomo][2]+
                    ", Tabla: "+categs_autonomo [id_categ_autonomo][3]+", Categ: "+categs_autonomo [id_categ_autonomo][4];
            
        return ""; 
    }


public String getMontoAutonomo(int id_categ_autonomo)
    { 
        if (id_categ_autonomo>0)
            return categs_autonomo [id_categ_autonomo][2];
            
        return ""; 
    }
public Double getMontoAutonomo_f (int id_categ_autonomo)
    { 
        if (id_categ_autonomo>0)
            return montos_autonomo [id_categ_autonomo];
            
        return 0d; 
    }



public static void Carga_categorias_autonomo () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int i=1;
    form_categorias_autonomo="<select name='categ_autonomo'> <option value='0'>Seleccionar</option>";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT id_categoria_autonomo, nom_categoria_autonomo, tope_categoria_autonomo, "
                                                    +               "FORMAT(monto_mensual_autonomo, 2, 'de_DE') monto, tabla_autonomo, "
                                                    +               "categoria_tabla_autonomo, monto_mensual_autonomo "
                                                    + " FROM CategoriasAutonomo");
        rs = pst.executeQuery();
        while (rs.next())
            {
             categs_autonomo [Integer.valueOf(rs.getString(1))][0]=rs.getString(2); 
             categs_autonomo [Integer.valueOf(rs.getString(1))][1]=rs.getString(3); 
             categs_autonomo [Integer.valueOf(rs.getString(1))][2]=rs.getString(4); 
             categs_autonomo [Integer.valueOf(rs.getString(1))][3]=rs.getString(5); 
             categs_autonomo [Integer.valueOf(rs.getString(1))][4]=rs.getString(6); 
             
             montos_autonomo[Integer.valueOf(rs.getString(1))]=rs.getDouble(7);
             i++; 
             form_categorias_autonomo+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+"</option>";
               }
        form_categorias_autonomo+="</select>";
        setIgnited_categorias_autonomo(true);
        long_autonomos=i;
        }
    catch (SQLException ex) {
          // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    



//////////  Categorias monotributo
    
public static Boolean getIgnited_categorias_monotributo()
    { return ignited_categorias_monotributo; }

public static void setIgnited_categorias_monotributo (Boolean state)
    { ignited_categorias_monotributo=state; }


public static String getCategoriaMonotributo(int id_cond_iva)
    { 
        if (id_cond_iva>0)
            return "Categoria Monotributo: <b>"+categs_monotributo [id_cond_iva][0]+
                    "</b><br> Cat. Adicional: <b>"+categs_monotributo [id_cond_iva][1]+
                    ".</b> Tope: <b>"+categs_monotributo [id_cond_iva][2]+
                    "</b><br>Monto Mensual: <b>"+categs_monotributo [id_cond_iva][3]+"</b>";
            
        return ""; 
    }

public String getCategoriaMonotributo_min(int id_cond_iva)
    { 
        if (id_cond_iva>0)
            return "Categoria: "+categs_monotributo [id_cond_iva][0]+
                    ", Cat. Adicional: "+categs_monotributo [id_cond_iva][1]+
                    ", Monto Mensual: "+categs_monotributo [id_cond_iva][3];
            
        return ""; 
    }


public  String getMontoMonotributo(int id_categ_monotributo)
    { 
        if (!ignited_categorias_monotributo)
                Carga_categorias_monotributo();
        if (id_categ_monotributo>0 && id_categ_monotributo<long_monotributos)
            return categs_monotributo [id_categ_monotributo][3];
            
        return ""; 
    }

public  Double getMontoMonotributo_f (int id_categ_monotributo)
    { 
        if (id_categ_monotributo>0 && id_categ_monotributo<long_monotributos)
            return montos_monotributo [id_categ_monotributo];
            
        return 0d; 
    }


public static void Carga_categorias_monotributo () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int i=1, k=0;
    form_categorias_monotributo="<select name='categ_monotributo'> <option value='0'>Seleccionar</option>";
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT * FROM CategoriasMonotributo");
        rs = pst.executeQuery();
        while (rs.next())
            {
             k=Integer.valueOf(rs.getString(1));
             categs_monotributo [k][0]=rs.getString(2); 
             categs_monotributo [k][1]=rs.getString(3); 
             categs_monotributo [k][2]=rs.getString(4); 

             montos_monotributo[k]=rs.getDouble(5);
             categs_monotributo [k][3]=String.format(Locale.GERMAN, "%,.2f", montos_monotributo[k]); 
             
             i++; 
             form_categorias_monotributo+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+", "+rs.getString(5)+"</option>";
             }
        form_categorias_monotributo+="</select>";
        setIgnited_categorias_monotributo(true);
        long_monotributos=i;
        }
    catch (SQLException ex) {
        // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    








public  String getScriptCategsIVA()
    {   String resul=script_categorias_iva1;
        int i;
        for (i=1; i<long_autonomos; i++)
            resul+=" var option = document.createElement(\"option\");  option.value = '"+Integer.toString(i)+"'; option.text = '"+categs_autonomo[i][0]+","+categs_autonomo[i][1]+", "+categs_autonomo[i][2]+","+categs_autonomo[i][3]+","+categs_autonomo[i][4]+"'; document.perfil.categ.appendChild(option);  \n";
        resul+=script_categorias_iva2;
        for (i=1; i<long_monotributos; i++)
            resul+=" var option = document.createElement(\"option\");  option.value = '"+Integer.toString(i)+"'; option.text = '"+categs_monotributo[i][0]+","+categs_monotributo[i][1]+", "+categs_monotributo[i][2]+","+categs_monotributo[i][3]+"'; document.perfil.categ.appendChild(option);  \n";
        return resul+script_categorias_iva3;
    }





public static void setIgnited_parametros (Boolean state)
    { ignited_parametros=state; }

public static Boolean getIgnited_parametros()
    { return ignited_parametros; }



public Float getIIBB_caba()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }

      return alicuota_iibb_caba; 
}


public String getIIBB_caba_prn()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }

      return Float.toString(alicuota_iibb_caba*100); 
}


public Float getIIBB_ba()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }

      return alicuota_iibb_ba; 
}

public String getIIBB_ba_prn()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }

      return Float.toString(alicuota_iibb_ba*100); 
}



public Float getIVA()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }

      return alicuota_iva; 
}

public String getIVA_prn()  // varia por localidad => provincia
    { 
      if (!ignited_parametros)
        cargaParametros();

      return prn_iva; 
}



public static Float getAlicuotaPF()
    { 
      if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }
      return alicuota_pago_facil; }

public static String getAlicuotaPF_prn()
    { 
     if (!ignited_parametros)
        {cargaParametros();
         ignited_parametros=true;
        }
     return Float.toString(alicuota_pago_facil); 
    }

public static Float getComisionPF()
    { return comision_pago_facil; }

public static String getComisionPF_prn()
    { return Float.toString(comision_pago_facil); }



public static void cargaParametros () 
    {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try 
        {
        con=CX.getCx_pool();
        pst = con.prepareStatement( "SELECT alicuota_iibb_provincia, alicuota_iibb_caba, alicuota_iva, alicuota_pago_facil, comision_pago_facil "
                                    + "FROM Parametros");
        rs = pst.executeQuery();
        if (rs.next())
            {
             comision_pago_facil=rs.getFloat(5);  
             alicuota_pago_facil=rs.getFloat(4);  
             alicuota_prn_iibb_ba=rs.getString(1);
             alicuota_prn_iibb_caba=rs.getString(2);
             prn_iva=rs.getString(3);
             
             alicuota_iva=Float.valueOf(prn_iva)/100;
             alicuota_iibb_ba=Float.valueOf(alicuota_prn_iibb_ba)/100;
             alicuota_iibb_caba=Float.valueOf(alicuota_prn_iibb_caba)/100;

            
            }
        ignited_parametros=true;
        }
    catch (SQLException ex) {
        // resul= "<br><br>ERROR: "+ex.getMessage()+"<br><br>";
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
            //resul= ex.getMessage();
            }
        }

    }    




////////////////////      Reportes

public String getSubtotalReporte_calculo(String saldo_iva, String saldo_iibb, String gan, String afip, String suss)
    {
        if (saldo_iva == null || !saldo_iva.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (saldo_iibb == null || !saldo_iibb.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (gan == null || !gan.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (afip == null || !afip.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (suss == null || !suss.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        return Float.toString (Float.parseFloat(saldo_iva) + Float.parseFloat(saldo_iibb) + Float.parseFloat(gan) + Float.parseFloat(afip) + Float.parseFloat(suss));
    }

public String getSaldoPF_reporte_calculo(String subtotal, String alic)
    {  
        if (alic == null || !alic.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (subtotal == null || !subtotal.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        return Float.toString (Float.parseFloat(alic) * Float.parseFloat(subtotal) / 100);
    }

////////  subtotal+saldo_pago_facil+comision_pago_facil

public String getTotalReporte_calculo(String subtotal, String saldo, String comision)
    {  
        if (saldo == null || !saldo.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (subtotal == null || !subtotal.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        if (comision == null || !comision.matches("-?\\d+(\\.\\d+)?"))
            return "0";
        return Float.toString (Float.parseFloat(saldo) + Float.parseFloat(subtotal) + Float.parseFloat(comision));
    }

public static String isNumber(String nro)
    {  
        if (nro == null || !nro.matches("-?\\d+(\\.\\d+)?"))
            return "ko";
        return "number_ok";
    }



}
