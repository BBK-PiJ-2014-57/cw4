package contactmanager;

import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting {

	private static int totalMeetings;
	private int id;
	private Calendar date;
	private Set<Contact> contacts;
	
	public MeetingImpl(Calendar date, Set<Contact> contacts)
	{
		if(contacts.size() > 0)
		{
			this.date = date;
			this.contacts = contacts;
			this.id = totalMeetings;
			totalMeetings++;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	public MeetingImpl(Meeting meeting)
	{
		this.id = meeting.getId();
		this.contacts = meeting.getContacts();
		this.date = meeting.getDate();
	}
	
	public static int getTotal()
	{
		return totalMeetings;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Calendar getDate() {
		return this.date;
	}

	@Override
	public Set<Contact> getContacts() {
		return this.contacts;
	}

}
