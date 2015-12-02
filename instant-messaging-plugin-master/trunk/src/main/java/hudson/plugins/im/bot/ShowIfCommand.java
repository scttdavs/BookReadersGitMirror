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
import java.util.Date;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import hudson.plugins.im.tools.ExceptionHelper;
import java.text.DateFormat;
import java.text.ParseException;

import java.lang.Integer;
/**
 * @author BookReaders CS 427 Group UIUC
 */


@Extension
public class ShowIfCommand extends AbstractSourceQueryCommand {
    private static final Logger LOGGER  = Logger.getLogger(ShowIfCommand.class.getName());
    @Override
    public Collection<String> getCommandNames() {
        System.out.println("[CHECK] getCommandNames() was reached in showIf\n");
        return Arrays.asList("showIf","si");
    }

    protected ArrayList<ArrayList<String>> queryGovernor(ArrayList<ArrayList<String>> list) {
        /* add a governor to the query output if none are specified */
    	boolean flag = false;
    	for (int i = 0; i < list.size(); i++) {
            ArrayList<String> queries = list.get(i);
            for(int j = 0; j < queries.size(); j++) {
                if(queries.get(j).trim().equals("build")) {
                    flag = true;
                    break;
                }
            }
            if(flag) { 
                break; 
            }
    	}

    	if (!flag) {
    		ArrayList<String> temp = new ArrayList<String>();
    		temp.add("build");
    		temp.add("10");
    		list.add(temp);
    	}

        return list;
    }

    private CharSequence distroToFilter(ArrayList<ArrayList<String>> list, String[] args, Collection<AbstractBuild<?,?>> builds) {
        
        StringBuilder msg = new StringBuilder(builds.size());
        
        /* govern the query */
        int init_query_size = list.size();
        this.queryGovernor(list);
        int governed_query_size = list.size();

        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> query = list.get(i);
            String query_type = query.get(0);

            // apply showif filters here
            if(!checkQuery(query, query_type)){
                msg.append("Malformed "+query_type+" Command!\n");
                System.out.println("Malformed "+query_type+" Command!\n");
                continue;
            }
            
            builds = this.runQuery(query, query_type , builds);

            if (builds.isEmpty()) {
                StringBuilder temp = new StringBuilder(32);
                temp.append("No Builds Found! \n");
                return temp;
            }
        }
        for (AbstractBuild<?, ?> abBuild: builds) {
            msg.append("last build: ").append(abBuild.getNumber()).append(" (")
                .append(abBuild.getTimestampString()).append(" ago): ").append(abBuild.getResult())
                //.append(": ").append(MessageHelper.getBuildURL(abBuild))
                .append(System.getProperty("line.separator"));
        }

        if(init_query_size != governed_query_size) {
            msg.append("\nOUTPUT IS GOVERNED TO 10 ITEMS!\n");
        }


        return msg;
    }

    protected boolean malformed_test(ArrayList<String> query_terms, int size) {
        if (query_terms.size() <= size) { 
            return true; 
        } else { 
            return false; 
        }
    }

    protected boolean checkQuery(ArrayList<String> query, String query_type) {
        // each .get(i) call on the query is accessing a query argument
        switch (query_type) {
            case "user":
                if (malformed_test(query, 1)) {
                    return false;
                } 
                break;
            case "date":
                if (malformed_test(query, 2)) {
                    return false;
                } 
                break;
            case "project":
                if (malformed_test(query, 1)) {
                    return false;
                }
                break;
            case "build":
                if (malformed_test(query, 1)) {
                    return false;
                }
                break;
            case "jobs":
                if (malformed_test(query, 2)) {
                    return false;
                }
                break;
            default: // add evil message
                return false;
        }

        return true;
    }

    protected Collection<AbstractBuild<?,?>> runQuery(ArrayList<String> query, String query_type, Collection<AbstractBuild<?,?>> builds) {

        // each .get(i) call on the query is accessing a query argument
        switch (query_type) {
            case "user":
                if (malformed_test(query, 1)) {
                    return builds;
                } 
                builds = userFilter(builds,query.get(1));
                break;
            case "date":
                if (malformed_test(query, 2)) {
                    return builds;
                } 
                    builds = dateFilter(builds,query.get(2),query.get(1));
                break;
            case "project":
                if (malformed_test(query, 1)) {
                    return builds;
                }
                builds = projectFilter(builds,query.get(1));
                break;
            case "build":
                if (malformed_test(query, 1)) {
                    return builds;
                }
                builds = buildFilter(builds,query.get(1));
                break;
            case "jobs":
                if (malformed_test(query, 2)) {
                    return builds;
                }
                builds = jobsFilter(builds,query.get(2),query.get(1));
                break;
            default: // add evil message
                    return builds;
        }

        return builds;
    }


    @Override
    protected CharSequence getMessageForJob(Collection<AbstractProject<?, ?>> projects, String[] args) {
        // add a logger for args
        String log_msg = args.toString();
        LOGGER.warning(log_msg);

        String spacer = "|";
        int arg_len = args.length;
        
        LOGGER.warning("there are " + Integer.toString(arg_len) + " args items\n");

        // convert projects Collection to ArrayList
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        
        // Parse Query
        for (int i = 0; i < arg_len; i++) {
            if((!args[i].equals("showIf") && !args[i].equals("si")) && !args[i].equals("!jenkins") && !args[i].equals(spacer)){
                ArrayList<String> temp = new ArrayList<String>();
                for (; i < arg_len && !args[i].equals(spacer) && !args[i].equals("showIf") && !args[i].equals("si"); ++i) {
                    temp.add(args[i]);
                }
                list.add(temp);
            }
        }

        // get all builds
        // if this command is slow may need to put limiter on while loop
        Collection<AbstractBuild<?,?>> builds = new ArrayList<AbstractBuild<?,?>>();
        for (AbstractProject<?, ?> abProj: projects) {
            AbstractBuild<?,?> tempbuild = abProj.getLastBuild();
            while(tempbuild != null){
                builds.add(tempbuild);
                tempbuild = tempbuild.getPreviousBuild();
            }
        }

        return distroToFilter( list,args, builds);
    }



    @Override
    protected String getCommandShortName() {
        return "detailed history";
    }

    // sub query priavate funcitons
    private Collection<AbstractBuild<?, ?>> userFilter(Collection<AbstractBuild<?, ?>> builds, String username) {
        return new GetUserHistory(builds, username);
    }
    
    private Collection<AbstractBuild<?, ?>> dateFilter(Collection<AbstractBuild<?,?>> builds, String dt, String op) {
         // *.getTime() might be a thing
        Iterator<AbstractBuild<?,?>> it = builds.iterator();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd-HH-mm");
        long date = 0;
        try{
            date = (format.parse(dt)).getTime();
        }
        catch(ParseException e) {
            LOGGER.warning(ExceptionHelper.dump(e));
        }
        while(it.hasNext()) {
            AbstractBuild<?,?> item = it.next();
            long temp = (item.getTimestamp()).getTimeInMillis();
            switch(op) {
                case "<":
                    if (!(temp < date)) {
                        it.remove();
                    }
                    break;
                case ">":
                    if (!(temp > date)) {
                        it.remove();
                    }
                    break;
                case "=":
                    if (!(temp + 60000 > date && temp - 60000 < date)) {
                        it.remove();
                    }
                    break;
                default:
                    LOGGER.warning("WARNING: Bad operator passed into date filter for ShowIfCommand");
                    break;
            }
        }
        return builds;
    }
    
    private Collection<AbstractBuild<?, ?>> jobsFilter(Collection<AbstractBuild<?,?>> builds, String job, String op) {
        Iterator<AbstractBuild<?,?>> it = builds.iterator();
        int job_num = Integer.parseInt(job);
        while(it.hasNext()) {
            AbstractBuild<?,?> item = it.next();
            int temp = item.getNumber();
            switch(op) {
                case "<":
                    if (!(temp < job_num)) {
                        it.remove();
                    }
                    break;
                case ">":
                    if (!(temp > job_num)) {
                        it.remove();
                    }
                    break;
                case "=":
                    if (!(temp == job_num)) {
                        it.remove();
                    }
                    break;
                default:
                    LOGGER.warning("WARNING: Bad operator passed into job filter for ShowIfCommand");
                    break;
            }
        }
        return builds;
    }

    private Collection<AbstractBuild<?, ?>> projectFilter(Collection<AbstractBuild<?,?>> builds, String projectName) {
        Iterator<AbstractBuild<?,?>> it = builds.iterator(); // get an interator from the collection
        while(it.hasNext()) {
            AbstractBuild<?,?> item = it.next();
            if (!(item.getProject().getName().equals(projectName))) {
                it.remove();
            }
        }
        return builds;
    }
    
    private Collection<AbstractBuild<?, ?>> buildFilter(Collection<AbstractBuild<?,?>> builds, String n) {
        // parse the number to determine how many builds to return
        int number_of_recent_builds = (Integer.parseInt(n) < 0) ? -Integer.parseInt(n) : Integer.parseInt(n); // make sure the number is non-negative
        Iterator<AbstractBuild<?,?>> it = builds.iterator(); // get an interator from the collection
        while(it.hasNext()) {
            AbstractBuild<?,?> item = it.next();
            if (number_of_recent_builds <= 0) {  // get the first n builds
                try {
                    it.remove();  // remove all the rest
                } catch (IllegalStateException e) {
                    LOGGER.warning(ExceptionHelper.dump(e));
                };
            };
            number_of_recent_builds--; // decrementer of builds
        }
        // return truncated builds
        return builds;
    }
}
