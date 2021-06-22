package com.xrest.hamrobazar.ResponseClass

data class Post(
       val _id:String?=null,
        val User:Users?=null,
       val Name:String?=null,
       val Category:String?=null,
       val Price:String?=null,
       val Negotiable:Boolean?=true,
       val SoldOut:Boolean?=null,
       val UsedFor:Int?=null,
       val Condition:String?=null,
       val Description:String?=null,
       val Images:String?=null,
       val Likes:MutableList<Likes>?=null,
       val Comments:MutableList<Comments>?=null





) {
}
data class Comments(
        val _id:String?=null,
        val user:Users?=null,
        val comment:String?=null
)
data class Likes(  val _id:String?=null,
                   val user:String?=null)