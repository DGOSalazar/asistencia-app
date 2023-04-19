package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.R
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.datasource.LoginDTO
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.datasource.mappers.LoginMapper
import com.example.myapplication.data.datasource.mappers.UserRegisterMapper
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.core.utils.statusNetwork.makeCall
import com.example.myapplication.data.mappers.AttendanceHistoryRegisterMapper
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.AttendanceHistoryModel
import com.example.myapplication.data.models.LocationModel
import com.example.myapplication.data.remote.request.AttendanceHistoryRegisterRequest
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


interface FirebaseTask {
    suspend fun doLogin(email: String, pass: String): ResponseStatus<Login>
    suspend fun doUserRegister(user: UserRegister): ResponseStatus<Boolean>
    suspend fun doAuthRegister(email: String, pass: String): ResponseStatus<Boolean>
    suspend fun doUploadImage(uri: Uri): ResponseStatus<Uri>
    suspend fun doGetOfficeLocation(): ResponseStatus<LocationModel>
    suspend fun doAttendanceHistoryRegister(request: AttendanceHistoryModel): ResponseStatus<Boolean>
    suspend fun doGetAttendanceDates(): ResponseStatus<ArrayList<DayCollectionResponse>>
    suspend fun doGetAttendanceHistory(): ResponseStatus<ArrayList<AttendanceHistoryModel>>
}

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: StorageReference,
    private val firebase: FirebaseClientModule
) :
    FirebaseTask {

    override suspend fun doLogin(email: String, pass: String): ResponseStatus<Login> {
        return withContext(Dispatchers.IO) {
            val getLoginFeared = async { getDoLogin(email, pass) }
            val getLoginResponse = getLoginFeared.await()

            if (getLoginResponse is ResponseStatus.Success) {
                val loginMapper = LoginMapper()
                val data = loginMapper.fromDtoToDomain(getLoginResponse.data)
                ResponseStatus.Success(data)
            } else {
                ResponseStatus.Error(R.string.user_not_exist)
            }
        }
    }

    override suspend fun doUserRegister(user: UserRegister): ResponseStatus<Boolean> {
        return withContext(Dispatchers.IO) {
            val getRegisterUser = async { sendRegisterUser(user) }
            val getRegisterUserResponse = getRegisterUser.await()

            if (getRegisterUserResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getRegisterUserResponse.data)
            } else
                ResponseStatus.Error(R.string.problem_registering_user)
        }
    }

    override suspend fun doAuthRegister(email: String, pass: String): ResponseStatus<Boolean> {
        return withContext(Dispatchers.IO) {
            val registerAuth = sendAuthRegister(email, pass)
            registerAuth
        }
    }

    override suspend fun doUploadImage(uri: Uri): ResponseStatus<Uri> {
        return withContext(Dispatchers.IO) {
            val uploadImage = async { sendUpLoadImage(uri) }
            val getImageResponse = uploadImage.await()

            if (getImageResponse is ResponseStatus.Success) {
                if (getImageResponse.data.uploadSessionUri != null)
                    ResponseStatus.Success(getImageResponse.data.uploadSessionUri!!)
                else
                    ResponseStatus.Error(R.string.upload_image_error)
            } else {
                ResponseStatus.Error(R.string.upload_image_error)
            }
        }
    }

    override suspend fun doGetOfficeLocation(): ResponseStatus<LocationModel> {
        return withContext(Dispatchers.IO) {
            val firebaseLocationRequest = async { getOfficeLocation() }
            val firebaseLocationResponse = firebaseLocationRequest.await()

            if (firebaseLocationResponse is ResponseStatus.Success) {
                ResponseStatus.Success(firebaseLocationResponse.data)
            } else {
                ResponseStatus.Error(R.string.error_get_location)
            }
        }
    }

    override suspend fun doAttendanceHistoryRegister(request: AttendanceHistoryModel): ResponseStatus<Boolean> {
        return withContext(Dispatchers.IO) {
            val firebaseLocationRequest = async { sendAttendanceHistoryRegister(request) }
            val firebaseLocationResponse = firebaseLocationRequest.await()
            if (firebaseLocationResponse is ResponseStatus.Success) {
                ResponseStatus.Success(true)
            } else {
                ResponseStatus.Error(R.string.error_get_location)
            }
        }
    }

    override suspend fun doGetAttendanceDates(): ResponseStatus<ArrayList<DayCollectionResponse>> {
        return withContext(Dispatchers.IO) {
            val firebaseLocationRequest = async { getAttendanceDates() }
            val firebaseLocationResponse = firebaseLocationRequest.await()
            if (firebaseLocationResponse is ResponseStatus.Success) {
                ResponseStatus.Success(firebaseLocationResponse.data)
            } else {
                ResponseStatus.Error(R.string.error_get_attendance_dates)
            }
        }
    }

    override suspend fun doGetAttendanceHistory(): ResponseStatus<ArrayList<AttendanceHistoryModel>> {
        return withContext(Dispatchers.IO) {
            val firebaseLocationRequest = async { getAttendanceHistory() }
            val firebaseLocationResponse = firebaseLocationRequest.await()
            if (firebaseLocationResponse is ResponseStatus.Success) {
                ResponseStatus.Success(firebaseLocationResponse.data)
            } else {
                ResponseStatus.Error(R.string.error_get_attendance_history)
            }
        }
    }

    private suspend fun getDoLogin(email: String, pass: String): ResponseStatus<LoginDTO> =
        makeCall {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val modelDTO =
                LoginDTO(
                    email = result.user?.email.toString(),
                    isEmailVerified = result.user?.isEmailVerified ?: false
                )
            modelDTO
        }

    private suspend fun sendAuthRegister(email: String, pass: String): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    isSuccess = it.isSuccessful
                }.await()
            isSuccess
        }

    private suspend fun sendRegisterUser(user: UserRegister): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
            firebase.userCollection
                .document(user.email)
                .set(UserRegisterMapper().fromDtoToDomain(user)
            ).addOnCompleteListener {
                isSuccess= it.isSuccessful
            }.await()
            isSuccess
        }

    private suspend fun sendUpLoadImage(uri: Uri): ResponseStatus<UploadTask.TaskSnapshot> =
        makeCall {
            val ref: StorageReference = firebaseStorage.child("image${uri.lastPathSegment}")
            val uploadTask = ref.putFile(uri).await()
            uploadTask
        }

    private suspend fun getOfficeLocation(): ResponseStatus<LocationModel> = makeCall {
        val firebaseLocation = LocationModel()
        Firebase.firestore.collection("office-location").get()
            .addOnSuccessListener {
                it.forEach { j ->
                    firebaseLocation.latitude = (j.get("latitude") as String).toDouble()
                    firebaseLocation.longitude = (j.get("longitude") as String).toDouble()
                }
            }.await()
        firebaseLocation
    }

    private suspend fun sendAttendanceHistoryRegister(request: AttendanceHistoryModel):ResponseStatus<Boolean> = makeCall {
        withContext(Dispatchers.IO){
            val response = async { firebase.attendanceHistory.document()
                .set(AttendanceHistoryRegisterMapper().map(request))}
            val snap = response.await()
            snap.isSuccessful
        }
    }

    private suspend fun getAttendanceDates(): ResponseStatus<ArrayList<DayCollectionResponse>> = makeCall {
        val list = arrayListOf<DayCollectionResponse>()
        firebase.dayCollection.get().addOnCompleteListener { it
           it.result.forEach{ document->
               list.add(document.toObject<DayCollectionResponse>())
           }
        }.await()
        list
    }

    private suspend fun getAttendanceHistory(): ResponseStatus<ArrayList<AttendanceHistoryModel>> = makeCall {
        val list = arrayListOf<AttendanceHistoryModel>()
        val documents = firebase.attendanceHistory.get().await().documents
        documents.forEach { d ->
            val netUser = d.toObject<AttendanceHistoryModel>()
            list.add(netUser!!)
        }
        list
    }

}