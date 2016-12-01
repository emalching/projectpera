var mongoose = require('mongoose');
var jwt = require('jsonwebtoken');

var userSchema = new mongoose.Schema({
  username: {
    type: String,
    unique: true,
    required: true
  },
  displayName: {
    type: String
  },
  cardNumber: {
    type: Number,
    unique: true,
    required: true
  },
  emailAddress: {
    type: String
  },
  userType: {
    type: String,
    "default": 'user',
    required: true
  }
});

userSchema.methods.generateJwt = function () {
  var expiry = new Date();
  expiry.setDate(expiry.getDate() + 7);
  
  return jwt.sign({
    _id: this._id,
    username: this.username,
    displayName: this.displayName,
    cardNumber: this.cardNumber,
    emailAddress: this.emailAddress,
    userType: this.userType,
    exp: parseInt(expiry.getTime() / 1000),
  }, process.env.JWT_SECRET);
};

mongoose.model('User', userSchema);
