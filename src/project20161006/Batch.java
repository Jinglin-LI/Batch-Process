package project20161006;

import java.util.LinkedHashMap;
import java.util.Map;


public class Batch
{
    private Map<String, Command> commandMap;
    private Command localCmd;
    private String localKey;
    
    public Batch(){	
    	commandMap = new LinkedHashMap<String, Command>();
    }

    public void addCommand(String key, Command command)
    {	
    	
    	localCmd = command;
    	localKey = key;
    	commandMap.put(localKey,localCmd);    
    }

    public Map<String, Command> getCommands() {
    			  	
    	return commandMap;
    }


}
