package com.app.honey.data.remote.repo

import com.app.honey.data.remote.ApiNames
import com.app.honey.data.remote.ApiService
import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.request.*
import com.app.honey.data.remote.model.response.*
import com.app.honey.domain.repo.CacheRepo
import com.app.honey.domain.repo.ProductRepo
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(apiService: ApiService, val cacheRepo: CacheRepo) :
    ProductRepo,
    BaseRepo(apiService) {


}