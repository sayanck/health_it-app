package com.healthit.application.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.healthit.application.utils.constant.HelperConstant

class FirebaseHelper {
    var db = Firebase.firestore
    var usersRef = db.collection(HelperConstant.CollectionName.sUsers)

}