package framework.DataObject;

import java.util.Comparator;

public class DisplayListItem implements Comparable<DisplayListItem> {

	public int Id;
	public float Distance;
	public String ThumbNail;
	public String[] Info;
	public String Created;
	public String StoreID;

	public DisplayListItem(int id, String picture, String[] info, float distance , String  created, String StoreID) {
		Id = id;
		ThumbNail = picture;
		Info = info;
		Distance = distance;
		Created = created;
		this.StoreID = StoreID;
	}

	@Override
	public int compareTo(DisplayListItem item2) {
		// TODO Auto-generated method stub
		return this.Info[0].compareTo(item2.Info[0]);
	}

	public void swapID(DisplayListItem another) {

		int temp = this.Id;
		this.Id = another.Id;
		another.Id = temp;
	}

	public static class Comparators {

		public static Comparator<DisplayListItem> Distance = new Comparator<DisplayListItem>() {
			@Override
			public int compare(DisplayListItem item1, DisplayListItem item2) {
				if (Float.compare(item1.Distance, item2.Distance) < 0)
					item1.swapID(item2);
				return Float.compare(item1.Distance, item2.Distance);
			}
		};
		public static Comparator<DisplayListItem> Name = new Comparator<DisplayListItem>() {
			@Override
			public int compare(DisplayListItem item1, DisplayListItem item2) {
				if (item1.Info[0].compareTo(item2.Info[0]) < 0)
					item1.swapID(item2);
				return item1.Info[0].compareTo(item2.Info[0]);
			}
		};
		public static Comparator<DisplayListItem> Date = new Comparator<DisplayListItem>() {
			@Override
			public int compare(DisplayListItem item1, DisplayListItem item2) {
				if (item1.Created.compareTo(item2.Created) < 0)
					item1.swapID(item2);
				return item1.Created.compareTo(item2.Created);
			}
		};

	}

}
