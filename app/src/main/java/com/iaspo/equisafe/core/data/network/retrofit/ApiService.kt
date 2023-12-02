package com.iaspo.equisafe.core.data.network.retrofit

import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.response.authresponse.LoginResponse
import com.iaspo.equisafe.core.data.network.response.authresponse.RegisterResponse
import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.response.accountresponse.AccountResponse
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticleResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.UserDataResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse.ChroniclesGameResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.MapResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle.RecommendationArticleResponse
import com.iaspo.equisafe.core.data.network.response.learningresponse.LearningResponse
import com.iaspo.equisafe.core.domain.model.PostData
import com.iaspo.equisafe.core.domain.model.RequestDelete
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("signup")
    suspend fun userRegister(
        @Body requestRegister: RegisterRequest
    ): Response<RegisterResponse>

    @GET("users/self")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): Response<UserDataResponse>

    @GET("users/{username}")
    suspend fun getAccountData(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<AccountResponse>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @PATCH("users")
    suspend fun saveAccountData(
        @Header("Authorization") token: String,
        @Body saveDataRequest: SaveDataRequest
    ): Response<CommonResponse>

    @PATCH("users/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<CommonResponse>

    @PATCH("users/emergency")
    suspend fun changeEmergency(
        @Header("Authorization") token: String,
        @Body changeEmergencyRequest:  ChangeEmergencyRequest
    ): Response<CommonResponse>

    @GET("videos")
    suspend fun getVideoLearning(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("search") search: String?,
    ): LearningResponse

    @PATCH("videos/{id_video}/recent")
    suspend fun saveLastSeenVideo(
        @Header("Authorization") token: String,
        @Path("id_video") idVideo: String
    ): Response<CommonResponse>

    @GET("articles")
    suspend fun getArticles(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): ArticleResponse

    @GET("articles/{id_article}")
    suspend fun getArticlesRecommendation(
        @Header("Authorization") token: String,
        @Path("id_article") idArticle : String,
    ): Response<RecommendationArticleResponse>

    @GET("maps")
    suspend fun getMaps(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<MapResponse>

    @POST("users/favorites")
    suspend fun addUserFavoritesVideo(
        @Header("Authorization") token: String,
        @Body postData: PostData
    ): Response<CommonResponse>

    @POST("users/favorites/delete")
    suspend fun deleteUserFavoritesVideo(
        @Header("Authorization") token: String,
        @Body requestDelete: RequestDelete
    ): Response<CommonResponse>

    @GET("games/jawaban/play")
    suspend fun getChroniclesGame(
        @Header("Authorization") token: String,
        @Query("number_question") numberQuestion: Int
    ): Response<ChroniclesGameResponse>
}