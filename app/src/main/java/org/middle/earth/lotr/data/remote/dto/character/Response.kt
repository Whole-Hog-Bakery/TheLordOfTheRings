package org.middle.earth.lotr.data.remote.dto.character

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CharacterResponse (

    @SerialName("docs")
    val characters : List<Character>,

    @SerialName("total")
    val total : Int,

    @SerialName("limit")
    val limit : Int,

    @SerialName("offset")
    val offset : Int? = null,

    @SerialName("page")
    val page : Int,

    @SerialName("pages")
    val pages : Int
)

@Keep
@Serializable
data class Character (
    @SerialName("_id")
    val characterId : String,

    @SerialName("height")
    val height : String,

    @SerialName("race")
    val race : String,

    @SerialName("gender")
    val gender : String? = null,

    @SerialName("birth")
    val birth : String,

    @SerialName("spouse")
    val spouse : String,

    @SerialName("death")
    val death : String,

    @SerialName("realm")
    val realm : String,

    @SerialName("hair")
    val hair : String,

    @SerialName("name")
    val name : String,

    @SerialName("wikiUrl")
    val wikiUrl : String? = null
)