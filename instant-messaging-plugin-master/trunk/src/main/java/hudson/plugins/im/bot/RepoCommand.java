package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.Sender;
import hudson.FilePath;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import jenkins.model.Jenkins;

@Extension
public class RepoCommand extends AbstractSourceQueryCommand {

	private static final String SYNTAX = " [-url|-changed]";
	private static final String HELP = SYNTAX + " - show the list of the changed files";
	public final String REPO = "repo";
	
	@Override
	public Collection<String> getCommandNames() {
		return Collections.singleton("repo");
	}

	@Override
	protected CharSequence getMessageForJob(Collection<AbstractProject<?, ?>> projects, String[] args) {
		//StringBuilder msg = new StringBuilder(32);
		Collection<AbstractBuild<?,?>> builds = new ArrayList<AbstractBuild<?,?>>();
		
		// commit authors and messages
//		AbstractBuild<?, ?> lastBuild = projects.getLastBuild();
//		for (Entry entry : lastBuild.getChangeSet()) {
//			msg.append("* " + entry.getAuthor()).append(": ").append(entry.getMsg());
//		}
		
		// get all builds
        // if this command is slow may need to put limiter on while loop
        
        for (AbstractProject<?, ?> abProj: projects) {
            AbstractBuild<?,?> tempbuild = abProj.getLastBuild();
            while(tempbuild != null){
                builds.add(tempbuild);
                tempbuild = tempbuild.getPreviousBuild();
            }
        }
		
        StringBuilder msg = new StringBuilder(builds.size());
        
        for (AbstractBuild<?, ?> abBuild: builds) {
            msg.append("Building #: ").append(abBuild.getNumber());
            //.append(" (")
              //  .append(abBuild.getTimestampString()).append(" ago): ").append(abBuild.getResult())
            
            //    .append(System.getProperty("line.separator"));
         //try to get the repourl of build;
            FilePath[] fpp = abBuild.getModuleRoots();
            for (FilePath fp : fpp){
            	msg.append(fp.toString());//^^
            }
            
            for (Entry entry : abBuild.getChangeSet()) {
            	msg.append(entry.getCommitId()); //**
            	msg.append(entry.getAffectedPaths());//^^
    			msg.append("* " + entry.getAuthor()).append(": ").append(entry.getMsg());
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
}
