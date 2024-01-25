package org.middle.earth.lotr.data.remote

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

val mockEngineCharactersOk = MockEngine { _ ->
    httpResponseData(HttpStatusCode.OK, CHARACTER_RESPONSE)
}

val mockEngineNoContent = MockEngine { _ ->
    httpResponseData(HttpStatusCode.NoContent)
}

val mockEnginePermanentRedirect = MockEngine { _ ->
    httpResponseData(HttpStatusCode.MovedPermanently)
}

val mockEngineBadRequest = MockEngine { _ ->
    httpResponseData(HttpStatusCode.BadRequest)
}

val mockEngineServiceUnavailable = MockEngine { _ ->
    httpResponseData(HttpStatusCode.ServiceUnavailable)
}

private fun MockRequestHandleScope.httpResponseData(statusCode: HttpStatusCode) = respond(
    content = ByteReadChannel(""),
    status = statusCode,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
)


private fun MockRequestHandleScope.httpResponseData(statusCode: HttpStatusCode, dummyContent: String) = respond(
    content = ByteReadChannel(dummyContent),
    status = statusCode,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
)

const val CHARACTER_RESPONSE = """
    {
    "docs": [
        {
            "_id": "5cd99d4bde30eff6ebccfbbe",
            "height": "",
            "race": "Human",
            "gender": "Female",
            "birth": "",
            "spouse": "Belemir",
            "death": "",
            "realm": "",
            "hair": "",
            "name": "Adanel",
            "wikiUrl": "http://lotr.wikia.com//wiki/Adanel"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbbf",
            "height": "",
            "race": "Human",
            "gender": "Male",
            "birth": "Before ,TA 1944",
            "spouse": "",
            "death": "Late ,Third Age",
            "realm": "",
            "hair": "",
            "name": "Adrahil I",
            "wikiUrl": "http://lotr.wikia.com//wiki/Adrahil_I"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbc0",
            "height": "",
            "race": "Human",
            "gender": "Male",
            "birth": "TA 2917",
            "spouse": "Unnamed wife",
            "death": "TA 3010",
            "realm": "",
            "hair": "",
            "name": "Adrahil II",
            "wikiUrl": "http://lotr.wikia.com//wiki/Adrahil_II"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbc1",
            "height": "",
            "race": "Elf",
            "gender": "Male",
            "birth": "YT during the ,Noontide of Valinor",
            "spouse": "Loved ,Andreth but remained unmarried",
            "death": "FA 455",
            "realm": "",
            "hair": "Golden",
            "name": "Aegnor",
            "wikiUrl": "http://lotr.wikia.com//wiki/Aegnor"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbc2",
            "height": "",
            "race": "Human",
            "gender": "Female",
            "birth": "Mid ,First Age",
            "spouse": "Brodda",
            "death": "FA 495",
            "realm": "",
            "hair": "",
            "name": "Aerin",
            "wikiUrl": "http://lotr.wikia.com//wiki/Aerin"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbc3",
            "height": "",
            "race": "Human",
            "gender": "Female",
            "birth": "Between ,SA 700, and ,SA 750",
            "spouse": "Orchaldor",
            "death": "Early ,Second Age",
            "realm": "",
            "hair": "",
            "name": "Ailinel",
            "wikiUrl": "http://lotr.wikia.com//wiki/Ailinel"
        },
        {
            "_id": "5cd99d4bde30eff6ebccfbc4",
            "height": "",
            "race": "Human",
            "gender": "Male",
            "birth": "TA 2827",
            "spouse": "Unnamed wife",
            "death": "TA 2932",
            "realm": "",
            "hair": "",
            "name": "Aglahad",
            "wikiUrl": "http://lotr.wikia.com//wiki/Aglahad"
        }
    ],
    "total": 933,
    "limit": 7,
    "offset": 0,
    "page": 1,
    "pages": 134
}
"""