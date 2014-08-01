package framework.Search;

import java.util.ArrayList;
import java.util.List;

import framework.DataObject.Merchant;
import framework.DataObject.Subcategory;
import framework.DataObject.WebCuisine;




public class CategoryModelAdapter {

	public static ArrayList<CategoryItem> Items;

	public static <T> void LoadModel(List<T> list) {

		Items = new ArrayList<CategoryItem>();
		int i = 0;
		for (T item : list) {
			if (item instanceof Subcategory) {
				Subcategory sItem = (Subcategory) item;
				Items.add(new CategoryItem(++i, sItem.SubcategoryDesc, item.getClass().getName(),
						sItem.CategoryCount));
			} else if (item instanceof WebCuisine) {

				WebCuisine sItem = (WebCuisine) item;
				Items.add(new CategoryItem(++i, sItem.WebCuisineDesc,item.getClass().getName(),
						sItem.WebCuisineCount));

			}
			else if (item instanceof Merchant) {

				Merchant sItem = (Merchant) item;
				Items.add(new CategoryItem(++i, sItem.Name,item.getClass().getName(),
						-1));

			}
		}

	}

	public static CategoryItem GetbyId(int id) {

		CategoryItem result = null;
		for (CategoryItem item : Items) {
			if (item.Id == id)
				result = item;
		}
		return result;
	}

}
