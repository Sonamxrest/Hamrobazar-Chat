const { Router } = require('express')
const express = require('express')
const { verifyUser } = require('../middleware/authentication')
const upload= require('../middleware/upload')
const route =express.Router()
const Message = require('../models/message')



route.post('/startChatting/:id',verifyUser,(req,res)=>{
    const sender = req.user._id
    const reciever= req.params.id
    
    console.log(sender,reciever)
    Message.findOne({$or:[{'Sender':req.user._id,'Reciever':req.params.id},{'Sender':req.params.id,'Reciever':req.user.id}]}).then((data)=>{
if(data){
    console.log(data)
    return res.status(200).json({success:true,message:data._id})

}
else{
    const data = new Message({
        Sender:sender,
        Reciever:reciever,
        Messages:[]
        
        })
        data.save().then((d)=>{
            
            return res.status(200).json({success:true,message:data._id})
        }).catch((err)=>{
            return res.status(200).json({success:false,message:err})

        })
}
       
    })


    

})

route.put('/message/:id',verifyUser,upload.single('image'),(req,res)=>{
const sender = req.user._id
const format = req.body.format
if(format=="Message")
{
    console.log("message")
    Message.findOneAndUpdate({_id:req.params.id},{
        $push:{Messages:{user:sender,message:req.body.message,format:"Message"}}
       }).then((d)=>{
     
              
           return res.status(200).json({success:true,message:"messagae sent"})
    
       }).catch((err)=>{
           return res.status(200).json({success:false,message:err})
       })
}
else
{
    console.log("image")

    Message.findOneAndUpdate({_id:req.params.id},{
        $push:{Messages:{user:sender,message:req.file.filename,format:"Image"}}
       }).then((d)=>{
     
           console.log(req.file)
              
           return res.status(200).json({success:true,message:req.file.filename})
    
       }).catch((err)=>{
           return res.status(200).json({success:false,message:err})
       })
}

})




route.get('/messages/:id',(req,res)=>{

Message.findOne({_id:req.params.id}).populate('Sender').populate('Reciever').populate('Messages.user').then((data)=>{

  
    return res.status(200).json({success:true,data:data})
}).catch((err)=>{
    console.log(err)
    return res.status(200).json({success:false,data:"Something went Wrong "})
})


})

route.get('/getAllMessages',verifyUser,(req,res)=>{
    console.log("Get All Messages")
    Message.find({$or:[{'Sender':req.user._id},{'Reciever':req.user._id}]}).populate('Sender').populate('Reciever').populate('Messages.user').then((d)=>{
    console.log(d)
    return res.status(200).json({success:true,data:d})

}).catch((err)=>{
    return res.status(200).json({success:false,message:err})
})

})


module.exports = route