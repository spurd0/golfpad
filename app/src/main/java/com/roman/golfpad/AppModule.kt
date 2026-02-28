package com.roman.golfpad

import android.content.Context
import com.roman.data.HoleRepository
import com.roman.domain.CalculateGIRUseCase
import com.roman.domain.DistanceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDistanceProvider(): DistanceProvider = AndroidDistanceProvider()

    @Provides
    @Singleton
    fun provideCalculateGIRUseCase(distanceProvider: DistanceProvider): CalculateGIRUseCase =
        CalculateGIRUseCase(distanceProvider)

    @Provides
    @Singleton
    fun provideHoleRepository(@ApplicationContext context: Context): HoleRepository =
        HoleRepository(context)
}