package contactmanager;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class contactSetMatcher extends TypeSafeMatcher<Set<Contact>>{
		private final Set<Contact> expected;
		public contactSetMatcher(Set<Contact> inputSet) {
			this.expected = inputSet;
		}
		/**
		* Creates a .equals() method, changing the same Object check.
		*/
		@Override
		public boolean matchesSafely(Set<Contact> actual){
			if(actual == expected) return true;
			if(actual.size()!=expected.size()) return false;
			for(Contact c : actual)
			{
				if(!expected.contains(c))
					return false;
				
			}
			return true;
		}
		@Override
		public void describeTo(Description descr)
		{
			descr.appendText("Not the same list or incorrect Format");
		}
}
