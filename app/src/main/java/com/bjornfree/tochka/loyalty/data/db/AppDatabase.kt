package com.bjornfree.tochka.loyalty.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bjornfree.tochka.loyalty.converters.CategoryConverter
import com.bjornfree.tochka.loyalty.data.db.dao.CategoryDao
import com.bjornfree.tochka.loyalty.data.db.dao.EventDao
import com.bjornfree.tochka.loyalty.data.db.dao.CityDao
import com.bjornfree.tochka.loyalty.data.db.dao.ProductDao
import com.bjornfree.tochka.loyalty.model.*
import com.bjornfree.tochka.loyalty.converters.ShopConverter


@Database(
    entities = [City::class, EventsImagesItem::class, Product::class, Category::class],
    version = 18,
    exportSchema = false
)
@TypeConverters(ShopConverter::class, CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopsDao(): CityDao
    abstract fun eventDao(): EventDao
    abstract fun productsDao(): ProductDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "loyaltyTochka"
                    )
                        .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

}