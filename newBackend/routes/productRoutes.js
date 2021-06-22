const express = require('express')
const route = express.Router()
const upload = require('../middleware/upload')
const {verifyUser} = require('../middleware/authentication')
const Product = require('../models/product')
const {check,validationResult} = require('express-validator') 
const { Router } = require('express')


route.post('/post/product',verifyUser,(req,res)=>{
const error = validationResult(req)
if(error.isEmpty()){
    const name = req.body.Name
    const category = req.body.Category
    const price = parseInt(req.body.Price)
    const description = req.body.Description
    const user = req.user._id
 const usedFor = parseInt(req.body.UsedFor)
 const negotiable = req.body.Negotiable
    const product = new Product({
 
 Name:name,
 Category:category,
 Price:price,
 Description:description,
 User:user,
 UsedFor:usedFor,
 Condition:req.body.Condition,
 Negotiable:negotiable
 
    })
    console.log(req.body.Negotiable)
    product.save().then((data)=>{
     
       return res.status(200).json({success:true,message:product._id})
 
    }).catch((error)=>{
 
        console.log(error)
 return res.status(400).json({success:false,error:error})
 
    })
}
else{
    return res.status(400).json({msg:"Validation error", success:false,error:error.array()})

}
  
   


})


route.put('/update/product/:id',(req,res)=>{

    const name = req.body.Name
    const category = req.body.Category
    const price = parseInt(req.body.Price)
    const description = req.body.Description

    
    const negotiable = req.body.Negotiable
    Product.findByIdAndUpdate({_id:req.params.id},{
        Name:name,
        Category:category,
        Price:price,
        Description:description,
     
        Condition:req.body.Condition,
        Negotiable:negotiable
    }).then((result)=>{
        return res.status(200).json({success:true,message:"Done"})
    })

   


})


route.put('/update/productImage/:id',upload.single('image'),(req,res)=>{
console.log(req.file)

Product.findByIdAndUpdate({_id:req.params.id},{
    Images:req.file.filename
}).then((data)=>{
    res.status(200).json({success:true,message:"Done"})
})
})

// route.put('/update/productImage/:id',upload.fields([{name:'image'}]),(req,res)=>{
// let newData =[]
//     Product.findOne({_id:req.params.id}).then((data)=>{
     
//         newData = data.Images
//     })

// req.files.image.forEach(data => {
//  newData.push({name:data.filename})
// })
//     Product.findByIdAndUpdate({_id:req.params.id},{

//        Images:newData        
//         }).then((data)=>{
//     return res.status(200).json({success:true,messahe:"Image Updated"})

// }).catch((er)=>{
//     console.log(er)
// return res.status(400).json({success:false,message:"Something went wrong"})
// })


// })

route.put('/like/:id',verifyUser,(req,res)=>{
console.log("asdasdasd")
var flag =false
    Product.findOne({_id:req.params.id}).then((data)=>{
data.Likes.forEach(e => {
    console.log(e,req.user._id)

    if(e.user===req.user._id.toString()){
       
        flag =true
      }
      else{
          flag=false
      }
});

if(flag===true)
{
    console.log("Id match vo")
    console.log(data.Likes[0].user)
    Product.findByIdAndUpdate({_id:req.params.id},{
        $pull:{Likes:{user:req.user._id}}
        
        }).then((data)=>{
            return res.status(200).json({success:false,message:"UnLiked"})
        })
  
}
else if(flag===false) {
    console.log("Id match vayena")

    Product.findByIdAndUpdate({_id:req.params.id},{
        
        $push:{Likes:{user:req.user._id}}
        
        }).then((data)=>{
            return res.status(200).json({success:true,message:"Liked"})
        })
}




    })

   
})

route.put('/comment',verifyUser,(req,res)=>{
const message= req.body.comment
console.log(message)
Product.findByIdAndUpdate({_id:req.body._id},{

    $push:{Comments:{user:req.user._id,comment:message}}
}).then((data)=>{
    return res.status(200).json({success:true,message:"One Comment Added"})
})


})
route.get('/get/product',(req,res)=>{
    Product.find().populate('User').populate('Comments.user').then((data)=>{
        
        return res.status(200).json({success:true,data:data})
    })
})

route.get('/person/post/:id',(req,res)=>{
    console.log("peroson post")
Product.find({User:req.params.id}).populate('User').populate('Comments.user').then((data)=>{

    return res.status(200).json({success:true,data:data})
}).catch((err)=>{
    res.status(400).json({success:false,message:"Something went wrong"})
})




})

route.delete('/delete/:id',(req,res)=>{
    console.log("delete")
    Product.findByIdAndDelete({_id:req.params.id}).then((re)=>{
        return res.status(200).json({success:true,message:"Deleted"})
    })
})
route.put('/sold/:id',(req,res)=>{
    Product.findOneAndUpdate({_id:req.params.id},{

SoldOut:true


    }).then((re)=>{
        return res.status(200).json({success:true,message:"Deleted"})
    })
})


route.get('/search/:name',(req,res)=>{

Product.find({Name:{$regex:req.params.name,$options:'$i'}}).then((data)=>{
    return res.status(200).json({success:true,data:data})
})

})
route.get('/category/:category',(req,res)=>{

    Product.find({Category:req.params.category}).then((data)=>{
        return res.status(200).json({success:true,data:data})
    })
    

})



module.exports = route