package contactmanager;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * Implements the Interface ContactManager. Basic principle is that data will be loaded and read from the XML by this program
 * only. Loads on empty List/Set of Meetings/Contacts if there is no XML found. Errors are output in numbered text files in
 * the same directory of the XML and Error numbers are logged in the XML itself.
 * @author lewispalmer
 *
 */
public class ContactManagerImpl implements ContactManager {

	private List<Meeting> meetingList;
	private Set<Contact> contactSet;
	private static final String fileName = File.separator + "contacts.txt";
	private static final String errorFileName = File.separator + "error";
	private int errorNumber = 0;
	private String dir = System.getProperty("user.dir");
	private Calendar currDate = new GregorianCalendar();
	/**
	 * XML Format - ContactManager node, Meeting node, Contact node
	 */
	private File savedData;
	private File errorData;
	//List of strings for XML Formatting - to save on typos!
	private final String meetingListNodeTag = "MeetingList";
	private final String meetingListElementTag = "Meeting";
	private final String NotesElementTag = "Note";
	private final String contactListNodeTag = "Contacts";
	private final String nameAttributeName = "Name";
	private final String contactListElementTag = "Contact";
	private final String idAttributeName = "id";
	private final String dateElementTag = "Date";
	private final String yearAttributeName = "Year";
	private final String monthAttributeName = "Month";
	private final String dateAttributeName = "Date";
	private final String hourAttributeName = "Hour";
	private final String minuteAttributeName = "Minute";
	private final String participantListElementTag = "Participants";
	private final String typeAttributeName = "Type";
	private final String headerElementTagName = "ContactManager";
	
	public ContactManagerImpl()
	{
		meetingList = new LinkedList<Meeting>();
		contactSet = new HashSet<Contact>();
		savedData = new File(dir + fileName);
		if(savedData.exists())
			loadSavedData();
	}
	
	private void loadSavedData()
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Parses saved Document as an XML! - Creates a new DOM Document
			Document savedDoc = dBuilder.parse(savedData);
			savedDoc.getDocumentElement().normalize();
			//Isolate the Contact section of the XML to be loaded into data structures.
			NodeList contactNode = savedDoc.getElementsByTagName(contactListNodeTag);
			if(contactNode.getLength() > 1)
			{
				errorNumber++;
				outputError("Incorrect XML Formatting! Only loading the first list of " + contactListNodeTag);
			}
			if(contactNode.getLength() > 0)
			{
				NodeList contactList = contactNode.item(0).getChildNodes();
				loadContactsFromXML(contactList);
			}
			//Takes the meeting part of the XML to read saved data.
			NodeList meetingNode = savedDoc.getElementsByTagName(meetingListNodeTag);
			if(meetingNode.getLength() > 1)
			{
				errorNumber++;
				outputError("Incorrect XML Formatting! Only loading the first " + meetingListNodeTag);
			}
			if(meetingNode.getLength()>0)
			{
				loadMeetingsFromXML(meetingNode);
			}
		}
		catch(FactoryConfigurationError err)
		{
			errorNumber++;
			outputError("Error with Document Builder Factory Parser " + err.toString());
		} catch (ParserConfigurationException err) {
			errorNumber++;
			outputError("Error with Document Builder configuration: " + err.toString());
		}catch (SAXParseException err)
		{
			errorNumber++;
			outputError("Error with SAX project parsing XML from saved file: " + err.toString());
		} catch (SAXException err) {
			errorNumber++;
			outputError("Error with SAX project loading XML from saved file: " + err.toString());
		} catch (IOException err) {
			errorNumber++;
			outputError("Error with Input of data from saved XML: " + err.toString());
		}
		
	}
	
	private void outputError(String errString)
	{
		File file = new File(dir + File.separator + errorFileName + errorNumber + ".txt");
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			out.write(errString);
		} catch (FileNotFoundException ex) {
			// This happens if file does not exist and cannot be created,
			// or if it exists, but is not writable
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
	}
	
	private void loadContactsFromXML(NodeList node)
	{
		int listLength = node.getLength();
		for(int i = 0; i < listLength; i++)
		{
			try
			{
				Element contact = (Element)node.item(i);
				String Name = contact.getAttribute(nameAttributeName);
				NodeList contactNotes = contact.getElementsByTagName(NotesElementTag);
				if(contactNotes.getLength() > 1)
				{
					errorNumber++;
					outputError("Incorrect XML Formatting for Contact Notes, Contact: " + Name + " Only copying first Note");
				}
				String contactNote = "";
				if(contactNotes.getLength() > 0)
				{
					contactNote = contactNotes.item(0).getNodeValue();
				}
				contactSet.add(new ContactImpl(Name, contactNote));
			}
			catch(IllegalArgumentException err)
			{
				errorNumber++;
				outputError("Error loading contact in XML list number: "+ i + ". Error: " + err.toString());
			}
			catch(ClassCastException err)
			{
				errorNumber++;
				outputError("Error parsing XML to Contact. Error: " + err.toString());
			}
			
		}
	}
	
	private void loadMeetingsFromXML(NodeList node)
	{
		int listLength = node.getLength();
		for(int i = 0; i < listLength; i++)
		{
			try
			{
				Element meeting = (Element)node.item(i);
				NodeList meetingNoteNode = meeting.getElementsByTagName(NotesElementTag);
				String meetingNotes = "";
				if(meetingNoteNode.getLength() > 0)
				{
					meetingNotes = meetingNoteNode.item(0).getNodeValue();
				}
				Element date;
				if(meeting.getElementsByTagName(dateElementTag).getLength() > 0)
				{
					date = (Element)meeting.getElementsByTagName(dateElementTag).item(0);
				}
				else
				{
					throw new IllegalArgumentException();
				}
				String year = date.getAttribute(yearAttributeName);
				String month = date.getAttribute(monthAttributeName);
				String day = date.getAttribute(dateAttributeName);
				String hour = date.getAttribute(hourAttributeName);
				String minute = date.getAttribute(minuteAttributeName);
				Calendar tempDate = new GregorianCalendar(
						Integer.parseInt(year),
						Integer.parseInt(month) - 1,
						Integer.parseInt(day),
						Integer.parseInt(hour),
						Integer.parseInt(minute));
				NodeList participantsNode = meeting.getElementsByTagName(participantListElementTag);
				int participants = participantsNode.getLength();
				Set<Contact> meetingParticipants = new HashSet<Contact>();
				for(int j = 0; j < participants; j++)
				{
					int contactid = Integer.parseInt(participantsNode.item(j).getNodeValue());
					try
					{
						meetingParticipants.addAll(getContacts(contactid));
					}
					catch(IllegalArgumentException err)
					{
						errorNumber++;
						outputError("Incorrect ID from contact in meeting: " + i + ". Error: " + err.toString());
					}
				}
				//Just to check if any correct contact were provided. No point adding a meeting no one is going to.
				if(meetingParticipants.size() > 0)
				{
					try{
						int type = Integer.parseInt(meeting.getAttribute(typeAttributeName));
						if(type == MeetingType.Future)
							meetingList.add(new FutureMeetingImpl(tempDate, meetingParticipants));
						else if(type == MeetingType.Past)
							meetingList.add(new PastMeetingImpl(tempDate, meetingParticipants, meetingNotes));
						else
							meetingList.add(new MeetingImpl(tempDate, meetingParticipants));
					}
					catch(NumberFormatException err)
					{
						errorNumber++;
						outputError("Error converting Meeting Type for meeting " + i + ". Error: " + err.toString());
						meetingList.add(new MeetingImpl(tempDate, meetingParticipants));
					}
				}
			}
			catch(DOMException err)
			{
				errorNumber++;
				outputError("Error loading Meetings from XML into classes: " + err.toString());
			}
			catch(IllegalArgumentException err)
			{
				errorNumber++;
				outputError("Error converting XML to Meeting: " + err.toString());
			}
		}
	}
	
	/**
	 * Accessor for list of meetings.
	 * @return MeetingList
	 */
	public List<Meeting> getMeetingList()
	{
		return this.meetingList;
	}
	
	/**
	 * Accessor for Contact list
	 * @return Contact list
	 */
	public Set<Contact> getContacts()
	{
		return this.contactSet;
	}
	
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		int toReturn = 0;
		for(Contact c : contacts)
		{
			if(!contactSet.contains(c))
				throw new IllegalArgumentException();
		}
		if(date.after(currDate))
		{
			Meeting temp = new FutureMeetingImpl(date, contacts);
			toReturn = temp.getId();
			meetingList.add(temp);
		}
		else
			throw new IllegalArgumentException();
		return toReturn;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		PastMeeting toReturn = null;
		for(int i = 0; i < meetingList.size(); i++)
		{
			if(meetingList.get(i).getId() == id)
				if(meetingList.get(i).getDate().before(currDate))
					toReturn = (PastMeeting)meetingList.get(i);
				else
					throw new IllegalArgumentException();
		}
		return toReturn;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		FutureMeeting toReturn = null;
		for(int i = 0; i < meetingList.size(); i++)
		{
			if(meetingList.get(i).getId() == id)
			{
				if(meetingList.get(i).getDate().after(currDate))
					toReturn = (FutureMeeting)meetingList.get(i);
				else
					throw new IllegalArgumentException();
			}
		}
		return toReturn;
	}

	@Override
	public Meeting getMeeting(int id) {
		for(int i = 0; i < meetingList.size(); i++)
		{
			if(meetingList.get(i).getId() == id)
				return meetingList.get(i);
		}
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> toReturn = new LinkedList<Meeting>();
		if(!contactSet.contains(contact))
			throw new IllegalArgumentException();
		for(int i = 0; i < meetingList.size(); i++)
		{
			if(meetingList.get(i).getContacts().contains(contact))
				toReturn.add(meetingList.get(i));
		}
		return toReturn;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> toReturn = new LinkedList<Meeting>();
		Calendar dateBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
		dateBefore.roll(Calendar.DATE, -1);
		Calendar dateAfter = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
		dateAfter.roll(Calendar.DATE, 1);
		for(int i = 0; i < meetingList.size(); i++)
		{
			Meeting tocheck = meetingList.get(i);
			if(date.get(Calendar.YEAR) == (tocheck.getDate().get(Calendar.YEAR))
					&& date.get(Calendar.MONTH) == (tocheck.getDate().get(Calendar.MONTH))
							&& date.get(Calendar.DAY_OF_MONTH) == (tocheck.getDate().get(Calendar.DAY_OF_MONTH)))
			{
				int j = 0;
				int size = toReturn.size();
				boolean added = false;
				while(j < size && !added)
				{
					if(tocheck.getDate().before(toReturn.get(j).getDate()))
					{
						toReturn.add(j, tocheck);
						added = true;
					}
					j++;
				}
				if(!added)
					toReturn.add(tocheck);
			}
		}
		return toReturn;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		List<PastMeeting> toReturn = new LinkedList<PastMeeting>();
		if(!contactSet.contains(contact))
			throw new IllegalArgumentException();
		int topend = meetingList.size();
		for(int i = 0; i < topend; i++)
		{
			Meeting tocheck = meetingList.get(i);
			if(tocheck.getContacts().contains(contact))
			{
				if(tocheck.getDate().before(currDate))
				{
					int j = 0;
					int size = toReturn.size();
					boolean added = false;
					while(j < size && !added)
					{
						if(tocheck.getDate().before(toReturn.get(j).getDate()))
						{
							toReturn.add(j, (PastMeeting)tocheck);
							added = true;
						}
						j++;
					}
					if(!added)
						toReturn.add((PastMeeting)tocheck);
				}
			}
		}
		return toReturn;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		if(contacts == null || date == null || text == null)
			throw new NullPointerException();
		if(contacts.size() == 0)
			throw new IllegalArgumentException();
		for(Contact c : contacts)
		{
			if(!contactSet.contains(c))
				throw new IllegalArgumentException();
		}
		if(date.after(currDate))
			throw new IllegalArgumentException();
		meetingList.add(new PastMeetingImpl(date, contacts, text));
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		boolean meetingExist = false;
		int index = 0;
		if(text == null)
			throw new NullPointerException();
		for(int i = 0; i < meetingList.size(); i++)
		{
			if(meetingList.get(i).getId() == id)
			{
				meetingExist = true;
				index = i;
				if(!meetingList.get(i).getDate().before(currDate))
					throw new IllegalStateException();
			}
		}
		if(!meetingExist)
			throw new IllegalArgumentException();
		PastMeeting toReplace = new PastMeetingImpl(meetingList.get(index), text);
		meetingList.remove(index);
		meetingList.add(index, toReplace);
	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || name.equals("") || notes == null)
			throw new NullPointerException();
		contactSet.add(new ContactImpl(name, notes));
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> toReturn = new HashSet<Contact>();
		boolean checkContact = false;
		for(int i : ids)
		{
			checkContact = false;
			for(Contact c : contactSet)
			{
				if(c.getId() == i)
				{
					toReturn.add(c);
					checkContact = true;
				}
			}
			if(!checkContact)
				throw new IllegalArgumentException();
		}
		return toReturn;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		if(name == null || name.equals(""))
			throw new NullPointerException();
		Set<Contact> toReturn = new HashSet<Contact>();
		for(Contact c : contactSet)
		{
			if(c.getName().contains(name))
				toReturn.add(c);
		}
		return toReturn;
	}
	
	public void updateCurrentDate(Calendar date)
	{
		this.currDate = date;
	}

	@Override
	public void flush() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			Document savedData = docBuilder.newDocument();
			Element mainNode = savedData.createElement(headerElementTagName);
			savedData.appendChild(mainNode);
			Element contactsNode = savedData.createElement(contactListNodeTag);
			mainNode.appendChild(contactsNode);
			for(Contact c : contactSet)
			{
				Element contact = savedData.createElement(contactListElementTag);
				contact.setAttribute(nameAttributeName, c.getName());
				Element contactNotes = savedData.createElement(NotesElementTag);
				contactNotes.setNodeValue(c.getNotes());
				contact.appendChild(contactNotes);
				contactsNode.appendChild(contact);
			}
			Element meetingsNode = savedData.createElement(meetingListNodeTag);
			mainNode.appendChild(meetingsNode);
			for(Meeting m : meetingList)
			{
				Element meeting = savedData.createElement(meetingListElementTag);
				Calendar tempDate = (Calendar)currDate.clone();
				tempDate.roll(Calendar.MONTH, 1);
				FutureMeeting future = new FutureMeetingImpl(tempDate, contactSet);
				tempDate.roll(Calendar.MONTH, -2);
				PastMeeting past = new PastMeetingImpl(tempDate, contactSet, "Temp");
				int Meetingtype;
				if(m.getClass().equals(future.getClass()))
					Meetingtype = MeetingType.Future;
				else if (m.getClass().equals(past.getClass()))
				{
					Meetingtype = MeetingType.Past;
					Element meetingNote = savedData.createElement(NotesElementTag);
					PastMeeting tempm = (PastMeeting)m;
					meetingNote.setNodeValue(tempm.getNotes());
					meeting.appendChild(meetingNote);
				}
				else
					Meetingtype = MeetingType.NormalMeeting;
				meeting.setAttribute(typeAttributeName, Integer.toString(Meetingtype));
				Element date = savedData.createElement(dateElementTag);
				date.setAttribute(yearAttributeName, Integer.toString(m.getDate().get(Calendar.YEAR)));
				date.setAttribute(monthAttributeName, Integer.toString(m.getDate().get(Calendar.MONTH)));
				date.setAttribute(dateAttributeName, Integer.toString(m.getDate().get(Calendar.DATE)));
				date.setAttribute(hourAttributeName, Integer.toString(m.getDate().get(Calendar.HOUR)));
				date.setAttribute(minuteAttributeName, Integer.toString(m.getDate().get(Calendar.MINUTE)));
				meeting.appendChild(date);
				Element participants = savedData.createElement(participantListElementTag);
				for(Contact c : m.getContacts())
				{
					Element participant = savedData.createElement(contactListElementTag);
					participant.setNodeValue(Integer.toString(c.getId()));
					participants.appendChild(participant);
				}
				meeting.appendChild(participants);
				meetingsNode.appendChild(meeting);
				Element errorNode = savedData.createElement("Errors");
				errorNode.setNodeValue(Integer.toString(errorNumber));
				mainNode.appendChild(errorNode);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(savedData);
			File outputFile = new File(dir + fileName);
			if(!outputFile.exists())
				outputFile.createNewFile();
			StreamResult result = new StreamResult(outputFile);
			transformer.transform(source, result);
		} catch (ParserConfigurationException err) {
			errorNumber++;
			outputError("Error Saving Data: " + err.toString());
		}
		catch (DOMException err)
		{
			errorNumber++;
			outputError("Error building XML. " + err.toString());
		}
		catch (TransformerConfigurationException err) {
			errorNumber++;
			outputError("Error outputting XML. " + err.toString());
		}
		catch (TransformerException err) {
			errorNumber++;
			outputError("Error writing XML. " + err.toString());
		}
		catch (IOException err) {
			errorNumber++;
			outputError("Error creating output file. " + err.toString());
		}
	}

}
