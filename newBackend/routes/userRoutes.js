const express = require('express')
const route = express.Router()
const bcrypt = require('bcryptjs')
const User = require('../models/user')
const jwt = require('jsonwebtoken')
const upload = require('../middleware/upload')
const { verifyUser } = require('../middleware/authentication')
const { Router } = require('express')



route.post('/insert/user',(req,res)=>{

const name = req.body.Name
const usernmae = req.body.Username
const password =req.body.Password
const phone = req.body.PhoneNumber



bcrypt.hash(password,10,(err,data)=>{
    const user = new User({
Name:name,
Username:usernmae,
Password:data,
PhoneNumber:phone

    })


    user.save().then((data)=>{

res.status(200).json({success:true,data:user})
console.log(user)

    }).catch()
})



})

route.post('/login',(req,res)=>{
const username = req.body['Username']
const password = req.body['Password']
console.log(req.body)
User.findOne({Username:username}).populate('Friends.user').then((data)=>{
if(!data){

res.status(200).json({success:false,msg:'no user found'})
}
else{   
console.log(data.Friends)
bcrypt.compare(password,data.Password).then((err,result)=>{
if(result===false){

    res.status(200).json({message:"Password did not matched",success:false})
}

User.findOneAndUpdate({_id:data._id},{
   isOnline:true
}).then((d)=>{
    const token = jwt.sign({id:data._id},'userId')
res.status(200).json({success:true,token:token,data:data})
})


})
}
}).catch((err)=>{
})
})

route.put('/update/profile/:id',upload.single('profile'),(req,res)=>{
    console.log("hello")
console.log(req.file)
User.findByIdAndUpdate({_id:req.params.id},{
    Profile:req.file.filename
}).then((data)=>{
    res.status(200).json({success:true,data:data})
})

})

route.put('/update/user',verifyUser,(req,res)=>{

    const name = req.body.Name
    const username = req.body.Username
    const phone = req.body.PhoneNumber
  
User.findByIdAndUpdate({_id:req.user._id},{
    Name:name,
    Username:username,
    PhoneNumber:phone
}).then((data)=>{
    res.status(200).json({success:true,message:"Done"})
})

})




route.get('/user',verifyUser,(req,res)=>{
res.status(200).json({success:true,data:req.user})
})
route.put('/logout',verifyUser,(req,res)=>{
User.findOneAndUpdate({_id:req.user._id},{
    isOnline:false
}).then((data)=>{
    res.status(200).json({success:true,message:"Logged Out"})
})
})



route.get('/showFriends',verifyUser,(req,res)=>{

User.findOne({_id:req.user._id}).populate('Friends.user').then((data)=>{

console.log(data)
res.status(200).json({success:true,data:data})

})



})

route.get('/all',(req,res)=>{
    console.log(
        "Alll"
    )
    User.find().then((data)=>{
        console.log(data)
     
        return res.status(200).json({success:true,data:data})
    })
})



module.exports = route