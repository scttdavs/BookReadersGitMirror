package hudson.plugins.im.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;
import jenkins.model.Jenkins;
/*Author Zehao
 Yuhang Wang */


@Extension
public class OpenCommand extends AbstractTextSendingCommand {

	private static final String SYNTAX = "<ProjectName> <Path to Directory or File>";
	private static final String HELP = SYNTAX + " - show the content of directory or the url of file";


	@Override
	public Collection<String> getCommandNames() {
		return Collections.singleton("open");
	}

	@Override
	protected String getReply(Bot bot, Sender sender, String[] args) {
		if ( args.length == 1 ) {
			// Prompt all available Project Name
			StringBuilder res = new StringBuilder();
			res.append("Syntax: open <Project Name> <Path To Dir/File>\n");
			res.append("Available Project Name:\n");
			List<AbstractProject<?,?>> projs = getJobProvider().getTopLevelJobs();
			for ( AbstractProject<?,?> proj : projs ) {
				res.append( proj.getFullDisplayName() + " " );
			}
			return res.toString();
			
		} else if ( args.length < 4 ){
			// Show content of directory or URL of file
			// Find the proper project
			AbstractProject<?,?> proj = getJobProvider().getJobByNameOrDisplayName( args[1] );
			if ( proj != null ) {
				
				// Find the proper workspace
				FilePath rootDir = getWorkSpace( proj );
				
				// Workspace is not available
				if ( rootDir == null ) return "Cannot Get WorkSpace!";
				
				// Find the directory or file
				String path = (args.length < 3)?"":args[2];
				FilePath destination = new FilePath(rootDir, path);
				try {
					if ( destination.isDirectory() ) {
						// List the directory and file of workspace
						List<FilePath> subDirs = destination.list();
						StringBuilder res = new StringBuilder();
						for ( FilePath file : subDirs ) {
							if ( file.isDirectory() ) {
								res.append( "["+file.getName()+"] " );
							} else {
								res.append( file.getName() + " " );
							}
						}
						return res.toString();
					} else {
						// Grab and return the file content or URL
						if ( destination.length() < 1500 ) { // return file content
							BufferedReader in = new BufferedReader(new InputStreamReader( destination.read() ));
							String line = null;

							StringBuilder res = new StringBuilder();
							while((line = in.readLine()) != null) {
							    res.append(line+"\n");
							}
							return res.toString();
						} else { // return the URL to the file
							return getBaseURL()+"job/"+args[1]+"/ws/"+path;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return "Directory/File Not Found!!";
				}
				
            } else {
                return sender.getNickname() + ": unknown job '" + args[1] + "'";
            }
		} else {
			// Invalid Command
			return giveSyntax(sender.getNickname(),"open");
		}
	}

	@Override
	public String getHelp() {
		return HELP;
	}

	private String giveSyntax(String sender, String cmd) {
		return sender + ": syntax is: '" + cmd + SYNTAX + "'";
	}
	
	String getBaseURL() {
		return Jenkins.getInstance().getRootUrl();
	}
	FilePath getWorkSpace( AbstractProject<?,?> proj ) {
		return proj.getLastBuild().getWorkspace();
	}
}
