var express = require("express");
var router = express.Router();

/* GET users listing. */
router.get("/", function (req, res, next) {
  res.json({ res: "ok", purpose: "should respond with the list of users" });
});

router.post("/login", async (req, res, next) => {
  console.log("Data recieved on backend: ", req.body);
  //perform database stuff here
  res.status(200).json({ msg: "success" });
});

module.exports = router;
