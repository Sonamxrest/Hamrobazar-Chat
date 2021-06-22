package com.xrest.hamrobazar.ResponseClass

data class Message(
        val _id:String?=null,
    val Sender:Users?=null,
    val Reciever:Users?=null,
val Messages:MutableList<InnerMessage>?=null


)
data class InnerMessage(
        val val_id:String?=null,
        val message:String?=null,
    val user:Users?=null,
        val format:String?=null
)