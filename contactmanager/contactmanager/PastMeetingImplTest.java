package contactmanager;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PastMeetingImplTest {

	private FutureMeeting testFuture;
	private MeetingImpl testMeeting;
	private PastMeeting testPastMeeting;
	private PastMeeting testPastMeeting2;
	private Calendar testDate;
	private Set<Contact> testContacts;
	Contact testContact;
	String testNotes;
	
	@Before
	public void setUpVariables()
	{
		testDate = new GregorianCalendar(2015, Calendar.MARCH, 27, 20, 0, 0);
		testContact = new ContactImpl("Harry");
		testContacts = new HashSet<Contact>();
		testContacts.add(testContact);
		testMeeting = new MeetingImpl(testDate, testContacts);
		testFuture = new FutureMeetingImpl(testDate, testContacts);
		testPastMeeting = new PastMeetingImpl(testFuture, testNotes);
		testPastMeeting2 = new PastMeetingImpl(testMeeting, testNotes);
	}
	
	@Test
	public void ctorFromFutureIdTest()
	{
		assertEquals(testPastMeeting.getId(), testFuture.getId());	
	}
	
	@Test
	public void ctorFromFutureDateTest()
	{
		assertEquals(testPastMeeting.getDate(), testFuture.getDate());
	}
	
	@Test
	public void ctorFromFutureContactTest()
	{
		assertEquals(testPastMeeting.getContacts(), testFuture.getContacts());
	}
	
	@Test
	public void getNotesFromFutureTest()
	{
		assertEquals(testPastMeeting.getNotes(), testNotes);
	}
	
	@Test
	public void ctorFromMeetingIdTest()
	{
		assertEquals(testPastMeeting2.getId(), testMeeting.getId());
	}
	
	@Test
	public void ctorFromMeetingDateTest()
	{
		assertEquals(testPastMeeting2.getDate(), testMeeting.getDate());
	}
	
	@Test
	public void ctorFromMeetingContactTest()
	{
		assertEquals(testPastMeeting2.getContacts(), testMeeting.getContacts());
	}
	
	@Test
	public void getNotesFromMeetingTest()
	{
		assertEquals(testPastMeeting2.getNotes(), testNotes);
	}
}
