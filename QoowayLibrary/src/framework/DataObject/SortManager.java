package framework.DataObject;

import java.util.Collections;
import java.util.List;


public class SortManager {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<T> void SortName(List<? extends Comparable> list) {
		Collections.sort(list);
	}

	public static<T> void SortDistance(List<DisplayListItem> list){

		if(list.get(0) instanceof DisplayListItem)
			Collections.sort(list , DisplayListItem.Comparators.Distance);
	}

	public static<T> void SortDate(List<DisplayListItem> list){

		if(list.get(0) instanceof DisplayListItem)
			Collections.sort(list , DisplayListItem.Comparators.Date);
	}
	
}

