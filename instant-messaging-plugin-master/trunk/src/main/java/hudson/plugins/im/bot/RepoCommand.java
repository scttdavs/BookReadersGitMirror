package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;
import hudson.FilePath;
import hudson.scm.ChangeLogSet;
import hudson.scm.RepositoryBrowser;
import hudson.scm.SCM;
import hudson.scm.ChangeLogSet.Entry;
import jenkins.model.Jenkins;

@Extension
public class RepoCommand extends AbstractSourceQueryCommand {

	private static final String SYNTAX = " [show <int> | reponumber <int> | all]";
	private static final String HELP = SYNTAX + " - show the repository details of build history";
	public final String REPO = "repo";
	HashMap<String, Integer> cmd_list;
	static int number = 0;
	static int counter = 20;
    static int flag = 1;
	@Override
	public Collection<String> getCommandNames() {
		return Collections.singleton("repo");
	}
	
	@Override
	protected CharSequence getMessageForJob(Collection<AbstractProject<?, ?>> projects, String[] args) {
		
		if (cmd_list == null) {
			cmd_list = new HashMap<String, Integer>();

			cmd_list.put("show", 1);

			cmd_list.put("reponumber", 2);

			cmd_list.put("all", 3);
		}

		if (args.length == 1) {
			counter = 20;
			flag = 1;
		} else if (args.length >= 2) {
			int cmd_idx = cmd_list.get(args[1]) == null ? 0 : cmd_list.get(args[1]);
			switch (cmd_idx) {
			case 1:
				if (args.length == 2){
					counter=20;
					flag=1;
				}
				else{
					counter = Integer.parseInt(args[2]);
					flag=1;
				}
				break;
			case 2:
				number = Integer.parseInt(args[2]);
				flag=1;
				break;
			case 3:
				flag=0;
				break;
			default:
				return getAvailableCommand();
			}
		} 
		//StringBuilder msg = new StringBuilder(32);
		Collection<AbstractBuild<?,?>> builds = new ArrayList<AbstractBuild<?,?>>();
		
		// commit authors and messages
//		AbstractBuild<?, ?> lastBuild = projects.getLastBuild();
//		for (Entry entry : lastBuild.getChangeSet()) {
//			msg.append("* " + entry.getAuthor()).append(": ").append(entry.getMsg());
//		}
		
		// get all builds
        // if this command is slow may need to put limiter on while loop
		String model_root ="";
		
        for (AbstractProject<?, ?> abProj: projects) {
        	// test svn url
        	//SCM scm = abProj.getScm();
//        	RepositoryBrowser<?> repo_bowser = scm.getBrowser();
//        	descriptor_list = repo_bowser.all();
        	//String repo_bowser = scm.getBrowser().toString();
        	
        	//important
        	//model_root = scm.getModuleRoot(workspace, build);
        	//model_root = abProj.getModuleRoot().toString();
        	        	
            AbstractBuild<?,?> tempbuild = abProj.getLastBuild();
            while(tempbuild != null){
                builds.add(tempbuild);
                tempbuild = tempbuild.getPreviousBuild();
            }
        }
		
        StringBuilder msg = new StringBuilder(builds.size());

        for (AbstractBuild<?, ?> abBuild: builds) {
            msg.append("Building #: ").append(abBuild.getNumber()); // job number
            
            counter--;
            
            // temp counter and flag
            if (counter<=0 && flag==1){
            	//continue;
            	break;
            }
            
            //.append(" (")
              //  .append(abBuild.getTimestampString()).append(" ago): ").append(abBuild.getResult())
            
            //    .append(System.getProperty("line.separator"));
         //try to get the repourl of build;
//            FilePath[] fpp = abBuild.getModuleRoots();
//            for (FilePath fp : fpp){
//            	msg.append(fp.toString());//^^
//            }
            
            // test svn url
//            msg.append(descriptor_list.toString());
                                   
            for (Entry entry : abBuild.getChangeSet()) {
            	msg.append("\nRevision:"+entry.getCommitId()); // version number
            	msg.append(entry.getAffectedPaths());// a list of changed file
            	//msg.append(entry.getAffectedFiles());// ?
            	msg.append("\nmodel_root:"+model_root);
            	msg.append("\nrootDir:"+entry.getParent().getRun().getRootDir().toString());
            	msg.append("\ngetUrl:"+entry.getParent().getRun().getUrl());
            	//msg.append("\nsearchUrl"+entry.getParent().getRun().getSearchUrl());
            	msg.append("\nBuildStatusUrl:"+entry.getParent().getRun().getBuildStatusUrl());
            	
            	
    			msg.append("\n* " + entry.getAuthor()).append(": ").append(entry.getMsg());
    		}
            msg.append(System.getProperty("line.separator"));
        }
        return msg;
	}

	@Override
	protected String getCommandShortName() {
		return REPO;
	}

	@Override
	public String getHelp() {
		return HELP;
	}
	
	private String getAvailableCommand() {
		StringBuilder buf = new StringBuilder();
		buf.append("Available Command: ");
		buf.append("show [how many last builds wanted], ");
		buf.append("reponumber [the repository number for search], ");
		buf.append("all(be careful to choose, it will take a long time to run) ");
		return buf.substring(0, buf.length() - 1);
	}
}
