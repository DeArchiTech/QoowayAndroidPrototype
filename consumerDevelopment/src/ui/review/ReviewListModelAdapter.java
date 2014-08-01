package ui.review;

import java.util.ArrayList;
import java.util.List;
import framework.DataObject.Review;

public class ReviewListModelAdapter {

	public static ArrayList<ReviewDisplayListItem> Items;

	public static void LoadModel(List<Review> list) {

		int id = 0;
		Items = new ArrayList<ReviewDisplayListItem>();
		for (Review item : list) {
			String displayName = "Anonymous";
			if(item.Anonymous==0)
				displayName=item.NickName;
			Items.add(new ReviewDisplayListItem(++id, item.DateCreated , displayName,item.Remark,
					item.Item1Score, item.Item2Score , item.Item3Score, item.Score, item.MerchantName));
		}

	}

	public static ReviewDisplayListItem GetbyId(int id) {

		for (ReviewDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
}


