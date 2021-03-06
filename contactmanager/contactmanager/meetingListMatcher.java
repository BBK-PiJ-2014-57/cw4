package contactmanager;


import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class meetingListMatcher extends TypeSafeMatcher<List<Meeting>>{
		private final List<Meeting> expected;
		public meetingListMatcher(List<Meeting> inputAsList) {
			this.expected = inputAsList;
		}
		/**
		* Creates a .equals() method, changing the same Object check.
		*/
		@Override
		public boolean matchesSafely(List<Meeting> actual){
			if(actual == expected) return true;
			if(actual.size()!=expected.size()) return false;
			for(int i = 0; i< actual.size(); i++)
			{
				if(!checkExpected(actual.get(i).getId()))
				{
						return false;
				}
				if(i != actual.size() - 1)
				{
					if(!actual.get(i).getDate().after(actual.get(i+1).getDate()))
						return false;
				}
				if(duplicateCheck(actual, actual.get(i).getId()))
					return false;
			}
				return true;
		}
		@Override
		public void describeTo(Description descr)
		{
			descr.appendText("Not the same list or incorrect Format");
		}
		
		private boolean checkExpected(int id)
		{
			for(int i = 0; i < expected.size(); i++)
			{
				if(expected.get(i).getId() == id)
					return true;
			}
			return false;
		}
		
		private boolean duplicateCheck(List<Meeting> checkList, int id)
		{
			int count = 0;
			for(int i = 0; i < checkList.size(); i++)
			{
				if((checkList.get(i).getId() == id))
					count++;
				if(count > 1)
					return true;
			}
			return false;
		}
}

