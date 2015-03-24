package contactmanager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ContactImplTest {

	private Contact testContact;
	private Contact testContact2;
	private Contact copytestContact;
	private Contact emptyNoteContact;
	private String testName = "Michael";
	private String testNote = "Test";
	private Contact noNoteCtorContact;
	private Contact firstContact;
	private Contact secondContact;
	@Before
	public void SetUpContact()
	{
		testContact = new ContactImpl(testName, testNote);
		testContact2 = new ContactImpl("Bruce", testNote);
		copytestContact = new ContactImpl("Harry", testNote);
		emptyNoteContact = new ContactImpl(testName, "");
		noNoteCtorContact = new ContactImpl("Joe");
		
	}
	
	@Test
	public void getIdTest(){
		int count = ContactImpl.getTotal();
		firstContact = new ContactImpl(testName, testNote);
		assertEquals(firstContact.getId(), count);
	}
	
	@Test
	public void getIdMultipleContactsTest(){
		int count = ContactImpl.getTotal();
		firstContact = new ContactImpl(testName, testNote);
		secondContact = new ContactImpl(testName, testNote);
		assertEquals(secondContact.getId(), count + 1);
	}
	
	@Test
	public void getIdDistinctIDTest(){
		assertFalse(testContact.getId() == copytestContact.getId());
	}
	
	@Test
	public void getNameTest(){
		assertEquals(testContact.getName(), testName);
	}
	
	@Test
	public void getNotesTest(){
		assertEquals(testContact.getNotes(), testNote);
	}
	
	@Test
	public void getNotesNoNoteTest(){
		assertEquals(noNoteCtorContact.getNotes(), "");
	}
	
	@Test
	public void addNotesTest(){
		emptyNoteContact.addNotes(testNote);
		assertEquals(testContact.getNotes(), testNote);
	}
	
	@Test
	public void addNotesnullNoteTest(){
		noNoteCtorContact.addNotes(null);
		assertEquals(noNoteCtorContact.getNotes(), "");
	}
	
	@Test
	public void hashCodeTest1(){
		String name = "Harry";
		String note = "the first one";
		firstContact = new ContactImpl(name, note);
		secondContact = new ContactImpl(name, note);
		int firstHash = firstContact.hashCode();
		int secondHash = secondContact.hashCode();
		assertFalse(firstHash == secondHash);
	}
	
	@Test
	public void hashCodeTest2(){
		String name = "John";
		firstContact = new ContactImpl(name);
		int firstHash = firstContact.hashCode();
		int secondHash = firstContact.hashCode();
		assertTrue(firstHash == secondHash);
	}
}
