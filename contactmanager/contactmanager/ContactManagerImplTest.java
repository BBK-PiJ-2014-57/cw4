package contactmanager;

import java.io.File;

import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;
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

	private String noFile = "." + File.separator + "doesnotexist.txt";
	private String savedFile = "." + File.separator + "contacts.txt";
	
	/**
	 * Testing that a list for meetings is created.
	 */
	@Test
	public void ContactManagerMeetingListTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		assertNotNull(testCM.meetingList);
	}
	
	/**
	 * Testing that a Set for Contacts is created.
	 */
	@Test
	public void ContactManagerContactsSetTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		assertNotNull(testCM.contacts);
	}
}
