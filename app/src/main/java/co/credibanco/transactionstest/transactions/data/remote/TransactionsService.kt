package co.credibanco.transactionstest.transactions.data.remote

import co.credibanco.transactionstest.transactions.data.remote.model.anullment.AnnulmentRequest
import co.credibanco.transactionstest.transactions.data.remote.model.anullment.AnnulmentResult
import co.credibanco.transactionstest.transactions.data.remote.model.authorization.AuthorizationRequest
import co.credibanco.transactionstest.transactions.data.remote.model.authorization.AuthorizationResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionsService {
    @POST("payments/authorization")
    suspend fun postAuthorization(@Body authorizationRequest: AuthorizationRequest): Response<AuthorizationResult>

    @POST("payments/annulment")
    suspend fun postAnnulment(@Body annulmentRequest: AnnulmentRequest): Response<AnnulmentResult>
}
