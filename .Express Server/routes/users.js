var express = require("express");
var router = express.Router();
const {PrismaClient} = require("@prisma/client");
const bcrypt = require("bcrypt");

const prisma = new PrismaClient();

/* GET users listing. */
router.get("/", function (req, res, next) {
  res.json({ res: "ok", purpose: "should respond with the list of users" });
});

router.post("/login", async (req, res, next) => {
  console.log("Data recieved on backend: ", req.body);
  //perform database stuff here
  let user = await prisma.users.findFirst({
    where: {
      OR: [
        {
          userName: req.body.username,
        },
        {
          userEmail: req.body.username,
        },
      ],
    },
  });

  if(!user){
    res.status(404).json({res: "ok", msg: "user doesn't exist" });
    return;
  }

  const match = await bcrypt.compare(req.body.password, user.userPassword);

  if (match){
    console.log("successful");
    res.status(200).json({ res: "ok", msg: "success", user: {name: user.userName, email: user.userEmail} });
    return;
  }
  else
    res.status(401).json({ res: "ok", msg: "unauthorized" });
    return;
});

module.exports = router;
