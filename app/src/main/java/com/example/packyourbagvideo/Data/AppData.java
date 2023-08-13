package com.example.packyourbagvideo.Data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.packyourbagvideo.Database.RoomDB;
import com.example.packyourbagvideo.Models.Items;
import com.example.packyourbagvideo.constants.MConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {
    RoomDB database;
    String category;
    Context context;

    public static final String LAST_VERSION="LAST_VERSION";
    public static final int NEW_VERSION=1;

    public AppData(RoomDB database)
    {
        this.database=database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData()
    {
        category="Basic Needs";
        List<Items> basicItem=new ArrayList<>();
        basicItem.add(new Items("Visa",category,false));
        basicItem.add(new Items("Passport",category,false));
        basicItem.add(new Items("Ticket",category,false));
        basicItem.add(new Items("Wallet",category,false));
        basicItem.add(new Items("Driving License",category,false));
        basicItem.add(new Items("Currency",category,false));
        basicItem.add(new Items("House Key",category,false));
        basicItem.add(new Items("Book",category,false));
        basicItem.add(new Items("Travel Pillow",category,false));
        basicItem.add(new Items("Eye Patch",category,false));
        basicItem.add(new Items("Umbrella",category,false));
        basicItem.add(new Items("Note Book",category,false));
        return  basicItem;

    }

    public List<Items> getPersonalCareData()
    {
        String[]data={"Tooth-brush","Tooth-paste","Floss","MouthWash"};
        return prepareItemList(MConstants.PERSONAL_CARE_CAMEL_CASE,data);
    }
    public List<Items> getClothingData()
    {
        String[]data={"Stoking","Underwear","Pajamas","T-Shirts","Casual","Dress"};
        return prepareItemList(MConstants.CLOTHING_CAMEL_CASE,data);
    }

    public List<Items> getBabyNeedData()
    {
        String[]data={"Snapsuit","Outfit","Jumpsuit","Baby Socks","Baby Hat","Baby Pajamas"};
        return prepareItemList(MConstants.BABY_NEEDS_CAMEL_CASE,data);
    }

    public List<Items> getHelthData()
    {
        String[]data={"Aspirin","Drug used","Vitamins used","lens solution","Condom","Hot water bag",
        "Tincture of iodine"};
        return prepareItemList(MConstants.HEALTH_CAMEL_CASE,data);
    }
    public List<Items> getTechnologyData()
    {
        String []data={"Mobile Phone","Phone cover","E-Book Reader","Camera","Camera Charger","Portable Speaker","Ipad"};
        return prepareItemList(MConstants.TECHNOLOGY_CAMEL_CASE,data);
    }
    public List<Items> getFoodData()
    {
        String[]data={"Snack","Sandwich","Juice","Tea Bags","Coffe","Water","Thermos"};
        return prepareItemList(MConstants.FOOD_CAMEL_CASE,data);
    }
    public List<Items> getBeachsuppliesData()
    {
        String[]data={"Sea glasses","Sea Bed","Suntan Cream","Beach Umbrella","Swim Fins","" +
                "Beach Bag"};
        return prepareItemList(MConstants.BEACH_SUPPLIES_CAMEL_CASE,data);
    }
    public List<Items> getCarSuppliesData()
    {
        String[]data={"Pump","Car Jack","Spare car key","Accident Record set","Auto refrigerator"};

        return prepareItemList(MConstants.CAR_SUPPLIES_CAMEL_CASE,data);
    }

    public List<Items> getNeedsData()
    {
        String[]data={"BackPack","Daily Bags","Laundry Bags","Sewing kit","Sport Equipment"};
        return prepareItemList(MConstants.NEEDS_CAMEL_CASE,data);
    }



    public List<Items> prepareItemList(String category,String[]data)
    {
        List<String> list= Arrays.asList(data);
        List<Items> datalist=new ArrayList<>();

        datalist.clear();

        for (int i=0;i<list.size();i++)
        {
            datalist.add(new Items(list.get(i),category,false));
        }
        return datalist;
    }

    public List<List<Items>> getAllData()
    {
        List<List<Items>> listOFAllItems=new ArrayList<>();
        listOFAllItems.clear();
        listOFAllItems.add(getBasicData());
        listOFAllItems.add(getClothingData());
        listOFAllItems.add(getPersonalCareData());
        listOFAllItems.add(getBabyNeedData());
        listOFAllItems.add(getHelthData());
        listOFAllItems.add(getTechnologyData());
        listOFAllItems.add(getFoodData());
        listOFAllItems.add(getBeachsuppliesData());
        listOFAllItems.add(getCarSuppliesData());
        listOFAllItems.add(getNeedsData());

        return listOFAllItems;
    }

    public void persistAllData()
    {
        List<List<Items>> listofAllItems=getAllData();
        for (List<Items>list:listofAllItems)
        {
            for (Items items:list)
            {
                database.mainDao().saveItem(items);
            }
        }
        System.out.println("Data added");
    }

    public void persistDataByCategory(String category,Boolean onlyDelete)
    {
        try {
          List<Items> list=deleteAndGetListByCategory(category,onlyDelete);
          if (onlyDelete)
          {

              for (Items items:list)
              {
                  database.mainDao().saveItem(items);
              }

          }
          else
          {
              Toast.makeText(this, category+" Reset Successfully.", Toast.LENGTH_SHORT).show();
          }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();

        }
    }
    private List<Items> deleteAndGetListByCategory(String category,Boolean onlyDelete)
    {
        if (onlyDelete)
        {
            database.mainDao().deleteAllByCategoryAndAddedBy(category,MConstants.SYSTEM_SMALL);
        }
        else
        {
            database.mainDao().deleteAllByCategory(category);

        }
        switch (category)
        {
            case MConstants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();
            case MConstants.CLOTHING_CAMEL_CASE:
                return getClothingData();
            case MConstants.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalCareData();
            case MConstants.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeedData();
            case MConstants.HEALTH_CAMEL_CASE:
                return getHelthData();
            case MConstants.TECHNOLOGY_CAMEL_CASE:
                return getTechnologyData();
            case MConstants.FOOD_CAMEL_CASE:
                return getFoodData();
            case MConstants.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachsuppliesData();
            case MConstants.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();
            case MConstants.NEEDS_CAMEL_CASE:
                return getNeedsData();
            default:
                return new ArrayList<>();

        }
    }
}
