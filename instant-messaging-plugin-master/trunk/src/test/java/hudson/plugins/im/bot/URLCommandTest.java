package hudson.plugins.im.bot;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import hudson.plugins.im.Sender;

public class URLCommandTest {

	private Pattern pattern = Pattern.compile("https?://[^/]*?/(([^/]*/)*)");
	private String getPath ( String url ) {
		Matcher m = pattern.matcher(url); 
		if (m.find()) {
            return m.group(1);
        } else {
            return "Not A valid URL!!";
        }
	}
	
	@Test
	public void testGetGlobalLog() {
		Sender sender = new Sender("tester");
		String[] args = { "geturl", "log" };
		
		URLCommand cmd = new URLCommand();
		String reply = cmd.getReply(null, sender, args);
		
		assertEquals(getPath(reply),"log/");
	}
	
	@Test
	public void testGetGlobalConfigure() {
		Sender sender = new Sender("tester");
		URLCommand cmd = new URLCommand();
		
		String[] args0 = { "geturl", "conf" };
		String reply = cmd.getReply(null, sender, args0);
		assertEquals(getPath(reply),"configure/");
		
		String[] args1 = { "geturl", "configure" };
		reply = cmd.getReply(null, sender, args1);
		assertEquals(getPath(reply),"configure/");
	}
	
	@Test
	public void testGetRootURL() {
		Sender sender = new Sender("tester");
		URLCommand cmd = new URLCommand();
		
		String[] args0 = { "geturl", "base" };
		String reply = cmd.getReply(null, sender, args0);
		assertEquals(getPath(reply),"");
		
		String[] args1 = { "geturl", "root" };
		reply = cmd.getReply(null, sender, args1);
		assertEquals(getPath(reply),"");
	}
	
	@Test
	public void testGetDefaultURL() {
		Sender sender = new Sender("tester");
		URLCommand cmd = new URLCommand();
		
		String[] args0 = { "geturl"};
		String reply = cmd.getReply(null, sender, args0);
		assertEquals(getPath(reply),"");
	}
	
	@Test
	public void testGetURLNotFound() {
		Sender sender = new Sender("tester");
		URLCommand cmd = new URLCommand();
		
		String[] args0 = { "geturl","This is some thing weird!"};
		String reply = cmd.getReply(null, sender, args0);
		assertTrue(reply.startsWith("Available Command:"));
	}
}
