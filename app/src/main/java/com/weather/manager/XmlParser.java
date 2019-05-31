package com.weather.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.widget.Toast;

public class XmlParser {
	 InputStream inStream;  
	    Element root;  
	  
	    public InputStream getInStream() {  
	        return inStream;  
	    }  
	  
	    public void setInStream(InputStream inStream) {  
	        this.inStream = inStream;  
	    }  
	  
	    public Element getRoot() {  
	        return root;  
	    }  
	  
	    public void setRoot(Element root) {  
	        this.root = root;  
	    }  
	  
	    public XmlParser() {  
	    }  
	  
	    public XmlParser(InputStream inStream) {  
	        if (inStream != null) {  
	            this.inStream = inStream;  
	            DocumentBuilderFactory domfac = DocumentBuilderFactory  
	                    .newInstance();  
	            try {  
	                DocumentBuilder domBuilder = domfac.newDocumentBuilder();  
	                Document doc = domBuilder.parse(inStream);  
	                root = doc.getDocumentElement();  
	            } catch (ParserConfigurationException e) {  
	                e.printStackTrace();  
	            } catch (SAXException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	  
	    public XmlParser(String path) {  
	        InputStream inStream = null;  
	        try {  
	            inStream = new FileInputStream(path);  
	        } catch (FileNotFoundException e1) {  
	            e1.printStackTrace();  
	        }  
	        if (inStream != null) {  
	            this.inStream = inStream;  
	            DocumentBuilderFactory domfac = DocumentBuilderFactory  
	                    .newInstance();  
	            try {  
	                DocumentBuilder domBuilder = domfac.newDocumentBuilder();  
	                Document doc = domBuilder.parse(inStream);  
	                root = doc.getDocumentElement();  
	            } catch (ParserConfigurationException e) {  
	                e.printStackTrace();  
	            } catch (SAXException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	  
	    public XmlParser(URL url) {  
	        InputStream inStream = null;  
	         
	        try {  
	        	 HttpURLConnection connect = null;
	        	connect = (HttpURLConnection) url.openConnection();  
	        	if(connect == null){
	        	connect.setConnectTimeout(5000); 
		        connect.setReadTimeout(5000);
	        	} 	
		            
		            InputStream is = connect.getInputStream();  
	            inStream = is;  
	        } catch (IOException e1) {  
	            e1.printStackTrace();  
	        }   
	        if (inStream != null) {     
	            this.inStream = inStream;  
	            DocumentBuilderFactory domfac = DocumentBuilderFactory  
	                    .newInstance();  
	            try {  
	                DocumentBuilder domBuilder = domfac.newDocumentBuilder();  
	                Document doc = domBuilder.parse(inStream);  
	                root = doc.getDocumentElement();  
	            } catch (ParserConfigurationException e) {   
	                e.printStackTrace();  
	            } catch (SAXException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            } catch (Exception e){
	            	e.printStackTrace();
	            } 
	        } else{
	        	System.out.println("sharenfanghuo..................");
	        }
	    }  
	  
	    /** 
	     *  
	     * @param nodes 
	     * @return �����ڵ���ֵ�Էֺŷָ� 
	     */  
	    public Map<String, String> getValue(String[] nodes) {  
	        if (inStream == null || root==null) {  
	            return null;  
	        }  
	        Map<String, String> map = new HashMap<String, String>();  
	        // ��ʼ��ÿ���ڵ��ֵΪnull  
	        for (int i = 0; i < nodes.length; i++) {  
	            map.put(nodes[i], null);  
	        }  
	  
	        // �����һ�ڵ�  
	        NodeList topNodes = root.getChildNodes();  
	        if (topNodes != null) {     
	            for (int i = 0; i < topNodes.getLength(); i++) {  
	                Node book = topNodes.item(i);  
	                if (book.getNodeType() == Node.ELEMENT_NODE) {  
	                    for (int j = 0; j < nodes.length; j++) {  
	                        for (Node node = book.getFirstChild(); node != null; node = node  
	                                .getNextSibling()) {  
	                        //	System.out.println(node.getNodeName()+"------------------------>名字");
                            //	System.out.println(node.getTextContent()+"_____________________________>内容");
	                            if (node.getNodeType() == Node.ELEMENT_NODE) {   
	                            	
	                                if (node.getNodeName().equals(nodes[j])) {  
	                                    //String val=node.getFirstChild().getNodeValue();  
	                                    String val = node.getTextContent();  
	                                    System.out.println(nodes[j] + ":" + val);  
	                                    // ���ԭ���Ѿ���ֵ���Էֺŷָ�  
	                                    String temp = map.get(nodes[j]);  
	                                    if (temp != null && !temp.equals("")) {  
	                                        temp = temp + ";" + val;  
	                                    } else {  
	                                        temp = val;  
	                                    }  
	                                    map.put(nodes[j], temp);  
	                                }  
	                            }  
	                        }  
	                    }  
	                }  
	            }  
	        }  
	        return map;  
	    }  
}
