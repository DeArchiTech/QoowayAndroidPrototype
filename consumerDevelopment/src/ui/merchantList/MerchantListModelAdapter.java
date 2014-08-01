package ui.merchantList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ui.home.HomeListModelAdapter;
import android.location.Location;
import framework.Entry;
import framework.DataObject.Merchant;


public class MerchantListModelAdapter {

	public static ArrayList<MerchantDisplayListItem> Items;
	public final static int alphaIndex = 0;
	public static int omegaIndex = 20;
	public static int startIndex = 0;
	public static int recieveAmount = 20;
	public static int id = 0;

	public static void LoadModel(List<Entry> list, Location loc) {
		int id = 0;
		float noDistanceValue = (float) -1.0;
		Items = new ArrayList<MerchantDisplayListItem>();
		float f_distance;
		for (Entry item : list) {
			float results[] = new float[3];
			String distance = null;
			if (loc != null) {
				String Longitude = (String) item.attribute.get("Longitude");
				String Latitude = (String) item.attribute.get("Latitude");
				if (!Longitude.equals("") && !Latitude.equals("")) {
					double venueLongitude = Double.parseDouble(Longitude);
					double venueLatitude = Double.parseDouble(Latitude);
					Location.distanceBetween(loc.getLatitude(),
							loc.getLongitude(), venueLatitude, venueLongitude,
							results);
					f_distance = results[0];
					results[0] = (Math.round(results[0] / 100)) / 10;
					if (results[0] > 1000)
						results[0] = Math.round(results[0]);
					distance = String.valueOf(results[0]) + "KM";
				} else {
					f_distance = Float.MAX_VALUE;
					distance = "N/A";
				}
			} else {
				f_distance = noDistanceValue;
			}
			String imgpath = (String) item.attribute.get("LogoPath");
			if (item.attribute.get("Id") != null) {
				@SuppressWarnings("unused")
				int Id = Integer.parseInt((String) item.attribute.get("Id"));
			}
			String storeName = item.name;
			String Address = (String) item.attribute.get("Address");
			String promotion = (String) item.attribute.get("SpecialOffer");
			String info[] = { storeName, Address, promotion, distance };
			String created = (String) (item.attribute.get("CreateTime"));
			String storeID = (String) item.attribute.get("StoreID");
			Items.add(new MerchantDisplayListItem(++id, imgpath, info,
					f_distance, created, storeID ));
		}

	}

	public static ArrayList<MerchantDisplayListItem> LoadJsonModel(List<Merchant> list, Location loc) {
		
		if(MerchantListModelAdapter.Items==null)
			MerchantListModelAdapter.Items = new ArrayList<MerchantDisplayListItem>();
	
		ArrayList<MerchantDisplayListItem> Items = new ArrayList<MerchantDisplayListItem>();

		for (Merchant item : list) {
			float results[] = new float[3];
			String distance = null;
			String Longitude = item.Longitude;
			String Latitude = item.Latitude;
			if (!Longitude.equals("") && !Latitude.equals("") && loc!=null) {
				double venueLongitude = Double.parseDouble(Longitude);
				double venueLatitude = Double.parseDouble(Latitude);
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(),
						venueLatitude, venueLongitude, results);
				results[0] = (float) (results[0] / 1000.0);
				results[0] = (float) (Math.round(results[0]*10))/10;
				distance = String.valueOf(results[0]) + " km";
			} else {
				distance = "N/A";
			}
			String imgpath = item.LogoPath;
			if (item.StoreID > 0) {
				@SuppressWarnings("unused")
				int Id = item.StoreID;
			}
			String storeName = item.Name;
			String Address = item.Address;
			String promotion = item.SpecialOffer;
			String info[] = { storeName, Address, promotion, distance };
			@SuppressWarnings("deprecation")
			String created = item.CreateTime.toGMTString();
			String storeID = ""+item.StoreID;
			Items.add(new MerchantDisplayListItem(id++, imgpath, info,
					results[0], created,storeID));
			;
		}
		
		MerchantListModelAdapter.Items.addAll(Items);
		return Items;
	}

	public static MerchantDisplayListItem GetbyId(int id) {

		for (MerchantDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
	
	public static void ClearList() {
		MerchantListModelAdapter.Items=null;
		MerchantListModelAdapter.id = 0 ;
		MerchantListModelAdapter.startIndex = 0;
	}

	public static void reInitializeIDs() {
		int i = 0;
		for (MerchantDisplayListItem item : Items) {
			item.Id = i++;
		}
	}

	public static <T> void SortDistance() {

		Collections.sort(Items, MerchantListModelAdapter.Comparators.Distance);
	}

	public static <T> void SortDate() {

		Collections.sort(Items, MerchantListModelAdapter.Comparators.Date);
	}

	public static class Comparators {

		public static Comparator<MerchantDisplayListItem> Distance = new Comparator<MerchantDisplayListItem>() {
			@Override
			public int compare(MerchantDisplayListItem item1,
					MerchantDisplayListItem item2) {

				return Float.compare(item1.Distance, item2.Distance);
			}
		};
		public static Comparator<MerchantDisplayListItem> Name = new Comparator<MerchantDisplayListItem>() {
			@Override
			public int compare(MerchantDisplayListItem item1,
					MerchantDisplayListItem item2) {

				return item1.Info[0].compareTo(item2.Info[0]);
			}
		};
		public static Comparator<MerchantDisplayListItem> Date = new Comparator<MerchantDisplayListItem>() {
			@Override
			public int compare(MerchantDisplayListItem item1,
					MerchantDisplayListItem item2) {

				return item1.Created.compareTo(item2.Created);
			}
		};

	}
	
}
