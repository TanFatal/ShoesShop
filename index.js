require('dotenv').config()
const express = require('express')
const webRoutes= require('./scr/routes/web')
const configViewEngine= require('./scr/config/viewEngine')
//khai bao env
const app = express()
const port = process.env.PORT||8080;
const path = require('path')

configViewEngine(app); 
//file static
app.use('/',webRoutes);
//get router
// app.get('/', (req, res) => {
//   res.send('Helsslos Wosrldasascasckl!')
// })
// app.get('/router',(reg,res)=>{
//   res.render('sample.ejs')
// })
app.listen(port, () => {
  console.log(`Example app listening on port http://localhost:${port}`)
})