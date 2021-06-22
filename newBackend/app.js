const express = require('express')
const bodyParser = require('body-parser')
const db = require('./db')
const route =require('./routes/userRoutes')
const productRoute = require('./routes/productRoutes')
const messageRoute = require('./routes/messageRoutes')
const reqR = require('./routes/requestRoutes')

const cors = require('cors')
const app = express()


const { verifyUser } = require('./middleware/authentication')
const User = require('./models/user')

app.use(bodyParser.urlencoded({extended:false}))
app.use(express.json())
app.use(cors())
app.use("/images",express.static(__dirname+"/images"))
app.use(route)
app.use(reqR)
app.use(productRoute)
app.use(messageRoute)

app.get('/',(req, res) => {
   
    res.sendFile(__dirname + '/index.html');
  });
const server = require('http').createServer(app);
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server:server });
  var client =0
  var newM={}
  wss.on('connection', function connection(ws) {
  
client++
console.log(client)
 
    ws.on('message', function incoming(message) {
       newM = JSON.parse(message)
       console.log(newM)
      
      wss.broadcast( JSON.stringify({id:newM.id,message:newM.message,user:newM.user,format:newM.format}))
newM={}
     
   
       });

       ws.on('close',(daat)=>{
        client--
        console.log(client)
         console.log("disconnected")
       })


    
  })

 
  wss.broadcast = function broadcast(msg) {
   
  wss.clients.forEach(function each(client) {
        client.send(msg);
       
     });
 };

  server.listen(3000,()=>{

    console.log('app is running on port 3000')
    
    });
    