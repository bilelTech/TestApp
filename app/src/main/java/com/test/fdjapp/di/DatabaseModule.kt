package com.test.fdjapp.di

import android.content.Context
import androidx.room.Room
import com.test.fdjapp.data.local.AppDataBase
import com.test.fdjapp.data.local.LeagueDao
import com.test.fdjapp.data.local.TeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDataBase {
        return Room
            .databaseBuilder(context, AppDataBase::class.java, AppDataBase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }


    @Singleton
    @Provides
    fun provideLeagueDataDao(db: AppDataBase): LeagueDao {
        return db.leagueDao()
    }

    @Singleton
    @Provides
    fun provideTeamDataDao(db: AppDataBase): TeamDao {
        return db.teamDao()
    }
}