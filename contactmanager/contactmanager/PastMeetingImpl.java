package contactmanager;

import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private String notes;
	
	public PastMeetingImpl(Calendar date, Set<Contact> participants, String notes)
	{
		super(date, participants);
		this.notes = notes;
	}
	
	public PastMeetingImpl(Meeting meeting, String notes)
	{
		super(meeting);
		this.notes = notes;
	}
	
	@Override
	public String getNotes() {
		return notes;
	}

}
