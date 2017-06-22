package com.palmap.exhibition.di.module;

import com.palmap.exhibition.repo.ActivityInfoRepo;
import com.palmap.exhibition.repo.impl.ActivityInfoRepoImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/10/19.
 */
@Module
public class RepoModule {

    @Provides
    ActivityInfoRepo providesActivityInfoRepo(ActivityInfoRepoImpl repo) {
        return repo;
    }

}
