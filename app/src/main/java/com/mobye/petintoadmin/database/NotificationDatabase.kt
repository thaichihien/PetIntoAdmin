package com.mobye.petintoadmin.database

import com.mobye.petintoadmin.models.Notification
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

object NotificationDatabase {
    private const val REALM_NAME = "NOTIFICATION_DB"
    private val realm : Realm by lazy {
        Realm.open(
            RealmConfiguration.Builder(
                setOf(Notification::class)
            ).name(REALM_NAME).build()
        )
    }

    fun createNotification(notification: Notification){
        realm.writeBlocking {
            copyToRealm(notification, UpdatePolicy.ALL)
        }
    }

    fun remove(notification: Notification){
        realm.writeBlocking {
            val notificationDB =findLatest(realm.query<Notification>("id == $0", notification.id).find().first())
            if(notificationDB != null){
                delete(notificationDB)
            }
        }
    }

    fun removeAll(){
        realm.writeBlocking {
            val notiList: RealmResults<Notification> = this.query<Notification>().find()
            delete(notiList)
        }
    }

    fun getAllNotification() : List<Notification>
            = realm.copyFromRealm(realm.query<Notification>().find())
}