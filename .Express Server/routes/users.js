var express = require("express");
var router = express.Router();
const {PrismaClient} = require("@prisma/client");
const bcrypt = require("bcrypt");

const prisma = new PrismaClient();

/* GET users listing. */
router.get("/", function (req, res, next) {
  res.json({ res: "ok", purpose: "should respond with the list of users" });
});

router.post("/signup", async(req, res, next)=>{

})

router.post("/login", async (req, res, next) => {

  const {username, password} = req.body;

  //server side validation
  if(!username || !password){
    res.status(403).json({ res: "ok", success: false, msg: "please fill in all the fields" });
    return;
  }

  //perform database stuff here
  let user = await prisma.users.findFirst({
    where: {
      OR: [
        {
          userName: username,
        },
        {
          userEmail: username,
        },
      ],
    },
  });

  if(!user){
    res.status(404).json({res: "ok", success: false, msg: "user doesn't exist" });
    return;
  }

  const match = await bcrypt.compare(password, user.userPassword);

  if (match){
    console.log("successful");
    res.status(200).json({ res: "ok", success: true, user: {name: user.userName, email: user.userEmail} });
    return;
  }
  else{
    res.status(401).json({ res: "ok", success: false, msg: "unauthorized" });
    return;
  }

});

module.exports = router;
