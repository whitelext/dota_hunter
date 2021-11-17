package com.whitelext.dotaHunter.domain.repository

import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource

interface MatchRepository {
    suspend fun getMatch(matchId: Long): Resource<MatchStatsQuery.Match>
}
