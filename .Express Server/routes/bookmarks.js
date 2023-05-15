var express = require("express");
var router = express.Router();
const {PrismaClient} = require("@prisma/client");

const prisma = new PrismaClient();

/* GET home page. */
router.get("/", async (req, res, next) => {
  const bookmarks = await prisma.bookmarks.findMany({});
  console.log("Data returned is: ", bookmarks);

  res.json({bookmarks});
});

router.post("/", async (req, res, next) => {

    //get user id
    //get recipe id
    
    //create bookmark
    // await prisma.bookmarks.create({
    //     data:{

    //     }
    // })
})

module.exports = router;
