/*
 * Created on Apr 22, 2007
 */
package hudson.plugins.im.bot;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.tools.MessageHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.*;


/**
 * @author BookReaders CS 427 Group UIUC
 */


@Extension
public class ShowIfCommand extends AbstractSourceQueryCommand {
    @Override
    public Collection<String> getCommandNames() {
        return Arrays.asList("showIf","si");
    }

    @Override
    protected CharSequence getMessageForJob(Collection<AbstractProject<?, ?>> projects, String[] args) {
        return new StringBuilder(32);
        // String spacer = "|";
        // int arg_len = args.length;
        
        // // convert projects Collection to ArrayList
        // ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        
        // // Parse Query
        // for (int i = 0; i < arg_len; i++) {
        //     if(!args[i].equals("showIf") && !args[i].equals(spacer)){
        //         ArrayList<String> temp = new ArrayList<String>();
        //         for (; !args[i].equals(spacer) && i < arg_len; i++) {
        //             temp.add(args[i]);
        //         }
        //         list.add(temp);
        //     }
        // }

        // // get all builds
        // // if this command is slow may need to put limiter on while loop
        // Collection<AbstractBuild<?,?>> builds = new ArrayList<AbstractBuild<?,?>>();
        // for (AbstractProject<?, ?> abProj: projects) {
        //     AbstractBuild<?,?> tempbuild = abProj.getLastBuild();
        //     while(tempbuild != null){
        //         builds.add(tempbuild);
        //         tempbuild = tempbuild.getPreviousBuild();
        //     }
        // }
        
        // // iterate over the query chunks
        // for (int i = 0; i < list.size(); i++) {
        //     switch ((list.get(i)).get(0)) {
        //         case "user":
        //             builds = userFilter(builds,(list.get(i)).get(1));
        //             break;
        //         case "date":
        //             builds = dateFilter(builds,(list.get(i)).get(2),(list.get(i)).get(1));
        //             break;
        //         case "project":
        //             builds = projectFilter(builds,(list.get(i)).get(1));
        //             break;
        //         case "build":
        //             builds = buildFilter(builds,(list.get(i)).get(1));
        //             break;
        //         case "jobs":
        //             builds = jobsFilter(builds,(list.get(i)).get(2),(list.get(i)).get(1));
        //             break;
        //         default: // add evil message
        //             break;
        //     }
        //     if (builds.isEmpty()) {
        //         StringBuilder temp = new StringBuilder(32);
        //         temp.append("No Builds Found! \n");
        //         return temp;
        //     }
        // }
        // StringBuilder msg = new StringBuilder(builds.size());
        // for (AbstractBuild<?, ?> abBuild: builds) {
        //     msg.append("last build: ").append(abBuild.getNumber()).append(" (")
        //         .append(abBuild.getTimestampString()).append(" ago): ").append(abBuild.getResult()).append(": ")
        //         .append(MessageHelper.getBuildURL(abBuild));
        // }
        // return msg;
    }



    @Override
    protected String getCommandShortName() {
        return "detailed history";
    }

    // // sub query priavate funcitons
    // private Collection<AbstractBuild<?, ?>> userFilter(Collection<AbstractBuild<?, ?>> builds, String username) {
    //     return builds;
    //     //return new GetUserHistory(builds, username);
    // }
    
    // private Collection<AbstractBuild<?, ?>> dateFilter(Collection<AbstractBuild<?,?>> builds, String dt, String op) {
    //      // *.getTime() might be a thing
    //     return builds;
    // }
    
    // private Collection<AbstractBuild<?, ?>> jobsFilter(Collection<AbstractBuild<?,?>> builds, String job, String op) {
    //     return builds;
    // }

    // private Collection<AbstractBuild<?, ?>> projectFilter(Collection<AbstractBuild<?,?>> builds, String projectName) {
    //     return builds;
    // }
    
    // private Collection<AbstractBuild<?, ?>> buildFilter(Collection<AbstractBuild<?,?>> builds, String n) {
    //     // parse the number to determine how many builds to return
    //     int number_of_recent_builds = (Integer.parseInt(n) < 0) ? -Integer.parseInt(n) : Integer.parseInt(n); // make sure the number is non-negative
    //     Iterator<AbstractBuild<?,?>> it = builds.iterator(); // get an interator from the collection

    //     while(it.hasNext()) {
    //         AbstractBuild<?,?> item = it.next();
    //         if (number_of_recent_builds <= 0) {  // get the first n builds
    //             try {
    //                 it.remove();  // remove all the rest
    //             } catch (IllegalStateException e) {
    //                 System.out.println("[ERROR] IllegalStateException from buildFilter\n");
    //             };
    //         };
    //         number_of_recent_builds--; // decrementer of builds
    //     }
    //     // return truncated builds
    //     return builds;
    // }
}
