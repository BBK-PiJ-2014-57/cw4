package contactmanager;

/**
 * Implementation of Contact Interface.
 * @author lewispalmer
 *
 */
public class ContactImpl implements Contact {

	/**
	 * Static counter for number of totalContacts. Also keeps IDs unique.
	 */
	private static int totalContacts;
	private String Name;
	private String Notes;
	private int id;
	
	/**
	 * Constructor, taking in Name and Notes
	 * @param Name name of the Contact
	 * @param Notes any associated Notes
	 */
	public ContactImpl(String Name, String Notes)
	{
		this.Name = Name;
		this.Notes = Notes;
		this.id = totalContacts;
		totalContacts++;
	}
	
	/**
	 * Accessor for the static totalContact, mainly used for JUnit tests
	 * @return the number of totalContact currently created.
	 */
	public static int getTotal()
	{
		return totalContacts;
	}
	
	/**
	 * Constructor for Contact with currently no associated notes.
	 * @param Name name of the Contact.
	 */
	public ContactImpl(String Name)
	{
		this.Name = Name;
		this.id = totalContacts;
		totalContacts++;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.Name;
	}

	@Override
	public String getNotes() {
		return (this.Notes == null) ? "" : this.Notes;
	}

	@Override
	public void addNotes(String note) {
		if(this.Notes == null)
			this.Notes = note;
		else
			this.Notes += note;
	}
	
	@Override
	public int hashCode()
	{
		return this.getId();
	}

}
