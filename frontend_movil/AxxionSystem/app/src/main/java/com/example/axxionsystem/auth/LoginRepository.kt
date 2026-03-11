package com.example.axxionsystem.auth

import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.common.api.TokenManager
import com.example.axxionsystem.auth.model.AuthResponse
import com.example.axxionsystem.auth.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    fun login(email: String, password: String, callback: (LoginResult) -> Unit) {
        RetrofitInstance.publicApi.login(LoginRequest(email, password))
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.accessToken
                        if (token != null) {
                            TokenManager.saveToken(token)
                            callback(LoginResult.Success)
                        } else {
                            callback(LoginResult.Error(LoginResult.ErrorType.INVALID_RESPONSE))
                        }
                    } else {
                        callback(LoginResult.Error(LoginResult.ErrorType.INVALID_CREDENTIALS))
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback(LoginResult.Error(LoginResult.ErrorType.NETWORK, t.message))
                }
            })
    }
}
