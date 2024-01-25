package org.middle.earth.lotr.data.remote

import org.middle.earth.lotr.BuildConfig.THE_ONE_API_BASE_API_URL

object PingHttpRoute {
    const val GOOGLE_CONNECTIVITY_CHECK_URL = "https://connectivitycheck.gstatic.com/generate_204"
}

object LotrHttpRoute {
    const val CHARACTER = "${THE_ONE_API_BASE_API_URL}character"
    const val QUOTE = "${THE_ONE_API_BASE_API_URL}quote"
}