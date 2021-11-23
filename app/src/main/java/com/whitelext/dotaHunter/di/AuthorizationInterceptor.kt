package com.whitelext.dotaHunter.di

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer $token"
            ).build()

        return chain.proceed(request)
    }

    companion object {
        const val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJodHRwczov" +
                "L3N0ZWFtY29tbXVuaXR5LmNvbS9vcGVuaWQvaWQvNzY1NjExOTgwNzUyM" +
                "zgzMzMiLCJ1bmlxdWVfbmFtZSI6IkRpY2tiZWFyb2ZmIiwiU3ViamVjdCI6I" +
                "jQ1OTczMTRhLWIwMzEtNGI3OS1iYTE4LTIwZDIxYjg2MDUwNyIsIlN0ZWFtSWQ" +
                "iOiIxMTQ5NzI2MDUiLCJuYmYiOjE2Mzc2ODMxNDcsImV4cCI6MTY2OTIxOTE0Nywi" +
                "aWF0IjoxNjM3NjgzMTQ3LCJpc3MiOiJodHRwczovL2FwaS5zdHJhdHouY29tIn0.PhHh" +
                "A3zqorrUr7VN2gjN0yl5cQoA3B9uhpwh337CE74"
    }
}
