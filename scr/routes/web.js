const express = require('express')
const router = express.Router()

// middleware that is specific to this router
const timeLog = (req, res, next) => {
  console.log('Time: ', Date.now())
  next()
}
router.use(timeLog)

//define homepage router
router.get('/', (req, res) => {
    res.send('Helsslos Wosrldasascasckl!')
  })
router.get('/router',(reg,res)=>{
    res.render('sample.ejs')
  })
  module.exports = router