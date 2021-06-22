const express = require('express')
const jwt = require('jsonwebtoken')
const User = require('../models/user')



module.exports.verifyUser =(req,res,next)=>{

try{
const token = req.headers.authorization.split(" ")[1];
const decodedData = jwt.verify(token,'userId');
User.findOne({_id:decodedData.id}).then((data)=>{

req.user = data
next()

}).catch((err)=>{
res.status(400).json({success:false,msg:"auth failes"})
})



}
catch(err){

    res.status(400).json({success:false,msg:"auth failes1"})
}



}