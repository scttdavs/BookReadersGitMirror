package hudson.plugins.im.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.maven.AbstractMavenProject;
import hudson.model.BallColor;
import hudson.model.FreeStyleBuild;
import hudson.model.HealthReport;
import hudson.model.ItemGroup;
import hudson.model.Result;
import hudson.plugins.im.Sender;
import hudson.plugins.im.tools.MessageHelper;

import org.junit.Test;

public class OverviewCommandTest {
	
	private final Pattern percentagePattern = Pattern.compile("\\D(\\d+)[%]"); 
	
//	@Test
//	public void sampleTest() {
//		
//	}
	
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
		when(build.getUrl()).thenReturn("job/foo/32/");
		
		HealthReport healthMock = mock(HealthReport.class);
		when(healthMock.getDescription()).thenReturn("Fine");
		when(healthMock.getScore()).thenReturn(100);
		
		AbstractMavenProject job = mock(AbstractMavenProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(job.getParent()).thenReturn(parent);
        when(job.getFullDisplayName()).thenReturn("fsProject");
        when(job.getLastBuild()).thenReturn(build);
        when(job.getLastBuild().getNumber()).thenReturn(9);
        when(job.getLastBuild().getTimestampString()).thenReturn("10 min");
        when(job.getLastBuild().getUrl()).thenReturn("http://www.fakeurl.com");
        //System.out.println(MessageHelper.getBuildURL(job.getLastBuild()));
        
        Result result = Result.SUCCESS;
        when(job.getLastBuild().getResult()).thenReturn(result);
        
        when(job.getBuildHealth()).thenReturn(healthMock);		
		OverviewCommand cmd = new OverviewCommand();
		String reply = cmd.getMessageForJob(job).toString();
		
		assertFalse(reply.contains(AbstractMultipleJobCommand.UNKNOWN_JOB_STR));
		assertTrue(reply.contains("fsProject"));
		//System.out.println(reply);
		Matcher m = percentagePattern.matcher(reply);
		assertTrue(m.find());
		String match = m.group(1);
		assertEquals("100", match);
	}
}
