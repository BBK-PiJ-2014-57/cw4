package contactmanager;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MeetingImplTest {
	
	private Calendar testDate = new GregorianCalendar(2015, Calendar.MARCH, 25, 20, 0, 0);
	private Calendar testDate2 = new GregorianCalendar();
	private Set<Contact> testContacts;
	private Contact contact1;
	private Contact contact2;
	private Meeting test1;
	
	@Test
	public void getIDTest()
	{
		testContacts = new HashSet<Contact>();
		testContacts.add(new ContactImpl("Tester"));
		int totalMeetings = MeetingImpl.getTotal();
		test1 = new MeetingImpl(testDate, testContacts);
		assertEquals(test1.getId(), totalMeetings);
	}
	
	@Test
	public void getDateTest()
	{
		testContacts = new HashSet<Contact>();
		testContacts.add(contact1);
		test1 = new MeetingImpl(testDate, testContacts);
		assertEquals(test1.getDate(), testDate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyContactListConstructorTest()
	{
		testContacts = new HashSet<Contact>();
		test1 = new MeetingImpl(testDate, testContacts);
	}
	
	@Test
	public void getContactsTest()
	{
		contact1 = new ContactImpl("Bob");
		contact2 = new ContactImpl("Harry");
		testContacts = new HashSet<Contact>();
		testContacts.add(contact1);
		testContacts.add(contact2);
		test1 = new MeetingImpl(testDate, testContacts);
		assertEquals(test1.getContacts(), testContacts);
	}
	
	
	
	
	
}
