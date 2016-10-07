package project20161006;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

public class BatchParser {
	
	private Batch buildBatch;
	
	public BatchParser()
	{
		buildBatch = new Batch();
	}
	

	public Batch buildBatch(File batchFile) throws ParserConfigurationException, SAXException, IOException, DOMException, ProcessException{

		File localFile = batchFile;
		
		/* parse the batch file here*/		
		FileInputStream fis = new FileInputStream(localFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fis);

		Element pnode = doc.getDocumentElement();
		NodeList nodes = pnode.getChildNodes();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node node = nodes.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				parseCommand(elem);
			}
		}
		return buildBatch;	
	}
	
	private void parseCommand(Element elem) throws ProcessException
	{
		Element localElement = elem;		
		String cmdName = localElement.getNodeName();
		
		FilenameCommand filenameCmd;
		ExecCommand execCmd;
		PipecmdCommand pipeCmd;
		
		if (cmdName == null) {
			
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("filename".equalsIgnoreCase(cmdName)) {

			filenameCmd = new FilenameCommand();
			filenameCmd.parse(localElement);
			buildBatch.addCommand(filenameCmd.getId(),filenameCmd); 
			
		}
		else if ("exec".equalsIgnoreCase(cmdName)) {

			execCmd = new ExecCommand();
			execCmd.parse(localElement);
			buildBatch.addCommand(execCmd.getId(),execCmd); 
			
		}
		else if ("pipecmd".equalsIgnoreCase(cmdName)) {

			pipeCmd = new PipecmdCommand();
			pipeCmd.parse(localElement);
			buildBatch.addCommand(pipeCmd.getId(),pipeCmd); 
			
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}		
	
}
