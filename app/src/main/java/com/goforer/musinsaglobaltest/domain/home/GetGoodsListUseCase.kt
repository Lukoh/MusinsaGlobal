package com.goforer.musinsaglobaltest.domain.home

import com.goforer.musinsaglobaltest.data.repository.home.GetGoodsListRepository
import com.goforer.musinsaglobaltest.domain.RepoUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGoodsListUseCase
@Inject
constructor(override val repository: GetGoodsListRepository) : RepoUseCase(repository)