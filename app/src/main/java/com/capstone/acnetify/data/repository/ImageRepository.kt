package com.capstone.acnetify.data.repository

import com.capstone.acnetify.data.model.ImageSubmissionsModel
import com.capstone.acnetify.data.model.ImagesByAcneTypeModel
import com.capstone.acnetify.data.remote.ApiService
import com.capstone.acnetify.data.remote.response.UploadAcneImageResponse
import com.capstone.acnetify.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class responsible for handling image-related operations.
 *
 * This repository interacts with the [ApiService] to perform operations such as uploading acne images
 * and fetching images based on acne type or user submissions.
 *
 * @property apiService The ApiService instance used for making network requests.
 */
class ImageRepository @Inject constructor(
    private val apiService: ApiService,
) {

    /**
     * Uploads an acne image to the server.
     *
     * This function creates a multipart request to upload the specified image file to the server.
     * It handles success and error cases, returning a [Result] object containing the response data
     * or an error message.
     *
     * @param imageFile The image file to upload.
     * @return A [Result] object containing the [UploadAcneImageResponse] or an error message.
     */
    suspend fun uploadAcneImage(imageFile: File): Result<UploadAcneImageResponse> {
        return try {
            // Creating request bodies for the image file.
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            // Creating a multipart request body with the image file.
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            // Making the API call to upload the image.
            val response = apiService.uploadAcneImage(multipartBody)
            Result.Success(response)
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }

    /**
     * Fetches all image submissions.
     *
     * This function makes an API call to fetch all images submitted by users. It handles success and
     * error cases, returning a [Result] object containing a list of [ImageSubmissionsModel] or an
     * error message.
     *
     * @return A [Result] object containing a list of [ImageSubmissionsModel] or an error message.
     */
    suspend fun getImagesSubmissions(): Result<List<ImageSubmissionsModel>> {
        return try {
            val response = apiService.getImagesSubmissions()
            Result.Success(response.data)
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }

    /**
     * Fetches images by acne type.
     *
     * This function makes an API call to fetch images filtered by the specified acne type. It handles
     * success and error cases, returning a [Result] object containing a list of [ImagesByAcneTypeModel]
     * or an error message.
     *
     * @param acneType The type of acne for which to fetch images.
     * @return A [Result] object containing a list of [ImagesByAcneTypeModel] or an error message.
     */
    suspend fun getImagesByAcneType(acneType: String): Result<List<ImagesByAcneTypeModel>> {
        return try {
            val response = apiService.getImagesByAcneType(acneType)
            Result.Success(response.data)
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }
}