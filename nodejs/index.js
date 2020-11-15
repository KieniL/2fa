
const express = require("express");
const bodyParser = require('body-parser');
const JsonDB = require('node-json-db').JsonDB;
const Config = require('node-json-db/dist/lib/JsonDBConfig').Config;
const uuid = require("uuid");
const speakeasy = require("speakeasy");
var QRCode = require('qrcode');

const app = express();
var path = require('path');

// The second argument is used to tell the DB to save after each push
// If you put false, you'll have to call the save() method.
// The third argument is to ask JsonDB to save the database in an human readable format. (default false)
// The last argument is the separator. By default it's slash (/)
var db = new JsonDB(new Config("myDataBase", true, false, '/'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// viewed at http://localhost:8080
app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname + '/index.html'));
});

app.get("/api", (req, res) => {
  res.json({ message: "Welcome to the two factor authentication exmaple" })
});


app.post("/api/register", (req, res) => {
  const id = uuid.v4();
  try {
    const path = `/user/${id}`;
    // Create temporary secret until it it verified
    const temp_secret = speakeasy.generateSecret();
    // Create user in the database
    db.push(path, { id, temp_secret });
    // Send user id and base32 key to user


    var auth_url = speakeasy.otpauthURL({ secret: temp_secret.base32, label: 'Name of Secret',
    algorithm: 'sha512', issuer:'nodejs', digits: 6, encoding: 'base32' });

    QRCode.toDataURL(auth_url, function (err, data_url) {
      res.json({ id, secret: temp_secret.base32, qrCode: data_url, msg: 'Please scan with your authenticator app' })
    })

  } catch (e) {
    console.log(e);
    res.status(500).json({ message: 'Error generating secret key' })
  }
})


//Use this to verify the mfa so that the tempsecret is stored permanently (only on registration)
app.post("/api/verify", (req, res) => {
  const { userId, token } = req.body;
  try {
    // Retrieve user from database
    const path = `/user/${userId}`;
    const user = db.getData(path);
    const { base32: secret } = user.temp_secret;
    const verified = speakeasy.totp.verify({
      secret,
      encoding: 'base32',
      token
    });
    if (verified) {
      // Update user data
      db.push(path, { id: userId, secret: user.temp_secret });
      res.json({ verified: true })
    } else {
      res.json({ verified: false })
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Error retrieving user' })
  };
})

//Use this to validate the mfa on login
app.post("/api/validate", (req, res) => {
  const { userId, token } = req.body;
  try {
    // Retrieve user from database
    const path = `/user/${userId}`;
    const user = db.getData(path);
    console.log({ user })
    const { base32: secret } = user.secret;
    // Returns true if the token matches
    const tokenValidates = speakeasy.totp.verify({
      secret,
      encoding: 'base32',
      token,
      window: 1
    });
    if (tokenValidates) {
      res.json({ validated: true })
    } else {
      res.json({ validated: false })
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Error retrieving user' })
  };
})

const port = 3000;

app.listen(port, () => {
  console.log(`App is running on PORT: ${port}.`);
});