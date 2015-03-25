package contactmanager;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MeetingImplTest {
	
	private Calendar testDate = new GregorianCalendar(2015, Calendar.MARCH, 24, 20, 0, 0);
	private Calendar testDate2 = new GregorianCalendar(2015, Calendar.MARCH, 25, 20, 0, 0);
	private Set<Contact> testContacts;
	private Contact 
	private Meeting test1;
	
	@Test
	public void getIDTest()
	{
		int totalMeetings = MeetingImpl.getTotal();
		test1 = new MeetingImpl(testDate, testContacts);
		assertEquals(test1.getId(), totalMeetings);
	}
	
	@Test
	public void getDateTest()
	{
		test1 = new MeetingImpl(testDate, testContacts);
		assertEquals(test1.getDate(), test1);
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
		
	}
	
	
	
	
	
}
