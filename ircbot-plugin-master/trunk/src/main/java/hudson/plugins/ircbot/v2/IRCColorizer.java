package hudson.plugins.ircbot.v2;

import hudson.model.ResultTrend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

import org.pircbotx.Colors;

/**
 * Simple support for IRC colors.
 * 
 * @author syl20bnr
 * @author kutzi
 */
// See http://flylib.com/books/en/4.74.1.47/1/ for some tips on IRC colors
public class IRCColorizer {
    
    /**
     * Very simple pattern to recognize test results.
     */
    private static final Pattern TEST_CLASS_PATTERN = Pattern.compile(".*test.*", Pattern.CASE_INSENSITIVE);
    
    //e.g.userPattern[nickname][user_prefered_string_pattern]
    private static HashMap<String, HashMap<Pattern, String> > userPattern; //TODO
    // implement or find api for load/dump the HashMap to/from external file

    /**
     * Colorize the message line if certain keywords are found in it. 
     */
    public static String colorize(String message){
        
        if(message.contains("Starting ")) {
            return message;
        } else {
            String line = colorForBuildResult(message);
            if (line == message) { // line didn't contain a build result
                Matcher m = TEST_CLASS_PATTERN.matcher(message);
                if (m.matches()){
                    return Colors.BOLD + Colors.MAGENTA + line;
                }
            }
            return line;
        }
    }
    
    
    
    
    // set the user defined color for specific string,
    // will be called by onMessage() method in PircListener.java
    private void setter(String nickname, String pattern, String color) //TODO
    {
    	// compile the string pattern and store it in userPattern, pattern:color
    	// e.g. Pattern cPattern = Pattern.compile(".*pattern*", Pattern.CASE_INSENSITIVE);
    	userPattern.put(nickname, cPattern->color);
    }
    
    // set the message with user preference if the preference can be retrived in userPattern
    private String getUserString(String nickname, String message) //TODO
    {
    	String res;
    	// look up userPattern and extract the user pre-set pattern preference if exist
    	// if not exist, return original string
    	
    	
    	//if exisit, parse through and encode the output string based on the user pattern preference
    	return res;
    	
    }
    
    private static String colorForBuildResult(String line) {
        for (ResultTrend result : ResultTrend.values()) {
            
            String keyword = result.getID();
            
            int index = line.indexOf(keyword);
            if (index != -1) {
                final String color;
                switch (result) {
                    case FIXED: color = Colors.BOLD + Colors.UNDERLINE + Colors.GREEN; break;
                    case SUCCESS: color = Colors.BOLD + Colors.GREEN; break;
                    case FAILURE: color = Colors.BOLD + Colors.UNDERLINE + Colors.RED; break;
                    case STILL_FAILING: color = Colors.BOLD + Colors.RED; break;
                    case UNSTABLE: color = Colors.BOLD + Colors.UNDERLINE + Colors.BROWN; break;
                    case STILL_UNSTABLE: color = Colors.BOLD + Colors.BROWN; break;
                    case NOW_UNSTABLE: color = Colors.BOLD + Colors.MAGENTA; break;
                    case ABORTED: color = Colors.BOLD + Colors.LIGHT_GRAY; break;
                    default: return line;
                    // default: getUserString(String nickname, String line) // TODO
                }
                
                return line.substring(0, index) + color + keyword + Colors.NORMAL
                        + line.substring(index + keyword.length(), line.length());
            }
        }
        return line;
    }

}
