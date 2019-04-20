package org.apache.maven.RestServiceProject;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.ws.BindingProvider;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("TestService")
public class MyResource {
	Statement st;
	Connection con;
	ResultSet rs;
	int c=0;
	String retValue="";
	char quotes ='"';

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws ParserConfigurationException 
     * @throws IOException 
     */
	
	
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String functions(@QueryParam("func") String func,@QueryParam("productid") String productid,@QueryParam("macid") String macid,@QueryParam("description") String description,@QueryParam("argument") String argument,@QueryParam("stockid") String stockid,@QueryParam("stock_code") String stock_code,@QueryParam("stockcode") String stockcode,@QueryParam("newstockcode") String newstockcode,@QueryParam("newdescription") String newdescription,@QueryParam("newargument") String newargument) throws IOException, ParserConfigurationException {
    	if(func.equals("readTestList")) //http://127.0.0.1:8080/RestServiceProject/TestService?func=readTestList&stock_code=GY270269
    		retValue=readTestList(stock_code);
    	else if(func.equals("readStockCodes")) //http://127.0.0.1:8080/RestServiceProject/TestService?func=readStockCodes
    		retValue=readStockCodes();
    	else if(func.equals("updateStockCodes"))
    		retValue=updateStock(stockcode,newstockcode,newdescription,newargument);
    	else if(func.equals("getProductMacId"))
    		retValue=getProductMacId(productid);
    	else if(func.equals("insertStock"))
    		retValue=insertStock(stockid,stockcode,description,argument);
    	else if(func.equals("setProduct"))
    		retValue=setProduct(productid,macid); //http://127.0.0.1:8080/RestServiceProject/TestService?func=setProduct&productid=IKB18140001&macid=ZZZZZZZZZZZZ
    	else if(func.equals("functions"))
    		retValue= functions();
    	else if(func.equals("getVersion"))
    		retValue="<OK code=\"0\" message=\"TCS v1.02 sendTestResult xml parse updated for multiple testroot tags.20180504 \"/>\r\n" + 
    				"\r\n" + 
    				"";
    	return retValue;
    	
    }
    @GET
    @Path("functions")
    @Produces(MediaType.APPLICATION_XML)
	public String functions() {
    	return " <functions><function>readTestList(stock_code)</function><function>readStockCodes(stock_code)</function><function>updateStock(stockcode,newstockcode,newdescription,newargument)</function><function>getProductMacId(productid)</function><function>insertStock(stockid,stockcode,description,argument)</function><function>setProduct(productid,macid)</function></functions> ";
		
	}
    @GET
    @Path("readStockCodes") //bu path'dende ulasılabilir
    @Produces(MediaType.APPLICATION_XML)
    public String readStockCodes() throws IOException, ParserConfigurationException {
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		String data = null; String data2=null; String data3=null;
		String strResult=null;
		try {
			String query = "SELECT * FROM stock";
			rs = st.executeQuery(query);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("ROOT");
			doc.appendChild(rootElement);


		while (rs.next()) {
			
		data = rs.getString("STOCK_CODE");
		data2= rs.getString("DESCRIPTION");
		data3= rs.getString("ARGUMENT");



		// root elements

		// staff elements
		Element stock = doc.createElement("STOCK");
		rootElement.appendChild(stock);

		// set attribute to stock element
		Attr attr = doc.createAttribute("STOCK_CODE");
		attr.setValue(data);
		stock.setAttributeNode(attr);

		Attr attr2=doc.createAttribute("DESCRIPTION");
		attr2.setValue(data2);
		stock.setAttributeNode(attr2);

		Attr attr3=doc.createAttribute("ARGUMENT");
		attr3.setValue(data3);
		stock.setAttributeNode(attr3);

		// shorten way
		// staff.setAttribute("id", "1");


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result =  new StreamResult(writer);
		transformer.transform(source, result);
		strResult = writer.toString();
		
		}

		} catch (Exception e) {
		System.out.println("Error : " + e);
		}
		return strResult;

		}
    
    @Path("Hi")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hiWorld() {
    	return "Hi";
    }
    
    @GET
    @Path("readTestList")
    @Produces(MediaType.APPLICATION_XML)
    public String readTestList(@QueryParam("stock_code") String stock_code) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		String data = null; String data2=null; String data3=null;
		String strResult=null;
		try {
			String query = "SELECT s.STOCK_CODE , t.TEST_NAME , s.ARGUMENT FROM stocktestlist as st INNER JOIN stock AS s ON st.STOCK_ID = s.STOCK_ID INNER JOIN tests AS t ON st.TEST_ID=t.TEST_ID WHERE s.STOCK_CODE='"+stock_code+"' GROUP BY s.STOCK_CODE , t.TEST_NAME , s.ARGUMENT;";
			rs = st.executeQuery(query);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("ROOT");
			doc.appendChild(rootElement);


		while (rs.next()) {
			
		data = rs.getString("STOCK_CODE");
		data2= rs.getString("TEST_NAME");
		data3= rs.getString("ARGUMENT");



		// root elements

		// staff elements
		Element stock = doc.createElement("STOCKTESTLIST");
		rootElement.appendChild(stock);

		// set attribute to stock element
		Attr attr = doc.createAttribute("STOCK_CODE");
		attr.setValue(data);
		stock.setAttributeNode(attr);

		Attr attr2=doc.createAttribute("TEST_ID");
		attr2.setValue(data2);
		stock.setAttributeNode(attr2);

		Attr attr3=doc.createAttribute("ARGUMENT");
		attr3.setValue(data3);
		stock.setAttributeNode(attr3);

		// shorten way
		// staff.setAttribute("id", "1");


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result =  new StreamResult(writer);
		transformer.transform(source, result);
		strResult = writer.toString();



		}

		} catch (Exception e) {
		System.out.println("Error : " + e);
		}
		return strResult;
		
	}
    
    @GET
    @Path("getProductMacId/{productid}")
    @Produces(MediaType.APPLICATION_XML)
    public String getProductMacId(@PathParam("productid") String productid) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		String data = null; String data2=null; String data3=null; String data4=null; String data5=null; String data6=null;
		String data7= null; String data8=null; String data9=null;
		String strResult=null;
		try {
			String query = "SELECT * FROM products WHERE PRODUCT_ID='"+productid+"'";
			rs = st.executeQuery(query);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("TESTROOT");
			doc.appendChild(rootElement);


		while (rs.next()) {
			
		data = rs.getString("M1");
		data2= rs.getString("M2");
		data3= rs.getString("M3");
		data4= rs.getString("M4");
		data5= rs.getString("M5");
		data6= rs.getString("M6");
		data7= rs.getString("PRODUCT_ID");
		data8= rs.getString("PROD_DATE");
		data9= rs.getString("PCP_NO");



		// root elements

		// staff elements
		Element mac = doc.createElement("MAC");
		rootElement.appendChild(mac);

		// set attribute to stock element
		Attr attr = doc.createAttribute("MAC_ID");
		attr.setValue(data2+":"+data+":"+data4+":"+data3+":"+data6+":"+data5);
		mac.setAttributeNode(attr);

		Attr attr2=doc.createAttribute("PRODUCT_ID");
		attr2.setValue(data7);
		mac.setAttributeNode(attr2);

		Attr attr3=doc.createAttribute("PCB_ID");
		attr3.setValue(data9);
		mac.setAttributeNode(attr3);
		
		Attr attr4=doc.createAttribute("PROD_DATE");
		attr4.setValue(data8);
		mac.setAttributeNode(attr4);

		// shorten way
		// staff.setAttribute("id", "1");


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result =  new StreamResult(writer);
		transformer.transform(source, result);
		strResult = writer.toString();



		}

		} catch (Exception e) {
		System.out.println("Error : " + e);
		}
		return strResult;
		
	}
    
    
    @GET
    @Path("updateStock/{stockcode}/{newstockcode}/{newdescription}/{newargument}")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateStock(@PathParam("stockcode")String stockcode,@PathParam("newstockcode")String newstockcode,@PathParam("newdescription")String newdescription,@PathParam("newargument")String newargument) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		try {
			String query="UPDATE stock SET STOCK_CODE = '"+newstockcode+"' , DESCRIPTION='"+newdescription+"' , ARGUMENT='"+newargument+"' WHERE STOCK_CODE = '"+stockcode+"'";
			st.executeUpdate(query);
			System.out.println("Stock updated");
			

		} catch (Exception e) {
			System.out.println("Error : " + e);
		}
		return "New stock has been updated!";
		
	}
    
    @GET
    @Path("insertStock/{stockid}/{stockcode}/{description}/{argument}")
    @Produces(MediaType.APPLICATION_XML)
    public String insertStock(@PathParam("stockid")String stockid,@PathParam("stockcode")String stockcode,@PathParam("description")String description,@PathParam("argument")String argument) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		try {
			String query = " insert into stock (STOCK_ID,STOCK_CODE, DESCRIPTION, ARGUMENT)" + " values ('"+stockid+"','"+stockcode+"','"+description+"','"+argument+"')";
			st.executeUpdate(query);
			System.out.println("New Stock added on stock database");

		} catch (Exception e) {
			System.out.println("Error : " + e);
		}
		return "<response>New stock has been added to database!</response>";
	}
    
    @GET
    @Path("setProduct/{productid}/{macid}")
    @Produces(MediaType.APPLICATION_XML)
    public String setProduct(@PathParam("productid")String productid,@PathParam("macid")String macid) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kentkart?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root","");
			st = con.createStatement();
			} catch (Exception e) {
			System.out.println("Error  : " + e);
			}
		try {
			String query="UPDATE products SET M1 = '"+macid.substring(2, 4)+"' , M2= '"+macid.substring(0, 2)+"', M3= '"+macid.substring(6, 8)+"',  M4= '"+macid.substring(4, 6)+"'  , M5 ='"+macid.substring(10, 12)+"' , M6 = '"+macid.substring(8, 10)+"'    WHERE PRODUCT_ID = '"+productid+"' ";
			st.executeUpdate(query);
			System.out.println("Product updated");

		} catch (Exception e) {
			System.out.println("Error : " + e);
		}
		return "<response>Product has been updated!</response>";
		
		
	}
  

   
}
