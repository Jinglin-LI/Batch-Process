package project20161006;


import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class FilenameCommand extends Command{
	
	private String id;
	private String path;
	
	public FilenameCommand() {
		
	}
	

	
	public void execute() throws Exception {
		

	}
	
	
	public void parse(Element element) throws ProcessException {
		
		Element localElement = element;

		NamedNodeMap attrMap = localElement.getAttributes();
		
		for(int i = 0; i < attrMap.getLength(); i++)
		{
			if("id".equalsIgnoreCase(attrMap.item(i).getNodeName().toString()))
			{
				id = attrMap.item(i).getNodeValue();
			}
			else if("path".equalsIgnoreCase(attrMap.item(i).getNodeName().toString()))
			{
				path = attrMap.item(i).getNodeValue();
			}
			else throw new ProcessException("Parsing unknown 'filename' node: " + attrMap.item(i).getNodeName().toString());
		}
		
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in node 'file' ");
		}
	
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in node 'file' ");
		}
		
		describe();

	}



	@Override
	public void describe() {

		System.out.println("FilenameCommand " +id.toString() + " parsing started!");
		System.out.println("ID: " + id);
		System.out.println("Path: " + path);
		System.out.println("FilenameCommand " +id.toString() + " parsing terminated!");
		System.out.println();
		
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getPath()
	{
		return path;
	}
	

}
