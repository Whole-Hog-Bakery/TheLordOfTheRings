import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class QuoteResponse (

	@SerialName("docs")
	val quotes : List<Quote>,

	@SerialName("total")
	val total : Int,

	@SerialName("limit")
	val limit : Int,

	@SerialName("offset")
	val offset : Int,

	@SerialName("page")
	val page : Int,

	@SerialName("pages")
	val pages : Int
)

@Keep
@Serializable
data class Quote (

	@SerialName("_id")
	val quoteId : String,

	@SerialName("dialog")
	val dialog : String,

	@SerialName("movie")
	val movieId : String,

	@SerialName("character")
	val characterId : String,

	@SerialName("id")
	val duplicateQuoteId : String
)