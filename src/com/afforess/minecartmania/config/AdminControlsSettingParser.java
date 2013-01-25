package com.afforess.minecartmania.config;

import java.io.File;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.entity.MinecartManiaWorld;

public class AdminControlsSettingParser implements SettingParser{
	private static final double version = 1.2;
	private static MinecartManiaLogger log = MinecartManiaLogger.getInstance();

	public boolean isUpToDate(Document document) {
		try {
			NodeList list = document.getElementsByTagName("version");
			Double version = MinecartManiaConfigurationParser.toDouble(list.item(0).getChildNodes().item(0).getNodeValue(), 0);
			log.debug("Admin Controls Config read: version: " + list.item(0).getTextContent());
			if (version == 1.2) {
				//Place the code to update to the next version here
				//version = 1.3;	//This needs to be updated to the next version of the document.
				//list.item(0).setTextContent(version.toString());
				
			}
			return version == AdminControlsSettingParser.version;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean read(Document document) {
		//Set the default configuration before we try to read anything.
		setDefaultConfiguration();

		NodeList list;
		try {
			list = document.getElementsByTagName("MinecartManiaConfiguration").item(0).getChildNodes();	//get the root nodes of the ConfigurationTree
			String elementChildName = "";		//holds the name of the node
			String elementChildValue = "";		//holds the value of the node
			//loop through each of the child nodes of the document
			for (int idx = 0; idx < list.getLength(); idx++) {
				Node elementChild = list.item(idx);	//extract the node
				elementChildName = "";				//reset the child name
				elementChildValue = null;			//reset the child value
				//do we have a valid element node
				if (elementChild.getNodeType() == Node.ELEMENT_NODE) {
					elementChildName = elementChild.getNodeName();	//get the node name
					elementChildValue = elementChild.getTextContent(); //get the node value
					if (elementChildValue != null && elementChildValue != "") {
						//Handle the possible nodes we have at this level.
						if (elementChildName == "version") {
							if (elementChildValue != String.valueOf(version)) { /* documentUpgrade(document); */ }
						} else if (elementChildName == "EmptyMinecartKillTimer"
								|| elementChildName == "EmptyStorageMinecartKillTimer"
								|| elementChildName == "EmptyPoweredMinecartKillTimer"
								) {
							MinecartManiaWorld.getConfiguration().put(elementChildName, MinecartManiaConfigurationParser.toInt(elementChildValue, getDefaultConfigurationIntegerValue(elementChildName)));
							log.debug("Admin Controls Config read: " + elementChildName + " = " + elementChildValue);
						} else if (elementChildName == "MinecartTrackAdjuster") {
							MinecartManiaWorld.getConfiguration().put(elementChildName, MinecartManiaConfigurationParser.toItem(elementChildValue));
							log.debug("Admin Controls Config read: " + elementChildName + " = " + elementChildValue);
						} else {
							log.info("Admin Controls Config read unknown node: " + elementChildName);
						}
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		debugShowConfigs();
		return true;
	}
	private void setDefaultConfiguration() {
		//Create the default Configuration values
		MinecartManiaWorld.getConfiguration().put("EmptyMinecartKillTimer",			getDefaultConfigurationIntegerValue("EmptyMinecartKillTimer"));
		MinecartManiaWorld.getConfiguration().put("EmptyStorageMinecartKillTimer",	getDefaultConfigurationIntegerValue("EmptyStorageMinecartKillTimer"));
		MinecartManiaWorld.getConfiguration().put("EmptyPoweredMinecartKillTimer",	getDefaultConfigurationIntegerValue("EmptyPoweredMinecartKillTimer"));
        MinecartManiaWorld.getConfiguration().put("MinecartTrackAdjuster",          MinecartManiaConfigurationParser.toItem("66"));
	}
	private int getDefaultConfigurationIntegerValue(String ConfigName) {
		if (ConfigName == "EmptyMinecartKillTimer") return (-1);
		if (ConfigName == "EmptyStorageMinecartKillTimer") return (-1);
		if (ConfigName == "EmptyPoweredMinecartKillTimer") return (-1);
		return 0;
	}

	private void debugShowConfigs() {
		//Display global configuration values
		for (Enumeration<String> ConfigKeys = MinecartManiaWorld.getConfiguration().keys(); ConfigKeys.hasMoreElements();) {
			String temp = ConfigKeys.nextElement();
			String value = MinecartManiaWorld.getConfigurationValue(temp,"").toString();
			if     (temp == "EmptyMinecartKillTimer"
				 || temp == "EmptyStorageMinecartKillTimer"
				 || temp == "EmptyPoweredMinecartKillTimer"
                 || temp == "MinecartTrackAdjuster"
                 || temp == "AlwaysRememberStation"
				){
				log.debug("Admin Controls Config: " + temp + " = " + value);
			}
		}
	}

	public boolean write(File configuration, Document document) {
		try {
			if (document == null) {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				//root elements
				document = docBuilder.newDocument();
				document.setXmlStandalone(true);
				Element rootElement = document.createElement("MinecartManiaConfiguration");
				document.appendChild(rootElement);
				Element setting = document.createElement("version");
				setting.appendChild(document.createTextNode(String.valueOf(version)));
				rootElement.appendChild(setting);

				setting = document.createElement("EmptyMinecartKillTimer");
				Comment comment = document.createComment("After a minecart has been empty longer than the set time (in seconds) it will be destroyed. Negative values will disable the timer");
				setting.appendChild(document.createTextNode("-1"));
				rootElement.appendChild(setting);
				rootElement.insertBefore(comment,setting);

				setting = document.createElement("EmptyStorageMinecartKillTimer");
				comment = document.createComment("After a storage minecart has been empty longer than the set time (in seconds) it will be destroyed. Negative values will disable the timer");
				setting.appendChild(document.createTextNode("-1"));
				rootElement.appendChild(setting);
				rootElement.insertBefore(comment,setting);

				setting = document.createElement("EmptyPoweredMinecartKillTimer");
				comment = document.createComment("After a powered minecart has been empty longer than the set time (in seconds) it will be destroyed. Negative values will disable the timer");
				setting.appendChild(document.createTextNode("-1"));
				rootElement.appendChild(setting);
				rootElement.insertBefore(comment,setting);

                setting = document.createElement("MinecartTrackAdjuster");
                comment = document.createComment("Clicking on track with this item will cause it to cycle through all possible rail shapes.");
                setting.appendChild(document.createTextNode("270"));
                rootElement.appendChild(setting);
                rootElement.insertBefore(comment,setting);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(configuration);
			transformer.transform(source, result);
		}
		catch (Exception e) { return false; }
		return true;
	}
}
