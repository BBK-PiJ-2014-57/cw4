package contactmanager;

import java.io.File;

import org.junit.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.io.*;
import java.net.URISyntaxException;

import javax.print.DocFlavor.URL;

import static org.junit.Assert.*;

import java.net.*;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * JUnit tests for ContactManagerImplTest - tests the different functionality.
 * @author lewispalmer
 *
 */
public class ContactManagerImplTest {

	
	private final String dir = System.getProperty("user.dir");
	private String noFile = dir  + File.separator + "doesnotexist.txt";
	private String savedFile = dir + File.separator + "contacts.txt";
	private String testFileName = dir + File.separator;
	private File testFile;
	private Set<Contact> testContacts;
	private Contact testContact;
	private Calendar testDate;
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
	 * Testing for error from Incorrect XML (two Meeting Lists).
	 */
	@Test
	public void ContactManagerTwoMeetingListsTest()
	{
		createTestFile("twoMeetingLists.txt");
		ContactManager testCM = new ContactManagerImpl();
		File errorFile = new File(dir + File.separator + "error1.txt");
		assertTrue(errorFile.exists());
		errorFile.delete();
		undoTestFile();
	}
	
	/**
	 * Testing for error from Incorrect XML (two Contact Lists).
	 */
	@Test
	public void ContactManagerTwoContactListsTest()
	{
		createTestFile("twoContactLists.txt");
		ContactManager testCM = new ContactManagerImpl();
		File errorFile = new File(dir + File.separator + "error1.txt");
		assertTrue(errorFile.exists());
		errorFile.delete();
		undoTestFile();
	}
	
	@Test
	public void ContactManagerTwoContactNotesTest()
	{
		createTestFile("twoContactNotes.txt");
		ContactManager testCM = new ContactManagerImpl();
		File errorFile = new File(dir + File.separator + "error2.txt");
		assertTrue(errorFile.exists());
		errorFile.delete();
		undoTestFile();
	}
	
	@Test
	public void ContactManagerContactHarryLoadTest()
	{
		createTestFile("twoContactNotes.txt");
		ContactManager testCM = new ContactManagerImpl();
		Set<Contact> testSet = testCM.getContacts();
		assertTrue(checkSet(testSet, "Harry"));
	}
	
	private void createTestFile(String name)
	{
		testFileName += name;
		testFile = new File(testFileName);
		testFile.renameTo(new File(savedFile));
	}
	
	private void undoTestFile()
	{
		testFile.renameTo(new File(testFileName));
		testFileName = dir + File.separator;
	}
	
	private boolean checkSet(Set<Contact> set, String name)
	{
		for(Contact c : set)
		{
			if(c.getName().equals(name))
				return true;
		}
		return false;
	}
	
	private void setUpSetContacts()
	{
		testContacts = new HashSet<Contact>();
		testContact = new ContactImpl("Bob");
		testContacts.add(testContact);
	}
	
	@Test
	public void addFutureMeetingidTest()
	{
		testDate = new GregorianCalendar();
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Hi");
		Set<Contact> fortest = testCM.getContacts("Bob");
		int id = testCM.addFutureMeeting(fortest, testDate);
		int expid = MeetingImpl.getTotal() - 1;
		assertEquals(id, expid);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addFutureMeetingThrowTest1()
	{
		testDate = new GregorianCalendar(1990, Calendar.JANUARY, 1);
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Hi!");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testCM.addFutureMeeting(fortest, testDate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addFutureMeetingThrowTest2()
	{
		testDate = new GregorianCalendar(2017, Calendar.MARCH, 1);
		ContactManager testCM = new ContactManagerImpl();
		setUpSetContacts();
		testCM.addFutureMeeting(testContacts, testDate);
	}
	
	@Test
	public void getPastMeetingTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Hi");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testDate = new GregorianCalendar(1999, Calendar.FEBRUARY, 1);
		testCM.addNewPastMeeting(fortest, testDate, "Test");
		PastMeeting testresult = testCM.getPastMeeting(0);
		int id = MeetingImpl.getTotal();
		assertEquals(testresult.getId(), id);
	}
	
	@Test
	public void getPastMeetingTestNull()
	{
		ContactManager testCM = new ContactManagerImpl();
		assertNull(testCM.getPastMeeting(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getPastMeetingThrownTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Test");
		testDate = new GregorianCalendar(2016, Calendar.FEBRUARY, 1);
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate);
		int id = MeetingImpl.getTotal() - 1;
		testCM.getPastMeeting(id);
	}
	
	@Test
	public void getFutureMeetingTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Test");
		testDate = new GregorianCalendar(2016, Calendar.FEBRUARY, 1);
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate);
		int id = MeetingImpl.getTotal() - 1;
		assertEquals(testCM.getFutureMeeting(id).getId(), id);
	}
	
	@Test
	public void getFutureMetingTestNull()
	{
		ContactManager testCM = new ContactManagerImpl();
		assertNull(testCM.getFutureMeeting(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getFutureMeetingTestThrown()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2014, Calendar.JUNE, 1);
		testCM.addNewContact("Bob", "Test");
		testCM.addNewPastMeeting(testCM.getContacts("Bob"), testDate, "Hi");
		int id = MeetingImpl.getTotal() - 1;
		testCM.getFutureMeeting(id);
	}
	
	@Test
	public void getMeetingTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2014, Calendar.MARCH, 1);
		testCM.addNewContact("Bob", "Test");
		testCM.addNewPastMeeting(testCM.getContacts("Bob"), testDate, "Hello");
		int id = MeetingImpl.getTotal() - 1;
		assertEquals(testCM.getMeeting(id).getId(), id);
	}
	
	@Test
	public void getMeetingTestNull()
	{
		ContactManager testCM = new ContactManagerImpl();
		assertNull(testCM.getMeeting(0));
	}
	
	@Test
	public void getFutureMeetingList()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2016, Calendar.MARCH, 1);
		Calendar testDate2 = new GregorianCalendar(2016, Calendar.FEBRUARY, 1);
		testCM.addNewContact("Bob", "Notes");
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate);
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate2);
		List<Meeting> testresult = testCM.getFutureMeetingList((Contact)testCM.getContacts("Bob").toArray()[0]);
		List<Meeting> expectedresult = new LinkedList<Meeting>();
		expectedresult.add(testCM.getMeeting(MeetingImpl.getTotal() - 2));
		expectedresult.add(testCM.getMeeting(MeetingImpl.getTotal() - 1));
		assertThat(testresult, new meetingListMatcher(expectedresult));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getFutureMeetingListThrownTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		List<Meeting> result = testCM.getFutureMeetingList(new ContactImpl("Harry"));
	}
	
	@Test
	public void getFutureMeetingfromDateList()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2016, Calendar.MARCH, 1, 20, 0, 0);
		Calendar testDate2 = new GregorianCalendar(2016, Calendar.MARCH, 21, 0, 0);
		testCM.addNewContact("Bob", "Notes");
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate);
		testCM.addFutureMeeting(testCM.getContacts("Bob"), testDate2);
		List<Meeting> testresult = testCM.getFutureMeetingList(testDate);
		List<Meeting> expectedresult = new LinkedList<Meeting>();
		expectedresult.add(testCM.getMeeting(MeetingImpl.getTotal() - 2));
		expectedresult.add(testCM.getMeeting(MeetingImpl.getTotal() - 1));
		assertThat(testresult, new meetingListMatcher(expectedresult));
	}
	
	@Test
	public void getPastMeetingList()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2014, Calendar.MARCH, 1);
		Calendar testDate2 = new GregorianCalendar(2014, Calendar.FEBRUARY, 1);
		testCM.addNewContact("Bob", "Notes");
		testCM.addNewPastMeeting(testCM.getContacts("Bob"), testDate, "Test1");
		testCM.addNewPastMeeting(testCM.getContacts("Bob"), testDate2, "Test2");
		List<PastMeeting> testresult = testCM.getPastMeetingList((Contact)testCM.getContacts("Bob").toArray()[0]);
		List<PastMeeting> expectedresult = new LinkedList<PastMeeting>();
		expectedresult.add(testCM.getPastMeeting(MeetingImpl.getTotal() - 2));
		expectedresult.add(testCM.getPastMeeting(MeetingImpl.getTotal() - 1));
		assertThat(testresult, new pastMeetingListMatcher(expectedresult));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getPastMeetingListThrownTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		List<PastMeeting> result = testCM.getPastMeetingList(new ContactImpl("Harry"));
	}
	
	@Test
	public void addNewPastMeetingidTest()
	{
		testDate = new GregorianCalendar(2014, Calendar.JANUARY, 1);
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Hi");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testCM.addNewPastMeeting(fortest, testDate, "Hi");
		int id = testCM.getPastMeetingList((Contact)testCM.getContacts("Bob").toArray()[0]).get(0).getId();
		int expid = MeetingImpl.getTotal() - 1;
		assertEquals(id, expid);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addNewPastMeetingThrowTest1()
	{
		testDate = new GregorianCalendar(2017, Calendar.JANUARY, 1);
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Hi!");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testCM.addNewPastMeeting(fortest, testDate, "Hi");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addNewPastMeetingThrowTest2()
	{
		testDate = new GregorianCalendar(2012, Calendar.MARCH, 1);
		ContactManager testCM = new ContactManagerImpl();
		setUpSetContacts();
		testCM.addFutureMeeting(testContacts, testDate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addMeetingNotesThrownTest1()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addMeetingNotes(0,  "Nothing decided");
	}
	
	@Test(expected = IllegalStateException.class)
	public void addMeetingNotesThrownTest2()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2017, Calendar.APRIL, 1);
		testCM.addNewContact("Bob", "CEO");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testCM.addFutureMeeting(fortest, testDate);
		int id = MeetingImpl.getTotal() - 1;
		testCM.addMeetingNotes(id, "Nothing");
	}
	
	@Test(expected = NullPointerException.class)
	public void addMeetingNotesThrownTest3()
	{
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2017, Calendar.NOVEMBER, 1);
		testCM.addNewContact("Bob", "Test");
		Set<Contact> fortest = testCM.getContacts("Bob");
		testCM.addFutureMeeting(fortest, testDate);
		int id = MeetingImpl.getTotal() - 1;
		testDate = new GregorianCalendar(2014, Calendar.JANUARY, 1);
		testCM.addMeetingNotes(0,  "Hi");
	}
	
	@Test
	public void addMeetingNotesTest()
	{
		String notes = "Testing";
		ContactManager testCM = new ContactManagerImpl();
		testDate = new GregorianCalendar(2015, Calendar.NOVEMBER, 12);
		testCM.addNewContact("Bob",  "Notes");
		testCM.addFutureMeeting(testCM.getContacts("Bob"),  testDate);
		testDate = new GregorianCalendar(2015, Calendar.NOVEMBER, 13);
		testCM.updateCurrentDate(testDate);
		int id = MeetingImpl.getTotal();
		testCM.addMeetingNotes(id, notes);
		assertEquals(testCM.getPastMeeting(id).getNotes(), notes);
	}
	
	@Test(expected = NullPointerException.class)
	public void addNewContactTestThrow()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact(null, "Test");
	}
	
	@Test(expected = NullPointerException.class)
	public void addNewContactTestThrowNullNotes()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Harry", null);
	}
	
	@Test
	public void addNewContactTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Harry", "First Contact");
		assertTrue(testCM.getContacts("Harry").size() > 0);
	}
	
	@Test
	public void getContactsTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Harry", "Hi");
		testCM.addNewContact("Joe", "Hello");
		int id = MeetingImpl.getTotal() - 1;
		Set<Contact> result = testCM.getContacts(id-1, id);
		Set<Contact> tocheck = testCM.getContacts("Harry");
		tocheck.addAll(testCM.getContacts("Joe"));
		assertThat(result, new contactSetMatcher(tocheck));
	}
	
	@Test(expected = NullPointerException.class)
	public void getContactStringTestThrown1()
	{
		ContactManager testCM = new ContactManagerImpl();
		String tester = null;
		Set<Contact> result = testCM.getContacts(tester);
	}
	
	@Test(expected = NullPointerException.class)
	public void getContactStringTestThrown2()
	{
		ContactManager testCM = new ContactManagerImpl();
		String tester = "";
		Set<Contact> result = testCM.getContacts(tester);
	}
	
	@Test
	public void getContactString()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Test");
		testCM.addNewContact("Bobby", "Other Bob");
		int id = MeetingImpl.getTotal();
		Set<Contact> tocompare = testCM.getContacts(id, id-1);
		Set<Contact> result = testCM.getContacts("Bob");
		assertThat(tocompare, new contactSetMatcher(result));
	}
	
	@Test
	public void flushTest()
	{
		ContactManager testCM = new ContactManagerImpl();
		testCM.addNewContact("Bob", "Test");
		testCM.addNewContact("Michael", "Tester");
		Set<Contact> forMeeting = testCM.getContacts("Bob");
		testCM.addFutureMeeting(forMeeting, new GregorianCalendar(2015, Calendar.DECEMBER, 25));
		testCM.flush();
		ContactManager newTestCM = new ContactManagerImpl();
		assertThat(testCM.getContacts("Bob"), new contactSetMatcher(newTestCM.getContacts("Bob")));
	}
}
