package framework.NearBy;

import java.util.ArrayList;
import java.util.List;
import ui.DisplayListItem;
import android.location.Location;
import framework.Entry;
import framework.DataObject.Merchant;


public class NearByModelAdapter {

	public static ArrayList<DisplayListItem> Items;
	public static int startIndex=0;
	public static int recieveAmount=10;
	//xml method , deprecated
	public static void LoadModel(List<Entry> list , Location loc) {
		int id = 0;
		Items = new ArrayList<DisplayListItem>();
		
		for (Entry item : list)
		{
			float results[] = new float[3];
			String distance= null;
			String Longitude = (String)item.attribute.get("Longitude");
			String Latitude = (String)item.attribute.get("Latitude");
			if(!Longitude.equals("")&&!Latitude.equals(""))
			{
				double venueLongitude = Double.parseDouble(Longitude);
				double venueLatitude = Double.parseDouble(Latitude);
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), venueLatitude, venueLongitude , results);
				results[0] = (Math.round(results[0]/100))/10;
				if(results[0]>1000)
					results[0] = Math.round(results[0]);
				distance = String.valueOf(results[0]) + "KM";
			}
			else
			{
				distance= "N/A";
			}
			String imgpath =(String)item.attribute.get("LogoPath");
			if(item.attribute.get("Id")!=null)
			{
				@SuppressWarnings("unused")
				int Id = Integer.parseInt((String)item.attribute.get("Id"));
			}
			String storeName=item.name;
			String Address=(String)item.attribute.get("Address");
			String promotion=(String)item.attribute.get("SpecialOffer");
			String info[] = {storeName ,Address ,promotion , distance};
			String created = (String)(item.attribute.get("CreateTime"));
			String storeID = (String)(item.attribute.get("StoreID"));
			Items.add(new DisplayListItem(++id, imgpath, info, results[0], created,storeID));
		}
	}

	public static void LoadJsonModel(List<Merchant> list , Location loc) {
		int id = 0;
		Items = new ArrayList<DisplayListItem>();
		
		for (Merchant item : list)
		{
			float results[] = new float[3];
			String distance= null;
			String Longitude = item.Longitude;
			String Latitude = item.Latitude;
			if(!Longitude.equals("")&&!Latitude.equals(""))
			{
				double venueLongitude = Double.parseDouble(Longitude);
				double venueLatitude = Double.parseDouble(Latitude);
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), venueLatitude, venueLongitude , results);
				results[0] = (Math.round(results[0]/100))/10;
				if(results[0]>1000)
					results[0] = Math.round(results[0]);
				distance = String.valueOf(results[0]) + "KM";
			}
			else
			{
				distance= "N/A";
			}
			String imgpath =item.LogoPath;
			if(item.StoreID>0)
			{
				@SuppressWarnings("unused")
				int Id = item.StoreID;
			}
			String storeName=item.Name;
			String Address=item.Address;
			String promotion=item.SpecialOffer;
			String info[] = {storeName ,Address ,promotion , distance};
			@SuppressWarnings("deprecation")
			String created = item.CreateTime.toGMTString();
			String storeID = ""+item.StoreID ;
			Items.add(new DisplayListItem(++id, imgpath, info, results[0], created,storeID));
		}
	}
	
	public static DisplayListItem GetbyId(int id) {

		for (DisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
	
	

}
