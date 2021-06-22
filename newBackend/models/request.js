const mongoose = require('mongoose')

const Request = mongoose.model('Request',{
From:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
To:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},

})
module.exports = Request
