package hudson.plugins.im.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.maven.AbstractMavenProject;
import hudson.model.BallColor;
import hudson.model.FreeStyleBuild;
import hudson.model.HealthReport;
import hudson.model.ItemGroup;
import hudson.model.Result;
import hudson.model.User;
import hudson.plugins.im.Sender;
import hudson.plugins.im.tools.MessageHelper;
import hudson.scm.ChangeLogSet;

import org.junit.Test;

public class OverviewCommandTest {
	
	private final Pattern percentagePattern = Pattern.compile("\\D(\\d+)[%]"); 
	
	@Test
	public void testNoJobFound() {
	    JobProvider jobProvider = mock(JobProvider.class);
		OverviewCommand cmd = new OverviewCommand();
		cmd.setJobProvider(jobProvider);
		
		Sender sender = new Sender("tester");
		String[] args = {"overview"};
		String reply = cmd.getReply(null, sender, args);
		
		assertEquals(sender + ": no job found", reply);
	}
	
	@Test
	public void testOverview() throws Exception {
		
		FreeStyleBuild build = mock(FreeStyleBuild.class);
		when(build.getUrl()).thenReturn("http://www.fakeurl.com");
		
		HealthReport healthMock = mock(HealthReport.class);
		when(healthMock.getDescription()).thenReturn("Fine");
		when(healthMock.getScore()).thenReturn(100);
		
		AbstractMavenProject job = mock(AbstractMavenProject.class);
		
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(job.getParent()).thenReturn(parent);
        when(job.getFullDisplayName()).thenReturn("fsProject");
        when(job.getLastBuild()).thenReturn(build);
        
        User user = mock(User.class);
		when(user.toString()).thenReturn("Batman");
        ChangeLogSet.Entry item = mock(ChangeLogSet.Entry.class);
		Object[] items = new Object[1];
		items[0] = item;
		when(item.getAuthor()).thenReturn(user);
		when(item.getMsg()).thenReturn("Batman to the rescue!");
		ChangeLogSet changeSet = mock(ChangeLogSet.class);
		
		Iterator<ChangeLogSet.Entry> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(iterator.next()).thenReturn(item);
        
        when(changeSet.isEmptySet()).thenReturn(false);
        when(changeSet.iterator()).thenReturn(iterator);
        
		//when(changeSet.getItems()).thenReturn(items);
		when(build.getChangeSet()).thenReturn(changeSet);		
		
        when(build.getNumber()).thenReturn(9);
        when(build.getTimestampString()).thenReturn("10 min");
        
        // same as line 49 <2015/10/27>
        //when(job.getLastBuild().getUrl()).thenReturn("http://www.fakeurl.com");
        
        Result result = Result.SUCCESS;
        when(build.getResult()).thenReturn(result);
        
        when(job.getBuildHealth()).thenReturn(healthMock);		
		OverviewCommand cmd = new OverviewCommand();
		String reply = cmd.getMessageForJob(job).toString();
		
		assertFalse(reply.contains(AbstractMultipleJobCommand.UNKNOWN_JOB_STR));
		assertTrue(reply.contains("fsProject"));
		System.out.println(reply);
		Matcher m = percentagePattern.matcher(reply);
		assertTrue(m.find());
		String match = m.group(1);
		assertEquals("100", match);
	}
	
//	@Test
//	public void testFailure() throws Exception {
//		
//	}
}
