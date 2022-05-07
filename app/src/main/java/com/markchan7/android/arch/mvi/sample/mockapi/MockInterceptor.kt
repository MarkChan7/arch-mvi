package com.markchan7.android.arch.mvi.sample.mockapi

import com.markchan7.android.arch.mvi.sample.BuildConfig
import okhttp3.*

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url().uri().toString()
            val responseString = when {
                uri.endsWith("mock") -> getMockApiResponse
                else -> ""
            }

            return Response.Builder()
                .request(chain.request())
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        MediaType.get("application/json"),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            // just to be on safe side.
            throw IllegalAccessError(
                """
                MockInterceptor is only meant for Testing Purposes and bound to be used only with DEBUG mode
                """.trimIndent()
            )
        }
    }
}

const val getMockApiResponse = """
{
  "articles": [
    {
      "title": "Title-1",
      "description": "Description-1",
      "imageUrl": "imageUrl"
    },
    {
      "title": "Title-2",
      "description": "Description-2",
      "imageUrl": "imageUrl"
    }
  ]
}"""
