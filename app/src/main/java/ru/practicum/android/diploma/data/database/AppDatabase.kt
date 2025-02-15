package ru.practicum.android.diploma.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1)
abstract class AppDatabase: RoomDatabase() {
    // TODO: необходимо добавить в DI в DataModule инициализацию БД
}
