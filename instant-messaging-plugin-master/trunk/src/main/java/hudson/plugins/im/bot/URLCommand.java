package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import hudson.Extension;
import hudson.plugins.im.Sender;
import jenkins.model.Jenkins;

@Extension
public class URLCommand extends AbstractTextSendingCommand {

    private static final String SYNTAX = "<page>";
    private static final String HELP = SYNTAX + " - retrieve the spesific URL for Jenkins";

    HashMap<String, Integer> cmd_list;

    @Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("geturl");
    }

    private String getBaseURL(){
    	String rootURL = "http://host:port/";
    	try {
    		rootURL = Jenkins.getInstance().getRootUrl();
    	} catch ( NullPointerException e) {
//    		System.out.println("Jenkins Not Found!!");
    	}
    	if ( !rootURL.endsWith("/") ) {
			rootURL += "/";
		}
    	return rootURL;
    }
    private String getGlobalConfigureURL(){
    	return getBaseURL()+"configure/";
    }
    private String getGlobalSystemLogURL(){
    	return getBaseURL()+"log/";
    }
    private String getAvailableCommand()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("Available Command: ");
        for ( String key : cmd_list.keySet() )
        {
            buf.append(key);
            buf.append(", ");
        }
        return buf.substring(0, buf.length() - 2);
    }
    @Override
	protected String getReply(Bot bot, Sender sender, String[] args) {
        if(cmd_list == null)
        {
            cmd_list = new HashMap<String,Integer>();
            
            cmd_list.put("base", 1);
            cmd_list.put("root", 1);
            
            cmd_list.put("configure", 2);
            cmd_list.put("conf", 2);
            
            cmd_list.put("log", 3);

        }
        
    	if ( args.length == 1  ) {
    		return getBaseURL();
    	} 
        else if (args.length == 2)
        {
            int cmd_idx = cmd_list.get(args[1]) == null ? 0 : cmd_list.get(args[1]);
            switch(cmd_idx)
            {
                case 1: return getBaseURL();   
                case 2: return getGlobalConfigureURL();
                case 3: return getGlobalSystemLogURL();
                default: return getAvailableCommand(); 
            }
    	} else {
    		return giveSyntax(sender.getNickname(), args[0]);
    	}
	} 

    @Override
	public String getHelp() {
		return HELP;
	}

	private String giveSyntax(String sender, String cmd) {
		return sender + ": syntax is: '" + cmd +  SYNTAX + "'";
	}

}
