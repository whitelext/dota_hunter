package com.whitelext.dotaHunter.domain.repository

import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.api.GetMatchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepositoryImpl @Inject constructor(private val api: GetMatchApi) :
    MatchRepository {

    override suspend fun getMatch(matchId: Long): Resource<MatchStatsQuery.Match> =
        api.getMatch(matchId)
}
