const mongoose = require('mongoose')

const product = mongoose.model('Product',{
    User:{
        type:mongoose.Schema.Types.ObjectId,
        require:true,
        ref:"User"
    },
Name:{
    type:String,
    require:true
},
createdAt:{
    type:String,
    default:Date.now()
},
Category:{
    type:String,
    enum:['Electronics','AutoMobiles','Furniture','Musical','Real State']
},
Price:{
    type:String,
    default:"0"
},
Negotiable:{
    type:Boolean,
    default:false
},
SoldOut:{
    type:Boolean,
    default:false
},
UsedFor:{
    type:Number,
    default:0
},
Condition:{
    type:String,
    enum:["Brand New","Like New","Used"],
    defaule:"Used"
},
Description:{
    type:String
},
// Images:[{
//     name:{
//         type:String,
//     default:"no-image.jpg"
// }}],
Images:{
    type:String,
    default:"no-image.jpg"
},
Likes:[
    {

        user:{
            type:String,
            
        },
        

    }
],

Comments:[{

user:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
comment:{
    type:String
}

}]

})
module.exports =product