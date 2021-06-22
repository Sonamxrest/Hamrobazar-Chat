const express = require('express')
const { verifyUser } = require('../middleware/authentication')
const route =express.Router()
const Request = require('../models/request')
const User = require('../models/user')



route.post('/sendRequest/:id',verifyUser,(req,res)=>{
const data =new Request({From:req.user._id,To:req.params.id})
data.save().then((data)=>{res.status(200).json({success:true,message:"Request Sent"})})
})

route.put('/acceptRequest',verifyUser,(req,res)=>{
// ma aaile to ho
const id = req.body._id
const sender = req.body.From._id
const me = req.user._id

Request.findByIdAndDelete({_id:id}).then((Data)=>{
    
    User.findByIdAndUpdate({_id:sender},{


        $push:{Friends:{user:me}}
    }).then( (data)=>{User.findByIdAndUpdate({_id:me},{


        $push:{Friends:{user:sender}}
    }).then((data)=>{
       return res.status(200).json({success:true,message:"Request Accepted"})
    })

})
    
})
})

route.get('/showRequest',verifyUser,(req,res)=>{


Request.find({To:req.user._id}).populate('From').populate("To").then((data)=>{
   return res.status(200).json({success:true,data:data})
})



})

route.delete('/deleteRequest/:id',(req,res)=>{
    Request.findByIdAndDelete({_id:req.params.id}).then((data)=>{
        return res.status(200).json({success:true,message:"Done"})
    })

})


module.exports = route