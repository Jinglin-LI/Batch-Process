package project20161006;

/**
 * Operating System Project 1: Batch Project
 */
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
  
 
/**
 * @author nana.xu
 * This is the main class which drives both the parsing of the batch file into commands and 
 * the execution of those commands
 *
 */
public class BatchProcessor {
			
	/* create BatchProcessor */
	static BatchProcessor batchProcessor = new BatchProcessor();

	/**
	 * the main function 
	 * 1)creates BatchParser;
	 * 2)get Batch object returned by BatchParser function BuildBatch(batchFile:File)
	 * 3)execute Batch with function executeBatch(batch:Batch)
	 * 4)creates ProcessException and catches it where needed.
	 * @throws Exception 
	 */	
	public static void main(String[] args) throws Exception{
		
	   String[] fileString = args;
	   for(int i = 0; i < fileString.length; i ++)
	   {
		   File file = new File(fileString[i]);
		   batchProcessor.executeFile(file);
	   }

	   /* file batch1.xml~batch5.xml */
//	   File file1 = new File("batch1.xml");		
//	   File file2 = new File("batch2.xml");	
//	   File file3 = new File("batch3.xml");	
//       File file4 = new File("batch4.xml");	
//	   File file5 = new File("batch5.xml");
		
	   /* execute batch files batch1.xml~batch5.xml */	
//	   batchProcessor.executeFile(file1);
//	   batchProcessor.executeFile(file2);
//	   batchProcessor.executeFile(file3);
//	   batchProcessor.executeFile(file4);
//	   batchProcessor.executeFile(file5);	

	}

	
	/**
	 * the file execution function 
	 * 1)build batch;
	 * 2)build commands to batch
	 * 3)execute batch
	 * @throws Exception 
	 */		
	private void executeFile(File file) throws Exception
	{
		System.out.println("Executing " + file.toString() + " Started");
		System.out.println();
		
		/* create BatchParser */
		BatchParser batchParser = new BatchParser();
		
		/* build batch with file batch.xml */
		File localFile = file;
		
		try{
			
			/* build batch */	
			Batch localBatch = batchParser.buildBatch(localFile);

			/* execute batch */	
			batchProcessor.executeBatch(localBatch);
			
		}catch (ProcessException e)
		{
			throw new Exception(e.getMessage() + " of file " + localFile.getName());
		}
	
		
		/* batch excuting finished*/
		System.out.println("Executing " + file.toString() +" Terminated!");
		System.out.println();
	}	
	
	/**
	 * the batch execution function 
	 * 1)get Commands
	 * 2)describe commands
	 * 2)execute Commands
	 * @throws Exception 
	 */		
	private void executeBatch(Batch batch) throws Exception
	{		
		/* receive the Batch passed in */			
		Batch localBatch = batch;	
		
		// split batch into 3 different maps according to commands
		Map<String, FilenameCommand> filenameMap = new LinkedHashMap<String, FilenameCommand>();
		
		Map<String, ExecCommand> execMap = new LinkedHashMap<String, ExecCommand>();
		
		Map<String, PipecmdCommand> pipecmdMap = new LinkedHashMap<String, PipecmdCommand>();
		
		
		/* get all the commands in batch file */
		Map<String, Command> commandlistMap = localBatch.getCommands();
			
		/* iterate all map nodes and store into 3 different Maps accordingly */
		for (Map.Entry<String, Command> commandlistEntry: commandlistMap.entrySet()) {
			
			// if FilenameCommand
			if(commandlistEntry.getKey().contains("file"))
			{
				// store into filenameMap
				filenameMap.put(commandlistEntry.getKey(), (FilenameCommand)commandlistEntry.getValue());
				
			}
			else if(commandlistEntry.getKey().contains("cmd"))
			{
				// store into execMap
				execMap.put(commandlistEntry.getKey(), (ExecCommand)commandlistEntry.getValue());
				
			}
			else if(commandlistEntry.getKey().contains("pipe"))
			{
				// store into pipecmdMap
				pipecmdMap.put(commandlistEntry.getKey(), (PipecmdCommand)commandlistEntry.getValue());
			}	
		}	
		
	
		// process exec commands here
		// for every command,go through filenameMap, get the match of in file and out file, then call execCommand execute()
		for (Map.Entry<String, ExecCommand> execCmdlistEntry : execMap.entrySet()) {
			
			// get "in" and "out" values of exec command
			String in = execCmdlistEntry.getValue().getIn();
			
			String out = execCmdlistEntry.getValue().getOut();		
			
			// iterate filenameMap to find the match of in file and out file and set them into execCommand
			for (Map.Entry<String, FilenameCommand> filenameCmdlistEntry : filenameMap.entrySet()) {
				
				if(filenameCmdlistEntry.getValue().getId().equalsIgnoreCase(in))
				{
					// set in file to real file name
					execCmdlistEntry.getValue().setIn(filenameCmdlistEntry.getValue().getPath());
				}
				else if(filenameCmdlistEntry.getValue().getId().equalsIgnoreCase(out))
				{
					// set out file to real file name
					execCmdlistEntry.getValue().setOut(filenameCmdlistEntry.getValue().getPath());
				}				
			}
			
			// after set in and out file, execute command by itself
			execCmdlistEntry.getValue().execute();		
		}
		
		// process pipe commands here
		for (Map.Entry<String, PipecmdCommand> pipeCmdlistEntry : pipecmdMap.entrySet()) {
			
			// get in and out from child execCommands
			String in = pipeCmdlistEntry.getValue().getExecCommandIn().getIn();
			String out = pipeCmdlistEntry.getValue().getExecCommandOut().getOut();
			
			// iterate filenameMap to find the match of in file and out file and set them
			for (Map.Entry<String, FilenameCommand> filenameCmdlistEntry : filenameMap.entrySet()) {
				
				if(filenameCmdlistEntry.getValue().getId().equalsIgnoreCase(in))
				{
					// set in file to real file name
					pipeCmdlistEntry.getValue().getExecCommandIn().setIn(filenameCmdlistEntry.getValue().getPath());
				}
				else if(filenameCmdlistEntry.getValue().getId().equalsIgnoreCase(out))
				{
					// set out file to real file name
					pipeCmdlistEntry.getValue().getExecCommandOut().setOut(filenameCmdlistEntry.getValue().getPath());
				}	
				
			}
			
			// after match in and out file, execute command by itself
			pipeCmdlistEntry.getValue().execute();	

		}
		
	}

}
