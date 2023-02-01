package com.goforer.musinsaglobaltest.data

import javax.inject.Singleton

@Singleton
class Params constructor(val replyCount: Int, val query: Query)