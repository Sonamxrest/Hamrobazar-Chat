const mongoose = require('mongoose')


const chat = mongoose.model('Chat',{


Sender:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
Reciever:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
Messages:[
    {
message:{
    type:String
},
user:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
format:{
    type:String
},

    }
]




})
module.exports = chat