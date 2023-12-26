package com.example.i_prep.di

import com.example.i_prep.data.repository.PTestRepositoryImpl
import com.example.i_prep.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: PTestRepositoryImpl): Repository
}