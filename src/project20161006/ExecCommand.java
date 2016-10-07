package project20161006;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class ExecCommand extends Command {
	
	private String in;
	private String out;
	private String id;
	private String path;
	private ArrayList<String> args;
	private ArrayList<String> command;
	private ProcessBuilder processBuilder;
	private Process process;
	private File outFile;
	private File inFile;
	private Scanner sc;
	
	public ExecCommand() {
		
		in = new String();
		out = new String();
		id = new String();
		path = new String();
		args = new ArrayList<String>();
		command = new ArrayList<String>();
		processBuilder = new ProcessBuilder();
	}
	
	public void describe() {
		
		System.out.println("ExecCommand " +id.toString() + " Parsing started!");
		
		System.out.println("ID: " + id);
		
		if (!(args == null || args.isEmpty())){
			System.out.println("Args: " + args);
		}
		
		if (!(path == null || path.isEmpty())){
			System.out.println("Path: " + path);
		}
		
		if (!(in == null || in.isEmpty())) {
			System.out.println("inID: " + in);
		}
		
		if (!(out == null || out.isEmpty())) {
			System.out.println("outID: " + out);
		}
		
		if(!(outFile==null || outFile.toString().isEmpty()))
		{
			System.out.println("The output is saved in " + outFile.toString());
		}
		
		System.out.println("ExecCommand " +id.toString() + " Parsing terminated!");
		System.out.println();

	}
	
	// execute commands
	public void execute() throws IOException, InterruptedException {

		
		if(!(path.isEmpty()))
		{
			command.add(path);
		}
		
		if(!(args.isEmpty()))
		{
			for(String argument : this.args)
	        {
				command.add(argument);
	        }
			//command.add(args);

		}
		
		processBuilder.command(command);//.inheritIO();
		
		// set directory
		File workingDir = processBuilder.directory();	
		
		if(!(in.isEmpty()))
		{
			inFile = new File(workingDir,in);
			processBuilder.redirectInput(inFile);
		}

		if(!(out.isEmpty()))
		{
			outFile = new File(workingDir,out);
			processBuilder.redirectOutput(outFile);
		}
		
		process = processBuilder.start();
		process.waitFor();
		
		if(!(outFile==null || outFile.toString().isEmpty()))
		{
			System.out.println("The output is saved in " + outFile.toString());

		}
		System.out.println("ExecCommand " +id.toString() + " Executing terminated!");
		System.out.println();

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
			else if("args".equalsIgnoreCase(attrMap.item(i).getNodeName().toString())){
				String temp=attrMap.item(i).getNodeValue();
				sc = new Scanner(temp);
				while(sc.hasNext())
				{
					args.add(sc.next());
				}
			}
			else if("in".equalsIgnoreCase(attrMap.item(i).getNodeName().toString())){
				in = attrMap.item(i).getNodeValue();
			}
			else if("out".equalsIgnoreCase(attrMap.item(i).getNodeName().toString())){
				out = attrMap.item(i).getNodeValue();
			}
			else throw new ProcessException("Parsing unknown 'exec' node: " + attrMap.item(i).getNodeName().toString());
			
		}
		
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in Exec Command " + id.toString());
		}
		
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in Exec Command "+ id.toString());
		}
	
		describe();
	}


	public String getId() {

		return id;
	}
	
	public String getIn()
	{
		return in;
	}
	
	public String getOut()
	{
		return out;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public ArrayList<String> getArgs()
	{
		return args;
	}

	
	public void setIn(String infile)
	{
		in = infile;
	}
	
	public void setOut(String outfile)
	{
		out = outfile;
	}
	
	public String toString()
	{
		return ("id = "+ id+", in = " + in+", out = "+out+", args = "+args+ ", path = "+path);
	}
	
	
}
