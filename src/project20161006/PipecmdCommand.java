package project20161006;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class PipecmdCommand extends Command {

	private String id;
	private ExecCommand execCommandIn;
	private ExecCommand execCommandOut;
	private String pipeInId;
	private String pipeInPath;
	private ArrayList<String> pipeInArgs;
	private String pipeInIn;
	private String pipeOutId;
	private String pipeOutPath;
	private ArrayList<String> pipeOutArgs;
	private String pipeOutOut;
	private List<String> command1;
	private List<String> command2;
	private File inFile;
	private File outFile;


	public PipecmdCommand() {

		// initialize pipe in command and pipe out command
		execCommandIn = new ExecCommand();
		execCommandOut = new ExecCommand();

	}

	public void describe() {

		// completed in subnodes exec commands parsing
	}

	public void execute() throws ProcessException, IOException {

		// create pipe in command
		command1 = new ArrayList<String>();
		command1.add(pipeInPath);
		for(String argument : this.pipeInArgs)
        {
			command1.add(argument);
        }
		
		// create pipe out command
		command2 = new ArrayList<String>();
		command2.add(pipeOutPath);
		for(String argument : this.pipeOutArgs)
        {
			command2.add(argument);
        }

		ProcessBuilder builder1 = new ProcessBuilder(command1);
		ProcessBuilder builder2 = new ProcessBuilder(command2);
		
		// Set pipeInIn and pipeOutOut using execCommandIn and execCommandOut
		// On parsing, pipeInIn and pipeOutOut is set to the original value from the command,
		// i.e. 'file1' and 'file2', so does the in&out in the execCommand In&Out.
		// Before entering the execute function, the execCommand In&Out has been modified
		// so that the execCommandIn.in and execCommandOut.out is set to the actual file path
		// needed. 
		// Thus, resetting the value is need. 
		pipeInIn = execCommandIn.getIn();
		pipeOutOut = execCommandOut.getOut();

		File workingDir1 = builder1.directory();
		File workingDir2 = builder2.directory();
		
		// Set first command input file.
		if(!(pipeInIn.isEmpty()))
		{
			inFile = new File(workingDir1,pipeInIn);
			builder1.redirectInput(inFile);
		}
		
		// Set second command output file.
		if(!(pipeOutOut.isEmpty()))
		{
			outFile = new File(workingDir2,pipeOutOut);
			builder2.redirectOutput(outFile);
		}

		System.out.println("Pipe Command "+ id.toString() +" Executing started!");

		// start process for both command.
		final Process process1 = builder1.start();
		final Process process2 = builder2.start();

		// process1.getInputStream() will returns the input stream connected to the
		// normal output of the process1, i.e. the output stream of process1.
		InputStream is = process1.getInputStream();
		
		// process2.getOutputStream() will returns the output stream connected to the
		// normal input of the process2. i.e. the input stream of process2
		OutputStream os = process2.getOutputStream();

		// copy the output of process1 to the input of process2, i.e. the pipe command.
		copyStreams(is, os);
			
		System.out.println("Pipe Command "+ id.toString() +" Executing terminated!");
	}

	public void parse(Element element) throws ProcessException {

		Element localElement = element;

		if(!localElement.getNodeName().equalsIgnoreCase("pipecmd"))
		{
			throw new ProcessException("Parsing unknown 'pipe' node: " + localElement.getNodeName().toString());
		}

		id = localElement.getAttribute("id");

		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in Pipe Command: " + id.toString());
		}

		System.out.println("PipeCommand " +id.toString() + " Parsing started!");

		NodeList attrMap = localElement.getElementsByTagName("exec");

		for(int i = 0; i < attrMap.getLength(); i++)
		{

			if(!(((Element)attrMap.item(i)).getAttribute("in") == null || ((Element)attrMap.item(i)).getAttribute("in").isEmpty()))
			{
					execCommandIn.parse((Element)attrMap.item(i));
			}
			else if(!(((Element)attrMap.item(i)).getAttribute("out") == null || ((Element)attrMap.item(i)).getAttribute("out").isEmpty()))
			{
					execCommandOut.parse((Element)attrMap.item(i));
			}
		}

		System.out.println("PipeCommand " +id.toString() + " Parsing terminated!");


		// get pipe in and out command parameters
		pipeInId = execCommandIn.getId();

		if (pipeInId == null || pipeInId.isEmpty()) {
			throw new ProcessException("Missing id in Pipe In Command");
		}

		pipeInPath = execCommandIn.getPath();
		if (pipeInPath == null || pipeInPath.isEmpty()) {
			throw new ProcessException("Missing path in Pipe In Command");
		}

		pipeInArgs = execCommandIn.getArgs();
		if (pipeInArgs == null || pipeInArgs.isEmpty()) {
			throw new ProcessException("Missing args in Pipe In Command");
		}

		pipeOutId = execCommandOut.getId();
		if (pipeOutId == null || pipeOutId.isEmpty()) {
			throw new ProcessException("Missing id in Pipe Out Command");
		}

		pipeOutPath = execCommandOut.getPath();
		if (pipeOutPath == null || pipeOutPath.isEmpty()) {
			throw new ProcessException("Missing path in Pipe Out Command");
		}

		pipeOutArgs = execCommandOut.getArgs();
		if (pipeOutArgs == null || pipeOutArgs.isEmpty()) {
			throw new ProcessException("Missing args in Pipe Out Command");
		}

		// read in in file and out file first
		pipeInIn = execCommandIn.getIn();
		if (pipeInIn == null || pipeInIn.isEmpty()) {
			throw new ProcessException("Missing In file in Pipe Out Command");
		}

		pipeOutOut = execCommandOut.getOut();
		if (pipeOutOut == null || pipeOutOut.isEmpty()) {
			throw new ProcessException("Missing Out file in Pipe Out Command");
		}

	}


	public String getId() {

		return id;
	}

	public ExecCommand getExecCommandIn() {

		return execCommandIn;
	}

	public ExecCommand getExecCommandOut() {

		return execCommandOut;
	}

	/*public String toString()
	{
		return ("id = "+ id +
			",\npipeInId = " + pipeInId +
			",\npipeInPath = "+pipeInPath+
			",\npipeInArgs = "+pipeInArgs+
			",\npipeInIn = "+pipeInIn+
			",\npipeOutId = "+pipeOutId+
			",\npipeOutPath = "+pipeOutPath+
			",\npipeOutArgs = "+pipeOutArgs+
			",\npipeOutOut = "+pipeOutOut+
			",\ncommand1 = "+command1+
			",\ncommand2 = "+command2+
			",\ninFile = "+inFile+
			",\noutFile = "+outFile
			);
	}*/

	void copyStreams(final InputStream is, final OutputStream os) {
		Runnable copyThread = (new Runnable() {
			@Override
			public void run()
			{
				try {
					int achar;
					while ((achar = is.read()) != -1) {
						os.write(achar);
					}
					os.close();
				}
				catch (IOException ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		});
		new Thread(copyThread).start();
	}

}
