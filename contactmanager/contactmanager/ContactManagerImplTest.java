package contactmanager;

import java.io.File;

import org.junit.*;

import java.io.*;
import java.net.URISyntaxException;

import javax.print.DocFlavor.URL;

import static org.junit.Assert.*;
import java.net.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * JUnit tests for ContactManagerImplTest - tests the different functionality.
 * @author lewispalmer
 *
 */
public class ContactManagerImplTest {

	
	private final String dir = System.getProperty("user.dir");
	private String noFile = dir  + File.separator + "doesnotexist.txt";
	private String savedFile = dir + File.separator + "contacts.txt";
	
	/**
	 * Testing that a list for meetings is created.
	 */
	@Test
	public void ContactManagerMeetingListTest()
	{
		ContactManagerImpl testCM = new ContactManagerImpl();
		assertNotNull(testCM.getMeetingList());
	}
	
	/**
	 * Testing that a Set for Contacts is created.
	 */
	@Test
	public void ContactManagerContactsSetTest()
	{
		ContactManagerImpl testCM = new ContactManagerImpl();
		assertNotNull(testCM.getContacts());
	}
	
	/**
	 * Testing for error from Incorrect XML (two Meeting Lists) but still creates list.
	 */
	@Test
	public void ContactManagerTwoMeetingListsTest()
	{
		String testfileName = dir + File.separator + "twoMeetingLists.txt";
		File testfile = new File(testfileName);
		if(testfile.exists())
			testfile.length();
		testfile.renameTo(new File(savedFile));
		ContactManager testCM = new ContactManagerImpl();
		File errorFile = new File("." + File.separator + "error1.txt");
		assertTrue(errorFile.exists());
		errorFile.delete();
		testfile.renameTo(new File(testfileName));
	}
	
	@Test
	public void ContactManagerNoErrorTest()
	{
		String testFileName = "." + File.separator + "emptyList.txt";
		File testfile = new File(testFileName);
		testfile.renameTo(new File(savedFile));
		ContactManagerImpl testCM = new ContactManagerImpl();
		assertNotNull(testCM.getContacts());
	}
}
