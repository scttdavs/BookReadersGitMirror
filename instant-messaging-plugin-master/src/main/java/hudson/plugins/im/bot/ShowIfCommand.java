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


/**
 * Job/project status command for the jabber bot
 * @author Pascal Bleser
 */


@Extension
public class ShowIfCommand extends AbstractSourceQueryCommand {
    @Override
    public Collection<String> getCommandNames() {
        return Arrays.asList("showIf","showif","si");
    }

    @Override
    protected CharSequence getMessageForJob(Collection<AbstractProject<?, ?>> projects, String[] args) {

        // Parse Query
        // Collection<String, String> (<user | date | job | build>), argument)
        //
        // builds = []
        // for (AbstractProject<?,?> project: projects) {
        //      get all builds and append to builds
        //

        // iterate over the projects
            // for sq in subQuery
            //      switch sq[0] <- modify builds inplace
            //      case user
            //          userFilter(builds, username) -> return build.username == username
            //      case date
            //          dateFilter(builds, date, op) -> return build.date op date
            //      case jobs
            //          jobFilter(builds, jobnum, op) -> return build.jobnum op jobnum
            //      case build
            //          buildFilter(builds, count) -> return recent count number of builds
            //      default  <- error message ?
            //
        
        // build output display to propagate over to IRC
        // StringBuilder msg = new StringBuilder(32);
        // msg.append("whatever\n")
    return new StringBuilder(32);
    }

    @Override
    protected String getCommandShortName() {
        return "detailed history";
    }

    // sub query priavate funcitons
    /*private Collection<AbstractBuild<?, ?>> userFilter(Collection<AbstractBuild<?,?>> builds, String username) {
     *
     *      retun new Collection<AbstractBuild<?,?>>();
     *}
     *
     *private Collection<AbstractBuild<?, ?>> dateFilter(Collection<AbstractBuild<?,?>> builds, Datetime dt, char op) {
     *     // *.getTime() might be a thing
     *      retun new Collection<AbstractBuild<?,?>>();
     *}
     *
     *private Collection<AbstractBuild<?, ?>> jobFilter(Collection<AbstractBuild<?,?>> builds, int job, char op) {
     *
     *      retun new Collection<AbstractBuild<?,?>>();
     *}
     *
     *private Collection<AbstractBuild<?, ?>> buildFilter(Collection<AbstractBuild<?,?>> builds, int n) {
     *      // truncate builds
     *
     *      retun new Collection<AbstractBuild<?,?>>();
     *}
     *
     */
}
